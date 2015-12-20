package com.pieter_jan.redditzor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Pieter-Jan on 18/12/2015.
 */
public class SubRedditFragment extends Fragment implements PostAdapter.PostSelectedListener, Callback<Listing>
{
    public static final String SUBREDDIT_NUMBER = "subreddit_number";
    public static final String LOAD_LIMIT = "load_limit";

    private RecyclerView mRecyclerView;
    private PostAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private List<Post> postList;
    private Listing listing;
    private String subreddit;

    private static int current_page = 1;
    private LoadLimiter loadLimiter;

    public interface LoadLimiter
    {
        int getLoadLimit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_subreddit, container, false);
        int i = getArguments().getInt(SUBREDDIT_NUMBER);
        subreddit = getResources().getStringArray(R.array.subreddits_array)[i];

        postList = new ArrayList<>();
        loadLimiter = (LoadLimiter) getActivity();
        loadData(current_page);

        mRecyclerView = ((RecyclerView) rootView.findViewById(R.id.post_list));
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new PostAdapter(getActivity(), postList);
        mAdapter.setListener(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager)
        {
            @Override
            public void onLoadMore(int current_page)
            {
                loadMoreData(listing.getAfter());
            }
        });
        //getActivity().setTitle(subreddit);

        return rootView;
    }

    private void loadData(int current_page)
    {
        // I have not used current page for showing demo, if u use a webservice
        // then it is useful for every call request
        /*for (int i = ival; i <= loadLimit; i++)
        {
            Post p = new Post(subreddit + i, subreddit + i + "-TEXT", "https://www.google.be", "http://i.imgur.com/5plPFPU.png");
            postList.add(p);
            ival++;
        }*/

        Gson gson = new GsonBuilder()
                .serializeNulls()
                .registerTypeAdapter(Listing.class, new JsonListingDeserializer())
                .create();
        String BASE_URL = "http://reddit.com";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        RedditAPI redditAPI = retrofit.create(RedditAPI.class);

        Call<Listing> call = redditAPI.loadPostsAfter(subreddit, null, Integer.toString(loadLimiter.getLoadLimit()));
        call.enqueue(this);
    }

    private void loadMoreData(String after)
    {
        // I have not used current page for showing demo, if u use a webservice
        // then it is useful for every call request
        /*loadLimit = ival + 10;
        for (int i = ival; i <= loadLimit; i++)
        {
            Post p = new Post(subreddit + i, subreddit + i + "-TEXT", "https://www.google.be", "http://i.imgur.com/5plPFPU.png", "");
            postList.add(p);
            ival++;
        }
        mAdapter.notifyDataSetChanged();*/
        Gson gson = new GsonBuilder()
                .serializeNulls()
                .registerTypeAdapter(Listing.class, new JsonListingDeserializer())
                .create();
        String BASE_URL = "http://reddit.com";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        RedditAPI redditAPI = retrofit.create(RedditAPI.class);

        Call<Listing> call = redditAPI.loadPostsAfter(subreddit, after, Integer.toString(loadLimiter.getLoadLimit()));
        call.enqueue(this);
    }

    @Override
    public void postSelected(Post post)
    {
        Intent intent = new Intent(getActivity(), PostDetailActivity.class);
        intent.putExtra(PostDetailActivity.POST, post);
        startActivity(intent);
    }

    @Override
    public void onResponse(Response<Listing> response, Retrofit retrofit)
    {
        listing = response.body();
        postList.addAll(response.body().getPosts());
        Log.e(subreddit, loadLimiter.getLoadLimit() + " posts have been loaded.");
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFailure(Throwable t)
    {
        t.printStackTrace();
    }
}
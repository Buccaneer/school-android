package com.pieter_jan.redditzor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.pieter_jan.redditzor.model.*;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
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

    @Bind(R.id.list_spinner_container)
    FrameLayout spinnerContainer;
    @Bind(R.id.list_spinner)
    ProgressBar spinner;
    @Bind(R.id.not_found)
    TextView notFound;

    @Bind(R.id.post_list)
    RecyclerView mRecyclerView;
    private PostAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private Listing expandingListing;
    private String subreddit;

    private Coordinator coordinator;

    public interface Coordinator
    {
        int getLoadLimit();
        void persist(Listing listing);
        Listing getListing(String subreddit);
        void goToPost(Post post);
    }

    public Listing getListing()
    {
        return expandingListing;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        coordinator = (Coordinator) getActivity();
        int index = getArguments().getInt(SUBREDDIT_NUMBER);
        initListing(index);
    }

    private void initListing(int index)
    {
        subreddit = getResources().getStringArray(R.array.subreddits_array)[index];
        expandingListing = coordinator.getListing(subreddit);
        if (expandingListing == null)
            expandingListing = new Listing(new ArrayList<Post>(), subreddit);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.post_list, container, false);
        ButterKnife.bind(this, rootView);

        if (expandingListing.getPosts().size() == 0)
            loadData();

        initAdapter(expandingListing, mRecyclerView);
        return rootView;
    }

    private void initAdapter(final Listing listing, RecyclerView recyclerView)
    {
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new PostAdapter(getActivity(), listing.getPosts());
        mAdapter.setListener(this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager)
        {
            @Override
            public void onLoadMore(int current_page)
            {
                loadMoreData(listing.getAfter());
            }
        });
    }

    private void loadData()
    {
        //LEGACY FOR TESTING PURPOSES
        /*for (int i = ival; i <= loadLimit; i++)
        {
            Post p = new Post(subreddit + i, subreddit + i + "-TEXT", "https://www.google.be", "http://i.imgur.com/5plPFPU.png");
            postList.add(p);
            ival++;
        }*/
        spinnerContainer.setVisibility(View.VISIBLE);
        spinner.setVisibility(View.VISIBLE);
        notFound.setVisibility(View.INVISIBLE);

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

        Call<Listing> call = redditAPI.loadPostsAfter(subreddit, null, Integer.toString(coordinator.getLoadLimit()));
        call.enqueue(this);
    }

    private void loadMoreData(String after)
    {
        //LEGACY FOR TESTING PURPOSES
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

        Call<Listing> call = redditAPI.loadPostsAfter(subreddit, after, Integer.toString(coordinator.getLoadLimit()));
        call.enqueue(this);
    }

    public void reload()
    {
        expandingListing = new Listing(new ArrayList<Post>(), subreddit);
        initAdapter(expandingListing, mRecyclerView);
        loadData();
    }

    public void goToSubreddit(int index)
    {
        initListing(index);
        initAdapter(expandingListing, mRecyclerView);
        if (expandingListing.getPosts().size() == 0)
            loadData();
    }

    @Override
    public void postSelected(Post post)
    {
        coordinator.goToPost(post);
    }

    @Override
    public void onResponse(Response<Listing> response, Retrofit retrofit)
    {
        Listing newListing = response.body();
        expandingListing.getPosts().addAll(response.body().getPosts());
        expandingListing.setAfter(newListing.getAfter());
        coordinator.persist(expandingListing);
        Log.e(subreddit, coordinator.getLoadLimit() + " posts have been loaded.");
        mAdapter.notifyDataSetChanged();
        spinnerContainer.setVisibility(View.GONE);
    }

    @Override
    public void onFailure(Throwable t)
    {
        spinner.setVisibility(View.INVISIBLE);
        notFound.setVisibility(View.VISIBLE);
        notFound.setText(R.string.not_found);
        t.printStackTrace();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                getActivity().onBackPressed();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
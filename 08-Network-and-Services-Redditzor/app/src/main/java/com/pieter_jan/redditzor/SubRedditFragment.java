package com.pieter_jan.redditzor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pieter-Jan on 18/12/2015.
 */
public class SubRedditFragment extends Fragment implements PostAdapter.PostSelectedListener
{
    public static final String ARG_SUBREDDIT_NUMBER = "subreddit_number";

    private RecyclerView mRecyclerView;
    private PostAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private List<Post> postList;
    private String subreddit;

    private static int current_page = 1;
    private int ival = 1;
    private int loadLimit = 10;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_subreddit, container, false);
        int i = getArguments().getInt(ARG_SUBREDDIT_NUMBER);
        subreddit = getResources().getStringArray(R.array.subreddits_array)[i];

        postList = new ArrayList<>();
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
                loadMoreData(current_page);
            }
        });
        //getActivity().setTitle(subreddit);
        return rootView;
    }

    private void loadData(int current_page)
    {
        // I have not used current page for showing demo, if u use a webservice
        // then it is useful for every call request
        for (int i = ival; i <= loadLimit; i++)
        {
            Post p = new Post(subreddit + i, subreddit + i + "-TEXT", "https://www.google.be", "http://i.imgur.com/5plPFPU.png");
            postList.add(p);
            ival++;
        }
    }

    private void loadMoreData(int current_page)
    {
        // I have not used current page for showing demo, if u use a webservice
        // then it is useful for every call request
        loadLimit = ival + 10;
        for (int i = ival; i <= loadLimit; i++)
        {
            Post p = new Post(subreddit + i, subreddit + i + "-TEXT", "https://www.google.be", "http://i.imgur.com/5plPFPU.png");

            postList.add(p);
            ival++;
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void postSelected(Post post)
    {
        Intent intent = new Intent(getActivity(), PostDetailActivity.class);
        intent.putExtra(PostDetailActivity.POST, post);
        startActivity(intent);
    }
}
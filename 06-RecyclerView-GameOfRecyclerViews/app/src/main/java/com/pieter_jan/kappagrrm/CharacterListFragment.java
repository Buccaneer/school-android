package com.pieter_jan.kappagrrm;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Pieter-Jan on 7/11/2015.
 */
public class CharacterListFragment extends Fragment
{

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.list, null);
        ButterKnife.bind(this, view);
        setUpRecyclerView();
        return view;
    }

    private void setUpRecyclerView()
    {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        MainAdapter adapter = new MainAdapter(getActivity());
        adapter.initTouchHelper().attachToRecyclerView(mRecyclerView);
        adapter.setListener((MainAdapter.CharacterSelectedListener) getActivity());
        mRecyclerView.setAdapter(adapter);
    }

}

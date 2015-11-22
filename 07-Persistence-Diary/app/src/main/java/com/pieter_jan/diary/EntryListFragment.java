package com.pieter_jan.diary;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by Pieter-Jan on 16/11/2015.
 */
public class EntryListFragment extends ListFragment
{
    private OnEntrySelectedListener listener;

    public interface OnEntrySelectedListener
    {
        void onEntrySelected(int entry);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //TODO: Replace empty String array
        setListAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, Mock.titles));
    }

    @Override
    public void onAttach(Context activity)
    {
        super.onAttach(activity);
        try
        {
            listener = (OnEntrySelectedListener) activity;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString()
                    + " must implement OnEntrySelectedListener");
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
        super.onListItemClick(l, v, position, id);
        listener.onEntrySelected(position);
    }

}

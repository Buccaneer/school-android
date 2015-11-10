package com.example.paradoxexercise;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by Pieter-Jan on 26/10/2015.
 */
public class ParadoxListFragment extends ListFragment
{

    OnParadoxSelectedListener listener;

    public interface OnParadoxSelectedListener
    {
        void onParadoxSelected(int paradox);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setListAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, Paradoxes.ParadoxNames));
    }

    @Override
    public void onAttach(Context activity)
    {
        super.onAttach(activity);
        try
        {
            listener = (OnParadoxSelectedListener) activity;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString()
                    + " must implement OnParadoxSelectedListener");
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
        super.onListItemClick(l, v, position, id);
        listener.onParadoxSelected(position);
    }
}

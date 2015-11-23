package com.pieter_jan.diary;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.pieter_jan.diary.persistence.MetaData;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Pieter-Jan on 16/11/2015.
 */
public class EntryListFragment extends ListFragment
{
    private OnEntrySelectedListener listener;

    public interface OnEntrySelectedListener
    {
        void onEntrySelected(String[] entry);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        updateUI();
    }

    @Override
    public void onResume()
    {
        super.onResume();
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
        CursorWrapper cursorWrapper = (CursorWrapper) getListView().getItemAtPosition(position);
        String t = cursorWrapper.getString(cursorWrapper.getColumnIndex(MetaData.EntryTable.TITLE));
        String c = cursorWrapper.getString(cursorWrapper.getColumnIndex(MetaData.EntryTable.CONTENT));
        long longDate = cursorWrapper.getLong(cursorWrapper.getColumnIndex(MetaData.EntryTable.DATE));
        String d = new SimpleDateFormat("HH:mm:ss dd,MM,yyyy").format(new Date(longDate));
        listener.onEntrySelected(new String[]{t, c, d});
    }

    public void updateUI() {
        Uri uri = MetaData.CONTENT_URI;
        Cursor cursor = getActivity().getContentResolver().query(uri,null,null,null,null);

        ListAdapter listAdapter = new SimpleCursorAdapter(
                getActivity(),
                R.layout.entry,
                cursor,
                new String[]{MetaData.EntryTable.TITLE, MetaData.EntryTable.DATE},
                new int[]{R.id.entry_title, R.id.date},
                0
        );
        setListAdapter(listAdapter);
    }

}

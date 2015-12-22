package com.pieter_jan.diary;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.pieter_jan.diary.persistence.MetaData;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Pieter-Jan on 16/11/2015.
 */
public class EntryListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>
{
    private OnEntrySelectedListener mListener;
    private SimpleCursorAdapter mAdapter;

    public interface OnEntrySelectedListener
    {
        void onEntrySelected(String[] entry);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        updateUI();
        getLoaderManager().initLoader(0, null, this);
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
            mListener = (OnEntrySelectedListener) activity;
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
        String d = new SimpleDateFormat("dd-MM-yyyy").format(new Date(longDate))
                + " - " + new SimpleDateFormat("HH:mm:ss").format(new Date(longDate));
        mListener.onEntrySelected(new String[]{t, c, d});
    }

    public void updateUI()
    {
        Uri uri = MetaData.CONTENT_URI;
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);

        mAdapter = new SimpleCursorAdapter(
                getActivity(),
                R.layout.entry,
                cursor,
                new String[]{MetaData.EntryTable.TITLE, MetaData.EntryTable.DATE},
                new int[]{R.id.entry_title, R.id.entry_date},
                0
        );

        mAdapter.setViewBinder(
                new SimpleCursorAdapter.ViewBinder()
                {
                    @Override
                    public boolean setViewValue(View view, Cursor cursor, int columnIndex)
                    {
                        if (columnIndex == cursor.getColumnIndex(MetaData.EntryTable.DATE))
                        {
                            long date = cursor.getLong(columnIndex);
                            TextView textView = (TextView) view;
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                            SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
                            StringBuilder dateBuilder = new StringBuilder();
                            Date d = new Date(date);
                            dateBuilder.append(dateFormat.format(d))
                                    .append(" - ")
                                    .append(hourFormat.format(d));
                            textView.setText(dateBuilder.toString());
                            return true;
                        }
                        return false;
                    }
                }
        );
        setListAdapter(mAdapter);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        Uri baseUri = MetaData.CONTENT_URI;
        return new CursorLoader(getActivity(), baseUri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
        mAdapter.swapCursor(null);
    }

}

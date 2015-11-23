package com.pieter_jan.diary;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Pieter-Jan on 17/11/2015.
 */
public class DisplayEntryFragment extends Fragment
{
    public static final String TITLE = "title";
    public static final String CONTENT = "content";
    public static final String DATE = "date";

    @Bind(R.id.title)
    TextView title;

    @Bind(R.id.date)
    TextView date;

    @Bind(R.id.content)
    TextView content;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.display_entry, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        Bundle args = getArguments();
        if (args != null)
        {
            update(args.getString(TITLE), args.getString(CONTENT), args.getString(DATE));
        }
    }

    private void update(String t, String c, String d)
    {
        //title.setText(Mock.entries.get(entry)[0]);
        //date.setText(Mock.entries.get(entry)[1]);
        //content.setText(Mock.entries.get(entry)[2]);
        title.setText(t);
        date.setText(d);
        content.setText(c);
    }
}

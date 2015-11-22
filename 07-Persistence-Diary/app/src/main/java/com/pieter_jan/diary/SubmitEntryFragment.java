package com.pieter_jan.diary;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Pieter-Jan on 16/11/2015.
 */
public class SubmitEntryFragment extends Fragment
{
    public static String PARCEL = "diaryParcel";

    private OnEntrySubmittedListener listener;

    @Bind(R.id.title)
    EditText title;

    @Bind(R.id.content)
    EditText content;

    @Bind(R.id.submit_button)
    Button button;

    public interface OnEntrySubmittedListener
    {
        void onEntrySubmitted();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.submit_entry, null);
        ButterKnife.bind(this, view);
        addListener(button);
        return view;
    }

    @Override
    public void onAttach(Context activity)
    {
        super.onAttach(activity);
        try
        {
            listener = (OnEntrySubmittedListener) activity;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString()
                    + " must implement OnEntrySubmittedListener");
        }
    }

    public void addListener(Button button)
    {
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String[] data = {title.getText().toString(), content.getText().toString(), new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())};
                Mock.entries.add(data);
                Mock.titles.add(title.getText().toString());
                listener.onEntrySubmitted();
            }
        });
    }
}

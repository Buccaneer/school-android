package com.example.paradoxexercise;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Pieter-Jan on 26/10/2015.
 */
public class ParadoxDescriptionFragment extends Fragment
{
    private int currentParadox = -1;

    @Bind(R.id.title)
    TextView title;

    @Bind(R.id.description)
    TextView description;


    @Override
    public void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_paradox_description, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Bundle args = getArguments();
        if (args != null) {
            update(args.getInt(Constants.PARADOX));
        } else if (currentParadox != -1) {
            update(currentParadox);
            // TODO
        }
    }

    private void update(int paradox)
    {
        title.setText(Paradoxes.ParadoxNames[paradox]);
        description.setText(Paradoxes.ParadoxDescription[paradox]);
    }

}

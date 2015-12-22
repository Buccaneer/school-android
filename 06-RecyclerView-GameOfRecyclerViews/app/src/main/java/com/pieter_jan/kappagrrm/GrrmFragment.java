package com.pieter_jan.kappagrrm;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Pieter-Jan on 22/12/2015.
 */
public class GrrmFragment extends Fragment
{
    @Bind(R.id.image1)
    ImageView image1;

    @Bind(R.id.image2)
    ImageView image2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.grrm, null);
        ButterKnife.bind(this, view);
        Glide.with(image1.getContext())
                .load(R.drawable.grrm0)
                .fitCenter()
                .into(image1);
        Glide.with(image2.getContext())
                .load(R.drawable.grrm1)
                .fitCenter()
                .into(image2);
        return view;
    }
}

package com.pieter_jan.kappagrrm;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Pieter-Jan on 7/11/2015.
 */
public class CharacterDetailsFragment extends Fragment
{
    public static String PARCEL = "characterParcel";

    @Bind(R.id.scrollView)
    ScrollView scrollView;

    @Bind(R.id.image)
    ImageView image;

    @Bind(R.id.name)
    TextView name;

    @Bind(R.id.description)
    TextView description;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.details, null);
        ButterKnife.bind(this, view);
        Character c = getArguments().getParcelable(PARCEL);
        Glide.with(image.getContext())
                .load(c.getImageId())
                .listener(createListener())
                .into(image);
        name.setText(c.getName());
        description.setText(c.getDescription());
        return view;
    }

    private RequestListener<Integer, GlideDrawable> createListener()
    {
        return new RequestListener<Integer, GlideDrawable>()
        {
            @Override
            public boolean onException(Exception e, Integer model, Target<GlideDrawable> target, boolean isFirstResource)
            {
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, Integer model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource)
            {
                scrollView.postDelayed(new Runnable()
                {

                    @Override
                    public void run()
                    {
                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                }, 100);
                return false;
            }
        };
    }

}

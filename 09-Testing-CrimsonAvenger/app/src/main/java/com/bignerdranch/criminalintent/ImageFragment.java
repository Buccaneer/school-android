package com.bignerdranch.criminalintent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;

/**
 * Created by Pieter-Jan on 22/12/2015.
 */
public class ImageFragment extends DialogFragment
{
    public static final String EXTRA_IMAGE_PATH = "path";

    public static ImageFragment createInstance(String imagePath) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_IMAGE_PATH, imagePath);

        ImageFragment fragment = new ImageFragment();
        fragment.setArguments(args);
        fragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);

        return fragment;
    }

    private ImageView mImageView;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup parent, Bundle savedInstanceState) {
        mImageView = new ImageView(getActivity());
        mImageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        mImageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ImageFragment.this.dismiss();
            }
        });
        String path = (String)getArguments().getSerializable(EXTRA_IMAGE_PATH);

        Glide.with(mImageView.getContext())
                .load(path)
                .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                .into(mImageView);

        return mImageView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
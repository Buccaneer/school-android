package com.pieter_jan.redditzor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pieter_jan.redditzor.model.Post;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Pieter-Jan on 19/12/2015.
 */
public class PostDetailFragment extends Fragment
{
    public static String POST = "post";
    private Post post;

    @Bind(R.id.title)
    TextView mTitleTextView;

    @Bind(R.id.description)
    TextView mDescriptionTextView;

    @Bind(R.id.image)
    ImageView mImageView;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        post = getArguments().getParcelable(POST);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.post_detail, container, false);
        ButterKnife.bind(this, rootView);
        showPost();
        return rootView;
    }

    private void showPost()
    {
        mTitleTextView.setText(hyperlink(post.getTitle(), post.getUrl()));
        mTitleTextView.setMovementMethod(LinkMovementMethod.getInstance());
        if (post.getText() != null) mDescriptionTextView.setText(post.getText());
        if (post.getImage() != null)
            Glide.with(mImageView.getContext())
                .load(post.getImage())
                .into(mImageView);
    }

    private Spanned hyperlink(String text, String url)
    {
        return Html.fromHtml("<a href='" + url + "'>" + text + "</a>");
    }

}

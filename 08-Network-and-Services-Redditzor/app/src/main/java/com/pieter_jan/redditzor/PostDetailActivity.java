package com.pieter_jan.redditzor;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Pieter-Jan on 19/12/2015.
 */
public class PostDetailActivity extends AppCompatActivity
{
    public static String POST = "post";
    private Post post;

    @Bind(R.id.title)
    TextView mTitleTextView;

    @Bind(R.id.description)
    TextView mDescriptionTextView;

    @Bind(R.id.image)
    ImageView mImageView;

    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init()
    {
        setContentView(R.layout.post_detail);
        ButterKnife.bind(this);
        actionBar = getSupportActionBar();
        post = getIntent().getParcelableExtra(POST);
        showPost();
    }

    private void enableNavigateUp()
    {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void showPost()
    {
        actionBar.setTitle(post.getTitle());
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

    @Override
    public boolean onSupportNavigateUp()
    {
        finish();
        return true;
    }

}

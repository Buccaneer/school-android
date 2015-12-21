package com.pieter_jan.redditzor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pieter_jan.redditzor.model.Post;

import java.util.List;

/**
 * Created by Pieter-Jan on 18/12/2015.
 */
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder>
{
    Context mContext;
    List<Post> mPosts;
    PostSelectedListener mListener;

    public PostAdapter(Context context, List<Post> posts)
    {
        mContext = context;
        mPosts = posts;
    }

    public interface PostSelectedListener
    {
        void postSelected(Post post);
    }

    public void setListener(PostSelectedListener mListener)
    {
        this.mListener = mListener;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup viewGroup, int position)
    {
        View view = LayoutInflater.from(mContext).inflate(R.layout.post_list_item, viewGroup, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, final int position)
    {
        Post p = mPosts.get(position);
        holder.mView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mListener.postSelected(mPosts.get(position));
            }
        });
        holder.setAvatar(p.getThumbnail());
        holder.setTitle(p.getTitle());
    }

    @Override
    public int getItemCount()
    {
        return mPosts.size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder
    {
        private View mView;
        private ImageView mImageView;
        private TextView mTextView;

        public PostViewHolder(View itemView)
        {
            super(itemView);
            mView = itemView;
            mImageView = (ImageView) itemView.findViewById(R.id.avatar);
            mTextView = (TextView) itemView.findViewById(R.id.title);
        }

        public void setAvatar(String imgUrl)
        {
            Glide.with(mImageView.getContext())
                    .load(imgUrl)
                    .placeholder(R.drawable.reddit)
                    //.load(R.drawable.reddit)
                    .into(mImageView);
        }

        public void setTitle(String title)
        {
            mTextView.setText(title);
        }
    }

}


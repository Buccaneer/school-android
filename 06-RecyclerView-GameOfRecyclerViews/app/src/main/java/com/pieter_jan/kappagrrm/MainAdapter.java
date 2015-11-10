package com.pieter_jan.kappagrrm;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Pieter-Jan on 7/11/2015.
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder>
{
    Context mContext;
    List<Character> mCharacters;
    CharacterSelectedListener mListener;

    public MainAdapter(Context context, List<Character> characters)
    {
        mContext = context;
        mCharacters = characters;
    }

    public interface CharacterSelectedListener
    {
        void characterSelected(Character character);
    }

    public void setListener(CharacterSelectedListener mListener)
    {
        this.mListener = mListener;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup viewGroup, int position)
    {
        View view = LayoutInflater.from(mContext).inflate(R.layout.card, viewGroup, false);
        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, final int position)
    {
        Character c = mCharacters.get(position);
        holder.imageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mListener.characterSelected(mCharacters.get(position));
            }
        });
        holder.setImage(c.getImageId());
        holder.setName(c.getName());
    }

    @Override
    public int getItemCount()
    {
        return mCharacters.size();
    }

    class MainViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView imageView;
        private TextView textView;

        public MainViewHolder(View itemView)
        {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.card_image);
            textView = (TextView) itemView.findViewById(R.id.card_name);
        }

        public void setImage(int imgId)
        {
            Glide.with(imageView.getContext())
                    .load(imgId)
                    .into(imageView);
        }

        public void setName(String name)
        {
            textView.setText(name);
        }
    }

}

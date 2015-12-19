package com.pieter_jan.redditzor;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Pieter-Jan on 18/12/2015.
 */
public class Post implements Parcelable
{
    private String title;
    private String text;
    private String url;
    private String thumbnail;

    public Post(String title, String text, String url, String thumbnail)
    {
        setTitle(title);
        setText(text);
        setUrl(url);
        setThumbnail(thumbnail);
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getThumbnail()
    {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail)
    {
        this.thumbnail = thumbnail;
    }

    public static final Parcelable.Creator<Post> CREATOR = new Parcelable.Creator<Post>() {
        public Post createFromParcel(Parcel in) {
            return new Post(in.readString(), in.readString(), in.readString(), in.readString());
        }

        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(title);
        dest.writeString(text);
        dest.writeString(url);
        dest.writeString(thumbnail);
    }
}

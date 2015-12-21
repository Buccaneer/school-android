package com.pieter_jan.redditzor.legacy;

import java.util.List;

/**
 * Created by Pieter-Jan on 19/12/2015.
 */
public class Listing
{
    private List<Post> posts;
    private String before;
    private String after;

    public Listing(List<Post> posts, String before, String after)
    {
        this.posts = posts;
        this.before = before;
        this.after = after;
    }

    public List<Post> getPosts()
    {
        return posts;
    }

    public String getBefore()
    {
        return before;
    }

    public String getAfter()
    {
        return after;
    }

    public void setAfter(String after)
    {
        this.after = after;
    }

    public int getCount() {
        return posts != null ? posts.size() : 0;
    }
}

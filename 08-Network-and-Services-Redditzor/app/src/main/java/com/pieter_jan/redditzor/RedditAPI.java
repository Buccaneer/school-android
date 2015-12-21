package com.pieter_jan.redditzor;

import com.pieter_jan.redditzor.model.Listing;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Pieter-Jan on 19/12/2015.
 */
public interface RedditAPI
{
    @GET("/r/{subreddit}.json")
    Call<Listing> loadPostsAfter(@Path("subreddit") String subreddit, @Query("after") String after, @Query("limit") String limit);

    @GET("/r/{subreddit}.json")
    Call<Listing> loadPostsBefore(@Path("subreddit") String subreddit, @Query("before") String before, @Query("limit") String limit);
}

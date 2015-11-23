package com.pieter_jan.diary;

/**
 * Created by Pieter-Jan on 16/11/2015.
 */
public class DiaryEntry
{
    private int id;
    private String title;
    private String content;
    private long date;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public long getDate()
    {
        return date;
    }

    public void setDate(long date)
    {
        this.date = date;
    }
}

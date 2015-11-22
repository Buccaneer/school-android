package com.pieter_jan.diary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pieter-Jan on 22/11/2015.
 */
public class Mock
{
    public static List<String[]> entries = new ArrayList<>();
    static
    {
        entries.add(new String[]{ "Title1", "Date1", "Content1" });
        entries.add(new String[]{ "Title2", "Date2", "Content2" });
        entries.add(new String[]{ "Title3", "Date3", "Content3" });
        entries.add(new String[]{ "Title4", "Date4", "Content4" });
        entries.add(new String[]{ "Title5", "Date5", "Content5" });
    }
    public static List<String> titles = new ArrayList<>();
    static
    {
        titles.add("Title1");
        titles.add("Title2");
        titles.add("Title3");
        titles.add("Title4");
        titles.add("Title5");
    }
}

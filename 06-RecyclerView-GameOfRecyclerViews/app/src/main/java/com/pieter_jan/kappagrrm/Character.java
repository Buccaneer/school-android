package com.pieter_jan.kappagrrm;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Pieter-Jan on 7/11/2015.
 */
public class Character implements Parcelable
{
    private int imageId;
    private String name;
    private String description;

    public Character(int imageId, String name, String description)
    {
        this.imageId = imageId;
        this.name = name;
        this.description = description;
    }

    public int getImageId()
    {
        return imageId;
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    public static final Parcelable.Creator<Character> CREATOR = new Parcelable.Creator<Character>() {
        public Character createFromParcel(Parcel in) {
            return new Character(in.readInt(), in.readString(), in.readString());
        }

        public Character[] newArray(int size) {
            return new Character[size];
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
        dest.writeInt(imageId);
        dest.writeString(name);
        dest.writeString(description);
    }
}
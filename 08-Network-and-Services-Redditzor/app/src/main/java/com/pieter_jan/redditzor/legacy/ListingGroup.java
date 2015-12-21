package com.pieter_jan.redditzor.legacy;

/**
 * Created by Pieter-Jan on 21/12/2015.
 *
 * NOT USED
 */
public class ListingGroup
{
    private Listing[] listings = new Listing[3];

    private int count = 0;

    public ListingGroup(Listing before, Listing current, Listing after, int count)
    {
        listings[0] = before;
        listings[1] = current;
        listings[2] = after;
        this.count = count;
    }

    public int getCount()
    {
        int counter = count;
        for (Listing listing : listings)
            if (listing != null)
                counter += listing.getCount();
        return counter;
    }

    public String getBefore()
    {
        if (listings[0] != null)
            return listings[0].getBefore();
        else
            return listings[1].getBefore();
    }

    public String getAfter()
    {
        if (listings[2] != null)
            return listings[2].getBefore();
        else
            return listings[1].getBefore();
    }

    public ListingGroup next(Listing listing)
    {
        /*count += listings[0]
        listings[0] = listings[1];
        listings[1] = listings[2];
        listings[3] =*/
        return null;
    }

    public ListingGroup previous(Listing listing)
    {
        return null;
    }

}

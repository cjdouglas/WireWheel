package com.wirewheel.listings;

import android.content.Context;

import com.wirewheel.wirewheel.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chris on 9/15/2016.
 */
public class ListingDatabase {

    private static ListingDatabase sListingDatabase;

    private List<Listing> mListings;

    public static ListingDatabase get(Context context) {
        if (sListingDatabase == null) {
            sListingDatabase = new ListingDatabase(context);
        }
        return sListingDatabase;
    }

    private ListingDatabase(Context context) {
        mListings = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Listing listing = new Listing();
            listing.setTitle("2007 Lotus Exige # " + i + " For Sale");
            listing.setPrice("$" + i * 10000);
            listing.setMileage("" + i * 1234 + "\nmiles");
            listing.setPhotoResource(R.drawable.testphoto);

            mListings.add(listing);
        }
    }

    public List<Listing> getListings() {
        return mListings;
    }
}

package com.wirewheel.listings;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.wirewheel.database.ListingBaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chris on 9/15/2016.
 */
public class ListingDatabase {

    private static ListingDatabase sListingDatabase;

    private List<Listing> mListings;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static ListingDatabase get(Context context) {
        if (sListingDatabase == null) {
            sListingDatabase = new ListingDatabase(context);
        }
        return sListingDatabase;
    }

    private ListingDatabase(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new ListingBaseHelper(mContext).getWritableDatabase();

        mListings = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Listing listing = new Listing();
            listing.setTitle("2007 Lotus Exige # " + i + " For Sale");
            listing.setPrice("$" + i * 10000);
            listing.setMileage("" + i * 1234 + "\nmiles");
            // listing.setPhotoResource(R.drawable.testphoto);
            listing.setKeyImageLink("http://www.wirewheel.com/gallery/177078/2005_Lotus_Elise_Graphite.jpg");

            mListings.add(listing);
        }
    }

    public List<Listing> getListings() {
        return mListings;
    }
}

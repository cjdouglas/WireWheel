package com.wirewheel.listings;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wirewheel.database.ListingBaseHelper;
import com.wirewheel.database.ListingCursorWrapper;
import com.wirewheel.parsing.WebScraper;

import java.util.ArrayList;
import java.util.List;

import static com.wirewheel.database.ListingDbSchema.ListingTable;

/**
 * Created by Chris on 9/15/2016.
 */
public class ListingDatabase {

    private static ListingDatabase sListingDatabase;

    // private List<Listing> mListings;
    private Context mContext;
    private SQLiteDatabase mDatabase;
    private WebScraper mWebScraper;

    public static ListingDatabase get(Context context) {
        if (sListingDatabase == null) {
            sListingDatabase = new ListingDatabase(context);
        }
        return sListingDatabase;
    }

    private ListingDatabase(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new ListingBaseHelper(mContext).getWritableDatabase();
        mWebScraper = new WebScraper(mContext);

        refresh("http://www.wirewheel.com/LOTUS.html");

        /*
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
        */
    }

    private static ContentValues getContentValues(Listing listing) {
        ContentValues values = new ContentValues();
        values.put(ListingTable.Cols.LINK, listing.getLink());
        values.put(ListingTable.Cols.TITLE, listing.getTitle());
        values.put(ListingTable.Cols.PRICE, listing.getPrice());
        values.put(ListingTable.Cols.MILEAGE, listing.getMileage());
        // values.put(ListingTable.Cols.TEXT, listing.getText());
        values.put(ListingTable.Cols.KEY_IMG_LINK, listing.getKeyImageLink());

        return values;
    }

    private ListingCursorWrapper queryListings(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                ListingTable.NAME,
                null, // Select all columns
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new ListingCursorWrapper(cursor);
    }

    /**
     * This method is called when the user pulls to refresh on an AdListFragment
     * @param url The page of links we want to refresh from
     */
    public void refresh(String url) {
        new Thread() {
            @Override
            public void run() {
                mWebScraper.openService();
                mWebScraper.databaseTest();
                mWebScraper.closeService();
            }
        }.start();
    }

    public void addListing(Listing listing) {
        ContentValues values = getContentValues(listing);

        mDatabase.insertWithOnConflict(ListingTable.NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void update(Listing listing) {
        String link = listing.getLink();
        ContentValues values = getContentValues(listing);

        mDatabase.update(ListingTable.NAME, values, ListingTable.Cols.LINK + "= ?",
                new String[] {link} );
    }

    public Listing getListing(String link) {
        ListingCursorWrapper cursor = queryListings(
                ListingTable.Cols.LINK + " = ?",
                new String[] { link });

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getListing();
        } finally {
            cursor.close();
        }
    }

    public List<Listing> getListings() {
        List<Listing> listings = new ArrayList<>();

        ListingCursorWrapper cursor = queryListings(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                listings.add(cursor.getListing());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return listings;
    }
}

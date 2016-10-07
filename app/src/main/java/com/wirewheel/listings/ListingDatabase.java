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

    private ListingCursorWrapper queryListings(String tableName, String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                tableName,
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
    public void refreshPage(String url, String tableId) {
        mWebScraper.openService();
        mWebScraper.loadPage(url, tableId);
        mWebScraper.closeService();
    }

    public synchronized void addListing(Listing listing, String tableId) {
        ContentValues values = getContentValues(listing);

        mDatabase.insertWithOnConflict(tableId, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public Listing getListing(String link, String id) {
        ListingCursorWrapper cursor = queryListings(
                id,
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


    public List<Listing> getListingsFrom(String table) {

        String tableName;

        switch (table) {
            case ListingTable.NAME_RACECARS:
                tableName = ListingTable.NAME_RACECARS;
                break;
            case ListingTable.NAME_AUSTIN_HEALEY:
                tableName = ListingTable.NAME_AUSTIN_HEALEY;
                break;
            case ListingTable.NAME_JAGUAR:
                tableName = ListingTable.NAME_JAGUAR;
                break;
            case ListingTable.NAME_LOTUS:
                tableName = ListingTable.NAME_LOTUS;
                break;
            case ListingTable.NAME_MARCOS:
                tableName = ListingTable.NAME_MARCOS;
                break;
            case ListingTable.NAME_MINI:
                tableName = ListingTable.NAME_MINI;
                break;
            case ListingTable.NAME_MG:
                tableName = ListingTable.NAME_MG;
                break;
            case ListingTable.NAME_PANOZ:
                tableName = ListingTable.NAME_PANOZ;
                break;
            case ListingTable.NAME_TRIUMPH:
                tableName = ListingTable.NAME_TRIUMPH;
                break;
            case ListingTable.NAME_TVR:
                tableName = ListingTable.NAME_TVR;
                break;
            default:
                tableName = ListingTable.NAME_LOTUS; // Default to Lotus because we love Lotus <3
                break;
        }

        List<Listing> listings = new ArrayList<>();

        ListingCursorWrapper cursor = queryListings(tableName, null, null);

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

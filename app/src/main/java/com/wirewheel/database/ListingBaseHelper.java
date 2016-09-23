package com.wirewheel.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.wirewheel.database.ListingDbSchema.*;

/**
 * Created by Chris on 9/22/2016.
 */
public class ListingBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "listingBase.db";

    public ListingBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + ListingTable.NAME + "("+
                " _id integer primary key autoincrement, " +
                ListingTable.Cols.LINK + ", " +
                ListingTable.Cols.TITLE + ", " +
                ListingTable.Cols.PRICE + ", " +
                ListingTable.Cols.MILEAGE + ", " +
                ListingTable.Cols.TEXT + ", " +
                ListingTable.Cols.KEY_IMG_LINK + ", " +
                ListingTable.Cols.IMAGES +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

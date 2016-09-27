package com.wirewheel.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.wirewheel.listings.Listing;

import static com.wirewheel.database.ListingDbSchema.*;

/**
 * Created by Chris on 9/26/2016.
 */
public class ListingCursorWrapper extends CursorWrapper {
    public ListingCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Listing getListing() {
        String linkString = getString(getColumnIndex(ListingTable.Cols.LINK));
        String titleString = getString(getColumnIndex(ListingTable.Cols.TITLE));
        String priceString = getString(getColumnIndex(ListingTable.Cols.PRICE));
        String mileageString = getString(getColumnIndex(ListingTable.Cols.MILEAGE));
        String keyImageLink = getString(getColumnIndex(ListingTable.Cols.KEY_IMG_LINK));

        Listing listing = new Listing();
        listing.setLink(linkString);
        listing.setTitle(titleString);
        listing.setPrice(priceString);
        listing.setMileage(mileageString);
        listing.setKeyImageLink(keyImageLink);

        return listing;
    }
}

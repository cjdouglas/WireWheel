package com.wirewheel.database;

/**
 * Created by Chris on 9/22/2016.
 */
public class ListingDbSchema {
    public static final class ListingTable {

        public static final String NAME = "listings";

        public static final class Cols {
            public static final String LINK = "link";
            public static final String TITLE = "title";
            public static final String PRICE = "price";
            public static final String MILEAGE = "mileage";
            public static final String TEXT = "text";
            public static final String KEY_IMG_LINK = "key_img_link";
            public static final String IMAGES = "images";
        }
    }
}

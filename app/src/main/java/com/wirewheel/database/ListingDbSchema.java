package com.wirewheel.database;

/**
 * Created by Chris on 9/22/2016.
 */
public class ListingDbSchema {
    public static final class ListingTable {

        public static final String NAME_RACECARS = "Race_Cars";
        public static final String NAME_AUSTIN_HEALEY = "Austin_Healey";
        public static final String NAME_JAGUAR = "Jaguar";
        public static final String NAME_LOTUS = "Lotus";
        public static final String NAME_MARCOS = "Marcos";
        public static final String NAME_MINI = "Mini";
        public static final String NAME_MG = "MG";
        public static final String NAME_PANOZ = "Panoz";
        public static final String NAME_TRIUMPH = "Triumph";
        public static final String NAME_TVR = "TVR";

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

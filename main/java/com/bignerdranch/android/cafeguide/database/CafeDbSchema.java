package com.bignerdranch.android.cafeguide.database;

public class CafeDbSchema {

    // Defining CafeTable
    public static final class CafeTable {
        public static final String NAME = "cafes";

        // Defining table columns
        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String CAFE = "cafe";
            public static final String DATE = "visitedDate";
            public static final String REVIEW = "review";
            public static final String RECOMMENDED = "recommended";
        }
    }
}

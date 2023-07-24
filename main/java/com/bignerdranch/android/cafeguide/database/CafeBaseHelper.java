package com.bignerdranch.android.cafeguide.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bignerdranch.android.cafeguide.database.CafeDbSchema.CafeTable;

public class CafeBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "cafeBase.db";

    public CafeBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + CafeTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                CafeTable.Cols.UUID + ", " +
                CafeTable.Cols.CAFE + ", " +
                CafeTable.Cols.DATE + ", " +
                CafeTable.Cols.REVIEW + ", " +
                CafeTable.Cols.RECOMMENDED +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}

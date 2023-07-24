package com.bignerdranch.android.cafeguide;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bignerdranch.android.cafeguide.database.CafeBaseHelper;
import com.bignerdranch.android.cafeguide.database.CafeCursorWrapper;
import com.bignerdranch.android.cafeguide.database.CafeDbSchema.CafeTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CafeList {
    private static CafeList sCafeList;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static CafeList get(Context context) {
        if (sCafeList == null) {
            sCafeList = new CafeList(context);
        }
        return sCafeList;
    }

    private CafeList(Context context) {
        // Opening a SQLiteDatabase
        mContext = context.getApplicationContext();
        mDatabase = new CafeBaseHelper(mContext).getWritableDatabase();
    }

    // Inserting a row to database
    public void addCafe(Cafe c) {
        ContentValues values = getContentValues(c);

        mDatabase.insert(CafeTable.NAME, null, values);
    }

    // Returning cafe list
    public List<Cafe> getCafes() {
        List<Cafe> cafes = new ArrayList<>();

        CafeCursorWrapper cursor = queryCafes(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                cafes.add(cursor.getCafe());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return cafes;
    }

    // Returning cafe object
    public Cafe getCafe(UUID id) {
        CafeCursorWrapper cursor = queryCafes(
                CafeTable.Cols.UUID + " = ?",
                new String[] { id.toString() }
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getCafe();
        } finally {
            cursor.close();
        }
    }

    // Finding photo file location
    public File getPhotoFile(Cafe cafe) {
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, cafe.getPhotoFilename());
    }

    // Updating a row in database
    public void updateCafe(Cafe cafe) {
        String uuidString = cafe.getID().toString();
        ContentValues values = getContentValues(cafe);

        mDatabase.update(CafeTable.NAME, values,
                CafeTable.Cols.UUID + " = ?",
                new String[] { uuidString });
    }

    // Querying for Cafes
    private CafeCursorWrapper queryCafes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                CafeTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new CafeCursorWrapper(cursor);
    }

    public void deleteCafe(Cafe cafe) {
        mDatabase.delete(CafeTable.NAME,
                CafeTable.Cols.UUID + " = ?",
                new String[]{ cafe.getID().toString() });
    }

    // Creating a ContentValues
    private static ContentValues getContentValues(Cafe cafe) {
        ContentValues values = new ContentValues();
        values.put(CafeTable.Cols.UUID, cafe.getID().toString());
        values.put(CafeTable.Cols.CAFE, cafe.getCafeName());
        values.put(CafeTable.Cols.DATE, cafe.getVisitedDate().getTime());
        values.put(CafeTable.Cols.REVIEW, cafe.getReview());
        values.put(CafeTable.Cols.RECOMMENDED, cafe.isRecommended() ? 1 : 0);

        return values;
    }
}

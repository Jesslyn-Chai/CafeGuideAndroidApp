package com.bignerdranch.android.cafeguide.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.bignerdranch.android.cafeguide.Cafe;
import com.bignerdranch.android.cafeguide.database.CafeDbSchema.CafeTable;

import java.util.Date;
import java.util.UUID;

public class CafeCursorWrapper extends CursorWrapper {
    public CafeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Cafe getCafe() {
        String uuidString = getString(getColumnIndex(CafeTable.Cols.UUID));
        String cafeName = getString(getColumnIndex(CafeTable.Cols.CAFE));
        long visitedDate = getLong(getColumnIndex(CafeTable.Cols.DATE));
        String review = getString(getColumnIndex(CafeTable.Cols.REVIEW));
        int isRecommended = getInt(getColumnIndex(CafeTable.Cols.RECOMMENDED));

        Cafe cafe = new Cafe(UUID.fromString(uuidString));
        cafe.setCafeName(cafeName);
        cafe.setVisitedDate(new Date(visitedDate));
        cafe.setReview(review);
        cafe.setRecommended(isRecommended != 0);

        return cafe;
    }
}

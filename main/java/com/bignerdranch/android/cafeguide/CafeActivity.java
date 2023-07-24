package com.bignerdranch.android.cafeguide;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class CafeActivity extends SingleFragmentActivity {

    private static final String EXTRA_CAFE_ID = "com.bignerdranch.android.cafeguide.cafe_id";

    // Creating newIntent() method
    public static Intent newIntent(Context packageContext, UUID cafeID) {
        Intent intent = new Intent(packageContext, CafeActivity.class);
        intent.putExtra(EXTRA_CAFE_ID, cafeID);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        UUID cafeID = (UUID) getIntent().getSerializableExtra(EXTRA_CAFE_ID);
        return CafeFragment.newInstance(cafeID);
    }
}

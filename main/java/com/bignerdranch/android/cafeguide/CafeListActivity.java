package com.bignerdranch.android.cafeguide;

import android.support.v4.app.Fragment;

public class CafeListActivity extends SingleFragmentActivity{
    @Override
    protected Fragment createFragment() {
        return new CafeListFragment();
    }
}

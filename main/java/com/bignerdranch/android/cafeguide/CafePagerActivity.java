package com.bignerdranch.android.cafeguide;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

public class CafePagerActivity extends AppCompatActivity {

    private static final String EXTRA_CAFE_ID = "com.bignerdranch.android.cafeguide.cafe_id";

    private ViewPager mViewPager;
    private List<Cafe> mCafes;

    // Creating newIntent
    public static Intent newIntent(Context packageContext, UUID cafeID) {
        Intent intent = new Intent(packageContext, CafePagerActivity.class);
        intent.putExtra(EXTRA_CAFE_ID, cafeID);
        return intent;
    }

    // Setting up ViewPager
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cafe_pager);

        UUID cafeID = (UUID) getIntent().getSerializableExtra(EXTRA_CAFE_ID);

        // Setting up pager adapter
        mViewPager = (ViewPager) findViewById(R.id.cafe_view_pager);

        mCafes = CafeList.get(this).getCafes();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Cafe cafe = mCafes.get(position);
                return CafeFragment.newInstance(cafe.getID());
            }

            @Override
            public int getCount() {
                return mCafes.size();
            }
        });

        // Setting the initial pager item
        for (int i = 0; i < mCafes.size(); i++) {
            if (mCafes.get(i).getID().equals(cafeID)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }

    }
}

package com.bignerdranch.android.cafeguide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CafeListFragment extends Fragment {

    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";

    private RecyclerView mCafeRecyclerView;
    private CafeAdapter mAdapter;
    private boolean mSubtitleVisible;

    // Receiving menu callbacks
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    // Setting up the view for CafeListFragment
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cafe_list, container, false);

        mCafeRecyclerView = (RecyclerView) view.findViewById(R.id.cafe_recycler_view);
        mCafeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

        updateUI();

        return view;
    }

    // Reloading the list in onResume()
    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    // Saving subtitle visibility
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // Inflating a menu resource
        inflater.inflate(R.menu.fragment_cafe_list, menu);

        // Updating a MenuItem
        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if (mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    // Responding to menu selection
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_cafe_review:
                Cafe cafe = new Cafe();
                CafeList.get(getActivity()).addCafe(cafe);
                Intent intent = CafePagerActivity.newIntent(getActivity(), cafe.getID());
                startActivity(intent);

                return true;

            case R.id.show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Setting the toolbar's subtitle
    private void updateSubtitle() {
        CafeList cafeList = CafeList.get(getActivity());
        int cafeCount = cafeList.getCafes().size();
        String subtitle = getString(R.string.subtitle_format, cafeCount);

        // Showing or hiding the subtitle
        if (!mSubtitleVisible) {
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    private void updateUI() {
        CafeList cafeList = CafeList.get(getActivity());
        List<Cafe> cafes = cafeList.getCafes();

        if (mAdapter == null) {
            mAdapter = new CafeAdapter(cafes);
            mCafeRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setCafes(cafes);
            mAdapter.notifyDataSetChanged();
        }


        updateSubtitle();
    }

    // Implement ViewHolder
    private class CafeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Cafe mCafe;

        private TextView mCafeTextView;
        private TextView mVisitedDateTextView;
        private ImageView mRecommendedImageView;

        public CafeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_cafe, parent, false));
            itemView.setOnClickListener(this);

            mCafeTextView = (TextView) itemView.findViewById(R.id.cafe_name);
            mVisitedDateTextView = (TextView) itemView.findViewById(R.id.cafe_visited_date);
            mRecommendedImageView = (ImageView) itemView.findViewById(R.id.cafe_recommended);
        }

        // Binding list items
        public void bind(Cafe cafe) {
            mCafe = cafe;
            mCafeTextView.setText(mCafe.getCafeName());
            mVisitedDateTextView.setText(mCafe.getVisitedDate().toString());
            mRecommendedImageView.setVisibility(cafe.isRecommended() ? View.VISIBLE : View.GONE);
        }

        // Starting CafeActivity
        @Override
        public void onClick(View view) {
            // Start an instance of CafePagerActivity
            Intent intent = CafePagerActivity.newIntent(getActivity(), mCafe.getID());
            startActivity(intent);
        }
    }

    // Implement Adapter
    private class CafeAdapter extends RecyclerView.Adapter<CafeHolder> {
        private List<Cafe> mCafes;

        public CafeAdapter(List<Cafe> cafes) {
            mCafes = cafes;
        }

        @Override
        public CafeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new CafeHolder(layoutInflater, parent);
        }

        // Binding list items
        @Override
        public void onBindViewHolder(CafeHolder holder, int position) {
            Cafe cafe = mCafes.get(position);
            holder.bind(cafe);
        }

        @Override
        public int getItemCount() {
            return mCafes.size();
        }

        public void setCafes(List<Cafe> cafes) {
            mCafes = cafes;
        }
    }
}

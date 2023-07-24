package com.bignerdranch.android.cafeguide;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class CafeFragment extends Fragment {

    private static final String ARG_CAFE_ID = "cafe_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_TIME = "DialogTime";

    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;
    private static final int REQUEST_PHOTO = 2;

    private Cafe mCafe;
    private File mPhotoFile;
    private EditText mCafeNameField;
    private EditText mCafeReviewField;
    private Button mVisitedDateButton;
    private Button mVisitedTimeButton;
    private CheckBox mRecommendedCheckBox;
    private Button mShareButton;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;

    // Creating newInstance(UUID) method
    public static CafeFragment newInstance(UUID cafeID) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CAFE_ID, cafeID);

        CafeFragment fragment = new CafeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    // Overriding Fragment .onCreate(Bundle)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Getting cafe iD from the arguments
        UUID cafeID = (UUID) getArguments().getSerializable(ARG_CAFE_ID);
        mCafe = CafeList.get(getActivity()).getCafe(cafeID);
        mPhotoFile = CafeList.get(getActivity()).getPhotoFile(mCafe);
    }

    @Override
    public void onPause() {
        super.onPause();

        // Pushing updates
        CafeList.get(getActivity()).updateCafe(mCafe);
    }

    // Overriding onCreateView(...)
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cafe, container, false);

        // Get reference and add listener for mCafeNameField
        mCafeNameField = (EditText) v.findViewById(R.id.cafe_name);
        mCafeNameField.setText(mCafe.getCafeName());
        mCafeNameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // This space intentionally left blank
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCafe.setCafeName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // This one too
            }
        });

        // Get reference and add listener for mCafeReviewField
        mCafeReviewField = (EditText) v.findViewById(R.id.cafe_review);
        mCafeReviewField.setText(mCafe.getReview());
        mCafeReviewField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // This space intentionally left blank
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCafe.setReview(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // This one too
            }
        });

        // Get reference for mVisitedDateButton to display the visited date
        mVisitedDateButton = (Button) v.findViewById(R.id.cafe_visited_date);
        updateDate();
        mVisitedDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Showing the DialogFragment
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCafe.getVisitedDate());
                // Setting target fragment
                dialog.setTargetFragment(CafeFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);

            }
        });

        // Get reference for mVisitedTimeButton to display the visited date
        mVisitedTimeButton = (Button) v.findViewById(R.id.cafe_visited_time);
        updateTime();
        mVisitedTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Showing the DialogFragment
                FragmentManager manager = getFragmentManager();
                TimePickerFragment dialog = TimePickerFragment.newInstance(mCafe.getVisitedDate());
                // Setting target fragment
                dialog.setTargetFragment(CafeFragment.this, REQUEST_TIME);
                dialog.show(manager, DIALOG_TIME);
            }
        });

        // Get reference and add listener for mRecommendedCheckBox
        mRecommendedCheckBox = (CheckBox) v.findViewById(R.id.cafe_recommended);
        mRecommendedCheckBox.setChecked(mCafe.isRecommended());
        mRecommendedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCafe.setRecommended(isChecked);
            }
        });

        // Get reference and add listener for mShareButton
        mShareButton = (Button) v.findViewById(R.id.cafe_report);
        mShareButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Creating an implicit intent to send a cafe review report
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, getCafeReport());
                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.cafe_report_subject));
                i = Intent.createChooser(i, getString(R.string.send_report));
                startActivity(i);
            }
        });

        // Get reference and add listener for mPhotoButton
        // Firing a camera intent
        mPhotoButton = (ImageButton) v.findViewById(R.id.cafe_camera);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        PackageManager packageManager = getActivity().getPackageManager();
        boolean canTakePhoto = mPhotoFile != null && captureImage.resolveActivity(packageManager) != null;
        mPhotoButton.setEnabled(canTakePhoto);

        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = FileProvider.getUriForFile(getActivity(), "com.bignerdranch.android.cafeguide.fileprovider", mPhotoFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                List<ResolveInfo> cameraActivities = getActivity().getPackageManager().queryIntentActivities(captureImage, PackageManager.MATCH_DEFAULT_ONLY);

                for (ResolveInfo activity : cameraActivities) {
                    getActivity().grantUriPermission(activity.activityInfo.packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }

                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });

        // Get reference for mPhotoView
        mPhotoView = (ImageView) v.findViewById(R.id.cafe_photo);
        updatePhotoView();

        return v;
    }

    // Responding to the dialog
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCafe.setVisitedDate(date);
            updateDate();
        } else if (requestCode == REQUEST_TIME) {
            Date date = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            mCafe.setVisitedDate(date);
            updateTime();
        } else if (requestCode == REQUEST_PHOTO) {
            Uri uri = FileProvider.getUriForFile(getActivity(), "com.bignerdranch.android.cafeguide.fileprovider", mPhotoFile);
            getActivity().revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            updatePhotoView();
        }
    }

    private void updateDate() {
        mVisitedDateButton.setText(mCafe.getVisitedDateByFormat("EEE dd/MM/yyyy"));
    }

    private void updateTime() {
        mVisitedTimeButton.setText(mCafe.getVisitedDateByFormat("HH:mm"));
    }

    // Adding getCafeReport() to return a complete cafe review report
    private String getCafeReport() {
        String recommendedString = null;
        if (mCafe.isRecommended()) {
            recommendedString = getString(R.string.cafe_report_recommended);
        } else {
            recommendedString = getString(R.string.cafe_report_unrecommended);
        }

        String dateFormat = "EEE, MMM dd (HH:mm)";
        String dateString = DateFormat.format(dateFormat, mCafe.getVisitedDate()).toString();

        String review = getString(R.string.cafe_report_review, mCafe.getReview());

        String report = getString(R.string.cafe_report, mCafe.getCafeName(), dateString, recommendedString, review);

        return report;
    }

    // Updating mPhotoView
    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
    }
}

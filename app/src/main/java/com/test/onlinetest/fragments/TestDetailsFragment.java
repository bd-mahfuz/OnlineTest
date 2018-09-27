package com.test.onlinetest.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import com.test.onlinetest.R;
import com.test.onlinetest.model.Test;
import com.test.onlinetest.utility.OnlineTestUtility;

/**
 * A simple {@link Fragment} subclass.
 */
public class TestDetailsFragment extends Fragment {

    private TextView mStartDateTv, mTotalMarksTv, mTitleTv, mEstTimeTv;

    private TableLayout mDetailsTab;

    private Test mTest;

    public TestDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_test_details, container, false);

        mStartDateTv = view.findViewById(R.id.dStartDateTv);
        mTotalMarksTv = view.findViewById(R.id.dTotalMarksTv);
        mTitleTv = view.findViewById(R.id.dTitleTv);
        mEstTimeTv = view.findViewById(R.id.dEstTimeTv);

        Bundle bundle = getArguments();

        if (bundle != null) {
            mTest = (Test) bundle.getSerializable("test");
        }

        mTitleTv.setText(mTest.getTitle());
        mStartDateTv.setText("Created Date:"+ OnlineTestUtility.getDateToMilli(mTest.getCreateDate()));
        mTotalMarksTv.setText("Marks: "+mTest.getTotalMarks());
        mEstTimeTv.setText("Time:"+ OnlineTestUtility.getTimeToMilli(mTest.getEstimateTime()));


        return view;
    }

}

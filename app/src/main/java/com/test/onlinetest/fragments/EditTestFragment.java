package com.test.onlinetest.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.test.onlinetest.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditTestFragment extends Fragment {

    private TextView mStartDateTv, mTotalMarksTv, mTitleTv, mEstTimeTv, mFeeTv;


    public EditTestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_test, container, false);

        mStartDateTv = view.findViewById(R.id.dStartDateTv);
        mTotalMarksTv = view.findViewById(R.id.dTotalMarksTv);
        mTitleTv = view.findViewById(R.id.dTitleTv);
        mEstTimeTv = view.findViewById(R.id.dEstTimeTv);
        mFeeTv = view.findViewById(R.id.dFeeTv);


        return view;
    }

}

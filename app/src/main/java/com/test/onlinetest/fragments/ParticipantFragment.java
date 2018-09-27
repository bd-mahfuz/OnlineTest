package com.test.onlinetest.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.test.onlinetest.R;
import com.test.onlinetest.TestInfoActivity;
import com.test.onlinetest.adapter.ParticipantAdapter;
import com.test.onlinetest.model.ParticipantDetails;
import com.test.onlinetest.model.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ParticipantFragment extends Fragment {

    private ProgressBar mProgressBar;
    private RecyclerView mPRv;
    private SearchView mSearchView;
    private TextView mPMsg;

    private DatabaseReference mRootRef;
    private DatabaseReference mParticipantRef;
    private FirebaseAuth mAuth;

    private Test mTest;
    private List<ParticipantDetails> participantDetailsList = new ArrayList<>();
    TestInfoActivity activity;


    public ParticipantFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        activity = (TestInfoActivity) getActivity();

        mAuth = FirebaseAuth.getInstance();
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mParticipantRef = mRootRef.child("Participants");

        Bundle bundle = getArguments();

        if (bundle != null) {
            mTest = (Test) bundle.getSerializable("test");
        }

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_participant, container, false);

        initView(view);

        mPRv.setLayoutManager(new LinearLayoutManager(activity));

        return view;
    }


    public void initView(View view) {
        mProgressBar = view.findViewById(R.id.pBar);
        mPRv = view.findViewById(R.id.pRv);
        mSearchView = view.findViewById(R.id.pSeachView);
        mPMsg = view.findViewById(R.id.pMsg);
    }

    @Override
    public void onStart() {
        super.onStart();

        mProgressBar.setVisibility(View.VISIBLE);
        mParticipantRef.child(mTest.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    participantDetailsList.clear();
                    for (DataSnapshot participant : dataSnapshot.getChildren()) {
                        ParticipantDetails participantDetails = participant.getValue(ParticipantDetails.class);
                        participantDetailsList.add(participantDetails);
                    }

                    if (participantDetailsList.size() <= 0) {
                        mPMsg.setVisibility(View.VISIBLE);
                        mProgressBar.setVisibility(View.GONE);
                    } else {
                        ParticipantAdapter adapter = new ParticipantAdapter(activity, participantDetailsList);
                        mPRv.setAdapter(adapter);
                        mProgressBar.setVisibility(View.GONE);
                    }
                } else {
                    mPMsg.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                databaseError.toException();
                mProgressBar.setVisibility(View.GONE);
            }
        });

    }
}

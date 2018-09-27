package com.test.onlinetest.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.test.onlinetest.R;
import com.test.onlinetest.StartTestActivity;
import com.test.onlinetest.model.ParticipantDetails;
import com.test.onlinetest.model.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnswerDetailsFragment extends Fragment {

    private TextView mScoreTv, mRankTv;
    private TextView mQuizTitleTv, mTotalQTv, mCorrectAnsTv, mWrongAnsTv;
    private Button mReviewBTn;

    private Test mTest;

    private DatabaseReference mRootRef;
    private DatabaseReference mParticipantRef;
    private DatabaseReference mAnswerRef;
    private FirebaseAuth mAuth;

    private StartTestActivity activity;

    private List<ParticipantDetails> participantDetailsList = new ArrayList<>();


    private ProgressDialog mProgressDialog;

    public AnswerDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {

        activity = (StartTestActivity) getActivity();

        mRootRef = FirebaseDatabase.getInstance().getReference();
        mParticipantRef = mRootRef.child("Participants");
        mAnswerRef = mRootRef.child("Answers");
        mAuth = FirebaseAuth.getInstance();

        final Bundle bundle = getArguments();

        if (bundle!=null) {
            mTest = (Test) bundle.getSerializable("test");
        }

        //Log.d("title", mTest.getTitle());

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_answer_details, container, false);

        // initialize the view
        initView(view);

        mProgressDialog = new ProgressDialog(activity);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Loading...");


        setData();

        mParticipantRef.child(mTest.getId()).orderByChild("score").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot student: dataSnapshot.getChildren()) {
                    ParticipantDetails participantDetails = student.getValue(ParticipantDetails.class);
                    participantDetailsList.add(participantDetails);
                }

                Collections.reverse(participantDetailsList);

                int rank = 0;
                for (rank = 0; rank<participantDetailsList.size(); rank++) {
                    if (participantDetailsList.get(rank).getId().equals(mAuth.getCurrentUser().getUid())) {
                        break;
                    }
                }

                mRankTv.setText("Your rank:\n"+(rank+1));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.toException();
            }
        });


        mReviewBTn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReviewAnswerFragment fragment = new ReviewAnswerFragment();
                fragment.setArguments(bundle);
                replaceFragment(fragment);
            }
        });

        return view;
    }


    public void initView(View view) {
        mScoreTv = view.findViewById(R.id.scoreTv);
        mRankTv = view.findViewById(R.id.rankTv);
        mQuizTitleTv = view.findViewById(R.id.qTitleTv);
        mTotalQTv = view.findViewById(R.id.totalQTv);
        mCorrectAnsTv = view.findViewById(R.id.correctAnsTv);
        mWrongAnsTv = view.findViewById(R.id.wrongAnsTv);

        mReviewBTn = view.findViewById(R.id.sRegisterBtn);
    }


    public void setData() {
        mProgressDialog.show();

        mQuizTitleTv.setText(mTest.getTitle());

        mParticipantRef.child(mTest.getId()).child(mAuth.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                       // if (dataSnapshot.getValue() != null) {
                            String score = dataSnapshot.child("score").getValue()+"";
                            String totalQuestion  = dataSnapshot.child("totalQuestion").getValue()+"";
                            String totalAnsweredQ = dataSnapshot.child("totalAnswered").getValue()+"";

                            mScoreTv.setText("Your Score:\n"+ score+"/"+totalQuestion);
                            mTotalQTv.setText(totalQuestion);
                            mCorrectAnsTv.setText(totalAnsweredQ);


                            mProgressDialog.dismiss();
                        /*} else {
                            mScoreTv.setText("Your Score:\n"+ 0+"/"+q);
                            mTotalQTv.setText(totalQuestion);
                            mCorrectAnsTv.setText(totalAnsweredQ);
                        }*/
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        databaseError.toException();
                        mProgressDialog.dismiss();
                    }
                });

    }


    public void replaceFragment(Fragment destFragment)
    {
        // First get FragmentManager object.
        FragmentManager fragmentManager = activity.getSupportFragmentManager();

        // Begin Fragment transaction.
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the layout holder with the required Fragment object.
        fragmentTransaction.replace(R.id.startTestActivity, destFragment);

        // Commit the Fragment replace action.
        fragmentTransaction.commit();
    }




}

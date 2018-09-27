package com.test.onlinetest.fragments;


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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.test.onlinetest.R;
import com.test.onlinetest.StartTestActivity;
import com.test.onlinetest.model.Test;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class StartTestFragment extends Fragment {

    private TextView mTestTitleTv;
    private Button mStartTestBTn;

    private Test mTest;

    private FirebaseAuth mAuth;

    private DatabaseReference mRootRef;
    private DatabaseReference mParticipantRef;

    private StartTestActivity activity;

    public StartTestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootRef = FirebaseDatabase.getInstance().getReference();
        mParticipantRef = mRootRef.child("Participants");

        activity = (StartTestActivity) getActivity();

        mAuth = FirebaseAuth.getInstance();

        final Bundle bundle = getArguments();

        if (bundle!=null) {
            mTest = (Test) bundle.getSerializable("test");
        }

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_start_test, container, false);

        mTestTitleTv = view.findViewById(R.id.testTitleTv);
        mStartTestBTn = view.findViewById(R.id.startTestBtn);

        mStartTestBTn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                HashMap<String, String> hashMap = new HashMap<>();
//                hashMap.put("")

                mParticipantRef.child(mTest.getId()).child(mAuth.getCurrentUser().getUid())
                        .child("isFinished").setValue("false").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Bundle bundle1 = new Bundle();
                            bundle1.putSerializable("test", mTest);
                            AnswerFragment answerFragment = new AnswerFragment();
                            answerFragment.setArguments(bundle1);

                            replaceFragment(answerFragment);
                        }
                    }
                });


            }
        });

        return view;
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

package com.test.onlinetest.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.test.onlinetest.AdminPanelActivity;
import com.test.onlinetest.R;
import com.test.onlinetest.TestInfoActivity;
import com.test.onlinetest.adapter.TestAdapter;
import com.test.onlinetest.model.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TestsFragment extends Fragment {

    private List<Test> testList = new ArrayList<>();

    private RecyclerView mTestRv;
    private TextView mTestMsg;
    private FloatingActionButton mTestFab;

    private DatabaseReference mRootRef;
    private DatabaseReference mTestsRef;


    public TestsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_tests, container, false);

        mTestFab = view.findViewById(R.id.addTestFab);
        mTestMsg = view.findViewById(R.id.noTestMsgTv);
        mTestRv = view.findViewById(R.id.testRv);

        mTestRv.setLayoutManager(new LinearLayoutManager(getContext()));

        mTestFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new AddTestFragment());
            }
        });


        mRootRef = FirebaseDatabase.getInstance().getReference();
        mTestsRef = mRootRef.child("Tests");

        mTestsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                testList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Log.d("dataSnapshot", dataSnapshot1+"");
                    Test test = (Test) dataSnapshot1.getValue(Test.class);
                    testList.add(test);
                }

                if (testList.size() == 0) {
                    mTestMsg.setVisibility(View.VISIBLE);
                } else {
                    TestAdapter adapter = new TestAdapter(getContext(), testList);
                    mTestRv.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.toException();
            }
        });

        return view;
    }



    public void replaceFragment(Fragment destFragment)
    {
        // First get FragmentManager object.
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.popBackStack();

        // Begin Fragment transaction.
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the layout holder with the required Fragment object.
        fragmentTransaction.replace(R.id.adminFragmentContainer, destFragment);
        fragmentTransaction.addToBackStack(null);

        // Commit the Fragment replace action.
        fragmentTransaction.commit();
    }

}

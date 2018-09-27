package com.test.onlinetest.fragments;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.test.onlinetest.AdminPanelActivity;
import com.test.onlinetest.OnlineTest;
import com.test.onlinetest.R;
import com.test.onlinetest.model.Test;
import com.test.onlinetest.utility.OnlineTestUtility;

import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddTestFragment extends Fragment {

    private TextInputLayout mTitleEt, mTotalMarksEt, mEstTimeEt;
    private Button mAddTestBt;

    private String mTitle;
    int mTotalMark;
    long mEstTime;


    private int mHour;
    private int mMinuit;

    private FirebaseAuth mAuth;
    private DatabaseReference mRootRef;
    private DatabaseReference mTestsRef;


    public AddTestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();

        mRootRef = FirebaseDatabase.getInstance().getReference();
        mTestsRef = mRootRef.child("Tests");

        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_add_test, container, false);

       /* // update app bar title
       AdminPanelActivity activity = (AdminPanelActivity) getActivity();
       activity.getSupportActionBar().setTitle("Create Test");*/


       mTitleEt = view.findViewById(R.id.titleEt);
       mTotalMarksEt = view.findViewById(R.id.marksEt);
       mEstTimeEt = view.findViewById(R.id.timeEt);
       mAddTestBt = view.findViewById(R.id.addTestBt);


        mEstTimeEt.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog pickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        mHour = i;
                        mMinuit = i1;
                        mEstTimeEt.getEditText().setText(i+" hour "+ i1+" minute");
                    }
                }, hour, minute, true);
                pickerDialog.setTitle("Select time");
                pickerDialog.show();
            }
        });

       mAddTestBt.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if (validate()) {
                   // getting input data
                   getInputData();

                   String id = mTestsRef.push().getKey();
                   //Log.d("id", id);
                   Test test = new Test(id, mTitle, mTotalMark, mEstTime, new Date().getTime());

                   mTestsRef.child(id).setValue(test).addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           if (task.isSuccessful()) {
                               Toast.makeText(getActivity(), "New Test is created Successfully", Toast.LENGTH_SHORT).show();
                               replaceFragment(new TestsFragment());
                           }
                           else {
                               Toast.makeText(getActivity(), "New Test is not Created.", Toast.LENGTH_SHORT).show();
                           }
                       }
                   });
               }

           }
       });

       return view;
    }

    public void getInputData() {
        mTitle = mTitleEt.getEditText().getText().toString();
        mTotalMark = Integer.valueOf(mTotalMarksEt.getEditText().getText().toString());
        mEstTime = (mHour * 60 * 60 * 1000) + (mMinuit * 60 * 1000);
    }

    public boolean validate() {
        boolean isValid = true;

        if (mTitleEt.getEditText().getText().toString().isEmpty()) {
            mTitleEt.getEditText().setError("Title should not be empty!");
            isValid = false;
        }

        if (mTotalMarksEt.getEditText().getText().toString().isEmpty()) {
            mTotalMarksEt.getEditText().setError("Total marks should not be empty!");
            isValid = false;
        }
        if (mEstTimeEt.getEditText().getText().toString().isEmpty()) {
            mEstTimeEt.getEditText().setError("Estimate time should not be empty!");
            isValid = false;
        }

        return isValid;
    }

    public void replaceFragment(Fragment destFragment)
    {
        // First get FragmentManager object.
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        // Begin Fragment transaction.
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the layout holder with the required Fragment object.
        fragmentTransaction.replace(R.id.adminFragmentContainer, destFragment);
        // Commit the Fragment replace action.
        fragmentTransaction.commit();
    }

}

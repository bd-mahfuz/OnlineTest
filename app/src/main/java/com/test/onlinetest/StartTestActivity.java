package com.test.onlinetest;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.test.onlinetest.fragments.AnswerDetailsFragment;
import com.test.onlinetest.fragments.StartTestFragment;
import com.test.onlinetest.model.Test;

public class StartTestActivity extends AppCompatActivity {

    private Toolbar mToolBar;

    private DatabaseReference mRootRef;
    private DatabaseReference mParticipants;

    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_test);

        mAuth = FirebaseAuth.getInstance();

        mRootRef = FirebaseDatabase.getInstance().getReference();
        mParticipants = mRootRef.child("Participants");

        Test test = (Test) getIntent().getSerializableExtra("test");

        final Bundle bundle = new Bundle();
        bundle.putSerializable("test", test);

        mToolBar = findViewById(R.id.startTestToolBar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle(test.getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        mParticipants.child(test.getId()).child(mAuth.getCurrentUser().getUid()).child("isFinished")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            String isFinished = dataSnapshot.getValue().toString();

                            if (isFinished.equals("true")) {
                                AnswerDetailsFragment answerDetailsFragment = new AnswerDetailsFragment();
                                answerDetailsFragment.setArguments(bundle);
                                replaceFragment(answerDetailsFragment);
                            } else {
                                StartTestFragment startTestFragment = new StartTestFragment();
                                startTestFragment.setArguments(bundle);
                                replaceFragment(startTestFragment);
                            }
                        } else {
                            StartTestFragment startTestFragment = new StartTestFragment();
                            startTestFragment.setArguments(bundle);
                            replaceFragment(startTestFragment);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        databaseError.toException();
                    }
                });

    }


    public void replaceFragment(Fragment destFragment)
    {
        // First get FragmentManager object.
        FragmentManager fragmentManager = this.getSupportFragmentManager();

        // Begin Fragment transaction.
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the layout holder with the required Fragment object.
        fragmentTransaction.replace(R.id.startTestActivity, destFragment);

        // Commit the Fragment replace action.
        fragmentTransaction.commit();
    }
}

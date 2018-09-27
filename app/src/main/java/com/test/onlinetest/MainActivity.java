package com.test.onlinetest;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.test.onlinetest.adapter.PublishedTestAdapter;
import com.test.onlinetest.adapter.TestAdapter;
import com.test.onlinetest.model.Test;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mRootRef;
    private DatabaseReference mStudentRef;
    private DatabaseReference mTestsRef;

    private List<Test> testList = new ArrayList<>();

    private Toolbar mToolBar;

    private String mIsPaid;
    private String mRole;

    private TextView mTestMsg;
    private RecyclerView mTestRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mIsPaid = getIntent().getStringExtra("isPaid");
        mRole = getIntent().getStringExtra("role");

        mAuth = FirebaseAuth.getInstance();
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mStudentRef = mRootRef.child("Students");
        mTestsRef = mRootRef.child("Tests");

        mTestMsg = findViewById(R.id.testMsg);
        mTestRv = findViewById(R.id.pTestRv);

        //GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mTestRv.setLayoutManager(new LinearLayoutManager(this));

        mToolBar = findViewById(R.id.mainToolBar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Online Test");

    }

    public void replaceFragment(Fragment destFragment)
    {
        // First get FragmentManager object.
        FragmentManager fragmentManager = this.getSupportFragmentManager();

        // Begin Fragment transaction.
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the layout holder with the required Fragment object.
        fragmentTransaction.replace(R.id.mainActivityXml, destFragment);

        // Commit the Fragment replace action.
        fragmentTransaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

        mCurrentUser = mAuth.getCurrentUser();
        if (mCurrentUser == null) {
            // go to Login activity
            sendToLoginActivity();
        } else {


            mTestsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    testList.clear();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        Log.d("dataSnapshot", dataSnapshot1+"");
                        Test test = (Test) dataSnapshot1.getValue(Test.class);
                        /*if (test.isPublish()) {
                            testList.add(test);
                        }*/
                        testList.add(test);
                    }

                    if (testList.size() == 0) {
                        mTestMsg.setVisibility(View.VISIBLE);
                    } else {
                        PublishedTestAdapter adapter = new PublishedTestAdapter(MainActivity.this, testList);
                        mTestRv.setAdapter(adapter);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    databaseError.toException();
                }
            });



            /*mStudentRef.child(mCurrentUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue()!= null) {
                        mIsPaid = dataSnapshot.child("paid").getValue().toString();
                        Log.d("paid", mIsPaid);
                        if (mIsPaid.equals("false")) {
                            Log.d("paid,", mIsPaid+"");
                            Intent paymentIntent = new Intent(MainActivity.this, PaymentActivity.class);
                            startActivity(paymentIntent);
                            finish();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    databaseError.toException();
                }
            });*/
        }



    }

    private void sendToLoginActivity() {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_main, menu);

        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.logoutMenu:
                FirebaseAuth.getInstance().signOut();
                sendToLoginActivity();
                break;
        }

        return true;
    }
}

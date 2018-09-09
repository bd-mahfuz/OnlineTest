package com.test.onlinetest;

import android.content.Intent;
import android.media.tv.TvContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mRootRef;
    private DatabaseReference mStudentRef;

    private Toolbar mToolBar;

    private String mIsPaid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mIsPaid = getIntent().getStringExtra("isPaid");

        mAuth = FirebaseAuth.getInstance();
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mStudentRef = mRootRef.child("Students");

        mToolBar = findViewById(R.id.mainToolBar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Online Test");
    }

    @Override
    protected void onStart() {
        super.onStart();

        mCurrentUser = mAuth.getCurrentUser();
        if (mCurrentUser == null) {
            // go to Login activity
            sendToLoginActivity();
        } else {
            mStudentRef.child(mCurrentUser.getUid()).addValueEventListener(new ValueEventListener() {
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

                }
            });
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

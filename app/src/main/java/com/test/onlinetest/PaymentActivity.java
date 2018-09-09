package com.test.onlinetest;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PaymentActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mRootRef;
    private DatabaseReference mStudentRef;
    private DatabaseReference mPaymentRef;

    private Toolbar mToolBar;


    private Button mPaymentBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        mAuth = FirebaseAuth.getInstance();
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mStudentRef = mRootRef.child("Students");
        mPaymentRef = mRootRef.child("Payment_Details");


        mToolBar = findViewById(R.id.paymentToolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Payment");

        mPaymentBt = findViewById(R.id.paymentBt);

        mPaymentBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // saving the payment details


                //update the paid status of the user.
                mStudentRef.child(mAuth.getCurrentUser().getUid()).child("paid").setValue("true")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(PaymentActivity.this, "Successfully paid the payment.", Toast.LENGTH_SHORT).show();
                            Intent mainIntent = new Intent(PaymentActivity.this, MainActivity.class);
                            startActivity(mainIntent);
                            finish();
                        }else {
                            Toast.makeText(PaymentActivity.this, "Failed to Pay! Try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }


    private void sendToLoginActivity() {
        Intent loginIntent = new Intent(PaymentActivity.this, LoginActivity.class);
        startActivity(loginIntent);
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

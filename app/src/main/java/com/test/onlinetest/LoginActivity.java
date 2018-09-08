package com.test.onlinetest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private TextView mRegisterBt;
    private Button mLoginBt;
    private TextInputLayout mEmailEt, mPasswordEt;

    private String mEmail, mPassword;

    private FirebaseAuth mAuth;
    private DatabaseReference mRootRef;
    private DatabaseReference mStudentRef;

    String mCurrentUser;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mStudentRef = mRootRef.child("Students");



        mEmailEt = findViewById(R.id.lEmailEt);
        mPasswordEt = findViewById(R.id.lPassEt);
        mRegisterBt = findViewById(R.id.registerTv);
        mLoginBt = findViewById(R.id.loginBt);

        //register button action
        mRegisterBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });

        //login button action
        mLoginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginStudent();
            }
        });
    }

    private void loginStudent() {
        //getting input data
        getInputData();
        
        if (validate()) {

            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Logging in.., Please wait.");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
            mAuth.signInWithEmailAndPassword(mEmail, mPassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                mCurrentUser = mAuth.getCurrentUser().getUid();

                                // checking user is paid or not. if paid student go MainActivity otherwise PaymentActivity
                                mStudentRef.child(mCurrentUser).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        // checking if user is exist or not in database
                                        if (dataSnapshot.getValue() != null) {
                                            boolean isPaid = (boolean) dataSnapshot.child("paid").getValue();

                                            if (isPaid) {
                                                Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                                                startActivity(mainIntent);
                                            }else {
                                                Intent mainIntent = new Intent(LoginActivity.this, PaymentActivity.class);
                                                startActivity(mainIntent);
                                            }
                                            mProgressDialog.dismiss();

                                        } else {
                                            Toast.makeText(LoginActivity.this, "Ops! User Not found in database.", Toast.LENGTH_SHORT).show();
                                            mProgressDialog.dismiss();
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        databaseError.toException();
                                        Toast.makeText(LoginActivity.this, "databaseError: Please contact with developer.", Toast.LENGTH_SHORT).show();
                                        mProgressDialog.dismiss();
                                    }
                                });

                            }else {
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                mProgressDialog.dismiss();

                            }
                        }
                    });

        }else {
            Toast.makeText(this, "Validation Faild! Please try again.", Toast.LENGTH_SHORT).show();
        }


    }

    private boolean isPaidStudent() {

        boolean isPaid = true;

        mStudentRef.child(mCurrentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild(mCurrentUser)) {
                    boolean isPaid = (boolean) dataSnapshot.child("paid").getValue();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.toException();
                Toast.makeText(LoginActivity.this, "databaseError: Please contact with developer.", Toast.LENGTH_SHORT).show();
            }
        });

        return isPaid;
    }

    private void getInputData() {
        mEmail = mEmailEt.getEditText().getText().toString();
        mPassword = mPasswordEt.getEditText().getText().toString();
    }

    public boolean validate() {
        boolean valid = true;
        if (mEmail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {
            mEmailEt.getEditText().setError("Please enter valid email address!");
            valid = false;
        }

        if (mPassword.isEmpty() || mPassword.length() < 6) {
            mPasswordEt.getEditText().setError("Password length must be greater than 6 digit!");
            valid = false;
        }
        return valid;
    }
}

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

    private RadioGroup mRadioGroup;
    private RadioButton mStudentRadio;
    private RadioButton mAdminRadio;

    private String mRole = "Student";
    private String mAdminRole;

    private String mEmail, mPassword;

    private FirebaseAuth mAuth;
    private DatabaseReference mRootRef;
    private DatabaseReference mStudentRef;
    private DatabaseReference mAdminRef;

    String mCurrentUser;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailEt = findViewById(R.id.lEmailEt);
        mPasswordEt = findViewById(R.id.lPassEt);
        mRegisterBt = findViewById(R.id.registerTv);
        mLoginBt = findViewById(R.id.loginBt);

        mAuth = FirebaseAuth.getInstance();
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mStudentRef = mRootRef.child("Students");
        mAdminRef = mRootRef.child("Admins");

        // getting value of radio group
        mRadioGroup = findViewById(R.id.roleRadioGr);
        mStudentRadio = findViewById(R.id.studentRadio);
        mAdminRadio = findViewById(R.id.adminRadio);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                switch (id) {
                    case R.id.studentRadio:
                        mRole = mStudentRadio.getText().toString();
                        break;
                    case R.id.adminRadio:
                        mRole = mAdminRadio.getText().toString();
                        break;
                }
            }
        });





        //register button action
        mRegisterBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRole.equals("Student")) {
                    Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(registerIntent);
                } else {
                    Toast.makeText(LoginActivity.this, "Only student can register here. Not Admin.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //login button action
        mLoginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRole.equals("Student")) {
                    loginStudent();
                }

                if (mRole.equals("Admin")) {
                    adminLogin();
                }
            }
        });
    }

    private void adminLogin() {
        //getting input data
        getInputData();
        if (validate()) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Logging in.., Please wait.");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();

            mAuth.signInWithEmailAndPassword(mEmail, mPassword)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String adminId = mAuth.getCurrentUser().getUid();
                                mAdminRef.child(adminId).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.getValue() != null) {
                                            if (dataSnapshot.child("role").getValue().equals("Admin")) {
                                                Intent adminIntent = new Intent(LoginActivity.this, AdminPanelActivity.class);
                                                startActivity(adminIntent);
                                                Toast.makeText(LoginActivity.this, "Log in successful (Admin).",
                                                        Toast.LENGTH_SHORT).show();
                                                mProgressDialog.dismiss();
                                                finish();
                                            } else {
                                                Toast.makeText(LoginActivity.this, "Login Failed(Admin)! Please try again.",
                                                        Toast.LENGTH_SHORT).show();
                                                mProgressDialog.dismiss();
                                            }
                                        } else {
                                            Toast.makeText(LoginActivity.this, "Admin not found or this credential not associated with admin role!",
                                                    Toast.LENGTH_SHORT).show();
                                            mProgressDialog.dismiss();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            } else {
                                Toast.makeText(LoginActivity.this, "Log in Failed (Admin). Try again.",
                                        Toast.LENGTH_SHORT).show();
                                mProgressDialog.dismiss();
                            }
                        }
                    });
        }
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
                                            String isPaid =  dataSnapshot.child("paid").getValue().toString();

                                            if (isPaid.equals("true")) {
                                                Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                                                mainIntent.putExtra("isPaid", isPaid);
                                                startActivity(mainIntent);
                                                finish();
                                            }else {
                                                Intent paymentIntent = new Intent(LoginActivity.this, PaymentActivity.class);
                                                startActivity(paymentIntent);
                                                finish();

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
                                        //Toast.makeText(LoginActivity.this, "databaseError: Please contact with developer.", Toast.LENGTH_SHORT).show();
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




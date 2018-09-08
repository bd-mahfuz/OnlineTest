package com.test.onlinetest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.test.onlinetest.model.Student;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private DatabaseReference mRootRef;
    private DatabaseReference mStudentRef;


    private TextInputLayout mNameEt, mEmailEt, mPhoneNoEt, mPasswordEt, mConfirmPassEt;

    private String mName, mEmail, mPhone, mPassword, mConfirmPass;
    private Button mRegisterBt;

    private FirebaseAuth mAuth;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mStudentRef = mRootRef.child("Students");

        mNameEt = findViewById(R.id.rNameEt);
        mEmailEt = findViewById(R.id.rEmailRt);
        mPhoneNoEt = findViewById(R.id.rPhoneEt);
        mPasswordEt = findViewById(R.id.rPasswordEt);
        mConfirmPassEt = findViewById(R.id.rConfirmPassEt);
        mRegisterBt = findViewById(R.id.registerBt);

        mRegisterBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerStudent();
            }
        });

    }

    private void registerStudent() {
        // getting data from input field
        getInputData();
        if (validate()) {

            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Student registering.., Please wait.");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();

            mAuth.createUserWithEmailAndPassword(mEmail, mPassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String studentId = mAuth.getCurrentUser().getUid();
                                Student student = new Student(studentId, mName, mEmail, mPhone, mPassword);

                                mStudentRef.child(studentId).setValue(student).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            Intent paymentIntent = new Intent(RegisterActivity.this, PaymentActivity.class);
                                            startActivity(paymentIntent);

                                            Toast.makeText(RegisterActivity.this, "Registration successful.",
                                                    Toast.LENGTH_SHORT).show();
                                            mProgressDialog.dismiss();
                                        }else {
                                            Toast.makeText(RegisterActivity.this, "Student is not added to database.",
                                                    Toast.LENGTH_SHORT).show();

                                            mProgressDialog.dismiss();
                                        }
                                    }
                                });

                            }else {
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                mProgressDialog.dismiss();
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "Registration Failed for not proper validation", Toast.LENGTH_SHORT).show();
        }

    }

    private void getInputData() {
        mName = mNameEt.getEditText().getText().toString();
        mEmail = mEmailEt.getEditText().getText().toString();
        mPhone = mPhoneNoEt.getEditText().getText().toString();
        mPassword = mPasswordEt.getEditText().getText().toString();
        mConfirmPass = mConfirmPassEt.getEditText().getText().toString();
    }

    public boolean validate() {
        boolean valid = true;
        if (mName.isEmpty() || mName.length() > 32) {
            mNameEt.getEditText().setError("Please enter valid name!");
            valid = false;
        }

        if (mEmail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {
            mEmailEt.getEditText().setError("Please enter valid email address!");
            valid = false;
        }

        if (mPhone.isEmpty() || !Patterns.PHONE.matcher(mPhone).matches()) {
            mPhoneNoEt.getEditText().setError("Please enter valid phone number!");
            valid = false;
        }

        if (mPassword.isEmpty()) {
            mPasswordEt.getEditText().setError("Please enter valid password!");
            valid = false;
        }

        if (mConfirmPass.isEmpty() || !mPassword.equals(mConfirmPass)) {
            mConfirmPassEt.getEditText().setError("Password does not matched with previous password!");
            valid = false;
        }

        return valid;

    }
}

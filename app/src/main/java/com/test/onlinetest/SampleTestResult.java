package com.test.onlinetest;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.test.onlinetest.model.ParticipantDetails;

public class SampleTestResult extends AppCompatActivity {

    private TextView mScoreTv, mTotalQTv, mAnsweredQTv;
    private Button mRegisterBtn;

    private DatabaseReference mRootRef;
    private DatabaseReference mSampleAnsRef;

    ParticipantDetails details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_test_result);

        mRootRef = FirebaseDatabase.getInstance().getReference();
        mSampleAnsRef = mRootRef.child("SampleAnswer");

        details = (ParticipantDetails) getIntent().getSerializableExtra("details");

        initView();

        mScoreTv.setText("Your Score:\n"+details.getScore());
        mTotalQTv.setText(details.getTotalQuestion());
        mAnsweredQTv.setText(details.getTotalAnswered());

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent registerIntent = new Intent(SampleTestResult.this, RegisterActivity.class);
                registerIntent.putExtra("details", details);
                startActivity(registerIntent);
                finish();
            }
        });


    }

    private void initView() {

        mScoreTv = findViewById(R.id.sScoreTv);
        mTotalQTv = findViewById(R.id.sTotalQTv);
        mAnsweredQTv = findViewById(R.id.sCorrectAnsTv);
        mRegisterBtn = findViewById(R.id.sRegisterBtn);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mSampleAnsRef.child(details.getId()).removeValue().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });

    }
}

package com.test.onlinetest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.test.onlinetest.model.Question;
import com.test.onlinetest.model.Test;

import java.util.ArrayList;
import java.util.List;

public class AddQuestionActivity extends AppCompatActivity {

    private Toolbar mToolBar;

    private TextInputLayout questionEt, option1Et, option2Et, option3Et, option4Et;
    private EditText mExplanationEt;
    private Button mAddQBtn;
    private String mQuestionTitle, mOption1, mOption2, mOption3, mOption4, mExplanation;
    private int mCorrectAns = 1;

    private RadioGroup mOptionRG;
    private RadioButton mOption1Rd, mOption2Rd, mOption3Rd, mOption4Rd;

    private Test mTest;

    private DatabaseReference mRootRef;
    private DatabaseReference mQuestionsRef;

    private Question mQuestion;
    private List<String> mOptionList;

    private ProgressDialog mProgressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);

        mRootRef = FirebaseDatabase.getInstance().getReference();
        mQuestionsRef = mRootRef.child("Questions");

        mTest = (Test) getIntent().getSerializableExtra("test");
        //initializing view
        initView();


        //getting the value of radio button
        mOptionRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.option1Rd:
                        mCorrectAns = 1;
                        Toast.makeText(AddQuestionActivity.this, mCorrectAns+"", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.option2Rd:
                        mCorrectAns = 2;
                        Toast.makeText(AddQuestionActivity.this, mCorrectAns+"", Toast.LENGTH_SHORT).show();

                        break;
                    case R.id.option3Rd:
                        mCorrectAns = 3;
                        break;
                    case R.id.option4Rd:
                        mCorrectAns = 4;
                        break;


                }
            }
        });


        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Add Question");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent testInfoIntent = new Intent(AddQuestionActivity.this, TestInfoActivity.class);
                testInfoIntent.putExtra("test", mTest);
                startActivity(testInfoIntent);
                finish();
            }
        });



        mAddQBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mProgressDialog = new ProgressDialog(AddQuestionActivity.this);
                mProgressDialog.setMessage("Please wait while we add your question.");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();

                // getInput data
                getInputData();
                mOptionList = new ArrayList<>();
                mOptionList.add(mOption1);
                mOptionList.add(mOption2);
                mOptionList.add(mOption3);
                mOptionList.add(mOption4);

                Toast.makeText(AddQuestionActivity.this, mOptionList.get(1), Toast.LENGTH_SHORT).show();

                String questionId = mQuestionsRef.push().getKey();
                System.out.println(questionId);
                mQuestion = new Question(questionId, mQuestionTitle, mOptionList, mCorrectAns, mExplanation);

                mQuestionsRef.child(mTest.getId()).child(questionId).setValue(mQuestion)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AddQuestionActivity.this, "New Question is created!", Toast.LENGTH_SHORT).show();
                            mProgressDialog.dismiss();
                        } else {
                            Toast.makeText(AddQuestionActivity.this, "New Question Creation failed! try again.", Toast.LENGTH_SHORT).show();
                            mProgressDialog.dismiss();
                        }
                    }
                });
            }
        });


    }

    public void initView () {

        mToolBar = findViewById(R.id.addQToolBar);
        questionEt = findViewById(R.id.questionEt);
        option1Et = findViewById(R.id.option1Et);
        option2Et = findViewById(R.id.option2Et);
        option3Et = findViewById(R.id.option3Et);
        option4Et = findViewById(R.id.option4Et);

        mExplanationEt = findViewById(R.id.explanationEt);

        mOptionRG = findViewById(R.id.addQuestionRg);

        mOption1Rd = findViewById(R.id.option1Rd);
        mOption2Rd = findViewById(R.id.option2Rd);
        mOption3Rd = findViewById(R.id.option3Rd);
        mOption4Rd = findViewById(R.id.option4Rd);
        
        mAddQBtn = findViewById(R.id.addQuestionBT);


    }


    public void getInputData() {

        mQuestionTitle = questionEt.getEditText().getText().toString();
        mOption1 = option1Et.getEditText().getText().toString();
        mOption2 = option2Et.getEditText().getText().toString();
        mOption3 = option3Et.getEditText().getText().toString();
        mOption4 = option4Et.getEditText().getText().toString();

        mExplanation = mExplanationEt.getText().toString();



    }

}

package com.test.onlinetest;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.test.onlinetest.fragments.AnswerDetailsFragment;
import com.test.onlinetest.model.ParticipantDetails;
import com.test.onlinetest.model.Question;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SampleTestActivity extends AppCompatActivity  implements View.OnClickListener {

    private TextView mQTitleTv;
    private RadioGroup mQuestionRg;
    private RadioButton mOption1, mOption2, mOption3, mOption4;

    private ImageButton mNextBtn, mPreviousBtn;
    private Button mFinishBtn;
    private LinearLayout mLinearLayout;

    private List<Question> questionList = new ArrayList<>();
    private List<Button> buttonList = new ArrayList<>();

    private int mQuestionCount;
    private int mScore;
    private int mAnswerCount;

    private String selectedOption;

    private DatabaseReference mRootref;
    private DatabaseReference mSampleAnswer;

    private ProgressDialog mProgressDialog;

    String sampleAnswerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_test);

        mRootref = FirebaseDatabase.getInstance().getReference();
        mSampleAnswer = mRootref.child("SampleAnswer");

        sampleAnswerId = mSampleAnswer.push().getKey().toLowerCase();

        initView();

        initQuestion();


        createAllButton(questionList.size());
        // setting question list

        // setting question list
        setQuestion(0);

        // remain the previous state
        remainState(questionList.get(0).getId());

        //navigate questions
        navigateQuestion();

        // disable the previous button for first question
        mPreviousBtn.setEnabled(false);
        mPreviousBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));


        mNextBtn.setOnClickListener(this);
        mPreviousBtn.setOnClickListener(this);
        mFinishBtn.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setIcon(R.drawable.info);
        builder.setTitle(R.string.info);
        builder.setMessage(R.string.sample_test_message);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });


        AlertDialog dialog = builder.create();
        dialog.show();





    }


    public void initView() {
        mQTitleTv = findViewById(R.id.sQuestionTitleTv);
        mQuestionRg = findViewById(R.id.sRg);
        mOption1 = findViewById(R.id.sOption1);
        mOption2 = findViewById(R.id.sOption2);
        mOption3 = findViewById(R.id.sOption3);
        mOption4 = findViewById(R.id.sOption4);
        mNextBtn = findViewById(R.id.sNextBtn);
        mPreviousBtn = findViewById(R.id.sPreviousBtn);
        mFinishBtn = findViewById(R.id.sFinishBtn);
        mLinearLayout = findViewById(R.id.sBtnLinearLayout);
    }

    public void initQuestion() {
        List<String> options = new ArrayList<>();
        options.add("Red");
        options.add("Blue");
        options.add("Green");
        options.add("White");

        Question question1 = new Question(
                "0",
                "what is the color of blood?",
                options,
                1,
                "Each hemoglobin protein is made up subunits called hemes, which are what give blood its red color"
        );
        questionList.add(question1);

        options = new ArrayList<>();
        options.add("Cell walls");
        options.add("Mitochondria");
        options.add("Ribosomes");
        options.add("Cytoplasm");

        Question question2 = new Question(
                "1",
                "Which of the following is not present in animal cells?",
                options,
                1,
                "Animal cells do not have cell walls. Cell walls are found in plant cells. Other given organelles like Mitochondria, Ribosome and Cytoplasm are present"
        );
        questionList.add(question2);

        options = new ArrayList<>();
        options.add("Iron");
        options.add("Copper");
        options.add("Gold");
        options.add("Silver");

        Question question3 = new Question(
                "2",
                "Which of the following was most probably the first metal to be used in India?",
                options,
                3,
                "The earliest historic evidences of use of metal are of Chalcolithic Age or Stone-Copper Age, which covered the period from 1800 to 800 BC."
        );
        questionList.add(question3);

        options = new ArrayList<>();
        options.add("Kathak");
        options.add("Sattriya");
        options.add("Manipuri");
        options.add("Bhangra");

        Question question4 = new Question(
                "3",
                "Which of the following is not a classical dance of India?",
                options,
                4,
                "The Sangeet Natak Akademi recognizes Bharatnatyam, Kathak, Kuchipudi, Odishi, Kathakali, Sattriya, Manipuri and Mohiniyattam as classical dances in India."
        );
        questionList.add(question4);


        options = new ArrayList<>();
        options.add("Banganapalle");
        options.add("Alphonso");
        options.add("Sindhoora");
        options.add("Red Dacca");

        Question question5 = new Question(
                "4",
                "Which of the following is not a variety of mango?",
                options,
                4,
                "Red Dacca is a variety of banana with reddish-purple skin. Others are varieties of mango found in India."
        );
        questionList.add(question5);

        options = new ArrayList<>();
        options.add("4");
        options.add("7");
        options.add("11");
        options.add("6");

        Question question6 = new Question(
                "5",
                "How many colors are there in a rainbow?",
                options,
                2,
                "No explanation is given for this question"
        );
        questionList.add(question6);



        options = new ArrayList<>();
        options.add("Mumbai");
        options.add("Kolkata");
        options.add("Asam");
        options.add("Tamil");

        Question question7 = new Question(
                "6",
                "Where is the tower of silence situated in India?",
                options,
                1,
                "No explanation is given for this question"
        );
        questionList.add(question7);

        options = new ArrayList<>();
        options.add("Urenus");
        options.add("Venus");
        options.add("Pluto");
        options.add("Earth");

        Question question8 = new Question(
                "7",
                "Which is the brightest planet in our solar system?",
                options,
                2,
                "No explanation is given for this question"
        );
        questionList.add(question8);

        options = new ArrayList<>();
        options.add("80");
        options.add("120");
        options.add("300");
        options.add("206");

        Question question9 = new Question(
                "8",
                "How many bones are there in a human body?",
                options,
                4,
                "No explanation is given for this question"
        );
        questionList.add(question9);

        options = new ArrayList<>();
        options.add("Abul Kalam Azad");
        options.add("Subhas Chandra Bose");
        options.add("Rabindranath Tagore");
        options.add("Mahatma Gandhi");

        Question question10 = new Question(
                "9",
                "Who was the first Asian to be awarded the Nobel Prize?",
                options,
                3,
                "No explanation is given for this question"
        );
        questionList.add(question10);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sNextBtn:

                gotoNextQuestion();

                break;
            case R.id.sPreviousBtn:
                gotoPreviousQuestion();
                break;
            case R.id.sFinishBtn:
                finishQuize();
                break;

        }
    }

    private void finishQuize() {
        gotoDetails();
    }

    private void gotoPreviousQuestion() {

        mQuestionCount --;
        // set previous question
        setQuestion(mQuestionCount);

        remainState(questionList.get(mQuestionCount).getId());

        // handling button disable and enable state
        if (mQuestionCount <= 0) {
            mPreviousBtn.setEnabled(false);
            mPreviousBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

            mNextBtn.setEnabled(true);
            mNextBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));

        }

        mNextBtn.setEnabled(true);
        mNextBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));


    }

    private void gotoNextQuestion() {
        selectedOption = getSelectedOption();

        // save selected option to database
        saveSelectedOption(selectedOption, questionList.get(mQuestionCount).getId());

        mQuestionCount ++;

        if(mQuestionCount <= questionList.size() -1) {

            // set next question
            setQuestion(mQuestionCount);

            // remain the state
            remainState(questionList.get(mQuestionCount).getId());

        }

        // handling button disable and enable state
        if (mQuestionCount > questionList.size() - 1) {
            mNextBtn.setEnabled(false);
            mNextBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

            mPreviousBtn.setEnabled(true);
            mPreviousBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));

        }
        mPreviousBtn.setEnabled(true);
        mPreviousBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));

    }

    public void setQuestion(int listPosition) {


        mQTitleTv.setText(listPosition+1+". "+questionList.get(listPosition).getName());
        mOption1.setText(questionList.get(listPosition).getOptions().get(0));
        mOption2.setText(questionList.get(listPosition).getOptions().get(1));
        mOption3.setText(questionList.get(listPosition).getOptions().get(2));
        mOption4.setText(questionList.get(listPosition).getOptions().get(3));
        // clear the check radio button
        mQuestionRg.clearCheck();

    }

    public String getSelectedOption() {

        if (mOption1.isChecked()) {
            return "1";
        } else if (mOption2.isChecked()) {
            return "2";
        } else if (mOption3.isChecked()) {
            return "3";
        } else if (mOption4.isChecked()){
            return "4";
        }
        return "";

    }

    public void saveSelectedOption(final String selectedOption, final String questionId) {
        mSampleAnswer.child(sampleAnswerId).child(questionId).child("selectedOption").setValue(selectedOption)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            // save the correct status if selected value is correct or not
                            String correctAns = String.valueOf(questionList.get(mQuestionCount-1).getCorrectAns());
//                                Log.d("mcountq ", mQuestionCount+"");
//                                Log.d("correct Ans:", correctAns);
                            if (selectedOption.equals(correctAns)) {

                                mSampleAnswer.child(sampleAnswerId).child(questionId).child("isCorrect")
                                        .setValue("true").addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                    }
                                });

                            } else{
                                mSampleAnswer.child(sampleAnswerId).child(questionId).child("isCorrect")
                                        .setValue("false").addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                    }
                                });
                            }
                        }
                    }
                });
    }


    public void remainState(String questionId) {
        mSampleAnswer.child(sampleAnswerId).child(questionId).child("selectedOption")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            //Log.d("dataSnapshot:", dataSnapshot+"");
                            String selectedAns = dataSnapshot.getValue().toString();


                            switch (selectedAns) {
                                case "1":
                                    mOption1.setChecked(true);
                                    break;
                                case "2":
                                    mOption2.setChecked(true);
                                    break;
                                case "3":
                                    mOption3.setChecked(true);
                                    break;
                                case "4":
                                    mOption4.setChecked(true);
                                    break;
                                default:
                                    mQuestionRg.clearCheck();
                                    break;
                            }
                        } else {
                            mQuestionRg.clearCheck();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        databaseError.toException();
                    }
                });
    }



    public void createAllButton(int listSize) {

        for (int j = 1; j <= listSize; j++ ){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            Button btnTag = new Button(this);
            btnTag.setId(j);
            btnTag.setTag("qBtn"+j);
            btnTag.setGravity(Gravity.CENTER);
            btnTag.setTextColor(ContextCompat.getColor(this, R.color.white));
            btnTag.setBackground(ContextCompat.getDrawable(this, R.drawable.circle_border));
            btnTag.setLayoutParams(params);
            btnTag.getLayoutParams().width = 100;
            btnTag.getLayoutParams().height = 100;
            params.setMargins(0,0, 30, 0);
            btnTag.setText(j+"");
            btnTag.setTextSize(10);
            btnTag.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
            mLinearLayout.addView(btnTag);
        }

        initAllButton();
    }

    public void initAllButton() {
        for (int i = 1; i<= questionList.size(); i++) {
            Button button = findViewById(i);
            buttonList.add(button);
        }
    }


    public void navigateQuestion () {
        getAllAnswerForNavigateButton();
    }


    public void getAllAnswerForNavigateButton() {
        mSampleAnswer.child(sampleAnswerId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int count = 0;
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            String selectedOption = dataSnapshot1.child("selectedOption").getValue().toString();

                            //Log.d("seletected option", selectedOption);
                            if(!(selectedOption.isEmpty())) {
                                Log.d("count", count+"");
                                Button selectedBtn = buttonList.get(count);
                                selectedBtn.setBackground(ContextCompat.getDrawable(SampleTestActivity.this, R.drawable.circle_border_background));
                                selectedBtn.setTextColor(ContextCompat.getColor(SampleTestActivity.this, R.color.white));
                            }
                            count ++;

                        }

                        // click listener for all button
                        for (int i = 0; i < questionList.size(); i++) {
                            final int temp = i;
                            buttonList.get(i).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    mQuestionCount = temp;

                                    //set question again for update question count
                                    setQuestion(mQuestionCount);

                                    //first clear the check list
                                    mQuestionRg.clearCheck();

                                    // and then remain the state
                                    remainState(questionList.get(mQuestionCount).getId());

                                    if (mQuestionCount != questionList.size() - 1) {
                                        mNextBtn.setEnabled(true);
                                        mNextBtn.setBackgroundColor(ContextCompat.getColor(SampleTestActivity.this, R.color.colorPrimary));
                                    }

                                    // disable the right button
                                    if (mQuestionCount > questionList.size() - 1) {
                                        mNextBtn.setEnabled(false);
                                        mNextBtn.setBackgroundColor(ContextCompat.getColor(SampleTestActivity.this, R.color.colorPrimaryDark));
                                    }

                                    if (mQuestionCount == 0) {
                                        mPreviousBtn.setEnabled(false);
                                        mPreviousBtn.setBackgroundColor(ContextCompat.getColor(SampleTestActivity.this, R.color.colorPrimaryDark));
                                    } else {
                                        mPreviousBtn.setEnabled(true);
                                        mPreviousBtn.setBackgroundColor(ContextCompat.getColor(SampleTestActivity.this, R.color.colorPrimary));
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        databaseError.toException();
                    }
                });
    }


    public void gotoDetails() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Finishing...");
        mProgressDialog.show();
        mSampleAnswer.child(sampleAnswerId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.getValue() != null) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                String isCorrect = dataSnapshot1.child("isCorrect").getValue()+"";
                                String selectedOption = dataSnapshot1.child("selectedOption").getValue().toString();
                                if (isCorrect.equals("true")) {
                                    mScore ++;
                                }
                                //counting total answer
                                if (!selectedOption.equals("")) {
                                    mAnswerCount++;
                                }
                            }
                        }


                        ParticipantDetails details = new ParticipantDetails(sampleAnswerId,
                                "true", mScore+"", mAnswerCount+"",
                                questionList.size()+"");

                        Intent testResultIntent = new Intent(SampleTestActivity.this, SampleTestResult.class);
                        testResultIntent.putExtra("details", details);
                        mProgressDialog.dismiss();
                        startActivity(testResultIntent);
                        finish();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        mProgressDialog.dismiss();
                        databaseError.toException();
                    }
                });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        mSampleAnswer.child(sampleAnswerId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });

    }
}

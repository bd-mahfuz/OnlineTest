package com.test.onlinetest.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.test.onlinetest.R;
import com.test.onlinetest.StartTestActivity;
import com.test.onlinetest.model.Question;
import com.test.onlinetest.model.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewAnswerFragment extends Fragment implements View.OnClickListener {

    private TextView mQTitleTv, mExplanationTv;
    private RadioGroup mQuestionRg;
    private RadioButton option1RB, option2RB, option3RB, option4RB;
    private ImageButton mNextBtn, mPreviousBtn;
    private Button mFinishBtn;
    private LinearLayout mLinearLayout;

    private View view;
    private ProgressDialog mProgressDialog;

    private List<Question> questionList = new ArrayList<>();
    private List<Question> answerList = new ArrayList<>();
    private List<Button> buttonList = new ArrayList<>();

    private Test mTest;

    private DatabaseReference mRootRef;
    private DatabaseReference mQuestionRef;
    private DatabaseReference mAnswerRef;
    private DatabaseReference mParticipants;

    private FirebaseAuth mAuth;

    private int mQuestionCount;
    private int mAnswerCount;

    private String selectedOption = "";

    private int mScore;

    private Context context;
    private StartTestActivity activity;

    private Bundle bundle;


    public ReviewAnswerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        activity = (StartTestActivity) getActivity();

        mRootRef = FirebaseDatabase.getInstance().getReference();
        mQuestionRef = mRootRef.child("Questions");
        mAnswerRef = mRootRef.child("Answers");
        mParticipants = mRootRef.child("Participants");
        mAuth = FirebaseAuth.getInstance();

        bundle = getArguments();

        if (bundle!=null) {
            mTest = (Test) bundle.getSerializable("test");
        }

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_review_answer, container, false);
        this.view = view;

        // initialize all view
        initView(view);

        mNextBtn.setOnClickListener(this);
        mPreviousBtn.setOnClickListener(this);

        return view;
    }

    public void initView(View view) {
        mQTitleTv = view.findViewById(R.id.sQuestionTitleTv);
        mExplanationTv = view.findViewById(R.id.explanationTv);
        mQuestionRg = view.findViewById(R.id.rQuestionRg);
        option1RB = view.findViewById(R.id.rOption1);
        option2RB = view.findViewById(R.id.rOption2);
        option3RB = view.findViewById(R.id.rOption3);
        option4RB = view.findViewById(R.id.rOption4);

        mNextBtn = view.findViewById(R.id.rNextBtn);
        mPreviousBtn = view.findViewById(R.id.rPreviousBtn);

        mLinearLayout = view.findViewById(R.id.rBtnLinearLayout);



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rNextBtn:

                gotoNextQuestion();

                break;
            case R.id.rPreviousBtn:
                gotoPreviousQuestion();
                break;


        }
    }


    @Override
    public void onStart() {
        super.onStart();

        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setMessage("Loading");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        mQuestionRef.child(mTest.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                questionList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Question question = dataSnapshot1.getValue(Question.class);
                    questionList.add(question);
                }

                createAllButton(questionList.size());
                // setting question list
                setQuestion(0);

                // remain the previous state
                remainState(questionList.get(0).getId());

                //navigate questions
                navigateQuestion();


                // disable the previous button for first question
                mPreviousBtn.setEnabled(false);
                mPreviousBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));

                mProgressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.toException();
                mProgressDialog.dismiss();
            }
        });



    }

    public void replaceFragment(Fragment destFragment)
    {
        // First get FragmentManager object.
        FragmentManager fragmentManager = activity.getSupportFragmentManager();

        // Begin Fragment transaction.
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the layout holder with the required Fragment object.
        fragmentTransaction.replace(R.id.startTestActivity, destFragment);

        // Commit the Fragment replace action.
        fragmentTransaction.commit();
    }


    private void gotoPreviousQuestion() {

        mQuestionCount --;
        // set previous question
        setQuestion(mQuestionCount);

        remainState(questionList.get(mQuestionCount).getId());

        // handling button disable and enable state
        if (mQuestionCount <= 0) {
            mPreviousBtn.setEnabled(false);
            mPreviousBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));

            mNextBtn.setEnabled(true);
            mNextBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));

        }

        mNextBtn.setEnabled(true);
        mNextBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));


    }


    private void gotoNextQuestion() {

        /*selectedOption = getSelectedOption();

        // save selected option to database
        saveSelectedOption(selectedOption, questionList.get(mQuestionCount).getId());*/

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
            mNextBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));

            mPreviousBtn.setEnabled(true);
            mPreviousBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));

        }
        mPreviousBtn.setEnabled(true);
        mPreviousBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));

    }

    public String getSelectedOption() {

        if (option1RB.isChecked()) {
            return "1";
        } else if (option2RB.isChecked()) {
            return "2";
        } else if (option3RB.isChecked()) {
            return "3";
        } else if (option4RB.isChecked()){
            return "4";
        }
        return "";

    }

    public void setQuestion(int listPosition) {


        mQTitleTv.setText(listPosition+1+". "+questionList.get(listPosition).getName());
        mExplanationTv.setText(questionList.get(listPosition).getExplanation());
        option1RB.setText(questionList.get(listPosition).getOptions().get(0));
        option2RB.setText(questionList.get(listPosition).getOptions().get(1));
        option3RB.setText(questionList.get(listPosition).getOptions().get(2));
        option4RB.setText(questionList.get(listPosition).getOptions().get(3));
        // clear the check radio button
        mQuestionRg.clearCheck();

    }


    public void saveSelectedOption(final String selectedOption, final String questionId) {
        mAnswerRef.child(mAuth.getCurrentUser().getUid())
                .child(mTest.getId()).child(questionId).child("selectedOption").setValue(selectedOption)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            // save the correct status if selected value is correct or not
                            String correctAns = String.valueOf(questionList.get(mQuestionCount-1).getCorrectAns());
//                                Log.d("mcountq ", mQuestionCount+"");
//                                Log.d("correct Ans:", correctAns);
                            if (selectedOption.equals(correctAns)) {

                                mAnswerRef.child(mAuth.getCurrentUser().getUid())
                                        .child(mTest.getId()).child(questionId).child("isCorrect")
                                        .setValue("true").addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                    }
                                });

                            } else{
                                mAnswerRef.child(mAuth.getCurrentUser().getUid())
                                        .child(mTest.getId()).child(questionId).child("isCorrect")
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
        mAnswerRef.child(mAuth.getCurrentUser().getUid())
                .child(mTest.getId()).child(questionId).child("selectedOption")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            //Log.d("dataSnapshot:", dataSnapshot+"");
                            String selectedAns = dataSnapshot.getValue().toString();


                            switch (selectedAns) {
                                case "1":
                                    option1RB.setChecked(true);
                                    break;
                                case "2":
                                    option2RB.setChecked(true);
                                    break;
                                case "3":
                                    option3RB.setChecked(true);
                                    break;
                                case "4":
                                    option4RB.setChecked(true);
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
            Button btnTag = new Button(context);
            btnTag.setId(j);
            btnTag.setTag("qBtn"+j);
            btnTag.setGravity(Gravity.CENTER);
            btnTag.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
            btnTag.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.circle_border));
            btnTag.setLayoutParams(params);
            btnTag.getLayoutParams().width = 100;
            btnTag.getLayoutParams().height = 100;
            params.setMargins(0,0, 30, 0);
            btnTag.setText(j+"");
            btnTag.setTextSize(10);
            btnTag.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            mLinearLayout.addView(btnTag);
        }

        initAllButton(view);
    }

    public void initAllButton(View view) {
        for (int i = 1; i<= questionList.size(); i++) {
            Button button = view.findViewWithTag("qBtn"+i);
            buttonList.add(button);
        }
    }

    public void navigateQuestion () {
        getAllAnswerForNavigateButton();
    }

    public void getAllAnswerForNavigateButton() {
        mAnswerRef.child(mAuth.getCurrentUser().getUid()).child(mTest.getId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int count = 0;
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            String selectedOption = dataSnapshot1.child("selectedOption").getValue().toString();

                            //Log.d("seletected option", selectedOption);
                            if(!(selectedOption.isEmpty())) {
                                Log.d("count", count+"");
                                Button selectedBtn = buttonList.get(count);
                                selectedBtn.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.circle_border_background));
                                selectedBtn.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
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
                                        mNextBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                                    }

                                    // disable the right button
                                    if (mQuestionCount > questionList.size() - 1) {
                                        mNextBtn.setEnabled(false);
                                        mNextBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                                    }

                                    if (mQuestionCount == 0) {
                                        mPreviousBtn.setEnabled(false);
                                        mPreviousBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                                    } else {
                                        mPreviousBtn.setEnabled(true);
                                        mPreviousBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
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
}

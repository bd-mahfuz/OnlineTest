package com.test.onlinetest.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.test.onlinetest.AddQuestionActivity;
import com.test.onlinetest.R;
import com.test.onlinetest.adapter.QuestionAdapter;
import com.test.onlinetest.model.Question;
import com.test.onlinetest.model.Test;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionsFragment extends Fragment {

    private TextView mQMsg;
    private RecyclerView mRecyclerView;
    private ProgressDialog mProgressDialog;

    private Test mTest;

    private FloatingActionButton mAddQFab;

    private QuestionAdapter mQuestionAdapter;

    private DatabaseReference mRootRef;
    private DatabaseReference mQuestionRef;
    private List<Question> questionList = new ArrayList<>();


    public QuestionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_questions, container, false);

        Bundle bundle = getArguments();

        if (bundle!=null) {
            mTest = (Test) bundle.getSerializable("test");
        }

        mQMsg = view.findViewById(R.id.noQMsg);
        mAddQFab = view.findViewById(R.id.questionFAB);
        mRecyclerView = view.findViewById(R.id.questionRv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mAddQFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent questionIntent = new Intent(getActivity(), AddQuestionActivity.class);
                questionIntent.putExtra("test", mTest);
                startActivity(questionIntent);
            }
        });


        mRootRef = FirebaseDatabase.getInstance().getReference();
        mQuestionRef = mRootRef.child("Questions");

        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setMessage("Loading...");
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

                if (questionList.size() == 0) {
                    mQMsg.setVisibility(View.VISIBLE);
                    mProgressDialog.dismiss();
                } else {
                    mQuestionAdapter = new QuestionAdapter(getActivity(), questionList);
                    mRecyclerView.setAdapter(mQuestionAdapter);
                    mProgressDialog.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.getMessage();
                mProgressDialog.dismiss();
            }
        });



        return view;
    }

}

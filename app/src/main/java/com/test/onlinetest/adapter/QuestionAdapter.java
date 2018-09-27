package com.test.onlinetest.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.test.onlinetest.R;
import com.test.onlinetest.model.Question;

import java.util.List;

/**
 * Created by mahfuz on 9/19/18.
 */

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {

    private Context context;
    private List<Question> questionList;


    public QuestionAdapter(Context context, List<Question> questionList) {
        this.context = context;
        this.questionList = questionList;
    }

    @NonNull
    @Override
    public QuestionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.layout_questions_list,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionAdapter.ViewHolder holder, int position) {

        int correctAns = questionList.get(position).getCorrectAns();

        holder.questionTv.setText(position+1+". "+questionList.get(position).getName());
        holder.option1Rd.setText("a) "+questionList.get(position).getOptions().get(0));
        holder.option2Rd.setText("b) "+questionList.get(position).getOptions().get(1));
        holder.option3Rd.setText("c) "+questionList.get(position).getOptions().get(2));
        holder.option4Rd.setText("d) "+questionList.get(position).getOptions().get(3));

        switch (correctAns) {
            case 1:
                holder.option1Rd.setChecked(true);
                break;
            case 2:
                holder.option2Rd.setChecked(true);
                break;
            case 3:
                holder.option3Rd.setChecked(true);
                break;
            case 4:
                holder.option4Rd.setChecked(true);
                break;
        }


    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView questionTv;
        RadioButton option1Rd, option2Rd, option3Rd, option4Rd;

        View view;

        public ViewHolder(View itemView) {
            super(itemView);

            view = itemView;

            questionTv = itemView.findViewById(R.id.questionTv);
            option1Rd = itemView.findViewById(R.id.showOption1);
            option2Rd = itemView.findViewById(R.id.showOption2);
            option3Rd = itemView.findViewById(R.id.showOption3);
            option4Rd = itemView.findViewById(R.id.showOption4);
        }
    }
}

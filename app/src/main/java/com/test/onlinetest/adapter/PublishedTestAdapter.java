package com.test.onlinetest.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.test.onlinetest.R;
import com.test.onlinetest.StartTestActivity;
import com.test.onlinetest.model.Test;

import java.util.List;

/**
 * Created by mahfuz on 9/21/18.
 */

public class PublishedTestAdapter extends RecyclerView.Adapter<PublishedTestAdapter.ViewHolder> {
    
    private Context context;
    private List<Test> testList;

    private DatabaseReference mRootRef;
    private DatabaseReference mParticipantRef;
    private FirebaseAuth mAuth;

    public PublishedTestAdapter(Context context, List<Test> testList) {
        this.context = context;
        this.testList = testList;
    }

    @NonNull
    @Override
    public PublishedTestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        
        View view = LayoutInflater.from(context).inflate(R.layout.layout_published_test_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PublishedTestAdapter.ViewHolder holder, final int position) {
        holder.testTitleTv.setText(testList.get(position).getTitle());
        holder.quizFlTv.setText(testList.get(position).getTitle().charAt(0)+"");
        //holder.marksTv.setText("Marks: "+testList.get(position).getTotalMarks()+"");


        if (testList.get(position).isPublish()) {
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    Intent startTestIntent = new Intent(context, StartTestActivity.class);
                    startTestIntent.putExtra("test", testList.get(position));
                    context.startActivity(startTestIntent);

                    /*mParticipantRef.child(testList.get(position).getId())
                            .child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null) {
                                String isFinished = dataSnapshot.child("isFinished").getValue().toString();
                                if (isFinished.equals("true")) {
                                    Intent startTestIntent = new Intent(context, StartTestActivity.class);
                                    startTestIntent.putExtra("test", testList.get(position));
                                    context.startActivity(startTestIntent);
                                } else {
                                    Intent startTestIntent = new Intent(context, StartTestActivity.class);
                                    startTestIntent.putExtra("test", testList.get(position));
                                    context.startActivity(startTestIntent);
                                }
                            } else {
                                Intent startTestIntent = new Intent(context, StartTestActivity.class);
                                startTestIntent.putExtra("test", testList.get(position));
                                context.startActivity(startTestIntent);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            databaseError.toException();
                        }
                    });*/

                    
                }
            });
        } else {
            holder.startBtn.setVisibility(View.VISIBLE);
            holder.barTv.setVisibility(View.VISIBLE);
            holder.startBtn.setEnabled(false);
        }


    }

    @Override
    public int getItemCount() {
        return testList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View view;
        
        TextView testTitleTv, quizFlTv, barTv;
        Button startBtn;

        public ViewHolder(View itemView) {
            super(itemView);

            view = itemView;
            
            testTitleTv = itemView.findViewById(R.id.titleTv);
            startBtn = itemView.findViewById(R.id.startBtn);
            quizFlTv = itemView.findViewById(R.id.quizFlTV);
            barTv = itemView.findViewById(R.id.barTv);


            mAuth = FirebaseAuth.getInstance();
            mRootRef = FirebaseDatabase.getInstance().getReference();
            mParticipantRef = mRootRef.child("Participants");

        }
    }
}

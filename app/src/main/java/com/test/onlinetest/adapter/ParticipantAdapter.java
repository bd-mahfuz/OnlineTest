package com.test.onlinetest.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
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
import com.test.onlinetest.R;
import com.test.onlinetest.model.ParticipantDetails;
import com.test.onlinetest.model.Student;

import java.util.List;

/**
 * Created by mahfuz on 9/26/18.
 */

public class ParticipantAdapter extends RecyclerView.Adapter<ParticipantAdapter.ViewHolder> {

    private Context context;
    private List<ParticipantDetails> participantDetailsList;


    private DatabaseReference mStudentRef;
    private DatabaseReference mRootRef;

    public ParticipantAdapter(Context context, List<ParticipantDetails> participantDetailsList) {
        this.context = context;
        this.participantDetailsList = participantDetailsList;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.layout_participant, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        mStudentRef.child(participantDetailsList.get(position).getId())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Student student = dataSnapshot.getValue(Student.class);
                holder.nameTv.setText(student.getName());
                holder.nameFlTv.setText(student.getName().charAt(0)+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        
        
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "click", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return participantDetailsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View view;
        TextView nameFlTv, nameTv;

        public ViewHolder(View itemView) {
            super(itemView);

            mRootRef = FirebaseDatabase.getInstance().getReference();
            mStudentRef = mRootRef.child("Students");

            view = itemView;

            nameFlTv = itemView.findViewById(R.id.nameFLTv);
            nameTv = itemView.findViewById(R.id.sNameTv);
        }
    }
}

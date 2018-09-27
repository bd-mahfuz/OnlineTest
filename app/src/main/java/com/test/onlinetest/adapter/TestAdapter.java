package com.test.onlinetest.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.test.onlinetest.AdminPanelActivity;
import com.test.onlinetest.R;
import com.test.onlinetest.TestInfoActivity;
import com.test.onlinetest.fragments.EditTestFragment;
import com.test.onlinetest.model.Test;
import com.test.onlinetest.utility.OnlineTestUtility;

import java.util.List;

/**
 * Created by mahfuz on 9/12/18.
 */

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.ViewHolder>  {

    private Context context;
    private List<Test> testList;

    private DatabaseReference mRootRef;
    private DatabaseReference mTestRef;

    public TestAdapter(Context context, List<Test> testList) {
        this.context = context;
        this.testList = testList;
    }

    @NonNull
    @Override
    public TestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.layout_test_list,parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TestAdapter.ViewHolder holder, final int position) {


        final Test test = testList.get(position);

        holder.titleTv.setText(testList.get(position).getTitle());
        //holder.feeTv.setText("Fees: "+String.valueOf(testList.get(position).getFees())+"$");
        holder.startDateTv.setText("Created Date:"+ test.getCreateDate());


        if (test.isPublish()) {
            holder.publishBtn.setText("Unpublished");
        } else {
            holder.publishBtn.setText("Published");
        }


        holder.publishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("You are publishing the Test.");
                builder.setMessage("Are you want to publish the Test");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        publishTest(test, holder);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        // handle click action on item
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, testList.get(position).getTitle()+"", Toast.LENGTH_SHORT).show();
                CharSequence options[] = new CharSequence[]{"Details", "Edit Test", "Delete Test"};

                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Choose One..");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, final int i) {
                        switch (i) {
                            case 0:

                                Intent testInfoIntent = new Intent(context, TestInfoActivity.class);
                                testInfoIntent.putExtra("test", test);
                                context.startActivity(testInfoIntent);
                                break;
                            case 1:
                                replaceFragment(new EditTestFragment());
                                break;
                            case 2:
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                                builder1.setTitle("Are you want to delete the test?");
                                builder1.setIcon(R.drawable.warning);
                                builder1.setMessage("If you delete, all question are also be deleted that are related to the test.");
                                builder1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        deleteTest(testList.get(position));
                                        dialogInterface.dismiss();
                                    }
                                });

                                builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });

                                AlertDialog alertDialog = builder1.create();
                                alertDialog.show();
                                break;
                        }
                    }
                });

                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return testList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView titleTv, startDateTv;
        Button publishBtn;
        View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;

            titleTv = itemView.findViewById(R.id.titleTv);
            startDateTv = itemView.findViewById(R.id.startDateTv);
            publishBtn = itemView.findViewById(R.id.publishBtn);
        }


    }


    public void replaceFragment(Fragment destFragment)
    {
        AdminPanelActivity activity = (AdminPanelActivity) context;

        // First get FragmentManager object.
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        // Begin Fragment transaction.
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the layout holder with the required Fragment object.
        fragmentTransaction.replace(R.id.adminFragmentContainer, destFragment);

        fragmentTransaction.addToBackStack(null);

        // Commit the Fragment replace action.
        fragmentTransaction.commit();
    }


    public void deleteTest(Test test) {
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mTestRef = mRootRef.child("Tests");

        mTestRef.child(test.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Test is deleted successfully.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Test is not deleted successfully! please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void publishTest(Test test, final TestAdapter.ViewHolder holder) {
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mTestRef = mRootRef.child("Tests");

        Log.d("test id:", test.getId());

        if (test.isPublish()) {
            mTestRef.child(test.getId()).child("publish").setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        holder.publishBtn.setText("Published");
                        Toast.makeText(context, "The Test is Unpublished Successfully.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "The Test is not Unpublished. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            mTestRef.child(test.getId()).child("publish").setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        holder.publishBtn.setText("Unpublished");
                        Toast.makeText(context, "The Test is published Successfully.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "The Test is not Published. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}

package com.test.onlinetest;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.test.onlinetest.adapter.TestAdapter;
import com.test.onlinetest.fragments.AddTestFragment;
import com.test.onlinetest.fragments.TestsFragment;
import com.test.onlinetest.model.Test;

import java.util.ArrayList;
import java.util.List;

public class AdminPanelActivity extends AppCompatActivity {
    private Toolbar mToolbar;

    private FirebaseAuth mAuth;

    private List<Test> testList = new ArrayList<>();

    private RecyclerView mTestRv;
    private TextView mTestMsg;
    private FloatingActionButton mTestFab;

    private DatabaseReference mRootRef;
    private DatabaseReference mTestsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        mAuth = FirebaseAuth.getInstance();


        mToolbar = findViewById(R.id.adminToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Admin Panel");

        replaceFragment(new TestsFragment());





    }

    public void replaceFragment(Fragment destFragment)
    {
        // First get FragmentManager object.
        FragmentManager fragmentManager = this.getSupportFragmentManager();

        // Begin Fragment transaction.
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the layout holder with the required Fragment object.
        fragmentTransaction.replace(R.id.adminFragmentContainer, destFragment);
        // Commit the Fragment replace action.
        fragmentTransaction.commit();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.logoutMenu:
                mAuth.signOut();
                sendToLoginActivity();
                break;
        }

        return true;
    }


    private void sendToLoginActivity() {
        Intent loginIntent = new Intent(AdminPanelActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }
}

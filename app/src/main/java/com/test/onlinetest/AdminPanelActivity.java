package com.test.onlinetest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

public class AdminPanelActivity extends AppCompatActivity {
    private Toolbar mToolbar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        mToolbar = findViewById(R.id.adminToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Admin Panel");

        mAuth = FirebaseAuth.getInstance();

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

package com.test.onlinetest;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.test.onlinetest.adapter.SectionsPagerAdapter;
import com.test.onlinetest.fragments.ParticipantFragment;
import com.test.onlinetest.fragments.QuestionsFragment;
import com.test.onlinetest.fragments.TestDetailsFragment;
import com.test.onlinetest.model.Test;

public class TestInfoActivity extends AppCompatActivity {

    private TabLayout mTestInfoTab;
    private ViewPager mTestInfoPager;

    private Toolbar mToolBar;

    private Test mTest;

    private SectionsPagerAdapter mSectionPageAdapter;

    private TestDetailsFragment detailsFragment;
    private QuestionsFragment questionFragment;
    private ParticipantFragment participantFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_info);

        Test test = (Test) getIntent().getSerializableExtra("test");




        Bundle bundle = new Bundle();
        bundle.putSerializable("test", test);


        detailsFragment = new TestDetailsFragment();
        detailsFragment.setArguments(bundle);

        questionFragment = new QuestionsFragment();
        questionFragment.setArguments(bundle);

        participantFragment = new ParticipantFragment();
        participantFragment.setArguments(bundle);

        mSectionPageAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mSectionPageAdapter.addFragment(detailsFragment, getString(R.string.detail_title));
        mSectionPageAdapter.addFragment(questionFragment, getString(R.string.question_title));
        mSectionPageAdapter.addFragment(participantFragment, getString(R.string.participant_title));


        mTestInfoTab = findViewById(R.id.testInfoTab);
        mTestInfoPager = findViewById(R.id.testInfoViewPager);
        mToolBar = findViewById(R.id.testInfoToolBar);



        mTestInfoPager.setAdapter(mSectionPageAdapter);

        mTestInfoTab.setupWithViewPager(mTestInfoPager);

        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle(test.getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }
}

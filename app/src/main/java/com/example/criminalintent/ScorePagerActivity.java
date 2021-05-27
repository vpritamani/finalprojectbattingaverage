package com.example.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.List;
import java.util.UUID;

public class ScorePagerActivity extends
        AppCompatActivity {

    private static final String EXTRA_CRIME_ID =
            "com.bignerdranch.android.criminalintent.crime_id";


    private ViewPager mViewPager;
    private List<Score> mScores;

    public static Intent newIntent(Context packageContext, UUID crimeId) {
        Intent intent = new Intent(packageContext,
                ScorePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }

    UUID scoreId = (UUID) getIntent()
            .getSerializableExtra(EXTRA_CRIME_ID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_pager);
        mViewPager = (ViewPager)
                findViewById(R.id.crime_view_pager);
        mScores = ScoreList.get(this).getScores();
        FragmentManager fragmentManager =
                getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
                                          @Override
                                          public Fragment getItem(int position) {
                                              Score score = mScores.get(position);
                                              return
                                                      ScoreFragment.newInstance(score.getId());
                                          }
                                          @Override
                                          public int getCount() {
                                              return mScores.size();
                                          }
                                      });
        for (int i = 0; i < mScores.size(); i++) {
            if
            (mScores.get(i).getId().equals(scoreId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}

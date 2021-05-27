package com.example.finalprojectbattingaverage;

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

    private static final String EXTRA_SCORE_ID =
            "com.example.android.finalprojectbattingaverage.score_id";


    private ViewPager mViewPager;
    private List<Score> mScores;

    UUID scoreId = (UUID) getIntent()
            .getSerializableExtra(EXTRA_SCORE_ID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_pager);
        mViewPager = (ViewPager) findViewById(R.id.crime_view_pager);
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

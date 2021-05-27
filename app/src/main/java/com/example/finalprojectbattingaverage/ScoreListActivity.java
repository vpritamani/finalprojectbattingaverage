package com.example.finalprojectbattingaverage;

import androidx.fragment.app.Fragment;

public class ScoreListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new ScoreListFragment();
    }
}
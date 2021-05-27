package com.example.criminalintent;

import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;

import java.util.UUID;

public class ScoreActivity extends SingleFragmentActivity {

    private static final String EXTRA_SCORE_ID = "com.bignerdranch.android.criminalintent.crime_id";

    public static Intent newIntent (Context packageContext, UUID scoreId){
        Intent intent = new Intent(packageContext, ScoreActivity.class);
        intent.putExtra(EXTRA_SCORE_ID, scoreId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        UUID scoreId = (UUID) getIntent().getSerializableExtra(EXTRA_SCORE_ID);
        return ScoreFragment.newInstance(scoreId);
    }

}
package com.example.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.fragment.app.Fragment;


import androidx.fragment.app.FragmentManager;

import java.util.UUID;

import static android.widget.CompoundButton.*;
import static java.lang.String.valueOf;

public class ScoreFragment extends Fragment {

    private static final String ARG_SCORE_ID = "crime_id";

    private EditText mScoreField;
    private Score mScore;
    private CheckBox mSolvedCheckBox;

    public static ScoreFragment newInstance(UUID crimeId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_SCORE_ID, crimeId);

        ScoreFragment fragment = new ScoreFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_SCORE_ID);
        mScore = ScoreList.get(getActivity()).getScore(crimeId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //mCrime = new Crime();
        View v = inflater.inflate(R.layout.fragment_score, container,
                false);
                mScoreField = (EditText) v.findViewById(R.id.score_value);
        mScoreField.setText(valueOf(mScore.getTitle()));
        mScoreField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count,
                    int after) {

            }
            @Override
            public void onTextChanged(
                    CharSequence s, int start, int
                    before, int count) {
                mScore.setTitle(Integer.valueOf(s.toString()));
            }
            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

        mSolvedCheckBox = (CheckBox) v.findViewById(R.id.out_or_not_value);
        mSolvedCheckBox.setChecked(mScore.isOut());
        mSolvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                mScore.setOut(isChecked);
            }
        });
        FragmentManager fm = getFragmentManager();
        Fragment fragment =
                fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = new ScoreFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container,
                            fragment)
                    .commit();
        }
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int
            resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
    }


}
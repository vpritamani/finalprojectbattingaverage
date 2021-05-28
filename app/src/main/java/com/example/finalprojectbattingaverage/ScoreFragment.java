package com.example.finalprojectbattingaverage;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;


import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;

import static android.widget.CompoundButton.*;
import static java.lang.String.valueOf;

public class ScoreFragment extends Fragment {

    private static final String ARG_SCORE_ID = "score_id";

    private EditText mScoreField;
    private EditText mOppositionField;
    private Score mScore;
    private CheckBox mSolvedCheckBox;

    public static ScoreFragment newInstance(UUID scoreId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_SCORE_ID, scoreId);

        ScoreFragment fragment = new ScoreFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID scoreId = (UUID) getArguments().getSerializable(ARG_SCORE_ID);
        mScore = ScoreList.get(getActivity()).getScore(scoreId);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_score, container, false);
        mScoreField = (EditText) v.findViewById(R.id.score_value);
        mScoreField.setText(valueOf(mScore.getRuns()));
        mScoreField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().length() == 0){
                    mScore.setRuns(0);
                }
                else {
                    mScore.setRuns(Integer.valueOf(s.toString()));
                }
            }
            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

        mOppositionField = (EditText) v.findViewById(R.id.opposition_name);
        mOppositionField.setText(mScore.getOpposition());
        mOppositionField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mScore.setOpposition(s.toString());
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
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mScore.setOut(isChecked);
            }
        });
        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = new ScoreFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
        return v;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_score, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.show_average_in_editing_a_score:
                ScoreList scoreList = ScoreList.get(getActivity());
                String averageToShow;
                if(scoreList.findTotalOuts(scoreList) == 0){
                    averageToShow = "NA - No Scores Are Out!";
                }
                else {
                    averageToShow = new DecimalFormat("#.##").format(scoreList.findAverage(scoreList));
                }
                Toast toast = Toast.makeText(getContext(), averageToShow, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM, 0, 0);
                toast.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
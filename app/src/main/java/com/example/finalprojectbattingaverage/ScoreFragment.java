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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import androidx.fragment.app.FragmentManager;

import java.text.DecimalFormat;
import java.util.UUID;

import static android.widget.CompoundButton.*;
import static java.lang.String.valueOf;

public class ScoreFragment extends Fragment {

    private static final String ARG_SCORE_ID = "score_id";

    // all the buttons/edit texts/check boxes in the fragment
    private EditText mScoreField;
    private EditText mBallsFacedField;
    private EditText mOppositionField;
    private Button mConfirmScoreButton;
    private Button mDeleteScoreButton;
    private CheckBox mOutOrNotCheckBox;

    private Score mScore;
    private UUID mUUID;

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

        // set the mUUID to the scoreId so that can be accessed to
        // delete the item when one presses 'delete crime'
        mUUID = scoreId;
        mScore = ScoreList.get(getActivity()).getScore(scoreId);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_score, container, false);

        // Sets runs to whatever is in the scorefield as input by user
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

        // Sets balls faced to whatever is in the ballsfacedfield as input by user
        mBallsFacedField = (EditText) v.findViewById(R.id.balls_faced_value);
        mBallsFacedField.setText(valueOf(mScore.getBallsFaced()));
        mBallsFacedField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().length() == 0){
                    mScore.setBallsFaced(0);
                }
                else {
                    mScore.setBallsFaced(Integer.valueOf(s.toString()));
                }
            }
            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

        // Sets opposition to what is in the oppositionfield as input by user
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

        // Sets boolean of out to true or false depending on if the check box is checked
        mOutOrNotCheckBox = (CheckBox) v.findViewById(R.id.out_or_not_value);
        mOutOrNotCheckBox.setChecked(mScore.getIfOut());
        mOutOrNotCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
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

        // adds the score to the list and returns to the home/list activity
        mConfirmScoreButton = (Button) v.findViewById(R.id.ConfirmScoreButton);
        mConfirmScoreButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent myIntent = new Intent(getContext(), ScoreListActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });

        // deletes the score from the list and returns to the home/list activity
        mDeleteScoreButton = (Button) v.findViewById(R.id.DeleteScoreButton);
        mDeleteScoreButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                ScoreList scoreListFromWhichToDelete = ScoreList.get(getActivity());
                scoreListFromWhichToDelete.deleteItem(mUUID);
                Intent returnIntent = new Intent(getContext(), ScoreListActivity.class);
                startActivityForResult(returnIntent, 0);
            }
        });

        return v;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        /*
         deletes the arrow button from the top left of the menu so that the user
         just has to click the 'confirm score' button which is much more intuitive
        */
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setHomeButtonEnabled(false);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(false);

        inflater.inflate(R.menu.fragment_score, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.show_average_in_editing_a_score:
                ScoreList scoreList = ScoreList.get(getActivity());
                String averageToShow;

                /*
                 if there are no outs finding an average would compute to an error
                 (as you would divide by 0), therefore display a different text
                 if an average is being computed with no outs, otherwise show the
                 average in proper decimal format with only two decimal points so
                 that the toast is not filled with decimal points
                */
                if(scoreList.findTotalOuts(scoreList) == 0){
                    averageToShow = "NA - No Scores Are Out!";
                }
                else {
                    averageToShow = new DecimalFormat("#.##").format(scoreList.findAverage(scoreList));
                }

                // create the toast and display it at the bottom of the screen
                Toast toast = Toast.makeText(getContext(), averageToShow, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM, 0, 0);
                toast.show();

                return true;
            case R.id.show_strike_rate_in_editing_score:
                // show strike rate of the entire list (not of the individual score)
                ScoreList scoreListStrikeRate = ScoreList.get(getActivity());
                String strikeRateToShow;

                // similar to showaverage with outs, a strike rate is not computable with no balls
                // faced, therefore display a different text if a ball has not been faced yet
                if(scoreListStrikeRate.findTotalBallsFaced(scoreListStrikeRate) == 0){
                    strikeRateToShow = "NA - You haven't faced a ball yet!";
                }
                else{
                    strikeRateToShow = new DecimalFormat("###.##").format(scoreListStrikeRate.findStrikeRate(scoreListStrikeRate));
                }

                Toast strikeRateToast = Toast.makeText(getContext(), strikeRateToShow, Toast.LENGTH_LONG);
                strikeRateToast.setGravity(Gravity.BOTTOM, 0, 0);
                strikeRateToast.show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
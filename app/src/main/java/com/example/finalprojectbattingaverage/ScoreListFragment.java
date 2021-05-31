package com.example.finalprojectbattingaverage;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.String.valueOf;
import android.view.Gravity;

public class ScoreListFragment extends Fragment {

    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";
    private RecyclerView mScoreRecyclerView;
    private ScoreAdapter mAdapter;
    private boolean mSubtitleVisible;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }
        View view = inflater.inflate(R.layout.fragment_score_list, container, false);
        mScoreRecyclerView = (RecyclerView) view.findViewById(R.id.score_recycler_view);
        mScoreRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;

    }

    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_score_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if (mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_score:
                Score score = new Score();
                ScoreList.get(getActivity()).addScore(score);
                Intent intent = ScoreActivity.newIntent(getActivity(), score.getId());
                startActivity(intent);
                updateUI();
                return true;
            case R.id.show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            case R.id.show_average:
                ScoreList scoreList = ScoreList.get(getActivity());
                String averageToShow;
                if(scoreList.findTotalOuts(scoreList) == 0){
                    averageToShow = "NA - Add a Score in Which You Were Out!";
                }
                else {
                    double average = scoreList.findAverage(scoreList);
                    averageToShow = new DecimalFormat("#.##").format(average);
                    if(average < 20.0){
                        averageToShow += " - I'm guessing you're a bowler because you can't bat!";
                    }
                    else if(average < 40.0){
                        averageToShow += " - I'm guessing you're an allrounder because you aren't good or bad";
                    }
                    else{
                        averageToShow += " - I'm guessing you're a batsman because you're pretty good at batting!";
                    }
                }
                Toast toast = Toast.makeText(getContext(), averageToShow, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM, 0, 0);
                toast.show();
                return true;
            case R.id.show_strike_rate:
                ScoreList scoreListStrikeRate = ScoreList.get(getActivity());
                String strikeRateToShow;
                if(scoreListStrikeRate.findTotalBallsFaced(scoreListStrikeRate) == 0){
                    strikeRateToShow = "NA - You haven't faced a ball yet!";
                }
                else {
                    double strikeRate = scoreListStrikeRate.findStrikeRate(scoreListStrikeRate);
                    strikeRateToShow = new DecimalFormat("###.##").format(strikeRate);
                    if(strikeRate <= 75.0){
                        strikeRateToShow += " - You're batting as if you're playing tests!";
                    }
                    else if(strikeRate <= 125.0){
                        strikeRateToShow += " - It seems like you're playing ODI!";
                    }
                    else{
                        strikeRateToShow += " - It seems like you're playing t20!";
                    }
                }
                Toast toastForStrikeRate = Toast.makeText(getContext(), strikeRateToShow, Toast.LENGTH_LONG);
                toastForStrikeRate.setGravity(Gravity.BOTTOM, 0, 0);
                toastForStrikeRate.show();
                return true;
            case R.id.show_oppositions:
                ScoreList scoreListCurrent = ScoreList.get(getActivity());
                String toDisplay = "NA";
                List<String> addedNames = new ArrayList<>();

                if(scoreListCurrent.getScores().size() > 0){
                    for(int i = 0; i < scoreListCurrent.getScores().size(); i++){
                        String toAdd = scoreListCurrent.getScores().get(i).getOpposition();
                        if(toAdd.equals("")){
                            toAdd = "Undefined opposition";
                        }
                        if(!addedNames.contains(toAdd)){
                            addedNames.add(toAdd);
                        }

                    }
                    Collections.sort(addedNames);
                    for(int i = 0; i < addedNames.size(); i++){
                        if(i == 0){
                            toDisplay = addedNames.get(i);
                        }
                        else{
                            toDisplay += ", " + addedNames.get(i);
                        }
                    }
                }
                Toast toastForOpposition = Toast.makeText(getContext(), toDisplay, Toast.LENGTH_LONG);
                toastForOpposition.setGravity(Gravity.BOTTOM, 0,0);
                toastForOpposition.show();
                return true;
            case R.id.show_average_by_opposition:
                ScoreList scoreListForListingAverage = ScoreList.get(getActivity());
                String toShow = "NA - Add A Score!";
                List<String> oppositionsAlreadyListed = new ArrayList<>();
                if(scoreListForListingAverage.getScores().size() > 0){
                    ArrayList<String> toAdd = new ArrayList<>();
                    toShow = "";
                    for(int j = 0; j < scoreListForListingAverage.getScores().size(); j++){
                        String current = scoreListForListingAverage.getScores().get(j).getOpposition();
                        if(!oppositionsAlreadyListed.contains(current)){
                            oppositionsAlreadyListed.add(current);
                            ScoreList scoreOfThisOpposition = new ScoreList(getContext());
                            for(int k = 0; k < scoreListForListingAverage.getScores().size(); k++){
                                if(scoreListForListingAverage.getScores().get(k).getOpposition().toLowerCase().equals(current.toLowerCase())){
                                    scoreOfThisOpposition.addScore(scoreListForListingAverage.getScores().get(k));
                                }
                            }
                            if(current.equals("")){
                                current = "Undefined Opposition";
                            }
                            if(scoreOfThisOpposition.findTotalOuts(scoreOfThisOpposition) == 0){
                                toAdd.add(current + ": No Outs Against This Opposition, ");
                            }
                            else {
                                toAdd.add(current + ": " + new DecimalFormat("#.##").format(scoreOfThisOpposition.findAverage(scoreOfThisOpposition)) + ", ");
                            }
                        }
                    }
                    Collections.sort(toAdd);
                    for(int i = 0; i < toAdd.size(); i++){
                        toShow += toAdd.get(i);
                    }
                    toShow = toShow.substring(0, toShow.length() - 2);
                }
                Toast test = Toast.makeText(getContext(), toShow, Toast.LENGTH_LONG);
                test.setGravity(Gravity.BOTTOM, 0,0);
                test.show();
                return true;
            case R.id.show_strike_rate_by_opposition:
                ScoreList scoreListForListingStrikeRateOpposition = ScoreList.get(getActivity());
                String toDisplayStrikeRate = "NA - Add A Score!";
                List<String> oppositionsAlreadyAdded = new ArrayList<>();
                if(scoreListForListingStrikeRateOpposition.getScores().size() > 0){
                    ArrayList<String> toAdd = new ArrayList<>();
                    toDisplayStrikeRate = "";
                    for(int j = 0; j < scoreListForListingStrikeRateOpposition.getScores().size(); j++){
                        String current = scoreListForListingStrikeRateOpposition.getScores().get(j).getOpposition();
                        if(!oppositionsAlreadyAdded.contains(current)){
                            oppositionsAlreadyAdded.add(current);
                            ScoreList scoreOfThisOpposition = new ScoreList(getContext());
                            for(int k = 0; k < scoreListForListingStrikeRateOpposition.getScores().size(); k++){
                                if(scoreListForListingStrikeRateOpposition.getScores().get(k).getOpposition().toLowerCase().equals(current.toLowerCase())){
                                    scoreOfThisOpposition.addScore(scoreListForListingStrikeRateOpposition.getScores().get(k));
                                }
                            }
                            if(current.equals("")){
                                current = "Undefined Opposition";
                            }
                            if(scoreOfThisOpposition.findTotalBallsFaced(scoreOfThisOpposition) == 0){
                                toAdd.add(current + ": No Balls Faced Against This Opposition, ");
                            }
                            else {
                                toAdd.add(current + ": " + new DecimalFormat("###.##").format(scoreOfThisOpposition.findStrikeRate(scoreOfThisOpposition)) + ", ");
                            }
                        }
                    }
                    Collections.sort(toAdd);
                    for(int i = 0; i < toAdd.size(); i++){
                        toDisplayStrikeRate += toAdd.get(i);
                    }
                    toDisplayStrikeRate = toDisplayStrikeRate.substring(0, toDisplayStrikeRate.length() - 2);
                }
                Toast showingSRByOpp = Toast.makeText(getContext(), toDisplayStrikeRate, Toast.LENGTH_LONG);
                showingSRByOpp.setGravity(Gravity.BOTTOM, 0,0);
                showingSRByOpp.show();
                return true;
            case R.id.clear_list:
                ScoreList.get(getActivity()).clearList();
                Fragment fragmentOfScoreList = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                FragmentTransaction betweenFragmentTransaction = getFragmentManager().beginTransaction();
                betweenFragmentTransaction.detach(fragmentOfScoreList);
                betweenFragmentTransaction.attach(fragmentOfScoreList);
                betweenFragmentTransaction.commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateSubtitle() {
        ScoreList scoreList = ScoreList.get(getActivity());
        int scoreCount = scoreList.getScores().size();
        String subtitle = getString(R.string.subtitle_format, scoreCount);

        if (!mSubtitleVisible) {
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    private class ScoreHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mScoreTextView;
        private TextView mOppositionTextView;

        private Score mScore;

        public ScoreHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_score, parent, false));
            itemView.setOnClickListener(this);
            mScoreTextView = (TextView) itemView.findViewById(R.id.score_value);
            mOppositionTextView = (TextView) itemView.findViewById(R.id.opposition_value);
        }

        public void bind(Score score){
            mScore = score;
            String outOrNot;
            if(score.getIfOut()){
                outOrNot = " out";
            }
            else{
                outOrNot = " not out";
            }
            mScoreTextView.setText(valueOf(mScore.getRuns()) + outOrNot + " off " + valueOf(mScore.getBallsFaced()) + " balls");
            mOppositionTextView.setText("Opposition: " + mScore.getOpposition());
        }

        @Override
        public void onClick(View view){
            Intent intent = ScoreActivity.newIntent(getActivity(), mScore.getId());
            startActivity(intent);
        }
    }

    private class ScoreAdapter extends RecyclerView.Adapter<ScoreHolder> {
        private List<Score> mScores;
        public ScoreAdapter(List<Score> scores) {
            mScores = scores;
        }

        @Override
        public ScoreHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new ScoreHolder(layoutInflater, parent);
        }
        @Override
        public void onBindViewHolder(ScoreHolder holder, int position) {
            Score score = mScores.get(position);
            holder.bind(score);
        }
        @Override
        public int getItemCount() {
            return mScores.size();
        }

    }

    private void updateUI() {
        ScoreList scoreList = ScoreList.get(getActivity());
        List<Score> scores = scoreList.getScores();
        if(mAdapter == null) {
            mAdapter = new ScoreAdapter(scores);
            mScoreRecyclerView.setAdapter(mAdapter);
        }
        else{ mAdapter.notifyDataSetChanged(); }
        updateSubtitle();
    }

}
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

                // average isn't computable if total outs is 0
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

                // make and show toast with average and commentary/interpretation of the average
                Toast toast = Toast.makeText(getContext(), averageToShow, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM, 0, 0);
                toast.show();

                return true;
            case R.id.show_strike_rate:
                ScoreList scoreListStrikeRate = ScoreList.get(getActivity());
                String strikeRateToShow;

                // strike rate isn't computable is total balls is 0
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

                // make and show toast of strike rate and commentary/interpretation of the strike rate
                Toast toastForStrikeRate = Toast.makeText(getContext(), strikeRateToShow, Toast.LENGTH_LONG);
                toastForStrikeRate.setGravity(Gravity.BOTTOM, 0, 0);
                toastForStrikeRate.show();

                return true;
            case R.id.show_oppositions:
                ScoreList scoreListCurrent = ScoreList.get(getActivity());
                List<String> addedNames = new ArrayList<>();

                // toDisplay should be NA if there are no scores
                String toDisplay = "NA";

                if(scoreListCurrent.getScores().size() > 0){
                    for(int i = 0; i < scoreListCurrent.getScores().size(); i++){
                        String toAdd = scoreListCurrent.getScores().get(i).getOpposition();
                        if(toAdd.equals("")){
                            toAdd = "Undefined opposition";
                        }

                        // prevent adding an opposition twice in the text of the toast
                        if(!addedNames.contains(toAdd)){
                            addedNames.add(toAdd);
                        }

                    }

                    // sort it alphabetically
                    Collections.sort(addedNames);

                    for(int i = 0; i < addedNames.size(); i++){
                        if(i == 0){
                            // first item/string added to the list would replace the 'NA' with the first opposition
                            toDisplay = addedNames.get(i);
                        }
                        else{
                            // add a comma then add the next opposition
                            toDisplay += ", " + addedNames.get(i);
                        }
                    }
                }

                // make and show toast of all oppositions
                Toast toastForOpposition = Toast.makeText(getContext(), toDisplay, Toast.LENGTH_LONG);
                toastForOpposition.setGravity(Gravity.BOTTOM, 0,0);
                toastForOpposition.show();

                return true;
            case R.id.show_average_by_opposition:
                ScoreList scoreListForListingAverage = ScoreList.get(getActivity());
                List<String> oppositionsAlreadyListed = new ArrayList<>();

                // If there are no scores then the text should be to tell the user to add a score
                String toShow = "NA - Add A Score!";

                if(scoreListForListingAverage.getScores().size() > 0){

                    // set toShow to an empty string that will be added to now that we know there are scores in the list
                    toShow = "";

                    ArrayList<String> toAdd = new ArrayList<>();

                    for(int j = 0; j < scoreListForListingAverage.getScores().size(); j++){
                        String current = scoreListForListingAverage.getScores().get(j).getOpposition();

                        if(!oppositionsAlreadyListed.contains(current)){
                            /*
                             if we have not already computed the average for this opposition,
                             go through the list and compute the average for this opposition
                             and make sure we don't do this opposition again
                            */
                            oppositionsAlreadyListed.add(current);
                            ScoreList scoreOfThisOpposition = new ScoreList(getContext());
                            for(int k = 0; k < scoreListForListingAverage.getScores().size(); k++){
                                if(scoreListForListingAverage.getScores().get(k).getOpposition().toLowerCase().equals(current.toLowerCase())){
                                    scoreOfThisOpposition.addScore(scoreListForListingAverage.getScores().get(k));
                                }
                            }

                            // if user does not set the opposition name to anything, show it in the list as 'undefined opposition'
                            if(current.equals("")){
                                current = "Undefined Opposition";
                            }

                            // average is not computable if there are no outs
                            if(scoreOfThisOpposition.findTotalOuts(scoreOfThisOpposition) == 0){
                                toAdd.add(current + ": No Outs Against This Opposition, ");
                            }
                            else {
                                toAdd.add(current + ": " + new DecimalFormat("#.##").format(scoreOfThisOpposition.findAverage(scoreOfThisOpposition)) + ", ");
                            }
                        }
                    }

                    // sort alphabetically, then add to the toShow String
                    Collections.sort(toAdd);
                    for(int i = 0; i < toAdd.size(); i++){
                        toShow += toAdd.get(i);
                    }

                    // as we added a ", " after each item, delete the ", " from the
                    // end of the String (which shows the last average computed)
                    toShow = toShow.substring(0, toShow.length() - 2);
                }

                // create and show toast of average by opposition, sorted alphabetically by opposition
                Toast test = Toast.makeText(getContext(), toShow, Toast.LENGTH_LONG);
                test.setGravity(Gravity.BOTTOM, 0,0);
                test.show();

                return true;
            case R.id.show_strike_rate_by_opposition:
                // similar to the show average by opposition, but instead of showing the
                // average, we display the strike rate
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

                // create and show toast of strike rate by opposition, sorted alphabetically by opposition
                Toast showingSRByOpp = Toast.makeText(getContext(), toDisplayStrikeRate, Toast.LENGTH_LONG);
                showingSRByOpp.setGravity(Gravity.BOTTOM, 0,0);
                showingSRByOpp.show();

                return true;
            case R.id.clear_list:
                // first clear the list of scores that we have
                ScoreList.get(getActivity()).clearList();

                // update the fragment so that the display now shows the updated list (of no scores)
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

        // if we aren't supposed to show the amount of scores, set subtitle
        // to null so it doesn't display the amount of shows
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

            // show the score in the format, for example of 43 runs off 64 balls while being out
            // as "43 out off 64 balls", and also show the opposition below that score
            mScoreTextView.setText(valueOf(mScore.getRuns()) + outOrNot + " off " + valueOf(mScore.getBallsFaced()) + " balls");
            mOppositionTextView.setText("Opposition: " + mScore.getOpposition());
        }

        // if a score is clicked, open an activity of that score
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
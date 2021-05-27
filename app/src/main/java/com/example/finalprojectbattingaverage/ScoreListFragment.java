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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.valueOf;
import android.view.Gravity;

public class ScoreListFragment extends Fragment {

    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";
    private RecyclerView mScoreRecyclerView;
    private CrimeAdapter mAdapter;
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
            case R.id.show_oppositions:
                ScoreList scoreList1 = ScoreList.get(getActivity());
                String toDisplay = "NA";
                int undefinedOppositionCount = 0;
                List<String> addedNames = new ArrayList<>();

                if(scoreList1.getScores().size() > 0){
                    for(int i = 0; i < scoreList1.getScores().size(); i++){
                        String toAdd = scoreList1.getScores().get(i).getOpposition();
                        if(toAdd == ""){
                            undefinedOppositionCount++;
                            toAdd = "Undefined opposition " + undefinedOppositionCount;
                        }
                        if(i == 0){
                            toDisplay = toAdd;
                            addedNames.add(toAdd);
                        }
                        else{
                            if(!addedNames.contains(toAdd)){
                                addedNames.add(toAdd);
                                toDisplay += ", " + toAdd;
                            }
                        }
                    }
                }

                Toast toastForOpposition = Toast.makeText(getContext(), toDisplay, Toast.LENGTH_LONG);
                toastForOpposition.setGravity(Gravity.BOTTOM, 0,0);
                toastForOpposition.show();
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
            if(score.isOut()){
                outOrNot = " out";
            }
            else{
                outOrNot = " not out";
            }
            mScoreTextView.setText(valueOf(mScore.getRuns()) + outOrNot);
            mOppositionTextView.setText("Opposition: " + mScore.getOpposition());
        }

        @Override
        public void onClick(View view){
            Intent intent = ScoreActivity.newIntent(getActivity(), mScore.getId());
            startActivity(intent);
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<ScoreHolder> {
        private List<Score> mScores;
        public CrimeAdapter(List<Score> scores) {
            mScores = scores;
        }

        @Override
        public ScoreHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new ScoreHolder(layoutInflater, parent);
        }
        @Override
        public void onBindViewHolder(ScoreHolder holder, int position) {
            Score crime = mScores.get(position);
            holder.bind(crime);
        }
        @Override
        public int getItemCount() {
            return mScores.size();
        }

    }

    private void updateUI() {
        ScoreList scoreList = ScoreList.get(getActivity());
        List<Score> crimes = scoreList.getScores();
        if(mAdapter == null) {
            mAdapter = new CrimeAdapter(crimes);
            mScoreRecyclerView.setAdapter(mAdapter);
        }
        else{ mAdapter.notifyDataSetChanged(); }
        updateSubtitle();
    }

}
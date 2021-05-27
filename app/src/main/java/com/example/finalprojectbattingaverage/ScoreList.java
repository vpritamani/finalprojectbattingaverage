package com.example.finalprojectbattingaverage;

import android.content.Context;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ScoreList {

    private static ScoreList sScoreList;

    private Map<UUID, Score> mScores;

    public static ScoreList get(Context context) {
        if (sScoreList == null) {
            sScoreList = new ScoreList(context);
        }
        return sScoreList;
    }

    private ScoreList(Context context) {
        mScores = new LinkedHashMap<>();
    }

    public void addScore(Score c) {
        mScores.put(c.getId(), c);
    }

    public List<Score> getScores() {
        return new ArrayList<>(mScores.values());
    }


    public Score getScore(UUID id) {
        if (mScores.containsKey(id)){
            return mScores.get(id);
        }
        else {
            return null;
        }
    }

    public int findTotalOuts(ScoreList scoreList){
        int amountOfOuts = 0;
        for(int i = 0; i < scoreList.getScores().size(); i++){
            if(scoreList.getScores().get(i).isOut()){
                amountOfOuts++;
            }
        }
        return amountOfOuts;
    }

    public int findTotalRuns(ScoreList scoreList){
        int totalRuns = 0;
        for(int i = 0; i < scoreList.getScores().size(); i++){
            totalRuns += scoreList.getScores().get(i).getRuns();
        }
        return totalRuns;
    }

    public double findAverage(ScoreList scoreList){
        return (double) findTotalRuns(scoreList) / (double) findTotalOuts(scoreList);
    }

}
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

    // constructor
    public ScoreList(Context context) {
        mScores = new LinkedHashMap<>();
    }

    // get the entire list/create a list and return it
    public static ScoreList get(Context context) {
        if (sScoreList == null) {
            sScoreList = new ScoreList(context);
        }
        return sScoreList;
    }

    // add score method
    public void addScore(Score c) {
        mScores.put(c.getId(), c);
    }

    // get methods
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

    // clear list and delete item methods
    public void clearList(){
        mScores.clear();
    }
    public void deleteItem(UUID idToDelete){
        mScores.remove(idToDelete);
    }

    // find methods that go through entire list and return the total outs/runs/balls
    public int findTotalOuts(ScoreList scoreList){
        int amountOfOuts = 0;
        for(int i = 0; i < scoreList.getScores().size(); i++){
            if(scoreList.getScores().get(i).getIfOut()){
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
    public int findTotalBallsFaced(ScoreList scoreList){
        int totalballsFaced = 0;
        for(int i = 0; i < scoreList.getScores().size(); i++){
            totalballsFaced += scoreList.getScores().get(i).getBallsFaced();
        }
        return totalballsFaced;
    }

    // find average/strike rate methods simply use the above helper methods to return average/SR
    public double findAverage(ScoreList scoreList){
        return (double) findTotalRuns(scoreList) / (double) findTotalOuts(scoreList);
    }
    public double findStrikeRate(ScoreList scoreList){
        return 100.0 * (double) findTotalRuns(scoreList) / (double) findTotalBallsFaced(scoreList);
    }
}
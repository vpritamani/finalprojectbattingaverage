package com.example.finalprojectbattingaverage;

import java.util.UUID;

public class Score {

    /*
     create variables for unique ID for each score,
     and variables for runs, out, and oppositions
     which will need to be displayed
    */
    private final UUID mId;
    private int mRuns;
    private boolean mOut;
    private String mOpposition;
    private int mBallsFaced;

    /*
     Constructor - set an ID, and set default values that
     would not impact average each time you add a score -
     adding 0 runs and 0 outs has no impact on the average
     that would be computed
    */
    public Score() {
        mId = UUID.randomUUID();
        mRuns = 0;
        mOut = false;
        mOpposition = "";
        mBallsFaced = 0;
    }

    // get methods
    public UUID getId() {
        return mId;
    }
    public int getRuns() {
        return mRuns;
    }
    public boolean getIfOut() {
        return mOut;
    }
    public String getOpposition(){
        return mOpposition;
    }
    public int getBallsFaced(){
        return mBallsFaced;
    }

    // set methods
    public void setRuns(int runs) {
        mRuns = runs;
    }
    public void setOpposition(String opposition){
        mOpposition = opposition;
    }
    public void setOut(boolean out) {
        mOut = out;
    }
    public void setBallsFaced(int ballsFaced){
        mBallsFaced = ballsFaced;
    }
}

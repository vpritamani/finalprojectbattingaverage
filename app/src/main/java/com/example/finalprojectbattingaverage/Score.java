package com.example.finalprojectbattingaverage;

import java.util.UUID;

public class Score {

    private final UUID mId;
    private int mRuns;
    private boolean mOut;


    public Score() {
        mId = UUID.randomUUID();
        mRuns = 0;
    }


    public UUID getId() {
        return mId;
    }
    public int getRuns() {
        return mRuns;
    }
    public void setRuns(int runs) {
        mRuns = runs;
    }

    public boolean isOut() {
        return mOut;
    }
    public void setOut(boolean out) {
        mOut = out;
    }   
}

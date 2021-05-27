package com.example.criminalintent;

import java.util.UUID;

public class Score {
    //figured out how to clone, commit and push
    private UUID mId;
    private int mTitle;
    private boolean mOut;


    public Score() {
        mId = UUID.randomUUID();
        mTitle = 0;
    }


    public UUID getId() {
        return mId;
    }
    public int getTitle() {
        return mTitle;
    }
    public void setTitle(int title) {
        mTitle = title;
    }

    public boolean isOut() {
        return mOut;
    }
    public void setOut(boolean out) {
        mOut = out;
    }   
}

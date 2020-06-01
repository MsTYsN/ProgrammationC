package com.projet.programmationenc;

import android.widget.ImageButton;

public class CardViewExercices {
    private int imageResource;
    private String mText,mQuiz,mEx1,mEx2;
    private boolean selected;

    public CardViewExercices(int imageResource, String mText, String mQuiz, String mEx1, String mEx2, boolean selected) {
        this.imageResource = imageResource;
        this.mText = mText;
        this.mQuiz = mQuiz;
        this.mEx1 = mEx1;
        this.mEx2 = mEx2;
        this.selected = selected;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getmText() {
        return mText;
    }

    public String getmQuiz() {
        return mQuiz;
    }

    public String getmEx1() {
        return mEx1;
    }

    public String getmEx2() {
        return mEx2;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}

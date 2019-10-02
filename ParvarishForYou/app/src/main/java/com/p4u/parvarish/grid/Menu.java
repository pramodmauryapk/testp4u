package com.p4u.parvarish.grid;


import android.graphics.drawable.Drawable;

public class Menu {


    private Drawable mImg;
    private String mName;
    private int mPosition;

    public Menu(Drawable mImg, String mName, int mPosition) {
        this.mImg=mImg;
        this.mName = mName;
        this.mPosition = mPosition;
    }
    Drawable getmImg() {
        return mImg;
    }

    public void setmImg(Drawable mImg) {
        this.mImg = mImg;
    }
    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        mName = mName;
    }

    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int number) {
        mPosition = mPosition;
    }


}

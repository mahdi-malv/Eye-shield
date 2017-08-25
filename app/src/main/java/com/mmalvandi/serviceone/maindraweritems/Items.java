package com.mmalvandi.serviceone.maindraweritems;
/* Creator: Mahdi on 7/15/2017. */

public class Items {
    private int picResId;
    private String text;

    public Items(int picResId, String text) {
        this.picResId = picResId;
        this.text = text;
    }

    int getPicResId() {
        return picResId;
    }

    public void setPicResId(int picResId) {
        this.picResId = picResId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

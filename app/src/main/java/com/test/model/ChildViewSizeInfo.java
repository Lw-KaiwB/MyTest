package com.test.model;

import android.graphics.Matrix;

public class ChildViewSizeInfo {
    private int width;
    private int height;
    private int marginLeft;
    private int marginTop;
    private int marginRight;
    private int marginBottom;
    private boolean isOutScreen;
    private String text;

    public ChildViewSizeInfo(int width, int height, boolean isOutScreen, int marginLeft, int marginTop, int marginRight, int marginBottom,String s) {
        this.width = width;
        this.height = height;
        this.isOutScreen = isOutScreen;
        this.marginLeft = marginLeft;
        this.marginTop = marginTop;
        this.marginRight = marginRight;
        this.marginBottom = marginBottom;
        this.text = s;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isOutScreen() {
        return isOutScreen;
    }

    public void setOutScreen(boolean outScreen) {
        isOutScreen = outScreen;
    }

    public int getMarginLeft() {
        return marginLeft;
    }

    public void setMarginLeft(int marginLeft) {
        this.marginLeft = marginLeft;
    }

    public int getMarginTop() {
        return marginTop;
    }

    public void setMarginTop(int marginTop) {
        this.marginTop = marginTop;
    }

    public int getMarginRight() {
        return marginRight;
    }

    public void setMarginRight(int marginRight) {
        this.marginRight = marginRight;
    }

    public int getMarginBottom() {
        return marginBottom;
    }

    public void setMarginBottom(int marginBottom) {
        this.marginBottom = marginBottom;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}


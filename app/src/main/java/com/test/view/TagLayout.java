package com.test.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.test.model.ChildViewSizeInfo;

import java.util.ArrayList;

public class TagLayout extends ViewGroup {

    private ArrayList<Rect> childRects;
    private ArrayList<ChildViewSizeInfo> childViewSizeInfos;
    private ArrayList<ChildViewSizeInfo> tempChildViewSizeInfos;

    public TagLayout(Context context) {
        super(context);
    }

    public TagLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TagLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        childViewSizeInfos = new ArrayList<>();
        tempChildViewSizeInfos = new ArrayList<>();
        childRects = new ArrayList<>();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = getChildCount();
        int parentWidth = getMeasuredWidth();
        tempChildViewSizeInfos.clear();
        int usedWidth = 0;
        int usedHeight = 0;
        for (int i = 0; i < size; i++) {
            View childView = getChildAt(i);
            measureChildWithMargins(childView, widthMeasureSpec, 0, heightMeasureSpec, 0);
            MarginLayoutParams marginLayoutParams = (MarginLayoutParams) childView.getLayoutParams();
            int width = childView.getMeasuredWidth() + childView.getPaddingLeft() + childView.getPaddingRight();
            int height = childView.getMeasuredHeight() + childView.getPaddingTop() + childView.getPaddingBottom();
            int marginLeft = marginLayoutParams.leftMargin;
            int marginTop = marginLayoutParams.topMargin;
            int marginRight = marginLayoutParams.rightMargin;
            int marginBottom = marginLayoutParams.bottomMargin;
            boolean isFullScreenWidth = ((width + marginLeft + marginRight) >= parentWidth);
            TextView textView = (TextView) childView;
            String s = textView.getText().toString();
            ChildViewSizeInfo info = new ChildViewSizeInfo(width, height, isFullScreenWidth, marginLeft, marginTop, marginRight, marginBottom, s);

            if (isFullScreenWidth) {
                if (tempChildViewSizeInfos.size() > 0) {
                    usedHeight = countChildRect(tempChildViewSizeInfos, parentWidth, usedHeight);
                }
                childRects.add(new Rect(marginLeft, usedHeight + marginTop, parentWidth - marginRight, usedHeight + marginTop + height));
                usedHeight += (marginTop + marginBottom + height);
                tempChildViewSizeInfos.clear();
            } else {
                usedWidth += (width + marginLeft + marginRight);
                if (usedWidth <= parentWidth) {
                    tempChildViewSizeInfos.add(info);
                } else {
                    if (tempChildViewSizeInfos.size() > 0) {
                        usedHeight = countChildRect(tempChildViewSizeInfos, parentWidth, usedHeight);
                    }
                    tempChildViewSizeInfos.clear();
                    tempChildViewSizeInfos.add(info);
                    usedWidth = (width + marginLeft + marginRight);
                }
            }
        }
        if (tempChildViewSizeInfos.size() > 0) {
            countChildRect(tempChildViewSizeInfos, parentWidth, usedHeight);
        }
    }

    private int countChildRect(ArrayList<ChildViewSizeInfo> infos, int parentWidth, int usedHeight) {
        int allChildWidth = 0;
        int maxHeight = 0;
        int childLineWidth = 0;
        for (int i = 0; i < infos.size(); i++) {
            ChildViewSizeInfo info = infos.get(i);
            int width = info.getWidth();
            int height = info.getHeight();
            int l = info.getMarginLeft();
            int t = info.getMarginTop();
            int r = info.getMarginRight();
            int b = info.getMarginBottom();
            allChildWidth += (l + width + r);
            maxHeight = Math.max((height + t + b), maxHeight);
        }
        int hPadding = (parentWidth - allChildWidth) / 2;
        childLineWidth += hPadding;
        for (int i = 0; i < infos.size(); i++) {
            ChildViewSizeInfo info = infos.get(i);
            int width = info.getWidth();
            int height = info.getHeight();
            int marginLeft = info.getMarginLeft();
            int marginTop = info.getMarginTop();
            int marginRight = info.getMarginRight();
            int marginBottom = info.getMarginBottom();
            int vPadding = (maxHeight - (height + marginTop + marginBottom)) / 2;

            int rl = childLineWidth + marginLeft;
            int rt = usedHeight + vPadding;
            int rr = rl + width;
            int rb = rt + height;
            childRects.add(new Rect(rl, rt, rr, rb));

            childLineWidth = (rr + marginRight);
        }
        usedHeight += maxHeight;
        return usedHeight;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (childRects.size() > 0) {
            for (int i = 0; i < getChildCount(); i++) {
                Rect rect = childRects.get(i);
                View view = getChildAt(i);
                view.layout(rect.left, rect.top, rect.right, rect.bottom);
            }
        }

    }


    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}

package com.test.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.test.mytest.R;
import com.test.unit.Unit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2017/11/21.
 * 这是7天数据柱状图,主要用于“我的一天”
 */

public class ChartCylinderView extends View {
    private int mGoalNum = 10000;//用户设置的目标值
    //步数单位标识
    private final static int UNIT_STEP_TAG = 0;
    //公里单位标识
    private final static int UNIT_KM_TAG = 1;
    //米单位标识
    private final static int UNIT_M_TAG = 2;
    //完成目标顶部，底部颜色 ,未达到目标顶部，底部颜色，目标提示颜色
    private int mAchieveTopColor, mAchieveBottomColor, mUnAchieveTopColor, mUnAchieveBottomColor, mGoalColor;
    //柱状图标题
    private String mTitleText;
    //柱状图单位类型，0表示步数，1表示KM，2表示M，具体还要根据公英制转换为英尺，英寸
    private int mUnitType;

    //是否是公制单位
    private boolean isMetric = true;
    //坐标轴颜色
    private int mAxisColor;
    //刻度字体颜色
    private int mScaleTextColor;
    //标题的高度
    private float mTitleHeight = 0.0f;

    private Rect mBound;

    private Paint mPaint;
    private CustomGestureDetector mCustomGestureDetector;
    private boolean isLongPress = false;

    private List<Integer> mStepList = new ArrayList<>();
    private String[] mDate;
    private int mLeftMaxValue = Integer.MIN_VALUE;
    private int mLeftMinValue = Integer.MAX_VALUE;
    private int mTouchIndex = -1;

    public interface TouchModeListener {
        void change(String date, String leftValue, String rightValue);

        void onClick();
    }

    private TouchModeListener mTouchModeListener;

    public void setOnTouchListener(TouchModeListener listener) {
        this.mTouchModeListener = listener;
    }

    public ChartCylinderView(Context context) {
        this(context, null);
    }

    public ChartCylinderView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChartCylinderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.chart_cylinder, defStyleAttr, 0);
        mAchieveTopColor = typedArray.getColor(R.styleable.chart_cylinder_achieve_top_color, getResources().getColor(R.color.chart_cylinder_achieve_top_color));
        mAchieveBottomColor = typedArray.getColor(R.styleable.chart_cylinder_achieve_bottom_color, getResources().getColor(R.color.chart_cylinder_achieve_bottom_color));
        mUnAchieveTopColor = typedArray.getColor(R.styleable.chart_cylinder_unachieve_top_color, getResources().getColor(R.color.chart_cylinder_unachieve_top_color));
        mUnAchieveBottomColor = typedArray.getColor(R.styleable.chart_cylinder_unachieve_bottom_color, getResources().getColor(R.color.chart_cylinder_unachieve_bottom_color));
        mGoalColor = typedArray.getColor(R.styleable.chart_cylinder_goal_color, getResources().getColor(R.color.chart_cylinder_goal_color));
        mTitleText = getResources().getString(typedArray.getResourceId(R.styleable.chart_cylinder_chart_title_text, -1));
        mUnitType = typedArray.getInt(R.styleable.chart_cylinder_unit_type, 0);
        typedArray.recycle();

        mAxisColor = getResources().getColor(R.color.chart_axis_color);
        mScaleTextColor = getResources().getColor(R.color.chart_scale_text_color);

        if (mUnitType == UNIT_STEP_TAG) {
            mTitleText = mTitleText + " (" + getResources().getString(R.string.unit_step) + ")";
        } else if (mUnitType == UNIT_KM_TAG) {
            mTitleText = mTitleText + " (" + getResources().getString(R.string.unit_km) + ")";
        } else {
            mTitleText = mTitleText + " (" + getResources().getString(R.string.unit_height) + ")";
        }

        mPaint = new Paint();
        mBound = new Rect();
        createTestData();

        mCustomGestureDetector = new CustomGestureDetector(getContext(), new CustomGestureDetector.OnGestureListener() {
            @Override
            public boolean onDown2(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onUp2(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onUp(MotionEvent e) {
                //隐藏指示
                mTouchIndex = -1;
                isLongPress = false;
                if (mTouchModeListener != null) {
                    mTouchModeListener.change("", "", "");
                }
                postInvalidate();
                return true;
            }

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public void onShowPress(MotionEvent e) {
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                if (!isLongPress && mTouchModeListener != null) {
                    mTouchModeListener.onClick();
                }
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                if (e2.getAction() == MotionEvent.ACTION_MOVE && isLongPress) {
                    getTouchValue(e2.getRawX());
                }
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                getTouchValue(e.getRawX());
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //return super.onTouchEvent(event);
        return mCustomGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (isLongPress) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float width = getWidth();
        float height = getHeight();

        //将X轴长度分为1000份
        float itemX = width / 1000.0f;
        //将Y轴长度分为500份
        float itemY = height / 500.0f;

        //绘制柱状图
        drawCylinder(canvas, itemX, itemY);
        //绘制挡板
        drawBaffle(canvas, itemX, itemY);
        //绘制坐标轴
        drawAxis(canvas, itemX, itemY);

    }

    //绘制一个挡板，挡住圆柱半圆超出坐标轴的部分（暂且这么做先）
    private void drawBaffle(Canvas canvas, float itemX, float itemY) {
        float topTitlePadding = itemY * 30.0f;//标题距上边际的间隔
        float mTitleTextPadding = itemY * 30.0f;//柱状图距离标题上边距
        float yLineHeight = itemY * 330.0f;//Y轴高度
        RectF rectF = new RectF();
        rectF.left = 0;
        rectF.top = topTitlePadding + yLineHeight + mTitleTextPadding + mTitleHeight;
        rectF.right = getWidth();
        rectF.bottom = getHeight();
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(getResources().getColor(R.color.background_color));
        canvas.drawRect(rectF, mPaint);
    }

    private void drawAxis(Canvas canvas, float itemX, float itemY) {
        float topTitlePadding = itemY * 30.0f;//标题距上边际的间隔
        float mTitleTextPadding = itemY * 30.0f;//柱状图距离标题上边距
        float bottomPadding = itemY * 15.0f;//柱状图底边距
        float leftScaleRightPadding = itemX * 90.0f;//左边刻度文字右端距离左屏幕距离
        float yLinePadding = itemX * 110.0f;//Y轴X坐标
        float yLineHeight = itemY * 330.0f;//Y轴高度
        float xLineWidth = itemX * 810.0f;//X轴长度
        float mStartPadding = itemX * 30.0f;//第一个柱状图距离Y轴间隔
        float mCylinderWidth = itemX * 90.0f;//柱状图宽度
        float mCylinderPadding = itemX * 30.0f;//柱状图与柱状图间的间隔
        float mPointTopPadding = itemY * 20.0f;//圆点距离X轴的距离
        float mDateTopPadding = itemY * 65.0f;//日期距离X轴的距离
        //绘制XY轴
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mAxisColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(3.5f);

        topTitlePadding = topTitlePadding + mTitleTextPadding + mTitleHeight;
        canvas.drawLine(yLinePadding, topTitlePadding, yLinePadding, topTitlePadding + yLineHeight, mPaint);
        canvas.drawLine(yLinePadding, topTitlePadding + yLineHeight, yLinePadding + xLineWidth + mCylinderPadding, topTitlePadding + yLineHeight, mPaint);

        //绘制Y轴刻度文字
        String[] mLeftValue;
        if (mLeftMaxValue >= 1000) {
            float leftItem = mLeftMaxValue / 3.0f;
            mLeftValue = new String[]{Unit.getFloatScale(1, mLeftMaxValue / 1000.0f) + "k", Unit.getFloatScale(1, leftItem * 2 / 1000.0f) + "k",
                    Unit.getFloatScale(1, leftItem / 1000.0f) + "k", 0 + ""};
        } else {
            mLeftMaxValue = 1000;
            mLeftValue = new String[]{"1k", 0 + ""};
        }

        mPaint.setColor(mScaleTextColor);
        mPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
        int mLeftScaleSize = mLeftValue.length;
        float mLeftScaleHeight = yLineHeight / (mLeftScaleSize - 1);
        for (int i = 0; i < mLeftValue.length; i++) {
            canvas.drawText(mLeftValue[i], leftScaleRightPadding - mPaint.measureText(mLeftValue[i]),
                    topTitlePadding + mLeftScaleHeight * i + (mPaint.getFontMetrics().bottom) / 2,
                    mPaint);
        }

        //绘制7个圆形图标

        mPaint.setAntiAlias(true);
        mPaint.setColor(mScaleTextColor);
        mPaint.setStyle(Paint.Style.FILL);
        for (int i = 0; i < 7; i++) {
            if (i == 0 || i == 6) {
                canvas.drawCircle(yLinePadding + mStartPadding + (mCylinderPadding + mCylinderWidth) * i + mCylinderWidth / 2,
                        topTitlePadding + yLineHeight + mPointTopPadding, 8, mPaint);
            } else {
                canvas.drawCircle(yLinePadding + mStartPadding + (mCylinderPadding + mCylinderWidth) * i + mCylinderWidth / 2,
                        topTitlePadding + yLineHeight + mPointTopPadding, 5, mPaint);
            }

        }

        //绘制开始月/日，结束月/日
        mPaint.setColor(mScaleTextColor);
        mPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics()));
        canvas.drawText(mDate[0], yLinePadding + mStartPadding + mCylinderWidth / 2 - mPaint.measureText(mDate[0]) / 2,
                topTitlePadding + yLineHeight + mDateTopPadding, mPaint);
        canvas.drawText(mDate[1], yLinePadding + mStartPadding + (mCylinderPadding + mCylinderWidth) * 6 + mCylinderWidth / 2 - mPaint.measureText(mDate[1]) / 2,
                topTitlePadding + yLineHeight + mDateTopPadding, mPaint);
    }

    //绘制圆柱图
    private void drawCylinder(Canvas canvas, float itemX, float itemY) {
        float topTitlePadding = itemY * 30.0f;//标题距上边际的间隔
        float mTitleTextPadding = itemY * 30.0f;//柱状图距离标题上边距
        float bottomPadding = itemY * 15.0f;//柱状图底边距
        float leftScaleRightPadding = itemX * 90.0f;//左边刻度文字右端距离左屏幕距离
        float yLinePadding = itemX * 110.0f;//Y轴X坐标
        float yLineHeight = itemY * 330.0f;//Y轴高度
        float xLineWidth = itemX * 810.0f;//X轴长度
        float mStartPadding = itemX * 30.0f;//第一个柱状图距离Y轴间隔
        float mCylinderWidth = itemX * 90.0f;//柱状图宽度
        float mCylinderPadding = itemX * 30.0f;//柱状图与柱状图间的间隔

        float mItemHeight = yLineHeight / mLeftMaxValue;

        //绘制标题
        mPaint.reset();
        mPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
        mPaint.setColor(getResources().getColor(android.R.color.white));
        mPaint.setAntiAlias(true);
        mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mBound);
        canvas.drawText(mTitleText, leftScaleRightPadding / 2, topTitlePadding, mPaint);
        mTitleHeight = mBound.height();
        topTitlePadding = topTitlePadding+mTitleHeight + mTitleTextPadding;


        LinearGradient lg;
        float num;
        float mUserCylinderNum;
        float mGoalCylinderNum;
        //圆柱的左X坐标,圆柱的右X坐标
        float mCylinderLeft, mCylinderRight;
        //半圆的上Y坐标（用于绘制该圆柱对应的步数文字）
        float mRoundTop;

        for (int i = 0; i < mStepList.size(); i++) {
            num = mStepList.get(i);
            mUserCylinderNum = num - (mCylinderWidth / 2 / mItemHeight);
            mGoalCylinderNum = mGoalNum - (mCylinderWidth / 2 / mItemHeight);
            mPaint.reset();
            mPaint.setAntiAlias(true);
            mPaint.setStyle(Paint.Style.FILL);

            //圆柱的左、右X坐标
            mCylinderLeft = yLinePadding + mStartPadding + (mCylinderWidth + mCylinderPadding) * i;
            mCylinderRight = yLinePadding + mStartPadding + (mCylinderWidth + mCylinderPadding) * i + mCylinderWidth;
            //半圆的RectF
            RectF mArcF = new RectF();
            mArcF.left = mCylinderLeft;
            mArcF.right = mCylinderRight;
            //圆柱的RectF
            RectF mCylinderF = new RectF();
            mCylinderF.left = mCylinderLeft;
            mCylinderF.right = mCylinderRight;
            if (num >= mGoalNum) {
                //设置半圆top,bottom
                if (mUserCylinderNum <= 0.0f) {
                    mRoundTop = topTitlePadding + mItemHeight * (mLeftMaxValue - num);
                    mArcF.top = mRoundTop;
                    mArcF.bottom = topTitlePadding + mItemHeight * (mLeftMaxValue - num) + mCylinderWidth;
                } else {
                    mRoundTop = topTitlePadding + mItemHeight * (mLeftMaxValue - mUserCylinderNum) - mCylinderWidth / 2;
                    mArcF.top = mRoundTop;
                    mArcF.bottom = topTitlePadding + mItemHeight * (mLeftMaxValue - mUserCylinderNum) + mCylinderWidth / 2 + mCylinderWidth / 8;
                }
                //设置圆柱top,bottom
                if (mUserCylinderNum <= 0.0f) {
                    lg = new LinearGradient(mCylinderLeft,
                            topTitlePadding + mItemHeight * (mLeftMaxValue - num) + ((topTitlePadding + yLineHeight - (topTitlePadding + mItemHeight * (mLeftMaxValue - num))) / 2),
                            mCylinderLeft,
                            topTitlePadding + yLineHeight,
                            mAchieveTopColor, mAchieveBottomColor, Shader.TileMode.MIRROR);
                    mCylinderF.top = topTitlePadding + yLineHeight;
                    mCylinderF.bottom = topTitlePadding + yLineHeight;
                } else {
                    lg = new LinearGradient(mCylinderLeft,
                            topTitlePadding + mItemHeight * (mLeftMaxValue - mUserCylinderNum),
                            mCylinderLeft,
                            topTitlePadding + yLineHeight,
                            mAchieveTopColor, mAchieveBottomColor, Shader.TileMode.MIRROR);
                    mCylinderF.top = topTitlePadding + mItemHeight * (mLeftMaxValue - mUserCylinderNum);
                    mCylinderF.bottom = topTitlePadding + yLineHeight;
                }

                //绘制半圆
                mPaint.reset();
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setColor(mAchieveTopColor);
                canvas.drawArc(mArcF, -180, 180, true, mPaint);
                //绘制圆柱
                mPaint.reset();
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setAntiAlias(true);
                mPaint.setShader(lg);
                canvas.drawRect(mCylinderF, mPaint);
            } else {
                //设置目标半圆top,bottom
                if (mGoalCylinderNum <= 0.0f) {
                    mRoundTop = topTitlePadding + mItemHeight * (mLeftMaxValue - num);
                    mArcF.top = mRoundTop;
                    mArcF.bottom = topTitlePadding + mItemHeight * (mLeftMaxValue - num) + mCylinderWidth;
                } else {
                    mRoundTop = topTitlePadding + mItemHeight * (mLeftMaxValue - mGoalCylinderNum) - mCylinderWidth / 2;
                    mArcF.top = mRoundTop;
                    mArcF.bottom = topTitlePadding + mItemHeight * (mLeftMaxValue - mGoalCylinderNum) + mCylinderWidth / 2 + mCylinderWidth / 8;
                }
                //设置圆柱top,bottom
                if (mGoalCylinderNum <= 0.0f) {
                    lg = new LinearGradient(yLinePadding + mStartPadding + (mCylinderWidth + mCylinderPadding) * i,
                            topTitlePadding + mItemHeight * (mLeftMaxValue - num) + ((topTitlePadding + yLineHeight - (topTitlePadding + mItemHeight * (mLeftMaxValue - num))) / 2),
                            yLinePadding + mStartPadding + (mCylinderWidth + mCylinderPadding) * i,
                            topTitlePadding + yLineHeight,
                            mGoalColor, mGoalColor, Shader.TileMode.MIRROR);
                    mCylinderF.top = topTitlePadding + yLineHeight;
                    mCylinderF.bottom = topTitlePadding + yLineHeight;
                } else {
                    lg = new LinearGradient(yLinePadding + mStartPadding + (mCylinderWidth + mCylinderPadding) * i,
                            topTitlePadding + mItemHeight * (mLeftMaxValue - mGoalCylinderNum),
                            yLinePadding + mStartPadding + (mCylinderWidth + mCylinderPadding) * i,
                            topTitlePadding + yLineHeight,
                            mGoalColor, mGoalColor, Shader.TileMode.MIRROR);
                    mCylinderF.top = topTitlePadding + mItemHeight * (mLeftMaxValue - mGoalCylinderNum);
                    mCylinderF.bottom = topTitlePadding + yLineHeight;
                }

                //绘制目标半圆，圆柱
                mPaint.reset();
                mPaint.setAntiAlias(true);
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setColor(mGoalColor);
                canvas.drawArc(mArcF, -180, 180, true, mPaint);
                mPaint.reset();
                mPaint.setAntiAlias(true);
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setShader(lg);
                canvas.drawRect(mCylinderF, mPaint);

                //设置用户实际运动数据半圆top,bottom
                if (mUserCylinderNum <= 0.0f) {
                    mArcF.top = topTitlePadding + mItemHeight * (mLeftMaxValue - num);
                    mArcF.bottom = topTitlePadding + mItemHeight * (mLeftMaxValue - num) + mCylinderWidth;
                } else {
                    mArcF.top = topTitlePadding + mItemHeight * (mLeftMaxValue - mUserCylinderNum) - mCylinderWidth / 2;
                    mArcF.bottom = topTitlePadding + mItemHeight * (mLeftMaxValue - mUserCylinderNum) + mCylinderWidth / 2 + mCylinderWidth / 8;
                }
                //设置圆柱top,bottom
                if (mUserCylinderNum <= 0.0f) {
                    lg = new LinearGradient(yLinePadding + mStartPadding + (mCylinderWidth + mCylinderPadding) * i,
                            topTitlePadding + mItemHeight * (mLeftMaxValue - num) + ((topTitlePadding + yLineHeight - (topTitlePadding + mItemHeight * (mLeftMaxValue - num))) / 2),
                            yLinePadding + mStartPadding + (mCylinderWidth + mCylinderPadding) * i,
                            topTitlePadding + yLineHeight,
                            mUnAchieveTopColor, mUnAchieveBottomColor, Shader.TileMode.MIRROR);
                    mCylinderF.top = topTitlePadding + yLineHeight;
                    mCylinderF.bottom = topTitlePadding + yLineHeight;
                } else {
                    lg = new LinearGradient(yLinePadding + mStartPadding + (mCylinderWidth + mCylinderPadding) * i,
                            topTitlePadding + mItemHeight * (mLeftMaxValue - mUserCylinderNum),
                            yLinePadding + mStartPadding + (mCylinderWidth + mCylinderPadding) * i,
                            topTitlePadding + yLineHeight,
                            mUnAchieveTopColor, mUnAchieveBottomColor, Shader.TileMode.MIRROR);
                    mCylinderF.top = topTitlePadding + mItemHeight * (mLeftMaxValue - mUserCylinderNum);
                    mCylinderF.bottom = topTitlePadding + yLineHeight;
                }

                //绘制未达标半圆，圆柱
                mPaint.reset();
                mPaint.setAntiAlias(true);
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setColor(mUnAchieveTopColor);
                canvas.drawArc(mArcF, -180, 180, true, mPaint);
                mPaint.reset();
                mPaint.setAntiAlias(true);
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setShader(lg);
                canvas.drawRect(mCylinderF, mPaint);
            }
            //当用户长按柱状图后显示当前柱状图对应的运动数据
            if (isLongPress && mTouchIndex == i && (mTouchIndex < mStepList.size())) {
                String value = mStepList.get(mTouchIndex) + "";
                mPaint.reset();
                mPaint.setColor(getResources().getColor(android.R.color.white));
                mPaint.setAntiAlias(true);
                mPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                canvas.drawText(value,
                        mCylinderLeft + (mCylinderWidth - mPaint.measureText(value)) / 2,
                        mRoundTop - (-mPaint.getFontMetrics().top) / 3,
                        mPaint);
            }
        }

    }

    private void getTouchValue(float touchX) {
        isLongPress = true;
        //将X轴长度分为1000份
        float itemX = getWidth() / 1000.0f;
        //将Y轴长度分为500份
        float itemY = getHeight() / 500.0f;

        float topPadding = itemX * 30.0f;//柱状图上边距
        float bottomPadding = itemY * 15.0f;//柱状图底边距
        float yLinePadding = itemX * 110.0f;//Y轴X坐标
        float yLineHeight = itemY * 390.0f;//Y轴高度
        float xLineWidth = itemX * 810.0f;//X轴长度
        float mStartPadding = itemX * 30.0f;//第一个柱状图距离Y轴间隔
        float mCylinderWidth = itemX * 90.0f;//柱状图宽度
        float mCylinderPadding = itemX * 30.0f;//柱状图与柱状图间的间隔
        for (int i = 0; i < mStepList.size(); i++) {
            float left = yLinePadding + mStartPadding + (mCylinderWidth + mCylinderPadding) * i;
            float right = yLinePadding + mStartPadding + (mCylinderWidth + mCylinderPadding) * i + mCylinderWidth;
            if (touchX >= left && touchX <= right) {
                mTouchIndex = i;
                break;
            } else {
                mTouchIndex = -1;
            }
        }
        if (mTouchModeListener != null && mTouchIndex >= 0 && mStepList.size() > mTouchIndex) {
            String value = mStepList.get(mTouchIndex) + "";
            mTouchModeListener.change("", value, "");
        }

        if (mTouchIndex >= 0 && mStepList.size() > mTouchIndex) {
            postInvalidate();
        }

    }

    private void createTestData() {
        Random random = new Random();
        for (int i = 0; i < 7; i++) {
            int num = random.nextInt(50000);
            if (i == 1) {
                num = 2000;
            } else if (i == 4) {
                num = 200;
            }
            mStepList.add(num);
            if (mLeftMaxValue < num) {
                mLeftMaxValue = num;
            }
            if (mLeftMinValue > num) {
                mLeftMinValue = num;
            }
        }
        if (mLeftMaxValue < mGoalNum) {
            mLeftMaxValue = mGoalNum;
        }
        mDate = Unit.getStartEndDay(new Date());
    }
}

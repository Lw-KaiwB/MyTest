package com.test.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.test.mytest.R;
import com.test.unit.Unit;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Administrator on 2017/11/28.
 * 渐变折线图，用于显示速度(0)，心率(1)，高度(2)，步频(3)，温度(4)
 */

public class ChartPolyLineViewOld extends View {
    private static final int CHART_TYPE_SPEED = 0;//速度折线图
    private static final int CHART_TYPE_HEART = 1;//心率折线图
    private static final int CHART_TYPE_HIGH = 2;//高度折线图
    private static final int CHART_TYPE_FREQ = 3;//步频折线图
    private static final int CHART_TYPE_TEMP = 4;//温度折线图
    //渐变折线图类型
    private int mChartType = 0;
    //折线图渐变顶部、底部颜色
    private int mTopColor, mBottomColor;
    //标题字符串
    private String mTitleTest;
    //单位
    private String mUnitText;
    //左右运动数值
    private String mLeftValue, mRightValue;
    //左右运动数值字符串（数值+单位）
    private String mLeftValueText, mRightValueText;
    //运动数值标题
    private String mLeftMessageTest, mRigthMessageTest;

    //运动时长：秒
    private int mSportDuration;
    //运动数据
    private ArrayList<Integer> mSportValueList;
    //最小值，平均值，最大值
    private String[] mMinMaxValue;


    private Paint mPaint;
    private int mMaxSportValue = Integer.MIN_VALUE;
    private int mMinSportValue = Integer.MAX_VALUE;

    private boolean isLongPress = false;

    public ChartPolyLineViewOld(Context context) {
        this(context, null);
    }

    public ChartPolyLineViewOld(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChartPolyLineViewOld(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ty = context.obtainStyledAttributes(attrs, R.styleable.polyline, defStyleAttr, 0);
        mChartType = ty.getInt(R.styleable.polyline_chart_type, 0);
        mTopColor = ty.getColor(R.styleable.polyline_polyline_top_color, getResources().getColor(R.color.chart_polyline_speed_top_color));
        mBottomColor = ty.getColor(R.styleable.polyline_polyline_bottom_color, getResources().getColor(R.color.chart_polyline_speed_bottom_color));
        mTitleTest = getResources().getString(ty.getResourceId(R.styleable.polyline_polyline_title, -1));
        mLeftMessageTest = getResources().getString(ty.getResourceId(R.styleable.polyline_polyline_left_message, -1));
        mRigthMessageTest = getResources().getString(ty.getResourceId(R.styleable.polyline_polyline_right_message, -1));
        ty.recycle();

        mSportDuration = createSportDuration();
        mSportValueList = createSportTestValue();

        mPaint = new Paint();

        if (mChartType == CHART_TYPE_SPEED) {
            mUnitText = getResources().getString(R.string.unit_speed_km_h);
            mLeftValue = mMinMaxValue[1] + "";
            mRightValue = mMinMaxValue[2] + "";

        } else if (mChartType == CHART_TYPE_HEART) {
            mUnitText = getResources().getString(R.string.unit_heart);
            mLeftValue = mMinMaxValue[1] + "";
            mRightValue = mMinMaxValue[2] + "";
        } else if (mChartType == CHART_TYPE_HIGH) {
            mUnitText = getResources().getString(R.string.unit_height);
            mLeftValue = mMinMaxValue[1] + "";
            mRightValue = mMinMaxValue[2] + "";
        } else if (mChartType == CHART_TYPE_FREQ) {
            mUnitText = getResources().getString(R.string.unit_freq);
            mLeftValue = mMinMaxValue[1] + "";
            mRightValue = mMinMaxValue[2] + "";
        } else {
            mUnitText = getResources().getString(R.string.unit_temp);
            mLeftValue = mMinMaxValue[0] + "";
            mRightValue = mMinMaxValue[2] + "";
        }
        mTitleTest = mTitleTest + "  (" + mUnitText + ")";
        mLeftValueText = mLeftValue + "" + mUnitText;
        mRightValueText = mRightValue + "" + mUnitText;

    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (isLongPress) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float itemX = getWidth() / 600.0f;
        float itemY = getHeight() / 360.0f;

        drawAxis(canvas, itemX, itemY);
    }

    private void drawAxis(Canvas canvas, float itemX, float itemY) {
        float mTopPadding = itemY * 30;//图形上边距
        float mTitleTextHeight = itemY * 20;//标题高度，比如：步频（SPM）字符串的高度
        float mTopPadding1 = itemY * 20;//标题与数值之间的间隔
        float mSportValueTextHeight = itemY * 30;//数值高度，比如：285 SPM
        float mTopPadding2 = itemY * 0;//数值与数值文字之间的间隔
        float mMessageTextHeight = itemY * 20;//数值文字高度，比如：平均步频
        float mTopPadding3 = itemY * 20;//数值文字与折线图之间的间隔
        float mAxisHeight = itemY * 150;//折线图的高度
        float mTopPadding4 = itemY * 15;//小圆点与折线图之间的间隔
        float mTopPadding5 = itemY * 30;//时间与小圆点间的间隔
        float mScaleTestRightPadding = itemX * 50;//Y轴刻度文字最右边距离屏幕的距离
        float mAxisStartX = itemX * 60;//坐标轴开始的X坐标
        float mAxisWidth = itemX * 490;//折线图的长度
        float mTitleTextLeftPadding = itemX * 20;//标题距离屏幕边距的距离
        float mXCenter = itemX * 300;//X轴中点坐标
        float mScaleItemY;//Y轴平均分数（Y轴高度除以运动数据最大值，如果为0，则除以1）
        float mScaleItemX;//X轴平均分数（X轴宽度除以运动数据个数，如果运动数据个数为0，则除以1）

        if (mMaxSportValue == 0) {
            mMaxSportValue = 1;
        }
        mScaleItemY = mAxisHeight / mMaxSportValue;
        int mSportCount = mSportValueList.size() - 1;
        if (mSportCount == 0) {
            mSportCount = 1;
        }
        mScaleItemX = mAxisWidth / mSportCount;

        //绘制标题
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(getResources().getColor(android.R.color.white));
        mPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
        canvas.drawText(mTitleTest, mTitleTextLeftPadding, mTopPadding, mPaint);
        mTitleTextHeight = -mPaint.getFontMetrics().top + mPaint.getFontMetrics().bottom;


        //绘制左右运动数据值
        //左边
        mPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 15, getResources().getDisplayMetrics()));
        float leftValueWidth = mPaint.measureText(mLeftValueText);
        float rightMessageWidth = mPaint.measureText(mRightValueText);
        mSportValueTextHeight = -mPaint.getFontMetrics().top + mPaint.getFontMetrics().bottom;

        canvas.drawText(mLeftValue,
                (mXCenter - leftValueWidth) / 2,
                mTopPadding + mTitleTextHeight + mTopPadding1,
                mPaint);
        mPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 9, getResources().getDisplayMetrics()));
        float unitWidth = mPaint.measureText(mUnitText);
        canvas.drawText(mUnitText,
                (mXCenter + leftValueWidth) / 2 - unitWidth,
                mTopPadding + mTitleTextHeight + mTopPadding1,
                mPaint);

        //右边
        mPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 15, getResources().getDisplayMetrics()));
        canvas.drawText(mRightValue,
                mXCenter + (mXCenter - rightMessageWidth) / 2,
                mTopPadding + mTitleTextHeight + mTopPadding1,
                mPaint);
        mPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 9, getResources().getDisplayMetrics()));
        canvas.drawText(mUnitText,
                mXCenter + (mXCenter + rightMessageWidth) / 2 - unitWidth,
                mTopPadding + mTitleTextHeight + mTopPadding1,
                mPaint);

        //绘制左右运动数据标题
        mPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 9, getResources().getDisplayMetrics()));
        canvas.drawText(mLeftMessageTest, (mXCenter - mPaint.measureText(mLeftMessageTest)) / 2,
                mTopPadding + mTitleTextHeight + mTopPadding1 + mSportValueTextHeight + mTopPadding2,
                mPaint);
        canvas.drawText(mRigthMessageTest, mXCenter + (mXCenter - mPaint.measureText(mRigthMessageTest)) / 2,
                mTopPadding + mTitleTextHeight + mTopPadding1 + mSportValueTextHeight + mTopPadding2,
                mPaint);
        mMessageTextHeight = -mPaint.getFontMetrics().top + mPaint.getFontMetrics().bottom;

        //绘制坐标轴
        float chartStartY = mTopPadding + mTitleTextHeight + mTopPadding1 + mSportValueTextHeight + mTopPadding2 + mMessageTextHeight + mTopPadding3;
        float chartEndY = chartStartY + mAxisHeight;
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(getResources().getColor(R.color.chart_axis_color));
        mPaint.setStrokeWidth(2.5f);
        canvas.drawLine(mAxisStartX, chartStartY, mAxisStartX, chartEndY, mPaint);
        canvas.drawLine(mAxisStartX, chartEndY, mAxisStartX + mAxisWidth, chartEndY, mPaint);

        //绘制左刻度
        float mScaleValue;
        float[] mScaleArr;
        if (mMaxSportValue > 3) {
            mScaleValue = mMaxSportValue / 3.0f;
            mScaleArr = new float[]{mMaxSportValue, mScaleValue * 2, mScaleValue, 0};
        } else {
            if (mMaxSportValue == 0) {
                mMaxSportValue = 1;
            }
            mScaleArr = new float[]{mMaxSportValue, 0};
        }

        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(getResources().getColor(android.R.color.white));
        mPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));

        for (int i = 0; i < mScaleArr.length; i++) {
            String value = Unit.getFloatScale(1, mScaleArr[i]) + "";
            float textWidth = mPaint.measureText(value);
            canvas.drawText(value, mScaleTestRightPadding - textWidth,
                    chartEndY - mScaleItemY * mScaleArr[i] + mPaint.getFontMetrics().bottom, mPaint);
        }


        //计算时间刻度
        String[] timeArr;
        if (mSportDuration > 5) {
            timeArr = new String[5];
            int mScaleTime = mSportDuration / 5;
            for (int i = 0; i < 5; i++) {
                if (i == 4) {
                    timeArr[i] = Unit.getSec2Time(mSportDuration);
                } else {
                    timeArr[i] = Unit.getSec2Time(mScaleTime * (i + 1));
                }
            }
        } else {
            timeArr = new String[]{Unit.getSec2Time(0), Unit.getSec2Time(mSportDuration)};
        }

        mPaint.reset();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(getResources().getColor(android.R.color.white));
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
        int timeSize = timeArr.length;
        float timeItemX = mAxisWidth / (timeSize);
        for (int i = 0; i < timeSize; i++) {
            //绘制圆圈
            canvas.drawCircle(mAxisStartX + timeItemX * (i + 1), chartEndY + mTopPadding4, 6, mPaint);
            //绘制时间刻度
            canvas.drawText(timeArr[i],
                    mAxisStartX + timeItemX * (i + 1) - mPaint.measureText(timeArr[i]) / 2,
                    chartEndY + mTopPadding4 + mTopPadding5,
                    mPaint);
        }

        //绘制折线
        Path path = new Path();
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mTopColor);
        mPaint.setStrokeWidth(2.0f);
        mPaint.setStyle(Paint.Style.FILL);

        if (mSportValueList.size() > 0) {
            path.moveTo(mAxisStartX, chartEndY - mSportValueList.get(0) * mScaleItemY);
            for (int i = 0; i < mSportValueList.size(); i++) {
                Log.e("TAG", "value(" + i + ")=" + mSportValueList.get(i));
                path.lineTo(mAxisStartX + mScaleItemX * (i * 1.0f), chartEndY - mSportValueList.get(i) * mScaleItemY);
                if (i==mSportValueList.size()-1){
                    path.lineTo(mAxisStartX + mScaleItemX * (i * 1.0f),chartEndY);
                    path.lineTo(mAxisStartX,chartEndY);
                }
            }

        }
        LinearGradient lg = new LinearGradient(mAxisStartX,chartEndY-Float.parseFloat(mMinMaxValue[2])*mScaleItemY,
                mAxisStartX,chartEndY,mTopColor,mBottomColor, Shader.TileMode.MIRROR);
        mPaint.setShader(lg);
        canvas.drawPath(path, mPaint);


    }

    //生成测试用的运动时间
    private int createSportDuration() {
        Random random = new Random();
        return random.nextInt(86400);
    }

    private ArrayList<Integer> createSportTestValue() {
        mMinMaxValue = new String[]{"0", "0", "0"};
        mSportValueList = new ArrayList<>();
        Random random = new Random();
        int count = 1;
        if (mSportDuration > 50) {
            count = mSportDuration / 50;

        }
        int mSum = 0;
        for (int i = 0; i < count; i++) {
            int num = random.nextInt(250);
            mSum += num;
            if (mMaxSportValue < num) {
                mMaxSportValue = num;
            }
            if (mMinSportValue > num) {
                mMinSportValue = num;
            }
            mSportValueList.add(num);
        }


        if (mSportValueList.size() > 0) {
            mMinMaxValue[0] = mMinSportValue + "";
            mMinMaxValue[1] = (mSum / count) + "";
            mMinMaxValue[2] = mMaxSportValue + "";
        }

        if (mMaxSportValue == 0) {
            mMaxSportValue = 1;
        } else {
            mMaxSportValue = 3 * mMaxSportValue / 2;
        }
        return mSportValueList;
    }


}

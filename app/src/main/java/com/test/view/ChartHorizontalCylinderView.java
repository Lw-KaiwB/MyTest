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
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.test.mytest.R;
import com.test.unit.Unit;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Administrator on 2017/11/29.
 * 配速横向柱状图
 */

public class ChartHorizontalCylinderView extends View {
    private static final int CHART_RIDING = 1;
    //柱状图类型，0表示单公里显示，比如：跑步，徒步，马拉松，1表示可能要5公里显示的，比如：骑行
    private int mChatType;
    //横向柱状图渐变开始、结束颜色
    private int mStartColor, mEndColor;

    private Paint mPaint;

    //是否长按
    private boolean isLongPress = false;
    //是否是公制单位
    private boolean isMetricUnit = true;

    //标题字符串
    private String mTitleText;
    //单位字符串
    private String mUnitString;
    //Y轴刻度
    private String[] mYScaleArr;
    //公里数
    private int mMileageNum;
    //最大配速，也就是耗时最长的配速
    private int mMaxPaceValue = Integer.MIN_VALUE;

    private ArrayList<Integer> mSportValueList;

    private Rect mBound;
    private RectF mRectF;


    public ChartHorizontalCylinderView(Context context) {
        this(context, null);
    }

    public ChartHorizontalCylinderView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChartHorizontalCylinderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ty = context.obtainStyledAttributes(attrs, R.styleable.horizontal_cylinder, defStyleAttr, 0);
        mChatType = ty.getInt(R.styleable.horizontal_cylinder_chart_cylinder_type, 0);
        mStartColor = ty.getColor(R.styleable.horizontal_cylinder_start_color, getResources().getColor(R.color.chart_horizontal_cylinder_start_color));
        mEndColor = ty.getColor(R.styleable.horizontal_cylinder_end_color, getResources().getColor(R.color.chart_horizontal_cylinder_end_color));
        ty.recycle();

        mTitleText = getResources().getString(R.string.title_pace);
        if (isMetricUnit) {
            mUnitString = getResources().getString(R.string.unit_min_km);
        } else {
            mUnitString = getResources().getString(R.string.unit_min_ft);
        }

        mTitleText = mTitleText + " (" + mUnitString + ")";

        mPaint = new Paint();

        mSportValueList = createTestValue();
        Log.e("TAG", "size=" + mSportValueList.size());
        if (mChatType == 1) {
            mYScaleArr = new String[mSportValueList.size()];
            for (int i = 0; i < mYScaleArr.length; i++) {
                mYScaleArr[i] = Unit.int2String(i + 1);
            }
        } else {
            mYScaleArr = new String[mSportValueList.size()];
            for (int i = 0; i < mYScaleArr.length; i++) {
                mYScaleArr[i] = Unit.int2String(i + 1);
            }
        }
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
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = widthSize;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            mBound = new Rect();
            mPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
            mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mBound);
            float mTitleTopPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());//标题距离顶部的间隔
            float mChartTopPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());//横向柱状图距离标题的间隔
            float mCylinderHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());//每个柱状图的高度
            float mCylinderPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());//柱状图间的间隔
            height = getPaddingBottom() + getPaddingTop() + mBound.height() * 2 + (int) mTitleTopPadding + (int) mChartTopPadding + (int) ((mCylinderHeight + mCylinderPadding) * mMileageNum);
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float itemX = getWidth() / 600.0f;//将屏幕宽均分为600份

        drawCylinder(canvas, itemX);
    }

    private void drawCylinder(Canvas canvas, float itemX) {
        float mTitlePaddingLeft = itemX * 20.0f;//标题左侧距离屏幕左边的距离
        float mTitleTopPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());//标题距离顶部的间隔
        float mTitleTextHeight;//标题的高度
        float mScaleTextRight = itemX * 60.0f;//Y轴刻度字符串右边距距离屏幕左边的距离
        float mCylinderStartX = itemX * 70.0f;//横向柱状图开始X坐标（就是X轴最左边坐标）
        float mXWidth = itemX * 400.0f;//X轴宽度
        float mRightPadding = itemX * 10.0f;//横向柱状图距离右侧文字的间隔
        float mChartTopPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());//横向柱状图距离标题的间隔
        float mCylinderHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());//每个柱状图的高度
        float mCylinderPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());//柱状图间的间隔
        float mYStart;//Y轴的起点Y坐标(就是Y轴的最高点)
        float mValueItemX = mXWidth / mMaxPaceValue;
        float mPaceInnerPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());//骑行在柱状图内距离柱状图右侧的距离


        //绘制标题
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(getResources().getColor(android.R.color.white));
        mPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
        mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mBound);
        canvas.drawText(mTitleText, mTitlePaddingLeft, mTitleTopPadding, mPaint);
        mTitleTextHeight = mBound.height();

        mYStart = mTitleTextHeight + mTitleTopPadding + mChartTopPadding;
        //绘制Y轴
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(getResources().getColor(R.color.chart_axis_color));
        mPaint.setStrokeWidth(2.0f);
        canvas.drawLine(mCylinderStartX, mYStart, mCylinderStartX, mYStart + mCylinderHeight * mMileageNum + mCylinderPadding * (mMileageNum - 1), mPaint);
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(getResources().getColor(android.R.color.white));
        mPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));

        if (mYScaleArr == null || mYScaleArr.length < 0) {
            return;
        }
        //绘制里程刻度
        for (int i = 0; i < mYScaleArr.length; i++) {
            String text = mYScaleArr[i];
            mPaint.getTextBounds(text, 0, text.length(), mBound);
            canvas.drawText(text,
                    mScaleTextRight - mBound.width(),
                    mYStart + (mCylinderHeight + mBound.height()) / 2 + (mCylinderPadding + mCylinderHeight) * i,
                    mPaint);
        }

        //绘制横向柱状图
        mRectF = new RectF();
        mPaint.reset();
        LinearGradient lg;
        for (int i = 0; i < mSportValueList.size(); i++) {
            mPaint.setAntiAlias(true);
            mRectF.top = mYStart + (mCylinderHeight + mCylinderPadding) * i;
            mRectF.left = mCylinderStartX;
            mRectF.right = mCylinderStartX + mValueItemX * mSportValueList.get(i);
            mRectF.bottom = mYStart + mCylinderHeight * (i + 1) + mCylinderPadding * (i);

            lg = new LinearGradient(mRectF.left, mRectF.top, mRectF.right, mRectF.top, mStartColor, mEndColor, Shader.TileMode.MIRROR);
            mPaint.setShader(lg);
            canvas.drawRect(mRectF, mPaint);

            //如果是骑行的话，要在柱状图上画上配速时间
            if (mChatType == CHART_RIDING) {
                mPaint.reset();
                mPaint.setAntiAlias(true);
                mPaint.setColor(getResources().getColor(android.R.color.white));
                mPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                String mPaceTime = Unit.getSec2Time(mSportValueList.get(i));
                mPaint.getTextBounds(mPaceTime, 0, mPaceTime.length(), mBound);
                if (mRectF.right > (mCylinderStartX + mBound.width() + mPaceInnerPadding)) {
                    canvas.drawText(mPaceTime, mRectF.right - mBound.width() - mPaceInnerPadding, mRectF.bottom - mBound.height() / 2, mPaint);
                } else {
                    canvas.drawText(mPaceTime, mRectF.right + mPaceInnerPadding, mRectF.bottom - mBound.height() / 2, mPaint);
                }
            }
        }

        //如果是骑行在右侧绘制的是速度，其他运动直接在右侧绘制配速时间
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(getResources().getColor(android.R.color.white));
        mPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
        for (int i = 0; i < mSportValueList.size(); i++) {
            String mText;
            if (mChatType == CHART_RIDING) {
                //骑行计算的是速度
                mText = Unit.sec2speed(0, mSportValueList.get(i)) + " km/h" ;
            } else {
                mText = Unit.getSec2Time(mSportValueList.get(i));
            }

            mPaint.getTextBounds(mText, 0, mText.length(), mBound);
            canvas.drawText(mText,
                    mCylinderStartX + mXWidth + mRightPadding,
                    mYStart + mCylinderHeight * (i + 1) + mCylinderPadding * (i) - mBound.height() / 2, mPaint);

        }


    }

    private ArrayList<Integer> createTestValue() {
        mSportValueList = new ArrayList<>();
        Random random = new Random();
        int mileage = random.nextInt(1000);
        if (mileage > 0) {
            for (int i = 0; i < mileage; i++) {
                int num = random.nextInt(86400);
                mSportValueList.add(num);
                if (mMaxPaceValue < num) {
                    mMaxPaceValue = num;
                }
            }
        }
        if (mMaxPaceValue == 0) {
            mMaxPaceValue = 1;
        }
        mMileageNum = mSportValueList.size();
        return mSportValueList;
    }

}

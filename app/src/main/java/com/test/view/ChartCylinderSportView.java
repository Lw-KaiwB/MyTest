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
import java.util.Date;
import java.util.Random;

/**
 * Created by Administrator on 2017/11/27.
 * 这是7天运动数据柱状图，主要用于运动模式里，比如跑步，骑行，登山……
 */

public class ChartCylinderSportView extends View {
    //步数单位标识
    private final static int UNIT_STEP_TAG = 0;
    //公里单位标识
    private final static int UNIT_KM_TAG = 1;
    //米单位标识
    private final static int UNIT_M_TAG = 2;

    //在公制单位下，最大值小于1公里的按1公里
    private int mMetricScaleValue = 1000;
    //在英制单位下，最大值小于1英里的按1英里
    private int mBitishScaleValue = 1609;
    //最大值顶部、底部渐变颜色
    private int mMaxTopColor, mMaxBottomColor;
    //中间值顶部、底部渐变颜色
    private int mCenterTopColor, mCenterBottomColor;
    //最小值顶部、顶部渐变颜色
    private int mMinTopColor, mMinBottomColor;
    //坐标轴颜色
    private int mAxisColor;
    //坐标轴刻度文字颜色
    private int mScaleTextColor;
    //标题
    private String mTitleText;
    //单位标志
    private int mUnitType;
    //是否是公制单位
    private boolean isMetric = true;
    //标题的高度
    private float mTitleHeight = 0.0f;

    private Rect mBound;

    private ArrayList<Integer> mSportValueList;
    private String[] mDateArr;

    private Paint mPaint;
    private int mMaxValue = Integer.MIN_VALUE;
    private int mMinValue = Integer.MAX_VALUE;

    private boolean isLongPress = false;
    private int mTouchIndex = -1;
    private CustomGestureDetector mGestureDetector;
    private TouchModeListener mListener;

    public interface TouchModeListener {
        void onClick();
    }

    private void setOnTouchListener(TouchModeListener listener) {
        this.mListener = listener;
    }


    public ChartCylinderSportView(Context context) {
        this(context, null);
    }

    public ChartCylinderSportView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChartCylinderSportView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ty = context.obtainStyledAttributes(attrs, R.styleable.chart_cylinder, defStyleAttr, 0);
        mMaxTopColor = ty.getColor(R.styleable.chart_cylinder_max_top_color, getResources().getColor(R.color.chart_cylinder_max_top_color));
        mMaxBottomColor = ty.getColor(R.styleable.chart_cylinder_max_bottom_color, getResources().getColor(R.color.chart_cylinder_max_bottom_color));
        mCenterTopColor = ty.getColor(R.styleable.chart_cylinder_center_top_color, getResources().getColor(R.color.chart_cylinder_center_top_color));
        mCenterBottomColor = ty.getColor(R.styleable.chart_cylinder_center_bottom_color, getResources().getColor(R.color.chart_cylinder_center_bottom_color));
        mMinTopColor = ty.getColor(R.styleable.chart_cylinder_min_top_color, getResources().getColor(R.color.chart_cylinder_min_top_color));
        mMinBottomColor = ty.getColor(R.styleable.chart_cylinder_min_bottom_color, getResources().getColor(R.color.chart_cylinder_min_bottom_color));
        mTitleText = getResources().getString(ty.getResourceId(R.styleable.chart_cylinder_chart_title_text, -1));
        mUnitType = ty.getInt(R.styleable.chart_cylinder_unit_type, 0);
        ty.recycle();

        if (mUnitType == UNIT_STEP_TAG) {
            mTitleText = mTitleText + " (" + getResources().getString(R.string.unit_step) + ")";
        } else if (mUnitType == UNIT_M_TAG) {
            mTitleText = mTitleText + " (" + getResources().getString(R.string.unit_height) + ")";
        } else {
            mTitleText = mTitleText + " (" + getResources().getString(R.string.unit_min_km) + ")";
        }

        mPaint = new Paint();
        mBound = new Rect();

        mAxisColor = getResources().getColor(R.color.chart_axis_color);
        mScaleTextColor = getResources().getColor(android.R.color.white);
        mSportValueList = getTestValues();
        mDateArr = Unit.getStartEndDay(new Date());

        mGestureDetector = new CustomGestureDetector(getContext(), new CustomGestureDetector.OnGestureListener() {
            @Override
            public boolean onDown2(MotionEvent e) {
                Log.e("TAG", "onDown2");
                return false;
            }

            @Override
            public boolean onUp2(MotionEvent e) {
                Log.e("TAG", "onUp2");
                return false;
            }

            @Override
            public boolean onUp(MotionEvent e) {
                Log.e("TAG", "onUp");
                //隐藏指示
                mTouchIndex = -1;
                isLongPress = false;

                postInvalidate();
                return true;
            }

            @Override
            public boolean onDown(MotionEvent e) {
                Log.e("TAG", "onDown");
                return true;
            }

            @Override
            public void onShowPress(MotionEvent e) {
                Log.e("TAG", "onShowPress");
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                Log.e("TAG", "onSingleTapUp");
                if (!isLongPress && mListener != null) {
                    mListener.onClick();
                }
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                Log.e("TAG", "onScroll");
                if (e2.getAction() == MotionEvent.ACTION_MOVE && isLongPress) {
                    getTouchValue(e2.getRawX());
                }
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                Log.e("TAG", "onLongPress");
                getTouchValue(e.getRawX());
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                Log.e("TAG", "onFling");
                return false;
            }
        });

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
        //return super.onTouchEvent(event);
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //将X轴长度分为1000份
        float itemX = getWidth() / 1000.0f;
        //将Y轴长度分为500份
        float itemY = getHeight() / 500.0f;

        //绘制柱状图
        drawCylinder(canvas, itemX, itemY);
        //绘制挡板
        drawBaffle(canvas, itemX, itemY);
        //绘制坐标轴
        drawAxis(canvas, itemX, itemY);
    }

    //绘制坐标轴
    private void drawAxis(Canvas canvas, float itemX, float itemY) {
        float topPadding = itemX * 30.0f;//标题距离视图顶部上边距
        float titleTopPadding = itemX * 30.0f;//柱状图距离标题上边距
        float bottomPadding = itemY * 15.0f;//柱状图底边距
        float leftScaleRightPadding = itemX * 100.0f;//左边刻度文字右端距离左屏幕距离
        float yLinePadding = itemX * 110.0f;//Y轴X坐标
        float yLineHeight = itemY * 330.0f;//Y轴高度
        float xLineWidth = itemX * 810.0f;//X轴长度
        float mStartPadding = itemX * 30.0f;//第一个柱状图距离Y轴间隔
        float mCylinderWidth = itemX * 90.0f;//柱状图宽度
        float mCylinderPadding = itemX * 30.0f;//柱状图与柱状图间的间隔
        float mPointTopPadding = itemY * 20.0f;//圆点距离X轴的距离
        float mDateTopPadding = itemY * 65.0f;//日期距离X轴的距离

        topPadding = topPadding + titleTopPadding + mTitleHeight;

        //绘制XY轴
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mAxisColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(3.5f);

        canvas.drawLine(yLinePadding, topPadding, yLinePadding, topPadding + yLineHeight, mPaint);
        canvas.drawLine(yLinePadding, topPadding + yLineHeight, yLinePadding + xLineWidth + mCylinderPadding, topPadding + yLineHeight, mPaint);

        //绘制Y轴刻度文字
        if (mMaxValue < mMetricScaleValue) {
            mMaxValue = mMetricScaleValue;
        }

        float mLeftScaleHeight = yLineHeight / 3.0f;
        float mScaleItemValue = mMaxValue / 3.0f;
        String[] mScaleValueText = new String[]{Unit.getFloatScale(1, mMaxValue / 1000f) + "km"
                , Unit.getFloatScale(1, mScaleItemValue * 2 / 1000) + "km",
                Unit.getFloatScale(1, mScaleItemValue / 1000f) + "km", "0"};
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
        mPaint.setColor(mScaleTextColor);
        for (int i = 0; i < mScaleValueText.length; i++) {
            canvas.drawText(mScaleValueText[i], leftScaleRightPadding - mPaint.measureText(mScaleValueText[i]),
                    topPadding + mLeftScaleHeight * i + (mPaint.getFontMetrics().bottom) / 2,
                    mPaint);
        }
        //绘制7个圆点
        mPaint.setAntiAlias(true);
        mPaint.setColor(mScaleTextColor);
        mPaint.setStyle(Paint.Style.FILL);
        for (int i = 0; i < 7; i++) {
            if (i == 0 || i == 6) {
                canvas.drawCircle(yLinePadding + mStartPadding + (mCylinderPadding + mCylinderWidth) * i + mCylinderWidth / 2,
                        topPadding + yLineHeight + mPointTopPadding, 8, mPaint);
            } else {
                canvas.drawCircle(yLinePadding + mStartPadding + (mCylinderPadding + mCylinderWidth) * i + mCylinderWidth / 2,
                        topPadding + yLineHeight + mPointTopPadding, 5, mPaint);
            }
        }

        //绘制开始月/日，结束月/日
        mPaint.setColor(mScaleTextColor);
        mPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics()));
        canvas.drawText(mDateArr[0], yLinePadding + mStartPadding + mCylinderWidth / 2 - mPaint.measureText(mDateArr[0]) / 2,
                topPadding + yLineHeight + mDateTopPadding, mPaint);
        canvas.drawText(mDateArr[1], yLinePadding + mStartPadding + (mCylinderPadding + mCylinderWidth) * 6 + mCylinderWidth / 2 - mPaint.measureText(mDateArr[1]) / 2,
                topPadding + yLineHeight + mDateTopPadding, mPaint);
    }

    //绘制挡板
    private void drawBaffle(Canvas canvas, float itemX, float itemY) {
        float topPadding = itemX * 30.0f;//标题距离视图顶部上边距
        float titleTopPadding = itemX * 30.0f;//柱状图距离标题上边距
        float yLineHeight = itemY * 330.0f;//Y轴高度

        topPadding = titleTopPadding + titleTopPadding + mTitleHeight;

        RectF rectF = new RectF();
        rectF.left = 0;
        rectF.top = topPadding + yLineHeight;
        rectF.right = getWidth();
        rectF.bottom = getHeight();
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(getResources().getColor(R.color.background_color));
        canvas.drawRect(rectF, mPaint);
    }

    //绘制半圆柱状图
    private void drawCylinder(Canvas canvas, float itemX, float itemY) {
        float topPadding = itemX * 30.0f;//标题距离视图顶部上边距
        float titleTopPadding = itemX * 30.0f;//柱状图距离标题上边距
        float bottomPadding = itemY * 15.0f;//柱状图底边距
        float yLinePadding = itemX * 110.0f;//Y轴X坐标
        float yLineHeight = itemY * 330.0f;//Y轴高度
        float xLineWidth = itemX * 810.0f;//X轴长度
        float mStartPadding = itemX * 30.0f;//第一个柱状图距离Y轴间隔
        float mCylinderWidth = itemX * 90.0f;//柱状图宽度
        float mCylinderPadding = itemX * 30.0f;//柱状图与柱状图间的间隔

        float mItemHeight = yLineHeight / mMaxValue;

        //绘制标题
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
        mPaint.setColor(getResources().getColor(android.R.color.white));
        mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mBound);
        canvas.drawText(mTitleText, yLinePadding / 2, topPadding, mPaint);
        mTitleHeight = mBound.height();
        topPadding = topPadding + mTitleHeight + titleTopPadding;

        LinearGradient lg;
        int num;
        float mUserCylinderNum;
        //圆柱的左X坐标,圆柱的右X坐标
        float mCylinderLeft = 0.0f, mCylinderRight;
        //半圆的上Y坐标（用于绘制该圆柱对应的步数文字）
        float mRoundTop = 0.0f;

        for (int i = 0; i < mSportValueList.size(); i++) {
            num = mSportValueList.get(i);
            mUserCylinderNum = num - (mCylinderWidth / 2 / mItemHeight);
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

            //设置半圆top,bottom
            if (mUserCylinderNum <= 0.0f) {
                mRoundTop = topPadding + mItemHeight * (mMaxValue - num);
                mArcF.top = mRoundTop;
                mArcF.bottom = topPadding + mItemHeight * (mMaxValue - num) + mCylinderWidth;
            } else {
                mRoundTop = topPadding + mItemHeight * (mMaxValue - mUserCylinderNum) - mCylinderWidth / 2;
                mArcF.top = mRoundTop;
                mArcF.bottom = topPadding + mItemHeight * (mMaxValue - mUserCylinderNum) + mCylinderWidth / 2 + mCylinderWidth / 8;
            }
            //设置圆柱top,bottom
            if (mUserCylinderNum <= 0.0f) {
                if (num == mMaxValue) {
                    lg = getNegativeLG(mCylinderLeft, topPadding, mItemHeight, yLineHeight, num, mMaxTopColor, mMaxBottomColor);

                } else if (num == mMinValue) {
                    lg = getNegativeLG(mCylinderLeft, topPadding, mItemHeight, yLineHeight, num, mMinTopColor, mMinBottomColor);

                } else {
                    lg = getNegativeLG(mCylinderLeft, topPadding, mItemHeight, yLineHeight, num, mCenterTopColor, mCenterBottomColor);

                }

                mCylinderF.top = topPadding + yLineHeight;
                mCylinderF.bottom = topPadding + yLineHeight;
            } else {
                if (num == mMaxValue) {
                    lg = getPositiveLG(mCylinderLeft, topPadding, mItemHeight, yLineHeight, mUserCylinderNum, mMaxTopColor, mMaxBottomColor);
                } else if (num == mMinValue) {
                    lg = getPositiveLG(mCylinderLeft, topPadding, mItemHeight, yLineHeight, mUserCylinderNum, mMinTopColor, mMinBottomColor);
                } else {
                    lg = getPositiveLG(mCylinderLeft, topPadding, mItemHeight, yLineHeight, mUserCylinderNum, mCenterTopColor, mCenterBottomColor);
                }

                mCylinderF.top = topPadding + mItemHeight * (mMaxValue - mUserCylinderNum);
                mCylinderF.bottom = topPadding + yLineHeight;
            }

            //绘制半圆
            mPaint.reset();
            mPaint.setStyle(Paint.Style.FILL);
            if (num == mMaxValue) {
                mPaint.setColor(mMaxTopColor);
            } else if (num == mMinValue) {
                mPaint.setColor(mMinTopColor);
            } else {
                mPaint.setColor(mCenterTopColor);
            }

            canvas.drawArc(mArcF, -180, 180, true, mPaint);
            //绘制圆柱
            mPaint.reset();
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setAntiAlias(true);
            mPaint.setShader(lg);
            canvas.drawRect(mCylinderF, mPaint);
            //当用户长按柱状图后显示当前柱状图对应的运动数据
            Log.e("TAG", "draw");
            if (isLongPress && mTouchIndex == i && (mTouchIndex < mSportValueList.size())) {
                String value = mSportValueList.get(mTouchIndex) + "";
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

    private LinearGradient getNegativeLG(float mCylinderLeft, float topPadding, float mItemHeight, float yLineHeight, int num, int topColor, int bottomColor) {
        return new LinearGradient(mCylinderLeft,
                topPadding + mItemHeight * (mMaxValue - num) + ((topPadding + yLineHeight - (topPadding + mItemHeight * (mMaxValue - num))) / 2),
                mCylinderLeft,
                topPadding + yLineHeight,
                topColor, bottomColor, Shader.TileMode.MIRROR);
    }

    private LinearGradient getPositiveLG(float mCylinderLeft, float topPadding, float mItemHeight, float yLineHeight, float mUserCylinderNum, int topColor, int bottomColor) {
        return new LinearGradient(mCylinderLeft,
                topPadding + mItemHeight * (mMaxValue - mUserCylinderNum),
                mCylinderLeft,
                topPadding + yLineHeight,
                topColor, bottomColor, Shader.TileMode.MIRROR);
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
        for (int i = 0; i < mSportValueList.size(); i++) {
            float left = yLinePadding + mStartPadding + (mCylinderWidth + mCylinderPadding) * i;
            float right = yLinePadding + mStartPadding + (mCylinderWidth + mCylinderPadding) * i + mCylinderWidth;
            if (touchX >= left && touchX <= right) {
                mTouchIndex = i;
                break;
            } else {
                mTouchIndex = -1;
            }
        }
        if (mTouchIndex >= 0 && mSportValueList.size() > mTouchIndex) {
            String value = mSportValueList.get(mTouchIndex) + "";
            Log.e("TAG", "value=" + value);
            postInvalidate();
        }

    }

    //生成测试数据
    private ArrayList<Integer> getTestValues() {
        mSportValueList = new ArrayList<>();
        Random random = new Random();
        int num;
        for (int i = 0; i < 7; i++) {
            num = random.nextInt(150000);
            if (mMaxValue < num) {
                mMaxValue = num;
            }
            if (mMinValue > num) {
                mMinValue = num;
            }
            mSportValueList.add(num);
        }
        return mSportValueList;
    }
}

package com.test.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
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

public class ChartPolyLineView extends View {
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
    //是否是公里制单位
    private boolean isKmUnit = true;

    public ChartPolyLineView(Context context) {
        this(context, null);
    }

    public ChartPolyLineView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChartPolyLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ty = context.obtainStyledAttributes(attrs, R.styleable.polyline, defStyleAttr, 0);
        mChartType = ty.getInt(R.styleable.polyline_chart_type, 0);
        mTopColor = ty.getColor(R.styleable.polyline_polyline_top_color, getResources().getColor(R.color.chart_polyline_speed_top_color));
        mBottomColor = ty.getColor(R.styleable.polyline_polyline_bottom_color, getResources().getColor(R.color.chart_polyline_speed_bottom_color));
        mTitleTest = getResources().getString(ty.getResourceId(R.styleable.polyline_polyline_title, -1));
        mLeftMessageTest = getResources().getString(ty.getResourceId(R.styleable.polyline_polyline_left_message, -1));
        mRigthMessageTest = getResources().getString(ty.getResourceId(R.styleable.polyline_polyline_right_message, -1));
        ty.recycle();

        /*mSportDuration = createSportDuration();
        mSportValueList = createSportTestValue();*/

        initData();

        mPaint = new Paint();
        isKmUnit = true;//UserInfoBLL.getInstance().isKilometreUnit();

        if (mChartType == CHART_TYPE_SPEED) {
            if (isKmUnit) {
                mUnitText = "km/h";
            } else {
                mUnitText = "mile/h";
            }

            mLeftValue = mMinMaxValue[1] + "";
            mRightValue = mMinMaxValue[2] + "";

        } else if (mChartType == CHART_TYPE_HEART) {
            mUnitText = "BMP";
            mLeftValue = mMinMaxValue[1] + "";
            mRightValue = mMinMaxValue[2] + "";
        } else if (mChartType == CHART_TYPE_HIGH) {
            if (isKmUnit) {
                mUnitText = "m";
            } else {
                mUnitText = "ft";
            }
            mLeftValue = mMinMaxValue[1] + "";
            mRightValue = mMinMaxValue[2] + "";
        } else if (mChartType == CHART_TYPE_FREQ) {
            mUnitText = getResources().getString(R.string.unit_freq);
            mLeftValue = mMinMaxValue[1] + "";
            mRightValue = mMinMaxValue[2] + "";
        } else {
            mUnitText = "℃";
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

        if (mChartType == CHART_TYPE_HIGH && mMinSportValue < 0) {
            //如果海拔出现负数，则起点就不是0了，而是负数最小值
            mScaleItemY = mAxisHeight / (mMaxSportValue + Math.abs(mMinSportValue));
        } else {
            mScaleItemY = mAxisHeight / mMaxSportValue;
        }

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
        String[] mScaleArr;
        if (mMaxSportValue > 3) {
            mScaleValue = mMaxSportValue / 3.0f;
            if (mChartType == CHART_TYPE_SPEED) {
                mScaleArr = new String[]{Unit.getFloatScale(1, mMaxSportValue) + "",
                        Unit.getFloatScale(1, mScaleValue * 2) + "",
                        Unit.getFloatScale(1, mScaleValue) + "", "0"};
            } else if (mChartType == CHART_TYPE_HIGH && mMinSportValue < 0) {
                //当海拔出现负数时,Y坐标轴需要展示出负数
                float mHighScaleValue = (mMaxSportValue + Math.abs(mMinSportValue)) / 3.0f;
                mScaleArr = new String[]{Math.round(mMaxSportValue) + "",
                        Math.round(mMaxSportValue - mHighScaleValue) + "",
                        Math.round(mMaxSportValue - mHighScaleValue * 2) + "",
                        Math.round(mMinSportValue) + ""};
            } else {
                mScaleArr = new String[]{mMaxSportValue + "",
                        Math.round(mScaleValue * 2) + "",
                        Math.round(mScaleValue) + "", "0"};
            }

        } else {
            if (mMaxSportValue == 0) {
                mMaxSportValue = 1;
            }
            mScaleArr = new String[]{mMaxSportValue + "", "0"};
        }

        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(getResources().getColor(android.R.color.white));
        mPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));

        if (mChartType == CHART_TYPE_HIGH && mMinSportValue < 0 && mScaleArr.length > 0) {
            //如果还不为负数
            for (int i = 0; i < mScaleArr.length; i++) {
                String value = mScaleArr[i];
                float textWidth = mPaint.measureText(value);
                canvas.drawText(value, mScaleTestRightPadding - textWidth,
                        chartEndY - mScaleItemY * (Float.valueOf(value) + Math.abs(mMinSportValue)) + mPaint.getFontMetrics().bottom, mPaint);
            }
        } else {
            for (int i = 0; i < mScaleArr.length; i++) {
                String value = mScaleArr[i];
                float textWidth = mPaint.measureText(value);
                canvas.drawText(value, mScaleTestRightPadding - textWidth,
                        chartEndY - mScaleItemY * Math.abs(Float.valueOf(value)) + mPaint.getFontMetrics().bottom, mPaint);
            }
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
            if (mChartType == CHART_TYPE_HIGH && mMinSportValue < 0) {
                //如果是海拔有负数
                path.moveTo(mAxisStartX, chartEndY - (mSportValueList.get(0) + Math.abs(mMinSportValue)) * mScaleItemY);
                for (int i = 0; i < mSportValueList.size(); i++) {
                    path.lineTo(mAxisStartX + mScaleItemX * (i * 1.0f), chartEndY - (mSportValueList.get(i) + Math.abs(mMinSportValue)) * mScaleItemY);
                    if (i == mSportValueList.size() - 1) {
                        path.lineTo(mAxisStartX + mScaleItemX * (i * 1.0f), chartEndY);
                        path.lineTo(mAxisStartX, chartEndY);
                    }
                }
            } else {
                path.moveTo(mAxisStartX, chartEndY - Math.abs(mSportValueList.get(0) * mScaleItemY));
                for (int i = 0; i < mSportValueList.size(); i++) {
                    path.lineTo(mAxisStartX + mScaleItemX * (i * 1.0f), chartEndY - Math.abs(mSportValueList.get(i) * mScaleItemY));
                    if (i == mSportValueList.size() - 1) {
                        path.lineTo(mAxisStartX + mScaleItemX * (i * 1.0f), chartEndY);
                        path.lineTo(mAxisStartX, chartEndY);
                    }
                }
            }
        }
        LinearGradient lg = new LinearGradient(mAxisStartX, chartEndY - Math.abs(Float.parseFloat(mMinMaxValue[2]) * mScaleItemY),
                mAxisStartX, chartEndY, mTopColor, mBottomColor, Shader.TileMode.MIRROR);

        mPaint.setShader(lg);
        canvas.drawPath(path, mPaint);

        //绘制平均分割线
        if (mSportValueList.size() > 0) {
            mPaint.reset();
            mPaint.setAntiAlias(true);
            mPaint.setColor(Color.parseColor("#7ff0f0f0"));
            mPaint.setStrokeWidth(1.5f);
            if (mChartType == CHART_TYPE_HIGH && mMinSportValue < 0) {
                canvas.drawLine(mAxisStartX, chartEndY - (Float.valueOf(mMinMaxValue[1]) + Math.abs(mMinSportValue)) * mScaleItemY,
                        mAxisStartX + mAxisWidth, chartEndY - (Float.valueOf(mMinMaxValue[1]) + Math.abs(mMinSportValue)) * mScaleItemY, mPaint);
            } else {
                canvas.drawLine(mAxisStartX, chartEndY - Math.abs(Float.valueOf(mMinMaxValue[1]) * mScaleItemY),
                        mAxisStartX + mAxisWidth, chartEndY - Math.abs(Float.valueOf(mMinMaxValue[1]) * mScaleItemY), mPaint);
            }
        }
    }

    private void initData() {
        mMinMaxValue = new String[]{"0", "0", "0"};
        mSportValueList = new ArrayList<>();
        mSportDuration = 0;
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

    /**
     * 根据数据绘制折线图
     *
     * @param duration     运动总时长
     * @param data         运动数据
     * @param averageSpeed 如果是速度折线图的话，则平均速度通过公式：路程/时间得出，如果速度的话，则参数传递为-1
     */
    public void updatePolyLineView(int duration, String data, double averageSpeed) {
        mMaxSportValue = Integer.MIN_VALUE;
        mMinSportValue = Integer.MAX_VALUE;
        mSportDuration = duration;
        mMinMaxValue = new String[]{"0", "0", "0"};//最小，平均，最大
        mSportValueList.clear();
        mSportValueList = Unit.string2InterceptFloatArrayList(data);
        //int[] mValueData = Utils.getAverageAndMaxExceptZone(data);
        int mSum = 0;
        for (int i = 0; i < mSportValueList.size(); i++) {
            int num = mSportValueList.get(i);
            mSum += num;
            if (mMaxSportValue < num) {
                mMaxSportValue = num;
            }
            if (mMinSportValue > num) {
                mMinSportValue = num;
            }
        }


        if (mSportValueList.size() > 0) {
            mMinMaxValue[0] = mMinSportValue + "";
            mMinMaxValue[1] = (mSum / mSportValueList.size()) + "";
            mMinMaxValue[2] = mMaxSportValue + "";
        }

        //如果平均速度>0的话，说明当前要显示的是速度曲线
        if (averageSpeed > 0) {
            mMinMaxValue[1] = averageSpeed + "";
        }

        if (mMaxSportValue <= 0) {
            mMaxSportValue = 1;
        } else {
            mMaxSportValue = 3 * mMaxSportValue / 2;
        }

        if (mChartType == CHART_TYPE_SPEED) {
            if (isKmUnit) {
                mLeftValueText = Unit.getDoubleScale(2, Double.valueOf(mMinMaxValue[1])) + "" + mUnitText;
                mRightValueText = Unit.getDoubleScale(2, Double.valueOf(mMinMaxValue[2])) + "" + mUnitText;
                mLeftValue = Unit.getDoubleScale(2, Double.valueOf(mMinMaxValue[1])) + "";
                mRightValue = Unit.getDoubleScale(2, Double.valueOf(mMinMaxValue[2])) + "";
            } else {
                mLeftValueText = Unit.getDoubleScale(2, Double.valueOf(mMinMaxValue[1]) * 0.6213711d) + "" + mUnitText;
                mRightValueText = Unit.getDoubleScale(2, Double.valueOf(mMinMaxValue[2]) * 0.6213711d) + "" + mUnitText;
                mLeftValue = Unit.getDoubleScale(2, Double.valueOf(mMinMaxValue[1]) * 0.6213711d) + "";
                mRightValue = Unit.getDoubleScale(2, Double.valueOf(mMinMaxValue[2]) * 0.6213711d) + "";
            }

        } else if (mChartType == CHART_TYPE_HEART) {
            mLeftValueText = mMinMaxValue[1] + "" + mUnitText;
            mRightValueText = mMinMaxValue[2] + "" + mUnitText;
            mLeftValue = mMinMaxValue[1] + "";
            mRightValue = mMinMaxValue[2] + "";

            //如果是心率的话，要根据运动时间的分钟数产生多几个随机数，以便能保证1分钟2个心率数据
            //手表上传的心率值个数
            int mHeartValueSize = mSportValueList.size();
            //心率值总数（1分钟3个值）=手表上传心率个数+随机产生的个数
            //现在又改为总共150个点，不到的插入补齐，到了用手表的数据
            //int mHeartSize = (duration / 60) * 3;
            int mHeartSize = 150;
            int offset = mHeartSize - mSportValueList.size();
            if (offset > 0 && mHeartValueSize < 150 && mHeartValueSize > 0) {
                Random random = new Random();
                ArrayList<Integer> mTempList = (ArrayList<Integer>) mSportValueList.clone();
                mSportValueList.clear();
                //将随机产生的心率值，均匀插入手表上传的心率值中
                int offset1 = mHeartSize / mHeartValueSize;
                int index = 0;
                for (int i = 0; i < mHeartSize; i++) {
                    if (i == index * offset1 && index < mTempList.size()) {
                        mSportValueList.add(mTempList.get(index));
                        if (index + 1 < mTempList.size()) {
                            index++;
                        }
                    } else {
                        int[] minMax;
                        if (index <= 0) {
                            minMax = Unit.getMinMaxValue(mTempList.get(0), mTempList.get(index));
                        } else {
                            minMax = Unit.getMinMaxValue(mTempList.get(index - 1), mTempList.get(index));
                        }
                        int heart = random.nextInt(minMax[1]) % (minMax[1] - minMax[0] + 1) + minMax[0];
                        mSportValueList.add(heart);
                    }
                }

            }
        } else if (mChartType == CHART_TYPE_HIGH) {
            if (isKmUnit) {
                mLeftValueText = mMinMaxValue[0] + "" + mUnitText;
                mRightValueText = mMinMaxValue[2] + "" + mUnitText;
                mLeftValue = mMinMaxValue[0] + "";
                mRightValue = mMinMaxValue[2] + "";
            } else {
                mLeftValueText = Math.round(Float.valueOf(mMinMaxValue[0]) * 3.2808399f) + "" + mUnitText;
                mRightValueText = Math.round(Float.valueOf(mMinMaxValue[2]) * 3.2808399f) + "" + mUnitText;
                mLeftValue = Math.round(Float.valueOf(mMinMaxValue[0]) * 3.2808399f) + "";
                mRightValue = Math.round(Float.valueOf(mMinMaxValue[2]) * 3.2808399f) + "";
            }

        } else if (mChartType == CHART_TYPE_FREQ) {
            mLeftValueText = mMinMaxValue[1] + "" + mUnitText;
            mRightValueText = mMinMaxValue[2] + "" + mUnitText;
            mLeftValue = mMinMaxValue[1] + "";
            mRightValue = mMinMaxValue[2] + "";
        } else {
            mLeftValueText = mMinMaxValue[1] + "" + mUnitText;
            mRightValueText = mMinMaxValue[0] + "" + mUnitText;
            mLeftValue = mMinMaxValue[1] + "";
            mRightValue = mMinMaxValue[0] + "";
        }
        Log.e("TAG", "ave=" + mMinMaxValue[0] + " min=" + mMinMaxValue[1] + " max=" + mMinMaxValue[2]);
        postInvalidate();
    }

}

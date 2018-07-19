package com.test.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.test.mytest.R;

/**
 * Created by Administrator on 2017/12/5.
 */

public class CustomSportInfoCardView extends LinearLayout {
    //上下2层顶级父视图
    private LinearLayout mTopParentView, mBottomParentView;
    //上层左、右侧父视图
    private LinearLayout mTopLeftParentView, mTopRightParentView;
    //下层上、下父视图
    private LinearLayout mBottomTopParentView, mBottomBottomParentView;
    //用户头像
    private ImageView mHeadImg;
    //用户名称
    private TextView mNameText;
    //右上角2个View，比如：开始时间，12-50 11:15:58
    private TextView mTopRightMessageOneText, mTopRightMessageTwoText;
    //下层顶部左中右3个View
    private TextView mBottomTopMessageOneText, mBottomTopMessageTwoText, mBottomTopMessageThressText;
    //下层底部左右2个View
    private TextView mBottomBottomMessageOneText, mBottomBottomMessageTwoText;

    //上下2层顶级父视图布局规则
    private LayoutParams mTopParentLP, mBottomParentLP;
    //上层左、右父视图布局规则
    private LayoutParams mTopLeftParentLP, mTopRightParentLP;
    //下层上、下父视图布局规则
    private LayoutParams mBottomTopLP, mBottomBottomLP;
    //头像、用户名布局规则
    private LayoutParams mHeadLP, mNameLP;
    //右上角2个View布局规则
    private LayoutParams mTopRightOneLP, mTopRightTwoLP;
    //下层顶部左中右3个View布局规则
    private LayoutParams mBottomTopOneLP, mBottomTopTwoLP, mBottomTopThressLP;
    //下层底部左右2个View布局规则
    private LayoutParams mBottomBottomOneLP, mBottomBottomTwoLP;

    public CustomSportInfoCardView(Context context) {
        this(context, null);
    }

    public CustomSportInfoCardView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomSportInfoCardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView(context);
    }

    private void initView(Context mContext) {
        this.setOrientation(LinearLayout.VERTICAL);

        //顶层父视图
        mTopParentView = new LinearLayout(mContext);
        mTopParentView.setOrientation(LinearLayout.HORIZONTAL);

        //顶层左父视图
        mTopLeftParentView = new LinearLayout(mContext);
        mTopLeftParentView.setOrientation(LinearLayout.HORIZONTAL);
        mTopLeftParentView.setGravity(Gravity.CENTER_VERTICAL);

        //用户头像
        mHeadImg = new ImageView(mContext);
        mHeadImg.setImageDrawable(getResources().getDrawable(R.drawable.test));
        mHeadImg.setScaleType(ImageView.ScaleType.FIT_XY);
        mHeadLP = new LayoutParams((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics()),
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics()));
        //mHeadLP.setMargins(Utils.dp2px(mContext, 10), 0, 0, 0);
        mHeadLP.gravity = Gravity.CENTER_VERTICAL;
        mTopLeftParentView.addView(mHeadImg, mHeadLP);

        //用户昵称
        mNameText = new TextView(mContext);
        mNameText.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 8, getResources().getDisplayMetrics()));
        mNameText.setTextColor(ContextCompat.getColor(mContext, android.R.color.white));
        mNameText.setText("Aundiy");
        mNameText.setGravity(Gravity.BOTTOM);
        mNameText.setMaxEms(3);
        mNameText.setSingleLine();
        mNameText.setEllipsize(TextUtils.TruncateAt.END);
        mNameLP = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics()));
        mNameLP.setMargins(10, 0, 0, 0);
        mTopLeftParentView.addView(mNameText, mNameLP);

        //将顶层左侧父视图添加到顶层父视图中
        mTopLeftParentLP = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        mTopLeftParentLP.weight = 1;
        mTopParentView.addView(mTopLeftParentView, mTopLeftParentLP);

        //顶层右侧父视图
        mTopRightParentView = new LinearLayout(mContext);
        mTopRightParentView.setOrientation(LinearLayout.VERTICAL);
        mTopRightParentView.setGravity(Gravity.BOTTOM);

        //顶层右侧第一个消息
        mTopRightMessageOneText = new TextView(mContext);
        mTopRightMessageOneText.setTextColor(getResources().getColor(android.R.color.white));
        mTopRightMessageOneText.setText("开始时间");
        mTopRightMessageOneText.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 5, getResources().getDisplayMetrics()));
        mTopRightMessageOneText.setGravity(Gravity.BOTTOM);
        mTopRightOneLP = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mTopRightOneLP.gravity = Gravity.RIGHT;
        mTopRightParentView.addView(mTopRightMessageOneText, mTopRightOneLP);

        //顶层右侧第二个消息
        mTopRightMessageTwoText = new TextView(mContext);
        mTopRightMessageTwoText.setText("12-04 12:25:36");
        mTopRightMessageTwoText.setGravity(Gravity.BOTTOM);
        mTopRightMessageTwoText.setTextColor(getResources().getColor(android.R.color.white));
        mTopRightMessageTwoText.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 5, getResources().getDisplayMetrics()));
        mTopRightTwoLP = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mTopRightTwoLP.gravity = Gravity.RIGHT;
        mTopRightParentView.addView(mTopRightMessageTwoText, mTopRightTwoLP);

        //将顶层右侧父视图添加到顶层父视图中
        mTopRightParentLP = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics()));
        mTopRightParentLP.setMargins(0, 0,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()), 0);
        mTopRightParentLP.gravity = Gravity.CENTER_VERTICAL;
        mTopParentView.addView(mTopRightParentView, mTopRightParentLP);

        //将顶层父视图添加到View中
        mTopParentLP = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
        mTopParentLP.weight = 1;
        this.addView(mTopParentView, mTopParentLP);

        //上下层分隔线
        View view = new View(mContext);
        view.setBackgroundColor(getResources().getColor(R.color.chart_axis_color));
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 2, getResources().getDisplayMetrics()));
        lp.setMargins((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()),
                0,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()),
                0);
        this.addView(view, lp);
        //底层父视图
        mBottomParentView = new LinearLayout(mContext);
        mBottomParentView.setOrientation(LinearLayout.VERTICAL);

        //底层上层父视图
        mBottomTopParentView = new LinearLayout(mContext);
        mBottomTopParentView.setOrientation(LinearLayout.HORIZONTAL);

        //底层上层中的消息1，2，3
        mBottomTopMessageOneText = new TextView(mContext);
        mBottomTopMessageOneText.setGravity(Gravity.LEFT);
        mBottomTopMessageOneText.setText("里程:215.0KM");
        mBottomTopMessageOneText.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 5, getResources().getDisplayMetrics()));
        mBottomTopMessageOneText.setTextColor(getResources().getColor(android.R.color.white));
        mBottomTopOneLP = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        mBottomTopOneLP.weight = 1;
        mBottomTopParentView.addView(mBottomTopMessageOneText, mBottomTopOneLP);

        mBottomTopMessageTwoText = new TextView(mContext);
        mBottomTopMessageTwoText.setGravity(Gravity.CENTER);
        mBottomTopMessageTwoText.setText("高度：125M");
        mBottomTopMessageTwoText.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 5, getResources().getDisplayMetrics()));
        mBottomTopMessageTwoText.setTextColor(getResources().getColor(android.R.color.white));
        mBottomTopTwoLP = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        mBottomTopTwoLP.weight = 1;
        mBottomTopParentView.addView(mBottomTopMessageTwoText, mBottomTopTwoLP);

        mBottomTopMessageThressText = new TextView(mContext);
        mBottomTopMessageThressText.setGravity(Gravity.RIGHT);
        mBottomTopMessageThressText.setText("时间：12:15:36");
        mBottomTopMessageThressText.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 5, getResources().getDisplayMetrics()));
        mBottomTopMessageThressText.setTextColor(getResources().getColor(android.R.color.white));
        mBottomTopThressLP = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        mBottomTopThressLP.weight = 1;
        mBottomTopParentView.addView(mBottomTopMessageThressText, mBottomTopThressLP);


        //将底层上层父视图添加到底层父视图中
        mBottomTopLP = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        mBottomTopLP.weight = 1;
        mBottomParentView.addView(mBottomTopParentView, mBottomTopLP);

        //底层下层父视图
        mBottomBottomParentView = new LinearLayout(mContext);
        mBottomBottomParentView.setOrientation(LinearLayout.HORIZONTAL);

        //底层的2个消息视图
        mBottomBottomMessageOneText = new TextView(mContext);
        mBottomBottomMessageOneText.setGravity(Gravity.LEFT);
        mBottomBottomMessageOneText.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 5, getResources().getDisplayMetrics()));
        mBottomBottomMessageOneText.setTextColor(getResources().getColor(android.R.color.white));
        mBottomBottomMessageOneText.setText("平均心率：110MPB");
        mBottomBottomOneLP = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        mBottomBottomOneLP.weight = 1;
        mBottomBottomParentView.addView(mBottomBottomMessageOneText, mBottomBottomOneLP);

        mBottomBottomMessageTwoText = new TextView(mContext);
        mBottomBottomMessageTwoText.setGravity(Gravity.RIGHT);
        mBottomBottomMessageTwoText.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 5, getResources().getDisplayMetrics()));
        mBottomBottomMessageTwoText.setTextColor(getResources().getColor(android.R.color.white));
        mBottomBottomMessageTwoText.setText("平均速度：120.25km/h");
        mBottomBottomTwoLP = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        mBottomBottomTwoLP.weight = 1;
        mBottomBottomParentView.addView(mBottomBottomMessageTwoText, mBottomBottomOneLP);


        //将底层下层父视图添加到底层父视图中
        mBottomBottomLP = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        mBottomBottomLP.weight = 1;
        mBottomParentView.addView(mBottomBottomParentView, mBottomBottomLP);

        mBottomParentLP = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
        mBottomParentLP.weight = 1;
        this.addView(mBottomParentView, mBottomParentLP);
    }
}

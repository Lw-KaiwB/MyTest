package com.test.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.MenuPopupWindow;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.LinearInterpolator;

import com.test.unit.Utils;

public class MaterialEditText extends AppCompatEditText {
    private static final long ANIMATION_DURATION = 300;
    private float PADDING_LEFT = Utils.dip2px(2);
    private float PADDING_TOP = Utils.dip2px(8);
    private float PADDING_MOVE = Utils.dip2px(15);
    private float TEXT_SIZE = Utils.dip2px(16);
    private float WHOLE_PADDING_TOP = PADDING_TOP+TEXT_SIZE+Utils.dip2px(10);

    private float labelFraction = 0;
    private boolean isShow = false;
    private int textColor = Color.BLACK;

    private Paint mPaint;
    private ObjectAnimator mAnimator;
    public MaterialEditText(Context context) {
        super(context);
    }

    public MaterialEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray =context.obtainStyledAttributes(attrs,new int[]{android.R.attr.textColorLink});

        textColor = typedArray.getColor(0, Color.BLACK);
        typedArray.recycle();
        mPaint.setColor(textColor);
    }

    public MaterialEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(TEXT_SIZE);
        setPadding(getPaddingLeft(),(int)WHOLE_PADDING_TOP,getPaddingRight(),getPaddingBottom());

        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() >0 && !isShow){
                    isShow = true;
                    getAnimator().start();
                }else if (editable.toString().length() ==0&&isShow){
                    isShow = false;
                    getAnimator().reverse();
                }
            }
        });
    }

    public float getLabelFraction() {
        return labelFraction;
    }

    public void setLabelFraction(float labelFraction) {
        this.labelFraction = labelFraction;
        invalidate();
    }

    private ObjectAnimator getAnimator(){
        if (mAnimator ==null){
            mAnimator = ObjectAnimator.ofFloat(MaterialEditText.this,"labelFraction",1);
            mAnimator.setDuration(ANIMATION_DURATION);
            mAnimator.setInterpolator(new LinearInterpolator());
        }
        return mAnimator;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


            Log.e("TAG","lableFraction="+labelFraction);
            mPaint.setAlpha((int) (labelFraction * 0xff));
            float padding = (1-labelFraction) * (PADDING_MOVE);
            canvas.drawText(getHint().toString(),PADDING_LEFT,PADDING_TOP+TEXT_SIZE+padding,mPaint);
            mPaint.setAlpha(1);

    }
}

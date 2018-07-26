package com.test.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.test.unit.Utils;

public class CircleImageView extends AppCompatImageView {

    private final int PADDING = (int) Utils.dip2px(50);
    private final int RADIUS = (int) Utils.dip2px(100);
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    public CircleImageView(Context context) {
        super(context);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int size = (RADIUS+PADDING)*2;
        setMeasuredDimension(size,size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.parseColor("#EC407E"));
        canvas.drawCircle(PADDING+RADIUS,PADDING+RADIUS,RADIUS,mPaint);

    }
}

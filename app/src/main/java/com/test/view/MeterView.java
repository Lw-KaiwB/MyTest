package com.test.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2018/7/17.
 */

public class MeterView extends View {
	private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Path mPath = new Path();
	String s = "A";

	public MeterView(Context context) {
		super(context);
	}

	public MeterView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
	}

	public MeterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		/*mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(10);
		mPath.reset();
		mPath.addArc(new RectF(100, 100, 800, 800), 150, 240);
		canvas.drawPath(mPath, mPaint);

		for (int i = 150; i <= 390; i++) {
			if (i % 20 == 0) {
				mPaint.setStrokeWidth(20);
				canvas.drawArc(100 + 20, 100 + 20, 800 - 20, 800 - 20,
						i, 1,
						false, mPaint);
			} else if (i % 10 == 0) {
				mPaint.setStrokeWidth(50);
				canvas.drawArc(100 + 20, 100 + 20, 800 - 20, 800 - 20,
						i, 1,
						false, mPaint);
			}
		}
		PathMeasure pathMeasure = new PathMeasure(mPath,false);
		float arcLength = pathMeasure.getLength();
		float length = arcLength / 12.0f;
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setTextSize(40);
		for (int i = 1; i < 12; i++) {
			canvas.drawTextOnPath(i * 10 + "", mPath, length * i-20, 80, mPaint);
		}
		canvas.drawTextOnPath("0", mPath, -10, 80, mPaint);
		canvas.drawTextOnPath("120", mPath, arcLength-50, 80, mPaint);

		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(5);
		canvas.drawTextOnPath("A", mPath, -10, 120, mPaint);
		canvas.drawArc(200,200,600,600,150,1,true,mPaint);*/

		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(10);
		mPath.reset();
		float left = getWidth() / 6;
		float right = getWidth() - left;
		float top = left;
		float bottom = right;
		mPath.addArc(new RectF(left, top, right, bottom), 150, 240);
		canvas.drawPath(mPath, mPaint);

		for (int i = 150; i <= 390; i++) {
			if (i % 20 == 0) {
				mPaint.setStrokeWidth(20);
				canvas.drawArc(left + 20, top + 20, right - 20, bottom - 20,
						i, 1,
						false, mPaint);
			} else if (i % 10 == 0) {
				mPaint.setStrokeWidth(50);
				canvas.drawArc(left + 20, top + 20, right - 20, bottom - 20,
						i, 1,
						false, mPaint);
			}
		}
		PathMeasure pathMeasure = new PathMeasure(mPath, false);
		float arcLength = pathMeasure.getLength();
		float length = arcLength / 12.0f;
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setTextSize(40);
		for (int i = 1; i < 12; i++) {
			canvas.drawTextOnPath(i * 10 + "", mPath, length * i - 20, 80, mPaint);
		}
		canvas.drawTextOnPath("0", mPath, -10, 80, mPaint);
		canvas.drawTextOnPath("120", mPath, arcLength - 50, 80, mPaint);

		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(5);
		canvas.drawTextOnPath("A", mPath, -10, 120, mPaint);

		canvas.drawArc(left +100, top+100, right-100, bottom-100, 150, 1, true, mPaint);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}

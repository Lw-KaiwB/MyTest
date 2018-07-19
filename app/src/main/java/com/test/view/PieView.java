package com.test.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;


/**
 * Created by Administrator on 2018/7/17.
 */

public class PieView extends View {
	private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

	public PieView(Context context) {
		super(context);
	}

	public PieView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
	}

	public PieView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		float left = getWidth() / 6;
		float top = left;
		float right = getWidth() - left;
		float bottom = right;

		mPaint.setColor(getResources().getColor(android.R.color.holo_red_light));
		canvas.drawArc(left, top, right, bottom, 300, 60, true, mPaint);
		mPaint.setColor(getResources().getColor(android.R.color.holo_blue_light));
		canvas.drawArc(left, top, right, bottom, 0, 70, true, mPaint);
		mPaint.setColor(getResources().getColor(android.R.color.darker_gray));
		canvas.drawArc(left, top, right, bottom, 70, 120, true, mPaint);

		mPaint.setColor(getResources().getColor(android.R.color.holo_orange_light));
		canvas.drawArc(left-10, top-20, right-10, bottom-20, 190, 110, true, mPaint);

		mPaint.setTextSize(40);
		mPaint.setStrokeWidth(5);
		mPaint.setColor(getResources().getColor(android.R.color.holo_red_light));
		canvas.drawText("30.56%",150,150,mPaint);
		canvas.drawLine(150,160,280,160,mPaint);
		canvas.drawLine(280,160,450,350,mPaint);


		mPaint.setColor(getResources().getColor(android.R.color.holo_green_light));
		canvas.drawText("16.67%",900,350,mPaint);
		canvas.drawLine(900,360,1000,360,mPaint);
		canvas.drawLine(780,450,900,360,mPaint);



		mPaint.setColor(getResources().getColor(android.R.color.holo_orange_light));
		canvas.drawText("33.33%",150,950,mPaint);

		canvas.drawLine(150,980,300,980,mPaint);
		canvas.drawLine(300,980,450,700,mPaint);

		mPaint.setColor(getResources().getColor(android.R.color.black));
		canvas.drawText("19.44%",900,750,mPaint);

		canvas.drawLine(850,780,980,780,mPaint);
		canvas.drawLine(750,680,850,780,mPaint);

	}
}

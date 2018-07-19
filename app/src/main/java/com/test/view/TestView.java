package com.test.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.test.mytest.R;

/**
 * Created by Administrator on 2018/1/6.
 */

public class TestView extends View {
	private Paint mPaint;
	private Path mPath;
	private Bitmap mBitmap;
	private Matrix mMatrix;
	private int angle = 360;
	private String name="s";
	private int color = 0xff00ffff;

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
		invalidate();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		invalidate();
	}

	public int getAngle() {
		return angle;
	}

	public void setAngle(int angle) {
		this.angle = angle;
		invalidate();
	}

	public TestView(Context context) {
		this(context, null);
	}

	public TestView(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mPaint = new Paint();
	}

	{
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPath = new Path();
		mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.aodamiao);
		mMatrix = new Matrix();
		mMatrix.setTranslate(-mBitmap.getWidth() / 2, -mBitmap.getHeight() / 2);

		mMatrix.setRotate(180);
		mMatrix.setTranslate(mBitmap.getWidth() / 2, mBitmap.getHeight() / 2);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		mPath.reset();
		mPaint.setStrokeWidth(2);
		mPaint.setColor(color);
		for (int i = 0; i < getWidth(); i++) {
			if (i % 300 == 0) {
				canvas.drawLine(i, 0, i, getHeight(), mPaint);
			}
		}
		for (int i = 0; i < getHeight(); i++) {
			if (i % 300 == 0) {
				canvas.drawLine(0, i, getWidth(), i, mPaint);
			}
		}
		Log.e("TAG", "width=" + mBitmap.getWidth() + " height=" + mBitmap.getHeight() + " screenWidth=" + getWidth());
		//canvas.clipOutRect(100, 100, 556, 556);
		//mPath.addCircle(328, 328, 250, Path.Direction.CW);
		mPath.addArc(0, 0, 656, 656, 0, angle);
		//mPath.addPath(mPath,mMatrix);
		mMatrix.reset();
		/*mMatrix.preRotate(90);
		mMatrix.preTranslate(mBitmap.getWidth()/3,0);
		mMatrix.preTranslate(0,mBitmap.getWidth()/3);
		mMatrix.setTranslate(0,mBitmap.getHeight());*/

		/*mMatrix.postTranslate(mBitmap.getWidth(),0);
		mMatrix.preTranslate(0,mBitmap.getHeight());
		mMatrix.preTranslate(0,mBitmap.getHeight());*/

		/*mMatrix.preScale(2,2);
		mMatrix.postTranslate(200,200);*/
		/*mMatrix.reset();
		mMatrix.preScale(2,2);
		mMatrix.postTranslate(200,200);
		mMatrix.postScale(2,2);
		canvas.concat(mMatrix);*/
		canvas.clipPath(mPath);
		canvas.drawBitmap(mBitmap, 0, 0, mPaint);

		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setStrokeWidth(50);
		mPaint.setColor(getResources().getColor(android.R.color.darker_gray));
		canvas.drawPoint(0, 0, mPaint);


		mPaint.setTextSize(30);
		mPaint.setColor(Color.RED);
		canvas.drawText(name,300,500,mPaint);

		/*Drawable drawable = getResources().getDrawable(R.drawable.aodamiao);

		BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
		Bitmap bitmap = bitmapDrawable.getBitmap();

		Drawable drawable1 = new BitmapDrawable(getResources(),mBitmap);*/
	}
}

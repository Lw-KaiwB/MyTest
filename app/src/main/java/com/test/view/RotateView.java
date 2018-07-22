package com.test.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.test.mytest.R;
import com.test.unit.Utils;

/**
 * Created by Administrator on 2018/7/19.
 * 1. 分成两块来绘制，clip 的和不 clip 的，调用两次 drawBitmap 来绘制，可以得到一边翻起一边不翻起的效果；
 * 2. 斜向的切割效果，不必使用 clipPath() 加上计算角度来做，可以先把 Canvas 做一下旋转，然后直接切割矩形区域。
 */

public class RotateView extends View {

	private Paint mPaint;
	private Bitmap mBitmap;
	private Camera mCamera;

	private int YDegree = 0;
	private int XDegree = 0;
	private int CanvasDegree = 0;
	private boolean isShowRectRotate = false;

	private final int IMG_PADDING = (int) Utils.dip2px(60);
	private int IMG_WIDTH = (int) Utils.dip2px(50);

	public boolean isShowRectRotate() {
		return isShowRectRotate;
	}

	public void setShowRectRotate(boolean showRectRotate) {
		isShowRectRotate = showRectRotate;
	}

	public int getCanvasDegree() {
		return CanvasDegree;
	}

	public void setCanvasDegree(int canvasDegree) {
		CanvasDegree = canvasDegree;
		invalidate();
	}

	public int getYDegree() {
		return YDegree;
	}

	public void setYDegree(int yDegree) {
		this.YDegree = yDegree;
		invalidate();
	}

	public int getXDegree() {
		return XDegree;
	}

	public void setXDegree(int xDegree) {
		this.XDegree = xDegree;
		invalidate();
	}

	public RotateView(Context context) {
		super(context);
	}

	public RotateView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
	}

	public RotateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	{
		mCamera = new Camera();
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mBitmap = Utils.getBitmap(getResources(), IMG_WIDTH, R.drawable.aodamiao);
		IMG_WIDTH = mBitmap.getWidth();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		//这是好的右边往回弯折的效果
		/*canvas.save();
		canvas.translate((IMG_PADDING+IMG_WIDTH/2),(IMG_PADDING+IMG_WIDTH/2));
		mCamera.save();
		mCamera.rotate(XDegree,YDegree,0);
		mCamera.applyToCanvas(canvas);
		mCamera.restore();
		canvas.translate(-(IMG_PADDING+IMG_WIDTH/2),-(IMG_PADDING+IMG_WIDTH/2));
		canvas.drawBitmap(mBitmap,IMG_PADDING,IMG_PADDING,mPaint);
		canvas.restore();

		canvas.save();
		canvas.clipRect(IMG_PADDING,IMG_PADDING,IMG_PADDING+IMG_WIDTH/2,IMG_PADDING+IMG_WIDTH);
		canvas.drawBitmap(mBitmap,IMG_PADDING,IMG_PADDING,mPaint);
		canvas.restore();*/


		canvas.save();
		canvas.translate((IMG_PADDING+IMG_WIDTH/2),(IMG_PADDING+IMG_WIDTH/2));
		mCamera.save();
		canvas.rotate(CanvasDegree);
		mCamera.rotate(XDegree,YDegree,0);
		mCamera.applyToCanvas(canvas);
		canvas.rotate(-CanvasDegree);
		mCamera.restore();

		canvas.translate(-(IMG_PADDING+IMG_WIDTH/2),-(IMG_PADDING+IMG_WIDTH/2));
		canvas.drawBitmap(mBitmap,IMG_PADDING,IMG_PADDING,mPaint);
		canvas.restore();

		/*canvas.save();
		canvas.rotate(CanvasDegree);
		canvas.clipRect(IMG_PADDING,IMG_PADDING,IMG_PADDING+IMG_WIDTH/2,IMG_PADDING+IMG_WIDTH);
		canvas.rotate(-CanvasDegree);
		canvas.drawBitmap(mBitmap,IMG_PADDING,IMG_PADDING,mPaint);
		canvas.restore();*/
		Log.e("TAG","CanvasDegree="+CanvasDegree);
		if (CanvasDegree!=-270){
			canvas.save();
			canvas.translate((IMG_PADDING+IMG_WIDTH/2),(IMG_PADDING+IMG_WIDTH/2));
			canvas.rotate(CanvasDegree);
			canvas.clipRect(-IMG_WIDTH/2-IMG_WIDTH/4,-IMG_WIDTH/2-IMG_WIDTH/4,0,IMG_WIDTH/2+IMG_WIDTH/4);
			canvas.rotate(-CanvasDegree);
			canvas.translate(-(IMG_PADDING+IMG_WIDTH/2),-(IMG_PADDING+IMG_WIDTH/2));
			canvas.drawBitmap(mBitmap,IMG_PADDING,IMG_PADDING,mPaint);
			canvas.restore();
		}else {
			canvas.save();
			canvas.translate((IMG_PADDING+IMG_WIDTH/2),(IMG_PADDING+IMG_WIDTH/2));
			canvas.rotate(CanvasDegree);
			canvas.clipRect(0,-IMG_WIDTH/2,IMG_WIDTH/2,IMG_WIDTH/2);
			canvas.rotate(-CanvasDegree);
			canvas.translate(-(IMG_PADDING+IMG_WIDTH/2),-(IMG_PADDING+IMG_WIDTH/2));
			canvas.drawBitmap(mBitmap,IMG_PADDING,IMG_PADDING,mPaint);
			canvas.restore();
		}



	}
}

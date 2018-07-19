package com.test.mytest;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.LinearInterpolator;

import com.test.view.RotateView;

/**
 * Created by Administrator on 2018/7/19.
 */

public class RotateActivity extends AppCompatActivity {
	private RotateView mRotateView;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rotate_activity);

		mRotateView = (RotateView) findViewById(R.id.rotate_view);

		ObjectAnimator objectAnimator = ObjectAnimator.ofInt(mRotateView, "YDegree", -45);
		objectAnimator.setStartDelay(500);
		objectAnimator.setDuration(1500);
		objectAnimator.setInterpolator(new LinearInterpolator());
		objectAnimator.start();


		ObjectAnimator objectAnimator1 = ObjectAnimator.ofInt(mRotateView,"CanvasDegree",-45);
		objectAnimator1.setStartDelay(500);
		objectAnimator1.setDuration(1500);
		objectAnimator1.setInterpolator(new LinearInterpolator());
		objectAnimator1.start();
	}
}

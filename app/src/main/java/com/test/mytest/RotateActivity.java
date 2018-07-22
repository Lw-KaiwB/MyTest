package com.test.mytest;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
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

		/*ObjectAnimator objectAnimator = ObjectAnimator.ofInt(mRotateView, "YDegree", -45);
		objectAnimator.setStartDelay(500);
		objectAnimator.setDuration(1500);
		objectAnimator.setInterpolator(new LinearInterpolator());
		objectAnimator.start();


		ObjectAnimator objectAnimator1 = ObjectAnimator.ofInt(mRotateView,"CanvasDegree",-45);
		objectAnimator1.setStartDelay(500);
		objectAnimator1.setDuration(1500);
		objectAnimator1.setInterpolator(new LinearInterpolator());
		objectAnimator1.start();*/

		Keyframe keyframeY = Keyframe.ofInt(0,0);
		Keyframe keyframeY1 = Keyframe.ofInt(0.3f,-65);
		Keyframe keyframeY2 = Keyframe.ofInt(0.7f,0);
		Keyframe keyframeY3 = Keyframe.ofInt(1f,30);

		PropertyValuesHolder holder = PropertyValuesHolder.ofKeyframe("YDegree",keyframeY,keyframeY1,keyframeY2,keyframeY3);

		Keyframe keyframeC = Keyframe.ofInt(0,0);
		Keyframe keyframeC1 = Keyframe.ofInt(0.3f,0);
		Keyframe keyframeC2 = Keyframe.ofInt(0.7f,-270);
		//Keyframe keyframeC3 = Keyframe.ofInt(1f,-270);
		PropertyValuesHolder holder1 = PropertyValuesHolder.ofKeyframe("CanvasDegree",keyframeC,keyframeC1,keyframeC2/*,keyframeC3*/);

		ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(mRotateView,holder,holder1);
		objectAnimator.setStartDelay(1500);
		objectAnimator.setDuration(3000);
		objectAnimator.setInterpolator(new LinearInterpolator());
		objectAnimator.setRepeatCount(-1);
		objectAnimator.start();
	}
}

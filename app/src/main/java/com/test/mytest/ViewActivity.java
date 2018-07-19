package com.test.mytest;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateInterpolator;

import com.test.view.NamEvaluatro;
import com.test.view.TestView;

/**
 * Created by Administrator on 2018/7/18.
 */

public class ViewActivity extends AppCompatActivity {
	private TestView mAnimationView;
	private ViewPropertyAnimator mVPAnimator;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_acitivity);

		mAnimationView = (TestView) findViewById(R.id.my_animation_view);
		/*mVPAnimator = mAnimationView.animate();
		mVPAnimator.alpha(0.5f);
		mVPAnimator.translationX(100);
		mVPAnimator.scaleX(0.5f);
		mVPAnimator.setDuration(5000);
		mVPAnimator.setInterpolator(new DecelerateInterpolator());*/

		/*Keyframe keyframe4 = Keyframe.ofInt(0,0);
		Keyframe keyframe = Keyframe.ofInt(0.3f,180);
		Keyframe keyframe1 = Keyframe.ofInt(0.6f,-90);
		Keyframe keyframe2 = Keyframe.ofInt(1,180);
		PropertyValuesHolder holder = PropertyValuesHolder.ofKeyframe("angle",keyframe4,keyframe,keyframe1,keyframe2);
		ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(mAnimationView,holder);*/
		//ObjectAnimator objectAnimator = ObjectAnimator.ofInt(mAnimationView,"color",0xffff0000);
		ObjectAnimator objectAnimator = ObjectAnimator.ofObject(mAnimationView,"name",new NamEvaluatro(),"Monday1","Sunday99");
		//objectAnimator.setEvaluator(new ArgbEvaluator());
		objectAnimator.setDuration(5000);
		objectAnimator.setInterpolator(new AccelerateInterpolator());
		objectAnimator.start();


		/*AnimationSet animationSet = new AnimationSet(true);
		AlphaAnimation alphaAnimation = new AlphaAnimation(1f,0.5f);
		ScaleAnimation scaleAnimation = new ScaleAnimation(0.5f,0.5f,1f,1f);
		animationSet.addAnimation(alphaAnimation);
		animationSet.addAnimation(scaleAnimation);

		animationSet.setDuration(5000);
		mAnimationView.startAnimation(animationSet);*/

	}
}

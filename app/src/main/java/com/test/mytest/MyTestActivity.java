package com.test.mytest;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by Administrator on 2017/12/1.
 */

public abstract class MyTestActivity extends Activity {

	abstract void myTest(int value);

	abstract String getValue();

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}


}

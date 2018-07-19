package com.test.mytest;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothHeadset;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.RegionIterator;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2018/3/27.
 */

public class MyBluetoothTestActivity extends Activity implements View.OnClickListener {
	@IntDef({Flag.REQUEST_CODE})
	@Retention(RetentionPolicy.SOURCE)
	public @interface Flag {
		int REQUEST_CODE = 0x001;
	}

	private TextView mBtn;
	private MyBroadcastReceiver mMyBroadcastReceiver;

	private ImageView mImageView1, mImageView2;
	AudioManager audioManager;
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_bluetooth_activity);

		mBtn = findViewById(R.id.my_bluetooth_btm);
		mBtn.setOnClickListener(this);

		/*mMyBroadcastReceiver = new MyBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
		registerReceiver(mMyBroadcastReceiver, intentFilter);*/

		//mImageView1 = findViewById(R.id.img1);
		mImageView2 = findViewById(R.id.img2);

		//获取一个Bitmap图片
		Bitmap oldBit = BitmapFactory.decodeResource(getResources(), R.drawable.ic_settings_style);
		//根据原始图片的宽高，创建一个空白的Bitmap
		Bitmap bitmap = Bitmap.createBitmap(oldBit.getWidth(), oldBit.getHeight(), oldBit.getConfig());
		//使用空白的Bitmap创建画布
		Canvas canvas = new Canvas(bitmap);
		Paint paint = new Paint();
		Matrix matrix = new Matrix();
		//设置缩放变换，以画布的原点（0，0）为缩放中心，1表示不缩放
		matrix.setScale(1,1);
		//根据属性绘制图形
		canvas.drawBitmap(oldBit, matrix, paint);
	//	mImageView1.setImageBitmap(bitmap);

		Bitmap bitmap1 = Bitmap.createBitmap(oldBit.getWidth(), oldBit.getHeight(), oldBit.getConfig());
		Canvas canvas1 = new Canvas(bitmap1);
		Matrix matrix1 = new Matrix();
		//水平错切变换，以画布左上角（0，0）为原点,Y不变，X按一点比列迁移
		matrix1.setSkew(0.5f,0);

		canvas1.drawBitmap(oldBit, matrix1, paint);
		mImageView2.setImageBitmap(bitmap1);


		Canvas canvas2 = new Canvas();
		Paint paint1 = new Paint();
		paint1.setStrokeWidth(3);
		paint1.setStyle(Paint.Style.STROKE);
		Path path = new Path();
		path.addCircle(500,500,300, Path.Direction.CW);
		Region region = new Region();
		region.setPath(path,new Region(0,0,1000,1000));

		RegionIterator regionIterator = new RegionIterator(region);
		Rect rect = new Rect();
		while (regionIterator.next(rect)){
			canvas2.drawRect(rect,paint1);
		}
		//将矩阵输出
		/*float[] matrixValue = new float[9];
		matrix.getValues(matrixValue);
		for (int i = 0; i < 3; i++) {
			String temp = new String();
			for (int j = 0; j < 3; j++) {
				temp += matrixValue[3 * i + j] + "\t";
			}
			Log.e("TAG", temp);
		}*/

		audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		BluetoothHeadset headset;
		String values = "12dfa fd09><^0fdsaf````\\|//n..,.";
		String regEx = "[^0-9]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(values);
		String s = m.replaceAll("").trim();
		Log.e("TAH","s="+s+" size="+s.length());

	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mMyBroadcastReceiver != null) {
			unregisterReceiver(mMyBroadcastReceiver);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == Flag.REQUEST_CODE && resultCode == RESULT_OK) {

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.my_bluetooth_btm:
				/*Intent requestIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
				requestIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 150);
				startActivityForResult(requestIntent, Flag.REQUEST_CODE);*/
				//startActivity(new Intent(MyBluetoothTestActivity.this, MainActivity.class));

				boolean state =audioManager.isMicrophoneMute();
				Log.e("TAG","state="+state);
				if (state){
					audioManager.setMicrophoneMute(false);
				}else {
					audioManager.setMicrophoneMute(true);

				}
				break;
		}
	}

	private class MyBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)) {
				int state = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.SCAN_MODE_NONE);
				switch (state) {

					case BluetoothAdapter.SCAN_MODE_NONE:
						Log.e("TAG", "SCAN_MODE_NONE=" + BluetoothAdapter.SCAN_MODE_NONE);
						break;

					case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
						Log.e("TAG", "SCAN_MODE_CONNECTABLE=" + BluetoothAdapter.SCAN_MODE_CONNECTABLE);

						break;
					case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
						Log.e("TAG", "SCAN_MODE_CONNECTABLE_DISCOVERABLE=" + BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE);
						break;
				}
			}
		}
	}

}

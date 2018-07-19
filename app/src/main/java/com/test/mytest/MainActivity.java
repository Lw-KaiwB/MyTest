package com.test.mytest;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;

import com.test.view.ChartCylinderSportView;
import com.test.view.ChartCylinderView;
import com.test.view.ChartPolyLineView;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.Set;

public class MainActivity extends MyTestActivity {

	private static final int BLUETOOTH_REQUEST_STATE = 0x001;
	private ChartCylinderView mChartCylinderView;
	private ChartCylinderSportView mChartCylinderSportView;
	private ChartPolyLineView mLineView;

	private Button mBtn;
	private BluetoothAdapter mBtAdapter;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.numberpicker_test);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.e("TAG", "KEYCODE_BACK");
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return false;
			//return super.onKeyDown(keyCode, event);
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	/*@Override
	public void onBackPressed() {
		Log.e("TAG", "onBackPressed");
		super.onBackPressed();
	}*/

	private class SortBy implements Comparator {
		@Override
		public int compare(Object o1, Object o2) {
			String s1 = (String) o1;
			String s2 = (String) o2;

			return s1.compareTo(s2);
		}
	}

	public static int getCycleInt(String binaryString) {
		BigInteger bi = new BigInteger(binaryString, 2);    //转换为BigInteger类型
		return Integer.parseInt(bi.toString());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == BLUETOOTH_REQUEST_STATE) {
			if (resultCode == RESULT_OK) {
				if (mBtAdapter.isEnabled()) {
					showDeviceAddress();
				}
			}
		}
	}

	private void showDeviceAddress() {
		String address = mBtAdapter.getAddress();
		Log.e("TAG", "address=" + address);
		Set<BluetoothDevice> mDevicesList = mBtAdapter.getBondedDevices();
		if (mDevicesList.size() > 0) {
			for (BluetoothDevice device : mDevicesList) {
				Log.e("TAG", "device address=" + device.getAddress());
			}
		}
	}

	@Override
	void myTest(int value) {
		Log.e("TAG", "activity value=" + value);
	}

	@Override
	String getValue() {
		Log.e("TAG", "activity value=");
		return "activity";
	}
}

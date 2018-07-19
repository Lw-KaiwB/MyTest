package com.test.unit;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.TypedValue;

/**
 * Created by Administrator on 2018/1/29.
 */

public class Utils {

	/**
	 * 通过GPS坐标点计算出里程
	 *
	 * @param mLatLngList
	 * @return
	 */
   /* public static int getGPSMileage(ArrayList<LatLng> mLatLngList) {
		double distance = 0;
        if (mLatLngList.size() >= 2) {
            for (int i = 0; i < mLatLngList.size() - 1; i++) {
                distance = distance + getDistance(mLatLngList.get(i), mLatLngList.get(i + 1));
            }
        }
        return (int) Math.round(distance);
    }*/

	/**
	 * 计算2个左边点之间的距离
	 *
	 * @param
	 * @param
	 * @return
	 */
    /*private static double getDistance(LatLng mLatLng, LatLng mLatLng1) {
        double lat_a = mLatLng.latitude;//第一个坐标的纬度
        double lon_a = mLatLng.longitude;//第一个坐标的经度
        double lat_b = mLatLng1.latitude;//第二个坐标的纬度
        double lon_b = mLatLng1.longitude;//第二个坐标的经度
        double GD_PI = 3.14159265358979;
        double EARTH_R = 6371004.0;       // m
        double cos_AOB, j_AOB, distance;
        double j_NOA = 90 - lat_a;
        double j_NOB = 90 - lat_b;
        double j_aOd = lon_a - lon_b;
        double a, b, c, d, e, f;

        if (lon_a == lon_b && lat_a == lat_b) {
            return 0;
        }
        a = EARTH_R * Math.sin(angle2radian(j_NOA));
        b = EARTH_R * Math.cos(angle2radian(j_NOA));
        d = EARTH_R * Math.sin(angle2radian(j_NOB));
        e = EARTH_R * Math.cos(angle2radian(j_NOB));
        c = Math.sqrt(a * a + d * d - 2 * a * d * Math.cos(angle2radian(j_aOd)));
        f = Math.sqrt(c * c + (e - b) * (e - b));
        cos_AOB = (EARTH_R * EARTH_R + EARTH_R * EARTH_R - f * f) / (2 * EARTH_R * EARTH_R);
        j_AOB = Math.acos(cos_AOB);
        return 2 * GD_PI * EARTH_R * j_AOB / (2 * GD_PI);
    }

    private static double angle2radian(double angle) {
        double GD_PI = 3.14159265358979;
        return angle * GD_PI / 180;
    }*/

   /* public static float dp2px(float value){
    	return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,value,System)
	}*/
	public static Bitmap getBitmap(Resources res, int width, int resId) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);
		options.inJustDecodeBounds = false;
		options.outWidth = width;
		return BitmapFactory.decodeResource(res, resId, options);
	}

	public static float dip2px(float value) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, Resources.getSystem().getDisplayMetrics());
	}
}

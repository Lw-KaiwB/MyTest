package com.test.unit;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2017/11/21.
 */

public class Unit {
    /**
     * 获得当前和7天前的日期，以月/日的形式返回
     *
     * @param date
     */
    public static String[] getStartEndDay(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd");
        String endDay = simpleDateFormat.format(date);
        Date mData = new Date(date.getTime() - (7 * 24 * 60 * 60 * 1000L));
        String startDay = simpleDateFormat.format(mData);
        return new String[]{startDay, endDay};
    }

    public static float getFloatScale(int digit, float value) {
        BigDecimal b = new BigDecimal(value);
        return (float) b.setScale(digit, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 将整数的秒转为00:00:00这种形式
     *
     * @param sec
     * @return
     */
    public static String getSec2Time(int sec) {
        int h = sec / 3600;
        int m = (sec % 3600) / 60;
        int s = (sec % 3600) % 60;
        StringBuilder sb = new StringBuilder();
        if (h < 10) {
            sb.append(0).append(h).append(":");
        } else {
            sb.append(h).append(":");
        }
        if (m < 10) {
            sb.append(0).append(m).append(":");
        } else {
            sb.append(m).append(":");
        }
        if (s < 10) {
            sb.append(0).append(s);
        } else {
            sb.append(s);
        }
        return sb.toString();
    }

    /**
     * 将整数转为最少2位的字符串
     *
     * @param i
     * @return
     */
    public static String int2String(int i) {
        StringBuilder sb = new StringBuilder();
        if (i < 10) {
            sb.append(0).append(i);//.append("-").append(0).append(i+1);
        } else {
            sb.append(i);//.append("-").append(i+1);
        }
        return sb.toString();
    }

    /**
     * 将时间（秒）转为速度
     * @param type 公制还是英制速度
     * @param i 秒数
     * @return
     */
    public static String sec2speed(int type, int i) {
        String speedStr;
        if (i>0){
            speedStr = getFloatScale(2,1.0f/(i*1.0f/3600))+"";
        }else {
            speedStr = "0.00";
        }
        return speedStr;
    }
    /**
     * 将速度截取固定点数绘制曲线
     *
     * @param string
     * @return
     */
    public static ArrayList<Integer> string2InterceptFloatArrayList(String string) {
        ArrayList<Integer> list = new ArrayList<>();
        if (TextUtils.isEmpty(string)) {
            return list;
        }
        String[] strArr = string.split(",");
        for (int i = 0; i < strArr.length; i++) {
            list.add(Math.round(Float.valueOf(strArr[i])));
        }
        return list;
    }
    /**
     * 将double数据四舍五入保留digit位小数
     *
     * @param digit 保留几位小数
     * @param value 要取舍的数字
     * @return
     */
    public static double getDoubleScale(int digit, double value) {
        BigDecimal b = new BigDecimal(value);
        return b.setScale(digit, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    /**
     * 求2个整数的大小
     *
     * @param value0
     * @param value1
     * @return
     */
    public static int[] getMinMaxValue(int value0, int value1) {
        int[] value = new int[]{0, 0};
        if (value0 < value1) {
            value = new int[]{value0, value1};
        } else {
            value = new int[]{value1, value0};
        }
        return value;
    }
}

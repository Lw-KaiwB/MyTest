package com.test.view;

import android.animation.TypeEvaluator;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2018/7/19.
 */

public class NamEvaluatro implements TypeEvaluator<String> {

	String nameString = "Monday1,Tuesday1,Wednesday1,Thursday1,Friday1," +
			"Saturday2,Sunday2,Monday2,Tuesday2,Wednesday2,Thursday2,Friday2,Saturday2,Sunday2," +
			"Monday3,Tuesday3,Wednesday3,Thursday3,Friday3,Saturday3,Sunday3," +
			"Monday4,Tuesday4,Wednesday4,Thursday4,Friday4,Saturday4,Sunday4," +
			"Monday5,Tuesday,Wednesday,Thursday,Friday,Saturday,Sunday," +
			"Monday,Tuesday,Wednesday,Thursday,Friday,Saturday,Sunday," +
			"Monday,Tuesday,Wednesday,Thursday,Friday,Saturday,Sunday," +
			"Monday,Tuesday,Wednesday,Thursday,Friday,Saturday,Sunday," +
			"Monday,Tuesday,Wednesday,Thursday,Friday,Saturday,Sunday," +
			"Monday,Tuesday,Wednesday,Thursday,Friday,Saturday,Sunday99";
	private List<String> nameList = Arrays.asList(nameString.split(","));

	@Override
	public String evaluate(float fraction, String startValue, String endValue) {
		if (!nameList.contains(startValue)) {
			throw new IllegalStateException("startValue is true");
		}
		if (!nameList.contains(endValue)) {
			throw new IllegalStateException("endValue is not exit");
		}

		int index = (int) ((nameList.indexOf(endValue) - nameList.indexOf(startValue)) * fraction);
		if (index >= 0 && index < nameList.size())
			return nameList.get(index);
		return "UNKNOW";
	}
}

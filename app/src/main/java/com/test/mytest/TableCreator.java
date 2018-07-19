package com.test.mytest;

import android.util.Log;

import com.test.annotation.DBClass;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/12.
 */

public class TableCreator {
	public static String createTableSQL(String className) throws ClassNotFoundException {
		Log.e("TAG", "className=" + className);
		Class<?> cl = Class.forName(className);
		DBClass.DBTable dbTable = cl.getAnnotation(DBClass.DBTable.class);

		if (dbTable == null) {
			System.out.println("Not has DBTable Annotation in class " + className);
			return null;
		}

		String name = dbTable.name();
		if (name.length() < 1) {
			name = cl.getName().toUpperCase();
		}

		List<String> columnList = new ArrayList<>();
		Field[] fields = cl.getDeclaredFields();
		Field[] fields1 = cl.getFields();
		for (Field field : cl.getDeclaredFields()) {
			String columnName = null;

			Annotation[] annotations = field.getDeclaredAnnotations();
			if (annotations.length < 1) {
				continue;
			}

			if (annotations[0] instanceof DBClass.SQLInteger) {

				DBClass.SQLInteger sqlInteger = (DBClass.SQLInteger) annotations[0];
				if (sqlInteger.name().length() < 1) {
					columnName = field.getName().toUpperCase();
				} else {
					columnName = sqlInteger.name();
				}

				columnList.add(columnName + " INT " + getConstraints(sqlInteger.constraint()));
			}

			if (annotations[0] instanceof DBClass.SQLString) {
				DBClass.SQLString sqlString = (DBClass.SQLString) annotations[0];
				if (sqlString.name().length() < 1) {
					columnName = field.getName().toUpperCase();
				} else {
					columnName = sqlString.name();
				}

				columnList.add(columnName + " VARCHAR(" + sqlString.value() + " )" + getConstraints(sqlString.constraint()));
			}
		}

		StringBuilder sb = new StringBuilder("CREATE TABLE " + name + " (");
		for (String s : columnList) {
			sb.append("\n" + s + " ,");
		}
		return sb.substring(0, sb.length() - 1) + ");";
	}

	private static String getConstraints(DBClass.Constraints con) {
		String constraints = "";
		if (!con.allowNull()) {
			constraints += " NOT NULL ";
		}
		if (con.primaryKey()) {
			constraints += " PRIMARY KEY ";
		}
		if (con.unique()) {
			constraints += " UNIQUE";
		}
		return constraints;
	}
}

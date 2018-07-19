package com.test.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Administrator on 2018/7/12.
 */

public class DBClass {
	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface DBTable {
		String name() default "";
	}

	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface SQLInteger {
		//对应数据库表的列名
		String name() default "";

		Constraints constraint() default @Constraints;
	}

	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface SQLString {
		//对应数据库列名
		String name() default "";

		//对类型分配的长度
		int value() default 0;

		Constraints constraint() default @Constraints;
	}



	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Constraints {
		//判断是否作为主键约束
		boolean primaryKey() default false;

		//判断是否允许为null
		boolean allowNull() default false;

		//判断是否唯一
		boolean unique() default false;
	}
}

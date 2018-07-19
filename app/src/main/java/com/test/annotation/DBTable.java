package com.test.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Administrator on 2018/7/12.
 */
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DBTable {
	String name() default "";
	int age() ;
}
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@interface AnnotationTest{
	enum State {ONE,TWO}
	State s = State.ONE;
	Class <?> getMyClass() default Void.class;
	DBTable getDBTAble() default @DBTable(age = 1);

}

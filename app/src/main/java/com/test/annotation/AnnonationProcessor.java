package com.test.annotation;

import java.lang.reflect.Constructor;

/**
 * Created by Administrator on 2018/7/16.
 */

public class AnnonationProcessor {

	public static void init(Object object) {
		if (!(object instanceof User)) {
			throw new IllegalStateException("class[" + object.getClass().getName() + " is class User");
		}

		Constructor[] constructors = object.getClass().getConstructors();
		for (Constructor constructor : constructors) {

			if (constructor.isAnnotationPresent(UserData.class)) {
				UserData userData = (UserData) constructor.getAnnotation(UserData.class);
				int id = userData.id();
				String name = userData.name();
				int age = userData.age();

				((User) object).setId(id);
				((User) object).setName(name);
				((User) object).setAge(age);
			}
		}
	}
}

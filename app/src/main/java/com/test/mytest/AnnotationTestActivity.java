package com.test.mytest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.test.annotation.AnnonationProcessor;
import com.test.annotation.DBTable;
import com.test.annotation.DocumentA;
import com.test.annotation.DocumentB;
import com.test.annotation.FilterPath;
import com.test.annotation.User;

/**
 * Created by Administrator on 2018/7/12.
 */

public class AnnotationTestActivity extends AppCompatActivity {

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.annotation_layout);
		/*AA a = new AA();
		B b = new BB();

		Log.e("TAG","A ="+ AA.class.getAnnotation(DocumentA.class));
		Log.e("TAG","A ="+ A.class.getAnnotation(DocumentA.class));
		Log.e("TAG","A ="+ AA.class.getAnnotation(DocumentB.class));
		Log.e("TAG","A ="+ A.class.getAnnotation(DocumentB.class));
		Log.e("TAG", "A =" + Arrays.toString(a.getClass().getAnnotations()));
		Log.e("TAG", "A =" + Arrays.toString(AA.class.getDeclaredAnnotations()));
		Log.e("TAG", "A =" + DBTable.class.isAnnotation());
		Log.e("TAG", "A =" + AA.class.isAnnotationPresent(DocumentA.class));
		Log.e("TAG", "A =" + AA.class.isAnnotationPresent(DBTable.class));
		Log.e("TAG", "A =" + A.class.isAnnotationPresent(DBTable.class));
		Log.e("TAG", "A =" + AA.class.isAnnotationPresent(DocumentB.class));*/

		findViewById(R.id.annotation_btn).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				/*try {
					Log.e("TAG", TableCreator.createTableSQL(Member.class.getName()));
				} catch (Exception e) {
					Log.e("TAG", "e=" + e);
				}*/

				User user = new User();
				AnnonationProcessor.init(user);
				Log.e("TAG","user="+user.toString());
			}
		});

	}

	@FilterPath("")
	class E {

	}

	@DocumentA
	class A {
	}

	@DocumentB
	class B {
	}

	@DBTable(age = 1, name = "hello")
	class AA extends A {
	}

	class BB extends B {
	}
}

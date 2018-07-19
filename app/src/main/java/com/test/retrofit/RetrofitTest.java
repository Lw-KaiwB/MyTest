package com.test.retrofit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.test.mytest.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2018/7/13.
 */

public class RetrofitTest extends AppCompatActivity {
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		setContentView(R.layout.annotation_layout);

		findViewById(R.id.annotation_btn).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Retrofit retrofit = new Retrofit.Builder()
						.baseUrl("https://free-api.heweather.com/")
						.addConverterFactory(GsonConverterFactory.create())
						.build();

				GitHubServer gitHubServer = retrofit.create(GitHubServer.class);
				Call<String> call = gitHubServer.pullWeather();
				call.enqueue(new Callback<String>() {
					@Override
					public void onResponse(Call<String> call, Response<String> response) {
						Log.e("TAg","response="+response.body());
					}

					@Override
					public void onFailure(Call<String> call, Throwable t) {
						Log.e("TAG","onFailure");
					}
				});
			}
		});
	}
}

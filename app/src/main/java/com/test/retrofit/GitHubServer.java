package com.test.retrofit;



import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Administrator on 2018/7/13.
 */

public interface GitHubServer {
	@GET("blog/{id}")
	Call<ResponseBody> getBlog(@Path("id") int id);
	//s6/weather/forecast?location=CN101010100&key=HE1707151201281756
	@GET("s6/weather/forecast?location=CN101010100&key=HE1707151201281756")
	Call<WeatherInfo> getWeather();
	@GET("s6/weather/forecast?location=CN101010100&key=HE1707151201281756")
	Call<String> getWeatherInfo();
	@GET("s6/weather/forecast?location=CN101010100&key=HE1707151201281756/")
	Call<String> pullWeather();
}

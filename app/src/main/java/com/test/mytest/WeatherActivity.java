package com.test.mytest;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.test.model.WeatherInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/29.
 */

public class WeatherActivity extends Activity {

    private EditText mCityEdit;
    private Button mSearchBtn;
    private ListView mCityList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.weather_activity);
        mCityEdit = findViewById(R.id.city_edit);
        mSearchBtn = findViewById(R.id.search_weather_btn);
        mCityList = findViewById(R.id.weather_city_list);

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cityName = mCityEdit.getText().toString();
                if (TextUtils.isEmpty(cityName)) return;
                CityAsync mAsync = new CityAsync();
                mAsync.execute(cityName);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private class CityAsync extends AsyncTask<String, Integer, List<WeatherInfo>> {
        @Override
        protected void onPostExecute(List<WeatherInfo> weatherInfos) {
            super.onPostExecute(weatherInfos);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected List<WeatherInfo> doInBackground(String... strings) {
            /*List<WeatherInfo> infos = new ArrayList<>();
            String cityName = strings[0];
            String url = "https://api.heweather.com/s6/search?city like" + cityName + "&key=" + "ae02ae0f5fc24743a4d472c48efa2c52";
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            Call call = okHttpClient.newCall(request);
            try {
                Response response = call.execute();
                Log.e("TAG", "city=" + response.body().string());
            } catch (Exception e) {
                e.printStackTrace();
            }*/

            List<WeatherInfo> cities = new ArrayList<>();
            String cityName = strings[0];
            HttpURLConnection connection = null;
            InputStream is = null;
            try {
                URL url = new URL("https://api.heweather.com/v5/search?city=" + cityName + "&key=" + "ae02ae0f5fc24743a4d472c48efa2c52");
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                // 设置字符集
                connection.setRequestProperty("Charset", "UTF-8");
                // 设置文件类型
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                if (connection.getResponseCode() == 200) {
                    is = connection.getInputStream();
                    StringBuilder sb = new StringBuilder();
                    byte[] b = new byte[2048];
                    for (int n; (n = is.read(b)) != -1; ) {
                        sb.append(new String(b, 0, n));
                    }

                    String cityJson = sb.toString();
                    Log.e("TAG","CityJson="+cityJson);
                    JSONObject jsonObject = new JSONObject(cityJson);
                    if (jsonObject.has("HeWeather5")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("HeWeather5");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            WeatherInfo info = new WeatherInfo();
                            JSONObject dataOb = jsonArray.getJSONObject(i).getJSONObject("basic");
                            info.setCityName(dataOb.getString("city"));
                            info.setCityCode(dataOb.getString("id"));
                            cities.add(info);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Integer errorInteger = -1;
                publishProgress(errorInteger);
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            return cities;
        }
    }
}

package com.test.mytest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.test.unit.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CircleImageViewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.circle_image_view);
        findViewById(R.id.click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int version = -1;
                String ver = "";
                while (true){
                    if (TextUtils.isEmpty(ver)){
                        break;
                    }
                }
                ver = "V0_03.0";
                Log.e("TAG","ver0="+ver);
                String regEx = "[^0-9]";
                Pattern p = Pattern.compile(regEx);
                Matcher m = p.matcher(ver);
                Log.e("TAG","ver1="+ver+" ver1.length="+ver.length());
                ver = m.replaceAll("").trim();
                ver = "1.1";
                try {
                    if (!TextUtils.isEmpty(ver)&& Utils.isNumber(ver)){
                        version = Integer.valueOf(ver);
                    }
                    int i = Integer.valueOf(ver);
                    Log.e("TAG","version="+version);
                } catch (Exception e) {
                    e.printStackTrace();
                    if (ver.contains("V")) {
                        ver = ver.replace("V", "");
                    }
                    if (ver.contains("_")) {
                        ver = ver.replace("_", "");
                    }
                    if (ver.contains(".")) {
                        ver = ver.replace(".", "");
                    }
                    if (!TextUtils.isEmpty(ver)){
                        version = Integer.valueOf(ver);
                    }
                }
            }
        });
    }
}

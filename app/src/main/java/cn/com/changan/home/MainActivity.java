package cn.com.changan.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.com.changan.home.util.HttpUtil;
import cn.com.changan.home.util.ResultData;
import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {
    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,TestActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initData2() {
        Map<String,Object> map = new HashMap<>();
        map.put("sort","asc:");
        map.put("page","1");
        String url = "http://www.baidu.com";
        HttpUtil.postOKHttp(url, map, new HttpUtil.HttpCallBack() {
            @Override
            public void onFail(String error) {

            }

            @Override
            public void onSuccess(String string, Class<ResultData> resultDataClass) {


            }
        });
    }

    private void initData1() {
        Map<String,Object> map = new HashMap<>();
//        map.put("sort","asc:");
//        map.put("page","1");
        String url = "http://www.baidu.com";
        HttpUtil.getOKHttp(url, map, new HttpUtil.HttpCallBack() {
            @Override
            public void onFail(String error) {
                Log.d("JenActivity", "onFail: 访问失败");

            }

            @Override
            public void onSuccess(String string, Class<ResultData> resultDataClass) {
                Log.d("JenActivity", "onSuccess: 访问成功");

            }
        });

    }
}



















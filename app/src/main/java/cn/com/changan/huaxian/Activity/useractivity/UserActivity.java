package cn.com.changan.huaxian.Activity.useractivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.maps.CameraUpdateFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.com.changan.huaxian.Activity.login.LoginActivity;
import cn.com.changan.huaxian.R;
import cn.com.changan.huaxian.exit.MyParkingCarActivity;
import cn.com.changan.huaxian.http.HttpCallback;
import cn.com.changan.huaxian.http.OKHttpUtil;
import cn.com.changan.huaxian.http.ProjectConfig;
import cn.com.changan.huaxian.util.ConfirmCallable;
import cn.com.changan.huaxian.util.ConfirmUtils;
import okhttp3.Call;

public class UserActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView backBtn;
    private ImageView userHeadImg;
    private RelativeLayout myParkingCar;
    private Button exitLogin;

    private String mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        backBtn = findViewById(R.id.back_btn);
        myParkingCar = findViewById(R.id.my_parking_car);
        userHeadImg = findViewById(R.id.user_head_img);
        exitLogin = findViewById(R.id.logout);

        backBtn.setOnClickListener(this);
        myParkingCar.setOnClickListener(this);
        userHeadImg.setOnClickListener(this);
        exitLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_btn:
                finish();
                break;
            case R.id.my_parking_car:
                Intent intent = new Intent(this, MyParkingCarActivity.class);
                startActivity(intent);
                break;
            case R.id.user_head_img:
                break;
            case R.id.logout:
                Log.d("TAG", "onClick: 弹出是否退出对话框");
                openExitDialog();
                break;
        }
    }

    private void openExitDialog() {
        Log.d("TAG","openExitDialog:退出登录");
        ConfirmUtils utils = new ConfirmUtils();
        utils.showConfirmDialog(this, "退出登录", "是否确定退出登录?", new ConfirmCallable() {
            @Override
            public void unaccept() {

            }

            @Override
            public void accept() {
                String url = ProjectConfig.LOGOUT;
                Map<String, String> params = new HashMap<>();
                params.put("mobile", mobile);
                OKHttpUtil.getInstance().postInterceptHttpRequest(params, url, new HttpCallback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, String response, boolean tokenRefreshd)  {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int code = jsonObject.optInt("code");
                            Object data = jsonObject.optJSONObject("data");
                            String msg = jsonObject.optString("msg");
                            if (code == 0){
                                Intent intent = new Intent(UserActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }


}
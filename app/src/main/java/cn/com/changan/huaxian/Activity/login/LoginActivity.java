package cn.com.changan.huaxian.Activity.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.com.changan.huaxian.Activity.base.BaseCompatActivity;
import cn.com.changan.huaxian.Activity.main.BasicMapActivity;
import cn.com.changan.huaxian.R;
import cn.com.changan.huaxian.http.HttpCallback;
import cn.com.changan.huaxian.http.OKHttpUtil;
import cn.com.changan.huaxian.http.ProjectConfig;
import cn.com.changan.huaxian.tools.LegalTools;
import cn.com.changan.huaxian.util.DeviceUtils;
import okhttp3.Call;

public class LoginActivity extends BaseCompatActivity implements View.OnClickListener {
    private final String TAG="LoginActivity";
    private Context context ;
    //登录
    private EditText editPhone, editCode;
    private TextView numberTip,getCode;
    private ImageView deletePhone;
    private Button buttonLogin;
    //输入电话
    private String input = "";
    private String mobile;


    private View jumpMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = this;

        editPhone = findViewById(R.id.accman_input_phone);
        deletePhone = findViewById(R.id.accman_delete_phone);
        getCode = findViewById(R.id.accman_get_code);
        editCode = findViewById(R.id.accman_input_code);
        numberTip = findViewById(R.id.accman_err_phone);
        buttonLogin = findViewById(R.id.accman_login);

        //监听事件
        editPhone.setOnClickListener(this);
        deletePhone.setOnClickListener(this);
        getCode.setOnClickListener(this);
        editCode.setOnClickListener(this);
        buttonLogin.setOnClickListener(this);

        //手机号输入
        editPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                input = s.toString();
                if (TextUtils.isEmpty(input)){
                    deletePhone.setVisibility(View.GONE);//隐藏清空图标
                    return;

                }else {
                    deletePhone.setVisibility(View.VISIBLE);//显示清空图标
                }
                if (input.length()==11){
                    if (LegalTools.checkPhone(input)) {
                        numberTip.setVisibility(View.GONE);
                    }else {
                        numberTip.setVisibility(View.VISIBLE);
                    }

                }
            }
        });

        initJump();
    }

    private void initJump() {
        jumpMap = findViewById(R.id.jumpToMap);
        jumpMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginActivity.this.startActivity(new Intent(LoginActivity.this,BasicMapActivity.class));
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id ==R.id.accman_delete_phone) {
            input = "";
            editPhone.setText(input);
        }else if (id == R.id.accman_get_code) {
            if (DeviceUtils.isFastClick()){
                return;
            }
            if(input.length()!=11){
                numberTip.setVisibility(View.VISIBLE);
                return;
            }
            sendVerifyCode();
            mobile = input;
            countDownTime();
        }else if (id == R.id.accman_login){
            String confirmCode = editCode.getText().toString();
            if (confirmCode.length()!=6){
                Toast.makeText(context, "验证码输入错误", Toast.LENGTH_LONG).show();
            }else {
                controlAuth(confirmCode);

            }
        }
    }
    /**
     *  检测手机号,重新发送验证码
     */
    private boolean resendCode() {
        input = editPhone.getEditableText().toString();
        if (TextUtils.isEmpty(input)) {
            Toast.makeText(context, "手机号不能为空", Toast.LENGTH_LONG).show();
            return false;
        }

        if (!LegalTools.checkPhone(input)) {
            Toast.makeText(context, "手机号不合法，请检查", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;

    }

    /**
     * 验证短信验证码
     */
    private void controlAuth(String confirmCode) {
        String url = ProjectConfig.LOGIN;
        Map<String, String> params = new HashMap<>();
        params.put("mobile", mobile);
        params.put("verifyCode",confirmCode);
        OKHttpUtil.getInstance().postInterceptHttpRequest(params, url, new HttpCallback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, String response, boolean tokenRefreshd) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int code = jsonObject.optInt("code");
                    Object data = jsonObject.optJSONObject("data");
                    String token = jsonObject.optString("appToken");
                    String msg = jsonObject.optString("msg");
                    if (code == 0){
                        LoginActivity.this.startActivity(new Intent(LoginActivity.this,BasicMapActivity.class));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 获取短信验证码
     */
    private void sendVerifyCode() {
        String url = ProjectConfig.SEND_AUTHCODE;
        Map<String, String> params = new HashMap<>();
        params.put("mobile",input);
        OKHttpUtil.getInstance().postInterceptHttpRequest(params, url, new HttpCallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("changa_login","error "+e);
            }

            @Override
            public void onResponse(Call call, String response, boolean tokenRefreshd) {
                Log.d("huaxian_login","send code "+response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int code = jsonObject.optInt("code");
                    String data = jsonObject.optString("data");
                    String msg = jsonObject.optString("msg");
                    if(code == 0){
                        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * 发送验证码倒计时
     */
    private void countDownTime() {
        CountDownTimer countDownTimer = new CountDownTimer(60 * 1000, 1000) {

            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {

                getCode.setTextColor(getResources().getColor(R.color.after_send_code));
                getCode.setText(millisUntilFinished / 1000 + "s"+ "后重新发送");
                getCode.setClickable(false);// 防止重复点击
            }

            @Override
            public void onFinish() {
                // 可以在这做一些操作,如果没有获取到验证码再去请求服务器
                getCode.setTextColor(getResources().getColor(R.color.before_send_code));
                getCode.setClickable(true);// 防止重复点击
                getCode.setText("获取验证码");
            }
        };
        countDownTimer.start();
    }
    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
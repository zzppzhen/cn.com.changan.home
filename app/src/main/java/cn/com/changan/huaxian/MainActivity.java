package cn.com.changan.huaxian;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import cn.com.changan.huaxian.Activity.login.AccountManager;
import cn.com.changan.huaxian.Activity.login.LoginActivity;
import cn.com.changan.huaxian.Activity.main.BasicMapActivity;

public class MainActivity extends AppCompatActivity{

    private TextView go2Login,go2Map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(AccountManager.checkTokenIsNull(this)){
            //本地token 缓存为空，走登录
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }else{
            //已经登陆
            Intent intent = new Intent(this, BasicMapActivity.class);
            startActivity(intent);
        }
    }
}
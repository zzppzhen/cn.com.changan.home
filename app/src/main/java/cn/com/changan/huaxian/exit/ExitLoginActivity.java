package cn.com.changan.huaxian.exit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import cn.com.changan.huaxian.Activity.login.LoginActivity;
import cn.com.changan.huaxian.R;

public class ExitLoginActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvLoginOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exit_login);

        tvLoginOut = findViewById(R.id.login_out);

        tvLoginOut.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.login_out){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }else if (id==R.id.rl_myparkingcar){
            Intent intent = new Intent(this,MyParkingCarActivity.class);
            startActivity(intent);
        }
    }
}
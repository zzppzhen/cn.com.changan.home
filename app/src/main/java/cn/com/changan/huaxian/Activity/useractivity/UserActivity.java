package cn.com.changan.huaxian.Activity.useractivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import cn.com.changan.huaxian.R;
import cn.com.changan.huaxian.exit.MyParkingCarActivity;

public class UserActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView backBtn;
    private ImageView userHeadImg;
    private RelativeLayout myParkingCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        backBtn = findViewById(R.id.back_btn);
        myParkingCar = findViewById(R.id.my_parking_car);
        userHeadImg = findViewById(R.id.user_head_img);

        backBtn.setOnClickListener(this);
        myParkingCar.setOnClickListener(this);
        userHeadImg.setOnClickListener(this);
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
        }
    }
}
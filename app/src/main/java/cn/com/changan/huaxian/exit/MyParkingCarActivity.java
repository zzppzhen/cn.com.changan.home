package cn.com.changan.huaxian.exit;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.com.changan.huaxian.R;
import cn.com.changan.huaxian.entity.CarInformation;

public class MyParkingCarActivity extends AppCompatActivity {
    private RecyclerView mParkingCarList;
    private ParkingAdapter mParkingAdapter;
    private List<CarInformation> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_parking_car);

        mParkingCarList = findViewById(R.id.car_recyclerview);
        mParkingAdapter = new ParkingAdapter(data);
        mParkingCarList.setAdapter(mParkingAdapter);

    }
    private class ParkingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<CarInformation> data;
        public ParkingAdapter(List<CarInformation> data){
            this.data = data;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ParkingHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.my_car_recyclerciew,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            CarInformation carInformation = data.get(position);
            ParkingHolder parkingHolder = (ParkingHolder) holder;
            //加载具体内容
            parkingHolder.vin.setText(carInformation.getVin());
            parkingHolder.address.setText(carInformation.getAddress());
            parkingHolder.store.setText(carInformation.getStore());
            parkingHolder.missingPart.setText(carInformation.getMissingPart());
            //点击item跳转车辆详情页面
            parkingHolder.mRootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MyParkingCarActivity.this,CarDetailsActivity.class);
                    startActivity(intent);
                }
            });
        }
        //显示多少个item
        @Override
        public int getItemCount() {
            return data.size();
        }
        class ParkingHolder extends RecyclerView.ViewHolder{
            public TextView vin,address,store,missingPart;
            public LinearLayout mRootView;
            public ParkingHolder(@NonNull View itemView) {
                super(itemView);
                vin = itemView.findViewById(R.id.rv_car_vin);
                address = itemView.findViewById(R.id.rv_car_adress);
                store = itemView.findViewById(R.id.rv_car_store);
                missingPart = itemView.findViewById(R.id.rv_car_missingpart);
                mRootView = itemView.findViewById(R.id.root_view);

            }
        }
    }
}
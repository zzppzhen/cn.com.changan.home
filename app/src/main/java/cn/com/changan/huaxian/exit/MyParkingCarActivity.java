package cn.com.changan.huaxian.exit;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.com.changan.huaxian.R;
import cn.com.changan.huaxian.entity.CarInformation;
import cn.com.changan.huaxian.entity.DateEntity;
import cn.com.changan.huaxian.entity.MyParkingCarEntity;
import cn.com.changan.huaxian.http.OKHttpUtil;

public class MyParkingCarActivity extends AppCompatActivity {
    private String TAG = "MyParkingCarActivity";
    private RecyclerView mParkingCarList;
    private DateAdapter mParkingAdapter;
    private CarAdapter carAdapter;
    private List<MyParkingCarEntity> carEntityList = new ArrayList<>();
    private List<DateEntity> dateList = new ArrayList<>();

    private RefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_parking_car);
        //test
        generateData();
        //test
        mParkingCarList = findViewById(R.id.date_recyclerview);
        mParkingAdapter = new DateAdapter(dateList);
        mParkingCarList.setAdapter(mParkingAdapter);
        mParkingCarList.setLayoutManager(new LinearLayoutManager(this));
        //拉到底部刷新接口获取数据
        refreshLayout = findViewById(R.id.refresh_layout);
        refreshLayout.setRefreshHeader(new ClassicsHeader(this));
        refreshLayout.setRefreshFooter(new ClassicsFooter(this));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                generateData();
                mParkingAdapter.notifyDataSetChanged();
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
                generateData();
                mParkingAdapter.notifyDataSetChanged();
            }
        });
    }


    private class DateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<DateEntity> data;
        public DateAdapter(List<DateEntity> data){
            this.data = data;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ParkingHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.my_car_recyclerciew,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            DateEntity dateEntity = data.get(position);
            ParkingHolder parkingHolder = (ParkingHolder) holder;
            //加载具体内容
            parkingHolder.dateTxt.setText(dateEntity.getDate());
            carEntityList = dateEntity.getEntityList();
            Log.d(TAG,"carEntity list "+carEntityList.size());
            carAdapter = new CarAdapter(carEntityList);
            parkingHolder.carListView.setAdapter(carAdapter);
            LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
            parkingHolder.carListView.setLayoutManager(manager);
        }
        //显示多少个item
        @Override
        public int getItemCount() {
            return data.size();
        }
        class ParkingHolder extends RecyclerView.ViewHolder{
            private TextView dateTxt;
            private RecyclerView carListView;
            public ParkingHolder(@NonNull View itemView) {
                super(itemView);
                dateTxt = itemView.findViewById(R.id.myparkingcar_date_txt);
                carListView = itemView.findViewById(R.id.car_list_view);
            }
        }
    }

    private class CarAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private List<MyParkingCarEntity> data;
        public CarAdapter(List<MyParkingCarEntity> data){
            this.data = data;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new DataHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.parking_car_list_recyclerview,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class DataHolder extends RecyclerView.ViewHolder{

            public DataHolder(@NonNull View itemView) {
                super(itemView);

            }
        }
    }

    private void generateData(){
        //only for test
        for (int a = 0;a<3;a++){
            carEntityList = new ArrayList<>();
            for(int i = 0; i<3;i++){
                MyParkingCarEntity entity = new MyParkingCarEntity();
                entity.setVin("dafafasafwaraadsa");
                carEntityList.add(new MyParkingCarEntity());
            }
            DateEntity dateEntity = new DateEntity();
            dateEntity.setDate("今天");
            dateEntity.setEntityList(carEntityList);
            dateList.add(dateEntity);
        }
    }

}
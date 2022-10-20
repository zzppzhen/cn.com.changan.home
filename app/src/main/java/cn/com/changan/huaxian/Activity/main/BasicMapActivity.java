package cn.com.changan.huaxian.Activity.main;


import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.MyLocationStyle;
import com.example.pickerviewlibrary.picker.TeaPickerView;
import com.example.pickerviewlibrary.picker.entity.PickerData;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import cn.com.changan.huaxian.R;
import cn.com.changan.huaxian.util.ConfirmCallable;
import cn.com.changan.huaxian.util.ConfirmUtils;
import cn.com.changan.huaxian.tools.WareHouseListener;


/**
 * AMapV1地图中介绍如何显示世界图
 */
public class BasicMapActivity extends FragmentActivity implements OnClickListener {

	private MapView mapView;
	private AMap aMap;
	private LinearLayout parkingBtn,searchBtn;
	private UiSettings mUiSettings;//定义一个UiSettings对象
	private FragmentTransaction transaction;

	private ParkingFragment parkingFragment;
	private SearchFragment searchFragment;
	private final String PARK_FRAG = "parkingFragment";
	private final String SEARCH_FRAG = "searchFragment";
	private final int PERMISSON_REQUESTCODE = 1;
	private String nowFragmen = SEARCH_FRAG;
	private List<Fragment> mFragments = new ArrayList<>();

	private ImageView ivParkingTab;
	private TextView tvParkingTab;
	private ImageView ivSearchTab;
	private TextView tvSearchTab;

	private FrameLayout container;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.basicmap_activity);

		ImmersionBar.with(this).transparentStatusBar().statusBarDarkFont(true).init();

		ivParkingTab = findViewById(R.id.iv_parking_tab);
		tvParkingTab = findViewById(R.id.tv_parking_tab);
		ivSearchTab = findViewById(R.id.iv_search_tab);
		tvSearchTab = findViewById(R.id.tv_search_tab);

		parkingBtn = findViewById(R.id.parking_selector);
		searchBtn = findViewById(R.id.searching_selector);
		parkingBtn.setOnClickListener(this);
		searchBtn.setOnClickListener(this);

		container = findViewById(R.id.fram_container);

		AMapLocationClient.updatePrivacyShow(this,true,true);
		AMapLocationClient.updatePrivacyAgree(this,true);
		mapView = findViewById(R.id.main_map_view);
		mapView.onCreate(savedInstanceState);
		if (aMap == null) {
			aMap = mapView.getMap();
			mUiSettings = aMap.getUiSettings();//实例化UiSettings类对象
		}
		aMap.setMapType(AMap.MAP_TYPE_NORMAL);
		mUiSettings.setZoomControlsEnabled(false);

		getPersimmions();

		parkingFragment = new ParkingFragment();
		searchFragment = new SearchFragment();
		mFragments.add(parkingFragment);
		mFragments.add(searchFragment);
		hideOthersFragment(searchFragment,true);
		hideOthersFragment(parkingFragment,true);
		parkingFragment.setListener(new WareHouseListener() {
			@Override
			public void go2WareHouse() {
				//设置数据有多少层级
				PickerData data=new PickerData();
//				data.setFirstDatas(mProvinceDatas);//json: ["广东","江西"]
//				data.setSecondDatas(mSecondDatas);//json: {"江西":["南昌","赣州"],"广东":["广州","深圳","佛山","东莞"]}
				List<String> mThirdDatas = new ArrayList<>();
				mThirdDatas.add("1");
				data.setFirstDatas(mThirdDatas);//json: {"广州":["天河区","白云区","番禹区","花都区"],"赣州":["章贡区","黄金开发区"],"东莞":["东城","南城"],"深圳":["南山区","宝安区","龙华区"],"佛山":["禅城区","顺德区"],"南昌":["东湖区","青云谱区","青山湖区"]}
				data.setInitSelectText("请选择");

				TeaPickerView teaPickerView =new TeaPickerView(BasicMapActivity.this,data);
				teaPickerView
						.setDiscolourHook(true)
						.setRadius(25)
						.setContentLine(true)
						.setRadius(25)
						.setHeights(525)
						.build();
				teaPickerView.show(container);

				//选择器点击事件
				teaPickerView.setOnPickerClickListener(pickerData -> {
					teaPickerView.dismiss();//关闭选择器

				});
			}

			@Override
			public void finishWareHouse() {

			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.searching_selector:
				ivParkingTab.setImageResource(R.mipmap.ic_parking_tab_gray);
				tvParkingTab.setTextColor(getResources().getColor(R.color.third_class_text));
				ivSearchTab.setImageResource(R.mipmap.ic_search_tab_blue);
				tvSearchTab.setTextColor(getResources().getColor(R.color.tab_select));
				hideOthersFragment(searchFragment,false);
				//判断位置服务是否开启
				LocationManager locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
				boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
				if (!gps){
					openSystemGPS();
				}

				break;
			case R.id.parking_selector:
				ivParkingTab.setImageResource(R.mipmap.ic_parking_tab_select);
				tvParkingTab.setTextColor(getResources().getColor(R.color.tab_select));
				ivSearchTab.setImageResource(R.mipmap.ic_search_tab_gray);
				tvSearchTab.setTextColor(getResources().getColor(R.color.third_class_text));
				hideOthersFragment(parkingFragment,false);
				break;
		}
	}

	private void openSystemGPS() {
		ConfirmUtils utils = new ConfirmUtils();
		utils.showConfirmDialog(this, "需要打开系统定位开关", "用于提供精确的定位服务", "取消", "去设置", new ConfirmCallable() {
			@Override
			public void unaccept() {

			}

			@Override
			public void accept() {
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				startActivity(intent);

			}
		});

	}


	private void hideOthersFragment(Fragment showFragment, boolean add) {
		transaction = getSupportFragmentManager().beginTransaction();
		if (add) {
			transaction.add(R.id.fram_container, showFragment);
		}

		for (Fragment fragment : mFragments) {
			if (showFragment.equals(fragment)) {
				transaction.show(fragment);
			} else {
				transaction.hide(fragment);
			}
		}
		transaction.commitNowAllowingStateLoss();
	}

	private void showMyLoc(){
		//定位
		if(aMap == null){
			return;
		}
		MyLocationStyle myLocationStyle;
		myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
		myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
		myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
		Bitmap myPointer = BitmapFactory.decodeResource(getResources(),R.drawable.my_pointer);
		BitmapDescriptor descriptor = BitmapDescriptorFactory.fromBitmap(myPointer);
		myLocationStyle.myLocationIcon(descriptor);
		aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
		aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

		aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
			@Override
			public void onMyLocationChange(Location location) {
				//从location对象中获取经纬度信息，地址描述信息，建议拿到位置之后调用逆地理编码接口获取（获取地址描述数据章节有介绍）
				double myLat = location.getLatitude();
				double myLng = location.getLongitude();
				Log.d("location_mes","lat:"+myLat+"||myLng"+myLng);
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		//在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
		mapView.onDestroy();
	}
	@Override
	protected void onResume() {
		super.onResume();
		//在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
		mapView.onResume();
	}
	@Override
	protected void onPause() {
		super.onPause();
		//在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
		mapView.onPause();
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		//在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
		mapView.onSaveInstanceState(outState);
	}

	//定位权限申请
	/*
	 *判断当前是否是6.0版本
	 */
	@TargetApi(23)
	private void getPersimmions() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			ArrayList<String> permissions = new ArrayList<String>();
			/***
			 * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
			 */
			// 定位精确位置
			if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);

			}
			if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);

			}
			if (permissions.size() > 0) {
				requestPermissions(permissions.toArray(new String[permissions.size()]), PERMISSON_REQUESTCODE);
			} else {
				///当权限都有时，开始定位
				showMyLoc();
			}

		} else {

		}

	}


	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == PERMISSON_REQUESTCODE) {
			//            Settings.Secure.putInt(getContentResolver(), Settings.Secure.LOCATION_MODE, 1);
			// 转到手机设置界面，用户设置GPS
//            Intent intent = new Intent(
//                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//            startActivityForResult(intent, 0); // 设置完成后返回到原来的界面
			showMyLoc();
		}
	}


}

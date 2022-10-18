package cn.com.changan.huaxian.Activity.main;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import cn.com.changan.huaxian.Activity.useractivity.UserActivity;
import cn.com.changan.huaxian.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ParkingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ParkingFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "ParkingFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FrameLayout takePhoto;
    public static final int TAKE_PHOTO = 1;//声明一个请求码，用于识别返回的结果
    private ImageView picture;
    private Uri imageUri;
    private final String filePath = Environment.getExternalStorageDirectory() + File.separator+"Download"+ File.separator+ "incall"+ File.separator+ "output_image.jpg";
    private ViewGroup imgViewGroup;
    private ArrayList<Bitmap> imgList = new ArrayList<>();
    private ArrayList<View> deleteList = new ArrayList<>();

    //扫码
    public static final int REQUEST_CODE = 222;
    private ImageView scanVinBtn;
    private EditText vinTxt;
    private static final int SCAN = 0;
    private static final int SEARCH = 1;
    private int type = 0;
    private TextView tvManuallyLocate,tvAutoLocate ;

    //头像
    private ImageView headImg;


    public ParkingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ParkingFragment newInstance(String param1, String param2) {
        ParkingFragment fragment = new ParkingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        ZXingLibrary.initDisplayOpinion(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.submit_layout, container, false);

        tvManuallyLocate = view.findViewById(R.id.tv_manually_locate);
        tvManuallyLocate.setOnClickListener(this);
        tvAutoLocate = view.findViewById(R.id.tv_auto_locate);
        tvAutoLocate.setOnClickListener(this);

        takePhoto = view.findViewById(R.id.take_photo_btn);
        takePhoto.setOnClickListener(this);
        imgViewGroup = view.findViewById(R.id.img_viewgroup);

        scanVinBtn = view.findViewById(R.id.scan_vin);
        scanVinBtn.setOnClickListener(this);
        vinTxt = view.findViewById(R.id.car_vin_code);
        headImg = view.findViewById(R.id.submit_user_head);
        headImg.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_manually_locate:
                //手动定位
                tvManuallyLocate.setBackgroundResource(R.drawable.bg_locate_blue);
                tvManuallyLocate.setTextColor(getResources().getColor(R.color.white));
                tvAutoLocate.setBackgroundResource(R.color.white);
                tvAutoLocate.setTextColor(getResources().getColor(R.color.third_class_text));
                break;
            case R.id.tv_auto_locate:
                //自动定位
                tvAutoLocate.setBackgroundResource(R.drawable.bg_locate_blue);
                tvAutoLocate.setTextColor(getResources().getColor(R.color.white));
                tvManuallyLocate.setBackgroundResource(R.color.white);
                tvManuallyLocate.setTextColor(getResources().getColor(R.color.third_class_text));
                break;
            case R.id.take_photo_btn:
                requestPermission(SEARCH);
                break;
            case R.id.scan_vin:
                requestPermission(SCAN);
                break;
            case R.id.submit_user_head:
                Intent i = new Intent(getContext(), UserActivity.class);
                getContext().startActivity(i);
                break;
        }
    }

    //动态请求权限
    private void requestPermission(int i) {
        type = i;
        Log.d(TAG, "requestPermission I = "+i);
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //请求权限
            ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
            Log.d(TAG, "请求权限");
        } else {
            //调用
            if(i==SCAN){
                Intent intent = new Intent(getContext(), CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                Log.d(TAG, "打开CaptureActivity");
            }else {
                requestCamera();
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults != null && grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case 1: {
                    if(type == SEARCH){
                        requestCamera();
                    }else {
                        Intent intent = new Intent(getContext(), CaptureActivity.class);
                        startActivityForResult(intent, REQUEST_CODE);
                    }
                }
                break;
            }

        }
    }

    private void requestCamera() {
        Log.d(TAG, "requestCamera filePath="+filePath);
        File outputImage = new File(filePath);
        try
        {
            if (!outputImage.getParentFile().exists()) {
                outputImage.getParentFile().mkdirs();
            }
            if (outputImage.exists()) {
                outputImage.delete();
            }

            outputImage.createNewFile();

            if (Build.VERSION.SDK_INT >= 24) {
                imageUri = FileProvider.getUriForFile(getContext(),
                        "com.example.mydemo.fileprovider", outputImage);
            } else {
                imageUri = Uri.fromFile(outputImage);
            }
            //使用隐示的Intent，系统会找到与它对应的活动，即调用摄像头，并把它存储
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, TAKE_PHOTO);
            //调用会返回结果的开启方式，返回成功的话，则把它显示出来
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //处理返回结果的函数，下面是隐示Intent的返回结果的处理方式，具体见以前我所发的intent讲解
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(imageUri));
                        View view = View.inflate(getContext(),R.layout.picture_img_layout,null);
                        picture = view.findViewById(R.id.pic);
                        ImageView imgDeleteBtn = view.findViewById(R.id.delete_pic_btn);
                        imgDeleteBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                imgList.remove(deleteList.indexOf(imgDeleteBtn));
                                imgViewGroup.removeViewAt(deleteList.indexOf(imgDeleteBtn));
                                deleteList.remove(deleteList.indexOf(imgDeleteBtn));
                                Log.d("takephoto","index is "+deleteList.indexOf(imgDeleteBtn));

                            }
                        });
                        picture.setImageBitmap(extract(bitmap));
                        LinearLayout.LayoutParams margin = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        margin.setMargins(0, 0, 15, 0);
                        imgViewGroup.addView(view,margin);
                        imgList.add(bitmap);
                        deleteList.add(imgDeleteBtn);
                        if(imgList.size()>2){
                            takePhoto.setVisibility(View.GONE);
                        }else {
                            takePhoto.setVisibility(View.VISIBLE);
                        }
//                        picture.setImageBitmap(bitmap);
                        //将图片解析成Bitmap对象，并把它显现出来
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case REQUEST_CODE:
                //处理扫描结果（在界面上显示）
                if (null != data) {
                    Bundle bundle = data.getExtras();
                    if (bundle == null) {
                        return;
                    }
                    if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                        String result = bundle.getString(CodeUtils.RESULT_STRING);
                        vinTxt.setText("");
                        vinTxt.setText(result);
                    } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                        Toast.makeText(getContext(), "解析二维码失败", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            default:
                break;
        }
    }

    private Bitmap extract(Bitmap sourceBitmap){
        int h = sourceBitmap.getHeight();
        int w = sourceBitmap.getWidth();
        int myWidth;
        if(h>w){
            myWidth = w;
        }else {
            myWidth = h;
        }
        Bitmap myBit = Bitmap.createBitmap(sourceBitmap,0,0,myWidth,myWidth);
        int THUMB_SIZE;
        THUMB_SIZE = 5;
        Bitmap bmp = ThumbnailUtils.extractThumbnail(myBit, myWidth/ THUMB_SIZE, myWidth
                / THUMB_SIZE);
        return bmp;
    }







}
package cn.com.changan.huaxian.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import cn.com.changan.huaxian.R;
import cn.com.changan.huaxian.entity.ConfirmEntity;
import cn.com.changan.huaxian.util.ConfirmCallable;
import cn.com.changan.huaxian.util.ScreenUtil;

public class ConfirmSingleDialog extends Dialog implements View.OnClickListener {
    private Activity mActivity;
    private ConfirmEntity content = null;
    private TextView titleTV, contentTV;
    private TextView acceptBtn;
    private ConfirmCallable listenner = null;

    public ConfirmSingleDialog(@NonNull Context context, ConfirmEntity content) {
        super(context, R.style.Translucent_NoTitle);
        mActivity = (Activity) context;
        this.content = content;
        View confirmView = LayoutInflater.from(context).inflate(R.layout.dialog_confirm_single, null);
        titleTV = confirmView.findViewById(R.id.dialog_title);
        contentTV = confirmView.findViewById(R.id.dialog_content);
        acceptBtn = confirmView.findViewById(R.id.acceptbtn);

        initData();
        acceptBtn.setOnClickListener(this);
        setCanceledOnTouchOutside(false);
        setContentView(confirmView);
    }

    private void initData() {
        if (content != null) {
            String title = content.getTitle();
            if (TextUtils.isEmpty(title)) {
                titleTV.setText("滑线车辆");
            } else {
                titleTV.setText(title);
            }
            contentTV.setText(content.getContent());

            String acceptStr = content.getBtn_accept_content();

            if (TextUtils.isEmpty(acceptStr)) {
                acceptBtn.setText("确定");
            } else {
                acceptBtn.setText(acceptStr);
            }
            listenner = content.getListenner();
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.acceptbtn) {
            if (listenner != null) {
                listenner.accept();
            }
            dismiss();
        }
    }
//    @Override
//    protected void onStart() {
//        super.onStart();
//        ScreenUtil.getDeviceSize(getContext());
//        WindowManager.LayoutParams params = getWindow().getAttributes();
////        params.width = WindowManager.LayoutParams.MATCH_PARENT;
////        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        params.width = ScreenUtil.SCREEN_WIDTH * 2 / 3;
//        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        params.gravity = Gravity.CENTER;
//        getWindow().setAttributes(params);
//    }
    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (event.getKeyCode() == KeyEvent.KEYCODE_BACK)) {
            return true;//屏蔽返回键
        }
        return super.onKeyDown(keyCode, event);
    }

    public static ConfirmSingleDialog getInstance(Context context, ConfirmEntity content) {
        return new ConfirmSingleDialog(context, content);
    }

    public void setContent(String value){
        contentTV.setText(value);
    }

}

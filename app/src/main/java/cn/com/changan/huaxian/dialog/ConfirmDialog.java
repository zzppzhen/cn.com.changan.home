package cn.com.changan.huaxian.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import cn.com.changan.huaxian.R;
import cn.com.changan.huaxian.entity.ConfirmEntity;
import cn.com.changan.huaxian.util.ConfirmCallable;

public class ConfirmDialog extends Dialog implements View.OnClickListener {
    private ConfirmEntity content = null;
    private TextView titleTV, contentTV;
    private TextView cancleBtn, acceptBtn;
    private ConfirmCallable listenner = null;

    public ConfirmDialog(@NonNull Context context, ConfirmEntity content) {
        super(context, R.style.Translucent_NoTitle);
//        mActivity = (Activity) context;
        this.content = content;
        View confirmView = LayoutInflater.from(context).inflate(R.layout.dialog_confirm, null);
        titleTV = confirmView.findViewById(R.id.dialog_title);
        contentTV = confirmView.findViewById(R.id.dialog_content_one);
        cancleBtn = confirmView.findViewById(R.id.unacceptbtn);
        acceptBtn = confirmView.findViewById(R.id.acceptbtn);

        initData();
        cancleBtn.setOnClickListener(this);
        acceptBtn.setOnClickListener(this);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
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

            String cancleStr = content.getBtn_unaccept_content();
            String acceptStr = content.getBtn_accept_content();

            if (TextUtils.isEmpty(cancleStr) || TextUtils.isEmpty(acceptStr)) {
                cancleBtn.setText("取消");
                acceptBtn.setText("确定");
            } else {
                cancleBtn.setText(cancleStr);
                acceptBtn.setText(acceptStr);
            }
            listenner = content.getListenner();
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.unacceptbtn) {
            if (listenner != null) {
                listenner.unaccept();
            }
            dismiss();
        } else if (id == R.id.acceptbtn) {
            if (listenner != null) {
                listenner.accept();
            }
            dismiss();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (event.getKeyCode() == KeyEvent.KEYCODE_BACK)) {
            return true;//屏蔽返回键
        }
        return super.onKeyDown(keyCode, event);
    }
    public static ConfirmDialog getInstance(Context context, ConfirmEntity content) {
        return new ConfirmDialog(context, content);
    }
}

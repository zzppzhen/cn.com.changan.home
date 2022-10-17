package cn.com.changan.huaxian.util;

import android.content.Context;

import cn.com.changan.huaxian.dialog.ConfirmDialog;
import cn.com.changan.huaxian.dialog.ConfirmSingleDialog;
import cn.com.changan.huaxian.entity.ConfirmEntity;

public class ConfirmUtils {
    public ConfirmUtils() {

    }

    public void showConfirmDialog(Context activity, String title, String content, String cancleStr, String acceptStr, ConfirmCallable listenner) {
        ConfirmEntity dialog_content = new ConfirmEntity(title, content, cancleStr, acceptStr, listenner);
        ConfirmDialog.getInstance(activity, dialog_content).show();
    }

    public void showConfirmDialog(Context activity, String title, String content, ConfirmCallable listenner) {
        ConfirmEntity dialog_content = new ConfirmEntity(title, content, "取消", "确定", listenner);
        ConfirmDialog.getInstance(activity, dialog_content).show();
    }

    public void showSingleConfirmDialog(Context activity, String title, String content, ConfirmCallable listenner) {
        ConfirmEntity dialog_content = new ConfirmEntity(title, content, "", "确定", listenner);
        ConfirmSingleDialog.getInstance(activity, dialog_content).show();
    }

    public void showSingleConfirmDialog(Context activity, String title, String content,String btnText, ConfirmCallable listenner) {
        ConfirmEntity dialog_content = new ConfirmEntity(title, content, "", btnText, listenner);
        ConfirmSingleDialog.getInstance(activity, dialog_content).show();
    }

}

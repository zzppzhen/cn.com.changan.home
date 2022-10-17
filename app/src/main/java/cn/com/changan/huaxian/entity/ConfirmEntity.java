package cn.com.changan.huaxian.entity;

import cn.com.changan.huaxian.util.ConfirmCallable;
/**
 * ConfirmDialog的内容以及事件
 */
public class ConfirmEntity {

    private String title = "";//dialog标题
    private String content = "";//dialog提示内容
    private String btn_unaccept_content = "";//取消按钮上的文字
    private String btn_accept_content = "";//确定按钮上的文字
    private ConfirmCallable listenner = null;

    public ConfirmEntity(String title, String content, String btn_unaccept_content, String btn_accept_content, ConfirmCallable listenner) {
        this.title = title;
        this.content = content;
        this.btn_unaccept_content = btn_unaccept_content;
        this.btn_accept_content = btn_accept_content;
        this.listenner = listenner;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBtn_unaccept_content() {
        return btn_unaccept_content;
    }

    public void setBtn_unaccept_content(String btn_unaccept_content) {
        this.btn_unaccept_content = btn_unaccept_content;
    }

    public String getBtn_accept_content() {
        return btn_accept_content;
    }

    public void setBtn_accept_content(String btn_accept_content) {
        this.btn_accept_content = btn_accept_content;
    }

    public ConfirmCallable getListenner() {
        return listenner;
    }

    public void setListenner(ConfirmCallable listenner) {
        this.listenner = listenner;
    }

}

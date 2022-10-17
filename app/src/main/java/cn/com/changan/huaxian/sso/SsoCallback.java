package cn.com.changan.huaxian.sso;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/1/9.
 */

public interface SsoCallback {

    //接口调用成功，且返回ok
    void onResponse(Call call, Response response);

    //接口调用成功，但返回失败
    void onFailed(Call call, IOException e);

}

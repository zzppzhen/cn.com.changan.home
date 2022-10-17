package cn.com.changan.huaxian.http;


import java.io.IOException;

import okhttp3.Call;


public interface HttpCallback {
    void onFailure(Call call, IOException e);

    void onResponse(Call call, String response, boolean tokenRefreshd);

}

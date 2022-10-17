package cn.com.changan.huaxian.util;

import androidx.annotation.NonNull;

import org.json.JSONException;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpUtil {
    private static OkHttpClient okHttpClient;
    public HttpUtil(){
        okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .callTimeout(10,TimeUnit.SECONDS)
                .readTimeout(10,TimeUnit.SECONDS)
                .build();
    }
    private static HttpUtil instance;
    public static HttpUtil getInstance(){
        if (instance==null){
            instance = new HttpUtil();
      }
        return instance;
    }

    public static void getOKHttp(String url, Map<String,Object> map, HttpCallBack httpCallBack){
        getInstance();
        StringBuilder builder = new StringBuilder(url).append("?");
        for (Map.Entry<String,Object> entry:map.entrySet()){
            builder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        Request request = new Request.Builder()
                .get()
                .url(builder.toString())
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                httpCallBack.onFail(e.getMessage());

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    httpCallBack.onSuccess(Objects.requireNonNull(response.body()).string(),ResultData.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public static void postOKHttp(String url, Map<String,Object> map, HttpCallBack httpCallBack){
        getInstance();
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String,Object> entry:map.entrySet()){
            builder.add(entry.getKey(),String.valueOf(entry.getValue()));
        }
        FormBody formBody = builder.build();
        Request request = new Request.Builder()
                .post(formBody)
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                httpCallBack.onFail(e.getMessage());

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    httpCallBack.onSuccess(Objects.requireNonNull(response.body()).string(),ResultData.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
    public interface HttpCallBack{
//        void onSuccess(ResultData resultData);
        void onFail(String error);

        void onSuccess(String response, Class<ResultData> resultDataClass) throws JSONException;
    }

}











package cn.com.changan.huaxian.sso;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.com.changan.huaxian.cookie.CookiesManager;
import cn.com.changan.huaxian.util.Base64Utils;
import cn.com.changan.huaxian.util.RSAUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/1/9.
 */

public class SsoManager {
    public final static String TAG = "SsoManager";
    private static SsoManager instance;
    private static boolean isInit = false;
    public OkHttpClient client;//= new OkHttpClient();
    private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

    public static SsoManager getInstance() {
        if (!isInit) {
            throw new RuntimeException("SSOManager not init !!!");
        }
        return instance;
    }

    public static void init(Context context){
        if (!isInit) {
            instance = new SsoManager(context);
            isInit = true;
        }
    }


    private SsoManager(Context context) {
        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .cookieJar(new CookiesManager(context)).build();
    }

    public void getStatusCode(String thirdFlag, String redirect_uri, String state, String client_id, final SsoCallback cb) {
        String url = WXIDInfoEntry.server + "cac/api/v1/oauth2login/" + thirdFlag + "?response_type=" + WXIDInfoEntry.response_type + "&redirect_uri=" + redirect_uri + "&state=" + state + "&client_id=" + client_id;
        Request request = new Request.Builder().get().url(url).addHeader("Response-Form", "json").build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                cb.onFailed(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                InCallLog.d(TAG,"response = "+response.body().string());
                cb.onResponse(call, response);
            }
        });
    }

    public void isBind(String thirdFlag, String wxCode, String wxState, final SsoCallback cb) {

        String url = WXIDInfoEntry.server + "cac/api/v1/oauth2callback/" + thirdFlag + "?code=" + wxCode + "&state=" + wxState + "&appId=" + WXIDInfoEntry.mobileAppid;
        Request request = new Request.Builder().get()
                .addHeader("response-form", "json").url(url).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                cb.onFailed(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                cb.onResponse(call, response);
            }
        });
    }

    public void getCacInfo(String url, final SsoCallback cb) {
        Request request = new Request.Builder().get()
                .addHeader("response-form", "json").url(url).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                cb.onFailed(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                cb.onResponse(call, response);
            }
        });
    }


    public void getAccessToken(String cacCode, String redirect_uri, String CAC_APP_ID, String client_secret, String grant_type, final SsoCallback cb) { // 获取access_token函数
        String url = WXIDInfoEntry.server + "cac/api/v1/oauth2/access_token";
        RequestBody requestBody = new FormBody.Builder()
                .add("redirect_uri", redirect_uri)
                .add("client_id", CAC_APP_ID)
                .add("client_secret", client_secret)
                .add("grant_type", grant_type)
                .add("code", cacCode).build();
        Request request = new Request.Builder().post(requestBody).url(url).addHeader("Response-Form", "json").build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                cb.onFailed(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                cb.onResponse(call, response);
            }
        });
    }

    /**
     * 获取绑定验证码
     *
     * @param args
     */
    public void getWxSms(JSONArray args, final SsoCallback cb) {

        String mobile = "";
        try {
            mobile = args.getString(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = WXIDInfoEntry.server + "cac/api/v1/cac/send/smver/code";
        String jiami = "";
        try {
            jiami = Base64Utils.encode(RSAUtils.encryptByPublicKey(WXIDInfoEntry.CAC_APP_ID.getBytes(), Base64Utils.decode(WXIDInfoEntry.PUB_KEY)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody requestBody = new FormBody.Builder()
                .add("appid", WXIDInfoEntry.CAC_APP_ID)
                .add("seccode", jiami)
                .add("mobile", mobile)
                .build();
        Request request = new Request.Builder().post(requestBody)
                .addHeader("response-form", "json")
                .url(url).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                cb.onFailed(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                cb.onResponse(call, response);
            }
        });
    }


    public void refreshToken(String refreshToken, final SsoCallback cb) {
        String url = WXIDInfoEntry.server + "cac/api/v1/oauth2/refresh_token?client_id=" + WXIDInfoEntry.CAC_APP_ID + "&refresh_token=" + refreshToken;
        Request request = new Request.Builder().get().addHeader("response-form", "json").url(url).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                cb.onFailed(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                cb.onResponse(call, response);
            }
        });
    }


    public void goWxBind(JSONArray args, final SsoCallback cb) {
        String password = "", mobile = "", vercode = "";
        try {
            mobile = args.getString(0);
            password = args.getString(1);
            vercode = args.getString(2);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = WXIDInfoEntry.server + "cac/api/v1/oauth2login/bind";
        String pwdEncrpStr = "";
        try {
            pwdEncrpStr = Base64Utils.encode(RSAUtils.encryptByPublicKey(password.getBytes(), Base64Utils.decode(WXIDInfoEntry.PUB_KEY)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestBody requestBody = new FormBody.Builder()
                .add("appId", WXIDInfoEntry.CAC_APP_ID)
                .add("password", pwdEncrpStr)
                .add("mobile", mobile)
                .add("vercode", vercode)
                .build();

        Request request = new Request.Builder().post(requestBody)
                .addHeader("response-form", "json").url(url).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                cb.onFailed(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                cb.onResponse(call, response);
            }
        });
    }

    /**
     * 微信解绑
     *
     * @param accessToken
     * @param cb
     */
    public void unWxBind(String accessToken, final SsoCallback cb) {
        String url = WXIDInfoEntry.server + "cac/api/v1/oauth2/third/unbind?oauthFlag=0&access_token=" + accessToken;
        Request request = new Request.Builder().get().addHeader("response-form", "json").url(url).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                cb.onFailed(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                cb.onResponse(call, response);
            }
        });
    }

}

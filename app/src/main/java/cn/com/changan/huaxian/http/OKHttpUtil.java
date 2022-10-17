package cn.com.changan.huaxian.http;

import android.text.TextUtils;
import android.util.Base64;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OKHttpUtil {
    private static OkHttpClient client = null;
    SSLSocketFactory sslSocketFactory;
    private boolean isServerTrusted = true;
    private static final String POST_METHOD = "POST";
    private static final String GET_METHOD = "GET";

    private static final String TOKEN_KEY = "token";
    private static final String ACCESS_TOKEN_KEY = "access_token";

    private static final String PUB_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjoQkhfm1ZtX07+RlQ+dSXhlrpw+M0HGRMM6eohlZZyisAdjzEAU4Gt4+j+k8dZVT1P7hoO4/MxqJMyBmH2DnnuFOsxPEbKrOvYUcH1CGc4PjpvvAd9cHI+Npl/EvQlPxOht0020ovuBXSoWDxmVitHAOeYCeGDVWAqZnA8iznra3ntNpyKvpTcriZVGzhVek5n1LSulh2RuE7et00nUL7wOC8r68nTypasgOGWl6N/OTTf9XJsOENxgY3aKW8Q47TjgeGisUSks3M7Db0CbBl4Frz3cb1SFGVf3fjrinJug9PaEQUl5UOxv72a8NiVfjTsuty5rAhG/6V1Bnk+eWzwIDAQAB";

    private static OKHttpUtil httpsUtils = new OKHttpUtil();
    private X509TrustManager trustManager;

    public static OKHttpUtil getInstance() {
        return httpsUtils;
    }

    /**
     * 初始化HTTPS,添加信任证书
     */
    private OKHttpUtil() {
    }


    //    private void postHttpRequestInternal(final Map<String, String> params, final String url, final HttpCallback callback, final boolean needIntercept) {
//        RequestBody requestBody = OKHttpUtil.mapToBuilder(params);
//        Request request = new Request.Builder().url(url)
//                .post(requestBody)
//                .build();
//
//        OkHttpClient okHttpClient = getOkHttpClient(url);
//        okHttpClient.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                callback.onFailure(call, e);
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                checkResponse(call, response, params, url, callback, needIntercept, POST_METHOD);
//            }
//        });
//    }
//    /**
//     * 调用post方法进行网络请求，token过期时，会执行token刷新，获取到最新token再里进行请求.
//     * 但是，当请求的参数中没有token，没有必要进行拦截
//     *
//     * @param params
//     * @param url
//     * @param callback
//     */
    public void postInterceptHttpRequest(Map<String, String> params, String url, HttpCallback callback) {
        postHttpRequestInternal(params, url, callback, true);
    }

    private void postHttpRequestInternal(final Map<String, String> params, final String url, final HttpCallback callback, final boolean needIntercept) {
        RequestBody requestBody = OKHttpUtil.mapToBuilder(params);
        Request request = new Request.Builder().url(url)
                .addHeader(ProjectConfig.AccessSource.HEADER_NAME, ProjectConfig.AccessSource.SOURCE_INCALL)
                .post(requestBody)
                .build();

        OkHttpClient okHttpClient = getOkHttpClient(url);
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                checkResponse(call, response, params, url, callback, needIntercept, POST_METHOD);
                callback.onResponse(call,response.body().string(),false);
            }
        });
    }



    public static RequestBody mapToBuilder(Map map) {
        FormBody.Builder builder = new FormBody.Builder();
        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            builder.add(entry.getKey(), entry.getValue());
        }
        return builder.build();
    }

    /**
     * 返回获取ssl后的okhttpclient，用于请求访问https
     *
     * @return okhttpclient
     */
    private OkHttpClient getOkHttpClient(String url) {
        String baseUrl = ProjectConfig.BASE_URL;
        if (!TextUtils.isEmpty(baseUrl) && url.startsWith(baseUrl)) {
            if (client == null) {
                initVerifyHttpClient();
            }
            return client;
        } else {// 不是m.iov域名返回不进行公钥校验的client
            return getHostAlltClient();
        }
    }

    /**
     * 初始化HttpClient,添加信任证书
     */
    private void initVerifyHttpClient() {
        String baseUrl = ProjectConfig.BASE_URL;
        if (!TextUtils.isEmpty(baseUrl) && ProjectConfig.isReleaseEnvironment()) {//正式环境校验公钥
            try {
                sslSocketFactory = getSSLSocketFactory();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            client = new OkHttpClient.Builder().sslSocketFactory(sslSocketFactory).hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return isServerTrusted;
                }
            }).build();
        } else {
            client = getHostAlltClient();
        }
    }

    /**
     * 返回一个信任所有的client
     *
     * @return
     */
    public OkHttpClient getHostAlltClient() {
        OkHttpClient client = new OkHttpClient.Builder().hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        }).build();

        return client;
    }

    public SSLSocketFactory getSSLSocketFactory() throws Exception {
        // Create a trust manager that does not validate certificate chains
        final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {

            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            //客户端并为对ssl证书的有效性进行校验
            @Override
            public void checkServerTrusted(
                    X509Certificate[] chain,
                    String authType) throws CertificateException {
                for (X509Certificate cert : chain) {
                    String pubkey = replaceBlank(Base64.encodeToString(cert.getPublicKey().getEncoded(), Base64.DEFAULT));
                    cert.checkValidity();
                    if (replaceBlank(PUB_KEY).equalsIgnoreCase(pubkey)) {
                        isServerTrusted = true;
                        break;
                    } else {
                        isServerTrusted = false;
                    }
                }
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }};

        // Install the all-trusting trust manager
        final SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        // Create an ssl socket factory with our all-trusting manager
        return sslContext.getSocketFactory();
    }

    private String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }
}
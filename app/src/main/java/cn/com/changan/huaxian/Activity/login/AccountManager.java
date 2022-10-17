package cn.com.changan.huaxian.Activity.login;

import android.content.Context;

import cn.com.changan.huaxian.util.SPUtils;

public class AccountManager {
    public static void saveToken(Context context,String token){
        if(token==null){
            return;
        }
        SPUtils.getInstance(context)
                .put("app_huaxian_token",token);
    }

    public static String saveToken(Context context){
        String token = SPUtils.getInstance(context)
                .getString("app_huaxian_token");
        return token;
    }

    public static boolean checkTokenIsNull(Context context){
        boolean isTokenNull = (SPUtils.getInstance(context).getString("app_huaxian_token")==null ||
                SPUtils.getInstance(context).getString("app_huaxian_token").equals(""));
        return isTokenNull;
    }
}

package cn.com.changan.huaxian.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Map;

public class ScreenUtil {
    private static String LOG = "Util";

    public static int SCREEN_WIDTH = 0;
    public static int SCREEN_HEIGHT = 0;
    public static int SCREEN_DPI = 0;
    public static float SCREEN_DENSITY;
    public static float SCALED_DENSITY;

    /**
     * @param context
     */
    public static final void getDeviceSize(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        SCREEN_WIDTH = displayMetrics.widthPixels;
        SCREEN_HEIGHT = displayMetrics.heightPixels;
        SCREEN_DENSITY = displayMetrics.density;
        SCREEN_DPI = displayMetrics.densityDpi;
        SCALED_DENSITY = displayMetrics.scaledDensity;
        Log.i(LOG, "SCREEN_WIDTH = " + SCREEN_WIDTH + "\nSCREEN_HEIGHT = " +
                SCREEN_HEIGHT + "\nSCREEN_DENSITY = " + SCREEN_DENSITY + "\nSCREEN_DPI" + SCREEN_DPI + "\nSCALED_DENSITY=" + SCALED_DENSITY);
    }

    /**
     * @return
     */
    public static int dip2px(float dpValue) {
        return (int) (dpValue * SCREEN_DENSITY + 0.5f);
    }

    /**
     * @param res
     * @param dp
     * @return
     */
    public static int dpToPx(Resources res, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.getDisplayMetrics());
    }

    /**
     * @param pxValue
     * @return
     */
    public static int px2dip(float pxValue) {
        return (int) (pxValue / SCREEN_DENSITY + 0.5f);
    }

    /**
     * @param context
     * @return
     */
    public static int getWindowWidth(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * @param context
     * @return
     */
    public static int getWindowHeight(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }


    public static String getVersionName(Context context) {
        String versionName = "";
        try {
            String packName = context.getApplicationContext().getPackageName();
            versionName = context.getApplicationContext().getPackageManager().getPackageInfo(
                    packName, 0).versionName;

        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    public static void openAPK(Context context, File file) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        context.startActivity(intent);

    }


    /*
     * <uses-permission android:name="android.permission.CALL_PHONE"/>
     */
    public static void callRing(Context context, String num, boolean isDail) {
        Uri uri = Uri.parse("tel:" + num);
        Intent intent;
        if (isDail) {//
            intent = new Intent(Intent.ACTION_CALL, uri);
        } else {//
            intent = new Intent(Intent.ACTION_DIAL, uri);
        }
        context.startActivity(intent);
    }

    private final static String REGEX = ".00";
    private final static String REGEX2 = "0";

    public static String handleFloat(float input) {
        DecimalFormat decimalFormat = new DecimalFormat(REGEX);
        String data = null;
        if (input >= 1.0f) {
            data = decimalFormat.format(input);
        } else {
            if (input == 0f) {
                return "0";
            }
            return String.valueOf(input);//
        }

        if (data.endsWith(REGEX)) {
            return data.replace(REGEX, "");
        }

        if (input > 1f && data.endsWith(REGEX2)) {
            return data.replace(REGEX2, "");
        }

        return data;
    }


    //保留1位小数
    public static String handleOneFloat(float input) {
        DecimalFormat decimalFormat = new DecimalFormat(".0");
        return decimalFormat.format(input);
    }

    //保留2位小数
    public static String handleTwoFloat(float input) {
        DecimalFormat decimalFormat = new DecimalFormat(".00");
        return decimalFormat.format(input);
    }

    public static String handleFloat3(float input) {
//        String inf=String.valueOf(input);
        String inf = handleOneFloat(input);
        if (inf.endsWith(".0")) {
            return inf.replace(".0", "");
        } else {
            return inf;
        }
    }

    public static String handleFloat4(float input) {
//        String inf=String.valueOf(input);
        String inf = handleTwoFloat(input);
        if (inf.endsWith(".0")) {
            return inf.replace(".0", "");
        }
        if (inf.endsWith(".00")) {
            return inf.replace(".00", "");
        }
        if (inf.contains(".") && inf.endsWith("0")) {
//            return inf.replace("0", "");
            return inf.substring(0, inf.length() - 1);
        }
        return inf;
    }

    //处理整数float input为整数
    public static final String handleIntFloat(float input) {
        String s = String.valueOf(input);
        if (s.endsWith(".0")) {
            s.replace(".0", "");
        }
        return s;
    }


    public static String handleText(String input, int size) {
        int length = input.length();

        if (length == size) {
            return input;
        }

        if (length > 4) {
            return input.substring(0, size);
        }

        if (length < 4) {
            StringBuffer sb = new StringBuffer(input);
            for (int i = 0; i < (size - length); i++) {
                sb.append(" ");
            }

            return sb.toString();
        }
        return "";
    }

    //double 保留2位小数
    public static final String handle2D(double source) {
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(source);
    }

    public static int calculateWidth() {
        return 0;
    }

    //传入的distacne单位为米
    public static final String handleDistance(float distance) {
        String rs;
        if (distance >= 1000f) {
            rs = handleTwoFloat(distance / 1000);
            if (rs.endsWith(".00")) {
                rs.replace(".00", "");
            }
            rs = rs + "km";
        } else {
            rs = handleTwoFloat(distance);
            if (rs.endsWith(".00")) {
                rs.replace(".00", "");
            }
            rs = rs + "m";
        }
        return rs;
    }

//    public static boolean isShowTabTip(@NonNull Context context, @NonNull String versionName) {
//        if (versionName.equals(DevicesUtil.getVersionName(context))) {
//            return true;
//        }
//        return false;
//    }

    /**
     * 将map数据转换成字符串：?key1=value1&key2=value2
     *
     * @param map
     * @return
     */
    public static String mapToString(Map<String, String> map) {
        if (map == null || map.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (String key : map.keySet()) {
            if (sb.length() == 0) {
                sb.append("?");
            } else {
                sb.append("&");
            }

            sb.append(key);
            sb.append("=");
            sb.append(map.get(key));
        }
        return sb.toString();
    }

    /**
     * 设置距离
     *
     * @param distance 距离
     */
    public static String setDistance(double distance) {
        String sdis = "";

        if (distance >= 1000) {
            sdis = String.format("%1$.2f%2$s", distance / 1000, "km");
        } else {
            sdis = String.format("%1$.0f%2$s", distance, "m");
        }
        return sdis;
    }


}
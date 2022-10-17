package cn.com.changan.huaxian.tools;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LegalTools {
    /*
     * 正则表达式判断手机号是否有效
     *
     * @param phone
     *
     * @return
     */
    public static boolean checkPhone(String phone) {
        String phones = phone.toString().trim();
        boolean flag;
        String check = "^[1][3,4,5,6,7,8,9][0-9]{9}$";
        Pattern regex = Pattern.compile(check);
        if ("".equals(phones) && phones == null) {
            return false;
        }
        Matcher matcher = regex.matcher(phones);
        flag = matcher.matches();

        return flag;
    }

    /*
     * 正则表达式判断验证码是否有效
     */
    public static  boolean checkCode(String code){
        String codes = code.toString().trim();
        boolean flag;
        String check = "^[0-9]{6}$";
        Pattern regex = Pattern.compile(check);
        if ("".equals(codes) && codes == null) {
            return false;
        }
        Matcher matcher = regex.matcher(codes);
        flag = matcher.matches();
        return flag;
    }


    public static boolean checkChinese(String chinese) {
        String regex = "[\u4e00-\u9fa5]{1,10}";
        Pattern pt = Pattern.compile(regex);
        Matcher matcher = pt.matcher(chinese);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    /**
     * 验证字符串是否为有效的日期格式(yyyy-MM-dd)
     *
     * @param strDate 日期格式字符串(2015-07-31)
     * @return true表示有效, false反之
     */
    @SuppressLint("SimpleDateFormat")
    public static boolean checkDate(String strDate) {
        if (null == strDate || TextUtils.isEmpty(strDate))
            return false;
        // set the format to use as a constructor argument
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (strDate.trim().length() != dateFormat.toPattern().length())
            return false;
        // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，
        // 比如2007/02/29会被接受，并转换成2007/03/01
        dateFormat.setLenient(false);
        try {
            // parse the inDate parameter
            dateFormat.parse(strDate.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }
}

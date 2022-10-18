package cn.com.changan.huaxian.http;

public class ProjectConfig {
    public static String BASE_URL = "https://pre-m.iov.changan.com.cn";

    public static final String SEND_AUTHCODE = BASE_URL+"/app-apigw/trolleyCar/public/api/v1/app/check/sendVerifyCode";

    public static final String LOGIN = BASE_URL+"/app-apigw/trolleyCar/public/api/v1/app/check/login";

    public static final String GET_CAR_LIST = BASE_URL+"/app-apigw/trolleyCar/api/v1/app/bus/getStopCarByMyList";

    public static class AccessSource {
        public static final String HEADER_NAME = "vcs-app-id";

        public static final String SOURCE_UNI = "unit";
        public static final String SOURCE_INCALL = "inCall";
        public static final String SOURCE_CHANGANFAN = "chananFan";
    }

    /**
     * 判断当前是否为正式环境
     */
    public static boolean isReleaseEnvironment() {
        return BASE_URL.equalsIgnoreCase("https://m.iov.changan.com.cn");
    }
}

package cn.com.changan.huaxian.http;

public class ProjectConfig {
    public static String SERVER_TEST = "https://tspdemo.changan.com.cn";
    public static String SERVER_PRE = "https://iov.changan.com.cn";
    public static String SERVER_PROD = "https://iov.changan.com.cn";

//    public static String BASE_URL = "https://pre-m.iov.changan.com.cn";
    public static String BASE_URL = SERVER_PRE;

    /***************** 认证接口 start *****************/

    //APP获取登录验证码
    public static final String SEND_AUTHCODE = BASE_URL+"/app-apigw/trolleyCar/public/api/v1/app/check/sendVerifyCode";

    //APP登录
    public static final String LOGIN = BASE_URL+"/app-apigw/trolleyCar/public/api/v1/app/check/login";

    //APP登出
    public static final String LOGOUT = BASE_URL+"/app-apigw/trolleyCar/public/api/v1/app/check/logout";

    /***************** 认证接口 end *****************/


    /***************** 业务接口 start *****************/

    //APP提交停车信息接口
    public static final String POST_STOP_INFO = BASE_URL+"/app-apigw/trolleyCar/public/api/v1/app/bus/postStopInfo";

    //根据VIN码查询车辆信息
    public static final String GET_TROLLEY_CAR_BY_VIN_LIST = BASE_URL+"/app-apigw/trolleyCar/public/api/v1/app/bus/getTrolleyCarByVinList";

    //获取停放车详情
    public static final String GET_STOP_CAR_INFO = BASE_URL+"/app-apigw/trolleyCar/public/api/v1/app/bus/getStopCarInfo";

    //获取当前用户停放的滑线车列表
    public static final String GET_STOP_CAR_BY_MY_LIST = BASE_URL+"/app-apigw/trolleyCar/public/api/v1/app/bus/getStopCarByMyList";

    //根据位置信息获取停车信息
    public static final String GET_STOP_INFO_BY_LOCATION = BASE_URL+"/app-apigw/trolleyCar/public/api/v1/app/bus/getStopCarInfoByLocation";

    //获取厂区信息
    public static final String GET_FACTORY_DIC = BASE_URL+"/app-apigw/trolleyCar/public/api/v1/app/data/getFactoryDic";

    //获取仓库列表。可无限下级，注意集合中childrenList
    public static final String GET_WAREHOUSE_LIST = BASE_URL+"/app-apigw/trolleyCar/public/api/v1/app/data/getWarehouseList";

    //获取滑线零部件列表
    public static final String GET_IND_DIC = BASE_URL+"/admin-apigw/trolleyCar/api/manage/getIndDic";


    /***************** 业务接口 end *****************/


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

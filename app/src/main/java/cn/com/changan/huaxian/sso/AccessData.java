package cn.com.changan.huaxian.sso;

public class AccessData { // 定义获取access_token 的返回数据格式
    private String access_token;
	private String refresh_token;

		public String getAccessToken() {
			return access_token;
		}

	public String getRefresh_token() {
		return refresh_token;
	}
}
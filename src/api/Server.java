package api;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class Server {

	static OkHttpClient client;
	static{
//		CookieJar cookieJar =new CookieJar(){
//			Map<HttpUrl, List<Cookie>> cookiemap = new HashMap<HttpUrl, List<Cookie>>();
//
//			@Override
//			public List<Cookie> loadForRequest(HttpUrl key) {
//			
//				List<Cookie> cookies = cookiemap.get(key);
//				 				if(cookies==null){
//				 					return new ArrayList<Cookie>();
//				 				}else{
//				 					return cookies;
//				 				}
//			}
//
//			@Override
//			public void saveFromResponse(HttpUrl key, List<Cookie> value) {
//				cookiemap.put(key, value);
//				
//			}
//		};
		
		CookieManager cookieManager=new CookieManager();
		cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
		
		client = new OkHttpClient.Builder()
				 				.cookieJar(new JavaNetCookieJar(cookieManager))
				 				.build();			
	}
	
	public static OkHttpClient getSharedClient(){
		 		return client;
		 	}
		 	
	public static String serverAddress="http://172.27.0.42:8080/membercenter/";
	
		 	public static Request.Builder requestBuilderWithApi(String api){
		 		return new Request.Builder()
		 		.url(serverAddress+"api/"+api);
		 	}
	
}

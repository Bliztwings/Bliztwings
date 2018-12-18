package com.ehyf.ewashing.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author fqdeng
 * 用于存储本地线程数据
 */
public abstract class LocalThreadUtils {
	public static final String CONSTRANT_APP_ID = "appId";//微信APPID
	public static final String CONSTRANT_HTTP_REQUEST_URL = "url";
	
	
	private static final ThreadLocal<Map<String,Object>> localParams = new ThreadLocal<Map<String,Object>>();
	
	public static Object get(String k){
		return localParams.get()==null?null:localParams.get().get(k);
	}
	
	public static void set(String key,String value){
		if(null==localParams.get()){
			localParams.set(new HashMap<String,Object>());
		}
		localParams.get().put(key, value);
	}
	public static void remove(){
		localParams.remove();
	}
}

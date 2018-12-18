/*
 * Copyright 2000-2020 YGSoft.Inc All Rights Reserved.
 */
package com.ehyf.ewashing.util;

import java.io.IOException;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;

/**
 * 通过Apach实现HTTP POST/GET JSON请求. <br>
 * 
 * @author shenxiaozhong <br>
 * @version 1.0.0 2012-12-6 下午5:59:21 <br>
 * @see
 * @since JDK 1.6.0
 */
public final class HttpAccessUtil {

	/**
	 * POST HTTP JSON请求. <br>
	 * 
	 * @author shenxiaozhong 2012-12-6 下午4:52:43 <br>
	 * @param url
	 *            post的URL，例如：http://10.52.2.1:8080/Resteasy/ResteasyService/postTestInfo
	 * @param param
	 *            post参数的键值对.
	 * @return HttpResponse.
	 */
	public static String httpPostSSL(final String url, final Map<String, String> paramMap) {
		BasicNameValuePair[] paramArray = null;
		if (paramMap != null) {
			paramArray = new BasicNameValuePair[paramMap.size()];
			int index = 0;
			for (Map.Entry<String, String> entry : paramMap.entrySet()) {
				BasicNameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue());
				paramArray[index++] = pair;
			}
		}

		try {
			return HttpClientManager.getInstance().httpPostSSL(url, paramArray);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * GET HTTP JSON请求. <br>
	 * 
	 * @author shenxiaozhong 2012-12-6 下午4:52:43 <br>
	 * @param url
	 *            get的URL，例如：http://10.52.2.1:8080/Resteasy/ResteasyService/getTestInfo
	 * @return 服务方法返回值.
	 */
	public static String httpGetSSL(final String url) {
		try {
			return HttpClientManager.getInstance().httpGetSSL(url);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static String uploadFile(final String url, final String filePath) {
		try {
			return HttpClientManager.getInstance().uploadFile(url, filePath);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String httpPostSSL(final String url, final String json) {
		try {
			return HttpClientManager.getInstance().httpPostSSL(url, json);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String httpPostSSL(final String url) {
		try {
			return HttpClientManager.getInstance().httpPostSSL(url);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static byte[] httpGetByte(final String url) {
		try {
			return HttpClientManager.getInstance().httpGetByte(url);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String httpPost(final String url, final Map<String, String> paramMap) {
		BasicNameValuePair[] paramArray = null;
		if (paramMap != null) {
			paramArray = new BasicNameValuePair[paramMap.size()];
			int index = 0;
			for (Map.Entry<String, String> entry : paramMap.entrySet()) {
				BasicNameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue());
				paramArray[index++] = pair;
			}
		}

		return HttpClientManager.getInstance().httpPost(url, paramArray);
	}

	public static String httpPostGrc(final String url, final Map<String, String> paramMap, final String method) {
		return HttpClientManager.getInstance().httpPostGrc(url, paramMap, method);
	}
}

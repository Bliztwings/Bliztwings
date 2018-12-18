package com.ehyf.ewashing.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

public class HttpUtil {

	/**
	 * HttpClient 模拟POST请求
	 * 
	 * @param url
	 * @param params
	 */
	public static String sendPost(String url, Map<String, String> params) {
		// 构造HttpClient的实例
		HttpClient httpClient = new HttpClient();

		// 创建POST方法的实例
		PostMethod postMethod = new PostMethod(url);

		// 设置请求头信息
		// postMethod.setRequestHeader("Connection", "close");
		httpClient.getParams().setContentCharset("UTF-8");
		postMethod.setRequestHeader("ContentType",
				"application/x-www-form-urlencoded;charset=UTF-8");

		// 添加参数
		for (Map.Entry<String, String> entry : params.entrySet()) {
			postMethod.addParameter(entry.getKey(), entry.getValue());
		}

		// 使用系统提供的默认的恢复策略,设置请求重试处理，用的是默认的重试处理：请求三次
		// httpClient.getParams().setBooleanParameter("http.protocol.expect-continue",
		// false);

		// 接收处理结果
		String result = null;
		try {
			// 执行Http Post请求
			httpClient.executeMethod(postMethod);

			// 返回处理结果
			// result = postMethod.getResponseBodyAsString();
			InputStream is = postMethod.getResponseBodyAsStream();
			result = getStreamString(is);

		} catch (HttpException e) {
			// 发生致命的异常，可能是协议不对或者返回的内容有问题
			System.out.println("请检查输入的URL!");
			e.printStackTrace();
		} catch (IOException e) {
			// 发生网络异常
			System.out.println("发生网络异常!");
			e.printStackTrace();
		} finally {
			// 释放链接
			postMethod.releaseConnection();

			// 关闭HttpClient实例
			if (httpClient != null) {
				// ((SimpleHttpConnectionManager)
				// httpClient.getHttpConnectionManager()).shutdown();
				httpClient = null;
			}
		}
		return result;
	}

	/**
	 * HttpClient 模拟GET请求
	 * 
	 * @param url
	 * @param params
	 */
	public static String sendGet(String url, Map<String, String> params) {
		// 构造HttpClient实例
		HttpClient client = new HttpClient();
		GetMethod getMethod = null;

		// 拼接参数
		String paramStr = "";
		if (params != null && params.size() > 0) {
			for (String key : params.keySet()) {
				paramStr = paramStr + "&" + key + "=" + params.get(key);
			}
			paramStr = paramStr.substring(1);
		}

		if ("".equals(paramStr)) {
			// 创建GET方法的实例
			getMethod = new GetMethod(url);
		} else {
			getMethod = new GetMethod(url + "?" + paramStr);
		}

		// 接收返回结果
		String result = null;
		try {
			// 执行HTTP GET方法请求
			client.executeMethod(getMethod);

			// 返回处理结果
			//result = method.getResponseBodyAsString();
			InputStream is = getMethod.getResponseBodyAsStream();
			result = getStreamString(is);
		} catch (HttpException e) {
			// 发生致命的异常，可能是协议不对或者返回的内容有问题
			System.out.println("请检查输入的URL!");
			e.printStackTrace();
		} catch (IOException e) {
			// 发生网络异常
			System.out.println("发生网络异常!");
			e.printStackTrace();
		} finally {
			// 释放链接
			getMethod.releaseConnection();

			// 关闭HttpClient实例
			if (client != null) {
				// ((SimpleHttpConnectionManager)
				// client.getHttpConnectionManager()).shutdown();
				client = null;
			}
		}

		return result;
	}

	// 将输入流转化为字符串
	public static String getStreamString(InputStream tInputStream) {
		if (tInputStream != null) {
			try {
				BufferedReader tBufferedReader = new BufferedReader(
						new InputStreamReader(tInputStream));
				StringBuffer tStringBuffer = new StringBuffer();
				String sTempOneLine;

				while ((sTempOneLine = tBufferedReader.readLine()) != null) {
					tStringBuffer.append(sTempOneLine);
				}

				return tStringBuffer.toString();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}

	public static void main(String[] args) {
		String url = "http://localhost:8080/api/sys/menu/list";
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginId", "jelly");
		params.put("password", "jelly");

		String result = HttpUtil.sendPost(url, params);
		System.out.println(result);
	}
}

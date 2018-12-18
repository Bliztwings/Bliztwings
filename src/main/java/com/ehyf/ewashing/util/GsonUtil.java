package com.ehyf.ewashing.util;

import java.lang.reflect.Type;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * Gson 工具类
 * 
 * @author jelly
 *
 * @param <T>
 */
public class GsonUtil<T> {
	/**
	 * 将对象输出为JSON格式字符串
	 *
	 * @param obj
	 * @return
	 */
	public static String toJson(Object obj) {
		if (obj == null)
			return "";
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		return gson.toJson(obj);
	}

	/**
	 * 将JSON格式字符串转化为对象或对象列表
	 * 
	 * @param jsonStr
	 * @return
	 */
	public T fromJson(String jsonStr) {
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		Type type = new TypeToken<T>() {}.getType();
		return gson.fromJson(jsonStr, type);
	}
}

package com.ehyf.ewashing.restful.client;

public abstract class ResultCode {
	public static final String SUCCESS = "0000";
	public static final String FAILTURE = "9999";
	public static final String ACCESS_DENIED="0001";
	
	
	public static ResultData<Object> success(String msg,Object o){
		return new ResultData<Object>(SUCCESS,msg,o);
	}
	
	public static ResultData<Object> error(String msg,Object o){
		return new ResultData<Object>(FAILTURE,msg,o);
	}
	
	public static ResultData<Object> accessDenied(String msg,Object o){
		return new ResultData<Object>(ACCESS_DENIED,msg,o);
	}
}

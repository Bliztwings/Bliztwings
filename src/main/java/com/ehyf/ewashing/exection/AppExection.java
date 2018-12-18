package com.ehyf.ewashing.exection;

/**
 * 自定义异常
 * @author chenxiaozhong
 *
 */
public class AppExection extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7939764962036077549L;

	private int code =500;
	
	public AppExection(){
		super();
	}
	
	public AppExection(String message){
		super(message);
	}
	
	public AppExection(Throwable cause){
		super(cause);
	}
	
	public AppExection(int code,String message,Throwable cause){
		super(message, cause);
		this.code=code;
	}
	
	public int getCode(){
		return code;
	}
}

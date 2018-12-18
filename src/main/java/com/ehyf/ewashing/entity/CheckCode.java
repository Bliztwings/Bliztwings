package com.ehyf.ewashing.entity;


public class CheckCode extends BaseEntity<CheckCode> {

	private static final long serialVersionUID = 1L;

	/**电话**/
	private String telephone;

	/**APPID**/
	private String appId;

	/**验证码**/
	private String code;

	/**0：有效；1：失效**/
	private String status;

	/**验证码类型(O2O_REGISTER:O2O注册;XE_REGISTER:小E注册)**/
	private String type;

	private java.util.Date createTime;

	public void setTelephone(String telephone){
		this.telephone = telephone;
	}

	public String getTelephone(){
		return this.telephone;
	}
	public void setAppId(String appId){
		this.appId = appId;
	}

	public String getAppId(){
		return this.appId;
	}
	public void setCode(String code){
		this.code = code;
	}

	public String getCode(){
		return this.code;
	}
	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return this.status;
	}
	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return this.type;
	}
	public void setCreateTime(java.util.Date createTime){
		this.createTime = createTime;
	}

	public java.util.Date getCreateTime(){
		return this.createTime;
	}

}

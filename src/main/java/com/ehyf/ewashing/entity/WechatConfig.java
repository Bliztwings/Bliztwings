package com.ehyf.ewashing.entity;

import java.util.Date;

public class WechatConfig {

	private String id;
	
	
	/**
	 * 应用ID
	 */
	private String appId;
	/**
	 * 应用秘钥
	 */
	private String appSecret;
	
	/**
	 * token信息
	 */
	private String token;
	
	/**
	 * 过期时间
	 */
	private long expires_in;
	
	/**
	 * token 生成时间
	 */
	private Date createTime;
	
	/**
	 * 商户ID
	 */
	private String merchantId;
	
	/**
	 * 签名key
	 */
	private String signKey;
	
	
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public String getSignKey() {
		return signKey;
	}
	public void setSignKey(String signKey) {
		this.signKey = signKey;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	public long getExpires_in() {
		return expires_in;
	}
	public void setExpires_in(long expires_in) {
		this.expires_in = expires_in;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getAppSecret() {
		return appSecret;
	}
	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
}

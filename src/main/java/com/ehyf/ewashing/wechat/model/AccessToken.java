package com.ehyf.ewashing.wechat.model;

public class AccessToken {

	/**
	 * 用户token信息
	 */
	private String access_token;

	/**
	 * 过期时间
	 */
	private Integer expires_in;

	public String getAccess_token() {

		return access_token;
	}

	public void setAccess_token(String access_token) {

		this.access_token = access_token;
	}

	public Integer getExpires_in() {

		return expires_in;
	}

	public void setExpires_in(Integer expires_in) {

		this.expires_in = expires_in;
	}

}

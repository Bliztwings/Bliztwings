package com.ehyf.ewashing.vo;

import java.io.Serializable;

public class StoreUserVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1424970684608648357L;

	/**
	 * 门店用户关系表主键ID
	 */
	private String storeUserId;

	/**
	 * 用户登录名称
	 */
	private String userName;

	/**
	 * 用户真名
	 */
	private String realName;

	/**
	 * StoreUser 表主键ID
	 */
	private String id;

	public String getStoreUserId() {
		return storeUserId;
	}

	public void setStoreUserId(String storeUserId) {
		this.storeUserId = storeUserId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}

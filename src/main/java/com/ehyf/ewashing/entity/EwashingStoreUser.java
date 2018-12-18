package com.ehyf.ewashing.entity;

import java.util.Date;

/**
 * 门店用户关系
 * 
 * @author Administrator
 * 
 */
public class EwashingStoreUser extends BaseEntity<EwashingStoreUser> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2132282411143563233L;

	private String id;
	
	/**
	 * 门店编码
	 */
	private String storeCode;

	/**
	 * SecurityUser 主键ID
	 */
	private String securityUserId;

	/**
	 * 创建时间
	 */
	private Date createDate;

	/**
	 * 创建用户
	 */
	private String createUserName;

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public String getStoreCode() {
		return storeCode;
	}

	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}

	public String getSecurityUserId() {
		return securityUserId;
	}

	public void setSecurityUserId(String securityUserId) {
		this.securityUserId = securityUserId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	
}

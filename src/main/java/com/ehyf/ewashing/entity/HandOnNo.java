package com.ehyf.ewashing.entity;

import java.util.Date;

public class HandOnNo extends BaseEntity<HandOnNo> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6981359222172110842L;

	private String id;

	/**
	 * 门店ID
	 */
	private String storeId;
	
	/**
	 * 门店名称
	 */
	private String storeName;
	

	/**
	 * 挂衣区
	 */
	private String handonArea;

	/**
	 * 挂衣号
	 */
	private String handOnNo;

	/**
	 * 创建人ID
	 */
	private String createUserId;
	/**
	 * 创建人
	 */
	private String createUserName;

	/**
	 * 创建时间
	 */
	private Date createDate;

	/**
	 * 更新人
	 */
	private String updateUserName;

	/**
	 * 更新时间
	 */
	private Date updateDate;
	
	/**
	 * 更新人ID
	 */
	private String updateUserId;
	

	/**
	 * 0 :未使用 1：已使用
	 */
	private String status;

	/**
	 * 1:门店 2：工厂
	 */
	private String handType;

	
	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getHandType() {
		return handType;
	}

	public void setHandType(String handType) {
		this.handType = handType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getHandOnNo() {
		return handOnNo;
	}

	public void setHandOnNo(String handOnNo) {
		this.handOnNo = handOnNo;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getUpdateUserName() {
		return updateUserName;
	}

	public void setUpdateUserName(String updateUserName) {
		this.updateUserName = updateUserName;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getHandonArea() {
		return handonArea;
	}

	public void setHandonArea(String handonArea) {
		this.handonArea = handonArea;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}
	
}

package com.ehyf.ewashing.entity;

import java.util.Date;

public class HandOnArea extends BaseEntity<HandOnArea> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7820199957360533996L;

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
	private String handOnArea;

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
	 * 1:门店 2：工厂 3:萨维亚 4：浣衣坊
	 */
	private String handType;

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

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getHandOnArea() {
		return handOnArea;
	}

	public void setHandOnArea(String handOnArea) {
		this.handOnArea = handOnArea;
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

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	

}

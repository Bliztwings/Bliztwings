package com.ehyf.ewashing.entity;

import java.util.Date;

public class ClothesFlow extends BaseEntity<ClothesFlow> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -199078411137651837L;
	
	private String id;
	
	private String orderId;
	
	private String clothesId;
	
	private String optUserName;
	
	private String optUserId;
	
	private Date optDate;
	
	private String optDateStr;
	
	private String clothesStatus;
	
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getClothesId() {
		return clothesId;
	}

	public void setClothesId(String clothesId) {
		this.clothesId = clothesId;
	}

	public String getOptUserName() {
		return optUserName;
	}

	public void setOptUserName(String optUserName) {
		this.optUserName = optUserName;
	}

	public String getOptUserId() {
		return optUserId;
	}

	public void setOptUserId(String optUserId) {
		this.optUserId = optUserId;
	}

	public Date getOptDate() {
		return optDate;
	}

	public void setOptDate(Date optDate) {
		this.optDate = optDate;
	}

	public String getClothesStatus() {
		return clothesStatus;
	}

	public void setClothesStatus(String clothesStatus) {
		this.clothesStatus = clothesStatus;
	}

	public String getOptDateStr() {
		return optDateStr;
	}

	public void setOptDateStr(String optDateStr) {
		this.optDateStr = optDateStr;
	}
	
	

}

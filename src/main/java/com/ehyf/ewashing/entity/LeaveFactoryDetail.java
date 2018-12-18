package com.ehyf.ewashing.entity;

public class LeaveFactoryDetail extends BaseEntity<LeaveFactoryDetail> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -388237747783636364L;

	private String id;
	
	/**
	 * 出厂单号
	 */
	private String leaveFactoryNumber;
	
	/**
	 * 衣服ID
	 */
	private String clothesId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLeaveFactoryNumber() {
		return leaveFactoryNumber;
	}

	public void setLeaveFactoryNumber(String leaveFactoryNumber) {
		this.leaveFactoryNumber = leaveFactoryNumber;
	}

	public String getClothesId() {
		return clothesId;
	}

	public void setClothesId(String clothesId) {
		this.clothesId = clothesId;
	}
	
	
}

package com.ehyf.ewashing.entity;

import java.util.Date;

public class LeaveFactory extends BaseEntity<LeaveFactory> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7864914797922418538L;

	private String id;

	/**
	 * 门店ID
	 */
	private String storeId;

	/**
	 * 衣服数量
	 */
	private int count;
	
	/**
	 * 出厂单号
	 */
	private String leaveFactoryNumber;
	

	/**
	 * 出厂日期
	 */
	private Date leaveDate;

	/**
	 * 操作人
	 */
	private String userId;

	/**
	 * 操作时间
	 */
	private Date optDate;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getOptDate() {
		return optDate;
	}

	public void setOptDate(Date optDate) {
		this.optDate = optDate;
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

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Date getLeaveDate() {
		return leaveDate;
	}

	public void setLeaveDate(Date leaveDate) {
		this.leaveDate = leaveDate;
	}

	public String getLeaveFactoryNumber() {
		return leaveFactoryNumber;
	}

	public void setLeaveFactoryNumber(String leaveFactoryNumber) {
		this.leaveFactoryNumber = leaveFactoryNumber;
	}
	
	
}

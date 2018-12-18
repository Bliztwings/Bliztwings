package com.ehyf.ewashing.entity;

import java.util.Date;

/**
 * 送洗记录表
 * 
 * @author shenxiaozhong
 *
 */
public class SendWashing extends BaseEntity<SendWashing> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9085150009488971152L;

	/**
	 * 主键ID
	 */
	private String id;

	/**
	 * 送洗人ID
	 */
	private String sendUserId;

	/**
	 * 送洗人
	 */
	private String sendUserName;

	/**
	 * 送洗时间
	 */
	private Date sendDate;

	/**
	 * 送洗时间
	 */
	private String sendDateStr;

	/**
	 * 送洗开始时间
	 */
	private String beginDate;

	/**
	 * 送洗结束时间
	 */
	private String endDate;

	/**
	 * 送洗单号
	 */
	private String sendNumber;

	/**
	 * 送洗衣服数量
	 */
	private int clothesCount;

	/**
	 * 门店ID
	 */
	private String storeId;

	/**
	 * 门店名称
	 */
	private String storeName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSendUserId() {
		return sendUserId;
	}

	public void setSendUserId(String sendUserId) {
		this.sendUserId = sendUserId;
	}

	public String getSendUserName() {
		return sendUserName;
	}

	public void setSendUserName(String sendUserName) {
		this.sendUserName = sendUserName;
	}

	public Date getSendDate() {
		return sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	public int getClothesCount() {
		return clothesCount;
	}

	public void setClothesCount(int clothesCount) {
		this.clothesCount = clothesCount;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getSendNumber() {
		return sendNumber;
	}

	public void setSendNumber(String sendNumber) {
		this.sendNumber = sendNumber;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getSendDateStr() {
		return sendDateStr;
	}

	public void setSendDateStr(String sendDateStr) {
		this.sendDateStr = sendDateStr;
	}

	
}

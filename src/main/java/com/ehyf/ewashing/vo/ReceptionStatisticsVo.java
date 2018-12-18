package com.ehyf.ewashing.vo;

import java.math.BigDecimal;

public class ReceptionStatisticsVo {

	/**
	 * 送洗开始时间
	 */
	private String beginDate;
	
	/**
	 * 送洗结束时间
	 */
	private String endDate;

	/**
	 * 门店ID
	 */
	private String storeId;
	
	/**
	 * 门店名称
	 */
	private String storeName;

	/**
	 * 数量
	 */
	private String count;
	
	/**
	 * 送洗时间 str
	 */
	private String sendDateStr;
	
	/**
	 * 合计金额
	 */
	private BigDecimal amount;
	
	
	

	public String getSendDateStr() {
		return sendDateStr;
	}

	public void setSendDateStr(String sendDateStr) {
		this.sendDateStr = sendDateStr;
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

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

}

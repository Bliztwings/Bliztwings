package com.ehyf.ewashing.entity;

import java.math.BigDecimal;

public class StoreStatistics extends BaseEntity<StoreStatistics> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4722805797216288995L;
	
	/**
	 * 门店名称
	 */
	public String storeName;
	
	/**
	 * 门店ID
	 */
	private String storeId;
	
	/**
	 * 订单数
	 */
	private int orderCount;
	
	/**
	 * 收衣数量
	 */
	private int receiveCount;
	
	/**
	 * 现金支付金额
	 */
	private BigDecimal cashAmount;
	
	/**
	 * 会员卡支付金额
	 */
	private BigDecimal memberCardAmount;
	
	/**
	 * 未支付金额
	 */
	private BigDecimal noPayAmount;
	
	
	/**
	 * 充值金额 
	 */
	private BigDecimal rechargeAmount;
	
	/**
	 * 统计日期
	 */
	private String statisticsDateStr;
	
	/**
	 * 开始时间
	 */
	private String beginDate;
	
	/**
	 * 结束时间
	 */
	private String endDate;
	

	public String getStoreName() {
		return storeName;
	}


	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}


	public String getStoreId() {
		return storeId;
	}


	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}


	public int getReceiveCount() {
		return receiveCount;
	}


	public void setReceiveCount(int receiveCount) {
		this.receiveCount = receiveCount;
	}


	public BigDecimal getCashAmount() {
		return cashAmount;
	}


	public void setCashAmount(BigDecimal cashAmount) {
		this.cashAmount = cashAmount;
	}


	public BigDecimal getMemberCardAmount() {
		return memberCardAmount;
	}


	public void setMemberCardAmount(BigDecimal memberCardAmount) {
		this.memberCardAmount = memberCardAmount;
	}


	public BigDecimal getNoPayAmount() {
		return noPayAmount;
	}


	public void setNoPayAmount(BigDecimal noPayAmount) {
		this.noPayAmount = noPayAmount;
	}


	public BigDecimal getRechargeAmount() {
		return rechargeAmount;
	}


	public void setRechargeAmount(BigDecimal rechargeAmount) {
		this.rechargeAmount = rechargeAmount;
	}


	public String getStatisticsDateStr() {
		return statisticsDateStr;
	}


	public void setStatisticsDateStr(String statisticsDateStr) {
		this.statisticsDateStr = statisticsDateStr;
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


	public int getOrderCount() {
		return orderCount;
	}
	public void setOrderCount(int orderCount) {
		this.orderCount = orderCount;
	}

}

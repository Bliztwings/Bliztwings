package com.ehyf.ewashing.vo;

import java.io.Serializable;
import java.math.BigDecimal;

public class SendUserIncomeVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9169543080548516564L;
	/**
	 * 当天收入
	 */
	private BigDecimal incomeDay;
	/**
	 * 当月收入
	 */
	private BigDecimal incomeMonth;
	
	/**
	 * 小E id
	 */
	private String sendUserId;
	
	/**
	 * 任务状态
	 */
	private String taskStatus;
	private String startDate;
	private String endDate;
	
	/**
	 * 订单总金额
	 */
	private BigDecimal sumAmount;
	
	/**
	 * 分配类型 1：取 衣  2：送衣
	 */
	private String distributeType;
	
	private String appId;
	
	
	public BigDecimal getSumAmount() {
		return sumAmount;
	}

	public void setSumAmount(BigDecimal sumAmount) {
		this.sumAmount = sumAmount;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getDistributeType() {
		return distributeType;
	}

	public void setDistributeType(String distributeType) {
		this.distributeType = distributeType;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getSendUserId() {
		return sendUserId;
	}

	public void setSendUserId(String sendUserId) {
		this.sendUserId = sendUserId;
	}

	public String getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

	public BigDecimal getIncomeDay() {
		return incomeDay;
	}

	public void setIncomeDay(BigDecimal incomeDay) {
		this.incomeDay = incomeDay;
	}

	public BigDecimal getIncomeMonth() {
		return incomeMonth;
	}

	public void setIncomeMonth(BigDecimal incomeMonth) {
		this.incomeMonth = incomeMonth;
	}
	
	
}

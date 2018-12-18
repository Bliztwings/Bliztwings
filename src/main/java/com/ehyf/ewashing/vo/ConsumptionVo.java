package com.ehyf.ewashing.vo;

import java.math.BigDecimal;

import com.ehyf.ewashing.entity.SecurityUser;

public class ConsumptionVo {
	
	private String cardNumber;//卡号
	private BigDecimal operatorAmount;//操作金额
	private SecurityUser loginUser;//用户会话
	private String sourceId;//来源ID
	private String remark;//操作备注
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public BigDecimal getOperatorAmount() {
		return operatorAmount;
	}
	public void setOperatorAmount(BigDecimal operatorAmount) {
		this.operatorAmount = operatorAmount;
	}
	public SecurityUser getLoginUser() {
		return loginUser;
	}
	public void setLoginUser(SecurityUser loginUser) {
		this.loginUser = loginUser;
	}
	public String getSourceId() {
		return sourceId;
	}
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}

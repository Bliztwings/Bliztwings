package com.ehyf.ewashing.vo;

import java.math.BigDecimal;

public class MemberVo {
	private String memberId;
	private String cardNumber;
	private BigDecimal cashAmount;
	private BigDecimal givedAmount;
	private String payedMethod;
	
	
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public BigDecimal getCashAmount() {
		return cashAmount;
	}
	public void setCashAmount(BigDecimal cashAmount) {
		this.cashAmount = cashAmount;
	}
	public BigDecimal getGivedAmount() {
		return givedAmount;
	}
	public void setGivedAmount(BigDecimal givedAmount) {
		this.givedAmount = givedAmount;
	}
	public String getPayedMethod() {
		return payedMethod;
	}
	public void setPayedMethod(String payedMethod) {
		this.payedMethod = payedMethod;
	}
	
	
}

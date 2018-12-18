package com.ehyf.ewashing.wechat.pay;

import java.math.BigDecimal;

public class RechargeRuleMapping {
	private String sysSource;
	private BigDecimal rechargeAmount;
	private BigDecimal giveAmount;
	public BigDecimal getRechargeAmount() {
		return rechargeAmount;
	}
	public void setRechargeAmount(BigDecimal rechargeAmount) {
		this.rechargeAmount = rechargeAmount;
	}
	public BigDecimal getGiveAmount() {
		return giveAmount;
	}
	public void setGiveAmount(BigDecimal giveAmount) {
		this.giveAmount = giveAmount;
	}
	public String getSysSource() {
		return sysSource;
	}
	public void setSysSource(String sysSource) {
		this.sysSource = sysSource;
	}
	
	
}

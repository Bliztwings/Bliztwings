package com.ehyf.ewashing.vo;

import java.math.BigDecimal;

public class OrderPayVo {

	/**
	 * 订单ID
	 */
	private String orderId;
	
	/**
	 * 订单总金额
	 */
	private BigDecimal orderAmount;
	
	/**
	 * 运费
	 */
	private BigDecimal freight;
	
	/**
	 * 会员ID
	 */
	private String memberId;
	
	/**
	 * 支付类型
	 */
	private String payType;
	
	/**
	 * 会员手机号
	 */
	private String mobilePhone;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public BigDecimal getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(BigDecimal orderAmount) {
		this.orderAmount = orderAmount;
	}

	public BigDecimal getFreight() {
		return freight;
	}

	public void setFreight(BigDecimal freight) {
		this.freight = freight;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	
	
}

package com.ehyf.ewashing.entity;

import java.math.BigDecimal;
import java.util.Date;

public class OrderPayRecord extends BaseEntity<OrderPayRecord> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	private String id ;
	
	/**
	 * 订单ID
	 */
	private String orderId;
	
	/**
	 *  订单号
	 */
	private String orderCode;
	
	/**
	 *  订单类型
	 */
	private String orderType;
	
	/**
	 * 付款类型 1：现金 2：会员卡，3:微信
	 */
	private String payType;
	
	/**
	 * 会员ID
	 */
	private String memberId;
	
	/**
	 * 会员名称
	 */
	private String memberName;
	
	/**
	 * 会员卡号
	 */
	private String cardNumber;
	
	/**
	 * 订单金额
	 */
	private BigDecimal orderAmount;
	
	/**
	 *  付款时间
	 */
	private Date payDate;
	
	/**
	 *   付款开始时间
	 */
	private String beginDate;
	
	/**
	 *  付款结束时间
	 */
	private String endDate;
	
	
	/**
	 * 手机号
	 */
	private String mobilePhone;
	
	/**
	 *  门店ID
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

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

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

	

	public BigDecimal getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(BigDecimal orderAmount) {
		this.orderAmount = orderAmount;
	}

	public Date getPayDate() {
		return payDate;
	}

	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
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

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
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
	
}

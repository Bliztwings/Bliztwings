package com.ehyf.ewashing.entity;

import java.math.BigDecimal;
import java.util.Date;

public class PrepayOrder extends BaseEntity<PrepayOrder> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -841088087338598506L;

	private String id;

	/**
	 * 1：支付 2：充值
	 */
	private String type;
	/**
	 * 1:现金 2：会员卡 3：微信 4：支付宝
	 */
	private String payType;

	/**
	 * 支付金额
	 */
	private BigDecimal orderAmount;

	/**
	 * 订单ID
	 */
	private String orderId;

	/**
	 * 订单号
	 */
	private String orderCode;

	/**
	 * 签名
	 */
	private String sign;

	/**
	 * 预支付交易会话标识
	 */
	private String prepayId;

	/**
	 * 客户端IP 地址
	 */
	private String ipAddress;
	/**
	 * 交易类型
	 */
	private String tradeType;

	/**
	 * 创建时间
	 */
	private Date createDate;

	/**
	 * 商户ID
	 */
	private String merchantId;

	/**
	 * 外部交易订单号
	 */
	private String outTradeNo;

	/**
	 * 微信唯一流水号
	 */
	private String openId;
	
	/**
	 * 会员ID
	 */
	private String memberId;
	
	/**
	 * 会员卡ID
	 */
	private String memberCardId;
	
	/**
	 * 预付订单状态 1：未完成 2：已完成
	 */
	private String status;
	
	
	public String getMemberCardId() {
		return memberCardId;
	}

	public void setMemberCardId(String memberCardId) {
		this.memberCardId = memberCardId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getPrepayId() {
		return prepayId;
	}

	public void setPrepayId(String prepayId) {
		this.prepayId = prepayId;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public BigDecimal getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(BigDecimal orderAmount) {
		this.orderAmount = orderAmount;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

}

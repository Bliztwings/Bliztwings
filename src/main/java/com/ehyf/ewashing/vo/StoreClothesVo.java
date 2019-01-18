package com.ehyf.ewashing.vo;

import java.io.Serializable;
import java.math.BigDecimal;

public class StoreClothesVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5564247480789335958L;

	private String id;

	/**
	 * 订单号
	 */
	private String orderCode;
	
	/**
	 * 订单ID
	 */
	private String orderId;

	/**
	 * 条码
	 */
	private String barCode;

	/**
	 * 紧急程度
	 */
	private String urgency;

	/**
	 * 衣服数量
	 */
	private String clothesCount;

	/**
	 * 服务费
	 */
	private BigDecimal serviceFee;

	/**
	 * 总金额
	 */
	private BigDecimal sumAmount;

	/**
	 * 服务类型
	 */
	private String serviceType;

	/**
	 * 内容
	 */
	private String content;

	/**
	 * 会员ID
	 */
	private String memberId;

	/**
	 * 会员卡号
	 */
	private String cardNumber;

	private String clothesName;

	/**
	 * 衣服颜色
	 */
	private String color;

	/**
	 * 品牌
	 */
	private String brand;

	/**
	 * 附件
	 */
	private String attachment;
	
	/**
	 * 附件列表
	 */
	private String attachList;
	

	/**
	 * 瑕疵
	 */
	private String flaw;
	
	/**
	 * 查询条件
	 */
	private String queryKey ;
	
	/**
	 * 拍照文件路径
	 */
	private String imagePath;
	
	/**
	 * 拍照 base 64 文件
	 */
	private String baseStr;

	/**
	 * 条码自动增长
	 */
	private int barCodeAuto;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getBaseStr() {
		return baseStr;
	}

	public void setBaseStr(String baseStr) {
		this.baseStr = baseStr;
	}

	public String getClothesName() {
		return clothesName;
	}

	public void setClothesName(String clothesName) {
		this.clothesName = clothesName;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	public String getFlaw() {
		return flaw;
	}

	public void setFlaw(String flaw) {
		this.flaw = flaw;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public int getBarCodeAuto() {
		return barCodeAuto;
	}

	public void setBarCodeAuto(int barCodeAuto) {
		this.barCodeAuto = barCodeAuto;
	}

	public String getUrgency() {
		return urgency;
	}

	public void setUrgency(String urgency) {
		this.urgency = urgency;
	}

	public String getClothesCount() {
		return clothesCount;
	}

	public void setClothesCount(String clothesCount) {
		this.clothesCount = clothesCount;
	}

	public BigDecimal getServiceFee() {
		return serviceFee;
	}

	public void setServiceFee(BigDecimal serviceFee) {
		this.serviceFee = serviceFee;
	}

	public BigDecimal getSumAmount() {
		return sumAmount;
	}

	public void setSumAmount(BigDecimal sumAmount) {
		this.sumAmount = sumAmount;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getQueryKey() {
		return queryKey;
	}

	public void setQueryKey(String queryKey) {
		this.queryKey = queryKey;
	}

	public String getAttachList() {
		return attachList;
	}

	public void setAttachList(String attachList) {
		this.attachList = attachList;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	
}

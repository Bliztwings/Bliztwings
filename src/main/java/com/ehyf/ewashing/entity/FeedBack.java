package com.ehyf.ewashing.entity;

import java.util.Date;

/**
 * 用户反馈
 * @author shenxiaozhong
 *
 */
public class FeedBack extends BaseEntity<FeedBack> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6107566115884138193L;

	/**
	 * 主键
	 */
	private String id;

	/**
	 * 反馈时间
	 */
	private String feedBackContent;

	/**
	 * 反馈时间
	 */
	private Date feedBackDate;

	/**
	 * 评论类型 1：洗护质量 2：服务态度 3：物流速度 4：产品易用性 5：付款流程 6：其他
	 */
	private String feedBackType;
	
	/**
	 * 1：意见反馈 2：订单评价
	 */
	private String type;

	/**
	 * 订单ID
	 */
	private String orderId;
	/**
	 * 订单号
	 */
	private String orderCode;
	
	/**
	 * 用户手机
	 */
	private String mobilePhone;
	
	
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFeedBackContent() {
		return feedBackContent;
	}

	public void setFeedBackContent(String feedBackContent) {
		this.feedBackContent = feedBackContent;
	}
	

	public Date getFeedBackDate() {
		return feedBackDate;
	}

	public void setFeedBackDate(Date feedBackDate) {
		this.feedBackDate = feedBackDate;
	}

	public String getFeedBackType() {
		return feedBackType;
	}

	public void setFeedBackType(String feedBackType) {
		this.feedBackType = feedBackType;
	}
	
	
}

package com.ehyf.ewashing.entity;

import java.util.Date;


/**
 * 收送人员订单表
 * @author Administrator
 *
 */
public class SendReceivePersonOrderRelation extends BaseEntity<SendReceivePersonOrderRelation> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2656247404350672575L;
	
	private String id;
	
	/**
	 * 小E ID
	 */
	private String sendReceivePersonId;
	/**
	 * 订单ID
	 */
	private String orderId;
	
	/**
	 * 用来区分订单类型
	 */
	private String appId;
	
	/**
	 *   分配时间
	 */
	private Date distributeDate;
	/**
	 * 分配人ID
	 */
	private String distributeUserId;
	
	/**
	 * 分配人
	 */
	private String distributeUser;
	
	/**
	 * 分配类型 1：取 衣  2：送衣
	 */
	private String distributeType;
	
	/**
	 * 接受状状态  1:已分配  2：已接受 3：已拒绝
	 */
	private String acceptStatus;
	/**
	 * distributeType =1  1：未取 2：未交 3:完成
	 * distributeType =2  1：未取 2：未送 3:完成
	 */
	private String  taskStatus;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

	public String getAcceptStatus() {
		return acceptStatus;
	}

	public void setAcceptStatus(String acceptStatus) {
		this.acceptStatus = acceptStatus;
	}

	public String getDistributeType() {
		return distributeType;
	}

	public void setDistributeType(String distributeType) {
		this.distributeType = distributeType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSendReceivePersonId() {
		return sendReceivePersonId;
	}

	public void setSendReceivePersonId(String sendReceivePersonId) {
		this.sendReceivePersonId = sendReceivePersonId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Date getDistributeDate() {
		return distributeDate;
	}

	public void setDistributeDate(Date distributeDate) {
		this.distributeDate = distributeDate;
	}

	public String getDistributeUserId() {
		return distributeUserId;
	}

	public void setDistributeUserId(String distributeUserId) {
		this.distributeUserId = distributeUserId;
	}

	public String getDistributeUser() {
		return distributeUser;
	}

	public void setDistributeUser(String distributeUser) {
		this.distributeUser = distributeUser;
	}
	

	
	

}

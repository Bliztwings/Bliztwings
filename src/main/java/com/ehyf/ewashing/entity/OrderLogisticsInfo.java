package com.ehyf.ewashing.entity;


public class OrderLogisticsInfo extends BaseEntity<OrderLogisticsInfo> {

	private static final long serialVersionUID = 1L;

	private String id;

	/**订单ID**/
	private String orderId;

	/**订单当前状态：1：取件中 2：送往工厂 3：清洗中 4：送回中 5：已签收**/
	private String orderStatus;

	/**物流信息内容**/
	private String content;

	private java.util.Date createTime;

	private String createUser;

	private java.util.Date updateTime;

	private String updateUser;

	/**0：否；1：是**/
	private String isDeleted;

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return this.id;
	}
	public void setOrderId(String orderId){
		this.orderId = orderId;
	}

	public String getOrderId(){
		return this.orderId;
	}
	public void setOrderStatus(String orderStatus){
		this.orderStatus = orderStatus;
	}

	public String getOrderStatus(){
		return this.orderStatus;
	}
	public void setContent(String content){
		this.content = content;
	}

	public String getContent(){
		return this.content;
	}
	public void setCreateTime(java.util.Date createTime){
		this.createTime = createTime;
	}

	public java.util.Date getCreateTime(){
		return this.createTime;
	}
	public void setCreateUser(String createUser){
		this.createUser = createUser;
	}

	public String getCreateUser(){
		return this.createUser;
	}
	public void setUpdateTime(java.util.Date updateTime){
		this.updateTime = updateTime;
	}

	public java.util.Date getUpdateTime(){
		return this.updateTime;
	}
	public void setUpdateUser(String updateUser){
		this.updateUser = updateUser;
	}

	public String getUpdateUser(){
		return this.updateUser;
	}
	public void setIsDeleted(String isDeleted){
		this.isDeleted = isDeleted;
	}

	public String getIsDeleted(){
		return this.isDeleted;
	}

}

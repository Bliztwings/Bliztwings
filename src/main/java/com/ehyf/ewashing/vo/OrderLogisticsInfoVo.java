package com.ehyf.ewashing.vo;

public class OrderLogisticsInfoVo {
	private String orderId;//订单ID
	private String orderStatus;//当前节点订单状态，1：取件中 2：送往工厂 3：清洗中 4：送回中 5：已签收
	private String content;//物流信息内容
	
	
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}

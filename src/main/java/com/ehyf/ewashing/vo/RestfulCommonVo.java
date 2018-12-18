package com.ehyf.ewashing.vo;

import java.util.List;

import com.ehyf.ewashing.entity.StoreOrder;

public class RestfulCommonVo {


	/**
	 * 结果集
	 */
	private List<StoreOrder> orderList;

	/**
	 * 请求服务编码 -1 ：失败 0000：成功
	 */
	private String code;

	/**
	 * 请求服务返回结果 -1 ：错误信息
	 */
	private String message;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


	public List<StoreOrder> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<StoreOrder> orderList) {
		this.orderList = orderList;
	}

}

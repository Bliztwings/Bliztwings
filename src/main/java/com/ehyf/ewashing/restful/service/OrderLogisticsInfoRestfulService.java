package com.ehyf.ewashing.restful.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ehyf.ewashing.restful.client.ResultCode;
import com.ehyf.ewashing.restful.client.ResultData;
import com.ehyf.ewashing.service.OrderLogisticsInfoService;

@Service
public class OrderLogisticsInfoRestfulService {
	@Autowired
	OrderLogisticsInfoService orderLogisticsInfoService;
	
	public ResultData<Object> getLogisticsInfoByOrderId(String orderId){
		return StringUtils.isBlank(orderId)?ResultCode.error("订单ID不能为空", null):ResultCode.success("查询成功", orderLogisticsInfoService.getLogisticsInfoByOrderId(orderId));
	}
}

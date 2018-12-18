package com.ehyf.ewashing.restful.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ehyf.ewashing.restful.service.OrderLogisticsInfoRestfulService;

@Controller
@RequestMapping("/o2o")
public class OrderLogisticsInfoRestful {
	@Autowired
	OrderLogisticsInfoRestfulService orderLogisticsInfoRestfulService;
	
	@RequestMapping(method = RequestMethod.POST,consumes = "application/json", value = "/queryOrderLogisticsInfo")
	@ResponseBody
	public ResultData<Object> queryOrderLogisticsInfo(@RequestParam(value="orderId",required=false) String orderId) {
		ResultData<Object> ret = orderLogisticsInfoRestfulService.getLogisticsInfoByOrderId(orderId);
		return ret;
	}
	
}

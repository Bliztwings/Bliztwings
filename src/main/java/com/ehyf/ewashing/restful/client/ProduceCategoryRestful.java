package com.ehyf.ewashing.restful.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ehyf.ewashing.entity.ProduceCategory;
import com.ehyf.ewashing.restful.service.ProduceCategoryRestfulService;

@Controller
@RequestMapping("/o2o")
public class ProduceCategoryRestful {
	
	@Autowired
	ProduceCategoryRestfulService produceCategoryRestfulService;
	
	@RequestMapping(method = RequestMethod.POST,consumes = "application/json", value = "/getRoot")
	@ResponseBody
	public ResultData<Object> getRoot(@RequestBody ProduceCategory c) {
		ResultData<Object> ret = produceCategoryRestfulService.getRoot();
		return ret;
	}
	
	@RequestMapping(method = RequestMethod.POST,consumes = "application/json", value = "/getChildren")
	@ResponseBody
	public ResultData<Object> getChildren(@RequestBody ProduceCategory c) {
		ResultData<Object> ret = produceCategoryRestfulService.getChildren(c.getParentId(), c.getCode());
		return ret;
	}
	
}

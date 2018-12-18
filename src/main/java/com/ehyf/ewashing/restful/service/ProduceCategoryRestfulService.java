package com.ehyf.ewashing.restful.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ehyf.ewashing.restful.client.ResultCode;
import com.ehyf.ewashing.restful.client.ResultData;
import com.ehyf.ewashing.service.ProduceCategoryService;

@Service
public class ProduceCategoryRestfulService {
	@Autowired
	ProduceCategoryService produceCategoryService;
	
	public ResultData<Object> getRoot() {
		return ResultCode.success("操作成功", produceCategoryService.getRoot());
	}
	
	public ResultData<Object> getChildren(String parentId,String parentCode) {
		if(StringUtils.isBlank(parentId) && StringUtils.isBlank(parentCode)) return ResultCode.error("输入参数不能为空", null);
		return ResultCode.success("操作成功", produceCategoryService.getChildren(parentId, parentCode));
	}
}

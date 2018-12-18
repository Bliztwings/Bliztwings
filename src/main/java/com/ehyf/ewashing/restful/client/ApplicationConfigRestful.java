package com.ehyf.ewashing.restful.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ehyf.ewashing.common.Constants;
import com.ehyf.ewashing.service.DictionaryService;

/**
 * 应用相关配置接口
 * @author fqdeng
 *
 */
@Controller
@RequestMapping("/appConf")
public class ApplicationConfigRestful {
	@Autowired
	DictionaryService dictionaryService;
	
	@RequestMapping(method = RequestMethod.GET,consumes = "application/json", value = "/getPictureUrlPrefix")
	@ResponseBody
	public ResultData<Object> getPictureUrlPrefix() {
		return ResultCode.success("获取成功", Constants.PICTURE_URL_PREFIX);
	}
	
	@RequestMapping(method = RequestMethod.GET,consumes = "application/json", value = "/getCity")
	@ResponseBody
	public ResultData<Object> getCity() {
		return ResultCode.success("获取成功", dictionaryService.getDictionaryByCode("CITY"));
	}
	
	@RequestMapping(method = RequestMethod.GET,consumes = "application/json", value = "/getArea/{cityCode}")
	@ResponseBody
	public ResultData<Object> getArea(@PathVariable(value = "cityCode") String cityCode) {
		return ResultCode.success("获取成功", dictionaryService.getDictionaryByCode(cityCode));
	}
	
	
}

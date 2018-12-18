package com.ehyf.ewashing.restful.client;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ehyf.ewashing.common.Constants;
import com.ehyf.ewashing.entity.CheckCode;
import com.ehyf.ewashing.service.CheckCodeService;
import com.ehyf.ewashing.ucpass.UcpassEmsService;
import com.ehyf.ewashing.util.LocalThreadUtils;
import com.ehyf.ewashing.util.RandomUtil;

@Controller
@RequestMapping("/ck")
public class CheckCodeControllerRestful {
	Logger logger =Logger.getLogger(CheckCodeControllerRestful.class);
	@Autowired
	CheckCodeService checkCodeService;
	
	@Autowired
	private UcpassEmsService uspassService;
	
	@RequestMapping(method = RequestMethod.POST,consumes = "application/json", value = "/o2oRegisterCheckCode")
	@ResponseBody
	public ResultData<Object> getMessage4O2oRegister(@RequestBody CheckCode code) {
		if(null==code || StringUtils.isBlank(code.getTelephone()) ) return ResultCode.error("参数不能为空", null); 
		String appId = (String)LocalThreadUtils.get(LocalThreadUtils.CONSTRANT_APP_ID);
		if(null==appId || StringUtils.isBlank(appId) ) return ResultCode.error("appId不能为空", null);
		code.setAppId(appId);
		String checkCode = String.valueOf(RandomUtil.getRandNum(1,9999));
		code.setCode(checkCode);
		code.setType(Constants.MSG_TYPE_O2O_REGISTER);
		code.setStatus("0");
		code.setCreateTime(new Date());
		if(checkCodeService.insert(code)>0){
			logger.info("O2O用户注册，短信验证码发送，手机号："+code.getTelephone()+"，验证码："+code.getCode());
			uspassService.sendEms(code.getCode(), code.getTelephone(),code.getAppId());
		}
		return ResultCode.success("操作成功", checkCode);
	}
	
	@RequestMapping(method = RequestMethod.POST,consumes = "application/json", value = "/o2oRestPwdCk")
	@ResponseBody
	public ResultData<Object> getMessage4RestPwd(@RequestBody CheckCode code) {
		if(null==code || StringUtils.isBlank(code.getTelephone()) ) return ResultCode.error("参数不能为空", null); 
		String appId = (String)LocalThreadUtils.get(LocalThreadUtils.CONSTRANT_APP_ID);
		if(null==appId || StringUtils.isBlank(appId) ) return ResultCode.error("appId不能为空", null);
		code.setAppId(appId);
		String checkCode = String.valueOf(RandomUtil.getRandNum(1,9999));
		code.setCode(checkCode);
		code.setType(Constants.MSG_TYPE_RESET_PWD_O2O);
		code.setStatus("0");
		code.setCreateTime(new Date());
		if(checkCodeService.insert(code)>0){
			logger.info("O2O用户密码找回，短信验证码发送，手机号："+code.getTelephone()+"，验证码："+code.getCode());
			uspassService.sendEms(code.getCode(), code.getTelephone(),code.getAppId());
		}
		return ResultCode.success("操作成功", checkCode);
	}
	
}

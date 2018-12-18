package com.ehyf.ewashing.restful.client;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ehyf.ewashing.entity.Member;
import com.ehyf.ewashing.entity.MemberCard;
import com.ehyf.ewashing.restful.service.MemberRestfulService;
import com.ehyf.ewashing.service.MemberCardService;
import com.ehyf.ewashing.util.LocalThreadUtils;

@Controller
@RequestMapping("/o2o")
public class MemberRestful {
	@Autowired
	MemberRestfulService memberRestfulService;
	@Autowired
	MemberCardService memberCardService;
	
	@RequestMapping(method = RequestMethod.POST,consumes = "application/json", value = "/login")
	@ResponseBody
	public ResultData<Object> login(@RequestBody Member member) {
		ResultData<Object> ret = memberRestfulService.login(member);
		return ret;
	}
	@RequestMapping(method = RequestMethod.GET,consumes = "application/json", value = "/loginOut")
	@ResponseBody
	public ResultData<Object> loginOut(@RequestParam(value="memberId",required=false) String memberId) {
		return memberRestfulService.loginOut(memberId);
	}
	@RequestMapping(method = RequestMethod.POST,consumes = "application/json", value = "/onlineRegister")
	@ResponseBody
	public ResultData<Object> onlineRegister(@RequestBody Member member) {
		ResultData<Object> ret = memberRestfulService.onlineRegister(member);
		return ret;
	}
	@RequestMapping(method = RequestMethod.POST,consumes = "application/json", value = "/changePassword")
	@ResponseBody
	public ResultData<Object> changePassword(@RequestBody Member member) {
		ResultData<Object> ret = memberRestfulService.changePassword(member);
		return ret;
	}
	@RequestMapping(method = RequestMethod.POST,consumes = "application/json", value = "/resetPassword")
	@ResponseBody
	public ResultData<Object> resetPassword(@RequestBody Member member) {
		ResultData<Object> ret = memberRestfulService.resetPassword(member);
		return ret;
	}
	
	@RequestMapping(method = RequestMethod.POST,consumes = "application/json", value = "/onlineRecharge")
	@ResponseBody
	public ResultData<Object> onlineRecharge(@RequestBody MemberCard memberCard) {
		ResultData<Object> ret = memberRestfulService.onlineRecharge(memberCard);
		return ret;
	}
	@RequestMapping(method = RequestMethod.GET,consumes = "application/json", value = "/getMemberCardInfo")
	@ResponseBody
	public ResultData<Object> getMemberCardByKeywords(@RequestParam(value="key",required=false) String key) {
		if(StringUtils.isBlank(key)) return ResultCode.error("输入参数为空", null);
		MemberCard ret = memberCardService.queryMemberByCardOrMobile(key);
		return ret==null?ResultCode.success("查询结果为空", null):ResultCode.success("查询成功", ret);
	}
}

package com.ehyf.ewashing.restful.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ehyf.ewashing.entity.Member;
import com.ehyf.ewashing.entity.MemberReceiveAddress;
import com.ehyf.ewashing.restful.service.MemberReceiveAddressRestfulService;

@Controller
@RequestMapping("/o2o")
public class MemberReceiveAddressRestful {
	@Autowired
	MemberReceiveAddressRestfulService memberReceiveAddressRestfulService;
	
	@RequestMapping(method = RequestMethod.POST,consumes = "application/json", value = "/registerAddress")
	@ResponseBody
	public ResultData<Object> registerAddress(@RequestBody MemberReceiveAddress address) {
		ResultData<Object> ret = memberReceiveAddressRestfulService.registerAddress(address);
		return ret;
	}
	@RequestMapping(method = RequestMethod.POST,consumes = "application/json", value = "/modifyAddress")
	@ResponseBody
	public ResultData<Object> modifyAddress(@RequestBody MemberReceiveAddress address) {
		ResultData<Object> ret = memberReceiveAddressRestfulService.modifyAddress(address);
		return ret;
	}

	@RequestMapping(method = RequestMethod.POST,consumes = "application/json", value = "/delAddressById")
	@ResponseBody
	public ResultData<Object> delAddressById(@RequestBody MemberReceiveAddress address) {
		ResultData<Object> ret = memberReceiveAddressRestfulService.delAddressById(address.getId());
		return ret;
	}
	@RequestMapping(method = RequestMethod.POST,consumes = "application/json", value = "/getAddressByMemberId")
	@ResponseBody
	public ResultData<Object> getAddressByMemberId(@RequestBody Member member) {
		ResultData<Object> ret = memberReceiveAddressRestfulService.getAddressByMemberId(member.getId());
		return ret;
	}

}

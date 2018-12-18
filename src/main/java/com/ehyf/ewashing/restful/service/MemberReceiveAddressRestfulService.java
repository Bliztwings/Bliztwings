package com.ehyf.ewashing.restful.service;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ehyf.ewashing.entity.MemberReceiveAddress;
import com.ehyf.ewashing.restful.client.ResultCode;
import com.ehyf.ewashing.restful.client.ResultData;
import com.ehyf.ewashing.service.MemberReceiveAddressService;
import com.ehyf.ewashing.util.UUID;

@Service
public class MemberReceiveAddressRestfulService {
	Logger logger = Logger.getLogger(MemberReceiveAddressRestfulService.class);
	@Autowired
	MemberReceiveAddressService memberReceiveAddressService;
	
	
	public ResultData<Object> registerAddress(MemberReceiveAddress address){
		if(null==address || StringUtils.isBlank(address.getAreaCode()) || StringUtils.isBlank(address.getAreaName())
				|| StringUtils.isBlank(address.getCustomerName()) || StringUtils.isBlank(address.getMemberId()) || 
				StringUtils.isBlank(address.getTelephone()) || StringUtils.isBlank(address.getHomeAddress()) || StringUtils.isBlank(address.getDoorNumber())){
			return ResultCode.error("参数不能为空", null);
		}
		address.setId(UUID.getUUID32());
		address.setCreateTime(new Date());
//		address.setCreateUser(createUser);// FIXME ，当前登录用户
		return memberReceiveAddressService.insert(address)>0?ResultCode.success("操作成功", null):ResultCode.error("操作失败", null);
	}
	
	public ResultData<Object> modifyAddress(MemberReceiveAddress address){
		if(null==address || StringUtils.isBlank(address.getId())){
			return ResultCode.error("参数不能为空", null);
		}
		return memberReceiveAddressService.update(address)>0?ResultCode.success("操作成功", null):ResultCode.error("操作失败", null);
	}
	
	public ResultData<Object> delAddressById(String id){
		if(StringUtils.isBlank(id)){
			return ResultCode.error("参数不能为空", null);
		}
		return memberReceiveAddressService.deleteById(id)>0?ResultCode.success("操作成功", null):ResultCode.error("操作失败", null);
	}
	
	public ResultData<Object> getAddressByMemberId(String memberId){
		if(StringUtils.isBlank(memberId)){
			return ResultCode.error("参数不能为空", null);
		}
		return ResultCode.success("操作成功", memberReceiveAddressService.getAddressByMemberId(memberId));
	}
	
}

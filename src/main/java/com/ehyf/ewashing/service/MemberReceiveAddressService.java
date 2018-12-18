package com.ehyf.ewashing.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ehyf.ewashing.dao.MemberReceiveAddressDao;
import com.ehyf.ewashing.entity.MemberReceiveAddress;

/**
 * 
 * MemberReceiveAddressService  Service服务接口类
 * 
 **/
@Service
public class MemberReceiveAddressService extends BaseService<MemberReceiveAddressDao,MemberReceiveAddress> {
	
	public List<MemberReceiveAddress> getAddressByMemberId(String memberId){
		MemberReceiveAddress m = new MemberReceiveAddress();
		m.setMemberId(memberId);
		return findList(m);
	}
}
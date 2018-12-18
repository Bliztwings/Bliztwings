package com.ehyf.ewashing.service;

import com.ehyf.ewashing.entity.MemberCardRelation;
import com.ehyf.ewashing.dao.MemberCardRelationDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * MemberCardRelationService  Service服务接口类
 * 
 **/
@Service
public class MemberCardRelationService extends BaseService<MemberCardRelationDao,MemberCardRelation> {
	@Autowired
	MemberCardRelationDao memberCardRelationDao;
	public MemberCardRelation getByMemberId(String memberId){
		return memberCardRelationDao.getByMemberId(memberId);
	}

}
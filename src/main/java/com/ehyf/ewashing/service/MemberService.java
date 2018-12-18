package com.ehyf.ewashing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ehyf.ewashing.dao.MemberDao;
import com.ehyf.ewashing.entity.Member;

/**
 * 
 * MemberService  Service服务接口类
 * 
 **/
@Service
public class MemberService extends BaseService<MemberDao,Member> {
	@Autowired
	private MemberDao memberDao;
	
	public Member getMemberByCardId(String id){
		return memberDao.getMemberByCardId(id);
	}
	
	public Member getByMbileAndPwd(Member m){
		return memberDao.getByMbileAndPwd(m);
	}
	
	public Member getByToken(String token){
		return memberDao.getByToken(token);
	}
	
	
	@Transactional(readOnly = false)
	public int changePassword(Member member){
		return memberDao.changePassword(member);
	}
	
	@Transactional(readOnly = false)
	public int resetPassword(Member member){
		return memberDao.resetPassword(member);
	}
}
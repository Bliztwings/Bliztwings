package com.ehyf.ewashing.dao;

import com.ehyf.ewashing.entity.Member;
import org.springframework.stereotype.Repository;

/**
 * 
 * MemberDao  数据库操作接口类
 * 
 **/
@Repository
public interface MemberDao extends BaseDao<Member> {
	public Member getMemberByCardId(String id);
	
	public Member getByMbileAndPwd(Member m);
	
	public int changePassword(Member member);
	
	public Member getByToken(String token);
	
	public int resetPassword(Member member);
	
}
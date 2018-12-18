package com.ehyf.ewashing.dao;

import com.ehyf.ewashing.entity.MemberCardRelation;
import org.springframework.stereotype.Repository;

/**
 * 
 * MemberCardRelationDao  数据库操作接口类
 * 
 **/
@Repository
public interface MemberCardRelationDao extends BaseDao<MemberCardRelation> {
	public MemberCardRelation getByMemberId(String memberNo);
}
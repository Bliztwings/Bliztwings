package com.ehyf.ewashing.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ehyf.ewashing.entity.CardApplyRecord;

/**
 * 
 * CardApplyRecordDao  数据库操作接口类
 * 
 **/
@Repository
public interface CardApplyRecordDao extends BaseDao<CardApplyRecord> {
	public int validateCardNumber(List<String> cards);
	
	public int doApproved(CardApplyRecord record);
}
package com.ehyf.ewashing.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ehyf.ewashing.entity.SecurityOperLog;

/**
 * 
 * SecurityOperLogDao  数据库操作接口类
 * 
 **/
@Repository
public interface SecurityOperLogDao extends BaseDao<SecurityOperLog> {
	int batchDeleteByIds(List<String> ids);
}
package com.ehyf.ewashing.dao;

import com.ehyf.ewashing.entity.CheckCode;
import org.springframework.stereotype.Repository;

/**
 * 
 * CheckCodeDao  数据库操作接口类
 * 
 **/
@Repository
public interface CheckCodeDao extends BaseDao<CheckCode> {
	
	public int invalidCode(CheckCode code);//验证码失效

}
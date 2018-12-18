package com.ehyf.ewashing.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ehyf.ewashing.dao.SecurityOperLogDao;
import com.ehyf.ewashing.entity.SecurityOperLog;

/**
 * 
 * SecurityOperLogService  Service服务接口类
 * 
 **/
@Service
@Transactional
public class SecurityOperLogService extends BaseService<SecurityOperLogDao,SecurityOperLog> {
	@Autowired
	private SecurityOperLogDao securityOperLogDao;
	
	public int batchDeleteByIds(List<String> ids){
		return securityOperLogDao.batchDeleteByIds(ids);
	}
}
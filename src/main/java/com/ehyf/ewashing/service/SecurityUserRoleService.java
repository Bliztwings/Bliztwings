package com.ehyf.ewashing.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ehyf.ewashing.dao.SecurityUserRoleDao;
import com.ehyf.ewashing.entity.SecurityUserRole;
import com.ehyf.ewashing.vo.SecurityUserRoleVo;

/**
 * 
 * SecurityUserRoleService  Service服务接口类
 * 
 **/
@Service
public class SecurityUserRoleService extends BaseService<SecurityUserRoleDao,SecurityUserRole> {

	
	@Autowired
	private SecurityUserRoleDao userRole;
	
	@Transactional(readOnly=false)
	public void deleteByUserId(SecurityUserRole entity) {
		userRole.deleteByUserId(entity.getUserId());
	}

	public List<SecurityUserRoleVo> findUserRoleList(SecurityUserRole userRole2) {
		return userRole.findUserRoleList(userRole2);
	}

}
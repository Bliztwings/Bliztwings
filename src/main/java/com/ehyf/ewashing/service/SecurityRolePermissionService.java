package com.ehyf.ewashing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ehyf.ewashing.dao.SecurityRolePermissionDao;
import com.ehyf.ewashing.entity.SecurityRolePermission;

/**
 * 
 * SecurityRolePermissionService  Service服务接口类
 * 
 **/
@Service
public class SecurityRolePermissionService extends BaseService<SecurityRolePermissionDao,SecurityRolePermission> {

	@Autowired
	private SecurityRolePermissionDao rolePermission;
	
	@Transactional(readOnly=false)
	public void deleteByRoleId(SecurityRolePermission entity) {
		rolePermission.deleteByRoleId(entity.getRoleId());
	}

}
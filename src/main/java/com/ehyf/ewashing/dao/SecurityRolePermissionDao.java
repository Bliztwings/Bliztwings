package com.ehyf.ewashing.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ehyf.ewashing.entity.SecurityRolePermission;

/**
 * 
 * SecurityRolePermissionDao  数据库操作接口类
 * 
 **/
@Repository
public interface SecurityRolePermissionDao extends BaseDao<SecurityRolePermission> {

	void deleteByRoleId(String roleId);

	List<SecurityRolePermission> getSecurityRolePermissionByRoles(List<String> roleIdList);

}
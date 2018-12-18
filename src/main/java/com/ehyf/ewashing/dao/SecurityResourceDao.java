package com.ehyf.ewashing.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ehyf.ewashing.entity.SecurityResource;

/**
 * 
 * SecurityResourceDao  数据库操作接口类
 * 
 **/
@Repository
public interface SecurityResourceDao extends BaseDao<SecurityResource> {

	/***
	 * 根据permissionIds 获取 SecurityResource
	 * @param permissionIds
	 * @return
	 */
	List<SecurityResource> getSecurityResourceByPermissionIds(List<String> permissionIds);
}
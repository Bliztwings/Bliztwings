package com.ehyf.ewashing.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ehyf.ewashing.entity.SecurityUserRole;
import com.ehyf.ewashing.vo.SecurityUserRoleVo;

/**
 * 
 * SecurityUserRoleDao  数据库操作接口类
 * 
 **/
@Repository
public interface SecurityUserRoleDao extends BaseDao<SecurityUserRole> {

	int deleteByUserId(String userId);

	List<SecurityUserRoleVo> findUserRoleList(SecurityUserRole userRole2);

}
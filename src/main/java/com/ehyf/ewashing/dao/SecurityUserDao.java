package com.ehyf.ewashing.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ehyf.ewashing.entity.SecurityRole;
import com.ehyf.ewashing.entity.SecurityUser;

/**
 * 
 * SecurityUserDao  数据库操作接口类
 * 
 **/
@Repository
public interface SecurityUserDao extends BaseDao<SecurityUser> {
	SecurityUser findUserByLoginName(String username);
	
	/**
	 * 获取当前用户所拥有的角色
	 * @param userId
	 * @return
	 */
	List<SecurityRole> getUserRoleList(String userId);

	/**
	 * 获取所有用户
	 * @return
	 */
	List<SecurityUser> findAllList();

	/**
	 * 批量查询用户
	 * @param userIds
	 * @return
	 */
	List<SecurityUser> findUserByUserIds(List<String> userIds);
}
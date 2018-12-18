package com.ehyf.ewashing.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ehyf.ewashing.dao.SecurityResourceDao;
import com.ehyf.ewashing.dao.SecurityRolePermissionDao;
import com.ehyf.ewashing.dao.SecurityUserDao;
import com.ehyf.ewashing.dao.SecurityUserRoleDao;
import com.ehyf.ewashing.entity.EwashingStore;
import com.ehyf.ewashing.entity.SecurityResource;
import com.ehyf.ewashing.entity.SecurityRole;
import com.ehyf.ewashing.entity.SecurityRolePermission;
import com.ehyf.ewashing.entity.SecurityUser;
import com.ehyf.ewashing.entity.SecurityUserRole;
import com.ehyf.ewashing.exection.AppExection;
import com.ehyf.ewashing.shiro.credentials.PasswordHelper;

/**
 * 
 * SecurityUserService  Service服务接口类
 * 
 **/
@Service
public class SecurityUserService extends BaseService<SecurityUserDao,SecurityUser> {
	@Autowired
	private SecurityUserDao securityUserDao;
	
	@Autowired
	private SecurityResourceDao securityResourceDao;
	
	@Autowired
	private SecurityRolePermissionDao securityRolePermissionDao;
	
	@Autowired
	private SecurityUserRoleDao userRole;
	
	@Autowired
	private PasswordHelper passwordHelper;
	
	public SecurityUser findUserByLoginName(String username){
		return securityUserDao.findUserByLoginName(username);
	}
	
	public List<SecurityRole> getUserRoleList(String userId){
		return securityUserDao.getUserRoleList(userId);
	}
	
	public Set<String> getUserRoleNames(String userId){
		Set<String> roleNames = new HashSet<String>();
		List<SecurityRole> roleList = getUserRoleList(userId);
		if(roleList!=null && roleList.size()>0){
			for(SecurityRole role : roleList){
				roleNames.add(role.getName());
			}
		}
		return roleNames;
	}
	
	public Set<String> getPermissions(String userId) throws Exception {
		Set<String> permissions = new HashSet<String>();
		
		List<String> permissionIds = new ArrayList<String>();
		List<String> roleIdList = new ArrayList<String>();
		List<SecurityRole> roleList = getUserRoleList(userId);
		
		for (SecurityRole securityRole : roleList) {
			roleIdList.add(securityRole.getId().toString());
		}

		// 获取permission_id
		if(roleIdList.size()>0){
			List<SecurityRolePermission> resourceList = securityRolePermissionDao.getSecurityRolePermissionByRoles(roleIdList);
			for (SecurityRolePermission securityResource : resourceList) {
				permissionIds.add(securityResource.getPermissionId());
			}
		}

		// 获取 Permission
		if(permissionIds.size()>0){
			List<SecurityResource> resourceList = securityResourceDao.getSecurityResourceByPermissionIds(permissionIds);
			for (SecurityResource securityResource : resourceList) {
				permissions.add(securityResource.getPermission());
			}
		}
		return permissions;
	}
	
	public boolean validateUsernameAndPwd(String username, String rawPwd) {
		SecurityUser entity = new SecurityUser();
		entity.setUsername(username);
		List<SecurityUser> list = securityUserDao.findList(entity);

		if (list!=null && list.size()>0) {
			SecurityUser user = list.get(0);
			//计算密文
			String pwdS = passwordHelper.getEncryptPassword(user,rawPwd);
			if (user.getPassword().equals(pwdS)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public List<EwashingStore> findList(EwashingStore entity) {
		return null;
	}

	public List<SecurityUser> findAllList() {
		return securityUserDao.findAllList();
	}

	@Transactional(readOnly=false)   
	public void deleteUserAndRoleById(String id) throws Exception{
		// 删除用户
		int count =securityUserDao.deleteById(id);
		if(count<=0){
			throw new AppExection("删除用户失败");
		}
		//删除用户对应的角色
		int roleCount =userRole.deleteByUserId(id);
		if(roleCount<0){
			throw new AppExection("删除用户对应角色失败");
		}
	}

}
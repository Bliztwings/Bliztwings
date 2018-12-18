package com.ehyf.ewashing.common;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ehyf.ewashing.entity.SecurityResource;
import com.ehyf.ewashing.entity.SecurityUser;
import com.ehyf.ewashing.service.SecurityResourceService;
import com.ehyf.ewashing.service.SecurityUserService;

/**
 * 初始化系统权限代码
 * @author
 *
 */
@Service
public class PermissionInit {
	@Autowired
	private SecurityResourceService resourceService;
	@Autowired
	private SecurityUserService userService;
	
	public static Set<String> permissionSet = null;
	
	public static HashMap<String,SimpleAuthorizationInfo> currUserAuthorizationMap = null;

	private static Log log = LogFactory.getLog(PermissionInit.class);
	
	public void init(){
		try {
			if(permissionSet==null){
				permissionSet = new HashSet<String>();
				List<SecurityResource> resourceList = resourceService.findList(new SecurityResource());
				for(SecurityResource resource : resourceList){
					permissionSet.add(resource.getPermission());
				}
			}
		} catch (Exception e) {
			log.error("初始化权限信息失败");
		}
	}
	
	public void refresh(){
		permissionSet = null;
		init();
	}
	
	public void currUserAuthorizationInfo(){
		if(currUserAuthorizationMap == null){
			currUserAuthorizationMap = new HashMap<String,SimpleAuthorizationInfo>();
		}
		String username = (String)SecurityUtils.getSubject().getPrincipal();
		
		currUserAuthorizationMap.put(username, getAuthorizationInfo(username));
	}
	
	private SimpleAuthorizationInfo getAuthorizationInfo(String userName) {

		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		SecurityUser user;
		try {
			user = userService.findUserByLoginName(userName);
			if (null != user) {
				authorizationInfo.setRoles(userService.getUserRoleNames(user.getId()));
				authorizationInfo.setStringPermissions(userService.getPermissions(user.getId()));
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return authorizationInfo;
	}
}

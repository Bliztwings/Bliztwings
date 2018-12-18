package com.ehyf.ewashing.shiro.realm;


import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.ehyf.ewashing.common.PermissionInit;
import com.ehyf.ewashing.entity.SecurityUser;
import com.ehyf.ewashing.service.SecurityUserService;

/**
 * 认证、鉴权
 * @author Dell
 *
 */
public class UserRealm extends AuthorizingRealm {

	Log log = LogFactory.getLog(UserRealm.class);
	
	@Autowired
	private SecurityUserService userService;
	
	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		//log.info("授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用");
		String userName = (String) principals.getPrimaryPrincipal();
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		
		try {
			if (null != userName) {
				//如果是系统管理员，就赋所有角色和权限
				if(userName.equals("admin")){
					Set<String> roleSet = new HashSet<String>();
					roleSet.add("系统管理员");
					authorizationInfo.setRoles(roleSet);
					authorizationInfo.setStringPermissions(PermissionInit.permissionSet);
				}
				else{
					if(PermissionInit.currUserAuthorizationMap != null){
						authorizationInfo = PermissionInit.currUserAuthorizationMap.get(userName);
					}
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return authorizationInfo;
	}

	/**
	 * 认证回调函数, 登录时调用.
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		//log.info("认证回调函数, 登录时调用");
		String username = (String) token.getPrincipal();
		SecurityUser user = null;
		
		try {
			user = userService.findUserByLoginName(username);
			if (user == null) {
				throw new UnknownAccountException();// 没找到帐号
			}
			if (!(user.getStatus().equals("enabled"))) {
				throw new LockedAccountException(); // 帐号锁定
			}
			// 交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，如果觉得人家的不好可以自定义实现
			SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
					user.getUsername(), // 用户名
					user.getPassword(), // 密码
					ByteSource.Util.bytes(user.getCredentialsSalt()),// salt=username+salt
					getName() // realm name
			);
			return authenticationInfo;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
		super.clearCachedAuthorizationInfo(principals);
	}

	@Override
	public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
		super.clearCachedAuthenticationInfo(principals);
	}

	@Override
	public void clearCache(PrincipalCollection principals) {
		super.clearCache(principals);
	}

	public void clearAllCachedAuthorizationInfo() {
		getAuthorizationCache().clear();
	}

	public void clearAllCachedAuthenticationInfo() {
		getAuthenticationCache().clear();
	}

	public void clearAllCache() {
		clearAllCachedAuthenticationInfo();
		clearAllCachedAuthorizationInfo();
	}

}

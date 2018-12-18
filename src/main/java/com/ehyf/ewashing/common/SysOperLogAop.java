package com.ehyf.ewashing.common;

import java.util.Arrays;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.ehyf.ewashing.entity.SecurityOperLog;
import com.ehyf.ewashing.entity.SecurityOrganization;
import com.ehyf.ewashing.entity.SecurityResource;
import com.ehyf.ewashing.entity.SecurityRole;
import com.ehyf.ewashing.entity.SecurityUser;
import com.ehyf.ewashing.service.SecurityOperLogService;
import com.ehyf.ewashing.service.SecurityOrganizationService;
import com.ehyf.ewashing.service.SecurityResourceService;
import com.ehyf.ewashing.service.SecurityRoleService;
import com.ehyf.ewashing.service.SecurityUserService;
import com.ehyf.ewashing.util.IpUtil;
import com.ehyf.ewashing.util.UUID;

/**
 * 系统模块操作日志AOP
 * 
 * @author  huangfenglin
 * @version  [版本号, 2017年04月11日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
@Component
@Service
@Aspect
public class SysOperLogAop {

	Log log = LogFactory.getLog(SysOperLogAop.class);
	
	@Autowired
	private SecurityOperLogService securityOperLogService;
	@Autowired
	private  SecurityUserService userService;
	@Autowired
	private SecurityUserService securityUserService;
	@Autowired
	private SecurityRoleService securityRoleService;
	@Autowired
	private SecurityResourceService securityResourceService;
	@Autowired
	private SecurityOrganizationService  organizationService;

	
	/**
	 * 获取系统当前登录用户
	 * @return
	 * @throws Exception
	 */
	private SecurityUser getUser() throws Exception{
		String username = (String)SecurityUtils.getSubject().getPrincipal();
	    SecurityUser user = userService.findUserByLoginName(username);
	    return user;
	}
	
	/**
	 * 操作日志
	 * @param point
	 */
	@After(value = " execution(* com.ehyf.ewashing.controller.OperLogController.delete(..))")
	public void operLogAfter(JoinPoint point){
		SecurityOperLog securityOperLog = new SecurityOperLog();
		securityOperLog.setOperTime(new Date());
		securityOperLog.setId(UUID.getUUID32());
		try {
			boolean isSuccess = true ;
			String pointStr = Arrays.toString(point.getArgs());
			if(pointStr.contains("resultCode=0")){
				isSuccess = false;
			}
			if(!isSuccess){
				return;
			}
			
			SecurityUser user = getUser();
			
			HttpServletRequest request = (HttpServletRequest)point.getArgs()[0];
			securityOperLog.setUserName(user.getUsername());
			securityOperLog.setUserRealName(user.getRealname());
			securityOperLog.setIp(IpUtil.getIpAddress(request));
			
			securityOperLog.setOperObject("操作日志");
			if("delete".equals(point.getSignature().getName())){
				securityOperLog.setOperAction("删除");
				String ids = request.getParameter("ids").trim();
				String [] arr = ids.split(",");
				securityOperLog.setOperDesc("删除了  "+arr.length+" 条操作日志记录");
			}
			
			securityOperLogService.insert(securityOperLog);

		} catch (Exception e) {
			e.printStackTrace();
			log.error("记录操作日志出现异常......",e);
		}
		
	}
	
	/**
	 * 系统登录或踢用户
	 * @param point
	 */
	@After(value = " execution(* com.ehyf.ewashing.controller.IndexContrller.index(..))"
			+ "|| execution(* com.ehyf.ewashing.controller.IndexContrller.toLogin(..))"
			+ "|| execution(* com.ehyf.ewashing.controller.SessionController.kickOff(..))"
			)
	public void sysLoginAfter(JoinPoint point){
		SecurityOperLog securityOperLog = new SecurityOperLog();
		securityOperLog.setOperTime(new Date());
		securityOperLog.setId(UUID.getUUID32());
		try {
			boolean isSuccess = true;

			if("index".equals(point.getSignature().getName()) || "toLogin".equals(point.getSignature().getName())){
				SecurityUser user = getUser();
				HttpServletRequest request = (HttpServletRequest)point.getArgs()[0];
				
				securityOperLog.setUserName(user.getUsername());
				securityOperLog.setUserRealName(user.getRealname());
				securityOperLog.setIp(IpUtil.getIpAddress(request));
				
				securityOperLog.setOperObject("登录系统");
				securityOperLog.setOperAction("登录系统");
				securityOperLog.setOperDesc("用户  "+user.getUsername()+" 登录了系统");
		

			}else if("kickOff".equals(point.getSignature().getName())){
				
				String pointStr = Arrays.toString(point.getArgs());
				if(pointStr.contains("resultCode=0")){
					isSuccess = false;
				}
				if(!isSuccess){
					return;
				}
				
				SecurityUser user = getUser();
				HttpServletRequest request = (HttpServletRequest)point.getArgs()[0];
				securityOperLog.setUserName(user.getUsername());
				securityOperLog.setUserRealName(user.getRealname());
				securityOperLog.setIp(IpUtil.getIpAddress(request));
				
				securityOperLog.setOperObject("退出系统");
				securityOperLog.setOperAction("踢用户");
				String username = request.getParameter("username");
				securityOperLog.setOperDesc("用户  "+username+" 被踢出");
			}
			securityOperLogService.insert(securityOperLog);

		} catch (Exception e) {
			e.printStackTrace();
			log.error("记录系统登录或退出系统操作日志出现异常......",e);
		}
		
	}
	
	
	/**
	 * 用户管理
	 * @param point
	 */
	@After(value = " execution(* com.ehyf.ewashing.controller.SecurityUserController.create(..))"
			+ "|| execution(* com.ehyf.ewashing.controller.SecurityUserController.update(..))"
			+ "|| execution(* com.ehyf.ewashing.controller.SecurityUserController.setRole(..))"
			+ "|| execution(* com.ehyf.ewashing.controller.SecurityUserController.pwdChange(..))"
			+ "|| execution(* com.ehyf.ewashing.controller.SecurityUserController.stopOrStartUser(..))"
			)
	public void sysUserAfter(JoinPoint point){
		SecurityOperLog securityOperLog = new SecurityOperLog();
		securityOperLog.setOperTime(new Date());
		securityOperLog.setId(UUID.getUUID32());
		try {
			
			boolean isSuccess = true ;
			String pointStr = Arrays.toString(point.getArgs());
			if(pointStr.contains("resultCode=0")){
				isSuccess = false;
			}
			if(!isSuccess){
				return;
			}
			
			HttpServletRequest request = (HttpServletRequest)point.getArgs()[0];
			SecurityUser user = getUser();
			securityOperLog.setUserName(user.getUsername());
			securityOperLog.setUserRealName(user.getRealname());
			securityOperLog.setIp(IpUtil.getIpAddress(request));
			
			securityOperLog.setOperObject("用户管理");
			
			if("create".equals(point.getSignature().getName())){
				securityOperLog.setOperAction("新增");

				SecurityUser securityUser = (SecurityUser)point.getArgs()[1];
				securityOperLog.setOperDesc("新增了  "+securityUser.getUsername()+"|"+securityUser.getRealname()+" 用户");
		

			}else if("update".equals(point.getSignature().getName())){
				securityOperLog.setOperAction("修改");

				SecurityUser securityUser = (SecurityUser)point.getArgs()[1];
				securityOperLog.setOperDesc("对用户  "+securityUser.getUsername()+" 进行了修改操作");

			}else if("setRole".equals(point.getSignature().getName())){
				securityOperLog.setOperAction("角色设置");

				String usercid = request.getParameter("usercid");
				SecurityUser securityUser = securityUserService.getById(usercid);
				securityOperLog.setOperDesc("对用户  "+securityUser.getUsername()+" 进行了分配用户角色操作");

			}else if("pwdChange".equals(point.getSignature().getName())){
	
				String type = request.getParameter("type")==null? "info":request.getParameter("type");
				if(type.equals("info")){
					securityOperLog.setOperObject("个人信息维护");
					SecurityUser loginUser = (SecurityUser) request.getAttribute(Constants.CURRENT_USER);
					securityOperLog.setOperAction("个人信息");
					securityOperLog.setOperDesc("用户  "+loginUser.getUsername()+" 进行了个人信息修改");
				}else{
					securityOperLog.setOperObject("密码修改");
					securityOperLog.setOperAction("密码修改");
					SecurityUser loginUser = (SecurityUser) request.getAttribute(Constants.CURRENT_USER);
					securityOperLog.setOperDesc("用户  "+loginUser.getUsername()+" 进行了密码修改操作");
				}
				
			}else if("stopOrStartUser".equals(point.getSignature().getName())){
	
				String status = request.getParameter("status");
				String id = request.getParameter("id");
				SecurityUser loginUser = securityUserService.getById(id);
				if("1".equals(status)){
					securityOperLog.setOperAction("启用用户");
					securityOperLog.setOperDesc("用户  "+loginUser.getUsername()+" 启用成功");
				}else{
					securityOperLog.setOperAction("禁用用户");
					securityOperLog.setOperDesc("用户  "+loginUser.getUsername()+" 禁用成功");
				}

			}
			
			securityOperLogService.insert(securityOperLog);

		} catch (Exception e) {
			e.printStackTrace();
			log.error("记录用户管理操作日志出现异常......",e);
		}
		
	}
	
	
	/**
	 * 用户管理
	 * @param point
	 */
	@Before(value =  " execution(* com.ehyf.ewashing.controller.SecurityUserController.remove(..))"
			)
	public void sysUserBefore(JoinPoint point){
		SecurityOperLog securityOperLog = new SecurityOperLog();
		securityOperLog.setOperTime(new Date());
		securityOperLog.setId(UUID.getUUID32());
		try {
			boolean isSuccess = true ;
			String pointStr = Arrays.toString(point.getArgs());
			if(pointStr.contains("resultCode=0")){
				isSuccess = false;
			}
			if(!isSuccess){
				return;
			}
			
			HttpServletRequest request = (HttpServletRequest)point.getArgs()[0];
			SecurityUser user = getUser();
			securityOperLog.setUserName(user.getUsername());
			securityOperLog.setUserRealName(user.getRealname());
			securityOperLog.setIp(IpUtil.getIpAddress(request));
			
			securityOperLog.setOperObject("用户管理");
			
		    if("remove".equals(point.getSignature().getName())){
				securityOperLog.setOperAction("删除");
				
				String id = request.getParameter("id");
				SecurityUser securityUser = securityUserService.getById(id);
				securityOperLog.setOperDesc("删除了  "+securityUser.getUsername()+" 用户");

			}
			
			securityOperLogService.insert(securityOperLog);

		} catch (Exception e) {
			e.printStackTrace();
			log.error("记录用户管理操作日志出现异常......",e);
		}
		
	}
	
	/**
	 * 角色管理
	 * @param point
	 */
	@After(value = " execution(* com.ehyf.ewashing.controller.SecurityRoleController.create(..))"
			+ "|| execution(* com.ehyf.ewashing.controller.SecurityRoleController.update(..))"
			+ "|| execution(* com.ehyf.ewashing.controller.SecurityRoleController.savePermission(..))"
			)
	public void sysUserRoleAfter(JoinPoint point){
		SecurityOperLog securityOperLog = new SecurityOperLog();
		securityOperLog.setOperTime(new Date());
		securityOperLog.setId(UUID.getUUID32());
		
		try {
			
			boolean isSuccess = true ;
			String pointStr = Arrays.toString(point.getArgs());
			if(pointStr.contains("resultCode=0")){
				isSuccess = false;
			}
			if(!isSuccess){
				return;
			}
			
			HttpServletRequest request = (HttpServletRequest)point.getArgs()[0];
			SecurityUser user = getUser();
			securityOperLog.setUserName(user.getUsername());
			securityOperLog.setUserRealName(user.getRealname());
			securityOperLog.setIp(IpUtil.getIpAddress(request));
			
			securityOperLog.setOperObject("角色管理");
			
			if("create".equals(point.getSignature().getName())){
				securityOperLog.setOperAction("新增");

				SecurityRole securityRole = (SecurityRole)point.getArgs()[1];
				securityOperLog.setOperDesc("新增了  "+securityRole.getName()+" 角色");
		

			}else if("update".equals(point.getSignature().getName())){
				securityOperLog.setOperAction("修改");

				SecurityRole securityRole = (SecurityRole)point.getArgs()[1];
				securityOperLog.setOperDesc("对角色  "+securityRole.getName()+" 进行了修改操作");

			}else if("savePermission".equals(point.getSignature().getName())){
				securityOperLog.setOperAction("权限设置");

				String roleId = request.getParameter("roleId");
				SecurityRole securityRole = securityRoleService.getById(roleId);
				securityOperLog.setOperDesc("对角色  "+securityRole.getName()+" 进行了角色权限设置功能操作");

			}
			
			securityOperLogService.insert(securityOperLog);

		} catch (Exception e) {
			e.printStackTrace();
			log.error("记录角色管理操作日志出现异常......",e);
		}
		
	}
	
	/**
	 * 角色管理
	 * @param point
	 */
	@Before(value =  " execution(* com.ehyf.ewashing.controller.SecurityRoleController.remove(..))"
			)
	public void sysUserRoleBefore(JoinPoint point){
		SecurityOperLog securityOperLog = new SecurityOperLog();
		securityOperLog.setOperTime(new Date());
		securityOperLog.setId(UUID.getUUID32());
		try {
			
			boolean isSuccess = true ;
			String pointStr = Arrays.toString(point.getArgs());
			if(pointStr.contains("resultCode=0")){
				isSuccess = false;
			}
			if(!isSuccess){
				return;
			}
			
			HttpServletRequest request = (HttpServletRequest)point.getArgs()[0];
			SecurityUser user = getUser();
			securityOperLog.setUserName(user.getUsername());
			securityOperLog.setUserRealName(user.getRealname());
			securityOperLog.setIp(IpUtil.getIpAddress(request));
			
			securityOperLog.setOperObject("角色管理");
			
		    if("remove".equals(point.getSignature().getName())){
				securityOperLog.setOperAction("删除");
				
				String id = request.getParameter("id");
				SecurityRole securityRole = securityRoleService.getById(id);
				securityOperLog.setOperDesc("删除了  "+securityRole.getName()+" 角色");

			}
			
			securityOperLogService.insert(securityOperLog);

		} catch (Exception e) {
			e.printStackTrace();
			log.error("记录角色管理操作日志出现异常......",e);
		}
		
	}
	
	/**
	 * 资源管理
	 * @param point
	 */
	@After(value = " execution(* com.ehyf.ewashing.controller.ResourceController.createMenuOrButton(..))"
			+ "|| execution(* com.ehyf.ewashing.controller.ResourceController.editMenuOrButton(..))"
			)
	public void sysResourceAfter(JoinPoint point){
		SecurityOperLog securityOperLog = new SecurityOperLog();
		securityOperLog.setOperTime(new Date());
		securityOperLog.setId(UUID.getUUID32());
		try {
			
			boolean isSuccess = true ;
			String pointStr = Arrays.toString(point.getArgs());
			if(pointStr.contains("resultCode=0")){
				isSuccess = false;
			}
			if(!isSuccess){
				return;
			}
			
			HttpServletRequest request = (HttpServletRequest)point.getArgs()[0];
			SecurityUser user = getUser();
			securityOperLog.setUserName(user.getUsername());
			securityOperLog.setUserRealName(user.getRealname());
			securityOperLog.setIp(IpUtil.getIpAddress(request));
			
			securityOperLog.setOperObject("资源管理");
			
			if("createMenuOrButton".equals(point.getSignature().getName())){
				securityOperLog.setOperAction("新增");
				String parentId = request.getParameter("parentId");
				String level = request.getParameter("level");
				SecurityResource securityResource = (SecurityResource)point.getArgs()[2];
				SecurityResource  parentSecurityResource  =securityResourceService.getById(parentId);
				if(level.equals("2")){
					securityOperLog.setOperDesc("对菜单  "+parentSecurityResource.getName()+" 新增了按钮名称  "+securityResource.getName());
				}
				else{
					securityOperLog.setOperDesc("新增了菜单  "+securityResource.getName());
				}

			}else if("editMenuOrButton".equals(point.getSignature().getName())){
				securityOperLog.setOperAction("修改");

				SecurityResource securityResource = (SecurityResource)point.getArgs()[2];
				securityOperLog.setOperDesc("对菜单  "+securityResource.getName()+" 进行了修改操作");

			}
			
			securityOperLogService.insert(securityOperLog);

		} catch (Exception e) {
			e.printStackTrace();
			log.error("记录资源管理操作日志出现异常......",e);
		}
		
	}
	
	/**
	 * 资源管理
	 * @param point
	 */
	@Before(value = " execution(* com.ehyf.ewashing.controller.ResourceController.removeMenuOrButton(..))")
	public void sysResourceBefore(JoinPoint point){
		SecurityOperLog securityOperLog = new SecurityOperLog();
		securityOperLog.setOperTime(new Date());
		securityOperLog.setId(UUID.getUUID32());
		
		try {
			
			boolean isSuccess = true ;
			String pointStr = Arrays.toString(point.getArgs());
			if(pointStr.contains("resultCode=0")){
				isSuccess = false;
			}
			if(!isSuccess){
				return;
			}
			
			HttpServletRequest request = (HttpServletRequest)point.getArgs()[0];
			SecurityUser user = getUser();
			securityOperLog.setUserName(user.getUsername());
			securityOperLog.setUserRealName(user.getRealname());
			securityOperLog.setIp(IpUtil.getIpAddress(request));
			
			securityOperLog.setOperObject("资源管理");
			
		    if("removeMenuOrButton".equals(point.getSignature().getName())){
				securityOperLog.setOperAction("删除");
				
				String id = request.getParameter("id");
				SecurityResource  securityResource  =securityResourceService.getById(id);
				securityOperLog.setOperDesc("删除了  "+securityResource.getName()+" ");

			}
			
			securityOperLogService.insert(securityOperLog);

		} catch (Exception e) {
			e.printStackTrace();
			log.error("记录资源管理操作日志出现异常......",e);
		}
		
	}
	
	/**
	 * 机构组织管理
	 * @param point
	 */
	@After(value = " execution(* com.ehyf.ewashing.controller.OrganizationController.create(..))"
			+ "|| execution(* com.ehyf.ewashing.controller.OrganizationController.update(..))"
			+ "|| execution(* com.ehyf.ewashing.controller.OrganizationController.addPrivilege(..))"
			+ "|| execution(* com.ehyf.ewashing.controller.OrganizationController.deletePrivilege(..))"
			)
	public void sysOrganizationAfter(JoinPoint point){
		SecurityOperLog securityOperLog = new SecurityOperLog();
		securityOperLog.setOperTime(new Date());
		securityOperLog.setId(UUID.getUUID32());
		try {
			
			boolean isSuccess = true ;
			String pointStr = Arrays.toString(point.getArgs());
			if(pointStr.contains("resultCode=0")){
				isSuccess = false;
			}
			if(!isSuccess){
				return;
			}
			
			HttpServletRequest request = (HttpServletRequest)point.getArgs()[2];
			SecurityUser user = getUser();
			securityOperLog.setUserName(user.getUsername());
			securityOperLog.setUserRealName(user.getRealname());
			securityOperLog.setIp(IpUtil.getIpAddress(request));
			
			securityOperLog.setOperObject("组织机构管理");
			
			if("create".equals(point.getSignature().getName())){
				securityOperLog.setOperAction("新增");

				SecurityOrganization securityOrganization = (SecurityOrganization)point.getArgs()[0];
				securityOperLog.setOperDesc("新增组织机构  "+securityOrganization.getName());
		

			}else if("update".equals(point.getSignature().getName())){
				securityOperLog.setOperAction("修改");

				SecurityOrganization securityOrganization = (SecurityOrganization)point.getArgs()[0];
				securityOperLog.setOperDesc("修改组织机构  "+securityOrganization.getName());

			}else if("addPrivilege".equals(point.getSignature().getName())){
				securityOperLog.setOperAction("赋予角色权限");
				
				String id = (String)point.getArgs()[0];
				String roleId = request.getParameter("roleId");
				SecurityOrganization sysOrganization  =organizationService.getById(id);
				SecurityRole  SysRole  = securityRoleService.getById(roleId);
				securityOperLog.setOperDesc("对组织机构  "+sysOrganization.getName()+" 赋予了 "+ SysRole.getName() +" 角色权限");


			}else if("deletePrivilege".equals(point.getSignature().getName())){
				securityOperLog.setOperAction("删除角色权限");
				
				String id = (String)point.getArgs()[0];
				String roleId = request.getParameter("roleId");
				SecurityOrganization sysOrganization  =organizationService.getById(id);
				SecurityRole  SysRole  = securityRoleService.getById(roleId);
				securityOperLog.setOperDesc("对组织机构  "+sysOrganization.getName()+" 移除了 "+ SysRole.getName() +" 角色权限");
			}
			
			securityOperLogService.insert(securityOperLog);

		} catch (Exception e) {
			e.printStackTrace();
			log.error("记录机构组织管理操作日志出现异常......",e);
		}
		
	}
	
	/**
	 * 机构组织管理
	 * @param point
	 */
	@Before(value =  " execution(* com.ehyf.ewashing.controller.OrganizationController.delete(..))")
	public void sysOrganizationBefore(JoinPoint point){
		SecurityOperLog securityOperLog = new SecurityOperLog();
		securityOperLog.setOperTime(new Date());
		securityOperLog.setId(UUID.getUUID32());
		try {
			
			boolean isSuccess = true ;
			String pointStr = Arrays.toString(point.getArgs());
			if(pointStr.contains("resultCode=0")){
				isSuccess = false;
			}
			if(!isSuccess){
				return;
			}
			
			HttpServletRequest request = (HttpServletRequest)point.getArgs()[2];
			SecurityUser user = getUser();
			securityOperLog.setUserName(user.getUsername());
			securityOperLog.setUserRealName(user.getRealname());
			securityOperLog.setIp(IpUtil.getIpAddress(request));
			
			securityOperLog.setOperObject("组织机构管理");
			
		    if("delete".equals(point.getSignature().getName())){
				securityOperLog.setOperAction("删除");
				
				String id = request.getParameter("id");
				SecurityOrganization sysOrganization  =organizationService.getById(id);
				securityOperLog.setOperDesc("组织机构  "+sysOrganization.getName()+" 被删除");

			}
			
			securityOperLogService.insert(securityOperLog);

		} catch (Exception e) {
			e.printStackTrace();
			log.error("记录机构组织管理操作日志出现异常......",e);
		}
	}
}

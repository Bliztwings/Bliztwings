package com.ehyf.ewashing.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ehyf.ewashing.common.Constants;
import com.ehyf.ewashing.common.PermissionInit;
import com.ehyf.ewashing.entity.SecurityResource;
import com.ehyf.ewashing.entity.SecurityRole;
import com.ehyf.ewashing.entity.SecurityRolePermission;
import com.ehyf.ewashing.entity.SecurityUser;
import com.ehyf.ewashing.service.SecurityResourceService;
import com.ehyf.ewashing.service.SecurityRolePermissionService;
import com.ehyf.ewashing.service.SecurityUserService;

@Controller
public class IndexContrller {

	Log log = LogFactory.getLog(IndexContrller.class);

	@Autowired
	private SecurityResourceService securityResourceService;
	
	@Autowired
	private SecurityUserService securityUserService;
	
	//@Autowired
	//private SecurityOrganizationService securityOrganizationService;
	
	@Autowired
	private SecurityRolePermissionService securityRolePermissionService;
	
	@Autowired
	private PermissionInit permissionInit;

	/**
	 * <用户登录成功,跳转到系统首页>
	 * 
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	@RequestMapping(value = "/", method = { RequestMethod.POST, RequestMethod.GET })
	public String index(HttpServletRequest req,Model model) {
		return getMenuList(model);
	}

	@RequestMapping(value = "/index", method = { RequestMethod.POST, RequestMethod.GET })
	public String toLogin(HttpServletRequest req,Model model) {
		return getMenuList(model);
	}
	
	private String getMenuList(Model model){
        permissionInit.refresh();
        permissionInit.currUserAuthorizationInfo();
        
		SecurityResource t_securityResource = new SecurityResource();
		t_securityResource.setCategory("menu");
		t_securityResource.setParentId("0");
		t_securityResource.setAvailable(1);
		
		try {
			List<SecurityResource>  securityResourceList = securityResourceService.findList(t_securityResource);
	        
	        String userName = (String)SecurityUtils.getSubject().getPrincipal();
	        if(userName.equals("admin")){
	        	model.addAttribute("securityResourceList", securityResourceList);
				return "index";
	        }
	        SecurityUser securityUser = securityUserService.findUserByLoginName(userName);
	        List<SecurityRole> userRoleList = null;
	        List<SecurityRole> userAllList = new ArrayList<SecurityRole>();
	        List<String> roleIdList = new ArrayList<String>();
	        userRoleList = securityUserService.getUserRoleList(securityUser.getId());
	        
	        if(userRoleList!=null && userRoleList.size()>0){
	        	userAllList.addAll(userRoleList);
	        }
	        for(SecurityRole securityRole : userAllList){
	        	if(!roleIdList.contains(securityRole.getId())){
	        		roleIdList.add(securityRole.getId());
	        	}
	        }
	        
	        List<SecurityRolePermission> allPerList = new ArrayList<SecurityRolePermission>();
	        List<SecurityRolePermission> perList = null;
	        SecurityRolePermission securityRolePermission = null;
	        List<String> perIdList = new ArrayList<String>();
	        
	        for(String roleId : roleIdList){
	        	securityRolePermission = new SecurityRolePermission();
	        	securityRolePermission.setRoleId(roleId);
		        perList = securityRolePermissionService.findList(securityRolePermission);
		        if(perList!=null && perList.size()>0){
		        	allPerList.addAll(perList);
		        }
	        }
	        for(SecurityRolePermission securityRolePermission2 : allPerList){
	        	if(!perIdList.contains(securityRolePermission2.getPermissionId())){
	        		perIdList.add(securityRolePermission2.getPermissionId());
	        	}
	        }
	        List<SecurityResource> filterMenuList = new ArrayList<SecurityResource>();
	        List<SecurityResource> subMenuList = null;
	        
	        for(SecurityResource securityResource : securityResourceList){
	        	if(perIdList.contains(securityResource.getId())){
	        		subMenuList = new ArrayList<SecurityResource>();
	        		for(SecurityResource securityResource2 : securityResource.getChildren()){
	        			if(perIdList.contains(securityResource2.getId())){
	        				subMenuList.add(securityResource2);
	        			}
	        		}
	        		securityResource.setChildren(subMenuList);
	        		filterMenuList.add(securityResource);
	        	}
	        }
	        
	        model.addAttribute("securityResourceList", filterMenuList);
	        if("2".equals(securityUser.getUserType())){
	        	return "factory";
	        }
			return "index";
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	@RequestMapping(value = "/left/{id}", method = { RequestMethod.GET })
	public String toLeft(@PathVariable String id, Model model) {
		SecurityResource securityResource = new SecurityResource();
		securityResource.setParentId(id);
		securityResource.setCategory("menu");
		try {
			List<SecurityResource>  securityResourceList = securityResourceService.findList(securityResource);
	        model.addAttribute("securityResourceList", securityResourceList);
			return "left";
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
	
    @RequestMapping("/welcome")
    public String welcome() {
    	log.info("welcome = ");
        return "welcome";
    }
	@RequestMapping(value="/getUname")
	@ResponseBody
	public Object getList(HttpServletRequest req,Model model) {
		model.asMap().clear();
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			SecurityUser securityUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
			model.addAttribute("user", securityUser);
			return objectMapper.writeValueAsString(model);		
		} catch (Exception e) {
			log.error(e.toString(), e);
		} 
		return null;
	}

}

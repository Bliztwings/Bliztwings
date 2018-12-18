package com.ehyf.ewashing.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ehyf.ewashing.common.Constants;
import com.ehyf.ewashing.entity.SecurityOrganization;
import com.ehyf.ewashing.entity.SecurityOrganizationRole;
import com.ehyf.ewashing.entity.SecurityUser;
import com.ehyf.ewashing.service.SecurityOrganizationRoleService;
import com.ehyf.ewashing.service.SecurityOrganizationService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("/organization")
public class OrganizationController {
	
	Log log = LogFactory.getLog(OrganizationController.class);
	
	@Autowired
	private SecurityOrganizationService  securityOrganizationService;
	
	@Autowired
	private SecurityOrganizationRoleService securityOrganizationRoleService;

	/**
	 * <机构管理主页面>
	 * @param model
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	@RequestMapping(method = RequestMethod.GET)
    public String index(Model model,HttpServletRequest req) {
        return "security/organization/index";
    }
	
	/**
	 * <机构组织树展示>
	 * @param model
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    public String showTree(Model model,HttpServletRequest req) {
    	SecurityOrganization securityOrganization = new SecurityOrganization();
    	
        try {
        	List<SecurityOrganization> orgList =  securityOrganizationService.findList(securityOrganization);
			model.addAttribute("organizationList",orgList);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
        return "security/organization/tree";
    }
    
    /**
     * <机构组织详情及维护页面>
     * @param id
     * @param model
     * @return
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/{id}/maintain", method = RequestMethod.GET)
    public String showMaintainForm(@PathVariable("id") String id, Model model,HttpServletRequest req) {
        try {
        	SecurityOrganization org = securityOrganizationService.getById(id);
			model.addAttribute("organization", org);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
       return "security/organization/maintain";
        
    }
    
    
    /**
     * <进入创建机构组织>
     * @param parentId
     * @param model
     * @return
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/{parentId}/preCreate", method = RequestMethod.GET)
    public String preCreate(@PathVariable("parentId") String parentId, Model model,HttpServletRequest req) {
    	SecurityOrganization parent;
		try {
			parent = securityOrganizationService.getById(parentId);
	        model.addAttribute("parent", parent);
	        SecurityOrganization child = new SecurityOrganization();
	        child.setParentId(parentId);
	        model.addAttribute("child", child);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
        return "security/organization/create";
    } 
    
    
    /**
	 * <检查 机构组织名称是否存在>
	 * @param req
	 * @param securityUser
	 * @param model
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	@ResponseBody
	@RequestMapping(value = "/{parentId}/checkNameForUpdate", method = {RequestMethod.POST, RequestMethod.GET})
	public String checkNameForUpdate(@PathVariable("parentId") String parentId,HttpServletRequest req,Model model) {
		model.asMap().clear();
		ObjectMapper objectMapper = new ObjectMapper();
		String fieldId = req.getParameter("fieldId");
		String fieldValue = req.getParameter("fieldValue");
		Object  result[] = new Object[3] ;
		result[0] = fieldId;
		SecurityOrganization securityOrganization = new SecurityOrganization();
		if(fieldId.endsWith("name")){
			securityOrganization.setName(fieldValue);
		}
		try {
			List<SecurityOrganization> list = securityOrganizationService.findList(securityOrganization);
			if(list.size()>0){
				result[1] = false;
			}else{
				result[1] = true;
			}
			return  objectMapper.writeValueAsString(result);
		} catch (Exception e) {
			log.error(e);
		}
		return null;
	}
    
    /**
     * <添加机构组织>
     * @param organization
     * @return
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(SecurityOrganization securityOrganization,Model model,HttpServletRequest req) {
        try {
        	//securityOrganization.setCreateDate();
			SecurityUser securityUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
			securityOrganization.setCreateUser(securityUser.getUsername());
        	securityOrganizationService.insert(securityOrganization);
			model.addAttribute("resultMsg", "添加机构组织信息成功！");
			model.addAttribute("resultCode", 1);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			model.addAttribute("resultMsg", "添加机构组织信息失败！");
			model.addAttribute("resultCode", 0);
		}
        return "security/organization/create";
    }
    
    /**
     * <修改 机构组织>
     * @param organization
     * @return
     * @see [类、类#方法、类#成员]
     */
    @ResponseBody
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(SecurityOrganization securityOrganization,Model model,HttpServletRequest req) {
		model.asMap().clear();
		ObjectMapper objectMapper = new ObjectMapper();
        try {
        	//securityOrganization.setUpdateDate(new Date());
			SecurityUser securityUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
			securityOrganization.setUpdateUser(securityUser.getUsername());
			securityOrganizationService.update(securityOrganization);
			model.addAttribute("resultMsg", "修改机构组织信息成功！");
			model.addAttribute("resultCode", 1);
			return objectMapper.writeValueAsString(model);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			model.addAttribute("resultMsg", "修改机构组织信息失败！");
			model.addAttribute("resultCode", 0);
		}
        return  null;
    }
    
    /**
     * <修改 机构组织>
     * @param organization
     * @return
     * @see [类、类#方法、类#成员]
     */
    @ResponseBody
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String delete(SecurityOrganization org,Model model,HttpServletRequest req) {
		model.asMap().clear();
		ObjectMapper objectMapper = new ObjectMapper();
        try {
        	String id = req.getParameter("id");
        	securityOrganizationService.deleteById(id);
			model.addAttribute("resultMsg", "删除机构组织信息成功！");
			model.addAttribute("resultCode", 1);
			return objectMapper.writeValueAsString(model);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			model.addAttribute("resultMsg", "删除机构组织信息失败！");
			model.addAttribute("resultCode", 0);
		}
        return  null;
    }
    
    
    /**
     * <进入创建机构组织>
     * @param parentId
     * @param model
     * @return
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/{id}/setPrivilege", method = RequestMethod.GET)
    public String setPrivilege(@PathVariable("id") String id, Model model,HttpServletRequest req) {
    	log.info("id = "+id );
    	String isPrivilege = req.getParameter("isPrivilege");
		String pageNum = req.getParameter("pageNum");
		String pageSize = req.getParameter("pageSize");		
		try {

			if(StringUtils.isBlank(isPrivilege)){
				isPrivilege = "YES";		
			}
			
			if (StringUtils.isBlank(pageNum)) {
				pageNum = "1";
			}
			if (StringUtils.isBlank(pageSize)) {
				pageSize = "10";
			}
			
			SecurityOrganizationRole entity = new SecurityOrganizationRole();
			entity.setOrgId(id);
			
			PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize));
			List<SecurityOrganizationRole> list = securityOrganizationRoleService.findList(entity);
			PageInfo<SecurityOrganizationRole> page = new PageInfo<SecurityOrganizationRole>(list);

	        model.addAttribute("page", page);
	        model.addAttribute("isPrivilege", isPrivilege);
	        model.addAttribute("id", id);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
        return "security/organization/setPrivilege";
    } 
    
    /**
     * <添加 机构组织角色>
     * @param id
     * @param model
     * @param req
     * @return
     * @see [类、类#方法、类#成员]
     */
    @ResponseBody
    @RequestMapping(value = "/{id}/addPrivilege", method = {RequestMethod.POST, RequestMethod.GET})
    public String addPrivilege(@PathVariable("id") String id, Model model,HttpServletRequest req) {
		model.asMap().clear();
		ObjectMapper objectMapper = new ObjectMapper();
    	String isPrivilege = req.getParameter("isPrivilege");
    	String roleId = req.getParameter("roleId");
    	try {
    		SecurityOrganizationRole securityOrganizationRole = new SecurityOrganizationRole();
			if(StringUtils.isBlank(id)||StringUtils.isBlank(roleId)||StringUtils.isBlank(isPrivilege)){
				model.addAttribute("resultMsg", "添加机构组织角色时，传入参数异常！");
				model.addAttribute("resultCode", 0);
		        model.addAttribute("isPrivilege", isPrivilege);
		        model.addAttribute("id", id);
		        return objectMapper.writeValueAsString(model);
			}
    		securityOrganizationRole.setOrgId(id);
    		securityOrganizationRole.setRoleId(roleId);
    		securityOrganizationRoleService.insert(securityOrganizationRole);
			model.addAttribute("resultMsg", "添加机构组织角色成功！");
			model.addAttribute("resultCode", 1);
	        model.addAttribute("isPrivilege", isPrivilege);
	        model.addAttribute("id", id);
			return objectMapper.writeValueAsString(model);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			model.addAttribute("resultMsg", "添加机构组织角色失败！");
			model.addAttribute("resultCode", 1);
	        model.addAttribute("isPrivilege", isPrivilege);
	        model.addAttribute("id", id);
		}
    	return null;
    }
    
    /**
     * <>
     * @param id
     * @param model
     * @param req
     * @return
     * @see [类、类#方法、类#成员]
     */
    @ResponseBody
    @RequestMapping(value = "/{id}/deletePrivilege", method = {RequestMethod.POST, RequestMethod.GET})
    public String deletePrivilege(@PathVariable("id") String id, Model model,HttpServletRequest req) {
		model.asMap().clear();
		ObjectMapper objectMapper = new ObjectMapper();
    	log.info("id = "+id );
    	String roleId = req.getParameter("roleId");
    	String isPrivilege = req.getParameter("isPrivilege");
    	try {
    		SecurityOrganizationRole securityOrganizationRole = new SecurityOrganizationRole();
			if(StringUtils.isBlank(id)||StringUtils.isBlank(roleId)||StringUtils.isBlank(isPrivilege)){
				model.addAttribute("resultMsg", "删除机构组织角色时，传入参数异常！");
				model.addAttribute("resultCode", 0);
		        model.addAttribute("isPrivilege", isPrivilege);
		        model.addAttribute("id", id);
		        return objectMapper.writeValueAsString(model);
			}
			
			securityOrganizationRole.setId(id);
			securityOrganizationRole.setRoleId(roleId);
    		
			securityOrganizationRoleService.delete(securityOrganizationRole);
			model.addAttribute("resultMsg", "删除机构组织角色成功！");
			model.addAttribute("resultCode", 1);
	        model.addAttribute("isPrivilege", isPrivilege);
	        model.addAttribute("id", id);
			return objectMapper.writeValueAsString(model);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			model.addAttribute("resultMsg", "删除机构组织角色失败！");
			model.addAttribute("resultCode", 0);
	        model.addAttribute("isPrivilege", isPrivilege);
	        model.addAttribute("id", id);
		}
    	return null;
    }
    
}

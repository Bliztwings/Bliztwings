package com.ehyf.ewashing.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ehyf.ewashing.entity.SecurityResource;
import com.ehyf.ewashing.entity.SecurityRole;
import com.ehyf.ewashing.entity.SecurityRolePermission;
import com.ehyf.ewashing.service.SecurityResourceService;
import com.ehyf.ewashing.service.SecurityRolePermissionService;
import com.ehyf.ewashing.service.SecurityRoleService;
import com.ehyf.ewashing.util.UUID;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 角色管理
 * @author
 *
 */
@Controller
@RequestMapping("role")
public class SecurityRoleController {
	Log log = LogFactory.getLog(SecurityRoleController.class);
	
	@Autowired
	private SecurityRoleService securityRoleService;
	
	@Autowired
	private SecurityRolePermissionService securityRolePermissionService;
	
	@Autowired
	private SecurityResourceService securityResourceService;
	
	/**
	 * <根据相关条件获取角色信息列表
	 * 使用paginator 分页>
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	@RequestMapping(method={RequestMethod.GET,RequestMethod.POST})
	public String selectByExample(HttpServletRequest req, SecurityRole securityRole,
			Model model){
		model.asMap().clear();
		try {			
			String pageNum = req.getParameter("pageNum");
			String pageSize = req.getParameter("pageSize");
			//String name = req.getParameter("name");
			if (StringUtils.isBlank(pageNum)) {
				pageNum = "1";
			}
			if (StringUtils.isBlank(pageSize)) {
				pageSize = "10";
			}
			
			PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize));
			SecurityRole entity = new SecurityRole();
			List<SecurityRole> list = securityRoleService.findList(entity);
			PageInfo<SecurityRole> page = new PageInfo<SecurityRole>(list);
			
			model.addAttribute("page", page);
			return "security/role/list";
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}		
		return null;
	}
	
	/**
	 * <进入添加用户的页面>
	 * @param req
	 * @param securityUser
	 * @param model
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	@RequestMapping(value = "/preCreate", method = RequestMethod.GET)
	public String preCreate(HttpServletRequest req, SecurityRole securityRole,
			Model model) {
		model.asMap().clear();
		log.info(new Date().getTime());
		return "security/role/create";
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(HttpServletRequest req, SecurityRole securityRole,
			Model model, RedirectAttributes redirectAttributes) {
		model.asMap().clear();
		try {
			securityRole.setId(UUID.getUUID32());
			securityRoleService.insert(securityRole);
			model.addAttribute("resultMsg", "添加角色信息成功！");
			model.addAttribute("resultCode", 1);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			model.addAttribute("resultMsg", "添加角色信息失败！");
			model.addAttribute("resultCode", 0);
		}
		return "security/role/create";
	}
	
	@RequestMapping(value="/checkPrimaryKey", method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String checkPrimaryKey(HttpServletRequest req, SecurityRole securityRole,
			Model model){
		/** 1: 成功 ，0： 失败 */
		int resultCode = 1;
		String resultMsg = "成功！";
		model.asMap().clear();
		ObjectMapper objectMapper = new ObjectMapper();	
		try {
			String id = req.getParameter("id");
			log.info("id = " + id );
			if(StringUtils.isBlank(id)){
				log.error("需要修改的主键为空，非法参数");
				resultCode = 0;
				resultMsg = "需要修改的主键为空，非法参数";
				model.addAttribute("resultCode", resultCode);
				model.addAttribute("resultMsg", resultMsg);			
				return objectMapper.writeValueAsString(model);
			}
			securityRole = securityRoleService.getById(id);
			if(null == securityRole){
				log.error("对象不存在，非法参数");
				resultCode = 0;
				resultMsg = "对象不存在，非法参数";
				model.addAttribute("resultCode", resultCode);
				model.addAttribute("resultMsg", resultMsg);
				return objectMapper.writeValueAsString(model);											
			}
			model.addAttribute("resultCode", resultCode);
			model.addAttribute("resultMsg", resultMsg);
			model.addAttribute("id", id);
			return objectMapper.writeValueAsString(model);
		}catch(Exception e){
			log.error(e.getMessage(),e);
		}
		return null;
	}
	
	
	/**
	 * <进入角色更新页面>
	 * 
	 * @param req
	 * @param securityRole
	 * @param model
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	@RequestMapping(value="/preUpdate", method={RequestMethod.GET,RequestMethod.POST})
	public String preUpdate(HttpServletRequest req, SecurityRole securityRole,
			Model model){
		/** 1: 成功 ，0： 失败 */
		int resultCode = 1;
		String resultMsg = "";
		model.asMap().clear();
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			String id = req.getParameter("id");
			log.info("id = " + id );
			if(StringUtils.isBlank(id)){
				log.error("需要修改的主键为空，为非法参数");
				resultCode = 0;
				resultMsg = "需要修改的主键为空，为非法参数";
				model.addAttribute("resultCode", resultCode);
				model.addAttribute("resultMsg", resultMsg);				
				return objectMapper.writeValueAsString(model);
			}
			securityRole = securityRoleService.getById(id);
			if(null == securityRole){
				log.error("对象不存在，非法参数");
				resultCode = 0;
				resultMsg = "对象不存在，非法参数";
				model.addAttribute("resultCode", resultCode);
				model.addAttribute("resultMsg", resultMsg);
				return objectMapper.writeValueAsString(model);											
			}
			model.addAttribute("securityRole", securityRole);			
			return "security/role/update";
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}				
		return null;		
	}
	
	
	/**
	 * <更新角色信息>
	 * 
	 * @param req
	 * @param securityRole
	 * @param model
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	@RequestMapping(value="/update", method={RequestMethod.GET,RequestMethod.POST})
	public String  update(HttpServletRequest req, SecurityRole securityRole,
			Model model){
		try {
			securityRoleService.update(securityRole);
			model.addAttribute("resultCode", "1");	
			model.addAttribute("resultMsg", "角色修改成功");	
			return "security/role/update";
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			model.addAttribute("resultCode", "0");	
			model.addAttribute("resultMsg", "角色修改失败");	
			return null;
		}
	}
	
	/**
	 * <删除角色信息>
	 * 
	 * @param req
	 * @param securityRole
	 * @param model
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	@RequestMapping(value="/remove", method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String  remove(HttpServletRequest req, SecurityRole securityRole, Model model){
		ObjectMapper objectMapper = new ObjectMapper();
		model.asMap().clear();
		try {
			String  id = req.getParameter("id");
			securityRoleService.deleteById(id);	
			
			//删除角色对应的权限
			SecurityRolePermission entity = new SecurityRolePermission();
			entity.setRoleId(id);
			securityRolePermissionService.delete(entity);
			
			model.addAttribute("securityRole", securityRole);
			model.addAttribute("resultCode", "1");	
			model.addAttribute("resultMsg", "角色删除成功");	
			return objectMapper.writeValueAsString(model);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			model.addAttribute("resultCode", "0");	
			model.addAttribute("resultMsg", "角色删除");	
			return null;
		}	
	}
	
	@RequestMapping(value="/toSetPermission", method={RequestMethod.GET,RequestMethod.POST})
	public String toSetPermission(HttpServletRequest req, Model model){
		String id = req.getParameter("id");
		try {
			model.addAttribute("roleId", id);
			return "security/role/tree";
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			return null;
		}	
	}
	
	@RequestMapping(value = "/tree",method = {RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public String tree(HttpServletRequest req, Model model){
		String roleId = req.getParameter("roleId");
		
		try {
			SecurityResource resource = new SecurityResource();
			List<SecurityResource> list = securityResourceService.findList(resource);
			SecurityResource securityResource = null;
			StringBuilder sb = new StringBuilder();
			
			SecurityRolePermission entity = new SecurityRolePermission();
			entity.setRoleId(roleId);
			List<SecurityRolePermission> list2 = securityRolePermissionService.findList(entity);
			List<String> perList = new ArrayList<String>();
			if(list2!=null && list2.size()>0){
				for(SecurityRolePermission oo : list2){
					perList.add(oo.getPermissionId());
				}
			}
			
			if (list != null) {
				int size = list.size();
				sb.append("[");
				sb.append("{id:\"");
				sb.append("0");
				sb.append("\",pId:\"");
				sb.append("-1");
				sb.append("\",name:\"");
				sb.append("");
				sb.append("\",level:\"");
				sb.append("0");
				sb.append("\",checked:\"");
				sb.append(perList.contains("1")?true:false);
				sb.append("\"}");

				if (list.size() > 0) {
					sb.append(",");
				}

				for (int i = 0; i < list.size(); i++) {
					size--;
					securityResource = list.get(i);
					sb.append("{id:\"");
					sb.append(securityResource.getId());
					sb.append("\",pId:\"");
					sb.append(securityResource.getParentId());
					sb.append("\",name:\"");
					sb.append(securityResource.getName());
					sb.append("\",level:\"");
					sb.append(securityResource.getParentId().equals("1")?1:2);
					sb.append("\",checked:\"");
					sb.append(perList.contains(securityResource.getId())?true:false);
					
					sb.append("\"}");
					if (size > 0) {
						sb.append(",");
					}
				}
				sb.append("]");
			}
			
			model.asMap().clear();
			ObjectMapper mapper = new ObjectMapper(); //转换器  
			model.addAttribute("obj", sb.toString());
			
			return mapper.writeValueAsString(model);
		} catch (Exception e) {
			return null;
		}
	}
	
	@RequestMapping(value = "/savePermission",method = {RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public String savePermission(HttpServletRequest req, Model model){
		String roleId = req.getParameter("roleId");
		String permissions = req.getParameter("permissions");
		try {
			if(StringUtils.isBlank(permissions)){
				//清空权限
				SecurityRolePermission entity = new SecurityRolePermission();
				entity.setRoleId(roleId);
				securityRolePermissionService.deleteByRoleId(entity);
			}
			else{
				//先删除旧权限，再保存
				SecurityRolePermission entity = new SecurityRolePermission();
				entity.setRoleId(roleId);
				securityRolePermissionService.deleteByRoleId(entity);
				
				String[] perIds = permissions.split(",");
				SecurityRolePermission record = null;
				for(String perId : perIds){
					record = new SecurityRolePermission();
					record.setRoleId(roleId);
					record.setPermissionId(perId);
					record.setId(UUID.getUUID32());
					securityRolePermissionService.insert(record);
				}
			}
			model.asMap().clear();
			ObjectMapper objectMapper = new ObjectMapper(); //转换器  
			model.addAttribute("resultCode", "1");	
			model.addAttribute("resultMsg", "角色权限设置成功");	
			return objectMapper.writeValueAsString(model);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			return null;
		}	
	}
	
	
}

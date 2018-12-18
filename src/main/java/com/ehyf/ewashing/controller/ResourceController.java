package com.ehyf.ewashing.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ehyf.ewashing.entity.SecurityResource;
import com.ehyf.ewashing.service.SecurityResourceService;
import com.ehyf.ewashing.util.UUID;

/**
 * 资源管理
 * @author Dell
 *
 */
@Controller
@RequestMapping("/resource")
public class ResourceController {
	//private static Log log = LogFactory.getLog(ResourceController.class);
	
	@Autowired
	private SecurityResourceService securityResourceService;
	
	@RequestMapping(value = "/list",method = {RequestMethod.POST, RequestMethod.GET})
	public String list(Model model){
		return "security/resource/resource";
	}
	
	@RequestMapping(value = "/treeFrame",method = {RequestMethod.POST, RequestMethod.GET})
	public String treeFrame(Model model){
		return "security/resource/tree";
	}
	
	@RequestMapping(value = "/tree",method = {RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public String tree(Model model){
		try {
			SecurityResource entity = new SecurityResource();
			entity.setCategory("menu");
			List<SecurityResource> list = securityResourceService.findList(entity);
			SecurityResource securityResource = null;
			StringBuilder sb = new StringBuilder();
			
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
	
	@RequestMapping(value = "/content",method = {RequestMethod.POST, RequestMethod.GET})
	public String content(HttpServletRequest req,Model model){
		String parentId = req.getParameter("parentId");
		String level = req.getParameter("level");
		parentId = parentId==null? "1":parentId;
		String name = req.getParameter("name");
		
		try{
			SecurityResource entity = new SecurityResource();
			entity.setParentId(parentId);
			List<SecurityResource> list = securityResourceService.findList(entity);
			if(StringUtils.isNotBlank(name)){
				list = selectiveListByKeyword(name,list);
			}
			model.addAttribute("list", list);
			model.addAttribute("parentId", parentId);
			model.addAttribute("level", level);
			model.addAttribute("name", name);
			return "security/resource/content";
		}catch(Exception e){
			return null;
		}
	}
	
	private List<SecurityResource> selectiveListByKeyword(String name,List<SecurityResource> list){
		List<SecurityResource> newList = new ArrayList<SecurityResource>();
		if(list!=null && list.size()>0){
			for(SecurityResource securityResource : list){
				if(securityResource.getName().toLowerCase().contains(name.toLowerCase())){
					newList.add(securityResource);
				}
			}
		}
		return newList;
	}
	
	@RequestMapping(value = "/removeMenuOrButton",method = {RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public String removeMenuOrButton(HttpServletRequest req,Model model){
		model.asMap().clear();
		String id = req.getParameter("id");
		
		try{
			SecurityResource entity = new SecurityResource();
			entity.setParentId(id);
			List<SecurityResource> list = securityResourceService.findList(entity);
			if(list!=null && list.size()>0){
				ObjectMapper mapper = new ObjectMapper(); //转换器  
				model.addAttribute("resultMsg", "该菜单下存在子菜单或按钮，不能删除！");
				model.addAttribute("resultCode", 0);
				return mapper.writeValueAsString(model);
			}
			else{
				securityResourceService.deleteById(id);
				ObjectMapper mapper = new ObjectMapper(); //转换器  
				model.addAttribute("resultMsg", "删除菜单或按钮成功！");
				model.addAttribute("resultCode", 1);
				return mapper.writeValueAsString(model);
			}
		}catch(Exception e){
			return null;
		}
	}
	
	@RequestMapping(value = "/toCreateMenuOrButton",method = {RequestMethod.POST, RequestMethod.GET})
	public String toCreateMenuOrButton(HttpServletRequest req,Model model){
		String parentId = req.getParameter("parentId");
		String level = req.getParameter("level");
		
		try {
			SecurityResource securityResource = securityResourceService.getById(parentId);
			model.addAttribute("parentId", parentId);
			model.addAttribute("level", level);
			model.addAttribute("securityResource", securityResource);
			return "security/resource/create";
		} catch (Exception e) {
			return null;
		}
	}
	
	@RequestMapping(value = "/toEditMenuOrButton",method = {RequestMethod.POST, RequestMethod.GET})
	public String toEditMenuOrButton(HttpServletRequest req,Model model){
		String id = req.getParameter("id");
		String parentId = req.getParameter("parentId");
		String level = req.getParameter("level");
		
		try {
			SecurityResource securityResource = securityResourceService.getById(id);
			if(!level.equals("0")){
				SecurityResource p_securityResource = securityResourceService.getById(parentId);
				model.addAttribute("p_securityResource", p_securityResource);
			}
			model.addAttribute("id", id);
			model.addAttribute("parentId", parentId);
			model.addAttribute("level", level);
			model.addAttribute("securityResource", securityResource);
			return "security/resource/edit";
		} catch (Exception e) {
			return null;
		}
	}
	
	@RequestMapping(value = "/createMenuOrButton",method = {RequestMethod.POST, RequestMethod.GET})
	public String createMenuOrButton(HttpServletRequest req,Model model,SecurityResource securityResource){
		String parentId = req.getParameter("parentId");
		String level = req.getParameter("level");
		try{
			if(level.equals("2")){
				securityResource.setCategory("button");
			}
			else{
				securityResource.setCategory("menu");
			}
			securityResource.setAvailable(1);
			securityResource.setParentId(parentId);
			securityResource.setId(UUID.getUUID32());
			securityResourceService.insert(securityResource);
			model.addAttribute("resultMsg", "新增菜单或按钮成功！");
			model.addAttribute("resultCode", 1);
			return "security/resource/create";
		}catch(Exception e){
			model.addAttribute("resultMsg", "新增菜单或按钮成功！");
			model.addAttribute("resultCode", 0);
			return "security/resource/create";
		}
	}
	
	@RequestMapping(value = "/editMenuOrButton",method = {RequestMethod.POST, RequestMethod.GET})
	public String editMenuOrButton(HttpServletRequest req,Model model,SecurityResource securityResource){
		try{
			securityResourceService.update(securityResource);
			model.addAttribute("resultMsg", "修改菜单或按钮成功！");
			model.addAttribute("resultCode", 1);
			return "security/resource/edit";
		}catch(Exception e){
			model.addAttribute("resultMsg", "修改菜单或按钮失败！");
			model.addAttribute("resultCode", 0);
			return "security/resource/edit";
		}
	}
	
	
	
}

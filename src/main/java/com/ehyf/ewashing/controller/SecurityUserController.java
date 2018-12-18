package com.ehyf.ewashing.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONObject;
import com.ehyf.ewashing.common.Constants;
import com.ehyf.ewashing.entity.EwashingStore;
import com.ehyf.ewashing.entity.SecurityRole;
import com.ehyf.ewashing.entity.SecurityUser;
import com.ehyf.ewashing.entity.SecurityUserRole;
import com.ehyf.ewashing.service.EwashingStoreService;
import com.ehyf.ewashing.service.SecurityRoleService;
import com.ehyf.ewashing.service.SecurityUserRoleService;
import com.ehyf.ewashing.service.SecurityUserService;
import com.ehyf.ewashing.shiro.credentials.PasswordHelper;
import com.ehyf.ewashing.util.UUID;
import com.ehyf.ewashing.vo.SecurityUserRoleVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Controller()
@RequestMapping("/user")
public class SecurityUserController {

	Log log = LogFactory.getLog(SecurityUserController.class);

	@Autowired
	private SecurityUserService securityUserService;

	@Autowired
	private SecurityUserRoleService securityUserRoleService;
	
	@Autowired
	private SecurityRoleService roleService;
	
	@Autowired
	private SecurityUserRoleService userRoleService;
	
	@Autowired
	private PasswordHelper passwordHelper;
	
	@Autowired
	private EwashingStoreService storeService;
	

	/**
	 * <查询用户信息列表,分页组件不一样 使用 PageHelper 进行分页 >
	 * 
	 * @param securityUser
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	@RequestMapping(method = { RequestMethod.POST, RequestMethod.GET })
	public String list(HttpServletRequest req, SecurityUser securityUser,HttpSession session,
			Model model) {
		String pageNum = req.getParameter("pageNum");
		String pageSize = req.getParameter("pageSize");

		String username = req.getParameter("username");
		String realName = req.getParameter("realname");

		try {
			if (StringUtils.isBlank(pageNum)) {
				pageNum = "1";
			}
			if (StringUtils.isBlank(pageSize)) {
				pageSize = "10";
			}
			
			PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize));
			SecurityUser entity = new SecurityUser();
			List<SecurityUser> list = securityUserService.findList(entity);
			PageInfo<SecurityUser> page = new PageInfo<SecurityUser>(list);
			
			model.addAttribute("page", page);
			model.addAttribute("username", username);
			model.addAttribute("realName", realName);
			return "security/user/list";
		} catch (Exception e) {
			log.error("查询用户列表出现异常。", e);
			return null;
		}

	}

	//添加页面
	@RequestMapping(value = "/preCreate", method = RequestMethod.GET)
	public String preCreate(HttpServletRequest req, EwashingStore store, Model model) {
		//setCommonData(model);
		EwashingStore entity = new EwashingStore();
		List<EwashingStore> storeList = storeService.findList(entity);
		model.addAttribute("storeList", storeList);
		return "security/user/create";
	}
	
	@RequestMapping(value = "/setRoleForUser", method = RequestMethod.GET)
	public String setRoleForUser(HttpServletRequest req, Model model) {
		//setCommonData(model);
		String id =req.getParameter("id");
		SecurityUser user =securityUserService.getById(id);
		model.addAttribute("securityUser", user);
		return "security/user/setRoleForUser";
	}
	
	/**
	 * 获取所有角色
	 * @param req
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/preChooseRole", method = RequestMethod.GET)
	public String preChooseRole(HttpServletRequest req, Model model) {
		
		SecurityRole role = new SecurityRole();
		role.setId("1");
		String userId =req.getParameter("userId");
		List<SecurityRole> roleList =roleService.findList(role);
		model.addAttribute("roleList", roleList);
		model.addAttribute("userId", userId);
		return "security/user/preChooseRole";
	}
	
	
	
	@RequestMapping(value = "/queryUserRole", method = RequestMethod.POST)
	@ResponseBody
	public String queryUserRole(HttpServletRequest req, Model model) {
		/** 1: 成功 ，0： 失败 */
		int resultCode = 0;
		String resultMsg = "获取用户角色失败";
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			String id =req.getParameter("userId");
			// 获取该用户已经有的角色
			SecurityUserRole userRole = new SecurityUserRole();
			userRole.setUserId(id);
			List<SecurityUserRoleVo> userRoleList =userRoleService.findUserRoleList(userRole);
			resultCode = 1;
			resultMsg = "获取用户角色成功";
			model.addAttribute("userRoleList", userRoleList);
			model.addAttribute("resultCode", resultCode);
			model.addAttribute("resultMsg", resultMsg);
			return objectMapper.writeValueAsString(model);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			resultCode = 0;
			resultMsg = "获取用户角色失败";
			
			model.addAttribute("resultCode", resultCode);
			model.addAttribute("resultMsg", resultMsg);
			return JSONObject.toJSONString(model);
		}
	}
	
	/**
	 * 删除用户角色
	 * @param req
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/deleteUserRole", method = RequestMethod.POST)
	@ResponseBody
	public String deleteUserRole(HttpServletRequest req, Model model) {
		/** 1: 成功 ，0： 失败 */
		int resultCode = 0;
		String resultMsg = "删除用户角色失败";
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			String id =req.getParameter("id");
			// 获取该用户已经有的角色
			userRoleService.deleteById(id);
			resultCode = 1;
			resultMsg = "删除用户角色成功";
			model.addAttribute("resultCode", resultCode);
			model.addAttribute("resultMsg", resultMsg);
			return objectMapper.writeValueAsString(model);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			resultCode = 0;
			resultMsg = "删除用户角色失败";
			
			model.addAttribute("resultCode", resultCode);
			model.addAttribute("resultMsg", resultMsg);
			return JSONObject.toJSONString(model);
		}
	}
	

	// 初始化日期类型，页面日期传参
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}
	
	// 数据添加
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(HttpServletRequest req, SecurityUser securityUser,
			Model model, RedirectAttributes redirectAttributes) {
		securityUser.setId(UUID.getUUID32());
		//securityUser.setCreateTime(new Date());
		SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
		securityUser.setCreateUser(loginUser.getUsername());
		
		try {
			securityUser.setStatus("enabled");
			
			passwordHelper.encryptPassword(securityUser);
			securityUserService.insert(securityUser);
			model.addAttribute("resultMsg", "添加用户信息成功！");
			model.addAttribute("resultCode", 1);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			model.addAttribute("resultMsg", "添加用户信息失败！");
			model.addAttribute("resultCode", 0);
			return JSONObject.toJSONString(model);
		}
		return "security/user/create";
	}

	@RequestMapping(value = "/saveUserRole", method = RequestMethod.POST)
	@ResponseBody
	public String saveUserRole(HttpServletRequest req, Model model) {
		/** 1: 成功 ，0： 失败 */
		int resultCode = 0;
		String resultMsg = "设置用户角色失败";
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			String userId = req.getParameter("userId");
			String ids = req.getParameter("ids");
			
			if(ids.lastIndexOf(",")!=-1){
				String [] idArray =ids.substring(0,ids.length()-1).split(",");
				
				for(int i =0;i<idArray.length;i++){
					SecurityUserRole entity = new SecurityUserRole();
					entity.setId(UUID.getUUID32());
					entity.setRoleId(idArray[i]);
					entity.setUserId(userId);
					
					// 判断该用户是否存在角色
					SecurityUserRole sr = new SecurityUserRole();
					sr.setUserId(userId);
					sr.setRoleId(idArray[i]);
					List<SecurityUserRoleVo> list =securityUserRoleService.findUserRoleList(sr);
					if(!CollectionUtils.isEmpty(list)){
						resultCode = 0;
						resultMsg = "存在相同的角色";
						model.addAttribute("resultCode", resultCode);
						model.addAttribute("resultMsg", resultMsg);
						return JSONObject.toJSONString(model);
					}
					securityUserRoleService.insert(entity);
				}
			}
			resultCode = 1;
			resultMsg = "设置用户角色成功";
			model.addAttribute("resultCode", resultCode);
			model.addAttribute("resultMsg", resultMsg);
			return objectMapper.writeValueAsString(model);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			resultCode = 0;
			resultMsg = "设置用户角色失败";
			
			model.addAttribute("resultCode", resultCode);
			model.addAttribute("resultMsg", resultMsg);
			return JSONObject.toJSONString(model);
		}
	}
	
	
	
	/**
	 * <获得机构列表>
	 * 
	 * @param model
	 * @see [类、类#方法、类#成员]
	 */
	/**
	private void setCommonData(Model model) {
		try {
			SecurityOrganization entity = new SecurityOrganization();
			List<SecurityOrganization> orgList = organizationService.findList(entity);
			model.addAttribute("organizationList", orgList);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}**/

	// 弹出修改页面
	@RequestMapping(value = "/preUpdate", method = { RequestMethod.GET, RequestMethod.POST })
	public String preUpdate(HttpServletRequest req, SecurityUser securityUser,
			Model model) {
		/** 1: 成功 ，0： 失败 */
		int resultCode = 1;
		String resultMsg = "";
		model.asMap().clear();
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			String id = req.getParameter("id");
			log.info("id = " + id);
			if (StringUtils.isBlank(id)) {
				log.error("需要修改的主键为空，为非法参数");
				resultCode = 0;
				resultMsg = "需要修改的主键为空，为非法参数";
				model.addAttribute("resultCode", resultCode);
				model.addAttribute("resultMsg", resultMsg);
				return objectMapper.writeValueAsString(model);
			}
			securityUser = securityUserService.getById(id);
			if (null == securityUser) {
				log.error("对象不存在，非法参数");
				resultCode = 0;
				resultMsg = "对象不存在，非法参数";
				model.addAttribute("resultCode", resultCode);
				model.addAttribute("resultMsg", resultMsg);
				return objectMapper.writeValueAsString(model);
			}
			/**
			if(StringUtils.isNotBlank(securityUser.getOrgId())){
				securityUser.setOrgName(organizationService.getById(securityUser.getOrgId()).getName());
			}**/
			
			/**
			List<SecurityOrganization> orgList = organizationService.findList(new SecurityOrganization());
			model.addAttribute("organizationList", orgList);**/
			
			EwashingStore entity = new EwashingStore();
			List<EwashingStore> storeList = storeService.findList(entity);
			model.addAttribute("storeList", storeList);
			
			model.addAttribute("securityUser", securityUser);
			return "security/user/update";
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	// 根据主键进行数据修改（单主键）
	@RequestMapping(value = "/update", method = { RequestMethod.GET, RequestMethod.POST })
	public String update(HttpServletRequest req, SecurityUser securityUser,
			Model model) {
		// 获取当前用户
		SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
		securityUser.setUpdateUser(loginUser.getUsername());
		try {
			if (StringUtils.isBlank(securityUser.getId().toString())) {
				log.error("需要修改的主键为空，为非法参数");
				model.addAttribute("resultMsg", "修改用户信息失败！");
				model.addAttribute("resultCode", 0);
			}
			securityUserService.update(securityUser);
			model.addAttribute("resultMsg", "修改用户信息成功！");
			model.addAttribute("resultCode", 1);
			return "security/user/create";
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			model.addAttribute("resultMsg", "修改用户信息失败！");
			model.addAttribute("resultCode", 0);
			return JSONObject.toJSONString(model);
		}
	}

	// 批量删除
	@RequestMapping(value = "/remove", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String remove(HttpServletRequest req, Model model) {
		/** 1: 成功 ，0： 失败 */
		int resultCode = 0;
		String resultMsg = "删除数据失败";
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			String id = req.getParameter("id");
			// 根据主键进行批删
			securityUserService.deleteUserAndRoleById(id);
			resultCode = 1;
			resultMsg = "删除数据成功";
			model.addAttribute("resultCode", resultCode);
			model.addAttribute("resultMsg", resultMsg);
			return objectMapper.writeValueAsString(model);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			resultCode = 0;
			resultMsg = "删除数据失败";
			model.addAttribute("resultCode", resultCode);
			model.addAttribute("resultMsg", resultMsg);
			return JSONObject.toJSONString(model);
		}
	}
	
	/**
	 * 用户角色设置
	 * @param req
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/roleSetting", method = RequestMethod.GET)
    public String roleSetting(HttpServletRequest req,Model model) {
    	String userId = req.getParameter("userId");
    	//type  1:已授权角色，0：未授权角色
    	String type = req.getParameter("type")==null?"1":req.getParameter("type");
		String pageNum = req.getParameter("pageNum");
		String pageSize = req.getParameter("pageSize");		
		
		if (StringUtils.isBlank(pageNum)) {
			pageNum = "1";
		}
		if (StringUtils.isBlank(pageSize)) {
			pageSize = "10";
		}
		
		try {
			List<SecurityRole> list = null;
			
			if(type.equals("1")){
				list = securityUserService.getUserRoleList(userId);
			}
			else{
				list = securityUserService.getUserRoleList(userId);
			}
			
	        model.addAttribute("list", list);
	        model.addAttribute("type", type);
	        model.addAttribute("userId", userId);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
        return "security/user/setRole";
    } 
	
	@RequestMapping(value = "/addOrRemoveRole", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String addOrRemoveRole(HttpServletRequest req,Model model){
		String userId = req.getParameter("userId");
		String roleId = req.getParameter("roleId");
    	//type  1:已授权角色，0：未授权角色
    	String type = req.getParameter("type")==null?"1":req.getParameter("type");
    	ObjectMapper objectMapper = new ObjectMapper();
    	
    	try {
			if(type.equals("1")){
				//移除角色
				SecurityUserRole entity = new SecurityUserRole();
				entity.setRoleId(roleId);
				entity.setUserId(userId);
				securityUserRoleService.delete(entity);
				model.addAttribute("resultCode", "1");
				model.addAttribute("resultMsg", "移除用户角色成功");
		        return objectMapper.writeValueAsString(model);
			}
			else{
				//添加角色
				SecurityUserRole securityUserRole = new SecurityUserRole();
				securityUserRole.setId(UUID.getUUID32());
				securityUserRole.setUserId(userId);
				securityUserRole.setRoleId(roleId);
				
				securityUserRoleService.insert(securityUserRole);
				model.addAttribute("resultCode", "1");
				model.addAttribute("resultMsg", "添加用户角色成功");
		        return objectMapper.writeValueAsString(model);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}
	
	@RequestMapping(value = "/toPwdChange", method = { RequestMethod.GET, RequestMethod.POST })
	public String toPwdChange(HttpServletRequest req, Model model){
		String type = req.getParameter("type")==null? "info":req.getParameter("type");
		
		SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
		model.addAttribute("loginUser", loginUser);
		model.addAttribute("type", type);
		return "security/user/pwdChange";
	}
	
	@RequestMapping(value = "/pwdChange", method = { RequestMethod.GET, RequestMethod.POST })
	public String pwdChange(HttpServletRequest req,SecurityUser securityUser, Model model){
		String type = req.getParameter("type")==null? "info":req.getParameter("type");
		SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
		
		if(type.equals("info")){
			if(securityUser.getEmail()!=null){
				loginUser.setEmail(securityUser.getEmail());
			}
			if(securityUser.getRealname()!=null){
				loginUser.setRealname(securityUser.getRealname());
			}
			if(securityUser.getPhone()!=null){
				loginUser.setPhone(securityUser.getPhone());
			}
			
			try{
				securityUserService.update(loginUser);
				model.addAttribute("loginUser", loginUser);
				model.addAttribute("type", type);
				model.addAttribute("resultCode", "1");
				model.addAttribute("resultMsg", "个人信息修改成功");
			}catch(Exception e){
				model.addAttribute("loginUser", loginUser);
				model.addAttribute("type", type);
				model.addAttribute("resultCode", "0");
				model.addAttribute("resultMsg", "个人信息修改失败");
			}
		}
		else{
			String newPwd = req.getParameter("newPwd");
			String oldPwd = req.getParameter("oldPwd");
			//先验证原密码
			boolean valid = securityUserService.validateUsernameAndPwd(loginUser.getUsername(), oldPwd);
			if(valid){
				loginUser.setPassword(newPwd);
				passwordHelper.encryptPassword(loginUser);
				
				try{
					securityUserService.update(loginUser);
					model.addAttribute("type", type);
					model.addAttribute("resultCode", "1");
					model.addAttribute("resultMsg", "密码修改成功");
				}catch(Exception e){
					model.addAttribute("type", type);
					model.addAttribute("resultCode", "0");
					model.addAttribute("resultMsg", "密码修改失败");
				}
			}
			else{
				model.addAttribute("type", type);
				model.addAttribute("resultCode", "0");
				model.addAttribute("resultMsg", "原密码错误");
			}
		}
		
		return "security/user/pwdChange";
	}
	
	@RequestMapping(value = "/stopOrStartUser", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String stopOrStartUser(HttpServletRequest req, Model model){
		String status = req.getParameter("status");
		String id = req.getParameter("id");
		ObjectMapper objectMapper = new ObjectMapper();
		
		if("1".equals(status)){
			try {
				SecurityUser loginUser = securityUserService.getById(id);
				loginUser.setStatus("enabled");
				securityUserService.update(loginUser);
				model.addAttribute("resultCode", "1");
				model.addAttribute("resultMsg", "启用用户成功");
				return objectMapper.writeValueAsString(model);
			} catch (Exception e) {
				return null;
			}
		}
		else{
			try {
				SecurityUser loginUser = securityUserService.getById(id);
				loginUser.setStatus("disabled");
				securityUserService.update(loginUser);
				model.addAttribute("resultCode", "1");
				model.addAttribute("resultMsg", "停用用户成功");
				return objectMapper.writeValueAsString(model);
			} catch (Exception e) {
				return null;
			}
		}
	}
	
	
}

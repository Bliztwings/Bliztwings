package com.ehyf.ewashing.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.ehyf.ewashing.common.Constants;
import com.ehyf.ewashing.entity.SecurityUser;
import com.ehyf.ewashing.entity.SendReceivePerson;
import com.ehyf.ewashing.service.SendReceivePersonService;
import com.ehyf.ewashing.shiro.credentials.PasswordHelper;
import com.ehyf.ewashing.util.DESEncryption;
import com.ehyf.ewashing.util.UUID;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 小e 管理
 * 
 * @author shenxiaozhong
 *
 */
@Controller
@RequestMapping("/sendReceiver")
public class SendReceivePersonController {

	@Autowired
	private SendReceivePersonService service;
	
	@Autowired
	private PasswordHelper passwordHelper;

	private static Logger logger = Logger.getLogger(SendReceivePersonController.class);

	/**
	 * 初始化增加小E
	 * 
	 * @param req
	 * @param ewashingStore
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/initAdd", method = { RequestMethod.POST, RequestMethod.GET })
	public String initAdd(HttpServletRequest req, SendReceivePerson sendReceivePerson, HttpSession session,
			Model model) {
		return "ewashing/o2o/addReceicer";
	}

	/**
	 * 删除小E
	 * 
	 * @param req
	 * @param sendReceivePerson
	 *            小E信息
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/deleteReceicer", method = { RequestMethod.POST })
	@ResponseBody
	public String deleteReceicer(HttpServletRequest req, SendReceivePerson sendReceivePerson, HttpSession session,
			Model model) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			model.asMap().clear();
			String id = req.getParameter("id");
			service.deleteById(id);
			model.addAttribute("resultCode", "1");
			model.addAttribute("resultMsg", "小E删除成功");
			return objectMapper.writeValueAsString(model);
		} catch (Exception e) {
			logger.error(e.getMessage());
			model.addAttribute("resultCode", "0");
			model.addAttribute("resultMsg", "小E删除失败");
			return JSONObject.toJSONString(model);
		}

	}

	/**
	 * 更新小E
	 * 
	 * @param req
	 * @param sendReceivePerson
	 *            小E信息
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/update", method = { RequestMethod.POST })
	public String update(HttpServletRequest req, SendReceivePerson sendReceivePerson, HttpSession session,
			Model model) {
		model.asMap().clear();
		try {
			SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
			sendReceivePerson.setUpdateUserId(loginUser.getId());
			sendReceivePerson.setUpdateUser(loginUser.getRealname());
			sendReceivePerson.setUpdateTime(new Date());
			int count = service.update(sendReceivePerson);
			if (count <= 0) {
				model.addAttribute("resultCode", "0");
				model.addAttribute("resultMsg", "小E修改失败");
			} else {
				model.addAttribute("resultCode", "1");
				model.addAttribute("resultMsg", "小E修改成功");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			model.addAttribute("resultCode", "0");
			model.addAttribute("resultMsg", "小E修改失败");
			return JSONObject.toJSONString(model);
		}
		return "ewashing/o2o/preUpdate";
	}

	/**
	 * 获取小E 列表
	 * 
	 * @param req
	 * @param sendReceivePerson
	 *            小E信息
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list", method = { RequestMethod.POST, RequestMethod.GET })
	public String list(HttpServletRequest req, SendReceivePerson sendReceivePerson, HttpSession session, Model model) {

		String pageNum = req.getParameter("pageNum");
		String pageSize = req.getParameter("pageSize");

		try {
			if (StringUtils.isBlank(pageNum)) {
				pageNum = "1";
			}
			if (StringUtils.isBlank(pageSize)) {
				pageSize = "10";
			}
			PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize));
			List<SendReceivePerson> list = service.findList(sendReceivePerson);
			PageInfo<SendReceivePerson> page = new PageInfo<SendReceivePerson>(list);
			model.addAttribute("page", page);
			return "ewashing/o2o/list";
		} catch (Exception e) {
			logger.error("查询小E信息失败。", e);
			return null;
		}
	}

	/**
	 * 更新小E
	 * @param req
	 * @param sendReceivePerson
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/preUpdate", method = { RequestMethod.GET })
	public String preUpdate(HttpServletRequest req, SendReceivePerson sendReceivePerson, HttpSession session, Model model) {

		/** 1: 成功 ，0： 失败 */
		int resultCode = 1;
		String resultMsg = "";
		model.asMap().clear();
		String id = req.getParameter("id");
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			if (StringUtils.isBlank(id)) {
				resultCode = 0;
				resultMsg = "需要修改的主键为空，为非法参数";
				model.addAttribute("resultCode", resultCode);
				model.addAttribute("resultMsg", resultMsg);
				return objectMapper.writeValueAsString(model);
			}
			else
			{
				SendReceivePerson person = service.getById(id);
				model.addAttribute("person", person);
				return "ewashing/o2o/preUpdate";
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	/**
	 * 保存小E 信息
	 * 
	 * @param req
	 * @param sendReceivePerson
	 *            小E信息
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/saveSendReceivePerson", method = { RequestMethod.POST })
	@ResponseBody
	public String saveSendReceivePerson(HttpServletRequest req, SendReceivePerson sendReceivePerson,
			HttpSession session, Model model) {

		SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
		sendReceivePerson.setId(UUID.getUUID32());
		sendReceivePerson.setCreateTime(new Date());
		sendReceivePerson.setCreateUserId(loginUser.getId());
		sendReceivePerson.setCreateUser(loginUser.getUsername());
		ObjectMapper objectMapper = new ObjectMapper();
		model.asMap().clear();
		try {
			// 密文，需要解码
			String password =DESEncryption.decrypt(sendReceivePerson.getPassword(), sendReceivePerson.getMobile());
			// 加密密码
			SecurityUser user =new SecurityUser();
			user.setPassword(sendReceivePerson.getPassword());
			user.setUsername(sendReceivePerson.getMobile());
			sendReceivePerson.setPassword(passwordHelper.getEncryptPassword(user, password));
			
			// 判断是否存在手机号
			SendReceivePerson sp =new SendReceivePerson();
			sp.setMobile(sendReceivePerson.getMobile());
			List<SendReceivePerson> list =service.findList(sp);
			if(!CollectionUtils.isEmpty(list)){
				model.addAttribute("resultMsg", "手机号重复！");
				model.addAttribute("resultCode", 0);
				return objectMapper.writeValueAsString(model);
			}
			int result =service.insert(sendReceivePerson);
			if (result<=0){
				model.addAttribute("resultMsg", "添加小E失败！");
				model.addAttribute("resultCode", 0);
			}
			else {
				model.addAttribute("resultMsg", "添加小E成功！");
				model.addAttribute("resultCode", 1);
			}
			return objectMapper.writeValueAsString(model);
		} catch (Exception e) {
			model.addAttribute("resultMsg", "添加小E失败！");
			model.addAttribute("resultCode", 0);
		}
		return "ewashing/o2o/addReceicer";
	}

}

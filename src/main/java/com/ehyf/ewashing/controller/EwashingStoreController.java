package com.ehyf.ewashing.controller;

import java.util.Calendar;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.ehyf.ewashing.common.Constants;
import com.ehyf.ewashing.entity.EwashingStore;
import com.ehyf.ewashing.entity.EwashingStoreUser;
import com.ehyf.ewashing.entity.SecurityUser;
import com.ehyf.ewashing.service.EwashingStoreService;
import com.ehyf.ewashing.util.UUID;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 门店管理服务
 * 
 * @author shenxiaozhong
 *
 */
@Controller
@RequestMapping("/store")
public class EwashingStoreController {

	@Autowired
	private EwashingStoreService ewashingService;
	private static Logger logger = Logger.getLogger(EwashingStoreController.class);

	/**
	 * 初始化新增门店
	 * 
	 * @param req
	 * @param ewashingStore
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/initAdd", method = { RequestMethod.POST, RequestMethod.GET })
	public String initAdd(HttpServletRequest req, EwashingStore ewashingStore, HttpSession session, Model model) {
		return "ewashing/store/addStore";
	}

	@RequestMapping(value = "/setUsers", method = { RequestMethod.POST, RequestMethod.GET })
	public String setUsers(HttpServletRequest req, EwashingStoreUser ewashingStoreUser, HttpSession session,
			Model model) {

		model.asMap().clear();
		String id = req.getParameter("id");
		// 获取门店
		EwashingStore store = ewashingService.getById(id);
		model.addAttribute("store", store);
		return "ewashing/store/setUsers";
	}

	@RequestMapping(value = "/update", method = { RequestMethod.POST })
	public String update(HttpServletRequest req, EwashingStore store, HttpSession session, Model model) {
		model.asMap().clear();
		try {
			SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
			store.setUpdateUserId(loginUser.getId());
			store.setUpdateUserName(loginUser.getRealname());
			int count =ewashingService.update(store);
			if(count<=0){
				model.addAttribute("resultCode", "0");
				model.addAttribute("resultMsg", "门店修改失败");
			}
			else{
				model.addAttribute("resultCode", "1");
				model.addAttribute("resultMsg", "门店修改成功");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			model.addAttribute("resultCode", "0");
			model.addAttribute("resultMsg", "门店修改失败");
			return JSONObject.toJSONString(model);
		}
		return "ewashing/store/updateStore";
	}

	@RequestMapping(value = "/deleteStore", method = { RequestMethod.POST })
	@ResponseBody
	public String deleteStore(HttpServletRequest req, EwashingStore store, HttpSession session, Model model) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			model.asMap().clear();
			String id = req.getParameter("id");
			ewashingService.deleteById(id);
			model.addAttribute("resultCode", "1");
			model.addAttribute("resultMsg", "门店删除成功");
			return objectMapper.writeValueAsString(model);
		} catch (Exception e) {
			logger.error(e.getMessage());
			model.addAttribute("resultCode", "0");
			model.addAttribute("resultMsg", "门店删除失败");
			return JSONObject.toJSONString(model);
		}

	}

	/**
	 * 修改门店
	 * 
	 * @param req
	 * @param ewashingStore
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/preUpdate", method = { RequestMethod.GET })
	public String preUpdate(HttpServletRequest req, EwashingStore ewashingStore, HttpSession session, Model model) {

		/** 1: 成功 ，0： 失败 */
		int resultCode = 1;
		String resultMsg = "";
		model.asMap().clear();
		String id = req.getParameter("id");
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			if (StringUtils.isBlank(id)) {
				SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
				if (loginUser.getEwashingStore() ==null){
					resultCode = 0;
					resultMsg = "需要修改的主键为空，为非法参数";
					model.addAttribute("resultCode", resultCode);
					model.addAttribute("resultMsg", resultMsg);
					return objectMapper.writeValueAsString(model);
				}
				else
				{
					EwashingStore store = ewashingService.getById(loginUser.getEwashingStore().getId());
					model.addAttribute("store", store);
					return "ewashing/store/updateStore";
				}
			}
			else
			{
				EwashingStore store = ewashingService.getById(id);
				model.addAttribute("store", store);
				return "ewashing/store/updateStore";
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@RequestMapping(value = "/checkPrimaryKey", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String checkPrimaryKey(HttpServletRequest req, EwashingStore ewashingStore, Model model) {
		/** 1: 成功 ，0： 失败 */
		int resultCode = 1;
		String resultMsg = "成功！";
		model.asMap().clear();
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			String id = req.getParameter("id");
			logger.info("id = " + id);
			if (StringUtils.isBlank(id)) {
				logger.error("需要修改的主键为空，非法参数");
				resultCode = 0;
				resultMsg = "需要修改的主键为空，非法参数";
				model.addAttribute("resultCode", resultCode);
				model.addAttribute("resultMsg", resultMsg);
				return objectMapper.writeValueAsString(model);
			}
			ewashingStore = ewashingService.getById(id);
			if (null == ewashingStore) {
				logger.error("对象不存在，非法参数");
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
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 保存门店
	 * 
	 * @param req
	 * @param ewashingStore
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/saveStore", method = { RequestMethod.POST })
	public String saveStore(HttpServletRequest req, EwashingStore store, HttpSession session, Model model) {

		SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
		store.setId(UUID.getUUID32());
		store.setStoreCode("WH" + Calendar.getInstance().getTimeInMillis());
		store.setCreateDate(new Date());
		store.setCreateUserId(loginUser.getId());
		store.setCreateUserName(loginUser.getUsername());
		// 正常
		store.setStoreStatus("1");
		// 直营
		store.setStoreType("1");
		try {
			ewashingService.insert(store);
			model.addAttribute("resultMsg", "添加门店成功！");
			model.addAttribute("resultCode", 1);
		} catch (Exception e) {
			logger.error(e.getMessage());
			model.addAttribute("resultMsg", "添加门店失败！");
			model.addAttribute("resultCode", 0);
			return JSONObject.toJSONString(model);
		}
		return "ewashing/store/addStore";
	}

	/**
	 * 获取所有门店
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list", method = { RequestMethod.POST, RequestMethod.GET })
	public String list(HttpServletRequest req, EwashingStore ewashingStore, HttpSession session, Model model) {

		String pageNum = req.getParameter("pageNum");
		String pageSize = req.getParameter("pageSize");
		String storeCode = req.getParameter("storeCode");
		String storeName = req.getParameter("storeName");

		try {
			if (StringUtils.isBlank(pageNum)) {
				pageNum = "1";
			}
			if (StringUtils.isBlank(pageSize)) {
				pageSize = "10";
			}
			PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize));
			List<EwashingStore> list = ewashingService.findList(ewashingStore);
			PageInfo<EwashingStore> page = new PageInfo<EwashingStore>(list);

			model.addAttribute("page", page);
			model.addAttribute("storeCode", storeCode);
			model.addAttribute("storeName", storeName);
			return "ewashing/store/list";
		} catch (Exception e) {
			logger.error("查询门店信息失败。", e);
			return null;
		}
	}

}

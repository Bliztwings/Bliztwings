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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.ehyf.ewashing.common.Constants;
import com.ehyf.ewashing.entity.EwashingStore;
import com.ehyf.ewashing.entity.HandOnArea;
import com.ehyf.ewashing.entity.HandOnNo;
import com.ehyf.ewashing.entity.SecurityUser;
import com.ehyf.ewashing.service.EwashingHandonAreaService;
import com.ehyf.ewashing.service.EwashingHandonNoService;
import com.ehyf.ewashing.service.EwashingStoreService;
import com.ehyf.ewashing.util.UUID;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("/handon")
public class EwashingHandonController {

	@Autowired
	private EwashingHandonAreaService handonArea;
	@Autowired
	private EwashingHandonNoService handonNo;
	
	@Autowired
	private EwashingStoreService storeService;

	private static Logger logger = Logger.getLogger(EwashingHandonController.class);

	
	/**
	 * 挂衣区
	 * @param req
	 * @param handOnArea
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/area", method = { RequestMethod.POST, RequestMethod.GET })
	public String area(HttpServletRequest req, HandOnArea handOnArea, HttpSession session, Model model) {
		
		SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
		
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
			handOnArea.setHandType(loginUser.getUserType());
			if(loginUser.getEwashingStore()!=null){
				handOnArea.setStoreId(loginUser.getEwashingStore().getId());
			}
			List<HandOnArea> list = handonArea.findList(handOnArea);
			PageInfo<HandOnArea> page = new PageInfo<HandOnArea>(list);
			model.addAttribute("page", page);
			return "ewashing/handon/area";
		} catch (Exception e) {
			logger.error("查询挂衣区信息失败。", e);
			return null;
		}
	}

	
	@RequestMapping(value = "/initAdd", method = { RequestMethod.POST, RequestMethod.GET })
	public String initAdd(HttpServletRequest req, HandOnArea handOnArea, HttpSession session, Model model) {
		
		// 获取门店
		List<EwashingStore> list =storeService.findList( new EwashingStore());
		SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
		model.addAttribute("userType", loginUser.getUserType());
		model.addAttribute("list", list);
		return "ewashing/handon/addHandOnArea";
	}
	
	
	@RequestMapping(value = "/initAddHandOnNo", method = { RequestMethod.POST, RequestMethod.GET })
	public String initAddHandOnNo(HttpServletRequest req, HandOnNo handOnNo, HttpSession session, Model model) {
		
		String handType =req.getParameter("handType");
		SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
		model.addAttribute("userType", loginUser.getUserType());
		
		if(!com.ehyf.ewashing.util.StringUtils.isEmptyString(handType)){
			// 获取挂衣区
			HandOnArea area = new HandOnArea();
			area.setHandType(handType);
			
			if(loginUser.getEwashingStore()!=null){
				area.setStoreId(loginUser.getEwashingStore().getId());
			}
			List<HandOnArea> list =handonArea.findList(area);
			model.addAttribute("handonAreaList", list);	
		}
		if(loginUser.getEwashingStore()!=null){
			model.addAttribute("storeId", loginUser.getEwashingStore().getId());	
		}
		
		return "ewashing/handon/addHandOnNo";
	}

	@RequestMapping(value = "/saveHandOnArea", method = { RequestMethod.POST })
	public String saveHandOnArea(HttpServletRequest req, HandOnArea handOnArea, HttpSession session, Model model) {
		try{
			SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
			handOnArea.setId(UUID.getUUID32());
			handOnArea.setCreateDate(new Date());
			if(loginUser.getEwashingStore()!=null){
				handOnArea.setStoreId(loginUser.getEwashingStore().getId());
			}
			handOnArea.setCreateUserId(loginUser.getId());
			handOnArea.setCreateUserName(loginUser.getUsername());
			boolean flag =handonArea.HandOnArea(handOnArea);
			if(flag){
				model.addAttribute("resultCode", "1");	
				model.addAttribute("resultMsg", "设置挂衣区成功");
			}else{
				model.addAttribute("resultCode", "0");	
				model.addAttribute("resultMsg", "设置挂衣区失败");
			}
			
		}catch (Exception e) {
			model.addAttribute("resultCode", "0");	
			model.addAttribute("resultMsg", "设置挂衣区失败");
		}
		return "ewashing/handon/addHandOnArea";			
	}
	
	@RequestMapping(value = "/saveHandOnNo", method = { RequestMethod.POST })
	@ResponseBody
	public String saveHandOnNo(HttpServletRequest req, HandOnNo handOnNo, HttpSession session, Model model) {
		
		ObjectMapper objectMapper = new ObjectMapper();
		try{
			
			model.asMap().clear();
			int handOnNoBegin =Integer.parseInt(req.getParameter("handOnNoBegin"));
			int handOnNoEnd =Integer.parseInt(req.getParameter("handOnNoEnd"));
			SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
			handOnNo.setId(UUID.getUUID32());
			handOnNo.setCreateDate(new Date());
			handOnNo.setCreateUserId(loginUser.getId());
			if(loginUser.getEwashingStore()!=null){
				handOnNo.setStoreId(loginUser.getEwashingStore().getId());
			}
			handOnNo.setCreateUserName(loginUser.getUsername());
			handOnNo.setStatus("0");
			boolean flag =handonNo.insertHandOnNo(loginUser,handOnNoBegin,handOnNoEnd,handOnNo);
			if(flag){
				model.addAttribute("resultCode", "1");	
				model.addAttribute("resultMsg", "设置挂衣号成功");
			}else{
				model.addAttribute("resultCode", "0");	
				model.addAttribute("resultMsg", "设置挂衣号失败");	
			}
			return objectMapper.writeValueAsString(model);
		}catch (Exception e) {
			model.addAttribute("resultCode", "0");	
			model.addAttribute("resultMsg", e.getMessage());
			return JSONObject.toJSONString(model);
		}
	}
	
	
	@RequestMapping(value = "/deleteHandonArea",method = {RequestMethod.POST})
	@ResponseBody
	public String deleteHandonArea(HttpServletRequest req, HandOnArea handOnArea,HttpSession session,
			Model model) {
		ObjectMapper objectMapper = new ObjectMapper();
		try{
			model.asMap().clear();
			String id = req.getParameter("id");
			handonArea.deleteById(id);
			model.addAttribute("resultCode", "1");	
			model.addAttribute("resultMsg", "挂衣区删除成功");	
			return objectMapper.writeValueAsString(model);
		}
		catch(Exception e){
			model.addAttribute("resultCode", "0");	
			model.addAttribute("resultMsg", "挂衣区删除失败");
			return JSONObject.toJSONString(model);
		}
		
	}
	
	@RequestMapping(value = "/deleteHandonNo",method = {RequestMethod.POST})
	@ResponseBody
	public String deleteHandonNo(HttpServletRequest req, HandOnArea handOnArea,HttpSession session,
			Model model) {
		ObjectMapper objectMapper = new ObjectMapper();
		try{
			model.asMap().clear();
			String id = req.getParameter("id");
			handonNo.deleteById(id);
			model.addAttribute("resultCode", "1");	
			model.addAttribute("resultMsg", "挂衣号删除成功");	
			return objectMapper.writeValueAsString(model);
		}
		catch(Exception e){
			model.addAttribute("resultCode", "0");	
			model.addAttribute("resultMsg", "挂衣号删除失败");
			return JSONObject.toJSONString(model);
		}
	}
	
	
	
	
	/**
	 * 挂衣号
	 * @param req
	 * @param handOnNo
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/no", method = { RequestMethod.POST, RequestMethod.GET })
	public String no(HttpServletRequest req, HandOnNo handOnNo, HttpSession session, Model model) {

		SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
		String pageNum = req.getParameter("pageNum");
		String pageSize = req.getParameter("pageSize");
		String area =req.getParameter("handonArea");
		try {
			if (StringUtils.isBlank(pageNum)) {
				pageNum = "1";
			}
			if (StringUtils.isBlank(pageSize)) {
				pageSize = "10";
			}
			PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize));
			if(area==null){
				handOnNo.setHandonArea("0");
			}
			else
			{
				handOnNo.setHandonArea(area);
			}
			
			handOnNo.setHandType(loginUser.getUserType());
			
			if(loginUser.getEwashingStore()!=null){
				handOnNo.setStoreId(loginUser.getEwashingStore().getId());
			}
			
			List<HandOnNo> list = handonNo.findList(handOnNo);
			
			HandOnArea areaQuery=new HandOnArea();
			areaQuery.setHandType(loginUser.getUserType());
			if(loginUser.getEwashingStore()!=null){
				areaQuery.setStoreId(loginUser.getEwashingStore().getId());
			}
			List<HandOnArea> areaList =handonArea.findList(areaQuery);
			PageInfo<HandOnNo> page = new PageInfo<HandOnNo>(list);
			model.addAttribute("page", page);
			model.addAttribute("handOnNo", handOnNo.getHandOnNo());
			model.addAttribute("area", area);
			model.addAttribute("areaList", areaList);
			return "ewashing/handon/no";
		} catch (Exception e) {
			logger.error("查询挂衣区信息失败。", e);
			return null;
		}
	}
}

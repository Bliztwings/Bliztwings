package com.ehyf.ewashing.controller;

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
import com.ehyf.ewashing.entity.SecurityUser;
import com.ehyf.ewashing.entity.SendReceivePerson;
import com.ehyf.ewashing.entity.StoreClothes;
import com.ehyf.ewashing.entity.StoreOrder;
import com.ehyf.ewashing.service.EwashingStoreBusinessService;
import com.ehyf.ewashing.service.EwashingStoreOrderService;
import com.ehyf.ewashing.service.SendReceivePersonService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 收送人员订单管理
 * 
 * @author shenxiaozhong
 *
 */
@Controller
@RequestMapping("/o2oOrder")
public class SendReceivePersonOrderController {

	private static Logger logger = Logger.getLogger(SendReceivePersonOrderController.class);
	
	@Autowired
	private EwashingStoreOrderService orderService;

	@Autowired
	private SendReceivePersonService sendService;
	
	@Autowired
	private EwashingStoreBusinessService storeBusiness;

	/**
	 * 收送人员订单分配初始化页面
	 * 待取订单
	 * 
	 * @param req
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list", method = { RequestMethod.POST, RequestMethod.GET })
	public String list(HttpServletRequest req, HttpSession session, Model model) {

		String pageNum = req.getParameter("pageNum");
		String pageSize = req.getParameter("pageSize");
		String orderCode =req.getParameter("orderCode");
		String mobilePhone =req.getParameter("mobilePhone");
		String distributeStatus =req.getParameter("distributeStatus");
		try {
			if (StringUtils.isBlank(pageNum)) {
				pageNum = "1";
			}
			if (StringUtils.isBlank(pageSize)) {
				pageSize = "10";
			}
			PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize));
			StoreOrder order = new StoreOrder();
			order.setOrderType("2");
			order.setOrderStatus("20");
			//order.setDistributeStatus("0");
			
			if(null !=distributeStatus && !"".equals(distributeStatus)){
				order.setDistributeStatus(distributeStatus);
			}
			
			order.setOrderCode(orderCode);
			order.setMobilePhone(mobilePhone);
			List<StoreOrder> list = orderService.findO2oOrderList(order);
			PageInfo<StoreOrder> page = new PageInfo<StoreOrder>(list);
			// 获取小E信息
			SendReceivePerson send =new SendReceivePerson();
			//send.setUserType("1");
			List<SendReceivePerson> sendUserList = sendService.findList(send);
			
			model.addAttribute("orderCode", orderCode);
			model.addAttribute("mobilePhone", mobilePhone);
			model.addAttribute("distributeStatus", distributeStatus);

			model.addAttribute("sendUserList", sendUserList);
			model.addAttribute("page", page);
			return "ewashing/o2o/sendReceivePersonOrder";
		} catch (Exception e) {
			logger.error("查询O2O订单信息失败。", e);
			return null;
		}
	}
	
	
	/**
	 * 收送人员待送订单初始化页面
	 * 待送订单
	 * @param req
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/sendOrder", method = { RequestMethod.POST, RequestMethod.GET })
	public String sendOrder(HttpServletRequest req, HttpSession session, Model model) {

		String pageNum = req.getParameter("pageNum");
		String pageSize = req.getParameter("pageSize");
		String orderCode =req.getParameter("orderCode");
		String mobilePhone =req.getParameter("mobilePhone");
		String  sendDistributeStatus =req.getParameter("sendDistributeStatus");
		try {
			if (StringUtils.isBlank(pageNum)) {
				pageNum = "1";
			}
			if (StringUtils.isBlank(pageSize)) {
				pageSize = "10";
			}
			PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize));
			StoreOrder order = new StoreOrder();
			order.setOrderType("2");
			order.setOrderStatus("6");
			order.setOrderCode(orderCode);
			if(null !=sendDistributeStatus && !"".equals(sendDistributeStatus)){
				order.setSendDistributeStatus(sendDistributeStatus);
			}
			
			order.setMobilePhone(mobilePhone);
			List<StoreOrder> list = orderService.findO2oOrderList(order);
			PageInfo<StoreOrder> page = new PageInfo<StoreOrder>(list);
			// 获取小E信息
			SendReceivePerson send = new SendReceivePerson();
			//send.setUserType("2");// 收衣人员
			List<SendReceivePerson> sendUserList = sendService.findList(send);
			
			model.addAttribute("orderCode", orderCode);
			model.addAttribute("mobilePhone", mobilePhone);
			model.addAttribute("sendDistributeStatus", sendDistributeStatus);
			
			model.addAttribute("sendUserList", sendUserList);
			model.addAttribute("page", page);
			return "ewashing/o2o/sendOrder";
		} catch (Exception e) {
			logger.error("查询O2O订单信息失败。", e);
			return null;
		}
	}
	
	
	
	/**
	 * o2o 待打印订单信息
	 * @param req
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/printOrder", method = { RequestMethod.POST, RequestMethod.GET })
	public String printOrder(HttpServletRequest req, HttpSession session, Model model) {

		String pageNum = req.getParameter("pageNum");
		String pageSize = req.getParameter("pageSize");
		String orderCode =req.getParameter("orderCode");
		String mobilePhone =req.getParameter("mobilePhone");
		try {
			if (StringUtils.isBlank(pageNum)) {
				pageNum = "1";
			}
			if (StringUtils.isBlank(pageSize)) {
				pageSize = "10";
			}
			PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize));
			StoreOrder order = new StoreOrder();
			order.setOrderType("2");
			order.setOrderCode(orderCode);
			order.setMobilePhone(mobilePhone);
			List<StoreOrder> list = orderService.findO2oOrderList(order);
			PageInfo<StoreOrder> page = new PageInfo<StoreOrder>(list);
			model.addAttribute("orderCode", orderCode);
			model.addAttribute("mobilePhone", mobilePhone);
			model.addAttribute("page", page);
			return "ewashing/o2o/printOrder";
		} catch (Exception e) {
			logger.error("查询O2O订单信息失败。", e);
			return null;
		}
	}

	
	
	@RequestMapping(value = "/revoke", method = { RequestMethod.POST })
	@ResponseBody
	public String revoke(HttpServletRequest req,HttpSession session,
			Model model) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			model.asMap().clear();
			String id = req.getParameter("id");
			String type = req.getParameter("type");
			boolean flag =sendService.revoke(id,type);
			if(flag){
				model.addAttribute("resultCode", "1");
				model.addAttribute("resultMsg", "撤回配送订单成功");
			}else{
				model.addAttribute("resultCode", "0");
				model.addAttribute("resultMsg", "撤回配送订单失败");
			}
			return objectMapper.writeValueAsString(model);
		} catch (Exception e) {
			logger.error(e.getMessage());
			model.addAttribute("resultCode", "0");
			model.addAttribute("resultMsg", "撤回配送订单失败");
			return JSONObject.toJSONString(model);
		}

	}

	
	/**
	 * O2O分配订单
	 * 
	 * @param req
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/distribute", method = { RequestMethod.POST })
	@ResponseBody
	public String distribute(HttpServletRequest req, HttpSession session, Model model) {

		SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			model.asMap().clear();
			String ids = req.getParameter("ids");
			String sendUserId =req.getParameter("sendUserId");
			// 1：取，2：送
			String type =req.getParameter("type");
			if (ids == null || "".equals(ids)) {
				model.addAttribute("resultCode", "0");
				model.addAttribute("resultMsg", "分配衣服失败");
				return JSONObject.toJSONString(model);
			}
			if (ids.endsWith(",")) {
				ids = ids.substring(0, ids.length() - 1);
			}

			boolean flag = orderService.distribute(ids, loginUser,sendUserId,type);

			if (!flag) {
				model.addAttribute("resultCode", "0");
				model.addAttribute("resultMsg", "分配衣服失败");
				return JSONObject.toJSONString(model);
			}
			model.addAttribute("resultCode", "1");
			model.addAttribute("resultMsg", "分配衣服成功");
			return objectMapper.writeValueAsString(model);

		} catch (Exception e) {
			model.addAttribute("resultCode", "0");
			model.addAttribute("resultMsg", "分配衣服失败");
			return JSONObject.toJSONString(model);
		}
	}
	
	
	@RequestMapping(value = "/printLogistics", method = { RequestMethod.POST, RequestMethod.GET })
	public String printLogistics(HttpServletRequest req, HttpSession session,Model model) {

		String orderId = req.getParameter("orderId");
		//SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
		// 获取订单信息，门店信息
		StoreOrder order =orderService.getById(orderId);
		// 获取衣服信息
		StoreClothes clothes =new StoreClothes();
		clothes.setOrderCode(order.getOrderCode());
		List<StoreClothes> clothesList =storeBusiness.findClothes(clothes);
		model.addAttribute("clothesList", clothesList);
		model.addAttribute("order", order);
		model.addAttribute("orderStr", order.getOrderCode()+","+order.getId()+","+order.getMobilePhone());
		return "ewashing/o2o/printLogistics";
	}
	
}

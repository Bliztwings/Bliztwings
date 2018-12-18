package com.ehyf.ewashing.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;
import com.ehyf.ewashing.entity.MemberCard;
import com.ehyf.ewashing.entity.StoreClothes;
import com.ehyf.ewashing.entity.StoreOrder;
import com.ehyf.ewashing.service.EwashingStoreBusinessService;
import com.ehyf.ewashing.service.MemberCardService;

/**
 * o2o 订单分拣
 * 
 * @author shenxiaozhong
 *
 */
@Controller
@RequestMapping("/sorting")
public class SortingOrderController {

	@Autowired
	private EwashingStoreBusinessService storeBusiness;
	
	@Autowired
	private MemberCardService memberCardService;
	
	private static Logger logger = Logger.getLogger(SortingOrderController.class);

	/**
	 * 订单分拣初始化页面
	 * @param req
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list", method = { RequestMethod.POST, RequestMethod.GET })
	public String list(HttpServletRequest req, HttpSession session, Model model) {

		try {
			return "ewashing/o2o/sorting";
		} catch (Exception e) {
			logger.error("查询O2O订单信息失败。", e);
			return null;
		}
	}
	
	/**
	 * 根据封签号查询订单
	 * @param req
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/queryOrder", method = { RequestMethod.POST, RequestMethod.GET })
	public String queryOrder(HttpServletRequest req, HttpSession session, Model model) {
		String queryKey = req.getParameter("queryKey");
		model.asMap().clear();
		try {
			
			// 订单信息
			StoreOrder order =storeBusiness.getOrderBySealNumber(queryKey);
			if(order !=null){
				model.addAttribute("order", order);
				
				// 会员信息
				MemberCard member = memberCardService.queryMemberByCardOrMobileForBacken(order.getAppId(),order.getMobilePhone());
				if (member != null) {
					model.addAttribute("memberCard", member);
				}
				
				// 会员衣服信息
				StoreClothes clothes = new StoreClothes();
				clothes.setOrderCode(order.getOrderCode());
				List<StoreClothes> list = storeBusiness.findClothes(clothes);
				if (!CollectionUtils.isEmpty(list)) {
					model.addAttribute("orderCode", list.get(0).getOrderCode());
					model.addAttribute("clothesCount", list.size());
					model.addAttribute("clothesList", list);
				}
			}
			model.addAttribute("queryKey", queryKey);
			model.addAttribute("resultCode", "1");
			model.addAttribute("resultMsg", "获取数据成功");
			return "ewashing/o2o/sorting";
		} catch (Exception e) {
			model.addAttribute("resultCode", "0");
			model.addAttribute("resultMsg", "获取数据失败");
			return JSONObject.toJSONString(model);
		}
	}

}

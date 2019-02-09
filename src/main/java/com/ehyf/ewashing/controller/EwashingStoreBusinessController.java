package com.ehyf.ewashing.controller;

import java.math.BigDecimal;
import java.text.DateFormat;
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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ehyf.ewashing.common.Constants;
import com.ehyf.ewashing.emun.OrderStatus;
import com.ehyf.ewashing.entity.ClothesAttach;
import com.ehyf.ewashing.entity.ClothesPhoto;
import com.ehyf.ewashing.entity.EwashingDataDictionary;
import com.ehyf.ewashing.entity.EwashingStore;
import com.ehyf.ewashing.entity.HandOnArea;
import com.ehyf.ewashing.entity.HandOnNo;
import com.ehyf.ewashing.entity.MemberCard;
import com.ehyf.ewashing.entity.SecurityUser;
import com.ehyf.ewashing.entity.SendWashing;
import com.ehyf.ewashing.entity.StoreClothes;
import com.ehyf.ewashing.entity.StoreOrder;
import com.ehyf.ewashing.service.ClothesAttachService;
import com.ehyf.ewashing.service.EwashingDataDictionaryService;
import com.ehyf.ewashing.service.EwashingHandonAreaService;
import com.ehyf.ewashing.service.EwashingStoreBusinessService;
import com.ehyf.ewashing.service.EwashingStoreOrderService;
import com.ehyf.ewashing.service.EwashingStoreService;
import com.ehyf.ewashing.service.MemberCardService;
import com.ehyf.ewashing.util.DateUtil;
import com.ehyf.ewashing.util.PropertiesUtil;
import com.ehyf.ewashing.vo.StoreClothesVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 门店日常业务
 * 
 * @author chenxiaozhong
 *
 */
@Controller
@RequestMapping("/storeBusiness")
public class EwashingStoreBusinessController {

	@Autowired
	private EwashingHandonAreaService handonArea;

	@Autowired
	private MemberCardService memberCardService;
	
	@Autowired
	private EwashingStoreBusinessService storeBusiness;
	
	@Autowired
	private EwashingStoreOrderService storeOrder;
	
	@Autowired
	private ClothesAttachService clothesAttachService;
	@Autowired
	private EwashingDataDictionaryService ewashingDataService;
	@Autowired
	private EwashingStoreService storeService;
	

	private static PropertiesUtil propertiesUtil= new PropertiesUtil("config.properties");

	private static Logger logger = Logger.getLogger(EwashingStoreBusinessController.class);

	/**
	 * 收衣
	 * 
	 * @param req
	 * @param ewashingStoreUser
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/receive", method = { RequestMethod.POST, RequestMethod.GET })
	public String receive(HttpServletRequest req, HttpSession session, Model model) {

		
		return "ewashing/storeBusiness/receive";
	}
	
	
	/**
	 * 拍照功能
	 * @param req
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/takePhoto", method = { RequestMethod.POST, RequestMethod.GET })
	public String takePhoto(HttpServletRequest req, HttpSession session, Model model) {

		
		String tempUploadPath =propertiesUtil.getProperty("tempUploadPath");
		String uploadPath =propertiesUtil.getProperty("uploadPath");
		model.addAttribute("tempUploadPath", tempUploadPath);
		model.addAttribute("uploadPath", uploadPath);
		model.addAttribute("photoFromType", "2");//1：工厂   2：门店
		return "ewashing/storeBusiness/receiveClothesPhoto";
	}
	

	/**
	 * 添加衣物
	 * 
	 * @param req
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/saveClothes", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public String saveClothes(HttpServletRequest req, HttpSession session, StoreClothesVo storeClothesVo, Model model) {

		int barCodeAuto = 0;
		SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			model.asMap().clear();
			String barCode =storeClothesVo.getBarCode();
			barCode = "1";
			if(com.ehyf.ewashing.util.StringUtils.isEmptyString(barCode)){
				model.addAttribute("resultCode", "0");
				model.addAttribute("resultMsg", "条码不能为空");
				return JSONObject.toJSONString(model);
			}

			if(storeClothesVo.getSumAmount()==null || storeClothesVo.getSumAmount().compareTo(BigDecimal.ZERO)==0 || storeClothesVo.getSumAmount().compareTo(BigDecimal.ZERO)==-1){
				model.addAttribute("resultCode", "0");
				model.addAttribute("resultMsg", "金额不能为空");
				return JSONObject.toJSONString(model);
			}

			//查找衣服的最后一个记录的条码值
			StoreClothes c = new StoreClothes();
			c.setBarCode(barCode);

			List<StoreClothes> list =storeBusiness.findLastBarCode(c);
			if (CollectionUtils.isEmpty(list))
			{
				model.addAttribute("resultCode", "0");
				model.addAttribute("resultMsg", "保存失败，失败代码1013！");
				return JSONObject.toJSONString(model);
			}

			//条码加1
			barCodeAuto = list.get(0).getBarCodeAuto()+1;
			barCode = Long.toString(barCodeAuto);
			storeClothesVo.setBarCode(barCode);
			storeClothesVo.setBarCodeAuto(barCodeAuto);

//			List<StoreClothes> list =storeBusiness.findClothes(c);
//			if(!CollectionUtils.isEmpty(list)){
//				model.addAttribute("resultCode", "0");
//				model.addAttribute("resultMsg", "条码已经存在");
//				return JSONObject.toJSONString(model);
//			}

			boolean flag =storeBusiness.saveClothes(loginUser, storeClothesVo);
			if(flag){
				model.addAttribute("queryKey", storeClothesVo.getQueryKey());
				return objectMapper.writeValueAsString(model);
			}
			else{
				model.addAttribute("resultCode", "0");
				model.addAttribute("resultMsg", "收衣失败");
				return JSONObject.toJSONString(model);
			}
			
		} catch (Exception e) {
			model.addAttribute("resultCode", "0");
			model.addAttribute("resultMsg", e.getMessage());
			return JSONObject.toJSONString(model);
		}
	}
	
	
	/**
	 * 保存分拣衣服信息
	 * @param req
	 * @param session
	 * @param storeClothesVo
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/saveSoringClothes", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public String saveSoringClothes(HttpServletRequest req, HttpSession session, StoreClothesVo storeClothesVo, Model model) {

		int barCodeAuto = 0;
		SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			model.asMap().clear();
			String barCode =storeClothesVo.getBarCode();
            barCode = "1";
			if(com.ehyf.ewashing.util.StringUtils.isEmptyString(barCode)){
				model.addAttribute("resultCode", "0");
				model.addAttribute("resultMsg", "条码不能为空");
				return JSONObject.toJSONString(model);
			}
			
			if(storeClothesVo.getSumAmount()==null || storeClothesVo.getSumAmount().compareTo(BigDecimal.ZERO)==0 || storeClothesVo.getSumAmount().compareTo(BigDecimal.ZERO)==-1){
				model.addAttribute("resultCode", "0");
				model.addAttribute("resultMsg", "金额不能为空");
				return JSONObject.toJSONString(model);
			}

			//查找衣服的最后一个记录的条码值
			StoreClothes c = new StoreClothes();
			c.setBarCode(barCode);
			List<StoreClothes> list =storeBusiness.findLastBarCode(c);
            if (CollectionUtils.isEmpty(list))
            {
                model.addAttribute("resultCode", "0");
                model.addAttribute("resultMsg", "保存失败，失败代码1013！");
                return JSONObject.toJSONString(model);
            }
            //barCodeAuto = list.get(0).getBarCodeAuto();

			//条码加1
			barCodeAuto = list.get(0).getBarCodeAuto()+1;
			barCode = Long.toString(barCodeAuto);
            storeClothesVo.setBarCode(barCode);
            storeClothesVo.setBarCodeAuto(barCodeAuto);

			//存入数据库
			//StoreClothes c = new StoreClothes();
			//c.setBarCode(barCode);
			//List<StoreClothes> list =storeBusiness.findClothes(c);
			
//			if(!CollectionUtils.isEmpty(list)){
//				model.addAttribute("resultCode", "0");
//				model.addAttribute("resultMsg", "条码已经存在");
//				return JSONObject.toJSONString(model);
//			}

			boolean flag =storeBusiness.saveSoringClothes(loginUser, storeClothesVo);
			if(flag){
				model.addAttribute("queryKey", storeClothesVo.getQueryKey());
				return objectMapper.writeValueAsString(model);
			}
			else{
				model.addAttribute("resultCode", "0");
				model.addAttribute("resultMsg", "收衣失败");
				return JSONObject.toJSONString(model);
			}
			
		} catch (Exception e) {
			model.addAttribute("resultCode", "0");
			model.addAttribute("resultMsg", e.getMessage());
			return JSONObject.toJSONString(model);
		}
	}
	
	
	/**
	 * 更新衣服
	 * @param req
	 * @param session
	 * @param storeClothesVo
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/updateClothes", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public String updateClothes(HttpServletRequest req, HttpSession session, StoreClothesVo storeClothesVo, Model model) {

		SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			model.asMap().clear();
			String barCode =storeClothesVo.getBarCode();
			if(com.ehyf.ewashing.util.StringUtils.isEmptyString(barCode)){
				model.addAttribute("resultCode", "0");
				model.addAttribute("resultMsg", "条码不能为空");
				return JSONObject.toJSONString(model);
			}
			
			if(storeClothesVo.getSumAmount()==null || storeClothesVo.getSumAmount().compareTo(BigDecimal.ZERO)==0 || storeClothesVo.getSumAmount().compareTo(BigDecimal.ZERO)==-1){
				model.addAttribute("resultCode", "0");
				model.addAttribute("resultMsg", "金额不能为空");
				return JSONObject.toJSONString(model);
			}
			
			boolean flag =storeBusiness.updateClothes(loginUser, storeClothesVo);
			if(flag){
				model.addAttribute("queryKey", storeClothesVo.getQueryKey());
				return objectMapper.writeValueAsString(model);
			}
			else{
				model.addAttribute("resultCode", "0");
				model.addAttribute("resultMsg", "修改失败");
				return JSONObject.toJSONString(model);
			}
			
		} catch (Exception e) {
			model.addAttribute("resultCode", "0");
			model.addAttribute("resultMsg", e.getMessage());
			return JSONObject.toJSONString(model);
		}
	}
	
	
	/**
	 * 结账方式选择
	 * @param req
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/checkout", method = { RequestMethod.POST, RequestMethod.GET })
	public String checkout(HttpServletRequest req, HttpSession session,Model model) {

		String cardNumber = req.getParameter("cardNumber");
		String memberId = req.getParameter("memberId");
		String orderCode = req.getParameter("orderCode");
		String orderId = req.getParameter("orderId");
		// 0 已支付 1：欠款支付
		String arrears =req.getParameter("arrears");
		model.addAttribute("cardNumber", cardNumber);
		model.addAttribute("memberId", memberId);
		model.addAttribute("orderCode", orderCode);
		model.addAttribute("orderId", orderId);
		model.addAttribute("arrears", arrears);
		return "ewashing/storeBusiness/checkout";
	}
	
	/**
	 * 结算打印
	 * @param req
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/checkoutPrint", method = { RequestMethod.POST, RequestMethod.GET })
	public String checkoutPrint(HttpServletRequest req, HttpSession session,Model model) {

		String orderCode = req.getParameter("orderCode");
		SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
		String payType =req.getParameter("payType");
		// 获取订单信息，门店信息
		StoreOrder order =storeOrder.getStoreOrderByCode(orderCode);
		order.setPayGateWay(payType);
		// 获取衣服信息
		StoreClothes clothes =new StoreClothes();
		clothes.setOrderCode(orderCode);
		List<StoreClothes> clothesList =storeBusiness.findClothes(clothes);
		
		model.addAttribute("clothesList", clothesList);
		model.addAttribute("order", order);
		model.addAttribute("orderCode", orderCode);
		model.addAttribute("loginUserName", loginUser.getRealname());
		model.addAttribute("optDate", DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
		return "ewashing/storeBusiness/checkoutPrint";
	}
	
	
	/**
	 * 获取衣服照片
	 * @param req
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/showPhoto", method = { RequestMethod.POST, RequestMethod.GET })
	public String showPhoto(HttpServletRequest req, HttpSession session,Model model) {

		String clothesId = req.getParameter("clothesId");
		//SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
		List<ClothesPhoto> list =storeBusiness.queryPhotoList(clothesId);
		model.addAttribute("list", list);
		return "ewashing/storeBusiness/showPhoto";
	}
	
	
	
	
	/**
	 * 订单结算
	 * @param req
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/doPayment", method = { RequestMethod.POST })
	@ResponseBody
	public String doPayment(HttpServletRequest req, HttpSession session, Model model) {

		String cardNumber = req.getParameter("cardNumber");
		String memberId = req.getParameter("memberId");
		String orderCode = req.getParameter("orderCode");
		String orderId = req.getParameter("orderId");
		String payType = req.getParameter("payType");
		String arrears =req.getParameter("arrears");
		SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);

		ObjectMapper objectMapper = new ObjectMapper();
		try {
			model.asMap().clear();
			boolean flag = storeBusiness.doPayment(loginUser,cardNumber, memberId, orderCode, payType,arrears);
			if (!flag) {
				model.addAttribute("resultCode", "0");
				model.addAttribute("resultMsg", "支付失败");
				return JSONObject.toJSONString(model);
			}
			
			model.addAttribute("payType",payType);
			model.addAttribute("resultCode", "1");
			model.addAttribute("resultMsg", "支付成功");
			return objectMapper.writeValueAsString(model);
		} catch (Exception e) {
			model.addAttribute("resultCode", "0");
			model.addAttribute("resultMsg", "支付失败");
			return JSONObject.toJSONString(model);
		}
	}
	
	/**
	 * 欠费送洗
	 * @param req
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/arrearsWashing", method = { RequestMethod.POST })
	@ResponseBody
	public String arrearsWashing(HttpServletRequest req, HttpSession session, Model model) {

		/*String cardNumber = req.getParameter("cardNumber");
		String memberId = req.getParameter("memberId");*/
		String orderCode = req.getParameter("orderCode");
		SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);

		ObjectMapper objectMapper = new ObjectMapper();
		try {
			model.asMap().clear();
			boolean flag = storeBusiness.arrearsWashing(loginUser,orderCode);
			if (!flag) {
				model.addAttribute("resultCode", "0");
				model.addAttribute("resultMsg", "欠费洗衣失败");
				return JSONObject.toJSONString(model);
			}
			model.addAttribute("resultCode", "1");
			model.addAttribute("resultMsg", "欠费洗衣成功");
			return objectMapper.writeValueAsString(model);
		} catch (Exception e) {
			model.addAttribute("resultCode", "0");
			model.addAttribute("resultMsg", "欠费洗衣失败");
			return JSONObject.toJSONString(model);
		}
	}
	
	

	/**
	 * 删除未送洗衣服
	 * 
	 * @param req
	 * @param store
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/deleteClothes", method = { RequestMethod.POST })
	@ResponseBody
	public String deleteClothes(HttpServletRequest req, HttpSession session, Model model) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			model.asMap().clear();
			String id = req.getParameter("id");
			boolean flag = storeBusiness.deleteClothesById(id);
			if (!flag) {
				model.addAttribute("resultCode", "0");
				model.addAttribute("resultMsg", "送洗衣服删除失败");
				return JSONObject.toJSONString(model);
			}
			model.addAttribute("resultCode", "1");
			model.addAttribute("resultMsg", "送洗衣服删除成功");
			return objectMapper.writeValueAsString(model);
		} catch (Exception e) {
			model.addAttribute("resultCode", "0");
			model.addAttribute("resultMsg", "送洗衣服删除失败");
			return JSONObject.toJSONString(model);
		}
	}
	
	/**
	 * 删除O2O 分拣衣服
	 * @param req
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/deleteClothesO2o", method = { RequestMethod.POST })
	@ResponseBody
	public String deleteClothesO2o(HttpServletRequest req, HttpSession session, Model model) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			model.asMap().clear();
			String id = req.getParameter("id");
			boolean flag = storeBusiness.deleteClothesById(id);
			if (!flag) {
				model.addAttribute("resultCode", "0");
				model.addAttribute("resultMsg", "送洗衣服删除失败");
				return JSONObject.toJSONString(model);
			}
			model.addAttribute("resultCode", "1");
			model.addAttribute("resultMsg", "送洗衣服删除成功");
			return objectMapper.writeValueAsString(model);
		} catch (Exception e) {
			model.addAttribute("resultCode", "0");
			model.addAttribute("resultMsg", "送洗衣服删除失败");
			return JSONObject.toJSONString(model);
		}
	}

	/**
	 * 获取会员信息+会员未送洗衣服信息
	 * 
	 * @param req
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/queryMember", method = { RequestMethod.POST, RequestMethod.GET })
	public String queryMember(HttpServletRequest req, HttpSession session, Model model) {
		String queryKey = req.getParameter("queryKey");
		model.asMap().clear();

		EwashingStore ewashingStore;
		SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);

		//O2O收衣服时，让其门店对应"O2O门店"
		String id = "";
		String username = loginUser.getUsername();
		Boolean ret = username.equals("o2o");
		ewashingStore = loginUser.getEwashingStore();
		if(ewashingStore==null) id = "7cc64f488aa84f2d90f23f442362f83f";
		else id = loginUser.getEwashingStore().getId();

		try {
			// 会员信息
			model.addAttribute("queryKey", queryKey);
			MemberCard member = memberCardService.queryMemberByCardOrMobileForBacken(null, queryKey);
			if (member == null) {
				model.addAttribute("resultCode", "0");
				model.addAttribute("resultMsg", "会员不存在");
				return "ewashing/storeBusiness/receive";
			}
			// 会员衣服信息
			StoreClothes clothes = new StoreClothes();
			clothes.setQueryKey(member.getMemberId());
			clothes.setStatus("0");
			clothes.setStoreId(id);
			List<StoreClothes> list = storeBusiness.findClothesByMobileOrCardNumber(clothes);
			if (!CollectionUtils.isEmpty(list)) {
				model.addAttribute("orderCode", list.get(0).getOrderCode());
				model.addAttribute("orderId", list.get(0).getOrderId());
				model.addAttribute("clothesList", list);
				model.addAttribute("clothesCount", list.size());
			}
			
			model.addAttribute("memberCard", member);
			model.addAttribute("resultCode", "1");
			model.addAttribute("resultMsg", "获取数据成功");
			return "ewashing/storeBusiness/receive";
		} catch (Exception e) {
			model.addAttribute("resultCode", "0");
			model.addAttribute("resultMsg", "获取数据失败");
			return JSONObject.toJSONString(model);
		}
	}

	/**
	 * 查询门店收衣的列表，准备打印水洗唛
	 *
	 * @param req
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/queryStoreShuiXiMai")
	@ResponseBody
	public String queryStoreShuiXiMai(HttpServletRequest req,Model model)
	{
		String queryKey = req.getParameter("queryKey");
		model.asMap().clear();

		int i = 0;
		String OrderInfo = "";
		SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);

		//O2O收衣服时，让其门店对应"O2O门店"
		String id = "";
		String username = loginUser.getUsername();
		Boolean ret = username.equals("o2o");
		if (ret==true) id = "6c93ad2b66b74dfa95ce470ebb551c06";  //本地数据库
			//if (ret==true) id = "7cc64f488aa84f2d90f23f442362f83f";  //服务器数据
		else id = loginUser.getEwashingStore().getId();

		try {
			// 会员信息
			model.addAttribute("queryKey", queryKey);
			MemberCard member = memberCardService.queryMemberByCardOrMobileForBacken(null, queryKey);
			if (member == null) {
				model.addAttribute("resultCode", "0");
				model.addAttribute("resultMsg", "会员不存在");
				return "ewashing/storeBusiness/receive";
			}
			// 会员衣服信息
			StoreClothes clothes = new StoreClothes();
			clothes.setQueryKey(member.getMemberId());
			clothes.setStatus("0");
			clothes.setStoreId(id);
			List<StoreClothes> list = storeBusiness.findListReceiveClothes(clothes);
			//List<StoreClothes> list = storeBusiness.findClothesByMobileOrCardNumber(clothes);
			if (CollectionUtils.isEmpty(list))
			{
				return OrderInfo;
			}

			for (i = 0; i < list.size(); i++)
			{
				String strtakddate;
				Date takdate = list.get(i).getTakingDate();
				if(takdate==null)
				{
					list.get(i).setServiceType("");
					continue;
				};

				//日期格式，精确到日 2017-4-16
				DateFormat df1 = DateFormat.getDateInstance();
				strtakddate =df1.format(takdate);
				//用“服务类型”字段来放取衣日期
				list.get(i).setServiceType(strtakddate);
			}

			//把数组序列化成字符串
			//JSONArray json = JSONArray.fromObject(list);
			//OrderInfo = json.toString();
			OrderInfo = net.sf.json.JSONArray.fromObject(list).toString();
			return OrderInfo;
		}
		catch (Exception e) {
			return OrderInfo;
		}
	}

	@RequestMapping(value = "/showAttach", method = { RequestMethod.POST, RequestMethod.GET })
	public String showAttach(HttpServletRequest req, HttpSession session, Model model) {
		String clothesId = req.getParameter("clothesId");
		model.asMap().clear();
		try {
			List<ClothesAttach> list =storeBusiness.showAttach(clothesId);
			model.addAttribute("list", list);
			model.addAttribute("resultCode", "1");
			model.addAttribute("resultMsg", "获取数据成功");
			return "ewashing/storeBusiness/clothesAttach";
		} catch (Exception e) {
			model.addAttribute("resultCode", "0");
			model.addAttribute("resultMsg", "获取数据失败");
			return JSONObject.toJSONString(model);
		}
	}
	
	/**
	 * 查询会员对应的衣服
	 * 
	 * @param req
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/queryMemberClothes", method = { RequestMethod.POST, RequestMethod.GET })
	public String queryMemberClothes(HttpServletRequest req, HttpSession session, Model model) {
		String queryKey = req.getParameter("queryKey");
		ObjectMapper objectMapper = new ObjectMapper();
		model.asMap().clear();
		try {
			SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
			StoreClothes clothes = new StoreClothes();
			clothes.setQueryKey(queryKey);
			clothes.setStoreId(loginUser.getEwashingStore().getId());
			List<StoreClothes> list = storeBusiness.findClothesByMobileOrCardNumber(clothes);
			model.addAttribute("list", list);
			model.addAttribute("resultCode", "1");
			model.addAttribute("resultMsg", "获取数据成功");
			return objectMapper.writeValueAsString(model);
		} catch (Exception e) {
			model.addAttribute("resultCode", "0");
			model.addAttribute("resultMsg", "获取数据失败");
			return JSONObject.toJSONString(model);
		}
	}

	/**
	 * 增加衣物
	 * 
	 * @param req
	 * @param ewashingStoreUser
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/addClothes", method = { RequestMethod.POST, RequestMethod.GET })
	public String addClothes(HttpServletRequest req, HttpSession session,
			Model model) {

		String cardNumber = req.getParameter("cardNumber");
		String memberId = req.getParameter("memberId");
		String orderCode = req.getParameter("orderCode");
		String orderId = req.getParameter("orderId");
		String queryKey =req.getParameter("queryKey");
		// 订单号生成规则
		if (null == orderCode || "".equals(orderCode)) {
			orderCode = DateUtil.formatDate(new Date(), "yyyyMMddHHmmss");
		}
		model.addAttribute("memberId", memberId);
		model.addAttribute("orderCode", orderCode);
		model.addAttribute("orderId", orderId);
		model.addAttribute("cardNumber", cardNumber);
		model.addAttribute("queryKey", queryKey);
		return "ewashing/storeBusiness/addClothes";
	}
	
	/**
	 * O2O分拣衣物明细
	 * 
	 * @param req
	 * @param ewashingStoreUser
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/sortingClothes", method = { RequestMethod.POST, RequestMethod.GET })
	public String sortingClothes(HttpServletRequest req, HttpSession session,
			Model model) {

		String cardNumber = req.getParameter("cardNumber");
		String memberId = req.getParameter("memberId");
		String orderCode = req.getParameter("orderCode");
		String queryKey =req.getParameter("queryKey");
		// 订单号生成规则
		if (null == orderCode || "".equals(orderCode)) {
			orderCode = DateUtil.formatDate(new Date(), "yyyyMMddHHmmss");
		}
		model.addAttribute("memberId", memberId);
		model.addAttribute("orderCode", orderCode);
		model.addAttribute("cardNumber", cardNumber);
		model.addAttribute("queryKey", queryKey);
		return "ewashing/storeBusiness/sortingClothesDetail";
	}
	
	
	
	/**
	 * 编辑衣服
	 * @param req
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/editClothes", method = { RequestMethod.POST, RequestMethod.GET })
	public String editClothes(HttpServletRequest req, HttpSession session,
			Model model) {
		
		String clothesId = req.getParameter("clothesId");
		// 查询衣服信息，对应的ID
		StoreClothes clothesIds =storeBusiness.findClothesById(clothesId);
		// 查询衣服信息，ID 对应的value
		StoreClothes clothesName =storeBusiness.getId(clothesId);
		ClothesAttach atthach =new ClothesAttach();
		atthach.setClothesId(clothesId);
		List<ClothesAttach> attachList =clothesAttachService.findList(atthach);
		StringBuffer sbNames = new StringBuffer();
		StringBuffer sbIds = new StringBuffer();
		
		
		JSONArray array = new JSONArray();
		
		if(!CollectionUtils.isEmpty(attachList)){
			for(int i=0;i<attachList.size();i++){
				JSONObject obj = new JSONObject();
				obj.put("id", attachList.get(i).getAttachId());
				obj.put("attachBarCode", attachList.get(i).getAttachBarCode());
				array.add(obj);
				if(i>0){
					sbIds.append(",");
				}
				sbIds.append(attachList.get(i).getAttachId());
				sbNames.append("【");
				sbNames.append(attachList.get(i).getAttachName());
				sbNames.append("】");
			}
		}
		
		// 获取瑕疵
		StringBuffer flawNames = new StringBuffer();
		String flaws =clothesIds.getFlaw();
		if(!StringUtils.isEmpty(flaws)){
			
			List<EwashingDataDictionary> dataList = ewashingDataService.queryTextNameByIds(clothesIds.getFlaw().split(","));
			if (!CollectionUtils.isEmpty(dataList)) {
				for (int i = 0; i < dataList.size(); i++) {
					flawNames.append("【");
					flawNames.append(dataList.get(i).getDataName());
					flawNames.append("】");
				}
			}
		}
		model.addAttribute("attachArrayList", array);
		model.addAttribute("flawNames", flawNames);
		model.addAttribute("attachNames", sbNames);
		model.addAttribute("attachIds", sbIds);
		model.addAttribute("attachNames", sbNames);
		model.addAttribute("clothesId", clothesId);
		model.addAttribute("clothesIds", clothesIds);
		model.addAttribute("clothesName", clothesName);
		return "ewashing/storeBusiness/editClothes";
	}
	

	/**
	 * 送洗
	 * 
	 * @param req
	 * @param ewashingStoreUser
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/send", method = { RequestMethod.POST, RequestMethod.GET })
	public String send(HttpServletRequest req,  HttpSession session, Model model) {

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
			SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
			StoreClothes clothes = new StoreClothes();
			clothes.setStatus("1");
			
			if(loginUser!=null && loginUser.getEwashingStore()!=null){
				clothes.setStoreId(loginUser.getEwashingStore().getId());
			}
			List<StoreClothes> list = storeBusiness.findClothesByMobileOrCardNumber(clothes);
			PageInfo<StoreClothes> page = new PageInfo<StoreClothes>(list);
			model.addAttribute("page", page);
			return "ewashing/storeBusiness/send";
		} catch (Exception e) {
			logger.error("查询待送洗信息失败。", e);
			return null;
		}
	}
	
	
	@RequestMapping(value = "/warning", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public String warning(HttpServletRequest req, HttpSession session, Model model) {

		ObjectMapper objectMapper = new ObjectMapper();
		try {
			model.asMap().clear();
			SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
			StoreClothes clothes = new StoreClothes();
			clothes.setStatus("1");
			
			clothes.setStoreId(loginUser.getEwashingStore().getId());
			List<StoreClothes> list = storeBusiness.findClothesByMobileOrCardNumber(clothes);
			model.addAttribute("sendCount", list.size());
			model.addAttribute("userType", loginUser.getUserType());
			return objectMapper.writeValueAsString(model);
			
		} catch (Exception e) {
			model.addAttribute("resultCode", "0");
			model.addAttribute("resultMsg", "获取预警信息失败");
		}
		return JSONObject.toJSONString(model);
		
	}
	
	
	/**
	 * 送洗查询
	 * @param req
	 * @param sendWashing
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/sendQuery", method = { RequestMethod.POST, RequestMethod.GET })
	public String sendQuery(HttpServletRequest req, SendWashing sendWashing,HttpSession session, Model model) {

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
			SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
			sendWashing.setStoreId(loginUser.getEwashingStore().getId());
			List<SendWashing> list = storeBusiness.sendQuery(loginUser,sendWashing);
			PageInfo<SendWashing> page = new PageInfo<SendWashing>(list);
			
			model.addAttribute("beginDate", sendWashing.getBeginDate());
			model.addAttribute("endDate", sendWashing.getEndDate());
			model.addAttribute("sendNumber", sendWashing.getSendNumber());
			model.addAttribute("page", page);
			return "ewashing/storeQuery/sendQuery";
		} catch (Exception e) {
			logger.error("查询送洗记录失败。", e);
			return null;
		}
	}
	
	/**
	 * 送洗页面
	 * @param req
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/sendWashing", method = { RequestMethod.POST})
	@ResponseBody
	public String sendWashing(HttpServletRequest req, HttpSession session, Model model) {
		SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			model.asMap().clear();
			String ids = req.getParameter("ids");
			if(ids ==null || "".equals(ids)){
				model.addAttribute("resultCode", "0");
				model.addAttribute("resultMsg", "送洗衣服失败");
				return JSONObject.toJSONString(model);
			}
			
			if(ids.endsWith(",")){
				ids =ids.substring(0, ids.length()-1);
			}
			
			boolean flag =storeBusiness.sendWashing(loginUser,ids);
			if(!flag){
				model.addAttribute("resultCode", "0");
				model.addAttribute("resultMsg", "送洗衣服失败");
			}
			else
			{
				model.addAttribute("resultCode", "1");
				model.addAttribute("resultMsg", "送洗衣服成功");
			}
			return objectMapper.writeValueAsString(model);
			
		} catch (Exception e) {
			model.addAttribute("resultCode", "0");
			model.addAttribute("resultMsg", "送洗衣服失败");
		}
		return JSONObject.toJSONString(model);
	}
	
	/**
	 * 根据挂衣区获取挂衣号
	 * @param req
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/queryHandonNoByArea", method = { RequestMethod.POST})
	@ResponseBody
	public String queryHandonNoByArea(HttpServletRequest req, HttpSession session, Model model) {
		
		SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			model.asMap().clear();
			String areaId = req.getParameter("areaId");
			if(areaId ==null || "".equals(areaId)){
				model.addAttribute("resultCode", "0");
				model.addAttribute("resultMsg", "获取数据失败");
				return JSONObject.toJSONString(model);
			}
			List<HandOnNo> list =storeBusiness.queryHandonNoByArea(areaId,loginUser.getEwashingStore().getId());
			model.addAttribute("list", list);
			model.addAttribute("resultCode", "1");
			model.addAttribute("resultMsg", "获取数据成功");
			return objectMapper.writeValueAsString(model);
		} catch (Exception e) {
			model.addAttribute("resultCode", "0");
			model.addAttribute("resultMsg", "获取数据失败");
			return JSONObject.toJSONString(model);
		}
	}
	
	
	/**
	 * 获取门店挂衣区
	 * @param req
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/queryStoreHandOnArea", method = { RequestMethod.POST})
	@ResponseBody
	public String queryStoreHandOnArea(HttpServletRequest req, HttpSession session, Model model) {
		
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			model.asMap().clear();
			String storeId = req.getParameter("storeId");
			// 1:门店2：工厂
			String handType =req.getParameter("handType");
			if(!com.ehyf.ewashing.util.StringUtils.isEmptyString(storeId)){
				List<HandOnArea> list =storeBusiness.queryStoreHandOnArea(storeId,handType);
				model.addAttribute("list", list);
				model.addAttribute("resultCode", "1");
				model.addAttribute("resultMsg", "获取数据成功");
				return objectMapper.writeValueAsString(model);
			}
			
		} catch (Exception e) {
			model.addAttribute("resultCode", "0");
			model.addAttribute("resultMsg", "获取数据失败");
			return JSONObject.toJSONString(model);
		}
		return null;
	}
	
	/**
	 * 获取指定类型的挂衣区
	 * 如果是门店，需要根据门店查询
	 * @param req
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/queryHandOnAreaByHandType", method = { RequestMethod.POST})
	@ResponseBody
	public String queryHandOnAreaByHandType(HttpServletRequest req, HttpSession session, Model model) {
		
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			model.asMap().clear();
			String handType =req.getParameter("handType");
			SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
			String storeId =req.getParameter("storeId");
			HandOnArea area =new HandOnArea();
			if(loginUser != null && "1".equals(loginUser.getUserType())){
				area.setStoreId(storeId);
			}
			area.setHandType(handType);
			
			List<HandOnArea> list =storeBusiness.queryHandOnAreaByHandType(area);
			model.addAttribute("list", list);
			model.addAttribute("resultCode", "1");
			model.addAttribute("resultMsg", "获取数据成功");
			return objectMapper.writeValueAsString(model);
			
		} catch (Exception e) {
			model.addAttribute("resultCode", "0");
			model.addAttribute("resultMsg", "获取数据失败");
			return JSONObject.toJSONString(model);
		}
	}
	
	
	@RequestMapping(value = "/queryStoreByHandType", method = { RequestMethod.POST})
	@ResponseBody
	public String queryStoreByHandType(HttpServletRequest req, HttpSession session, Model model) {
		
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			model.asMap().clear();
			String handType =req.getParameter("handType");
			if(!com.ehyf.ewashing.util.StringUtils.isEmptyString(handType)){
				List<EwashingStore> list =storeService.findList(new EwashingStore());
				model.addAttribute("list", list);
				model.addAttribute("resultCode", "1");
				model.addAttribute("resultMsg", "获取数据成功");
				return objectMapper.writeValueAsString(model);
			}
			
		} catch (Exception e) {
			model.addAttribute("resultCode", "0");
			model.addAttribute("resultMsg", "获取数据失败");
			return JSONObject.toJSONString(model);
		}
		return null;
	}
	
	
	
	/**
	 * 送洗详情
	 * @param req
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/showDetail", method = { RequestMethod.POST, RequestMethod.GET })
	public String showDetail(HttpServletRequest req,HttpSession session, Model model) {

		
		String pageNum = req.getParameter("pageNum");
		String pageSize = req.getParameter("pageSize");
		String id =req.getParameter("id");
		try {
			if (StringUtils.isBlank(pageNum)) {
				pageNum = "1";
			}
			if (StringUtils.isBlank(pageSize)) {
				pageSize = "10";
			}
			PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize));
			// 获取送洗单表头
			SendWashing sendWashing =storeBusiness.querySendWashingById(id);
			// 获取送洗明细
			StoreClothes clothes = new StoreClothes();
			clothes.setSendNumber(sendWashing.getSendNumber());
			List<StoreClothes> list = storeBusiness.findClothesBySendNumber(clothes);
			PageInfo<StoreClothes> page = new PageInfo<StoreClothes>(list);
			model.addAttribute("sendWashing", sendWashing);
			model.addAttribute("page", page);
			return "ewashing/storeQuery/showDetail";
		} catch (Exception e) {
			logger.error("查询送洗记录失败。", e);
			return null;
		}
	}
	/**
	 * 签收
	 * 
	 * @param req
	 * @param ewashingStoreUser
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/sign", method = { RequestMethod.POST, RequestMethod.GET })
	public String sign(HttpServletRequest req, StoreClothes storeClothes, HttpSession session, Model model) {

		// 获取已出厂，待签收状态衣服
		String memberName =req.getParameter("memberName");
		String mobilePhone =req.getParameter("mobilePhone");
		String barCode =req.getParameter("barCode");
		SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
		StoreClothes c = new StoreClothes();
		c.setStatus("13");
		if(loginUser.getEwashingStore()!=null){
			c.setStoreId(loginUser.getEwashingStore().getId());
		}
		c.setMemberName(memberName);
		c.setMobilePhone(mobilePhone);
		c.setBarCode(barCode);
		List<StoreClothes> list =storeBusiness.findClothes(c);
		
		model.addAttribute("memberName", memberName);
		model.addAttribute("mobilePhone", mobilePhone);
		model.addAttribute("barCode", barCode);
		model.addAttribute("list", list);
		return "ewashing/storeBusiness/sign";
	}

	/**
	 * 上挂
	 * 
	 * @param req
	 * @param ewashingStoreUser
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/handon", method = { RequestMethod.POST, RequestMethod.GET })
	public String handon(HttpServletRequest req, HttpSession session, Model model) {

		// 获取门店的挂衣区
		SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
		HandOnArea area = new HandOnArea();
		area.setHandType(loginUser.getUserType());
		if(loginUser!=null && loginUser.getEwashingStore()!=null){
			area.setStoreId(loginUser.getEwashingStore().getId());
		}
		
		String memberName =req.getParameter("memberName");
		String mobilePhone =req.getParameter("mobilePhone");
		String barCode =req.getParameter("barCode");
		// 获取挂衣区
		List<HandOnArea> areaList = handonArea.findList(area);
		model.addAttribute("areaList", areaList);
		
		// 获取已出厂衣服
		StoreClothes c = new StoreClothes();
		if(loginUser!=null && loginUser.getEwashingStore()!=null){
			c.setStoreId(loginUser.getEwashingStore().getId());
		}
		c.setStatus("14");
		c.setMemberName(memberName);
		c.setMobilePhone(mobilePhone);
		c.setBarCode(barCode);
		
		List<StoreClothes> clothesList =storeBusiness.findClothes(c);
		model.addAttribute("clothesList", clothesList);
		
		model.addAttribute("memberName", memberName);
		model.addAttribute("mobilePhone", mobilePhone);
		model.addAttribute("barCode", barCode);
		
		model.addAttribute("waitHandOnCount", clothesList.size());
		return "ewashing/storeBusiness/handon";
	}
	
	/**
	 * 取衣服
	 * @param req
	 * @param clothes
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/takeClothes", method = { RequestMethod.POST, RequestMethod.GET })
	public String takeClothes(HttpServletRequest req,StoreClothes clothes, HttpSession session, Model model) {
		
		String pageNum = req.getParameter("pageNum");
		String pageSize = req.getParameter("pageSize");
		String orderCode =req.getParameter("orderCode");
		try {
			if (StringUtils.isBlank(pageNum)) {
				pageNum = "1";
			}
			if (StringUtils.isBlank(pageSize)) {
				pageSize = "10";
			}
			PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize));
			
			/** 查询条件不为空 **/
			if(StringUtils.isNoneBlank(orderCode)){
				StoreOrder order =storeOrder.getStoreOrderByCode(orderCode);
				order.setOrderStatus(OrderStatus.getName(order.getOrderStatus()));
				model.addAttribute("order", order);
				// 获取衣服信息
				StoreClothes c = new StoreClothes();
				c.setStatus("15");
				c.setOrderCode(clothes.getOrderCode());
				List<StoreClothes> list = storeBusiness.findClothes(c);
				PageInfo<StoreClothes> page = new PageInfo<StoreClothes>(list);
				
				MemberCard member = memberCardService.queryMemberByCardOrMobile(order.getMobilePhone());

				model.addAttribute("member", member);
				model.addAttribute("page", page);
				model.addAttribute("mobilePhone", clothes.getMobilePhone());
				model.addAttribute("orderCode", clothes.getOrderCode());
				model.addAttribute("barCode", clothes.getBarCode());
				return "ewashing/storeBusiness/takeClothes";
			}
		} catch (Exception e) {
			logger.error("查询记录失败。", e);
			return null;
		}
		// 查询后，条件需要保留
		model.addAttribute("mobilePhone", clothes.getMobilePhone());
		model.addAttribute("orderCode", clothes.getOrderCode());
		model.addAttribute("barCode", clothes.getBarCode());
		return "ewashing/storeBusiness/takeClothes";
	}

	/**
	 * 会员订单
	 * @param req
	 * @param clothes
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/memberOrder", method = { RequestMethod.POST, RequestMethod.GET })
	public String memberOrder(HttpServletRequest req,StoreClothes clothes, HttpSession session, Model model) {
		
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
			model.addAttribute("queryKey", clothes.getQueryKey());
			/** 查询条件不为空 **/
			SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
			List<StoreOrder> list =storeOrder.getStoreOrderByQueryKey(clothes.getQueryKey(),loginUser.getEwashingStore().getId());
			if(!CollectionUtils.isEmpty(list)){
				for(StoreOrder order :list){
					order.setOrderStatus(OrderStatus.getName(order.getOrderStatus()));
				}
			}
			PageInfo<StoreOrder> page = new PageInfo<StoreOrder>(list);
			model.addAttribute("page", page);
			return "ewashing/storeBusiness/memberOrders";
			
		} catch (Exception e) {
			logger.error("查询记录失败。", e);
			return null;
		}
	}
	/**
	 * 取衣服
	 * @param req
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/doTakeClothes", method = { RequestMethod.POST})
	@ResponseBody
	public String doTakeClothes(HttpServletRequest req, HttpSession session, Model model) {
		
		SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			model.asMap().clear();
			String ids = req.getParameter("ids");
			String orderCode =req.getParameter("orderCode");
			if(ids ==null || "".equals(ids)){
				model.addAttribute("resultCode", "0");
				model.addAttribute("resultMsg", "取衣服失败");
				return JSONObject.toJSONString(model);
			}
			if(ids.endsWith(",")){
				ids =ids.substring(0, ids.length()-1);
			}
			boolean flag =storeBusiness.doTakeClothes(loginUser,ids,orderCode);
			if(!flag){
				model.addAttribute("resultCode", "0");
				model.addAttribute("resultMsg", "取衣衣服失败");
				return JSONObject.toJSONString(model);
			}
			model.addAttribute("resultCode", "1");
			model.addAttribute("resultMsg", "取衣衣服成功");
			return objectMapper.writeValueAsString(model);
		} catch (Exception e) {
			model.addAttribute("resultCode", "0");
			model.addAttribute("resultMsg", "取衣衣服失败");
			return JSONObject.toJSONString(model);
		}
	}
	
	/**
	 * 确认上挂
	 * @param req
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/confirmHandOn", method = { RequestMethod.POST})
	@ResponseBody
	public String confirmHandOn(HttpServletRequest req, HttpSession session, Model model) {
		
		SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			model.asMap().clear();
			String clothes = req.getParameter("clothes");
			if(clothes ==null || "".equals(clothes)){
				model.addAttribute("resultCode", "0");
				model.addAttribute("resultMsg", "上挂失败");
				return JSONObject.toJSONString(model);
			}
			if(clothes.endsWith(",")){
				clothes =clothes.substring(0, clothes.length()-1);
			}
			
			boolean flag = storeBusiness.confirmHandOn(clothes,loginUser);
			
			if(!flag){
				model.addAttribute("resultCode", "0");
				model.addAttribute("resultMsg", "上挂衣服失败");
				return JSONObject.toJSONString(model);
			}
			model.addAttribute("resultCode", "1");
			model.addAttribute("resultMsg", "上挂衣服成功");
			return objectMapper.writeValueAsString(model);
			
		} catch (Exception e) {
			model.addAttribute("resultCode", "0");
			model.addAttribute("resultMsg", "上挂衣服失败");
			return JSONObject.toJSONString(model);
		}
	}
	
	/**
	 * 确认签收
	 * @param req
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/confirmSign", method = { RequestMethod.POST})
	@ResponseBody
	public String confirmSign(HttpServletRequest req, HttpSession session, Model model) {
		
		SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			model.asMap().clear();
			String clothes = req.getParameter("clothes");
			if(clothes ==null || "".equals(clothes)){
				model.addAttribute("resultCode", "0");
				model.addAttribute("resultMsg", "签收失败");
				return JSONObject.toJSONString(model);
			}
			if(clothes.endsWith(",")){
				clothes =clothes.substring(0, clothes.length()-1);
			}
			
			boolean flag = storeBusiness.confirmSign(clothes,loginUser);
			
			if(!flag){
				model.addAttribute("resultCode", "0");
				model.addAttribute("resultMsg", "签收衣服失败");
				return JSONObject.toJSONString(model);
			}
			model.addAttribute("resultCode", "1");
			model.addAttribute("resultMsg", "签收衣服成功");
			return objectMapper.writeValueAsString(model);
			
		} catch (Exception e) {
			model.addAttribute("resultCode", "0");
			model.addAttribute("resultMsg", "签收衣服失败");
			return JSONObject.toJSONString(model);
		}
	}
	
	@RequestMapping(value = "/savePhoto", method = { RequestMethod.POST})
	@ResponseBody
	public String savePhoto(HttpServletRequest req, HttpSession session, Model model){
		
		SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			model.asMap().clear();
			String imagePath = req.getParameter("imagePath");
			String clothesId =req.getParameter("clothesId");
			if(imagePath ==null || "".equals(imagePath)){
				model.addAttribute("resultCode", "0");
				model.addAttribute("resultMsg", "图片路径不存在");
				return JSONObject.toJSONString(model);
			}
			boolean flag = storeBusiness.savePhoto(imagePath,clothesId,loginUser);
			
			if(!flag){
				model.addAttribute("resultCode", "0");
				model.addAttribute("resultMsg", "保存图片失败");
				return JSONObject.toJSONString(model);
			}
			model.addAttribute("resultCode", "1");
			model.addAttribute("resultMsg", "保存图片成功");
			return objectMapper.writeValueAsString(model);
			
		} catch (Exception e) {
			model.addAttribute("resultCode", "0");
			model.addAttribute("resultMsg", "保存图片失败");
			return JSONObject.toJSONString(model);
		}
	}
	
}

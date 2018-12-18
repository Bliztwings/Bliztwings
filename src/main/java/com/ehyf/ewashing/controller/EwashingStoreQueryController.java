package com.ehyf.ewashing.controller;


import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
import com.ehyf.ewashing.emun.ClothesStatus;
import com.ehyf.ewashing.emun.OrderStatus;
import com.ehyf.ewashing.emun.PayStatus;
import com.ehyf.ewashing.emun.ServiceType;
import com.ehyf.ewashing.entity.ClothesFlow;
import com.ehyf.ewashing.entity.ClothesPhoto;
import com.ehyf.ewashing.entity.OrderPayRecord;
import com.ehyf.ewashing.entity.SecurityUser;
import com.ehyf.ewashing.entity.StoreClothes;
import com.ehyf.ewashing.entity.StoreOrder;
import com.ehyf.ewashing.entity.StoreStatistics;
import com.ehyf.ewashing.service.ClothesFlowService;
import com.ehyf.ewashing.service.EwashingStoreBusinessService;
import com.ehyf.ewashing.service.EwashingStoreClothesService;
import com.ehyf.ewashing.service.EwashingStoreOrderService;
import com.ehyf.ewashing.service.OrderPayRecordService;
import com.ehyf.ewashing.service.SendWashingService;
import com.ehyf.ewashing.vo.ReceptionStatisticsVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("/orderQuery")
public class EwashingStoreQueryController {

	private static Logger logger =Logger.getLogger(EwashingStoreController.class);
	
	@Autowired
	private EwashingStoreOrderService orderQueryService;
	@Autowired
	private EwashingStoreClothesService clothesQueryService;
	@Autowired
	private ClothesFlowService flowService;
	
	@Autowired
	private SendWashingService sendService;
	
	@Autowired
	private OrderPayRecordService payRecordService;
	
	@Autowired
    private EwashingStoreBusinessService storeBusiness;
	 
	/**
	 * 获取所有门店
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/order",method = {RequestMethod.POST, RequestMethod.GET})
	public String order(HttpServletRequest req, StoreOrder storeOrder,HttpSession session,
			Model model) {
		try {
			
			String memberName =req.getParameter("memberName");
			if(com.ehyf.ewashing.util.StringUtils.isNotEmptyString(memberName)){
			    //memberName = new String(memberName.getBytes("ISO-8859-1"),"UTF-8"); 
				memberName = URLDecoder.decode(memberName, "UTF-8"); 
			}
			storeOrder.setMemberName(memberName);
			
			SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
			if(loginUser!=null && loginUser.getEwashingStore()!=null){
				storeOrder.setStoreId(loginUser.getEwashingStore().getId());
			}
			
			String pageNum = req.getParameter("pageNum");
			String pageSize = req.getParameter("pageSize");
			String payStatus= storeOrder.getPayStatus();
			String barCode =req.getParameter("barCode");
			if (StringUtils.isBlank(pageNum)) {
				pageNum = "1";
			}
			if (StringUtils.isBlank(pageSize)) {
				pageSize = "10";
			}
			
			PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize));
			
			// 订单号为空，条码不为空
			if(com.ehyf.ewashing.util.StringUtils.isEmptyString(storeOrder.getOrderCode())){
				if(!com.ehyf.ewashing.util.StringUtils.isEmptyString(barCode)){
					StoreClothes c =new StoreClothes();
					c.setBarCode(barCode);
					List<StoreClothes> clothesList =clothesQueryService.findList(c);
					
					if(!CollectionUtils.isEmpty(clothesList)){
						storeOrder.setOrderCode(clothesList.get(0).getOrderCode());
					}
				}
			}
			
			// 设置衣服中文状态
			List<StoreOrder> list = orderQueryService.findList(storeOrder);
			if(!CollectionUtils.isEmpty(list)){
				
				for(StoreOrder order :list){
					order.setOrderStatus(OrderStatus.getName(order.getOrderStatus()));
					order.setPayStatus(PayStatus.getName(order.getPayStatus()));
				}
			}
			PageInfo<StoreOrder> page = new PageInfo<StoreOrder>(list);
			model.addAttribute("page", page);
			model.addAttribute("payStatus", payStatus);
			model.addAttribute("orderCode", storeOrder.getOrderCode());
			model.addAttribute("cardNumber", storeOrder.getCardNumber());
			model.addAttribute("barCode", barCode);
			model.addAttribute("mobilePhone", storeOrder.getMobilePhone());
			model.addAttribute("memberName", memberName);
			return "ewashing/storeQuery/order";
		} catch (Exception e) {
			logger.error("订单查询失败。", e);
			return null;
		}
	}
	
	/**
	 * 衣服时间轴初始化
	 * @param req
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/openClothesFlow",method = {RequestMethod.POST, RequestMethod.GET})
	public String openClothesFlow(HttpServletRequest req,HttpSession session,
			Model model) {
		String clothesId =req.getParameter("clothesId");
		model.addAttribute("clothesId", clothesId);
		return "ewashing/storeQuery/clothesFlow";
	}
	
	
	/**
	 * 获取衣服信息
	 * @param req
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/queryClothesFlows", method = { RequestMethod.POST })
	@ResponseBody
	public String queryClothesFlows(HttpServletRequest req, HttpSession session, Model model) {

		String clothesId =req.getParameter("clothesId");
		//SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);

		ObjectMapper objectMapper = new ObjectMapper();
		try {
			model.asMap().clear();
			ClothesFlow flow = new ClothesFlow();
			flow.setClothesId(clothesId);
			List<ClothesFlow> list =flowService.findClothesFlowList(flow);
			model.addAttribute("list", list);
			model.addAttribute("resultCode", "1");
			model.addAttribute("resultMsg", "获取成功");
			return objectMapper.writeValueAsString(model);
		} catch (Exception e) {
			model.addAttribute("resultCode", "0");
			model.addAttribute("resultMsg", "获取失败");
			return JSONObject.toJSONString(model);
		}
	}
	
	/**
	 * 获取所有门店
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/clothes",method = {RequestMethod.POST, RequestMethod.GET})
	public String clothes(HttpServletRequest req, StoreClothes storeClothes,HttpSession session,
			Model model) {

		try {
			
			String pageNum = req.getParameter("pageNum");
			String pageSize = req.getParameter("pageSize");
			
			if (StringUtils.isBlank(pageNum)) {
				pageNum = "1";
			}
			if (StringUtils.isBlank(pageSize)) {
				pageSize = "10";
			}
			
			PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize));
			
			SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
			if(loginUser!=null && loginUser.getEwashingStore()!=null){
				storeClothes.setStoreId(loginUser.getEwashingStore().getId());
			}
			
			List<StoreClothes> list = clothesQueryService.findList(storeClothes);
			
			if(!CollectionUtils.isEmpty(list)){
				
				for(StoreClothes clothes :list){
					clothes.setStatus(ClothesStatus.getName(clothes.getStatus()));
					clothes.setServiceType(ServiceType.getName(clothes.getServiceType()));
					
					// 获取衣服照片信息
					List<ClothesPhoto> photoList =storeBusiness.queryPhotoList(clothes.getId());
					if(!CollectionUtils.isEmpty(photoList)){
						clothes.setHasPhoto("1");
						clothes.setPhotoList(photoList);
					}
					
				}
			}

			PageInfo<StoreClothes> page = new PageInfo<StoreClothes>(list);
			model.addAttribute("page", page);
			model.addAttribute("barCode", storeClothes.getBarCode());
			model.addAttribute("cardNumber", storeClothes.getCardNumber());
			
			model.addAttribute("memberName", storeClothes.getMemberName());
			model.addAttribute("mobilePhone", storeClothes.getMobilePhone());
			
			return "ewashing/storeQuery/clothes";
		} catch (Exception e) {
			logger.error("订单查询失败。", e);
			return null;
		}
	}
	
	/**
	 * 衣物接收统计
	 * @param req
	 * @param storeOrder
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/receptionStatistics",method = {RequestMethod.POST, RequestMethod.GET})
	public String receptionStatistics(HttpServletRequest req, ReceptionStatisticsVo receptionStatisticsVo,HttpSession session,
			Model model) {
		try {
			
			String pageNum = req.getParameter("pageNum");
			String pageSize = req.getParameter("pageSize");
			
			if (StringUtils.isBlank(pageNum)) {
				pageNum = "1";
			}
			if (StringUtils.isBlank(pageSize)) {
				pageSize = "10";
			}
			
			PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize));
			SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
			if(loginUser!=null && loginUser.getEwashingStore()!=null){
				receptionStatisticsVo.setStoreId(loginUser.getEwashingStore().getId());
			}
			List<ReceptionStatisticsVo> list =sendService.receptionStatistics(receptionStatisticsVo);	
			PageInfo<ReceptionStatisticsVo> page = new PageInfo<ReceptionStatisticsVo>(list);
			model.addAttribute("page", page);
			return "ewashing/storeQuery/receptionStatistics";
		} catch (Exception e) {
			logger.error("统计失败。", e);
			return null;
		}
	}
	
	/**
	 *  付款流水查询
	 * @param req
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/payRecord",method = {RequestMethod.POST, RequestMethod.GET})
	public String payRecord(HttpServletRequest req,OrderPayRecord payRecord ,HttpSession session,
			Model model) {
		try {
			
			String pageNum = req.getParameter("pageNum");
			String pageSize = req.getParameter("pageSize");
			String mobilePhone =req.getParameter("mobilePhone");
			if(com.ehyf.ewashing.util.StringUtils.isNotEmptyString(mobilePhone)){
				payRecord.setMobilePhone(mobilePhone);
			}
			if (StringUtils.isBlank(pageNum)) {
				pageNum = "1";
			}
			if (StringUtils.isBlank(pageSize)) {
				pageSize = "10";
			}
			
			PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize));
			
			List<OrderPayRecord> list = payRecordService.findList(payRecord);
			PageInfo<OrderPayRecord> page = new PageInfo<OrderPayRecord>(list);
			model.addAttribute("page", page);
			
			model.addAttribute("memberName", payRecord.getMemberName());
			model.addAttribute("mobilePhone", payRecord.getMobilePhone());
			model.addAttribute("beginDate", payRecord.getBeginDate());
			model.addAttribute("endDate", payRecord.getEndDate());
			return "ewashing/storeQuery/payRecord";
		} catch (Exception e) {
			logger.error("查询失败。", e);
			return null;
		}
	}
	
	/**
	 * 订单详情
	 * @param req
	 * @param payRecord
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/orderDetail",method = {RequestMethod.POST, RequestMethod.GET})
	public String orderDetail(HttpServletRequest req,HttpSession session,
			Model model) {
		try {
			
			String orderId = req.getParameter("orderId");
			if(com.ehyf.ewashing.util.StringUtils.isEmptyString(orderId)){
				model.addAttribute("resultCode", "0");
				model.addAttribute("resultMsg", "参数不能为空");
				return JSONObject.toJSONString(model);
			}
			// 获取订单
			StoreOrder order =orderQueryService.getById(orderId);
			
			if(order==null){
				model.addAttribute("resultCode", "0");
				model.addAttribute("resultMsg", "找到不到对应订单");
				return JSONObject.toJSONString(model);
			}
			// 获取订单的衣服信息
			StoreClothes clothes =new StoreClothes();
			clothes.setOrderCode(order.getOrderCode());
			List<StoreClothes> clothesList =storeBusiness.findClothes(clothes);
			model.addAttribute("clothesList", clothesList);
			model.addAttribute("order", order);
			return "ewashing/storeQuery/orderDetail";
		} catch (Exception e) {
			logger.error("查询失败。", e);
			return null;
		}
	}
	
	
	/**
	 * 导出信息
	 * @param req
	 * @param storeOrder
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/exportExcel",method = {RequestMethod.POST, RequestMethod.GET})
	public void exportExcel(HttpServletRequest request,HttpServletResponse response, ReceptionStatisticsVo receptionStatisticsVo,HttpSession session,
			Model model) {
		
	     session.setAttribute("state", null);  
	     // 生成提示信息，  
	     response.setContentType("application/vnd.ms-excel");  
	     String codedFileName = null;  
	     OutputStream fOut = null;  
	     try  
	     {  
	    	 // 获取记录
			 List<ReceptionStatisticsVo> list =sendService.receptionStatistics(receptionStatisticsVo);	

	         // 进行转码，使其支持中文文件名  
	         codedFileName = java.net.URLEncoder.encode("门店送洗统计", "UTF-8");  
	         response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");  
	         // 产生工作簿对象  
	         HSSFWorkbook workbook = new HSSFWorkbook();  
	         //产生工作表对象  
	         HSSFSheet sheet = workbook.createSheet();  
	         
	         HSSFRow rowHead = sheet.createRow((int)0);//创建一行  
	         HSSFCell storeNameHead = rowHead.createCell((int)0);//创建一列  
	         storeNameHead.setCellValue("门店名称"); 
             HSSFCell sendDateHead = rowHead.createCell((int)1);//创建一列  
             sendDateHead.setCellValue("送洗时间"); 
             HSSFCell countHead = rowHead.createCell((int)2);//创建一列  
             countHead.setCellValue("数量"); 
	         
	         for (int i = 0; i <list.size(); i++)  
	         {  
	        	 ReceptionStatisticsVo vo =list.get(i);
	             HSSFRow row = sheet.createRow((int)i+1);//创建一行  
	             HSSFCell storeName = row.createCell((int)0);//创建一列  
	             storeName.setCellValue(vo.getStoreName());  
	             
	             HSSFCell sendDate = row.createCell((int)1);//创建一列  
	             sendDate.setCellValue(vo.getSendDateStr());  
	             
	             HSSFCell count = row.createCell((int)2);//创建一列  
	             count.setCellValue(vo.getCount());  
	         }  
	         fOut = response.getOutputStream();  
	         workbook.write(fOut); 
	         workbook.close();
	     }  
	     catch (UnsupportedEncodingException e1){
	    	 logger.error(e1.getMessage());
	     }  
	     catch (Exception e){
	    	 logger.error(e.getMessage());
	     }  
	     finally  
	     {  
	         try  
	         {  
	             fOut.flush();  
	             fOut.close();
	         }  
	         catch (IOException e){
	        	 logger.error(e.getMessage());
	         }  
	         session.setAttribute("state", "open");  
	     }  
	 }  
	
	
	@RequestMapping(value = "/storeStatistics",method = {RequestMethod.POST, RequestMethod.GET})
	public String storeStatistics(HttpServletRequest req,HttpSession session,
			Model model) {
		try {
			
			String beginDate =req.getParameter("beginDate");
			String endDate =req.getParameter("endDate");
			String storeId =null;
			
			SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
			
			if(loginUser!=null && loginUser.getEwashingStore()!=null){
				storeId =loginUser.getId();
			}
			List<StoreStatistics> list =orderQueryService.storeStatistics(beginDate,endDate,storeId);
			model.addAttribute("list", list);
			return "ewashing/storeQuery/storeStatistics";
		} catch (Exception e) {
			logger.error("查询失败。", e);
			return null;
		}
	}
}

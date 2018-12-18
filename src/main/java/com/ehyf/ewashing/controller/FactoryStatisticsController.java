package com.ehyf.ewashing.controller;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ehyf.ewashing.common.Constants;
import com.ehyf.ewashing.entity.EwashingStore;
import com.ehyf.ewashing.entity.EwashingStoreUser;
import com.ehyf.ewashing.entity.SecurityUser;
import com.ehyf.ewashing.entity.StoreClothes;
import com.ehyf.ewashing.service.EwashingStoreClothesService;
import com.ehyf.ewashing.service.EwashingStoreService;
import com.ehyf.ewashing.service.SendWashingService;
import com.ehyf.ewashing.util.DateUtil;
import com.ehyf.ewashing.vo.FactoryStatisticsVo;
import com.ehyf.ewashing.vo.ReceptionStatisticsVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("/statistics")
public class FactoryStatisticsController {

	@Autowired
	private EwashingStoreClothesService clothesQueryService;
	
	@Autowired
	private EwashingStoreService storeService;
	
	@Autowired
	private SendWashingService sendService;
	
	private static Logger logger =Logger.getLogger(FactoryStatisticsController.class);

	
	/**
	 * 获取当前工厂用户的入厂记录
	 * @param req
	 * @param ewashingStoreUser
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/clothesStatistics", method = { RequestMethod.POST, RequestMethod.GET })
    public String clothesStatistics(HttpServletRequest req, EwashingStoreUser ewashingStoreUser, HttpSession session, Model model) {
        
		try {
			String pageNum = req.getParameter("pageNum");
			String pageSize = req.getParameter("pageSize");
			String barCode =req.getParameter("barCode");
			String cardNumber =req.getParameter("cardNumber");
			String tabType =req.getParameter("tabType");
			if (StringUtils.isBlank(tabType)) {
				tabType = "3";
			}
			
			if (StringUtils.isBlank(pageNum)) {
				pageNum = "1";
			}
			if (StringUtils.isBlank(pageSize)) {
				pageSize = "10";
			}
			
			PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize));
	
			
	        SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
	        FactoryStatisticsVo fsVo =new FactoryStatisticsVo();
	        fsVo.setUserId(loginUser.getId());
	        fsVo.setBarCode(barCode);
	        fsVo.setClothesStatus(tabType);
	        fsVo.setCardNumber(cardNumber);
	        List<StoreClothes> list =clothesQueryService.clothesStatistics(fsVo);
	        
	        PageInfo<StoreClothes> page = new PageInfo<StoreClothes>(list);
			model.addAttribute("page", page);
			model.addAttribute("barCode", barCode);
			return "ewashing/statistics/clothesStatistics";
		} catch (Exception e) {
			logger.error("查询个人入厂信息失败。", e);
			return null;
		}
    }
	
	@RequestMapping(value = "/inFactoryStatistics", method = { RequestMethod.POST, RequestMethod.GET })
    public String inFactoryStatistics(HttpServletRequest req, EwashingStoreUser ewashingStoreUser, HttpSession session, Model model) {
        
		try {
			
			// 获取门店
			
			return "ewashing/statistics/inFactoryStatistics";
		} catch (Exception e) {
			logger.error("查询入厂统计失败", e);
			return null;
		}
    }
	
	/**
	 * 统计分析报表
	 * @param req
	 * @param ewashingStoreUser
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/statisticalAnalysis", method = { RequestMethod.POST, RequestMethod.GET })
    public String statisticalAnalysis(HttpServletRequest req,ReceptionStatisticsVo vo, HttpSession session, Model model) {
        
		try {
			// 获取门店
			List<EwashingStore> storeList =storeService.findList(new EwashingStore());
			String startDate ="";
			String endDate ="";
			String ds ="";
			int month =0;
			int year =0;
			Calendar c =Calendar.getInstance();
			if(vo.getBeginDate()==null || "".equals(vo.getBeginDate())){
				// 默认获取当前月的
				Date d =c.getTime();
				year =c.get(Calendar.YEAR);
				month =DateUtil.getMonth(DateUtil.formatDate(d, "yyyy-MM-dd HH:mm:ss"));
				ds =DateUtil.formatDate(d,"yyyy-MM");
			}
			else {
				ds =DateUtil.formatDate(DateUtil.parseData(vo.getBeginDate()+" 00:00:00"), "yyyy-MM");
				c.setTime(DateUtil.parseData(vo.getBeginDate()+" 00:00:00"));
				month =DateUtil.getMonth(vo.getBeginDate()+" 00:00:00");
				year =c.get(Calendar.YEAR);
			}
			
			startDate =ds+"-"+"01 00:00:01";
			endDate =getEndDate(ds, month, DateUtil.checkLeapYear(year));
			
			vo.setBeginDate(startDate);
			vo.setEndDate(endDate);
			
			List<ReceptionStatisticsVo> listVo =new ArrayList<ReceptionStatisticsVo>();
			if(!CollectionUtils.isEmpty(storeList)){
				
				for(EwashingStore store :storeList){
					ReceptionStatisticsVo sVo =new ReceptionStatisticsVo();
					sVo.setStoreId(store.getId());
					sVo.setBeginDate(startDate);
					sVo.setEndDate(endDate);
					// 获取统计信息
					List<ReceptionStatisticsVo> list =sendService.statisticalAnalysis(sVo);	
					
					
					if(CollectionUtils.isEmpty(list)){
						ReceptionStatisticsVo rs =new ReceptionStatisticsVo();
						rs.setStoreId(store.getId());
						rs.setStoreName(store.getStoreName());
						rs.setAmount(BigDecimal.ZERO);
						rs.setCount("0");
						rs.setSendDateStr(vo.getBeginDate());
						listVo.add(rs);
					}
					else {
						
						ReceptionStatisticsVo rso =list.get(0);
						rso.setSendDateStr(vo.getBeginDate());
						listVo.add(list.get(0));
					}
					
				}
			}
			
			model.addAttribute("list", listVo);
			model.addAttribute("storeList", storeList);
			model.addAttribute("beginDate", DateUtil.dataformat(vo.getBeginDate(), "yyyy-MM-dd"));
			model.addAttribute("storeId", vo.getStoreId());
			return "ewashing/statistics/statisticalAnalysis";
		} catch (Exception e) {
			logger.error("查询入厂统计失败", e);
			return null;
		}
    }
	
	
	private String getEndDate(String ym,int month ,boolean leapYear){
		
		if(month==1 || month ==3 || month ==5 || month ==7 || month==8 || month ==10 || month==12){
			return ym +"-31 23:23:59";
		}
		else if (month ==4 || month ==6 || month ==9 || month ==11){
			return ym +"-30 23:23:59";
		} else if (month==2){
			
			if(leapYear){
				return ym +"-29 23:23:59";
			}
			else {
				return ym +"-28 23:23:59";
			}
		}
		return null;
	}
}

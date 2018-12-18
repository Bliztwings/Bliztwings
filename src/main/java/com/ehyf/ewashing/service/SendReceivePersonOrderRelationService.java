package com.ehyf.ewashing.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ehyf.ewashing.common.Constants;
import com.ehyf.ewashing.dao.EwashingStoreOrderDao;
import com.ehyf.ewashing.dao.SendReceivePersonOrderRelationDao;
import com.ehyf.ewashing.entity.SendReceivePersonOrderRelation;
import com.ehyf.ewashing.entity.StoreOrder;
import com.ehyf.ewashing.exection.AppExection;
import com.ehyf.ewashing.util.DateUtil;
import com.ehyf.ewashing.vo.SendUserIncomeVo;

@Service
public class SendReceivePersonOrderRelationService extends BaseService<SendReceivePersonOrderRelationDao,SendReceivePersonOrderRelation> {

	@Autowired
	private EwashingStoreOrderDao orderDao;
	
	@Autowired
	private SendReceivePersonOrderRelationDao relationDao;
	
	/**
	 * 小E分配订单应答接口 1：接收 2：拒绝
	 * @param orderId 订单ID
	 * @param answerType 1：接收 2：拒绝
	 * @param distributeType   1：取衣 2：送衣
	 * @return
	 * @throws IOException
	 */
	@Transactional(readOnly=false)
	public boolean updatSendReceivePersonOrderRelation(String orderId, String answerType,String distributeType) {
		
		// 更新小E分配关系表，answerType 状态  1：接受 2:拒绝
		SendReceivePersonOrderRelation relation  =new SendReceivePersonOrderRelation();
		relation.setOrderId(orderId);
		relation.setDistributeType(distributeType);
		if("1".equals(answerType)){
			relation.setAcceptStatus("2"); // 接受
			// 送
			if("2".equals(distributeType)){
				// 送衣，接收后，直接变成未送
				relation.setTaskStatus("2");
			}
		}
		
		if("2".equals(answerType)){
			relation.setAcceptStatus("3"); // 拒绝
		}
		
		int result =relationDao.updatSendReceivePersonOrderRelation(relation);
		if(result<=0){
			return false;
		}
		
		// 更新订单信息
		StoreOrder order =new StoreOrder();
		order.setId(orderId);
		// 根据answerType 更新订单分配状态
		if("1".equals(answerType)){
			// 取衣
			if("1".equals(distributeType)){
				order.setDistributeStatus("2");// 更新为已分配
				// 更新订单为送往工厂
				order.setOrderStatus("21");
			}
			// 送衣服
			if("2".equals(distributeType)){
				order.setSendDistributeStatus("2");
				// 更新订单位送回中
				order.setOrderStatus("23");
			}
		}
		if("2".equals(answerType)){
			// 取
			if("1".equals(distributeType)){
				order.setDistributeStatus("0");// 更新为未分配
			}
			
			if("2".equals(distributeType)){
				order.setSendDistributeStatus("0");
			}
		}
		int result1 =orderDao.update(order);
		if(result1<=0){
			throw new AppExection("更新订单状态失败");
		}
		return true;
	}
	@Transactional(readOnly=false)
	public void updatSendReceivePersonOrderRelation(SendReceivePersonOrderRelation r) {
		relationDao.updatSendReceivePersonOrderRelation(r);
	}

	/**
	 * 更新小E 任务状态
	 * @param r
	 */
	@Transactional(readOnly=false)
	public void updatSendReceivePersonOrder(SendReceivePersonOrderRelation r) {
		relationDao.updatSendReceivePersonOrder(r);
	}
	
	@Transactional(readOnly=false)
	public SendUserIncomeVo queryIncome(String sendUserId, String userType) {
		
		// 当前时间
		String currentDate =DateUtil.formatDate(new Date(), "yyyy-MM-dd");
		// 月份
		Calendar c =Calendar.getInstance();
		int year =c.get(Calendar.YEAR);
		int montth =c.get(Calendar.MONTH)+1;
		System.out.println(c.get(Calendar.YEAR));		
		String appId="";
		if("1".equals(userType)){
			appId =Constants.APPID_SWY;
		}
		if("2".equals(userType)){
			appId =Constants.APPID_HYF;
		}
		SendUserIncomeVo result_day =queryIncome(currentDate+" 00:00:01",currentDate+" 23:23:59","3",sendUserId,appId);
		String endDate="";
		String preFix =DateUtil.formatDate(new Date(), "yyyy-MM");
		String startDate=preFix+"-01"+" 00:00:01";
		// 闰年 2月29天
		if (montth == 1 || montth == 3 || montth == 5 || montth == 7 || montth == 8 || montth == 10 || montth == 12) {
			endDate = preFix + "-31"  + " 23:59:59";
		} else if (montth == 2) {
			
			if(checkLeapYear(year)){
				endDate = preFix + "-29"  + " 23:59:59";
			}else{
				endDate = preFix + "-28"  + " 23:59:59";
			}
		} else if (montth == 4 || montth == 6 || montth == 9 || montth == 11) {
			endDate = preFix + "-30"  + " 23:59:59";
		}
		SendUserIncomeVo result_month =queryIncome(startDate,endDate,"3",sendUserId,appId);
		SendUserIncomeVo result =new SendUserIncomeVo();
		result.setIncomeDay(result_day==null ?BigDecimal.ZERO:result_day.getSumAmount());
		result.setIncomeMonth(result_month==null ? BigDecimal.ZERO :result_month.getSumAmount());
		
		if("1".equals(userType)){
			result.setIncomeDay(result.getIncomeDay().multiply(new BigDecimal(0.075)).setScale(2,BigDecimal.ROUND_HALF_UP));
			result.setIncomeMonth(result.getIncomeMonth().multiply(new BigDecimal(0.075)).setScale(2,BigDecimal.ROUND_HALF_UP));
		}
		if("2".equals(userType)){
			result.setIncomeDay(result.getIncomeDay().multiply(new BigDecimal(0.05)).setScale(2,BigDecimal.ROUND_HALF_UP));
			result.setIncomeMonth(result.getIncomeMonth().multiply(new BigDecimal(0.05)).setScale(2,BigDecimal.ROUND_HALF_UP));
		}
		return result;
	}
	
	
	private SendUserIncomeVo queryIncome(String stratDate, String endDate, String taskStatus, String sendUserId,
			String appId) {
		// 获取萨维亚订单-当天
		SendUserIncomeVo vo = new SendUserIncomeVo();
		vo.setStartDate(stratDate);
		vo.setEndDate(endDate);
		vo.setTaskStatus(taskStatus);
		vo.setSendUserId(sendUserId);
		vo.setAppId(appId);
		return relationDao.queryIncome(vo);
	}

	
	/*public static void main(String[] args) {
		String currentDate =DateUtil.formatDate(new Date(), "yyyy-MM");
		Calendar c =Calendar.getInstance();
		
		System.out.println(c.get(Calendar.MONTH)+1);

	}*/
	
	public boolean checkLeapYear(int year) {
		boolean flag = false;
		if ((year % 4 == 0) && ((year % 100 != 0) || (year % 400 == 0))) {
			flag = true;
		}
		return flag;
	}
}

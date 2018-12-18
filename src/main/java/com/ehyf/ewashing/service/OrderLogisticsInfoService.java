package com.ehyf.ewashing.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ehyf.ewashing.dao.OrderLogisticsInfoDao;
import com.ehyf.ewashing.entity.OrderLogisticsInfo;
import com.ehyf.ewashing.entity.SendReceivePerson;
import com.ehyf.ewashing.exection.AppExection;
import com.ehyf.ewashing.util.UUID;
import com.ehyf.ewashing.vo.OrderLogisticsInfoVo;

/**
 * 
 * OrderLogisticsInfoService  Service服务接口类
 * 
 **/
@Service
public class OrderLogisticsInfoService extends BaseService<OrderLogisticsInfoDao,OrderLogisticsInfo> {
	
	@Transactional(readOnly = false)
	public boolean newInstance(OrderLogisticsInfoVo lVo){
		if(StringUtils.isBlank(lVo.getOrderId())) return false;
		OrderLogisticsInfo olInfo = new OrderLogisticsInfo();
		olInfo.setId(UUID.getUUID32());
		olInfo.setCreateTime(new Date());
		olInfo.setOrderId(lVo.getOrderId());
		olInfo.setOrderStatus(lVo.getOrderStatus());
		olInfo.setContent(lVo.getContent());
		return dao.insert(olInfo)>0;
	}
	public List<OrderLogisticsInfo> getLogisticsInfoByOrderId(String orderId){
		if(StringUtils.isBlank(orderId)) return null;
		OrderLogisticsInfo olInfo = new OrderLogisticsInfo();
		olInfo.setOrderId(orderId);
		return dao.findList(olInfo);
	}
	
	@Transactional(readOnly=false)
	public int insertLogisticsInfo(String orderId, String message) {
		
		// 记录物流信息
		OrderLogisticsInfo olInfo = new OrderLogisticsInfo();
		olInfo.setId(UUID.getUUID32());
		olInfo.setCreateTime(new Date());
		olInfo.setOrderId(orderId);
		olInfo.setContent(message);

		int countInSert =insert(olInfo);
		if(countInSert<=0){
			throw new AppExection("记录物流信息失败"); 
		}
		
		return countInSert;
	}
}	
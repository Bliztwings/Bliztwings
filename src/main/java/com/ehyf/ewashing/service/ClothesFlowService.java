package com.ehyf.ewashing.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.ehyf.ewashing.dao.ClothesFlowDao;
import com.ehyf.ewashing.emun.ClothesStatus;
import com.ehyf.ewashing.entity.ClothesFlow;
import com.ehyf.ewashing.entity.SecurityUser;
import com.ehyf.ewashing.exection.AppExection;
import com.ehyf.ewashing.util.UUID;

@Service
public class ClothesFlowService extends BaseService<ClothesFlowDao,ClothesFlow> {

	@Autowired
	private ClothesFlowDao flowDao;
	
	@Transactional(readOnly=false)
	public int insertClothesFlow(String orderId,String clothesId, SecurityUser loginUser,String status) {
		
		ClothesFlow flow = new ClothesFlow();
		flow.setClothesId(clothesId);
		flow.setClothesStatus(status);
		flow.setOptDate(new Date());
		flow.setOptUserId(loginUser.getId());
		flow.setOptUserName(loginUser.getRealname());
		flow.setId(UUID.getUUID32());
		flow.setOrderId(orderId);
		// 记录衣服流水日志
		// 判断是否已经记录了日志
		ClothesFlow flowQuery =new ClothesFlow();
		flowQuery.setClothesId(clothesId);
		flowQuery.setClothesStatus(status);
		List<ClothesFlow> list =flowDao.findList(flowQuery);
		if(!CollectionUtils.isEmpty(list)){
			return 0;
		}			
		return flowDao.insert(flow);
	}

	public List<ClothesFlow> findClothesFlowList(ClothesFlow flow) {
		
		List<ClothesFlow> list =flowDao.findList(flow);
		
		if(!CollectionUtils.isEmpty(list)){
			for(ClothesFlow flows :list){
				flows.setClothesStatus(ClothesStatus.getName(flows.getClothesStatus()));
			}
		}
		return list;
	}

}

package com.ehyf.ewashing.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.ehyf.ewashing.dao.EwashingHandonNoDao;
import com.ehyf.ewashing.entity.HandOnNo;
import com.ehyf.ewashing.entity.SecurityUser;
import com.ehyf.ewashing.exection.AppExection;
import com.ehyf.ewashing.util.UUID;

@Service
public class EwashingHandonNoService extends BaseService<EwashingHandonNoDao,HandOnNo> {

	@Autowired
	private EwashingHandonNoDao handonDao;
	
	@Transactional(readOnly=false)
	public boolean insertHandOnNo(SecurityUser loginUser, int handOnNoBegin, int handOnNoEnd, HandOnNo handOnNo) {
		
		boolean flag =true;
		for(int i=handOnNoBegin;i<=handOnNoEnd;i++){
			HandOnNo handOnNoInsert = new HandOnNo();
			handOnNoInsert.setId(UUID.getUUID32());
			handOnNoInsert.setCreateDate(handOnNo.getCreateDate());
			handOnNoInsert.setHandonArea(handOnNo.getHandonArea());
			handOnNoInsert.setHandOnNo(String.valueOf(i));
			handOnNoInsert.setStoreId(handOnNo.getStoreId());
			handOnNoInsert.setCreateUserName(handOnNo.getCreateUserName());
			handOnNoInsert.setCreateUserId(handOnNo.getCreateUserId());
			handOnNoInsert.setHandType(handOnNo.getHandType());
			handOnNoInsert.setStatus("0");
			
			//判断是否存
			HandOnNo on = new HandOnNo();
			on.setHandType(handOnNo.getHandType());
			on.setHandonArea(handOnNo.getHandonArea());
			on.setHandOnNo(String.valueOf(i));
			List<HandOnNo> list =handonDao.findList(on);
			
			if(!CollectionUtils.isEmpty(list)){
				flag =false;
				throw new AppExection("存在相同记录，请重新设置");
			}else{
				handonDao.insert(handOnNoInsert);
			}
		}
		return flag;
	}

	
}

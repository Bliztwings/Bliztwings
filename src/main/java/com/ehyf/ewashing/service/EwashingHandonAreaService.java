package com.ehyf.ewashing.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.ehyf.ewashing.dao.EwashingHandonAreaDao;
import com.ehyf.ewashing.entity.HandOnArea;

@Service
public class EwashingHandonAreaService extends BaseService<EwashingHandonAreaDao, HandOnArea> {

	@Autowired
	private EwashingHandonAreaDao areaDao;

	@Transactional(readOnly = false)
	public boolean HandOnArea(HandOnArea handOnArea) {

		// 判断是否存在
		HandOnArea area = new HandOnArea();
		area.setHandType(handOnArea.getHandType());
		area.setHandOnArea(handOnArea.getHandOnArea());
		List<HandOnArea> list = areaDao.findList(area);
		if (!CollectionUtils.isEmpty(list)) {
			return false;
		}
		int count =areaDao.insert(handOnArea);
		if(count<=0){
			return false;
		}
		return true;
	}

}

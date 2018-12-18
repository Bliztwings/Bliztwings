package com.ehyf.ewashing.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.ehyf.ewashing.dao.EwashingDataDictionaryDao;
import com.ehyf.ewashing.entity.EwashingDataDictionary;

@Service
public class EwashingDataDictionaryService extends BaseService<EwashingDataDictionaryDao,EwashingDataDictionary> {

	@Autowired
	private EwashingDataDictionaryDao dataDao;
	
	public List<EwashingDataDictionary> queryTextNameByIds(String[] ids) {
		return dataDao.queryTextNameByIds(Arrays.asList(ids));
	}

	@Transactional(readOnly=false)
	public synchronized int insertDicData(EwashingDataDictionary ewashingDataDictionary) {
		
		EwashingDataDictionary ed =new EwashingDataDictionary();
		ed.setDataType(ewashingDataDictionary.getDataType());
		ed.setDataName(ewashingDataDictionary.getDataName());
		List<EwashingDataDictionary> list =dataDao.findList(ed);
		if(!CollectionUtils.isEmpty(list)){
			return 0;
		}
		else
		{
			return dataDao.insert(ewashingDataDictionary);
		}
		
	}
	@Transactional(readOnly=false)
	public synchronized int updateDicData(EwashingDataDictionary ewashingDataDictionary) {
		return dataDao.update(ewashingDataDictionary);
	}

	public List<EwashingDataDictionary> getDataDictionaryByCategoryId(String categoryId, String queryKey) {
		// TODO Auto-generated method stub
		return dataDao.getDataDictionaryByCategoryId(categoryId,queryKey);
	}

}

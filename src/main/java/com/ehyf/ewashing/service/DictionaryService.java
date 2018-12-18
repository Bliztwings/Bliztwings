package com.ehyf.ewashing.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ehyf.ewashing.dao.DictionaryDao;
import com.ehyf.ewashing.entity.Dictionary;

/**
 * 
 * DictionaryService  Service服务接口类
 * 
 **/
@Service
public class DictionaryService extends BaseService<DictionaryDao,Dictionary> {
	@Autowired
	DictionaryDao dictionaryDao;
	public List<Dictionary> getDictionaryByCode(String code){
		Dictionary d = new Dictionary();
		d.setCode(code);
		return dictionaryDao.findList(d);
	}
}
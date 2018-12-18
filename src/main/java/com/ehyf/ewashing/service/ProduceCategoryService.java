package com.ehyf.ewashing.service;

import com.ehyf.ewashing.entity.ProduceCategory;
import com.ehyf.ewashing.dao.ProduceCategoryDao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * ProduceCategoryService  Service服务接口类
 * 
 **/
@Service
public class ProduceCategoryService extends BaseService<ProduceCategoryDao,ProduceCategory> {
	@Autowired
	private ProduceCategoryDao produceCategoryDao;
	
	//获取跟节点
	public List<ProduceCategory> getRoot(){
		return produceCategoryDao.getRoot();
	}
	//根据父节点获取所有子节点
	public List<ProduceCategory> getChildren(String parentId,String parentCode){
		return produceCategoryDao.getChildren(parentId, parentCode);
	}
}
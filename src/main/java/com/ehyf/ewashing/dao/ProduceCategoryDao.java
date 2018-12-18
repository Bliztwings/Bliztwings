package com.ehyf.ewashing.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.ehyf.ewashing.entity.ProduceCategory;

/**
 * 
 * ProduceCategoryDao  数据库操作接口类
 * 
 **/
@Repository
public interface ProduceCategoryDao extends BaseDao<ProduceCategory> {
	
	public List<ProduceCategory> getRoot();
	
	public List<ProduceCategory> getChildren(@Param("parentId")String parentId,@Param("code")String code);
}
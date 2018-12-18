package com.ehyf.ewashing.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.ehyf.ewashing.entity.EwashingDataDictionary;

@Repository
public interface EwashingDataDictionaryDao extends BaseDao<EwashingDataDictionary> {

	List<EwashingDataDictionary> queryTextNameByIds(List<String> ids);

	List<EwashingDataDictionary> getDataDictionaryByCategoryId(@Param("categoryId") String categoryId, @Param("queryKey") String queryKey);

}

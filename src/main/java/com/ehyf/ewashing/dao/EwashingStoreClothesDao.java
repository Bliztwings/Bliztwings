package com.ehyf.ewashing.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.ehyf.ewashing.entity.StoreClothes;
import com.ehyf.ewashing.vo.FactoryStatisticsVo;

@Repository
public interface EwashingStoreClothesDao extends BaseDao<StoreClothes>{

	/**
	 * 根据订单号更新衣服信息
	 * @param clothes
	 * @return
	 */
	int updateStoreClothes(StoreClothes clothes);

	/**
	 * 批量更新衣服
	 * @param map
	 * @return
	 */
	int updateStoreClothesForBatch(Map<String, Object> map);

	/**
	 * 根据送洗单号查询送洗衣服
	 * @param clothes
	 * @return
	 */
	List<StoreClothes> findClothesBySendNumber(StoreClothes clothes);

	/**
	 * 获取衣服基本信息
	 * @param clothesId
	 * @return
	 */
	StoreClothes findClothesById(String clothesId);

	/**
	 * 查询当前用户的操作记录
	 * @param userId
	 * @return
	 */
	List<StoreClothes> clothesStatistics(FactoryStatisticsVo fsVo);

	/**
	 * 普通查询，关联少表
	 * @param storeClothes
	 * @return
	 */
	List<StoreClothes> findClothesCommon(StoreClothes storeClothes);

	/**
	 * 查找订单号
	 * @param storeClothes
	 * @return
	 */
	List<StoreClothes> findOrderCode(StoreClothes storeClothes);

	/**
	 * 查找订单信息
	 * @param storeClothes
	 * @return
	 */
	List<StoreClothes> findOrderInfo(StoreClothes storeClothes);

	/**
	 * 查找水洗唛信息
	 * @param storeClothes
	 * @return
	 */
	List<StoreClothes> findShuiXiMai(StoreClothes storeClothes);

	/**
	 * 获取待上挂衣服
	 * @param clothes
	 * @return
	 */
	List<StoreClothes> findHandonClothes(StoreClothes clothes);

	/**
	 * 获取o2o待上挂订单
	 * @param orderType
	 * @param clothesStatus
	 * @return
	 */
	List<StoreClothes> queryO2oOutFactoryOrder(Map<String, String> map);

	/**
	 * 获取O2O 订单
	 * @param entity
	 * @return
	 */
	List<StoreClothes> findListO2o(StoreClothes entity);

}

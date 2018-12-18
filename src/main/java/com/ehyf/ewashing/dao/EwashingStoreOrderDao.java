package com.ehyf.ewashing.dao;



import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ehyf.ewashing.entity.StoreClothes;
import com.ehyf.ewashing.entity.StoreOrder;

@Repository
public interface EwashingStoreOrderDao extends BaseDao<StoreOrder>{

	/**
	 * 更新订单信息
	 * @param order
	 * @return
	 */
	int updateStoreOrder(StoreOrder order);

	/**
	 * 根据订单号查询订单
	 * @param orderCode
	 * @return
	 */
	StoreOrder getStoreOrderByCode(String orderCode);

	/**
	 * 获取工厂带上挂订单
	 * @param order
	 * @return
	 */
	List<StoreOrder> findFactoryHandOnList(StoreOrder order);

	/**
	 * 根据订单信息
	 * @param queryKey
	 * @return
	 */
	List<StoreOrder> getStoreOrderByQueryKey(StoreClothes sc);

	/**
	 * 获取O2O 订单
	 * @param order
	 * @return
	 */
	List<StoreOrder> findO2oOrderList(StoreOrder order);

	/**
	 * O2O 下单
	 * @param order
	 * @return
	 */
	int placeOrder(StoreOrder order);

	/**
	 * 根据类型获取小E订单
	 * @param map
	 * @return
	 */
	List<StoreOrder> querySendReceiveOrder(Map<String, Object> map);

	/**
	 * 根据封签号获取订单
	 * @param queryKey
	 * @return
	 */
	StoreOrder getOrderBySealNumber(String queryKey);

	/**
	 * 更新衣服数量+1
	 * @param o
	 * @return
	 */
	int updateOrderCount(StoreOrder o);

}

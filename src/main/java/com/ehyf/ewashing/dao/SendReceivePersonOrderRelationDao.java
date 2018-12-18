package com.ehyf.ewashing.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ehyf.ewashing.entity.SendReceivePersonOrderRelation;
import com.ehyf.ewashing.vo.SendUserIncomeVo;

@Repository
public interface SendReceivePersonOrderRelationDao extends BaseDao<SendReceivePersonOrderRelation> {

	/**
	 * 更新小E关系表状态
	 * @param orderId
	 * @param distributeType
	 * @param answerType
	 * @return
	 */
	int updatSendReceivePersonOrderRelation(SendReceivePersonOrderRelation relation);

	/**
	 * 更新小E 任务状态
	 * @param r
	 */
	int updatSendReceivePersonOrder(SendReceivePersonOrderRelation r);

	/**
	 * 
	 * @param vo
	 * @return
	 */
	SendUserIncomeVo queryIncome(SendUserIncomeVo vo);

	/**
	 * 根据订单ID 获取分配订单信息
	 * @param r
	 * @return
	 */
	List<SendReceivePersonOrderRelation> findRevokeList(SendReceivePersonOrderRelation r);
}

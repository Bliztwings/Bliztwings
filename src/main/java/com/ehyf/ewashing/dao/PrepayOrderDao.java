package com.ehyf.ewashing.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.ehyf.ewashing.entity.PrepayOrder;

@Repository
public interface PrepayOrderDao extends BaseDao<PrepayOrder> {

	PrepayOrder querOrderBySign(@Param("sign") String sign);

	PrepayOrder queryOrderByTradeNo(@Param("out_trade_no") String out_trade_no);
}

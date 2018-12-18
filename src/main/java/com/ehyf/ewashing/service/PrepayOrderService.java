package com.ehyf.ewashing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ehyf.ewashing.dao.PrepayOrderDao;
import com.ehyf.ewashing.entity.PrepayOrder;

@Service
public class PrepayOrderService extends BaseService<PrepayOrderDao,PrepayOrder> {

	@Autowired
	private PrepayOrderDao prepayOrderDao;
	
	public PrepayOrder queryOrderByTradeNo(String out_trade_no) {
		
		return prepayOrderDao.queryOrderByTradeNo(out_trade_no);
	}

}

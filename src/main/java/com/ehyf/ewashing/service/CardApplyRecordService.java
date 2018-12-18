package com.ehyf.ewashing.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ehyf.ewashing.common.Constants;
import com.ehyf.ewashing.dao.CardApplyRecordDao;
import com.ehyf.ewashing.entity.CardApplyRecord;
import com.ehyf.ewashing.entity.MemberCard;
import com.ehyf.ewashing.util.UUID;

/**
 * 
 * CardApplyRecordService  Service服务接口类
 * 
 **/
@Service
public class CardApplyRecordService extends BaseService<CardApplyRecordDao,CardApplyRecord> {
	@Autowired
	private MemberCardService memberCardService;
	@Autowired
	private CardApplyRecordDao cardApplyRecordDao;
	
	
	@Transactional(readOnly = false)
	public boolean batchGenerateCards(CardApplyRecord record){
		if(record==null || record.getNumberBegin()==null || record.getNumberEnd()==null) return false;
		Long begin = Long.parseLong(record.getNumberBegin());
		Long end = Long.parseLong(record.getNumberEnd());
		if(begin>end) return false;
		List<String> cardsNumber = new ArrayList<String>();
		cardsNumber.add(record.getNumberBegin());
		while(begin.compareTo(end)<0)cardsNumber.add((++begin).toString());
		//简单实现
		for(String cardNo:cardsNumber){
			MemberCard card = new MemberCard();
			card.setId(UUID.getUUID32());
			card.setStoreId(record.getStoreId());
			card.setCardNumber(cardNo);
			card.setCashAmount(BigDecimal.ZERO);
			card.setGivedAmount(BigDecimal.ZERO);
			card.setTotalAmount(BigDecimal.ZERO);
			card.setCardStatus(Constants.CARD_STATUS_NEW);
			card.setCreateTime(new Date());
			card.setCreateUser(record.getApplyer());
			memberCardService.insert(card);
		}
		return true;
	}
	
	//验证卡区间是否有效，若区间卡号存在相同卡号，则返回失败false
	public boolean validateCardNumber(String cardNumberBegin,String cardNumberEnd){
		if(StringUtils.isBlank(cardNumberBegin) || StringUtils.isBlank(cardNumberEnd) || !cardNumberBegin.matches(number_regx) || !cardNumberEnd.matches(number_regx)) return false;
		Long begin = Long.parseLong(cardNumberBegin);
		Long end = Long.parseLong(cardNumberEnd);
		if(begin>end) return false;
		List<String> cards = new ArrayList<String>();
		cards.add(cardNumberBegin);
		while(begin.compareTo(end)<0){
			cards.add((++begin).toString());
			if(cards.size()==999){
				if(cardApplyRecordDao.validateCardNumber(cards)>0){
					return false;
				}
				cards.clear();
			}
		}
		return cardApplyRecordDao.validateCardNumber(cards)<=0;
	}
	
	//执行审批操作
	@Transactional(readOnly = false)
	public boolean doApproved(CardApplyRecord record){
		return cardApplyRecordDao.doApproved(record)>0;
	}
	
	private static final String number_regx = "(?!^0)\\d{1,15}";
}
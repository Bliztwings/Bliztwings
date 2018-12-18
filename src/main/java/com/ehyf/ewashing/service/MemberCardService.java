package com.ehyf.ewashing.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ehyf.ewashing.common.Constants;
import com.ehyf.ewashing.dao.CardOperatorRecordDao;
import com.ehyf.ewashing.dao.MemberCardDao;
import com.ehyf.ewashing.dao.MemberCardFlowingDao;
import com.ehyf.ewashing.dao.MemberCardRelationDao;
import com.ehyf.ewashing.dao.MemberDao;
import com.ehyf.ewashing.entity.CardOperatorRecord;
import com.ehyf.ewashing.entity.Member;
import com.ehyf.ewashing.entity.MemberCard;
import com.ehyf.ewashing.entity.MemberCardFlowing;
import com.ehyf.ewashing.entity.MemberCardRelation;
import com.ehyf.ewashing.entity.SecurityUser;
import com.ehyf.ewashing.util.LocalThreadUtils;
import com.ehyf.ewashing.util.UUID;
import com.ehyf.ewashing.vo.ConsumptionVo;

/**
 * 
 * MemberCardService  Service服务接口类
 * 
 **/
@Service
public class MemberCardService extends BaseService<MemberCardDao,MemberCard> {
	Logger logger = Logger.getLogger(MemberCardService.class);
	@Autowired
	private MemberCardRelationDao memberCardRelationDao;
	@Autowired
	private CardOperatorRecordDao cardOperatorRecordDao;
	@Autowired
	private MemberCardFlowingDao memberCardFlowingDao;
	@Autowired
	private MemberDao memberDao;
	@Autowired
	private MemberCardDao memberCardDao;
	
	public List<String> getNewCardNos(String storeId){
		return dao.getNewCardNos(storeId);
	}
	
	@Transactional(readOnly = false)
	public boolean distributionCard(Member member,MemberCard card,MemberCardRelation rel,CardOperatorRecord operRecord,MemberCardFlowing cardFlowing){
		return memberDao.update(member)>0 && update(card)>0 && memberCardRelationDao.insert(rel)>0 && cardOperatorRecordDao.insert(operRecord)>0 && memberCardFlowingDao.insert(cardFlowing)>0;
	}
	@Transactional(readOnly = false)
	public boolean doRecharge(MemberCard memberCard,MemberCardFlowing cardFlowing){
		return  update(memberCard)>0 && memberCardFlowingDao.insert(cardFlowing)>0;
	}
	@Transactional(readOnly = false)
	public boolean reportLoss(String memberCardId,CardOperatorRecord operRecord){
		return memberCardDao.reportLoss(memberCardId)>0 && cardOperatorRecordDao.insert(operRecord)>0;
	}
	
	@Transactional(readOnly = false)
	public boolean unReportLoss(String memberCardId,CardOperatorRecord operRecord){
		return memberCardDao.unReportLoss(memberCardId)>0 && cardOperatorRecordDao.insert(operRecord)>0;
	}
	@Transactional(readOnly = false)
	public boolean reback(String memberCardId,CardOperatorRecord operRecord){
		return memberCardDao.reback(memberCardId)>0 && cardOperatorRecordDao.insert(operRecord)>0;
	}
	
	@Transactional(readOnly = false)
	public boolean fillCard(MemberCardFlowing oldFlowing,MemberCardFlowing newFlowing,MemberCard oldCard,MemberCard newCard,CardOperatorRecord operRecord,MemberCardRelation rel){
		CardOperatorRecord cancelRecord = new CardOperatorRecord();
		try {
//			BeanUtils.copyProperties(cancelRecord, operRecord);
			cancelRecord.setId(UUID.getUUID32());
			cancelRecord.setCardId(oldCard.getId());
			cancelRecord.setStoreId(operRecord.getStoreId());
			cancelRecord.setCardNum(oldCard.getCardNumber());
			cancelRecord.setApprover(operRecord.getApprover());
			cancelRecord.setApprovedTime(new Date());
			cancelRecord.setCreateTime(new Date());
			cancelRecord.setCreateUser(operRecord.getCreateUser());
			cancelRecord.setIsDeleted(Constants.IS_DELETED_NO);
			cancelRecord.setOperatorType(Constants.CARD_OPERATOR_TYPE_CANCEL);
			cancelRecord.setStatus(Constants.CARD_OPERATOR_STATUS_APPROVED);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return memberCardFlowingDao.insert(oldFlowing)>0 && memberCardFlowingDao.insert(newFlowing)>0 
				&& cancel(oldCard.getId(),cancelRecord) && update(oldCard)>0 
				&& update(newCard)>0 && cardOperatorRecordDao.insert(operRecord)>0 && memberCardRelationDao.insert(rel)>0;
	}
	
	@Transactional(readOnly = false)
	public boolean cancel(String memberCardId,CardOperatorRecord operRecord){
		return memberCardDao.cancel(memberCardId)>0 && cardOperatorRecordDao.insert(operRecord)>0;
	}
	
	public MemberCard getCardByNo(String cardNumber){
		return dao.getCardByNo(cardNumber);
	}

	public MemberCard queryMemberByCardOrMobile(String queryKey) {
		MemberCard c = new MemberCard();
		c.setQueryKey(queryKey);
		c.setAppId((String)LocalThreadUtils.get(LocalThreadUtils.CONSTRANT_APP_ID));
		return dao.queryMemberByCardOrMobile(c);
	}
	@Transactional(readOnly = false)
	public boolean consumption(ConsumptionVo vo) throws Exception{
		Date now = new Date();
		BigDecimal zero = BigDecimal.valueOf(0);
		if(StringUtils.isBlank(vo.getCardNumber()) || vo.getOperatorAmount().compareTo(zero)<=0 || vo.getLoginUser() == null) throw new RuntimeException("参数校验失败!");
		MemberCard memberCard = getCardByNo(vo.getCardNumber());
		if(memberCard==null) throw new RuntimeException("会员卡"+vo.getCardNumber()+"不存在!");
		if(!Constants.CARD_STATUS_NORMAL.equals(memberCard.getCardStatus()) && !Constants.CARD_STATUS_FILLCARD.equals(memberCard.getCardStatus()))throw new RuntimeException("会员卡"+vo.getCardNumber()+"无效!");
		if(memberCard.getTotalAmount().compareTo(vo.getOperatorAmount())<0) throw new RuntimeException("会员卡"+vo.getCardNumber()+"余额不足!");
		BigDecimal givedOpeAmount = zero;
		BigDecimal cashOpeAmount = zero;
		try{
			if(memberCard.getGivedAmount().compareTo(zero)==1){
				if(memberCard.getGivedAmount().compareTo(vo.getOperatorAmount()) >=0){
					givedOpeAmount = vo.getOperatorAmount().negate();
				}else{
					givedOpeAmount = memberCard.getGivedAmount().negate();
					cashOpeAmount = vo.getOperatorAmount().subtract(memberCard.getGivedAmount()).negate();
					if(cashOpeAmount.compareTo(zero)==1) throw new RuntimeException("会员卡"+vo.getCardNumber()+"余额不足!");
				}
			}else{
				if(memberCard.getCashAmount().compareTo(vo.getOperatorAmount())<0) throw new RuntimeException("会员卡"+vo.getCardNumber()+"余额不足!");
				cashOpeAmount = vo.getOperatorAmount().negate();
			}
			MemberCardFlowing cardFlowing = new MemberCardFlowing();
			cardFlowing.setId(UUID.getUUID32());
			if(vo.getLoginUser()!=null && vo.getLoginUser().getEwashingStore()!=null){
				cardFlowing.setStoreId(vo.getLoginUser().getEwashingStore().getId());
			}
			cardFlowing.setCardId(memberCard.getId());
			cardFlowing.setCardNum(memberCard.getCardNumber());
			cardFlowing.setCashAmountBefore(memberCard.getCashAmount());
			cardFlowing.setCashAmountOper(cashOpeAmount);
			cardFlowing.setCashAmountAfter(cardFlowing.getCashAmountBefore().add(cardFlowing.getCashAmountOper()));
			cardFlowing.setGivedAmountBefore(memberCard.getGivedAmount());
			cardFlowing.setGivedAmountOper(givedOpeAmount);
			cardFlowing.setGivedAmountAfter(cardFlowing.getGivedAmountBefore().add(cardFlowing.getGivedAmountOper()));
			cardFlowing.setOperType(Constants.CARD_FLOWING_OPE_TYPE_CONSUMPTION);
			cardFlowing.setCreateTime(now);
			cardFlowing.setCreateUser(vo.getLoginUser().getUsername());
			cardFlowing.setIsDeleted(Constants.IS_DELETED_NO);
			cardFlowing.setRemark(vo.getRemark());
			cardFlowing.setSourceId(vo.getSourceId());
			memberCard.setCashAmount(memberCard.getCashAmount().add(cashOpeAmount));
			memberCard.setGivedAmount(memberCard.getGivedAmount().add(givedOpeAmount));
			memberCard.setTotalAmount(memberCard.getCashAmount().add(memberCard.getGivedAmount()));
			memberCard.setUpdateTime(now);
			memberCard.setUpdateUser(vo.getLoginUser().getId());
			return doRecharge(memberCard, cardFlowing);
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			throw new RuntimeException("卡消费异常!");
		}
	}
	
	public MemberCard queryCardByMemberId(String memberId){
		return memberCardDao.queryCardByMemberId(memberId);
	}

	public MemberCard queryMemberByCardOrMobileForBacken(String appid, String mobilePhone) {
		MemberCard c = new MemberCard();
		c.setQueryKey(mobilePhone);
		c.setAppId(appid);
		return dao.queryMemberByCardOrMobile(c);
	}
}
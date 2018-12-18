package com.ehyf.ewashing.restful.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.ehyf.ewashing.common.Constants;
import com.ehyf.ewashing.entity.CardOperatorRecord;
import com.ehyf.ewashing.entity.CheckCode;
import com.ehyf.ewashing.entity.Member;
import com.ehyf.ewashing.entity.MemberCard;
import com.ehyf.ewashing.entity.MemberCardFlowing;
import com.ehyf.ewashing.entity.MemberCardRelation;
import com.ehyf.ewashing.restful.client.ResultCode;
import com.ehyf.ewashing.restful.client.ResultData;
import com.ehyf.ewashing.service.CheckCodeService;
import com.ehyf.ewashing.service.MemberCardService;
import com.ehyf.ewashing.service.MemberService;
import com.ehyf.ewashing.util.LocalThreadUtils;
import com.ehyf.ewashing.util.MD5Util;
import com.ehyf.ewashing.util.UUID;

@Service
public class MemberRestfulService {
	@Autowired
	MemberService memberService;
	@Autowired
	private MemberCardService memberCardService;
	@Autowired
	private CheckCodeService checkCodeService;
	
	public ResultData<Object> login(Member member){
		Date now = new Date();
		if(null == member){
			return ResultCode.error("无效的用户请求", null);
		}
		if(StringUtils.isBlank(member.getMobilePhone()) || StringUtils.isBlank(member.getPassword())){
			return ResultCode.error("用户名或密码不能为空！", null);
		}
		member.setAppId((String)LocalThreadUtils.get(LocalThreadUtils.CONSTRANT_APP_ID));
		//1、验证用户密码
		member.setPassword(MD5Util.encode(member.getPassword()));
		Member m = memberService.getByMbileAndPwd(member);
		if(null == m){
			return ResultCode.error("用户名或密码错误!", null);
		}
		//2、token设置
		m.setToken(TokenService.O2O_TOKEN_FPRFIX.concat(UUID.getUUID32()));
		m.setLastLoginTime(now);
		if(StringUtils.isNoneBlank(member.getOpenId()))m.setOpenId(member.getOpenId());
		
		Member updateM = new Member();
		updateM.setId(m.getId());
		updateM.setToken(m.getToken());
		updateM.setLastLoginTime(now);
		updateM.setOpenId(m.getOpenId());
		return memberService.update(updateM)>0?ResultCode.success("操作成功", m):ResultCode.error("操作失败", null);
	
	}
	
	public ResultData<Object> loginOut(String memberId){
		if(StringUtils.isBlank(memberId)){
			return ResultCode.error("无效的用户请求", null);
		}
		Member updateM = new Member();
		updateM.setId(memberId);
		updateM.setLastLoginTime(TokenService.VALID_DATE);
		return memberService.update(updateM)>0?ResultCode.success("操作成功", null):ResultCode.error("操作失败", null);
	
	}
	
	
	/**
	 * 在线注册，返回null代表注册成功，否则返回错误信息
	 */
	@Transactional(readOnly = false)
	public ResultData<Object> onlineRegister(Member member){
		//验证码验证
		String appId = (String)LocalThreadUtils.get(LocalThreadUtils.CONSTRANT_APP_ID);
		CheckCode c = new CheckCode();
		c.setAppId(appId);
		c.setTelephone(member.getMobilePhone());
		c.setCode(member.getCheckCode());
		c.setType(Constants.MSG_TYPE_O2O_REGISTER);
		if(!checkCodeService.ckeck(c)) return ResultCode.error("手机验证码错误或已失效！", null);
		//1、会员信息
		member.setId(UUID.getUUID32());
		member.setCreateTime(new Date());
		member.setAppId(member.getAppId());
		member.setCreateUser(Constants.CREATE_USER_SYSTEM);
		member.setIsDeleted(Constants.IS_DELETED_NO);
		member.setBirthday(member.getBirthday());
		member.setClientType(Constants.MEMBER_CLIENT_TYPE_MOBILE);//mobile用户
		member.setType(Constants.MEMBER_TYPE_CARD);
		member.setPassword(MD5Util.encode(member.getPassword()));
		member.setAppId(appId);
		// 判断是否存在手机号的会员
		Member m = new Member();
		m.setMobilePhone(member.getMobilePhone());
		m.setAppId(appId);
		List<Member> list =memberService.findList(m);
		if(!CollectionUtils.isEmpty(list)){
			return ResultCode.error("手机号重复！", null);
		}else{
			//2、生成卡信息（电子）
			Date now=new Date();
			MemberCard card = new MemberCard();
			card.setId(UUID.getUUID32());
			// 微信openid 重复
			card.setCardNumber(Constants.MEMBER_CARD_PREFIX_MB.concat(UUID.getUUID32()));
			//card.setCardNumber(Constants.MEMBER_CARD_PREFIX_MB.concat(member.getOpenId()));
			card.setCashAmount(BigDecimal.ZERO);
			card.setGivedAmount(Constants.O2O_NEW_AMOUNT);
			card.setTotalAmount(card.getCashAmount().add(card.getGivedAmount()));
			card.setCreateTime(now);
			card.setCreateUser(Constants.CREATE_USER_SYSTEM);
			card.setPayedMethod(Constants.PAYED_METHOD_CASH);
			card.setCardStatus(Constants.CARD_STATUS_NORMAL);
			
			//3、会员卡流水信息
			MemberCardFlowing cardFlowing = new MemberCardFlowing();
			cardFlowing.setId(UUID.getUUID32());
			cardFlowing.setCardId(card.getId());
			cardFlowing.setCardNum(card.getCardNumber());
			cardFlowing.setCashAmountBefore(BigDecimal.ZERO);
			cardFlowing.setCashAmountOper(BigDecimal.ZERO);
			cardFlowing.setCashAmountAfter(BigDecimal.ZERO);
			cardFlowing.setGivedAmountBefore(BigDecimal.ZERO);
			cardFlowing.setGivedAmountOper(Constants.O2O_NEW_AMOUNT);
			cardFlowing.setGivedAmountAfter(Constants.O2O_NEW_AMOUNT);
			cardFlowing.setOperType(Constants.CARD_FLOWING_OPE_TYPE_RECHARGE);
			cardFlowing.setCreateTime(now);
			cardFlowing.setCreateUser(Constants.CREATE_USER_SYSTEM);
			cardFlowing.setIsDeleted(Constants.IS_DELETED_NO);
			cardFlowing.setPayedMethod(Constants.PAYED_METHOD_CASH);
			
			//4、记录关联关系
			MemberCardRelation rel = new MemberCardRelation();
			rel.setCardId(card.getId());
			rel.setMemberId(member.getId());
			
			//5、会员发卡记录
			CardOperatorRecord operRecord = new CardOperatorRecord();
			operRecord.setId(UUID.getUUID32());
			operRecord.setCardId(card.getId());
			operRecord.setCardNum(card.getCardNumber());
			operRecord.setOperatorType(Constants.CARD_OPERATOR_TYPE_GRANT);
			operRecord.setStatus(Constants.CARD_OPERATOR_STATUS_APPROVED);
			operRecord.setApprover(Constants.CREATE_USER_SYSTEM);
			operRecord.setApprovedTime(now);
			operRecord.setCreateTime(now);
			operRecord.setCreateUser(Constants.CREATE_USER_SYSTEM);
			operRecord.setIsDeleted(Constants.IS_DELETED_NO);
			if(memberService.insert(member)>0 && memberCardService.insert(card)>0 && memberCardService.distributionCard(member,card, rel, operRecord, cardFlowing)){
				return ResultCode.success("注册成功！", null);
			}else{
				return ResultCode.error("会员注册失败！", null);
			}
		}
	}
	
	/**
	 * 会员在线充值
	 * 成功：返回null
	 * 失败：返回错误信息
	 */
	public ResultData<Object> onlineRecharge(MemberCard memberCard){
		Date now = new Date();
		BigDecimal cashAmount = memberCard.getCashAmount();
		BigDecimal givedAmount = memberCard.getGivedAmount();
		String payedMethod =memberCard.getPayedMethod();
		
		memberCard = memberCardService.getById(memberCard.getId());
		if(null ==memberCard) return ResultCode.error("找不到有效的会员卡！", null);
		Member member = memberService.getMemberByCardId(memberCard.getId());
		if(null ==cashAmount && null==givedAmount) return ResultCode.error("金额为空！", null);
		cashAmount=(cashAmount==null?Constants.ZERO:cashAmount);
		givedAmount=(givedAmount==null?Constants.ZERO:givedAmount);
		if(null !=cashAmount && (cashAmount.compareTo(Constants.ZERO)==-1 ||cashAmount.compareTo(Constants.RECHARGE_MAX)==1)){
			return ResultCode.error("现金金额不能小于0或大于"+Constants.RECHARGE_MAX+"！", null);
		}
		if(null !=givedAmount && (givedAmount.compareTo(Constants.ZERO)==-1 ||givedAmount.compareTo(Constants.RECHARGE_MAX)==1)){
			return ResultCode.error("赠送金额不能小于0或大于"+Constants.RECHARGE_MAX+"！", null);
		}
		if(Constants.CARD_STATUS_NEW.equals(memberCard.getCardStatus()) || Constants.CARD_STATUS_BACKCARD.equals(memberCard.getCardStatus()) || Constants.CARD_STATUS_REPORTLOSS.equals(memberCard.getCardStatus())){
			return ResultCode.error("新卡\\退卡\\挂失状态的会员卡无法进行充值操作！", null);
		}
		if(null == member || null == memberCard)return ResultCode.error("卡信息异常！", null);
		try{
			//1、记录流水信息
			MemberCardFlowing cardFlowing = new MemberCardFlowing();
			cardFlowing.setId(UUID.getUUID32());
//			cardFlowing.setStoreId();
			cardFlowing.setCardId(memberCard.getId());
			cardFlowing.setCardNum(memberCard.getCardNumber());
			cardFlowing.setCashAmountBefore(memberCard.getCashAmount());
			cardFlowing.setCashAmountOper(cashAmount);
			cardFlowing.setCashAmountAfter(cardFlowing.getCashAmountBefore().add(cardFlowing.getCashAmountOper()));
			cardFlowing.setGivedAmountBefore(memberCard.getGivedAmount());
			cardFlowing.setGivedAmountOper(givedAmount);
			cardFlowing.setGivedAmountAfter(cardFlowing.getGivedAmountBefore().add(cardFlowing.getGivedAmountOper()));
			cardFlowing.setOperType(Constants.CARD_FLOWING_OPE_TYPE_RECHARGE);
			cardFlowing.setCreateTime(now);
			cardFlowing.setCreateUser(Constants.CREATE_USER_SYSTEM);
			cardFlowing.setIsDeleted(Constants.IS_DELETED_NO);
			cardFlowing.setPayedMethod(payedMethod);
			//2、卡信息更改
			memberCard.setCashAmount(memberCard.getCashAmount().add(cashAmount));
			memberCard.setGivedAmount(memberCard.getGivedAmount().add(givedAmount));
			memberCard.setTotalAmount(memberCard.getCashAmount().add(memberCard.getGivedAmount()));
			memberCard.setPayedMethod(payedMethod);
			memberCard.setUpdateTime(now);
			memberCard.setUpdateUser(Constants.CREATE_USER_SYSTEM);
			memberCard.setIsDeleted(Constants.IS_DELETED_NO);
			return memberCardService.doRecharge(memberCard, cardFlowing)?ResultCode.success("操作成功", null):ResultCode.error("操作失败", null);
		}catch(Exception e){
			return ResultCode.error("充值异常！", null);
		}
	}
	
	public ResultData<Object> changePassword(Member member){
		if(member == null || StringUtils.isBlank(member.getOldPassword()) || StringUtils.isBlank(member.getPassword()) || StringUtils.isBlank(member.getMobilePhone())){
			return ResultCode.error("输入信息不合法!", null);
		}
		member.setAppId((String)LocalThreadUtils.get(LocalThreadUtils.CONSTRANT_APP_ID));
		member.setOldPassword(MD5Util.encode(member.getOldPassword()));
		member.setPassword(MD5Util.encode(member.getPassword()));
		return memberService.changePassword(member)>0?ResultCode.success("操作成功", null):ResultCode.error("操作失败", null);
	}
	@Transactional(readOnly = false)
	public ResultData<Object> resetPassword(Member member){
		if(member == null || StringUtils.isBlank(member.getPassword()) || StringUtils.isBlank(member.getMobilePhone()) || StringUtils.isBlank(member.getCheckCode())){
			return ResultCode.error("输入信息不合法!", null);
		}
		//验证码验证
		String appId = (String)LocalThreadUtils.get(LocalThreadUtils.CONSTRANT_APP_ID);
		CheckCode c = new CheckCode();
		c.setAppId(appId);
		c.setTelephone(member.getMobilePhone());
		c.setCode(member.getCheckCode());
		c.setType(Constants.MSG_TYPE_RESET_PWD_O2O);
		if(!checkCodeService.ckeck(c)) return ResultCode.error("手机验证码错误或已失效！", null);
		member.setAppId((String)LocalThreadUtils.get(LocalThreadUtils.CONSTRANT_APP_ID));
		member.setPassword(MD5Util.encode(member.getPassword()));
		return memberService.resetPassword(member)>0?ResultCode.success("操作成功", null):ResultCode.error("操作失败", null);
	}
	
}

package com.ehyf.ewashing.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ehyf.ewashing.common.Constants;
import com.ehyf.ewashing.dao.MemberCardFlowingDao;
import com.ehyf.ewashing.entity.CardOperatorRecord;
import com.ehyf.ewashing.entity.Member;
import com.ehyf.ewashing.entity.MemberCard;
import com.ehyf.ewashing.entity.MemberCardFlowing;
import com.ehyf.ewashing.entity.MemberCardRelation;
import com.ehyf.ewashing.entity.SecurityUser;
import com.ehyf.ewashing.service.EwashingStoreService;
import com.ehyf.ewashing.service.MemberCardFlowingService;
import com.ehyf.ewashing.service.MemberCardRelationService;
import com.ehyf.ewashing.service.MemberCardService;
import com.ehyf.ewashing.service.MemberService;
import com.ehyf.ewashing.util.DateUtil;
import com.ehyf.ewashing.util.UUID;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("/memberCard")
public class MemberCardController {
	Logger logger =Logger.getLogger(MemberCardController.class);
	@Autowired
	EwashingStoreService storeService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private MemberCardService memberCardService;
	@Autowired
	MemberCardRelationService memberCardRelationService;
	@Autowired
	MemberCardFlowingService memberCardFlowingService;
	@Autowired
	MemberCardFlowingDao memberCardFlowingDao;
	
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(HttpServletRequest req, MemberCard memberCard, Model model){
		try {			
			String pageNum = req.getParameter("pageNum");
			String pageSize = req.getParameter("pageSize");
			memberCard.setCreateTime(DateUtil.parseData(req.getParameter("createTimeStr")));
			if (StringUtils.isBlank(pageNum)) {
				pageNum = "1";
			}
			if (StringUtils.isBlank(pageSize)) {
				pageSize = "10";
			}
			
			PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize));
			SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
			memberCard.setStoreId(loginUser.getEwashingStore()==null?null:loginUser.getEwashingStore().getId());
			List<MemberCard> list = memberCardService.findList(memberCard);
			PageInfo<MemberCard> page = new PageInfo<MemberCard>(list);
			
			model.addAttribute("page", page);
			model.addAttribute("member", null);
			model.addAttribute("createTimeStr",req.getParameter("createTimeStr"));
			return "ewashing/memberCard/list";
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}		
		return "ewashing/memberCard/list";
	}
	
	@RequestMapping(value = "/recharge", method = RequestMethod.GET)
	public String recharge(HttpServletRequest req, MemberCard memberCard, Model model){
		model.asMap().clear();
		memberCard = memberCardService.getById(memberCard.getId());
		if(memberCard == null) return msg(model, "卡不存在！", 0, "ewashing/memberCard/recharge");
		if(Constants.CARD_STATUS_NEW.equals(memberCard.getCardStatus())) return msg(model, "卡未关联用户信息，不能充值！", 0, "ewashing/memberCard/recharge");
		if(Constants.CARD_STATUS_CANCEL.equals(memberCard.getCardStatus())) return msg(model, "该卡已作废，不可充值！", 0, "ewashing/memberCard/recharge");
		if(Constants.CARD_STATUS_BACKCARD.equals(memberCard.getCardStatus())) return msg(model, "该卡已退卡，不可充值！", 0, "ewashing/memberCard/recharge");
		Member member = memberService.getMemberByCardId(memberCard.getId());
		model.addAttribute("member", member);
		model.addAttribute("memberCard",memberCard);
		return "ewashing/memberCard/recharge";
	}
	@RequestMapping(value = "/doRecharge", method = RequestMethod.POST)
	public String doRecharge(HttpServletRequest req, MemberCard memberCard, Model model){
		model.asMap().clear();
		Date now = new Date();
		BigDecimal cashAmount = memberCard.getCashAmount();
		BigDecimal givedAmount = memberCard.getGivedAmount();
		String payedMethod =memberCard.getPayedMethod();
		
		memberCard = memberCardService.getById(memberCard.getId());
		if(null ==memberCard) return msg(model,"找不到有效的会员卡！",0,"ewashing/memberCard/recharge");
		Member member = memberService.getMemberByCardId(memberCard.getId());
		model.addAttribute("member", member);
		model.addAttribute("memberCard",memberCard);
		if(null ==cashAmount && null==givedAmount) return msg(model,"金额为空！",0,"ewashing/memberCard/recharge");
		cashAmount=(cashAmount==null?Constants.ZERO:cashAmount);
		givedAmount=(givedAmount==null?Constants.ZERO:givedAmount);
		if(null !=cashAmount && (cashAmount.compareTo(Constants.ZERO)==-1 ||cashAmount.compareTo(Constants.RECHARGE_MAX)==1)){
			return msg(model,"金额不能小于0或大于"+Constants.RECHARGE_MAX+"！",0,"ewashing/memberCard/recharge");
		}
		if(null !=givedAmount && (givedAmount.compareTo(Constants.ZERO)==-1 ||givedAmount.compareTo(Constants.RECHARGE_MAX)==1)){
			return msg(model,"金额不能小于0或大于"+Constants.RECHARGE_MAX+"！",0,"ewashing/memberCard/recharge");
		}
		if(Constants.CARD_STATUS_NEW.equals(memberCard.getCardStatus()) || Constants.CARD_STATUS_BACKCARD.equals(memberCard.getCardStatus()) || Constants.CARD_STATUS_REPORTLOSS.equals(memberCard.getCardStatus())){
			return msg(model,"新卡\\退卡\\挂失状态的会员卡无法进行充值操作！",0,"ewashing/memberCard/recharge");
		}
		if(null == member || null == memberCard)return msg(model, "卡信息异常！", 0, "ewashing/memberCard/recharge");
		SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
		try{
			MemberCardFlowing cardFlowing = new MemberCardFlowing();
			cardFlowing.setId(UUID.getUUID32());
			cardFlowing.setStoreId(loginUser.getEwashingStore()==null?null:loginUser.getEwashingStore().getId());
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
			cardFlowing.setCreateUser(loginUser.getUsername());
			cardFlowing.setIsDeleted(Constants.IS_DELETED_NO);
			cardFlowing.setPayedMethod(payedMethod);
			memberCard.setCashAmount(memberCard.getCashAmount().add(cashAmount));
			memberCard.setGivedAmount(memberCard.getGivedAmount().add(givedAmount));
			memberCard.setTotalAmount(memberCard.getCashAmount().add(memberCard.getGivedAmount()));
			memberCard.setPayedMethod(payedMethod);
			memberCard.setUpdateTime(now);
			memberCard.setUpdateUser(loginUser.getId());
			memberCard.setIsDeleted(Constants.IS_DELETED_NO);
			boolean result = memberCardService.doRecharge(memberCard, cardFlowing);
			model.addAttribute("mcfId", cardFlowing.getId());
			return msg(model, result?"充值成功！":"充值失败！", result?1:0, "ewashing/memberCard/recharge");
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			return msg(model, "充值异常！", 0, "ewashing/memberCard/recharge");
		}
	}
	@RequestMapping(value = "/refund", method = RequestMethod.GET)
	public String refund(HttpServletRequest req, MemberCard memberCard, Model model){
		model.asMap().clear();
		memberCard = memberCardService.getById(memberCard.getId());
		if(memberCard == null) return msg(model, "卡不存在！", 0, "ewashing/memberCard/recharge");
		if(Constants.CARD_STATUS_NEW.equals(memberCard.getCardStatus())) return msg(model, "卡未关联用户信息，不能退款！", 0, "ewashing/memberCard/refund");
		if(Constants.CARD_STATUS_CANCEL.equals(memberCard.getCardStatus())) return msg(model, "该卡已作废，不能退款！", 0, "ewashing/memberCard/refund");
		if(Constants.CARD_STATUS_BACKCARD.equals(memberCard.getCardStatus())) return msg(model, "该卡已退卡，不能退款！", 0, "ewashing/memberCard/refund");
		Member member = memberService.getMemberByCardId(memberCard.getId());
		model.addAttribute("member", member);
		model.addAttribute("memberCard",memberCard);
		return "ewashing/memberCard/refund";
	}
	@RequestMapping(value = "/doRefund", method = RequestMethod.POST)
	public String doRefund(HttpServletRequest req, MemberCard memberCard, Model model){
		model.asMap().clear();
		Date now = new Date();
		BigDecimal cashAmount = memberCard.getCashAmount();
		BigDecimal givedAmount = memberCard.getGivedAmount();
		String payedMethod =memberCard.getPayedMethod();
		
		memberCard = memberCardService.getById(memberCard.getId());
		if(null == memberCard) return msg(model,"找不到有效的会员卡！",0,"ewashing/memberCard/recharge");
		Member member = memberService.getMemberByCardId(memberCard.getId());
		model.addAttribute("member", member);
		model.addAttribute("memberCard",memberCard);
		if(null ==cashAmount && null==givedAmount) return msg(model,"金额为空！",0,"ewashing/memberCard/recharge");
		cashAmount=(cashAmount==null?Constants.ZERO:cashAmount);
		givedAmount=(givedAmount==null?Constants.ZERO:givedAmount);
		if(null !=cashAmount && (cashAmount.compareTo(Constants.ZERO)==-1 ||cashAmount.compareTo(Constants.RECHARGE_MAX)==1)){
			return msg(model,"金额不能小于0或大于10000！",0,"ewashing/memberCard/recharge");
		}
		if(null !=givedAmount && (givedAmount.compareTo(Constants.ZERO)==-1 ||givedAmount.compareTo(Constants.RECHARGE_MAX)==1)){
			return msg(model,"金额不能小于0或大于10000！",0,"ewashing/memberCard/recharge");
		}
		if(Constants.CARD_STATUS_NEW.equals(memberCard.getCardStatus()) || Constants.CARD_STATUS_BACKCARD.equals(memberCard.getCardStatus()) || Constants.CARD_STATUS_REPORTLOSS.equals(memberCard.getCardStatus())){
			return msg(model,"新卡、退卡、挂失状态的会员卡无法进行退款操作！",0,"ewashing/memberCard/refund");
		}
		if(null!=memberCard && (memberCard.getCashAmount().compareTo(cashAmount)==-1 || memberCard.getGivedAmount().compareTo(givedAmount)==-1)){
			return msg(model,"退款金额超过卡余额，无法退款！",0,"ewashing/memberCard/refund");
		}
		if(null == member || null == memberCard)return msg(model, "卡信息异常！", 0, "ewashing/memberCard/refund");
		SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
		try{
			MemberCardFlowing cardFlowing = new MemberCardFlowing();
			cardFlowing.setId(UUID.getUUID32());
			cardFlowing.setStoreId(loginUser.getEwashingStore()==null?null:loginUser.getEwashingStore().getId());
			cardFlowing.setCardId(memberCard.getId());
			cardFlowing.setCardNum(memberCard.getCardNumber());
			cardFlowing.setCashAmountBefore(memberCard.getCashAmount());
			cardFlowing.setCashAmountOper(cashAmount.negate());
			cardFlowing.setCashAmountAfter(cardFlowing.getCashAmountBefore().add(cardFlowing.getCashAmountOper()));
			cardFlowing.setGivedAmountBefore(memberCard.getGivedAmount());
			cardFlowing.setGivedAmountOper(givedAmount.negate());
			cardFlowing.setGivedAmountAfter(cardFlowing.getGivedAmountBefore().add(cardFlowing.getGivedAmountOper()));
			cardFlowing.setOperType(Constants.CARD_FLOWING_OPE_TYPE_REFUND);
			cardFlowing.setCreateTime(now);
			cardFlowing.setCreateUser(loginUser.getUsername());
			cardFlowing.setIsDeleted(Constants.IS_DELETED_NO);
			cardFlowing.setPayedMethod(payedMethod);
			memberCard.setCashAmount(memberCard.getCashAmount().subtract(cashAmount));
			memberCard.setGivedAmount(memberCard.getGivedAmount().subtract(givedAmount));
			memberCard.setTotalAmount(memberCard.getCashAmount().add(memberCard.getGivedAmount()));
			memberCard.setPayedMethod(payedMethod);
			memberCard.setUpdateTime(now);
			memberCard.setUpdateUser(loginUser.getId());
			memberCard.setIsDeleted(Constants.IS_DELETED_NO);
			memberCardService.doRecharge(memberCard, cardFlowing);
			return msg(model, "退款成功！", 1, "ewashing/memberCard/refund");
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			return msg(model, "退款异常！", 0, "ewashing/memberCard/refund");
		}
	
	}
	@RequestMapping(value = "/reportLoss", method = RequestMethod.POST)
	@ResponseBody
	public String reportLoss(HttpServletRequest req, MemberCard memberCard, Model model){
		model.asMap().clear();
		try{
			if(memberCard==null || StringUtils.isBlank(memberCard.getId())){
				model.addAttribute("resultMsg", "请选择要操作的会员卡！");
				model.addAttribute("resultCode", 0);
				return new ObjectMapper().writeValueAsString(model);
			}
			SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
			memberCard = memberCardService.getById(memberCard.getId());
			if(!Constants.CARD_STATUS_NORMAL.equals(memberCard.getCardStatus()) && !Constants.CARD_STATUS_FILLCARD.equals(memberCard.getCardStatus())){
				model.addAttribute("resultMsg", "卡状态异常！");
				model.addAttribute("resultCode", 0);
				return new ObjectMapper().writeValueAsString(model);
			}
			CardOperatorRecord operRecord = newCardOperRecord(memberCard, loginUser);
			operRecord.setOperatorType(Constants.CARD_OPERATOR_TYPE_REPORTLOSS);
			operRecord.setStatus(Constants.CARD_OPERATOR_STATUS_APPROVED);
			boolean result = memberCardService.reportLoss(memberCard.getId(), operRecord);
			model.addAttribute("resultMsg", result?"操作成功！":"操作失败");
			model.addAttribute("resultCode", result?1:0);
			return new ObjectMapper().writeValueAsString(model);
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			model.addAttribute("resultMsg", "操作失败！");
			model.addAttribute("resultCode", 1);
		}
		return null;
	}
	
	@RequestMapping(value = "/unReportLoss", method = RequestMethod.POST)
	@ResponseBody
	public String unReportLoss(HttpServletRequest req, MemberCard memberCard, Model model){
		model.asMap().clear();
		if(memberCard==null || StringUtils.isBlank(memberCard.getId()))return msg(model, "请选择要操作的会员卡！", 0, null);
		SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
		try{
			memberCard = memberCardService.getById(memberCard.getId());
			CardOperatorRecord operRecord = newCardOperRecord(memberCard, loginUser);
			operRecord.setOperatorType(Constants.CARD_OPERATOR_TYPE_UNREPORTLOSS);
			operRecord.setStatus(Constants.CARD_OPERATOR_STATUS_APPROVED);
			boolean result = memberCardService.unReportLoss(memberCard.getId(), operRecord);
			model.addAttribute("resultMsg", result?"操作成功！":"操作失败");
			model.addAttribute("resultCode", result?1:0);
			return new ObjectMapper().writeValueAsString(model);
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			model.addAttribute("resultMsg", "操作失败！");
			model.addAttribute("resultCode", 1);
		}
		return null;
	}

	@RequestMapping(value = "/reback", method = RequestMethod.POST)
	@ResponseBody
	public String reback(HttpServletRequest req, MemberCard memberCard, Model model){
		model.asMap().clear();
		try{
			if(memberCard==null || StringUtils.isBlank(memberCard.getId())){
				model.addAttribute("resultMsg", "请选择要操作的会员卡！");
				model.addAttribute("resultCode", 0);
				return new ObjectMapper().writeValueAsString(model);
			}
			SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
			memberCard = memberCardService.getById(memberCard.getId());
			CardOperatorRecord operRecord = newCardOperRecord(memberCard, loginUser);
			operRecord.setOperatorType(Constants.CARD_OPERATOR_TYPE_REBACK);
			operRecord.setStatus(Constants.CARD_OPERATOR_STATUS_APPROVED);
			boolean result = memberCardService.reback(memberCard.getId(), operRecord);
			model.addAttribute("resultMsg", result?"操作成功！":"操作失败");
			model.addAttribute("resultCode", result?1:0);
			return new ObjectMapper().writeValueAsString(model);
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			model.addAttribute("resultMsg", "操作失败！");
			model.addAttribute("resultCode", 0);
		}
		return null;
	}
	@RequestMapping(value = "/fill", method = RequestMethod.GET)
	public String fill(HttpServletRequest req, MemberCard memberCard, Model model){
		model.asMap().clear();
		memberCard = memberCardService.getById(memberCard.getId());
		Member member = memberService.getMemberByCardId(memberCard.getId());
		if(null == member || null == memberCard) return msg(model, "卡信息异常！", 0, "ewashing/memberCard/fill");
		model.addAttribute("member", member);
		model.addAttribute("memberCard",memberCard);
		model.addAttribute("cardNos",memberCardService.getNewCardNos(member.getStoreId()));
		return "ewashing/memberCard/fill";
	}
	@RequestMapping(value = "/doFill", method = RequestMethod.POST)
	public String doFill(HttpServletRequest req, MemberCard memberCard, Model model){
		model.asMap().clear();
		Date now = new Date();
		SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
		memberCard = memberCardService.getById(memberCard.getId());
		Member member = memberService.getMemberByCardId(memberCard.getId());
		String newCardNo=req.getParameter("newCardNumber");
		model.addAttribute("member", member);
		model.addAttribute("memberCard",memberCard);
		if(StringUtils.isBlank(newCardNo) || null == member || null == memberCard) return msg(model, "卡信息异常！", 0, "ewashing/memberCard/fill");
		MemberCard newCard=memberCardService.getCardByNo(newCardNo);
		if(!newCard.getCardStatus().equals(Constants.CARD_STATUS_NEW))return msg(model, "卡状态异常！", 0, "ewashing/memberCard/fill");
		MemberCardFlowing oldCardFlowing = new MemberCardFlowing();
		oldCardFlowing.setId(UUID.getUUID32());
		oldCardFlowing.setStoreId(loginUser.getEwashingStore()==null?null:loginUser.getEwashingStore().getId());
		oldCardFlowing.setCardId(memberCard.getId());
		oldCardFlowing.setCardNum(memberCard.getCardNumber());
		oldCardFlowing.setCashAmountBefore(memberCard.getCashAmount());
		oldCardFlowing.setCashAmountOper(memberCard.getCashAmount().negate());
		oldCardFlowing.setCashAmountAfter(oldCardFlowing.getCashAmountBefore().add(oldCardFlowing.getCashAmountOper()));
		oldCardFlowing.setGivedAmountBefore(memberCard.getGivedAmount());
		oldCardFlowing.setGivedAmountOper(memberCard.getGivedAmount().negate());
		oldCardFlowing.setGivedAmountAfter(oldCardFlowing.getGivedAmountBefore().add(oldCardFlowing.getGivedAmountOper()));
		oldCardFlowing.setOperType(Constants.CARD_FLOWING_OPE_TYPE_REFUND);//原卡冲抵，操作类型当作“退款”处理
		oldCardFlowing.setCreateTime(now);
		oldCardFlowing.setCreateUser(loginUser.getUsername());
		oldCardFlowing.setIsDeleted(Constants.IS_DELETED_NO);
		oldCardFlowing.setPayedMethod(memberCard.getPayedMethod());
		MemberCardFlowing newCardFlowing = new MemberCardFlowing();
		newCardFlowing.setId(UUID.getUUID32());
		newCardFlowing.setStoreId(loginUser.getEwashingStore()==null?null:loginUser.getEwashingStore().getId());
		newCardFlowing.setCardId(newCard.getId());
		newCardFlowing.setCardNum(newCard.getCardNumber());
		newCardFlowing.setCashAmountBefore(Constants.ZERO);
		newCardFlowing.setCashAmountOper(memberCard.getCashAmount());
		newCardFlowing.setCashAmountAfter(newCardFlowing.getCashAmountBefore().add(newCardFlowing.getCashAmountOper()));
		newCardFlowing.setGivedAmountBefore(Constants.ZERO);
		newCardFlowing.setGivedAmountOper(memberCard.getGivedAmount());
		newCardFlowing.setGivedAmountAfter(newCardFlowing.getGivedAmountBefore().add(newCardFlowing.getGivedAmountOper()));
		newCardFlowing.setOperType(Constants.CARD_FLOWING_OPE_TYPE_RECHARGE);//旧卡账户余额转入新卡，当作“充值”业务
		newCardFlowing.setCreateTime(now);
		newCardFlowing.setCreateUser(loginUser.getUsername());
		newCardFlowing.setIsDeleted(Constants.IS_DELETED_NO);
		newCardFlowing.setPayedMethod(memberCard.getPayedMethod());
		newCard.setCardStatus(Constants.CARD_STATUS_FILLCARD);
		newCard.setSrcCardNumber(memberCard.getCardNumber());
		newCard.setCashAmount(memberCard.getCashAmount());
		newCard.setGivedAmount(memberCard.getGivedAmount());
		newCard.setTotalAmount(memberCard.getTotalAmount());
		newCard.setDiscount(memberCard.getDiscount());
		newCard.setPayedMethod(memberCard.getPayedMethod());
		newCard.setIsUsePassword(memberCard.getIsUsePassword());
		newCard.setPassword(memberCard.getPassword());
		newCard.setIsInvoiced(memberCard.getIsInvoiced());
		newCard.setInvoiceRemark(memberCard.getInvoiceRemark());
		memberCard.setCashAmount(Constants.ZERO);
		memberCard.setGivedAmount(Constants.ZERO);
		memberCard.setTotalAmount(Constants.ZERO);
		memberCard.setUpdateTime(now);
		memberCard.setUpdateUser(loginUser.getId());
		memberCard.setCardStatus(Constants.CARD_STATUS_CANCEL);
		MemberCardRelation rel = new MemberCardRelation();
		rel.setCardId(newCard.getId());
		rel.setMemberId(member.getId());
		CardOperatorRecord operRecord = newCardOperRecord(newCard, loginUser);
		operRecord.setOperatorType(Constants.CARD_OPERATOR_TYPE_FILL);
		operRecord.setStatus(Constants.CARD_OPERATOR_STATUS_APPROVED);
		boolean result = memberCardService.fillCard(oldCardFlowing,newCardFlowing,memberCard, newCard, operRecord,rel);
		return msg(model, result?"操作成功！":"操作失败！", result?1:0, "ewashing/memberCard/fill");
	}
	@RequestMapping(value = "/queryCardFlowing", method = RequestMethod.GET)
	public String queryCardFlowing(HttpServletRequest req, MemberCard memberCard, Model model){
		model.asMap().clear();
		String cardId = req.getParameter("id");
		String operType = req.getParameter("operType");
		if(StringUtils.isBlank(cardId) || StringUtils.isBlank(operType)) return msg(model, "参数非法！", 0, "ewashing/memberCard/rechargeHistory");
		String pageNum = req.getParameter("pageNum");
		String pageSize = req.getParameter("pageSize");
		if (StringUtils.isBlank(pageNum)) {
			pageNum = "1";
		}
		if (StringUtils.isBlank(pageSize)) {
			pageSize = "10";
		}
		PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize));
		MemberCardFlowing cardFlowing = new MemberCardFlowing();
		cardFlowing.setCardId(cardId);
		cardFlowing.setOperType(operType);
		List<MemberCardFlowing> list = memberCardFlowingService.findList(cardFlowing);
		PageInfo<MemberCardFlowing> page = new PageInfo<MemberCardFlowing>(list);
		model.addAttribute("page", page);
		model.addAttribute("id",cardId);
		model.addAttribute("operType",operType);
		return "ewashing/memberCard/rechargeHistory";
	}
	
	
	@RequestMapping(value = "/printRechargeTicket", method = RequestMethod.GET)
	public String printRechargeTicket(HttpServletRequest req, MemberCard memberCard, Model model){
		try {			
			model.addAttribute("currentTime", new Date());
			String mcfId = (String)req.getParameter("mcfId");
			if(StringUtils.isNotBlank(mcfId)){
				MemberCardFlowing flowing = memberCardFlowingDao.getById(mcfId);
				if(null != flowing){
					model.addAttribute("memberCardFlowing", flowing);
					model.addAttribute("store", storeService.getById(flowing.getStoreId()));
					MemberCard card = memberCardService.getById(flowing.getCardId());
					if(null != card){
						model.addAttribute("memberCard", card);
						Member member = memberService.getById(card.getMemberId());
						if(null != member){
							model.addAttribute("member", member);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}		
		return "ewashing/memberCard/printRechargeTicket";
	}
	
	
	private String msg(Model model,String mes,int code,String redirect) {
		model.addAttribute("resultMsg", mes);
		model.addAttribute("resultCode", code);
		return redirect;
	}
	private CardOperatorRecord newCardOperRecord(MemberCard memberCard, SecurityUser loginUser) {
		CardOperatorRecord operRecord = new CardOperatorRecord();
		operRecord.setId(UUID.getUUID32());
		operRecord.setCardId(memberCard.getId());
		operRecord.setStoreId(loginUser.getEwashingStore()==null?null:loginUser.getEwashingStore().getId());
		operRecord.setCardNum(memberCard.getCardNumber());
		operRecord.setApprover(loginUser.getUsername());
		operRecord.setApprovedTime(new Date());
		operRecord.setCreateTime(new Date());
		operRecord.setCreateUser(loginUser.getUsername());
		operRecord.setIsDeleted(Constants.IS_DELETED_NO);
		return operRecord;
	}
	
}

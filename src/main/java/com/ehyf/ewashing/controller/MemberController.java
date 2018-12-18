package com.ehyf.ewashing.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ehyf.ewashing.common.Constants;
import com.ehyf.ewashing.entity.CardOperatorRecord;
import com.ehyf.ewashing.entity.Member;
import com.ehyf.ewashing.entity.MemberCard;
import com.ehyf.ewashing.entity.MemberCardFlowing;
import com.ehyf.ewashing.entity.MemberCardRelation;
import com.ehyf.ewashing.entity.SecurityUser;
import com.ehyf.ewashing.service.MemberCardRelationService;
import com.ehyf.ewashing.service.MemberCardService;
import com.ehyf.ewashing.service.MemberService;
import com.ehyf.ewashing.util.DateUtil;
import com.ehyf.ewashing.util.MD5Util;
import com.ehyf.ewashing.util.UUID;
import com.ehyf.ewashing.vo.MemberVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("/member")
public class MemberController {
	Logger logger =Logger.getLogger(MemberController.class);
	
	@Autowired
	private MemberService memberService;
	@Autowired
	private MemberCardService memberCardService;
	@Autowired
	MemberCardRelationService memberCardRelationService;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(HttpServletRequest req, Member member, Model model){
		try {			
			String pageNum = req.getParameter("pageNum");
			String pageSize = req.getParameter("pageSize");
			if (StringUtils.isBlank(pageNum)) {
				pageNum = "1";
			}
			if (StringUtils.isBlank(pageSize)) {
				pageSize = "10";
			}
			
			PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize));
			SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
			member.setStoreId(loginUser.getEwashingStore()==null?null:loginUser.getEwashingStore().getId());
			List<Member> list = memberService.findList(member);
			PageInfo<Member> page = new PageInfo<Member>(list);
			
			model.addAttribute("page", page);
			model.addAttribute("member", member);
			return "ewashing/member/list";
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}		
		return "";
	}
	
	@RequestMapping(value = "/beforeAdd",method = {RequestMethod.POST, RequestMethod.GET})
	public String beforeAdd(HttpServletRequest req, Member member,HttpSession session,
			Model model) {
		SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
		member.setStoreName(loginUser.getEwashingStore()==null?null:loginUser.getEwashingStore().getStoreName());
		model.addAttribute("flag", Constants.FLAG_ADD);
		return "ewashing/member/addMember";
	}
	
	
	@RequestMapping(value = "/saveMember",method = RequestMethod.POST)
	public String saveMember(HttpServletRequest req, Member member,HttpSession session,
			Model model) {
		model.addAttribute("flag", Constants.FLAG_ADD);
		SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
		member.setId(UUID.getUUID32());
		member.setCreateTime(new Date());
		member.setCreateUser(loginUser.getUsername());
		member.setIsDeleted(Constants.IS_DELETED_NO);
		member.setStoreId(loginUser.getEwashingStore()==null?null:loginUser.getEwashingStore().getId());
		member.setBirthday(DateUtil.parseData(req.getParameter("birthdays")));
		member.setClientType(Constants.MEMBER_CLIENT_TYPE_PC);//PC用户
		member.setPassword(MD5Util.encode(Constants.INIT_PASSWORD));
		try {
			Member m = new Member();
			m.setMobilePhone(member.getMobilePhone());
			List<Member> list =memberService.findList(m);
			if(!CollectionUtils.isEmpty(list)){
				model.addAttribute("resultMsg", "手机号重复！");
				model.addAttribute("resultCode", 0);
			}
			else
			{
				memberService.insert(member);
				model.addAttribute("resultMsg", "添加会员成功！");
				model.addAttribute("resultCode", 1);
			}
		}
		catch(Exception e){
			model.addAttribute("resultMsg", "添加会员失败！");
			model.addAttribute("resultCode", 0);
		}
		return "ewashing/member/addMember";
	}
	
	@RequestMapping(value = "/beforeUpdate",method = {RequestMethod.POST, RequestMethod.GET})
	public String beforeUpdate(HttpServletRequest req, Member member,HttpSession session,
			Model model) {
		member = memberService.getById(member.getId());
		model.addAttribute("member", member);
		model.addAttribute("flag", Constants.FLAG_UPD);
		return "ewashing/member/addMember";
	}
	
	
	@RequestMapping(value = "/updateMember",method = RequestMethod.POST)
	public String updateMember(HttpServletRequest req, Member member,HttpSession session,
			Model model) {
		model.addAttribute("flag", Constants.FLAG_UPD);
		SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
		member.setUpdateTime(new Date());
		member.setUpdateUser(loginUser.getEwashingStore()==null?null:loginUser.getEwashingStore().getStoreName());
		member.setBirthday(DateUtil.parseData(req.getParameter("birthdays")));
		try {
			memberService.update(member);
			model.addAttribute("resultMsg", "修改会员成功！");
			model.addAttribute("resultCode", 1);
		}
		catch(Exception e){
			model.addAttribute("resultMsg", "修改会员失败！");
			model.addAttribute("resultCode", 0);
		}
		return "ewashing/member/addMember";
	}
	@RequestMapping(value = "/delMember",method = RequestMethod.POST)
	@ResponseBody
	public String delMember(HttpServletRequest req, Member member,HttpSession session,
			Model model) {
		model.asMap().clear();
		try {
			memberService.deleteById(member.getId());
			model.addAttribute("resultMsg", "删除会员成功！");
			model.addAttribute("resultCode", 1);
			return new ObjectMapper().writeValueAsString(model);
		}
		catch(Exception e){
			model.addAttribute("resultMsg", "删除会员失败！");
			model.addAttribute("resultCode", 0);
		}
		return null;
	}
	
	//会员发卡
	@RequestMapping(value = "/beforeDistributionCard",method = RequestMethod.GET)
	public String beforeDistributionCard(HttpServletRequest req, Member member,HttpSession session,
			Model model) {
		model.asMap().clear();
		member = memberService.getById(member.getId());
		MemberCard memberCard = memberCardService.queryCardByMemberId(member.getId());
		if(memberCard!=null){
			model.addAttribute("resultMsg", "该会员已绑定会员卡！");
			model.addAttribute("resultCode", 2);
			return "ewashing/member/distributionCard";
		}
		model.addAttribute("member", member);
		model.addAttribute("cardNos",memberCardService.getNewCardNos(member.getStoreId()));
		return "ewashing/member/distributionCard";
	}
	
	@RequestMapping(value = "/distributionCard",method = RequestMethod.POST)
	public String distributionCard(HttpServletRequest req, MemberVo memberVo,HttpSession session,
			Model model) {
		model.asMap().clear();
		Date now=new Date();
		SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
		Member member =memberService.getById(memberVo.getMemberId());
		MemberCard card= memberCardService.getCardByNo(memberVo.getCardNumber());
		model.addAttribute("member", member);
		model.addAttribute("cardNos",memberCardService.getNewCardNos(member.getStoreId()));
		BigDecimal cashAmount = memberVo.getCashAmount();
		BigDecimal givedAmount = memberVo.getGivedAmount();
		if(cashAmount==null && givedAmount==null){
			model.addAttribute("resultMsg", "金额不能为空！");
			model.addAttribute("resultCode", 0);
			return "ewashing/member/distributionCard";
		}
		if(cashAmount==null) cashAmount = zero;
		if(givedAmount==null)givedAmount = zero;
		if(null!=member && null != card){
			if(!Constants.CARD_STATUS_NEW.equals(card.getCardStatus())){
				model.addAttribute("resultMsg", "卡状态异常！");
				model.addAttribute("resultCode", 0);
				return "ewashing/member/distributionCard";
			}
			MemberCard memberCard = memberCardService.queryCardByMemberId(member.getId());
			if(null != memberCard){
				model.addAttribute("resultMsg", "用户已绑定会员卡！");
				model.addAttribute("resultCode", 0);
				return "ewashing/member/distributionCard";
			}
			member.setType(Constants.MEMBER_TYPE_CARD);
			MemberCardFlowing cardFlowing = new MemberCardFlowing();
			cardFlowing.setId(UUID.getUUID32());
			cardFlowing.setStoreId(loginUser.getEwashingStore()==null?null:loginUser.getEwashingStore().getId());
			cardFlowing.setCardId(card.getId());
			cardFlowing.setCardNum(card.getCardNumber());
			cardFlowing.setCashAmountBefore(card.getCashAmount());
			cardFlowing.setCashAmountOper(cashAmount);
			cardFlowing.setCashAmountAfter(cardFlowing.getCashAmountBefore().add(cardFlowing.getCashAmountOper()));
			cardFlowing.setGivedAmountBefore(card.getGivedAmount());
			cardFlowing.setGivedAmountOper(givedAmount);
			cardFlowing.setGivedAmountAfter(cardFlowing.getGivedAmountBefore().add(cardFlowing.getGivedAmountOper()));
			cardFlowing.setOperType(Constants.CARD_FLOWING_OPE_TYPE_RECHARGE);
			cardFlowing.setCreateTime(now);
			cardFlowing.setCreateUser(loginUser.getUsername());
			cardFlowing.setIsDeleted(Constants.IS_DELETED_NO);
			cardFlowing.setPayedMethod(memberVo.getPayedMethod());
			card.setCashAmount(card.getCashAmount().add(cashAmount));
			card.setGivedAmount(card.getGivedAmount().add(givedAmount));
			card.setTotalAmount(card.getCashAmount().add(card.getGivedAmount()));
			card.setPayedMethod(memberVo.getPayedMethod());
			card.setUpdateTime(now);
			card.setUpdateUser(loginUser.getId());
			card.setIsDeleted(Constants.IS_DELETED_NO);
			card.setCardStatus(Constants.CARD_STATUS_NORMAL);
			MemberCardRelation rel = new MemberCardRelation();
			rel.setCardId(card.getId());
			rel.setMemberId(memberVo.getMemberId());
			CardOperatorRecord operRecord = new CardOperatorRecord();
			operRecord.setId(UUID.getUUID32());
			operRecord.setCardId(card.getId());
			operRecord.setStoreId(loginUser.getEwashingStore()==null?null:loginUser.getEwashingStore().getId());
			operRecord.setCardNum(card.getCardNumber());
			operRecord.setOperatorType(Constants.CARD_OPERATOR_TYPE_GRANT);
			operRecord.setStatus(Constants.CARD_OPERATOR_STATUS_APPROVED);
			operRecord.setApprover(loginUser.getUsername());
			operRecord.setApprovedTime(now);
			operRecord.setCreateTime(now);
			operRecord.setCreateUser(loginUser.getUsername());
			operRecord.setIsDeleted(Constants.IS_DELETED_NO);
			if(memberCardService.distributionCard(member,card, rel, operRecord, cardFlowing)){
				model.addAttribute("resultMsg", "会员发卡成功！");
				model.addAttribute("resultCode", 1);
			}else{
				model.addAttribute("resultMsg", "会员发卡失败！");
				model.addAttribute("resultCode", 0);
			}
		}else{
			model.addAttribute("resultMsg", "数据异常！");
			model.addAttribute("resultCode", 0);
		}
		return "ewashing/member/distributionCard";
	}
	private static final BigDecimal zero= BigDecimal.valueOf(0);
}

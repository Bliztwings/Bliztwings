package com.ehyf.ewashing.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ehyf.ewashing.common.Constants;
import com.ehyf.ewashing.entity.CardApplyRecord;
import com.ehyf.ewashing.entity.SecurityUser;
import com.ehyf.ewashing.service.CardApplyRecordService;
import com.ehyf.ewashing.util.DateUtil;
import com.ehyf.ewashing.util.UUID;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("/memberCardApply")
public class MemberCardApplyController {
	Logger logger =Logger.getLogger(MemberCardApplyController.class);
	
	@Autowired
	private CardApplyRecordService cardApplyRecordService;
	
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(HttpServletRequest req, CardApplyRecord cardApplyRecord, Model model){
		try {			
			String pageNum = req.getParameter("pageNum");
			String pageSize = req.getParameter("pageSize");
			cardApplyRecord.setCreateTimeBegin(DateUtil.parseData(req.getParameter("createTimeBeginStr")));
			cardApplyRecord.setCreateTimeEnd(DateUtil.parseData(req.getParameter("createTimeEndStr")));
			if (StringUtils.isBlank(pageNum)) {
				pageNum = "1";
			}
			if (StringUtils.isBlank(pageSize)) {
				pageSize = "10";
			}
			
			PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize));
			SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
			List<CardApplyRecord> list = null;
			if(StringUtils.isNoneBlank(loginUser.getStoreId())){
				cardApplyRecord.setStoreId(loginUser.getStoreId());//各门店只能看到自己的申请记录，管理员可以看到所有门店申请记录
				list = cardApplyRecordService.findList(cardApplyRecord);
			}
			PageInfo<CardApplyRecord> page = new PageInfo<CardApplyRecord>(list);
			model.addAttribute("page", page);
			model.addAttribute("createTimeBeginStr",req.getParameter("createTimeBeginStr"));
			model.addAttribute("createTimeEndStr",req.getParameter("createTimeEndStr"));
			return "ewashing/memberCardApply/applyList";
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}		
		return "ewashing/memberCardApply/applyList";
	}
	
	@RequestMapping(value = "/receivedCard", method = RequestMethod.POST)
	@ResponseBody
	public String receivedCard(HttpServletRequest req, CardApplyRecord cardApplyRecord, Model model) throws Exception{
		model.asMap().clear();
		try{
			if(cardApplyRecord==null || StringUtils.isBlank(cardApplyRecord.getId())){
				model.addAttribute("resultMsg", "请选择要操作的申请记录！");
				model.addAttribute("resultCode",0);
				return new ObjectMapper().writeValueAsString(model);
			}
			SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
			cardApplyRecord = cardApplyRecordService.getById(cardApplyRecord.getId());
			if(!cardApplyRecord.getStatus().equals(Constants.CARD_APPLY_STATUS_APPROVED)){
				model.addAttribute("resultMsg", "该批卡还未审批，不可确认收卡！");
				model.addAttribute("resultCode",0);
				return new ObjectMapper().writeValueAsString(model);
			}
			if(!cardApplyRecordService.validateCardNumber(cardApplyRecord.getNumberBegin(), cardApplyRecord.getNumberEnd())) {
				model.addAttribute("resultMsg", "该卡区间已存在相同卡号，操作失败！");
				model.addAttribute("resultCode",0);
				return new ObjectMapper().writeValueAsString(model);
			}
			cardApplyRecord.setStatus(Constants.CARD_APPLY_STATUS_RECEIVED);
			cardApplyRecord.setUpdateUser(loginUser.getUsername());
			cardApplyRecord.setUpdateTime(new Date());
			boolean result = cardApplyRecordService.update(cardApplyRecord)>=1 && cardApplyRecordService.batchGenerateCards(cardApplyRecord)?true:false;
			model.addAttribute("resultMsg", result?"操作成功！":"操作失败");
			model.addAttribute("resultCode", result?1:0);
			return new ObjectMapper().writeValueAsString(model);
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			model.addAttribute("resultMsg", "操作失败！");
			model.addAttribute("resultCode", 1);
		}
		return new ObjectMapper().writeValueAsString(model);
	}
	
	
	@RequestMapping(value = "/beforeApply", method = {RequestMethod.POST, RequestMethod.GET})
	public String beforeApply(HttpServletRequest req, CardApplyRecord cardApplyRecord, Model model){
		SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
		cardApplyRecord.setCardSupplier(Constants.CARD_SUPPLIER);
		cardApplyRecord.setApplyer(loginUser.getUsername());
		cardApplyRecord.setStoreName(loginUser.getEwashingStore()==null?null:loginUser.getEwashingStore().getStoreName());
		model.addAttribute("flag", Constants.FLAG_ADD);
		return "ewashing/memberCardApply/memberCardApply";
	}
	
	@RequestMapping(value = "/saveMemberCardApply", method = {RequestMethod.POST, RequestMethod.POST})
	public String saveMemberCardApply(HttpServletRequest req, CardApplyRecord cardApplyRecord, Model model){
		model.addAttribute("flag", Constants.FLAG_ADD);
		if(StringUtils.isBlank(cardApplyRecord.getNumberBegin()) || StringUtils.isBlank(cardApplyRecord.getNumberEnd())) return msg(model, "开始编号和结束编号不能为空！", 0, "ewashing/memberCardApply/memberCardApply");
		if(!cardApplyRecord.getNumberBegin().matches(number_regx) || !cardApplyRecord.getNumberEnd().matches(number_regx)) return msg(model, "开始编号和结束编号必须为非0开头的数字，且最大不能超过15位！", 0, "ewashing/memberCardApply/memberCardApply");
		Long begin = Long.parseLong(cardApplyRecord.getNumberBegin());
		Long end = Long.parseLong(cardApplyRecord.getNumberEnd());
		if(begin>end) return msg(model, "开始编号必须大于或等于结束编号！", 0, "ewashing/memberCardApply/memberCardApply");
		if(!cardApplyRecordService.validateCardNumber(cardApplyRecord.getNumberBegin(), cardApplyRecord.getNumberEnd())) return msg(model, "该卡区间已存在相同卡号，操作失败！", 0, "ewashing/memberCardApply/memberCardApply");
		SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
		cardApplyRecord.setId(UUID.getUUID32());
		cardApplyRecord.setCreateTime(new Date());
		cardApplyRecord.setCreateUser(loginUser.getUsername());
		cardApplyRecord.setIsDeleted(Constants.IS_DELETED_NO);
		cardApplyRecord.setStoreId(loginUser.getEwashingStore()==null?null:loginUser.getEwashingStore().getId());
		cardApplyRecord.setCardSupplier(Constants.CARD_SUPPLIER);
		Long count = 1+end-begin;
		cardApplyRecord.setApplyNumber(count.intValue());
		cardApplyRecord.setApplyer(loginUser.getUsername());
		cardApplyRecord.setStatus(Constants.CARD_APPLY_STATUS_UNAPPROVED);
		try {
			cardApplyRecordService.insert(cardApplyRecord);
			return msg(model, "会员卡提交申请成功！", 1, "ewashing/memberCardApply/memberCardApply");
		}
		catch(Exception e){
			return msg(model, "会员卡提交申请失败！", 0, "ewashing/memberCardApply/memberCardApply");
		}
	}
	
	@RequestMapping(value = "/beforeUpdate",method = {RequestMethod.POST, RequestMethod.GET})
	public String beforeUpdate(HttpServletRequest req, CardApplyRecord cardApplyRecord,HttpSession session,
			Model model) {
		cardApplyRecord = cardApplyRecordService.getById(cardApplyRecord.getId());
		if(!Constants.CARD_APPLY_STATUS_UNAPPROVED.equals(cardApplyRecord.getStatus())) {
			return msg(model, "只有未审批过的单子才能执行修改操作！", 0, "ewashing/memberCardApply/applyList");
		}else{
			model.addAttribute("cardApplyRecord", cardApplyRecord);
			model.addAttribute("flag", Constants.FLAG_UPD);
			return "ewashing/memberCardApply/memberCardApply";
		}
	}
	
	
	@RequestMapping(value = "/update",method = RequestMethod.POST)
	public String update(HttpServletRequest req, CardApplyRecord cardApplyRecord,HttpSession session,
			Model model) {
		model.addAttribute("flag", Constants.FLAG_UPD);
		CardApplyRecord srcRecord = cardApplyRecordService.getById(cardApplyRecord.getId());
		if(!Constants.CARD_APPLY_STATUS_UNAPPROVED.equals(srcRecord.getStatus())) {
			return msg(model, "只有未审批过的单子才能执行修改操作！", 0, "ewashing/memberCardApply/memberCardApply");
		}
		if(StringUtils.isBlank(cardApplyRecord.getNumberBegin()) || StringUtils.isBlank(cardApplyRecord.getNumberEnd())) return msg(model, "开始编号和结束编号不能为空！", 0, "ewashing/memberCardApply/memberCardApply");
		if(!cardApplyRecord.getNumberBegin().matches(number_regx) || !cardApplyRecord.getNumberEnd().matches(number_regx)) return msg(model, "开始编号和结束编号必须为非0开头的数字，且最大不能超过15位！", 0, "ewashing/memberCardApply/memberCardApply");
		Long begin = Long.parseLong(cardApplyRecord.getNumberBegin());
		Long end = Long.parseLong(cardApplyRecord.getNumberEnd());
		if(begin>end) return msg(model, "开始编号必须大于或等于结束编号！", 0, "ewashing/memberCardApply/memberCardApply");
		if(!cardApplyRecordService.validateCardNumber(cardApplyRecord.getNumberBegin(), cardApplyRecord.getNumberEnd())) return msg(model, "该卡区间已存在相同卡号，操作失败！", 0, "ewashing/memberCardApply/memberCardApply");
		SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
		cardApplyRecord.setUpdateTime(new Date());
		cardApplyRecord.setUpdateUser(loginUser.getUsername());
		Long count = 1+end-begin;
		cardApplyRecord.setApplyNumber(count.intValue());
		try {
			cardApplyRecordService.update(cardApplyRecord);
			return msg(model, "操作成功！", 1, "ewashing/memberCardApply/memberCardApply");
		}
		catch(Exception e){
			return msg(model, "操作失败！", 0, "ewashing/memberCardApply/memberCardApply");
		}
	}
	@RequestMapping(value = "/delMemberCardApply",method = RequestMethod.POST)
	@ResponseBody
	public String delMemberCardApply(HttpServletRequest req, CardApplyRecord cardApplyRecord,HttpSession session,
			Model model) throws Exception {
		model.asMap().clear();
		CardApplyRecord srcRecord = cardApplyRecordService.getById(cardApplyRecord.getId());
		try {
			if(!Constants.CARD_APPLY_STATUS_UNAPPROVED.equals(srcRecord.getStatus())) {
				model.addAttribute("resultMsg", "只有未审批过的单子才能执行删除操作！");
				model.addAttribute("resultCode", 1);
				return new ObjectMapper().writeValueAsString(model);
			}
			cardApplyRecordService.deleteById(cardApplyRecord.getId());
			model.addAttribute("resultMsg", "删除成功！");
			model.addAttribute("resultCode", 1);
			return new ObjectMapper().writeValueAsString(model);
		}
		catch(Exception e){
			model.addAttribute("resultMsg", "删除失败！");
			model.addAttribute("resultCode", 0);
		}
		return new ObjectMapper().writeValueAsString(model);
	}
	
	
	
	@RequestMapping(value = "/approvedList", method = RequestMethod.GET)
	public String approvedList(HttpServletRequest req, CardApplyRecord cardApplyRecord, Model model){
		try {			
			String pageNum = req.getParameter("pageNum");
			String pageSize = req.getParameter("pageSize");
			cardApplyRecord.setCreateTimeBegin(DateUtil.parseData(req.getParameter("createTimeBeginStr")));
			cardApplyRecord.setCreateTimeEnd(DateUtil.parseData(req.getParameter("createTimeEndStr")));
			if (StringUtils.isBlank(pageNum)) {
				pageNum = "1";
			}
			if (StringUtils.isBlank(pageSize)) {
				pageSize = "10";
			}
			PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize));
			List<CardApplyRecord> list = cardApplyRecordService.findList(cardApplyRecord);
			PageInfo<CardApplyRecord> page = new PageInfo<CardApplyRecord>(list);
			model.addAttribute("page", page);
			model.addAttribute("createTimeBeginStr",req.getParameter("createTimeBeginStr"));
			model.addAttribute("createTimeEndStr",req.getParameter("createTimeEndStr"));
			return "ewashing/memberCardApply/approvedList";
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}		
		return "ewashing/memberCardApply/approvedList";
	}
	
	
	@RequestMapping(value = "/beforeApproved",method = {RequestMethod.POST, RequestMethod.GET})
	public String beforeApproved(HttpServletRequest req, CardApplyRecord cardApplyRecord,HttpSession session,
			Model model) {
		cardApplyRecord = cardApplyRecordService.getById(cardApplyRecord.getId());
		if(!Constants.CARD_APPLY_STATUS_UNAPPROVED.equals(cardApplyRecord.getStatus())) {
			return msg(model, "只有未审批过的单子才能执行审批操作！", 0, "ewashing/memberCardApply/approvedList");
		}else{
			model.addAttribute("cardApplyRecord", cardApplyRecord);
			return "ewashing/memberCardApply/memberCardApproved";
		}
	}
	
	@RequestMapping(value = "/approvedPass",method = RequestMethod.POST)
	@ResponseBody
	public String approvedPass(HttpServletRequest req, CardApplyRecord cardApplyRecord,HttpSession session,
			Model model) throws Exception{
		try {
			model.asMap().clear();
			String approvedRemark = cardApplyRecord.getApprovedRemark();
			cardApplyRecord = cardApplyRecordService.getById(cardApplyRecord.getId());
			if(!Constants.CARD_APPLY_STATUS_UNAPPROVED.equals(cardApplyRecord.getStatus())) {
				model.addAttribute("resultMsg", "只有未审批过的单子才能执行审批操作！");
				model.addAttribute("resultCode", 0);
				return new ObjectMapper().writeValueAsString(model);
			}
			if(StringUtils.isBlank(cardApplyRecord.getNumberBegin()) || StringUtils.isBlank(cardApplyRecord.getNumberEnd())) {
				model.addAttribute("resultMsg", "开始编号和结束编号不能为空！");
				model.addAttribute("resultCode", 0);
				return new ObjectMapper().writeValueAsString(model);
			}
			if(!cardApplyRecord.getNumberBegin().matches(number_regx) || !cardApplyRecord.getNumberEnd().matches(number_regx)){
				model.addAttribute("resultMsg", "开始编号和结束编号必须为非0开头的数字，且最大不能超过15位！");
				model.addAttribute("resultCode", 0);
				return new ObjectMapper().writeValueAsString(model);
			}
			Long begin = Long.parseLong(cardApplyRecord.getNumberBegin());
			Long end = Long.parseLong(cardApplyRecord.getNumberEnd());
			if(begin>end) {
				model.addAttribute("resultMsg", "开始编号必须大于或等于结束编号！");
				model.addAttribute("resultCode", 0);
				return new ObjectMapper().writeValueAsString(model);
			}
			if(!cardApplyRecordService.validateCardNumber(cardApplyRecord.getNumberBegin(), cardApplyRecord.getNumberEnd())){
				model.addAttribute("resultMsg", "该卡区间已存在相同卡号，操作失败！");
				model.addAttribute("resultCode", 0);
				return new ObjectMapper().writeValueAsString(model);
			}
			SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
			cardApplyRecord.setApprover(loginUser.getUsername());
			cardApplyRecord.setApprovedTime(new Date());
			cardApplyRecord.setStatus(Constants.CARD_APPLY_STATUS_APPROVED);
			cardApplyRecord.setApprovedRemark(approvedRemark);
		
			cardApplyRecordService.doApproved(cardApplyRecord);
			model.addAttribute("resultMsg", "操作成功！");
			model.addAttribute("resultCode", 1);
			return new ObjectMapper().writeValueAsString(model);
		}
		catch(Exception e){
			model.addAttribute("resultMsg", "操作失败！");
			model.addAttribute("resultCode", 0);
			
		}
		return new ObjectMapper().writeValueAsString(model);
	}
	@RequestMapping(value = "/approvedUnpass",method = RequestMethod.POST)
	@ResponseBody
	public String approvedUnpass(HttpServletRequest req, CardApplyRecord cardApplyRecord,HttpSession session,
			Model model) throws Exception{
		try {
			model.asMap().clear();
			String approvedRemark = cardApplyRecord.getApprovedRemark();
			CardApplyRecord srcRecord = cardApplyRecordService.getById(cardApplyRecord.getId());
			if(!Constants.CARD_APPLY_STATUS_UNAPPROVED.equals(srcRecord.getStatus())) {
				model.addAttribute("resultMsg", "只有未审批过的单子才能执行审批操作！");
				model.addAttribute("resultCode", 0);
				return new ObjectMapper().writeValueAsString(model);
			}
			SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
			cardApplyRecord.setApprover(loginUser.getUsername());
			cardApplyRecord.setApprovedTime(new Date());
			cardApplyRecord.setStatus(Constants.CARD_APPLY_STATUS_UNPASS);
			cardApplyRecord.setApprovedRemark(approvedRemark);
			cardApplyRecordService.doApproved(cardApplyRecord);
			model.addAttribute("resultMsg", "操作成功！");
			model.addAttribute("resultCode", 1);
			return new ObjectMapper().writeValueAsString(model);
		}
		catch(Exception e){
			model.addAttribute("resultMsg", "操作失败！");
			model.addAttribute("resultCode", 0);
		}
		return new ObjectMapper().writeValueAsString(model);
	}
	
	
	private String msg(Model model,String mes,int code,String redirect) {
		model.addAttribute("resultMsg", mes);
		model.addAttribute("resultCode", code);
		return redirect;
	}
	private static final String number_regx = "(?!^0)\\d{1,15}";
	
	public static void main(String args[]){
		System.out.println(UUID.getUUID32());
	}
}

package com.ehyf.ewashing.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ehyf.ewashing.dao.WechatTempleteDao;
import com.ehyf.ewashing.emun.OrderStatus;
import com.ehyf.ewashing.entity.StoreOrder;
import com.ehyf.ewashing.entity.WechatConfig;
import com.ehyf.ewashing.util.DateUtil;
import com.ehyf.ewashing.util.LocalThreadUtils;
import com.ehyf.ewashing.util.PropertiesUtil;
import com.ehyf.ewashing.util.StringUtils;
import com.ehyf.ewashing.wechat.model.AccessToken;
import com.ehyf.ewashing.wechat.model.TemplateMsg;
import com.ehyf.ewashing.wechat.model.TemplateMsgData;
import com.ehyf.ewashing.wechat.model.TemplateMsgResult;
import com.ehyf.ewashing.wechat.proxy.WeChatProxy;

@Service
public class WechatTempleteService {

	@Autowired
	private WechatTempleteDao wechatDao;
	
    private static PropertiesUtil propertiesUtil= new PropertiesUtil("config.properties");

	
	public boolean sendMessage(String templateId,TemplateMsg templateMsg){
		
		String appId =propertiesUtil.getProperty("AppID");
		String accessToken =getAccessToken(appId);
		TemplateMsgResult result =WeChatProxy.sendTemplateMsg(accessToken, templateMsg);
		String code =result.getErrcode();
		if("0".equals(code)){
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * 获取token
	 * 
	 * @param appId
	 *            微信应用ID
	 * @return
	 */
	private String getAccessToken(String appId) {

		String accessToken = "";
		WechatConfig config = wechatDao.getWechatConfigByAppId(appId);
		if (StringUtils.isEmptyString(config.getToken())) {
			// 获取token、设置时间
			AccessToken token = WeChatProxy.getAccessToken("client_credential", config.getAppId(),
					config.getAppSecret());
			accessToken = token.getAccess_token();
			WechatConfig wb = new WechatConfig();
			wb.setToken(accessToken);
			wb.setCreateTime(new Date());
			wechatDao.update(wb);
		}
		// 验证token 是否过期
		else {
			accessToken = config.getToken();
			Date tokenTime = config.getCreateTime();
			// 当前时间小于等于 token 获取时间+过期时间
			if (tokenTime.getTime() / 1000 + config.getExpires_in() >= new Date().getTime() / 1000) {
				AccessToken token = WeChatProxy.getAccessToken("client_credential", config.getAppId(),
						config.getAppSecret());
				WechatConfig wb = new WechatConfig();
				accessToken = token.getAccess_token();
				wb.setToken(token.getAccess_token());
				wb.setCreateTime(new Date());
				wechatDao.update(wb);
			}
		}
		return accessToken;
	}

	/**
	 * 订单状态更新提醒
	 * @param order
	 * @return
	 */
	public boolean sendOrderStatusNotice(StoreOrder order,String message){
		
		
		String currentAppId =order.getAppId();
		if(StringUtils.isEmptyString(currentAppId)){
			currentAppId =order.getAppId();
		}
		WechatConfig config =wechatDao.getWechatConfigByAppId(currentAppId);
		if(config ==null){
			return false;
		}
		String templateId="";
		String appId =config.getAppId();
		if(WeChatProxy.judgeAppIdType(appId)==1){
			templateId =propertiesUtil.getProperty("templete2");
		}
		if(WeChatProxy.judgeAppIdType(appId)==2){
			templateId =propertiesUtil.getProperty("templete4");
		}
		String accessToken =getAccessToken(appId);
		TemplateMsg msg =new TemplateMsg();
		// 模板基本信息
		msg.setTemplate_id(templateId);
		msg.setTouser(order.getOpenId());
		msg.setUrl(orderDetailUrl());
		// 模板内容
		Map<String, TemplateMsgData> data =new HashMap<String, TemplateMsgData>();
		
		// first
		TemplateMsgData first =new TemplateMsgData();
		first.setValue("订单状态更新");
		data.put("first", first);
		
		// 订单编号
		TemplateMsgData orderCode =new TemplateMsgData();
		orderCode.setValue(order.getOrderCode());
		data.put("keyword1", orderCode);
		
		//订单状态
		TemplateMsgData orderStatus =new TemplateMsgData();
		orderStatus.setValue(order.getOrderStatus());
		data.put("keyword2", orderStatus);
		
		//订单时间
		TemplateMsgData currentDate =new TemplateMsgData();
		currentDate.setValue(DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
		data.put("keyword3", currentDate);
		
		// remark
		TemplateMsgData remark =new TemplateMsgData();
		remark.setValue(message);
		data.put("remark", remark);
		msg.setData(data);
		TemplateMsgResult result =WeChatProxy.sendTemplateMsg(accessToken, msg);
		String code =result.getErrcode();
		if ("0".equals(code)) {
			return true;
		} else {
			return false;
		}
		
	}
	
	/**
	 *  下单通知提醒
	 * @param order
	 * @return
	 */
	public boolean sendOrderMesasage(StoreOrder order) {
		
		
		String currentAppId =String.valueOf(LocalThreadUtils.get(LocalThreadUtils.CONSTRANT_APP_ID));
		WechatConfig config =wechatDao.getWechatConfigByAppId(currentAppId);
		if(config ==null){
			return false;
		}
		
		String templateId="";
		String appId =config.getAppId();
		if(WeChatProxy.judgeAppIdType(appId)==1){
			templateId =propertiesUtil.getProperty("templete1");
		}
		if(WeChatProxy.judgeAppIdType(appId)==2){
			templateId =propertiesUtil.getProperty("templete3");
		}
		
		String accessToken =getAccessToken(appId);
		TemplateMsg msg =new TemplateMsg();
		// 模板基本信息
		msg.setTemplate_id(templateId);
		msg.setTouser(order.getOpenId());
		msg.setUrl(orderDetailUrl());
		// 模板内容
		Map<String, TemplateMsgData> data =new HashMap<String, TemplateMsgData>();
		
		// first
		TemplateMsgData first =new TemplateMsgData();
		first.setValue("订单处理提醒");
		data.put("first", first);
		
		// 姓名
		TemplateMsgData memberName =new TemplateMsgData();
		memberName.setValue(order.getMemberName());
		data.put("keyword1", memberName);
		
		//电话
		TemplateMsgData mobilePhone =new TemplateMsgData();
		mobilePhone.setValue(order.getMobilePhone());
		data.put("keyword2", mobilePhone);
		//地址
		TemplateMsgData address =new TemplateMsgData();
		address.setValue(order.getOrderAddress());
		data.put("keyword3", address);
		
		//下单时间
		TemplateMsgData orderDate =new TemplateMsgData();
		orderDate.setValue(DateUtil.formatDate(order.getCreateDate(), "yyyy-MM-dd HH:mm:ss"));
		data.put("keyword4", orderDate);
		
		// 上门时间
		TemplateMsgData visterDate =new TemplateMsgData();
		if(order.getPlaceOrderDate()!=null){
			visterDate.setValue(DateUtil.formatDate(order.getPlaceOrderDate(), "yyyy-MM-dd")+"   "+order.getPlaceOrderDateArea());
		}
		data.put("keyword5", visterDate);
		
		// remark
		TemplateMsgData remark =new TemplateMsgData();
		remark.setValue("感谢您的使用！");
		data.put("remark", remark);
		msg.setData(data);
		TemplateMsgResult result =WeChatProxy.sendTemplateMsg(accessToken, msg);
		String code =result.getErrcode();
		if ("0".equals(code)) {
			return true;
		} else {
			return false;
		}
	}
	
	private String orderDetailUrl(){
		// 设置模板详情URL
		if(WeChatProxy.judgeAppIdType(String.valueOf(LocalThreadUtils.get(LocalThreadUtils.CONSTRANT_APP_ID)))==1){
			return "http://www.sawyxd.com/xiyinode/wx/wx_login";
		}
		if(WeChatProxy.judgeAppIdType(String.valueOf(LocalThreadUtils.get(LocalThreadUtils.CONSTRANT_APP_ID)))==2){
			return "http://www.sawyxd.com/hyfnode/wx/wx_login";
		}
		return "";
	}

	public boolean sendOrderStatusNoticeForLocal(StoreOrder order, String messsage) {
		String currentAppId =order.getAppId();
		if(StringUtils.isEmptyString(currentAppId)){
			currentAppId =order.getAppId();
		}
		WechatConfig config =wechatDao.getWechatConfigByAppId(currentAppId);
		if(config ==null){
			return false;
		}
		String templateId="";
		String appId =config.getAppId();
		if(WeChatProxy.judgeAppIdType(appId)==1){
			templateId =propertiesUtil.getProperty("templete2");
		}
		if(WeChatProxy.judgeAppIdType(appId)==2){
			templateId =propertiesUtil.getProperty("templete4");
		}
		String accessToken =getAccessToken(appId);
		TemplateMsg msg =new TemplateMsg();
		// 模板基本信息
		msg.setTemplate_id(templateId);
		msg.setTouser(order.getOpenId());
		msg.setUrl(orderDetailUrlForLocal(currentAppId));
		// 模板内容
		Map<String, TemplateMsgData> data =new HashMap<String, TemplateMsgData>();
		
		// first
		TemplateMsgData first =new TemplateMsgData();
		first.setValue("订单状态更新");
		data.put("first", first);
		
		// 订单编号
		TemplateMsgData orderCode =new TemplateMsgData();
		orderCode.setValue(order.getOrderCode());
		data.put("keyword1", orderCode);
		
		//订单状态
		TemplateMsgData orderStatus =new TemplateMsgData();
		orderStatus.setValue(order.getOrderStatus());
		data.put("keyword2", orderStatus);
		
		//订单时间
		TemplateMsgData currentDate =new TemplateMsgData();
		currentDate.setValue(DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
		data.put("keyword3", currentDate);
		
		// remark
		TemplateMsgData remark =new TemplateMsgData();
		remark.setValue(messsage);
		data.put("remark", remark);
		msg.setData(data);
		TemplateMsgResult result =WeChatProxy.sendTemplateMsg(accessToken, msg);
		String code =result.getErrcode();
		if ("0".equals(code)) {
			return true;
		} else {
			return false;
		}
		
	}

	private String orderDetailUrlForLocal(String currentAppId) {
		// 设置模板详情URL
		if(WeChatProxy.judgeAppIdType(currentAppId)==1){
			return "http://www.sawyxd.com/xiyinode/wx/wx_login";
		}
		if(WeChatProxy.judgeAppIdType(currentAppId)==2){
			return "http://www.sawyxd.com/hyfnode/wx/wx_login";
		}
		return "";
	}
}

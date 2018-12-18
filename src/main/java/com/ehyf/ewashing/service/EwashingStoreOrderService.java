package com.ehyf.ewashing.service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.ParseException;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.ehyf.ewashing.dao.EwashingStoreOrderDao;
import com.ehyf.ewashing.dao.OrderLogisticsInfoDao;
import com.ehyf.ewashing.dao.PrepayOrderDao;
import com.ehyf.ewashing.dao.SendReceivePersonDao;
import com.ehyf.ewashing.dao.SendReceivePersonOrderRelationDao;
import com.ehyf.ewashing.dao.WechatTempleteDao;
import com.ehyf.ewashing.emun.OrderStatus;
import com.ehyf.ewashing.emun.PayStatus;
import com.ehyf.ewashing.entity.OrderLogisticsInfo;
import com.ehyf.ewashing.entity.PrepayOrder;
import com.ehyf.ewashing.entity.SecurityUser;
import com.ehyf.ewashing.entity.SendReceivePerson;
import com.ehyf.ewashing.entity.SendReceivePersonOrderRelation;
import com.ehyf.ewashing.entity.StoreClothes;
import com.ehyf.ewashing.entity.StoreOrder;
import com.ehyf.ewashing.entity.StoreStatistics;
import com.ehyf.ewashing.entity.WechatConfig;
import com.ehyf.ewashing.exection.AppExection;
import com.ehyf.ewashing.ucpass.UcpassEmsService;
import com.ehyf.ewashing.util.DateUtil;
import com.ehyf.ewashing.util.HttpClientUtil;
import com.ehyf.ewashing.util.LocalThreadUtils;
import com.ehyf.ewashing.util.PropertiesUtil;
import com.ehyf.ewashing.util.RandomUtil;
import com.ehyf.ewashing.util.UUID;
import com.ehyf.ewashing.util.WXPayUtil;

import net.sf.json.JSONObject;

@Service
public class EwashingStoreOrderService extends BaseService<EwashingStoreOrderDao,StoreOrder> {

	private static final PropertiesUtil propUtil = new PropertiesUtil("config.properties");
    private static final Logger logger = Logger.getLogger(EwashingStoreOrderService.class);
    
	@Autowired
	private EwashingStoreOrderDao storeOrderDao;
	
	@Autowired
	private SendReceivePersonOrderRelationDao relation;
	
	@Autowired
	private SendReceivePersonDao sendPerson;
	
	@Autowired
	private OrderLogisticsInfoDao  logisticsInfoDao;
	
	@Autowired
	private OrderLogisticsInfoDao orderLogDao;
	
	@Autowired
	private EwashingStoreBusinessService storeBusiness;
	
	@Autowired
	private PrepayOrderDao prepayOrderDao;
	
	@Autowired
	private WechatTempleteService templeteService;
	
	@Autowired
	private WechatTempleteDao wechatDao;

	@Autowired
	private OrderLogisticsInfoService  logisticsInfoService;
	
	@Autowired
	private UcpassEmsService uspassService;
	/**
	 * 更新订单信息
	 * @param order
	 * @return
	 */
	public int updateStoreOrder(StoreOrder order){
		return storeOrderDao.updateStoreOrder(order);
	}

	/**
	 * 根据订单号查询订单
	 * @param orderCode
	 * @return
	 */
	public StoreOrder getStoreOrderByCode(String orderCode){
		return storeOrderDao.getStoreOrderByCode(orderCode);
	}
	
	/**
	 * 根据手机号获取用户订单
	 * @param mobilePhone
	 * @return
	 */
	public List<StoreOrder> getStoreOrderMobile(String mobilePhone) {
		StoreOrder order = new StoreOrder();
		order.setMobilePhone(mobilePhone);
		return storeOrderDao.findList(order);
	}

	public List<StoreOrder> findFactoryHandOnList(StoreOrder order) {
		return storeOrderDao.findFactoryHandOnList(order);
	}

	/**
	 * 根据条件查询订单
	 * @param queryKey
	 * @param storeId 
	 * @return
	 */
	public List<StoreOrder> getStoreOrderByQueryKey(String queryKey, String storeId) {
		StoreClothes sc = new StoreClothes();
		sc.setQueryKey(queryKey);
		sc.setStoreId(storeId);
		return storeOrderDao.getStoreOrderByQueryKey(sc);
	}

	/**
	 * 门店统计
	 * @param beginDate
	 * @param endDate
	 * @param storeId
	 * @return
	 */
	public List<StoreStatistics> storeStatistics(String beginDate, String endDate, String storeId) {
		
		if(beginDate==null && endDate ==null){
			// 查询当天的
			Calendar c =Calendar.getInstance();
			beginDate =DateUtil.formatDate(c.getTime(), "yyyy-MM-dd");
			endDate =DateUtil.formatDate(c.getTime(), "yyyy-MM-dd");
		}
		
		if(beginDate !=null && endDate !=null){
			
		}
		
		return null;
	}

	@Transactional(readOnly=false)
	public int placeOrder(StoreOrder order) {
		
		int result =storeOrderDao.placeOrder(order);
		
		if(result<=0){
			throw new AppExection("下单失败"); 
		}
		
		StoreOrder storeOrder =getStoreOrderByCode(order.getOrderCode());
		// 记录物流信息
		OrderLogisticsInfo olInfo = new OrderLogisticsInfo();
		olInfo.setId(UUID.getUUID32());
		olInfo.setCreateTime(new Date());
		olInfo.setOrderId(storeOrder.getId());
		olInfo.setOrderStatus(storeOrder.getOrderStatus());
		olInfo.setContent("订单已经预约成功，等待分配小E");
		int count =logisticsInfoDao.insert(olInfo);
		if(count<=0){
			throw new AppExection("记录物流信息失败"); 
		}
		return 1;
	}

	/**
	 * 查询O2O 订单信息
	 * @param order
	 * @return
	 */
	public List<StoreOrder> findO2oOrderList(StoreOrder order) {
		List<StoreOrder> list = storeOrderDao.findO2oOrderList(order);
		if(!CollectionUtils.isEmpty(list)){
			for(StoreOrder orders :list){
				orders.setOrderStatus(OrderStatus.getName(orders.getOrderStatus()));
				orders.setPayStatus(PayStatus.getName(orders.getPayStatus()));
				// 物流信息
				OrderLogisticsInfo olInfo = new OrderLogisticsInfo();
				olInfo.setOrderId(orders.getId());
				List<OrderLogisticsInfo> logInoList =orderLogDao.findList(olInfo);
				orders.setLogistics(logInoList);
				
				StoreClothes clothes =new StoreClothes();
				clothes.setOrderCode(orders.getOrderCode());
				orders.setClothesList(storeBusiness.findClothes(clothes));
				// 判断订单是否已经接受
			}
		}
		return list;
	}

	/**
	 * 给小E分配订单
	 * @param ids
	 * @param loginUser
	 * @param sendUserId 
	 * @param type 
	 * @return
	 */
	@Transactional(readOnly=false)
	public boolean distribute(String ids, SecurityUser loginUser, String sendUserId, String type) {
		
		Date currentDate =new Date();
		String idArrays[] =ids.split(",");
		SendReceivePerson person =sendPerson.getById(sendUserId);
		for(String orderId :idArrays){
			
			StoreOrder orderInfo =storeOrderDao.getById(orderId);
			// 判断订单是否已经分配  type =1：取，type=2：送
			if("1".equals(type)){
				// 待取订单已经分配
				if("2".equals(orderInfo.getDistributeStatus())){
					continue;
				}
			}
			
			if("2".equals(type)){
				// 待送订单已经分配
				if("2".equals(orderInfo.getSendDistributeStatus())){
					continue;
				}
			}
			
			
			SendReceivePersonOrderRelation sr =new SendReceivePersonOrderRelation();
			sr.setOrderId(orderId);
			sr.setId(UUID.getUUID32());
			sr.setDistributeDate(currentDate);
			sr.setDistributeUser(loginUser.getUsername());
			sr.setAcceptStatus("1");
			sr.setDistributeType(type);
			sr.setTaskStatus("1");
			sr.setAppId(orderInfo.getAppId());
			// 分配人
			sr.setDistributeUserId(loginUser.getId());
			// 小E ID
			sr.setSendReceivePersonId(sendUserId);
			int count =relation.insert(sr);
			if(count<=0){
				throw new AppExection("分配订单失败");
			}
			
			// 更新订单为已分配
			StoreOrder order =new StoreOrder();
			order.setId(orderId);
			
			if("1".equals(type)){
				order.setDistributeStatus("2");
			}
			if("2".equals(type)){
				order.setSendDistributeStatus("2");
				// 设置送回中，待送订单，只要分配了，就是送回中
				order.setOrderStatus("23");
			}
			
			int upStatus =storeOrderDao.update(order);
			if(upStatus<=0){
				throw new AppExection("分配失败");
			}
			
			
			String message ="";
			// 1：取，2：送
			if("1".equals(type)){
				message ="订单已经安排小E"+person.getName()+"为您取衣服,联系方式:"+person.getMobile();
			}
			
			if("2".equals(type)){
				message ="订单已经安排小E"+person.getName()+"为您送衣服,联系方式:"+person.getMobile();
			}
			
			int countInSert =logisticsInfoService.insertLogisticsInfo(orderId, message);
			if(countInSert<=0){
				throw new AppExection("记录物流信息失败"); 
			}
			// 发送模板消息
			orderInfo.setOrderStatus(OrderStatus.getName(orderInfo.getOrderStatus()));
			templeteService.sendOrderStatusNoticeForLocal(orderInfo,message);
		}
		uspassService.sendNoticeEms(person.getMobile());
		return true;
	}

	/**
	 * 回写封签号
	 * @param orderId
	 * @param sealNumber
	 * @return
	 */
	@Transactional(readOnly=false)
	public int updateOrderSealNumberById(String orderId, String sealNumber) {
		StoreOrder order =new StoreOrder();
		order.setId(orderId);
		order.setSealNumber(sealNumber);
		
		// 判断封签号是否存在
		StoreOrder s =storeOrderDao.getOrderBySealNumber(sealNumber);
		if(s!=null){
			return 0;
		}
		return storeOrderDao.update(order);
	}

	/**
	 * 获取分配给小E的订单
	 * @param orderType 1：门店订单 2:o2o 订单
	 * @param sendReceiveUserId 小E id
	 * @param distributeType  1：收 2：送
	 * @param taskStatus 
	 * @param appId 
	 * @param limit 
	 * @param start 
	 * @return
	 */
	public List<StoreOrder> sendReceiveOrder(String orderType, String distributeType, String sendReceiveUserId,
			String taskStatus, String appId, int start, int limit) {
		
		Map<String,Object> map =new HashMap<String,Object>();
		map.put("orderType", orderType);
		map.put("sendReceiveUserId", sendReceiveUserId);
		map.put("distributeType", distributeType);
		map.put("taskStatus", taskStatus);
		map.put("appId", appId);
		map.put("start", start);
		map.put("limit", limit);
		return storeOrderDao.querySendReceiveOrder(map);
	}

	@Transactional(readOnly=false)
	public int updateStoreOrderAmount(String orderId, BigDecimal orderAmount, BigDecimal freight) {
		
		StoreOrder order =new StoreOrder();
		order.setId(orderId);
		order.setReceivableAmount(orderAmount.add(freight));
		order.setClothesAmount(orderAmount);
		order.setFreight(freight);
		order.setValuation("2");
		return storeOrderDao.update(order);
	}

	@Transactional(readOnly=false)
	public JSONObject prepayOrder(PrepayOrder order) throws Exception {

		String currentAppId =String.valueOf(LocalThreadUtils.get(LocalThreadUtils.CONSTRANT_APP_ID));
		WechatConfig config =wechatDao.getWechatConfigByAppId(currentAppId);
		if(config ==null){
			logger.info("currentAppId:"+currentAppId);
			return null;
		}
		
		logger.info("会员ID："+order.getMemberId());
		//JSONObject result = createXmlResult(order.getOrderAmount(), order.getIpAddress());
		JSONObject result = generateSignedXml(order.getOrderAmount(), order.getIpAddress(),order.getOpenId(),config);
		if ("SUCCESS".equals(result.getString("return_code"))) {

			String sign = result.getString("sign");
			// 判断是否已经下单
			PrepayOrder o = prepayOrderDao.querOrderBySign(sign);
			if (o != null) {
				return result;
			}

			logger.info("预付订单结果:"+result);
			int time = (int) (System.currentTimeMillis() / 1000);
			// 二次签名参数
			Map<String, String> data =new HashMap<String, String>();
			String noncestr =WXPayUtil.generateNonceStr();
			data.put("appId", config.getAppId());
			data.put("nonceStr", noncestr);
			data.put("package", "prepay_id="+result.getString("prepay_id"));
			data.put("signType", "MD5");
			data.put("timeStamp", String.valueOf(time));
			//String xmlStr =WXPayUtil.generateSignedXml(data, propUtil.getProperty("signkey"));		
			/*String newsign = getSign(result.getString("appid"), result.getString("nonce_str"), "Sign=WXPay",
					result.getString("mch_id"), String.valueOf(time), result.getString("prepay_id"));*/
			//String newsign =WXPayUtil.xmlToMap(xmlStr).get("sign");
			
			
			logger.info("参与二次签名参数："+WXPayUtil.mapToXml(data));
			String newsign =WXPayUtil.generateSignature(data, config.getSignKey());
			data.put("sign", newsign);
			// 验证前面是否正确
			if(!WXPayUtil.isSignatureValid(data,config.getSignKey())){
				logger.info("签名不正确。。。。。。。。。。。。。。。。。。。。。。");
			}
			
			result.put("sign", newsign);
			result.put("noncestr", noncestr);
			result.put("timestamp", time);
			// 保存订单记录
			Date currentDate = new Date();
			// 生成规则
			order.setId(new RandomUtil().getRandomId());
			order.setCreateDate(currentDate);
			order.setTradeType(result.getString("trade_type"));
			order.setMerchantId(result.getString("mch_id"));
			order.setPrepayId(result.getString("prepay_id"));
			order.setSign(sign);
			order.setOutTradeNo(result.getString("out_trade_no"));
			prepayOrderDao.insert(order);
		}
		return result;
	}

	private JSONObject generateSignedXml(BigDecimal orderAmount, String ipAddress,String openId, WechatConfig config) throws Exception {
		
		JSONObject json = new JSONObject();
		// 待签名数据
		Map<String, String> data =new HashMap<String, String>();
		
		logger.info("config:"+config.getAppId());
		data.put("appid", config.getAppId());
		data.put("mch_id", config.getMerchantId());
		data.put("nonce_str", WXPayUtil.generateNonceStr());
		data.put("body", "洗衣订单");
		data.put("notify_url", propUtil.getProperty("callBackUrl"));
		data.put("out_trade_no", new RandomUtil().getRandomId());
		data.put("spbill_create_ip", ipAddress);
		orderAmount=(orderAmount.setScale(2, BigDecimal.ROUND_HALF_UP)).multiply(new BigDecimal(100));
		data.put("total_fee", String.valueOf(orderAmount.intValue()));
		data.put("trade_type", "JSAPI");
		data.put("openid", openId);
		//String sign =WXPayUtil.generateSignature(data, propUtil.getProperty("signkey"));
		String xmlData =WXPayUtil.generateSignedXml(data, config.getSignKey());
		// 验证前面是否正确
		if(!WXPayUtil.isSignatureValid(xmlData,config.getSignKey())){
			return json;
		}
		logger.info("xml输出:" + xmlData);
		try {

			String result = HttpClientUtil.doWeixinPostNew(xmlData, propUtil.getProperty("weixinpay"));
			SAXReader reader = new SAXReader();
			InputStream input = new ByteArrayInputStream(result.getBytes());
			BufferedReader re = new BufferedReader(new InputStreamReader(input, "UTF-8"));
			Document resultDocument = reader.read(re);
			if (resultDocument == null) {
				logger.info("xml 为空");
				return null;
			}
			Element root = resultDocument.getRootElement();
			for (Iterator<?> it = root.elementIterator(); it.hasNext();) {
				Element element = (Element) it.next();
				json.put(element.getName(), element.getText());
			}
			json.put("out_trade_no", data.get("out_trade_no"));
			System.out.println(result);

		} catch (ParseException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return json;
	}

	/*private String getSign(String appid, String noncestr, String packages, String partnerid, String timestamp,
			String prepayid) {

		StringBuffer sb = new StringBuffer();
		sb.append("appid=" + appid);
		sb.append("&noncestr=" + noncestr);
		sb.append("&package=" + packages);
		sb.append("&partnerid=" + partnerid);
		sb.append("&timestamp=" + timestamp);
		sb.append("&prepayid=" + prepayid);
		String sign = getSign(sb.toString(), propUtil.getProperty("signkey"));
		return sign;
	}*/
	/**
	 * 生成响应XML
	 * 
	 * @param response
	 * @return
	 */
	private JSONObject createXmlResult(BigDecimal orderAmount, String ip) {
		
		JSONObject json = new JSONObject();
		Document document = DocumentHelper.createDocument();
		Element catalogElement = document.addElement("xml");
		StringBuffer str = new StringBuffer();

		// appid
		Element appid = catalogElement.addElement("appid");
		appid.addCDATA(propUtil.getProperty("AppID"));
		// appid.setText(propUtil.getProperty("weixinappId"));
		str.append("appid=" + propUtil.getProperty("AppID"));
		// 商户ID
		Element mch_id = catalogElement.addElement("mch_id");
		// mch_id.setText(propUtil.getProperty("merchantId"));
		mch_id.addCDATA(propUtil.getProperty("merchantId"));
		str.append("&mch_id=" + propUtil.getProperty("merchantId"));

		// 支付说明
		Element nonce_str = catalogElement.addElement("nonce_str");
		String noneStr = new RandomUtil().getRandomId();
		// nonce_str.setText(noneStr);
		nonce_str.addCDATA(noneStr);
		str.append("&nonce_str=" + noneStr);

		// 商品或支付单简要描述
		Element reason = catalogElement.addElement("body");
		// reason.setText("YOKI");
		reason.addCDATA("EWASHING");
		str.append("&body=EWASHING");

		// 支付回调URL
		Element notify_url = catalogElement.addElement("notify_url");
		// notify_url.setText(propUtil.getProperty("callBackUrl"));
		notify_url.addCDATA(propUtil.getProperty("callBackUrl"));
		str.append("&notify_url=" + propUtil.getProperty("callBackUrl"));

		// 商户订单号
		Element out_trade_no = catalogElement.addElement("out_trade_no");
		String tradeNo = new RandomUtil().getRandomId();
		// out_trade_no.setText(tradeNo);
		out_trade_no.addCDATA(tradeNo);
		str.append("&out_trade_no=" + tradeNo);

		// 终端IP
		Element spbill_create_ip = catalogElement.addElement("spbill_create_ip");
		spbill_create_ip.addCDATA(ip);
		str.append("&spbill_create_ip=" + ip);

		// 订单金额
		Element total_fee = catalogElement.addElement("total_fee");
		orderAmount=orderAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
		total_fee.addCDATA("total_fee");
		str.append("&total_fee=" + orderAmount);

		// 交易类型
		Element trade_type = catalogElement.addElement("trade_type");
		// trade_type.setText("APP");
		trade_type.addCDATA("JSAPI");
		str.append("&trade_type=JSAPI");
		
		/*// 交易类型
		Element sign_type = catalogElement.addElement("sign_type");
		// trade_type.setText("APP");
		sign_type.addCDATA("HMAC-SHA256");
		str.append("&sign_type=HMAC-SHA256");*/

		// sign
		Element signElement = catalogElement.addElement("sign");
		String sign = getSign(str.toString(), propUtil.getProperty("signkey"));
		// signElement.setText(sign);
		signElement.addCDATA(sign);
		String xml = document.asXML();
		logger.info("xml输出:" + xml);
		try {
			String result = HttpClientUtil.doWeixinPost(xml, propUtil.getProperty("weixinpay"));
			SAXReader reader = new SAXReader();
			try {
				InputStream input = new ByteArrayInputStream(result.getBytes());
				BufferedReader re = new BufferedReader(new InputStreamReader(input, "GBK"));
				Document resultDocument = reader.read(re);
				if (resultDocument == null) {
					logger.info("xml 为空");
					return null;
				}

				Element root = resultDocument.getRootElement();
				for (Iterator<?> it = root.elementIterator(); it.hasNext();) {
					Element element = (Element) it.next();
					json.put(element.getName(), element.getText());
				}

				json.put("out_trade_no", tradeNo);
				logger.info("返回结果：" + json);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} catch (ParseException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return json;

	}
    
	private String getSign(String queryString, String aliyunKey) {
		String sign = "";
		String[] keyValue = queryString.split("&");
		Map<String, String> map = new HashMap<String, String>();
		List<String> list = new ArrayList<String>();
		if (keyValue.length > 0) {
			for (int i = 0; i < keyValue.length; i++) {
				String[] kva = keyValue[i].split("=");
				// 去掉sign
				if (!"sign".equals(kva[0])) {
					if (kva.length == 1) {
						map.put(kva[0], null);
					} else {
						map.put(kva[0], kva[1]);
					}
					list.add(kva[0]);
				}
			}
			// 字典排序
			String[] keys = new String[list.size()];
			String[] key = (String[]) list.toArray(keys);
			System.out.println("排序前:" + printString(key));
			dictionarySorteNew(key);
			System.out.println("排序后:" + printString(key));
			// newQueryString =
			// MD5Util.string2MD5(getNewQuery(key,aliyunKey,map)).toUpperCase();
			String sortKeys = getNewQuery(key, aliyunKey, map);
			if (sortKeys.indexOf("package=Sign") != -1) {
				sortKeys = sortKeys.replace("package=Sign", "package=Sign=WXPay");
			}
			// sign = Md5.stringMD5(sortKeys).toUpperCase();
			// String s
			// =MD5Util.string2MD5("appid=wx4082b136afc8a0c6&body=YOKI语音支付&mch_id=1320775601&nonce_str=201603162014480484f74q79&notify_url=http://120.76.75.155:8080/yokivoice/weixinCallBack/weixin&out_trade_no=201603162014480489024t15&spbill_create_ip=10.106.219.168&total_fee=1&trade_type=APP&key=c95407fd3243c2d9fe6873d11966685e".getBytes("UTF-8"));
			logger.info("sortKeys:" + sortKeys);
			try {
				sign = DigestUtils.md5Hex(sortKeys.getBytes("UTF-8")).toUpperCase();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

		}
		return sign;
	}

	/*private String getSign(Map<String, String> params, String paternerKey) throws UnsupportedEncodingException {
		String string1 = createSign(params, false);
		String stringSignTemp = string1 + "&key=" + paternerKey;
		String signValue = DigestUtils.md5Hex(stringSignTemp).toUpperCase();
		return signValue;
	}*/
	
	public static String createSign(Map<String, String> params, boolean encode) throws UnsupportedEncodingException {
		Set<String> keysSet = params.keySet();
		Object[] keys = keysSet.toArray();
		Arrays.sort(keys);
		StringBuffer temp = new StringBuffer();
		boolean first = true;
		for (Object key : keys) {
			if (first) {
				first = false;
			} else {
				temp.append("&");
			}
			temp.append(key).append("=");
			Object value = params.get(key);
			String valueString = "";
			if (null != value) {
				valueString = value.toString();
			}
			if (encode) {
				temp.append(URLEncoder.encode(valueString, "UTF-8"));
			} else {
				temp.append(valueString);
			}
		}
		return temp.toString();
	}
	
	private String printString(String[] key) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < key.length; i++) {
			if (i > 0) {
				sb.append(",");
				sb.append(key[i]);

			} else {
				sb.append(key[i]);
			}
		}
		return sb.toString();
	}
	
	/**
	 * 
	 * @param key
	 *            排序后的数字
	 * @param aliyunKey
	 * @return
	 */
	private String getNewQuery(String[] key, String aliyunKey, Map<String, String> map) {

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < key.length; i++) {
			if (i > 0) {
				sb.append("&");
				sb.append("" + key[i] + "=" + map.get(key[i]));
			} else {
				sb.append("" + key[i] + "=" + map.get(key[i]));
			}
		}
		sb.append("&key=" + aliyunKey);
		return sb.toString();
	}
	
	/**
	 * 字典排序,区分大小写
	 * 
	 * @param ary
	 */
	private void dictionarySorteNew(String[] ary) {
		Arrays.sort(ary, new Comparator<String>() {
			public int compare(String o1, String o2) {
				String[] temp = { o1, o2 };
				Arrays.sort(temp);
				if (o1.equals(temp[0])) {
					return -1;
				} else if (temp[0].equals(temp[1])) {
					return 0;
				} else {
					return 1;
				}
			}
		});
	}

	public void prepayOrder(BigDecimal orderAmount, String ipAddress) {
		
		JSONObject result = createXmlResult(orderAmount, ipAddress);
		
	}

	/**
	 * 调用微信服务，查询订单支付状态
	 * @param out_trade_no
	 * @param appid 
	 * @return
	 */
	public JSONObject orderQuery(String appid,String out_trade_no) {
		
		WechatConfig config =wechatDao.getWechatConfigByAppId(appid);
		if(config ==null){
			logger.info("currentAppId:"+appid);
			return null;
		}
		
		JSONObject json =new JSONObject();
		// 待签名数据
		Map<String, String> data =new HashMap<String, String>();
		data.put("appid", config.getAppId());
		data.put("mch_id", config.getMerchantId());
		data.put("nonce_str", WXPayUtil.generateNonceStr());
		data.put("out_trade_no", out_trade_no);
		
		
		try {

			String xmlData =WXPayUtil.generateSignedXml(data, config.getSignKey());
			// 验证前面是否正确
			if(!WXPayUtil.isSignatureValid(xmlData,config.getSignKey())){
				return json;
			}
			logger.info("xml输出:" + xmlData);
			
			String result = HttpClientUtil.doWeixinPostNew(xmlData, propUtil.getProperty("orderquery"));
			SAXReader reader = new SAXReader();
			InputStream input = new ByteArrayInputStream(result.getBytes());
			BufferedReader re = new BufferedReader(new InputStreamReader(input, "UTF-8"));
			Document resultDocument = reader.read(re);
			if (resultDocument == null) {
				logger.info("xml 为空");
				return json;
			}
			Element root = resultDocument.getRootElement();
			for (Iterator<?> it = root.elementIterator(); it.hasNext();) {
				Element element = (Element) it.next();
				json.put(element.getName(), element.getText());
			}
			logger.info("json:" + result);
		} catch (ParseException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	public StoreOrder queryOrderByTradeNo(String string) {
		
		return null;
	}

	@Transactional(readOnly=false)
	public int updateOrderAndSendUserStatus(String orderId) {
		
		
		// 判断是否签收
		StoreOrder order =storeOrderDao.getById(orderId);
		if("24".equals(order.getOrderStatus())){
			return 2; // 已经签收
		}
		StoreOrder o =new StoreOrder();
		o.setId(orderId);
		// 已签收
		o.setOrderStatus("24");
		int count =update(o);
		if(count<=0){
			return 0;
		}
		// 更新小E任务状态
		SendReceivePersonOrderRelation r =new SendReceivePersonOrderRelation();
		r.setOrderId(orderId);
		r.setDistributeType("2");
		r.setTaskStatus("3");
		int uCount =relation.updatSendReceivePersonOrder(r);
		if(uCount<=0){
			throw new AppExection("更新信息失败");
		}
		return 1;
	}

}

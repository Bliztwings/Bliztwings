package com.ehyf.ewashing.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ehyf.ewashing.dao.ClothesAttachDao;
import com.ehyf.ewashing.dao.ClothesFlowDao;
import com.ehyf.ewashing.dao.ClothesPhotoDao;
import com.ehyf.ewashing.dao.EwashingHandonAreaDao;
import com.ehyf.ewashing.dao.EwashingHandonNoDao;
import com.ehyf.ewashing.dao.EwashingStoreClothesDao;
import com.ehyf.ewashing.dao.EwashingStoreOrderDao;
import com.ehyf.ewashing.dao.OrderPayRecordDao;
import com.ehyf.ewashing.dao.SendReceivePersonOrderRelationDao;
import com.ehyf.ewashing.dao.SendWashingDao;
import com.ehyf.ewashing.dao.SendWashingDetailDao;
import com.ehyf.ewashing.emun.ClothesStatus;
import com.ehyf.ewashing.emun.PayStatus;
import com.ehyf.ewashing.emun.ServiceType;
import com.ehyf.ewashing.emun.Urgency;
import com.ehyf.ewashing.entity.ClothesAttach;
import com.ehyf.ewashing.entity.ClothesFlow;
import com.ehyf.ewashing.entity.ClothesPhoto;
import com.ehyf.ewashing.entity.EwashingDataDictionary;
import com.ehyf.ewashing.entity.EwashingStore;
import com.ehyf.ewashing.entity.HandOnArea;
import com.ehyf.ewashing.entity.HandOnNo;
import com.ehyf.ewashing.entity.MemberCard;
import com.ehyf.ewashing.entity.OrderPayRecord;
import com.ehyf.ewashing.entity.SecurityUser;
import com.ehyf.ewashing.entity.SendReceivePersonOrderRelation;
import com.ehyf.ewashing.entity.SendWashing;
import com.ehyf.ewashing.entity.SendWashingDetail;
import com.ehyf.ewashing.entity.StoreClothes;
import com.ehyf.ewashing.entity.StoreOrder;
import com.ehyf.ewashing.exection.AppExection;
import com.ehyf.ewashing.util.DateUtil;
import com.ehyf.ewashing.util.UUID;
import com.ehyf.ewashing.vo.ConsumptionVo;
import com.ehyf.ewashing.vo.StoreClothesVo;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * 门店业务服务
 */
@Service
public class EwashingStoreBusinessService{

	@Autowired
	private EwashingStoreClothesDao clothesDao;
	@Autowired
	private EwashingStoreOrderDao orderDao;
	@Autowired
	private MemberCardService cardService;
	@Autowired
	private EwashingDataDictionaryService dicService;
	@Autowired
	private MemberCardService memberCarService;
	@Autowired
	private SendWashingDao washingDao;
	@Autowired
	private EwashingStoreClothesDao storeClothesDao;
	@Autowired
	private SendWashingDetailDao washingDetailDao;
	@Autowired
	private ClothesFlowService clothesFlowService;
	@Autowired
	private EwashingHandonNoDao handonNoDao;
	@Autowired
	private SendReceivePersonOrderRelationDao relation;
	@Autowired
	private EwashingHandonAreaDao handonAreaDao;
	
	
	@Autowired
	private ClothesAttachDao clothesAttachDao;
	@Autowired
	private OrderPayRecordDao payRecordDao;
	
	@Autowired
	private FileUploadService uploadService;
	
	@Autowired
	private ClothesPhotoDao mClothesPhotoDao;
	
	
	@Autowired
	private ClothesFlowDao flowDao;
	
	@Autowired
	private OrderLogisticsInfoService  logisticsInfoService;
	
	Logger logger =Logger.getLogger(EwashingStoreBusinessService.class);

	
	
	@Transactional(readOnly=false)
	public boolean saveSoringClothes(SecurityUser loginUser, StoreClothesVo storeClothesVo) {
		
		Date currentDate = new Date();
		EwashingStore store = loginUser.getEwashingStore();
		StoreOrder order =orderDao.getStoreOrderByCode(storeClothesVo.getOrderCode());
		// 获取会员，会员卡信息
		MemberCard cardInfo = cardService.queryMemberByCardOrMobileForBacken(order.getAppId(), order.getMobilePhone());
		// 保存衣服信息
		StoreClothes clothes = new StoreClothes();
		clothes.setId(UUID.getUUID32());
		if(store!=null){
			clothes.setStoreId(store.getId());
			clothes.setStoreName(store.getStoreName());
		}
		
		clothes.setMemberId(cardInfo.getMemberId());
		
		clothes.setUrgency(storeClothesVo.getUrgency());
		clothes.setBarCode(storeClothesVo.getBarCode());
		clothes.setCardNumber(storeClothesVo.getCardNumber());
		clothes.setMemberName(cardInfo.getMemberName());
		clothes.setMobilePhone(cardInfo.getMobilePhone());
		clothes.setCreateDate(currentDate);
		clothes.setCreateUserId(loginUser.getId());
		clothes.setCreateUserName(loginUser.getRealname());
		clothes.setServiceType(storeClothesVo.getServiceType());
		clothes.setClothesName(storeClothesVo.getClothesName());
		clothes.setBrand(storeClothesVo.getBrand());
		clothes.setColor(storeClothesVo.getColor());
		clothes.setFlaw(storeClothesVo.getFlaw());
		clothes.setPrice(storeClothesVo.getSumAmount());
		clothes.setServiceFee(storeClothesVo.getServiceFee());
		clothes.setAttachment(storeClothesVo.getAttachment());
		clothes.setStatus("0");
		clothes.setOrderId(order.getId());
		clothes.setOrderCode(storeClothesVo.getOrderCode());
		int count = clothesDao.insert(clothes);
		if (count <= 0) {
			throw new AppExection("收取衣服失败");
		}
		
		if(!StringUtils.isEmpty(storeClothesVo.getImagePath())){
			// 记录衣服拍照信息
			String fileName =String.valueOf(new Date().getTime());
			
			String qiniupath =uploadService.uploadFile(storeClothesVo.getImagePath(), fileName);
			
			ClothesPhoto potho =new ClothesPhoto();
			potho.setId(UUID.getUUID32());
			potho.setCreateDate(currentDate);
			potho.setCreateUserId(loginUser.getId());
			potho.setCreateUserName(loginUser.getRealname());
			potho.setClothesId(clothes.getId());
			potho.setPhotoPath(qiniupath);
			potho.setPhotoName(fileName);
			potho.setPhotoType("0");
			int pothoCount =mClothesPhotoDao.insert(potho);
			if (pothoCount <= 0) {
				throw new AppExection("记录衣服照片失败");
			}
		}
		
		// 记录衣服附件信息
		String attachs = storeClothesVo.getAttachList();
		JSONArray attachArrays = JSONArray.parseArray(attachs);
		if (!CollectionUtils.isEmpty(attachArrays)) {
			for (int i = 0; i < attachArrays.size(); i++) {

				JSONObject obj = attachArrays.getJSONObject(i);
				String attachId = obj.getString("id");
				String attachBarCode = obj.getString("attachBarCode");
				// 判断附件条码是否存在
				ClothesAttach attachQuery = new ClothesAttach();
				attachQuery.setAttachBarCode(attachBarCode);
				List<ClothesAttach> list = clothesAttachDao.findList(attachQuery);
				if (!CollectionUtils.isEmpty(list)) {
					throw new AppExection("记录重复，添加失败");
				}
				ClothesAttach attach = new ClothesAttach();
				attach.setId(UUID.getUUID32());
				attach.setAttachBarCode(attachBarCode);
				attach.setAttachId(attachId);
				attach.setClothesBarCode(clothes.getBarCode());
				attach.setClothesId(clothes.getId());
				int attachCunt = clothesAttachDao.insert(attach);
				if (attachCunt <= 0) {
					throw new AppExection("记录衣服附件失败");
				}
			}
		}
		
		// 记录衣服流水日志
		int clothesCount =clothesFlowService.insertClothesFlow(clothes.getOrderId(),clothes.getId(),loginUser,"0");
		if(clothesCount<=0){
			throw new AppExection("记录衣服流水日志失败");
		}
		return true;
	}
	
	@Transactional(readOnly=false)
	public boolean saveClothes(SecurityUser loginUser, StoreClothesVo storeClothesVo) {
		
		Date currentDate = new Date();
		EwashingStore store = loginUser.getEwashingStore();
		// 获取会员，会员卡信息
		MemberCard cardInfo = cardService.queryMemberByCardOrMobile(storeClothesVo.getMemberId());
		
		String orderId =storeClothesVo.getOrderId();
		// 判断订单信息是否存在
		StoreOrder storeOrder = orderDao.getStoreOrderByCode(storeClothesVo.getOrderCode());
		if (storeOrder != null) {

			BigDecimal oldAmount = storeOrder.getReceivableAmount() == null ? BigDecimal.valueOf(0)
					: storeOrder.getReceivableAmount();
			BigDecimal paidAmount = storeOrder.getPaidAmount() == null ? BigDecimal.valueOf(0)
					: storeOrder.getPaidAmount();
			
			int oldCount = storeOrder.getClothesCount();
			// 更新衣服数量,订单总价
			StoreOrder o = new StoreOrder();
			o.setOrderCode(storeOrder.getOrderCode());
			o.setPaidAmount(paidAmount);
			o.setReceivableAmount(oldAmount.add(storeClothesVo.getSumAmount()));
			o.setClothesCount(oldCount + 1);
			// 增加金额
			int status = orderDao.updateStoreOrder(o);
			if (status <= 0) {
				throw new AppExection("更新订单信息失败");
			}
		} else {
			
			orderId =UUID.getUUID32();
			// 记录订单信息
			StoreOrder order = new StoreOrder();
			order.setId(orderId);
			order.setOrderCode(storeClothesVo.getOrderCode());
			order.setClothesCount(1);
			order.setCardNumber(cardInfo.getCardNumber());
			order.setUpdateUserId(loginUser.getId());
			order.setCreateDate(new Date());
			order.setCreateUserName(loginUser.getRealname());
			order.setMemberId(cardInfo.getMemberId());
			order.setMemberName(cardInfo.getMemberName());
			order.setMobilePhone(cardInfo.getMobilePhone());
			order.setOrderDate(currentDate);
			order.setOrderStatus("0");
			order.setPayStatus("0");
			order.setOrderType("0");
			order.setReceivableAmount(storeClothesVo.getSumAmount());
			order.setPaidAmount(BigDecimal.ZERO);
			if(store!=null){
				order.setStoreId(store.getId());
				order.setStoreName(store.getStoreName());
			}
			
			// 设置默认坐标
			double longitude =order.getLongitude()==null ? 1 : order.getLongitude();
			double latitude =order.getLatitude()==null ? 2 : order.getLatitude();
			
			order.setLongitude(longitude);
			order.setLatitude(latitude);
			GeometryFactory geometryFactory = new GeometryFactory();
		    com.vividsolutions.jts.geom.Point point = geometryFactory.createPoint(new Coordinate(longitude, latitude));
		    order.setLocationString(point.toText());
			
			int orderCount = orderDao.insert(order);
			if (orderCount <= 0) {
				throw new AppExection("生成订单失败");
			}
		}
		
		// 保存衣服信息
		StoreClothes clothes = new StoreClothes();
		clothes.setId(UUID.getUUID32());
		if(store!=null){
			clothes.setStoreId(store.getId());
			clothes.setStoreName(store.getStoreName());
		}
		
		clothes.setMemberId(cardInfo.getMemberId());
		clothes.setOrderId(orderId);
		clothes.setUrgency(storeClothesVo.getUrgency());
		clothes.setBarCode(storeClothesVo.getBarCode());
		clothes.setCardNumber(storeClothesVo.getCardNumber());
		clothes.setMemberName(cardInfo.getMemberName());
		clothes.setMobilePhone(cardInfo.getMobilePhone());
		clothes.setCreateDate(currentDate);
		clothes.setCreateUserId(loginUser.getId());
		clothes.setCreateUserName(loginUser.getRealname());
		clothes.setServiceType(storeClothesVo.getServiceType());
		clothes.setClothesName(storeClothesVo.getClothesName());
		clothes.setBrand(storeClothesVo.getBrand());
		clothes.setColor(storeClothesVo.getColor());
		clothes.setFlaw(storeClothesVo.getFlaw());
		clothes.setPrice(storeClothesVo.getSumAmount());
		clothes.setServiceFee(storeClothesVo.getServiceFee());
		clothes.setAttachment(storeClothesVo.getAttachment());
		clothes.setStatus("0");
		clothes.setOrderCode(storeClothesVo.getOrderCode());
		int count = clothesDao.insert(clothes);
		if (count <= 0) {
			throw new AppExection("收取衣服失败");
		}
		
		if(!StringUtils.isEmpty(storeClothesVo.getImagePath())){
			// 记录衣服拍照信息
			String fileName =String.valueOf(new Date().getTime());
			
			String qiniupath =uploadService.uploadFile(storeClothesVo.getImagePath(), fileName);
			
			ClothesPhoto potho =new ClothesPhoto();
			potho.setId(UUID.getUUID32());
			potho.setCreateDate(currentDate);
			potho.setCreateUserId(loginUser.getId());
			potho.setCreateUserName(loginUser.getRealname());
			potho.setClothesId(clothes.getId());
			potho.setPhotoPath(qiniupath);
			potho.setPhotoName(fileName);
			potho.setPhotoType("0");
			int pothoCount =mClothesPhotoDao.insert(potho);
			if (pothoCount <= 0) {
				throw new AppExection("记录衣服照片失败");
			}
		}
		
		// 记录衣服附件信息
		String attachs = storeClothesVo.getAttachList();
		JSONArray attachArrays = JSONArray.parseArray(attachs);
		if (!CollectionUtils.isEmpty(attachArrays)) {
			for (int i = 0; i < attachArrays.size(); i++) {

				JSONObject obj = attachArrays.getJSONObject(i);
				String attachId = obj.getString("id");
				String attachBarCode = obj.getString("attachBarCode");
				// 判断附件条码是否存在
				ClothesAttach attachQuery = new ClothesAttach();
				attachQuery.setAttachBarCode(attachBarCode);
				List<ClothesAttach> list = clothesAttachDao.findList(attachQuery);
				if (!CollectionUtils.isEmpty(list)) {
					throw new AppExection("记录重复，添加失败");
				}
				ClothesAttach attach = new ClothesAttach();
				attach.setId(UUID.getUUID32());
				attach.setAttachBarCode(attachBarCode);
				attach.setAttachId(attachId);
				attach.setClothesBarCode(clothes.getBarCode());
				attach.setClothesId(clothes.getId());
				int attachCunt = clothesAttachDao.insert(attach);
				if (attachCunt <= 0) {
					throw new AppExection("记录衣服附件失败");
				}
			}
		}
		// 记录衣服流水日志
		int clothesCount =clothesFlowService.insertClothesFlow(clothes.getOrderId(),clothes.getId(),loginUser,"0");
		if(clothesCount<=0){
			throw new AppExection("记录衣服流水日志失败");
		}
		return true;
	}


	/**
	 * 获取待送洗数据
	 * 
	 * @param storeId
	 * @return
	 */
	public List<StoreClothes> findClothesByMobileOrCardNumber(StoreClothes storeClothes) {

		List<StoreClothes> list = clothesDao.findList(storeClothes);
		convertList(list);
		return list;
	}

	private void convertList(List<StoreClothes> list) {

		if (!CollectionUtils.isEmpty(list)) {

			for (StoreClothes clothes : list) {

				// 获取衣服照片信息
				ClothesPhoto photo =new ClothesPhoto();
				photo.setClothesId(clothes.getId());
				List<ClothesPhoto> photoList =mClothesPhotoDao.findList(photo);
				if(!CollectionUtils.isEmpty(photoList)){
					clothes.setHasPhoto("1");
					clothes.setPhotoList(photoList);
				}
				clothes.setServiceFee(clothes.getServiceFee()== null ? BigDecimal.ZERO :clothes.getServiceFee());
				clothes.setStatus(ClothesStatus.getName(clothes.getStatus()));
				clothes.setServiceType(ServiceType.getName(clothes.getServiceType()));
				clothes.setUrgency(Urgency.getName(clothes.getUrgency()));
				clothes.setPayStatus(PayStatus.getName(clothes.getPayStatus()));
				if (clothes.getId() != null && !"".equals(clothes.getId())) {
					// List<EwashingDataDictionary> dataList =
					// dicService.queryTextNameByIds(clothes.getAttachment().split(","));

					// 根据clothesId 获取对应的附件信息
					ClothesAttach attach = new ClothesAttach();
					attach.setClothesId(clothes.getId());
					List<ClothesAttach> dataList = clothesAttachDao.findList(attach);

					StringBuffer sb = new StringBuffer();
					if (!CollectionUtils.isEmpty(dataList)) {

						for (int i = 0; i < dataList.size(); i++) {
							if (i > 0) {
								sb.append(",");
								sb.append(dataList.get(i).getAttachName());
							} else {
								sb.append(dataList.get(i).getAttachName());
							}
						}
					}
					clothes.setAttachment(sb.toString());
				}

				if (clothes.getFlaw() != null && !"".equals(clothes.getFlaw())) {
					List<EwashingDataDictionary> dataList = dicService.queryTextNameByIds(clothes.getFlaw().split(","));
					StringBuffer sb = new StringBuffer();
					if (!CollectionUtils.isEmpty(dataList)) {

						for (int i = 0; i < dataList.size(); i++) {
							if (i > 0) {
								sb.append(",");
								sb.append(dataList.get(i).getDataName());
							} else {
								sb.append(dataList.get(i).getDataName());
							}
						}
					}
					clothes.setFlaw(sb.toString());
				}
			}

		}

	}

	/**
	 * 删除衣服，扣减订单应收金额
	 * @param id
	 */
	@Transactional(readOnly = false)
	public boolean deleteClothesById(String id) {
		// 获取衣服
		StoreClothes clothes = clothesDao.getById(id);
		// 获取订单
		StoreOrder order = orderDao.getStoreOrderByCode(clothes.getOrderCode());
		StoreOrder o = new StoreOrder();
		o.setOrderCode(order.getOrderCode());
		o.setReceivableAmount(order.getReceivableAmount().subtract((clothes.getPrice())));
		o.setClothesCount(order.getClothesCount() - 1);
		int status = orderDao.updateStoreOrder(o);

		if (status == 1) {
			int count1 = clothesDao.deleteById(id);
			if (count1 == 0) {
				throw new AppExection("删除衣服失败");
			}

			// 删除衣服对应的附件
			clothesAttachDao.deleteAttachByClothesId(id);
			
			// 判断是不是O2O 订单
			if(!"2".equals(order.getOrderType())){
				// 判断订单是否还有衣服
				StoreClothes c = new StoreClothes();
				c.setOrderCode(clothes.getOrderCode());
				List<StoreClothes> oList = clothesDao.findList(c);

				if (CollectionUtils.isEmpty(oList)) {
					int count2 = orderDao.deleteById(order.getId());
					if (count2 == 0) {
						throw new AppExection("删除衣服失败");
					}
				}
			}
			return true;
		}
		return false;
	}

	@Transactional(readOnly=false)
	public boolean doPayment(SecurityUser loginUser, String cardNumber, String memberId, String orderCode, String payType, String arrears) throws Exception {
		
		boolean flag =false;
		StoreOrder order = orderDao.getStoreOrderByCode(orderCode);
		// 会员支付
		if ("2".equals(payType)) {
			ConsumptionVo consVo = new ConsumptionVo();
			consVo.setCardNumber(cardNumber);
			consVo.setOperatorAmount(order.getReceivableAmount().subtract(order.getPaidAmount()));
			consVo.setRemark("消费扣款");
			consVo.setLoginUser(loginUser);
			/** 扣减会员卡余额 **/
			flag = memberCarService.consumption(consVo);
			if (!flag) {
				throw new AppExection("支付失败");
			}
		}
		
		Date payDate = new Date();
		/** 修改付款状态:已付款 、衣服状态：待送洗 **/
		StoreOrder updateOrder = new StoreOrder();
		updateOrder.setId(order.getId());
		updateOrder.setPayStatus("1");
		updateOrder.setPayDate(payDate);
		updateOrder.setPayGateWay(payType);
		updateOrder.setUpdateDate(payDate);
		updateOrder.setUpdateUserId(loginUser.getId());
		updateOrder.setUpdateUserName(loginUser.getRealname());
		updateOrder.setOrderCode(orderCode);
		updateOrder.setPaidAmount(order.getReceivableAmount().add(order.getPaidAmount()));
		int orderStatus = orderDao.update(updateOrder);
		if (orderStatus <= 0) {
			throw new AppExection("更新订单信息失败");
		}
		
		// 欠款洗涤，不需要再更新衣服信息
		if(!com.ehyf.ewashing.util.StringUtils.isEmptyString(arrears) && "0".equals(arrears)){
			/** 根据订单号修改衣服状态 **/
			StoreClothes clothes = new StoreClothes();
			clothes.setStatus("1");
			clothes.setOrderCode(orderCode);
			clothes.setUpdateDate(payDate);
			clothes.setUpdateUserId(loginUser.getId());
			clothes.setUpdateUserName(loginUser.getRealname());
			int clothesStatus = clothesDao.updateStoreClothes(clothes);
			if (clothesStatus <= 0) {
				throw new AppExection("更新衣服信息失败");
			}
		}
		
		
		// 获取更新的衣服,记录衣服流程日志
		StoreClothes sc = new StoreClothes();
		sc.setOrderCode(orderCode);
		List<StoreClothes> list = clothesDao.findList(sc);
		if (!CollectionUtils.isEmpty(list)) {
			for (StoreClothes c : list) {
				
				ClothesFlow flow = new ClothesFlow();
				flow.setClothesId(c.getId());
				flow.setClothesStatus("1");
				flow.setOptDate(new Date());
				flow.setOptUserId(loginUser.getId());
				flow.setOptUserName(loginUser.getRealname());
				flow.setId(UUID.getUUID32());
				flow.setOrderId(order.getId());
				if(!com.ehyf.ewashing.util.StringUtils.isEmptyString(arrears) && "0".equals(arrears)){
					
					// 判断是否已经记录了日志
					ClothesFlow flowQuery =new ClothesFlow();
					flowQuery.setClothesId(c.getId());
					flowQuery.setClothesStatus("1");
					List<ClothesFlow> listQuery =flowDao.findList(flowQuery);
					if(!CollectionUtils.isEmpty(listQuery)){
						throw new AppExection("已经做过对应操作");
					}	
					
					int insertCount =flowDao.insert(flow);
					if(insertCount<=0){
						throw new AppExection("记录操作流水失败");
					}
					
				}
			}
		}
		
		// 记录支付信息
		OrderPayRecord recode = new OrderPayRecord();
		recode.setId(UUID.getUUID32());
		recode.setCardNumber(cardNumber);
		recode.setMemberId(memberId);
		recode.setMemberName(order.getMemberName());
		recode.setStoreId(order.getStoreId());
		recode.setMobilePhone(order.getMobilePhone());
		recode.setOrderAmount(order.getReceivableAmount());
		recode.setOrderCode(orderCode);
		recode.setOrderType(order.getOrderType());
		recode.setPayDate(payDate);
		recode.setPayType(payType);
		recode.setOrderId(order.getId());
		int payRecordCount = payRecordDao.insert(recode);
		if (payRecordCount <= 0) {
			throw new AppExection("记录支付信息失败");
		}
		return true;
	}
	
	@Transactional(readOnly=false)
	public boolean o2oPayment(String orderId, BigDecimal orderAmount,BigDecimal freight, String memberId, String payType, String mobilePhone,SecurityUser loginUser) throws Exception {
		boolean flag =false;
		StoreOrder order = orderDao.getById(orderId);
		// 会员支付
		if ("2".equals(payType)) {
			ConsumptionVo consVo = new ConsumptionVo();
			consVo.setCardNumber(order.getCardNumber());
			consVo.setOperatorAmount(order.getReceivableAmount().subtract(order.getPaidAmount()  == null ? BigDecimal.ZERO :order.getPaidAmount()));
			consVo.setRemark("消费扣款");
			consVo.setLoginUser(loginUser);
			/** 扣减会员卡余额 **/
			flag = memberCarService.consumption(consVo);
			if (!flag) {
				logger.info("扣减会员失败------------------------------------");
				throw new AppExection("支付失败");
			}
		}
		logger.info("扣减会员成功------------------------------------");
		
		Date payDate = new Date();
		/** 修改付款状态:已付款 、衣服状态：待送洗 **/
		StoreOrder updateOrder = new StoreOrder();
		updateOrder.setId(order.getId());
		updateOrder.setPayStatus("1");
		// 更新订单状态，送往工厂
		updateOrder.setOrderStatus("21");
		updateOrder.setPayDate(payDate);
		updateOrder.setPayGateWay(payType);
		updateOrder.setUpdateDate(payDate);
		updateOrder.setOrderCode(order.getOrderCode());
		updateOrder.setPaidAmount(order.getReceivableAmount().add(order.getPaidAmount()));
		int orderStatus = orderDao.update(updateOrder);
		if (orderStatus <= 0) {
			logger.info("更新订单信息失败------------------------------------");
			throw new AppExection("更新订单信息失败");
		}
		logger.info("更新订单成功------------------------------------");
		// 记录物流信息
		logisticsInfoService.insertLogisticsInfo(order.getId(),"订单已经支付，正在送往工厂！");
		
		// 修改小E 分配订单状态
		SendReceivePersonOrderRelation orderRelation =new SendReceivePersonOrderRelation();
		orderRelation.setOrderId(orderId);
		// 更新为未交，已取
		orderRelation.setTaskStatus("2");
		orderRelation.setDistributeType("1");
		int updateResult =relation.updatSendReceivePersonOrderRelation(orderRelation);
		if (updateResult <= 0) {
			logger.info("更新小E订单新失败------------------------------------");

			throw new AppExection("更新小E订单新失败");
		}
		/** 根据订单号修改衣服状态 **/
		StoreClothes clothes = new StoreClothes();
		clothes.setStatus("1");
		clothes.setOrderCode(order.getOrderCode());
		clothes.setUpdateDate(payDate);
		clothesDao.updateStoreClothes(clothes);
		
		// 衣服信息不一定分拣了，不能更新
		/*int clothesStatus = clothesDao.updateStoreClothes(clothes);
		if (clothesStatus <= 0) {
			throw new AppExection("更新衣服信息失败");
		}*/
		
		
		
		// 获取更新的衣服,记录衣服流程日志
		StoreClothes sc = new StoreClothes();
		sc.setOrderCode(order.getOrderCode());
		List<StoreClothes> list = clothesDao.findList(sc);
		if (!CollectionUtils.isEmpty(list)) {
			for (StoreClothes c : list) {
				
				ClothesFlow flow = new ClothesFlow();
				flow.setClothesId(c.getId());
				flow.setClothesStatus("1");
				flow.setOptDate(new Date());
				flow.setId(UUID.getUUID32());
					
				// 判断是否已经记录了日志
				ClothesFlow flowQuery =new ClothesFlow();
				flowQuery.setClothesId(c.getId());
				flowQuery.setClothesStatus("1");
				List<ClothesFlow> listQuery =flowDao.findList(flowQuery);
				if(!CollectionUtils.isEmpty(listQuery)){
					logger.info("已经做过对应操作------------------------------------");

					throw new AppExection("已经做过对应操作");
				}	
				
				int insertCount =flowDao.insert(flow);
				if(insertCount<=0){
					logger.info("记录操作流水失败------------------------------------");

					throw new AppExection("记录操作流水失败");
				}
				
			}
		}
		
		// 记录支付信息
		OrderPayRecord recode = new OrderPayRecord();
		recode.setId(UUID.getUUID32());
		recode.setCardNumber(order.getCardNumber());
		recode.setMemberId(memberId);
		recode.setMemberName(order.getMemberName());
		recode.setStoreId(order.getStoreId());
		recode.setMobilePhone(order.getMobilePhone());
		recode.setOrderAmount(order.getReceivableAmount());
		recode.setOrderCode(order.getOrderCode());
		recode.setOrderType(order.getOrderType());
		recode.setPayDate(payDate);
		recode.setPayType(payType);
		recode.setOrderId(order.getId());
		int payRecordCount = payRecordDao.insert(recode);
		if (payRecordCount <= 0) {
			logger.info("记录支付信息失败------------------------------------");

			throw new AppExection("记录支付信息失败");
		}
		return true;
	}
	
	
	@Transactional(readOnly=false)
	public boolean sendWashing(SecurityUser loginUser, String clothesIds) {
		
		// 送洗单号
		String sendNumber = DateUtil.formatDate(new Date(), "yyyyMMddHHmmss");
		// 记录送洗记录
		SendWashing washing = new SendWashing();
		washing.setId(UUID.getUUID32());
		washing.setClothesCount(clothesIds.split(",").length);
		washing.setSendDate(new Date());
		washing.setSendNumber(sendNumber);
		washing.setSendUserId(loginUser.getId());
		washing.setSendUserName(loginUser.getRealname());
		washing.setStoreId(loginUser.getEwashingStore().getId());
		int count = washingDao.insert(washing);
		if (count <= 0) {
			return false;
		}
		// 记录送洗明细
		for (int i = 0; i < clothesIds.split(",").length; i++) {

			SendWashingDetail detail = new SendWashingDetail();
			detail.setId(UUID.getUUID32());
			detail.setClothesId(clothesIds.split(",")[i]);
			detail.setSendNumber(sendNumber);
			int washingDetail = washingDetailDao.insert(detail);
			if (washingDetail <= 0) {
				throw new AppExection("记录送洗明细失败");
			}
			// 记录衣服流水日志
			int clothesFlow = clothesFlowService.insertClothesFlow(clothesDao.getById(detail.getClothesId()).getOrderId(),detail.getClothesId(), loginUser, "2");
			if (clothesFlow <= 0) {
				throw new AppExection("记录衣服流水失败");
			}
		}
		
		// 更新衣服状态
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("updateUserName", loginUser.getRealname());
		map.put("updateDate", new Date());
		map.put("updateUserId", loginUser.getId());
		map.put("status", "2");
		map.put("ids", Arrays.asList(clothesIds.split(",")));
		int status = clothesDao.updateStoreClothesForBatch(map);
		if (status <= 0) {
			throw new AppExection("更新衣服信息失败");
		}
		// 更新订单状态
		Set<String> clothesIdSet = new HashSet<String>();
		// 更新订单状态,判断衣服是否全部签收
		for (int i = 0; i < clothesIds.split(",").length; i++) {
			String clothesId = clothesIds.split(",")[i];
			clothesIdSet.add(clothesId);
		}

		Iterator<String> it = clothesIdSet.iterator();
		while (it.hasNext()) {
			String clothesId = it.next();
			StoreClothes clothesObj = clothesDao.getById(clothesId);
			// 获取订单对应衣服，状态为待送洗
			StoreClothes query = new StoreClothes();
			query.setOrderCode(clothesObj.getOrderCode());
			query.setStatus("1");
			List<StoreClothes> list = clothesDao.findList(query);

			if (CollectionUtils.isEmpty(list)) {
				// 更新订单状态,待取衣
				StoreOrder order = new StoreOrder();
				order.setOrderCode(clothesObj.getOrderCode());
				order.setOrderStatus("1");
				int countOrder = orderDao.updateStoreOrder(order);
				if (countOrder <= 0) {
					throw new AppExection("更新订单失败");
				}
			}
		}
		
		return true;
	}


	/**
	 * 送洗记录查询
	 * @param loginUser 
	 * @param sendWashing
	 * @return
	 */
	public List<SendWashing> sendQuery(SecurityUser loginUser, SendWashing sendWashing) {
		return washingDao.findList(sendWashing);
	}

	/**
	 * 查询送洗信息
	 * @param id
	 * @return
	 */
	public SendWashing querySendWashingById(String id) {
		return washingDao.getById(id);
	}

	/**
	 * 根据送洗单号，查询送洗衣服明细
	 * @param clothes
	 * @return
	 */
	public List<StoreClothes> findClothesBySendNumber(StoreClothes clothes) {
		List<StoreClothes> list = storeClothesDao.findClothesBySendNumber(clothes);
		convertList(list);
		return list;
	}

	/**
	 * 根据 StoreClothes 对象查询衣服信息
	 * @param c
	 * @return
	 */
	public List<StoreClothes> findClothes(StoreClothes entity) {
		List<StoreClothes> list = storeClothesDao.findList(entity);
		convertList(list);
		return list;
	}


	/**
	 * 取衣服
	 * @param loginUser
	 * @param ids
	 * @param orderCode 
	 */
	@Transactional(readOnly=false)
	public boolean doTakeClothes(SecurityUser loginUser, String ids, String orderCode) {

		// 更新衣服状态
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("updateUserName", loginUser.getRealname());
		map.put("updateUserId", loginUser.getId());
		map.put("updateDate", new Date());
		map.put("status", "16");
		map.put("ids", Arrays.asList(ids.split(",")));
		int status = clothesDao.updateStoreClothesForBatch(map);

		if (status <= 0) {
			return false;
		}

		// 记录衣服流水
		for (int i = 0; i < Arrays.asList(ids.split(",")).size(); i++) {
			String id = Arrays.asList(ids.split(",")).get(i);
			int count =clothesFlowService.insertClothesFlow(clothesDao.getById(id).getOrderId(),id, loginUser, "16");
			if (count <= 0) {
				throw new AppExection("取衣失败");
			}
		}

		// 判断是否还有未取衣服,没有更新订单状态为完成
		StoreClothes c = new StoreClothes();
		c.setStatus("15");
		c.setOrderCode(orderCode);
		List<StoreClothes> list = clothesDao.findList(c);
		if (list == null || list.size() == 0) {
			// 更新订单状态为已完成
			StoreOrder o = new StoreOrder();
			o.setOrderCode(orderCode);
			o.setOrderStatus("8");
			int uOrder = orderDao.updateStoreOrder(o);
			if (uOrder <= 0) {
				throw new AppExection("取衣失败");
			}
			return true;
		}
		return true;
	}

	public List<HandOnNo> queryHandonNoByArea(String areaId,String storeId) {
		HandOnNo no = new HandOnNo();
		no.setHandonArea(areaId);
		no.setStatus("0");
		no.setStoreId(storeId);
		return handonNoDao.findList(no);
	}

	@Transactional(readOnly=false)
	public boolean confirmHandOn(String clothes, SecurityUser loginUser) {

		boolean flag = false;
		Date currentDate = new Date();
		String[] clothe = clothes.split(",");
		if (clothe.length == 0) {
			return false;
		}
		for (int i = 0; i < clothe.length; i++) {
			String[] sc = clothe[i].split("#");
			// 更新衣服信息
			StoreClothes c = new StoreClothes();
			c.setId(sc[0]);
			c.setHandOnArea(sc[6]);
			c.setHandOnNo(sc[7]);
			c.setStatus("15");
			c.setUpdateDate(currentDate);
			c.setUpdateUserName(loginUser.getRealname());
			int count = clothesDao.update(c);
			if (count <= 0) {
				throw new AppExection("上挂失败");
			}
			// 更改挂衣号状态
			HandOnNo on = new HandOnNo();
			on.setId(sc[7]);
			on.setStatus("1");
			on.setUpdateDate(currentDate);
			on.setUpdateUserName(loginUser.getRealname());
			on.setUpdateUserId(loginUser.getId());
			int count1 = handonNoDao.update(on);
			if (count1 <= 0) {
				throw new AppExection("上挂失败");
			}
			// 衣服流水日志
			int countFlow = clothesFlowService.insertClothesFlow(clothesDao.getById(sc[0]).getOrderId(),sc[0], loginUser, "15");
			if (countFlow <= 0) {
				throw new AppExection("上挂失败");
			}
			flag = true;
		}

		Set<String> clothesIdSet = new HashSet<String>();
		// 更新订单状态,判断衣服是否全部签收
		for (int i = 0; i < clothe.length; i++) {
			String[] sc = clothe[i].split("#");
			String clothesId = sc[0];
			clothesIdSet.add(clothesId);
		}

		Iterator<String> it = clothesIdSet.iterator();
		while (it.hasNext()) {
			String clothesId = it.next();
			StoreClothes clothesObj = clothesDao.getById(clothesId);
			// 获取订单对应衣服，状态为已签收，待上挂
			StoreClothes query = new StoreClothes();
			query.setOrderCode(clothesObj.getOrderCode());
			query.setStatus("14");
			List<StoreClothes> list = clothesDao.findList(query);

			if (CollectionUtils.isEmpty(list)) {
				// 更新订单状态,待取衣
				StoreOrder order = new StoreOrder();
				order.setOrderCode(clothesObj.getOrderCode());
				order.setOrderStatus("7");
				int count = orderDao.updateStoreOrder(order);
				if (count <= 0) {
					throw new AppExection("更新订单失败");
				}
			}
		}
		return flag;
	}

	@Transactional(readOnly = false)
	public boolean confirmSign(String clothes, SecurityUser loginUser) {

		boolean flag = false;
		Date currentDate = new Date();
		String[] clothe = clothes.split(",");
		if (clothe.length == 0) {
			return false;
		}
		for (int i = 0; i < clothe.length; i++) {
			String[] sc = clothe[i].split("#");
			// 更新衣服信息
			StoreClothes c = new StoreClothes();
			c.setId(sc[0]);
			c.setHandOnArea(sc[6]);
			c.setHandOnNo(sc[7]);
			c.setStatus("14");
			c.setUpdateDate(currentDate);
			c.setUpdateUserName(loginUser.getRealname());
			c.setUpdateUserId(loginUser.getId());
			int count = clothesDao.update(c);
			if (count <= 0) {
				throw new AppExection("签收失败");
			}
			// 衣服流水日志
			int countFlow = clothesFlowService.insertClothesFlow(clothesDao.getById(sc[0]).getOrderId(),sc[0], loginUser, "14");
			if (countFlow <= 0) {
				throw new AppExection("签收失败");
			}
			flag = true;
		}

		Set<String> clothesIdSet = new HashSet<String>();
		// 更新订单状态,判断衣服是否全部签收
		for (int i = 0; i < clothe.length; i++) {
			String[] sc = clothe[i].split("#");
			String clothesId = sc[0];
			clothesIdSet.add(clothesId);
		}

		Iterator<String> it = clothesIdSet.iterator();
		while (it.hasNext()) {
			String clothesId = it.next();
			StoreClothes clothesObj = clothesDao.getById(clothesId);
			// 获取订单对应衣服，状态为未签收
			StoreClothes query = new StoreClothes();
			query.setOrderCode(clothesObj.getOrderCode());
			query.setStatus("13");
			List<StoreClothes> list = clothesDao.findList(query);

			if (CollectionUtils.isEmpty(list)) {
				// 更新订单状态,以签收，代取衣
				StoreOrder order = new StoreOrder();
				order.setOrderCode(clothesObj.getOrderCode());
				order.setOrderStatus("6");
				int count = orderDao.updateStoreOrder(order);
				if (count <= 0) {
					throw new AppExection("更新订单失败");
				}
			}
		}
		return flag;
	}


	public List<ClothesAttach> showAttach(String clothesId) {
		ClothesAttach attach = new ClothesAttach();
		attach.setClothesId(clothesId);
		return clothesAttachDao.findList(attach);
	}


	/**
	 * 工厂根据订单上挂衣服
	 * @param orders
	 * @param loginUser
	 * @return
	 */
	public boolean confirmFactoryHandOn(String orders, SecurityUser loginUser) {
		Date currentDate = new Date();
		String[] order = orders.split(",");
		if (order.length == 0) {
			return false;
		}
		// 根据订单，更新衣服信息
		for (int i = 0; i < order.length; i++) {
			String[] sc = order[i].split("#");
			// 更新衣服信息
			StoreClothes c = new StoreClothes();
			c.setOrderCode(sc[2]);
			c.setFactoryHandOnArea(sc[4]);
			c.setFactoryHandOnNo(sc[5]);
			c.setStatus("12");
			c.setUpdateDate(currentDate);
			c.setUpdateUserName(loginUser.getRealname());
			c.setUpdateUserId(loginUser.getId());
			int count = clothesDao.updateStoreClothes(c);
			if (count <= 0) {
				throw new AppExection("工厂上挂失败");
			}

			// 更新订单状态为5
			StoreOrder storeOrder = new StoreOrder();
			storeOrder.setOrderCode(sc[2]);
			storeOrder.setOrderStatus("5");
			int orderCount = orderDao.updateStoreOrder(storeOrder);
			if (orderCount <= 0) {
				throw new AppExection("工厂上挂失败");
			}
		}

		// 记录衣服流水日志
		for (int i = 0; i < order.length; i++) {
			String[] sc = order[i].split("#");
			String orderCode = sc[2];
			// 根据订单获取衣服
			StoreClothes clothes = new StoreClothes();
			clothes.setOrderCode(orderCode);
			List<StoreClothes> clothesList = clothesDao.findList(clothes);

			if (!CollectionUtils.isEmpty(clothesList)) {
				for (int j = 0; j < clothesList.size(); j++) {
					int count = clothesFlowService.insertClothesFlow(clothesList.get(j).getOrderId(),clothesList.get(j).getId(), loginUser, "12");
					if (count <= 0) {
						throw new AppExection("工厂上挂失败");
					}
				}
			}
		}
		return true;
	}


	public boolean arrearsWashing(SecurityUser loginUser, String orderCode) {

		Date currentDate = new Date();
		/** 根据订单号修改衣服状态 **/
		StoreClothes clothes = new StoreClothes();
		clothes.setStatus("1");
		clothes.setOrderCode(orderCode);
		clothes.setUpdateDate(currentDate);
		clothes.setUpdateUserId(loginUser.getId());
		clothes.setUpdateUserName(loginUser.getRealname());
		int clothesStatus = clothesDao.updateStoreClothes(clothes);
		if (clothesStatus <= 0) {
			return false;
		}

		// 更新订单为欠款洗涤
		StoreOrder order = new StoreOrder();
		order.setOrderCode(orderCode);
		order.setPayStatus("2");
		order.setUpdateDate(currentDate);
		order.setUpdateUserId(loginUser.getId());
		order.setUpdateUserName(loginUser.getRealname());
		int orderStatus = orderDao.updateStoreOrder(order);
		if (orderStatus <= 0) {
			throw new AppExection("更新订单信息失败");
		}
		// 获取更新的衣服,记录衣服流程日志
		StoreClothes sc = new StoreClothes();
		sc.setOrderCode(orderCode);
		List<StoreClothes> list = clothesDao.findList(sc);

		if (!CollectionUtils.isEmpty(list)) {
			for (StoreClothes c : list) {
				/*int count = clothesFlowService.insertClothesFlow(c.getId(), loginUser, "1");
				if (count <= 0) {
					throw new AppExection("记录衣服流水信息失败");
				}*/
				
				ClothesFlow flow = new ClothesFlow();
				flow.setClothesId(c.getId());
				flow.setClothesStatus("1");
				flow.setOptDate(new Date());
				flow.setOptUserId(loginUser.getId());
				flow.setOptUserName(loginUser.getRealname());
				flow.setId(UUID.getUUID32());
				
				// 判断是否已经记录了日志
				ClothesFlow flowQuery =new ClothesFlow();
				flowQuery.setClothesId(c.getId());
				flowQuery.setClothesStatus("1");
				List<ClothesFlow> listQuery =flowDao.findList(flowQuery);
				if(!CollectionUtils.isEmpty(listQuery)){
					throw new AppExection("已经做过对应操作");
				}			
				int insertCount =flowDao.insert(flow);
				if(insertCount<=0){
					throw new AppExection("记录操作流水失败");
				}
			}
		}
		return true;
	}


	public StoreClothes findClothesById(String clothesId) {
		return clothesDao.findClothesById(clothesId);
	}


	public StoreClothes getId(String clothesId) {
		return clothesDao.getById(clothesId);
	}


	public boolean updateClothes(SecurityUser loginUser, StoreClothesVo storeClothesVo) {
		Date currentDate = new Date();
		
		//获取衣服原价
		StoreClothes sc =clothesDao.getById(storeClothesVo.getId());
		BigDecimal oldClothesAmount =sc.getPrice();
		// 修改衣服信息
		StoreClothes clothes = new StoreClothes();
		clothes.setId(storeClothesVo.getId());
		clothes.setUrgency(storeClothesVo.getUrgency());
		clothes.setUpdateDate(currentDate);
		clothes.setUpdateUserId(loginUser.getId());
		clothes.setUpdateUserName(loginUser.getRealname());
		clothes.setServiceType(storeClothesVo.getServiceType());
		clothes.setClothesName(storeClothesVo.getClothesName());
		clothes.setBrand(storeClothesVo.getBrand());
		clothes.setColor(storeClothesVo.getColor());
		clothes.setFlaw(storeClothesVo.getFlaw());
		clothes.setPrice(storeClothesVo.getSumAmount());
		int count = clothesDao.update(clothes);
		if (count <= 0) {
			throw new AppExection("编辑衣服失败");
		}
		
		if(!StringUtils.isEmpty(storeClothesVo.getImagePath())){
			// 记录衣服拍照信息
			String fileName =String.valueOf(new Date().getTime());
			
			String qiniupath =uploadService.uploadFile(storeClothesVo.getImagePath(), fileName);
			
			ClothesPhoto potho =new ClothesPhoto();
			potho.setId(UUID.getUUID32());
			potho.setCreateDate(currentDate);
			potho.setCreateUserId(loginUser.getId());
			potho.setCreateUserName(loginUser.getRealname());
			potho.setClothesId(clothes.getId());
			potho.setPhotoPath(qiniupath);
			potho.setPhotoName(fileName);
			potho.setPhotoType("0");
			int pothoCount =mClothesPhotoDao.insert(potho);
			if (pothoCount <= 0) {
				throw new AppExection("记录衣服照片失败");
			}
		}
		
		// 更新衣服订单总价
		StoreOrder storeOrder = orderDao.getStoreOrderByCode(storeClothesVo.getOrderCode());

		StoreOrder o = new StoreOrder();
		o.setOrderCode(storeOrder.getOrderCode());
		o.setReceivableAmount(storeOrder.getReceivableAmount().subtract(oldClothesAmount).add(storeClothesVo.getSumAmount()));
		// 增加金额
		int status = orderDao.updateStoreOrder(o);
		if (status <= 0) {
			throw new AppExection("更新订单信息失败");
		}
		
		// 删除之前的附件信息
		String clothesId =storeClothesVo.getId();
		clothesAttachDao.deleteAttachByClothesId(clothesId);
		/*if(deleteCount<=0){
			throw new AppExection("删除附件失败");
		}*/
		String attachs = storeClothesVo.getAttachList();
		JSONArray attachArrays = JSONArray.parseArray(attachs);
		if (!CollectionUtils.isEmpty(attachArrays)) {
			for (int i = 0; i < attachArrays.size(); i++) {

				JSONObject obj = attachArrays.getJSONObject(i);
				String attachId = obj.getString("id");
				String attachBarCode = obj.getString("attachBarCode");
				// 判断附件条码是否存在
				ClothesAttach attachQuery = new ClothesAttach();
				attachQuery.setAttachBarCode(attachBarCode);
				List<ClothesAttach> list = clothesAttachDao.findList(attachQuery);
				if (!CollectionUtils.isEmpty(list)) {
					throw new AppExection("记录重复，添加失败");
				}
				ClothesAttach attach = new ClothesAttach();
				attach.setAttachBarCode(attachBarCode);
				attach.setAttachId(attachId);
				attach.setId(UUID.getUUID32());
				attach.setClothesId(storeClothesVo.getId());
				attach.setClothesBarCode(storeClothesVo.getBarCode());
				
				int attachCunt = clothesAttachDao.insert(attach);
				if (attachCunt <= 0) {
					throw new AppExection("修改衣服附件失败");
				}
			}
		}
		return true;
	}


	public List<ClothesPhoto> queryPhotoList(String clothesId) {
		ClothesPhoto c = new ClothesPhoto();
		c.setClothesId(clothesId);
		return mClothesPhotoDao.findList(c);
	}


	public List<StoreClothes> findClothesCommon(StoreClothes storeClothes) {
		
		List<StoreClothes> list = clothesDao.findClothesCommon(storeClothes);
		return list;
	}

	//查找订单号
	public List<StoreClothes> findOrderCode(StoreClothes storeClothes) {

		List<StoreClothes> list = clothesDao.findOrderCode(storeClothes);
		return list;
	}

	//查找订单信息
	public List<StoreClothes> findOrderInfo(StoreClothes storeClothes) {

		List<StoreClothes> list = clothesDao.findOrderInfo(storeClothes);
		return list;
	}


	public List<StoreClothes> findHandonClothes(StoreClothes clothes) {
		List<StoreClothes> list = clothesDao.findHandonClothes(clothes);
		return list;
	}


	public boolean savePhoto(String imagePath, String clothesId, SecurityUser loginUser) {
		// 记录衣服拍照信息
		String fileName =String.valueOf(new Date().getTime());
		
		String qiniupath =uploadService.uploadFile(imagePath, fileName);
		if(com.ehyf.ewashing.util.StringUtils.isEmptyString(qiniupath)){
			return false;
		}
		
		ClothesPhoto potho =new ClothesPhoto();
		potho.setId(UUID.getUUID32());
		potho.setCreateDate(new Date());
		potho.setCreateUserId(loginUser.getId());
		potho.setCreateUserName(loginUser.getRealname());
		potho.setClothesId(clothesId);
		potho.setPhotoPath(qiniupath);
		potho.setPhotoName(fileName);
		potho.setPhotoType("1");
		int pothoCount =mClothesPhotoDao.insert(potho);
		if (pothoCount <= 0) {
			throw new AppExection("记录衣服照片失败");
		}
		
		return true;
	}


	public List<HandOnArea> queryStoreHandOnArea(String storeId, String handType) {
		HandOnArea no = new HandOnArea();
		no.setStoreId(storeId);
		no.setHandType(handType);
		return handonAreaDao.findList(no);
	}

	/**
	 * 获取指定类型的挂衣区
	 * @param area
	 * @return
	 */
	public List<HandOnArea> queryHandOnAreaByHandType(HandOnArea area) {
		return handonAreaDao.findList(area);
	}


	public StoreOrder getOrderBySealNumber(String queryKey) {
		// TODO Auto-generated method stub
		return orderDao.getOrderBySealNumber(queryKey);
	}

	public List<StoreClothes> queryO2oOutFactoryOrder(String orderType, String clothesStatus) {
		Map<String, String> map =new HashMap<String,String>();
		map.put("orderType", orderType);
		map.put("clothesStatus", clothesStatus);
		List<StoreClothes> list = clothesDao.queryO2oOutFactoryOrder(map);
		return list;
	}

	public List<StoreClothes> findClothesO2o(StoreClothes entity) {
		List<StoreClothes> list = storeClothesDao.findListO2o(entity);
		convertList(list);
		return list;
	}
}

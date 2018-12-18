package com.ehyf.ewashing.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.ehyf.ewashing.dao.ClothesPhotoDao;
import com.ehyf.ewashing.dao.EwashingHandonNoDao;
import com.ehyf.ewashing.dao.EwashingStoreClothesDao;
import com.ehyf.ewashing.dao.EwashingStoreOrderDao;
import com.ehyf.ewashing.dao.LeaveFactoryDao;
import com.ehyf.ewashing.dao.LeaveFactoryDetailDao;
import com.ehyf.ewashing.dao.OrderLogisticsInfoDao;
import com.ehyf.ewashing.emun.OrderStatus;
import com.ehyf.ewashing.entity.ClothesFlow;
import com.ehyf.ewashing.entity.ClothesPhoto;
import com.ehyf.ewashing.entity.HandOnNo;
import com.ehyf.ewashing.entity.LeaveFactory;
import com.ehyf.ewashing.entity.LeaveFactoryDetail;
import com.ehyf.ewashing.entity.OrderLogisticsInfo;
import com.ehyf.ewashing.entity.SecurityUser;
import com.ehyf.ewashing.entity.StoreClothes;
import com.ehyf.ewashing.entity.StoreOrder;
import com.ehyf.ewashing.exection.AppExection;
import com.ehyf.ewashing.util.UUID;

@Service
@Transactional
public class EwashingFactoryBusinessService {

	@Autowired
	private LeaveFactoryDao factoryDao;
	
	@Autowired
	private LeaveFactoryDetailDao factoryDetailDao;

	@Autowired
    private EwashingStoreClothesDao mStoreClothes;
	
	@Autowired
    private EwashingStoreOrderDao storeOrderDao;
    
	
	@Autowired
	private ClothesPhotoDao mClothesPhotoDao;
	
    @Autowired
	private ClothesFlowService clothesFlow;
    
    @Autowired
    private EwashingHandonNoDao handonNoDao;
	
    @Autowired
	private WechatTempleteService templeteService;
    
    @Autowired
	private OrderLogisticsInfoDao  logisticsInfoDao;
    
    @Autowired
	private OrderLogisticsInfoService  logisticsInfoService;
    
	@Transactional(readOnly=false)
	public boolean doOutFactory(SecurityUser loginUser, String ids, String batchNo, String storeId) {
		Date currentDate = new Date();
		// 记录表头
		LeaveFactory factory = new LeaveFactory();
		factory.setId(UUID.getUUID32());
		factory.setCount(ids.split(",").length);
		factory.setLeaveDate(currentDate);
		factory.setOptDate(currentDate);
		factory.setStoreId(storeId);
		factory.setUserId(loginUser.getId());
		factory.setLeaveFactoryNumber(batchNo);
		int count =factoryDao.insert(factory);
		if(count <=0){
			throw new AppExection("出厂失败");
		}
		// 记录明细
		for(int i=0;i<ids.split(",").length;i++){
			LeaveFactoryDetail detail = new LeaveFactoryDetail();
			detail.setId(UUID.getUUID32());
			detail.setClothesId(ids.split(",")[i]);
			detail.setLeaveFactoryNumber(batchNo);
			int count1 =factoryDetailDao.insert(detail);
			if(count1 <=0){
				throw new AppExection("出厂失败");
			}
			// 更改状态
			StoreClothes clothes = new StoreClothes();
			String clothesId =ids.split(",")[i];
			clothes.setId(clothesId);
			clothes.setStatus("13");
			int count2 =mStoreClothes.update(clothes);
			if(count2<=0){
				throw new AppExection("出厂失败");
			}
			// 记录衣服流水日志
			int count3 =clothesFlow.insertClothesFlow(mStoreClothes.getById(clothesId).getOrderId(),clothesId, loginUser, "13");
			if(count3<=0){
				throw new AppExection("出厂失败");
			}
			
			// 判断是否需要更新更新为已出厂
			boolean flag =false;
			StoreClothes sc =mStoreClothes.getById(clothesId);
			if(sc !=null){
				// 获取订单
				StoreOrder order =storeOrderDao.getStoreOrderByCode(sc.getOrderCode());
				StoreClothes c =new StoreClothes();
				c.setOrderCode(order.getOrderCode());
				List<StoreClothes> list =mStoreClothes.findList(c);
				if(!CollectionUtils.isEmpty(list)){
					
					for(StoreClothes s :list){
						// 判断是否有出厂记录
						ClothesFlow cf =new ClothesFlow();
						cf.setClothesId(s.getId());
						cf.setClothesStatus("13");
						List<ClothesFlow>  cfList =clothesFlow.findClothesFlowList(cf);
						if(!CollectionUtils.isEmpty(cfList)){
							flag =true;
						}
					}
				}
				
				if(flag){
					StoreOrder o =new StoreOrder();
					o.setOrderCode(order.getOrderCode());
					o.setOrderStatus("6");
					int o_count =storeOrderDao.updateStoreOrder(o);
					if(o_count<=0){
						throw new AppExection("更新订单状态失败"); 
					}
					if("2".equals(order.getOrderType())){
						// 发送模板消息
						order.setOrderStatus(OrderStatus.getName("6"));
						templeteService.sendOrderStatusNoticeForLocal(order, "您的订单已经出厂,我们尽快为您安排配送人员！");
						
						// 记录物流信息
						OrderLogisticsInfo logisticsInfo = new OrderLogisticsInfo();
						logisticsInfo.setId(UUID.getUUID32());
						logisticsInfo.setCreateTime(new Date());
						logisticsInfo.setOrderId(order.getId());
						logisticsInfo.setOrderStatus(order.getOrderStatus());
						logisticsInfo.setContent("订单已经 出厂,正在为准备配送！");
						
						int countInSert =logisticsInfoService.insertLogisticsInfo(order.getId(), "您的订单已经出厂,我们尽快为您安排配送人员！");
						if(countInSert<=0){
							throw new AppExection("记录物流信息失败"); 
						}
					}
					
					// 释放隔架号
					HandOnNo ho =new HandOnNo();
					ho.setStatus("0");
					ho.setId(sc.getFactoryHandOnNo());
					/*if("0".equals(order.getOrderType())){
						ho.setId(sc.getHandOnNo());
					}
					if("2".equals(order.getOrderType())){
						ho.setId(sc.getFactoryHandOnNo());
					}*/
					int upCount =handonNoDao.update(ho);
					if(upCount<=0){
						throw new AppExection("释放隔架号失败"); 
					}
					/*int olInfo =logisticsInfoDao.insert(logisticsInfo);
					if(olInfo<=0){
						throw new AppExection("记录物流信息失败"); 
					}*/
				}
			}
		}
		return true;
		
	}
	
	@Transactional(readOnly=false)
    public void insertClothesPhoto(ClothesPhoto clothesPhoto) {
	    mClothesPhotoDao.insert(clothesPhoto);
	}

	@Transactional(readOnly=false)
	public boolean doHandOn(String handOnArea, String handOnNo, String clothesId, SecurityUser loginUser, int updateFlag, StoreOrder order,String handType) {
		
		// 更新衣服信息
		StoreClothes c = new StoreClothes();
		c.setId(clothesId);
		c.setFactoryHandOnArea(handOnArea);
		c.setFactoryHandOnNo(handOnNo);
		c.setStatus("12");// 工厂已上挂
		c.setUpdateDate(new Date());
		c.setUpdateUserName(loginUser.getRealname());
		int count = mStoreClothes.update(c);
		if (count <= 0) {
			throw new AppExection("上挂失败");
		}
		
		// 记录衣服流水信息
		int count1 =clothesFlow.insertClothesFlow(mStoreClothes.getById(clothesId).getOrderId(),clothesId, loginUser, "12");
		if (count1 <= 0) {
			throw new AppExection("记录流水错误，上挂失败");
		}
		
		// 判断是否更新隔架号 updateFlag =1 新分配的隔架号需要更新为1 表示已占用
		if(updateFlag==1){
			HandOnNo no = new HandOnNo();
			no.setHandType(handType);
			no.setHandonArea(handOnArea);
			no.setHandOnNo(handOnNo);
			no.setStatus("1");
			int updateCount =handonNoDao.updateHandNo(no);
			if(updateCount<=0){
				throw new AppExection("更新隔架号失败");
			}
		}
		
		// 获取所有已上挂衣服，如果衣服都上挂完，需要更新订单状态为 5 =工厂已上挂(待出厂)
		StoreClothes handOnQueryFinal = new StoreClothes();
		handOnQueryFinal.setStatus("12");
		handOnQueryFinal.setOrderCode(order.getOrderCode());
		List<StoreClothes> list = mStoreClothes.findList(handOnQueryFinal);
		if(!CollectionUtils.isEmpty(list) && (list.size()==order.getClothesCount())){
			// 更新订单状态
			StoreOrder orderUpdate =new StoreOrder();
			orderUpdate.setOrderStatus("5");
			orderUpdate.setOrderCode(order.getOrderCode());
			int orderUpCount =storeOrderDao.updateStoreOrder(orderUpdate);
			if(orderUpCount<=0){
				throw new AppExection("更新订单失败");
			}
			
			// 记录物流信息
			OrderLogisticsInfo logisticsInfo = new OrderLogisticsInfo();
			logisticsInfo.setId(UUID.getUUID32());
			logisticsInfo.setCreateTime(new Date());
			logisticsInfo.setOrderId(order.getId());
			logisticsInfo.setOrderStatus(order.getOrderStatus());
			logisticsInfo.setContent("订单已经洗涤完成，等待出厂 ！");
			int olInfo =logisticsInfoDao.insert(logisticsInfo);
			if(olInfo<=0){
				throw new AppExection("记录物流信息失败"); 
			}
		}
		return true;
	}
}

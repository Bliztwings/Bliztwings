package com.ehyf.ewashing.task;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.ehyf.ewashing.entity.SecurityUser;
import com.ehyf.ewashing.entity.SendReceivePerson;
import com.ehyf.ewashing.entity.StoreOrder;
import com.ehyf.ewashing.service.EwashingStoreOrderService;
import com.ehyf.ewashing.service.SendReceivePersonService;
import com.ehyf.ewashing.util.PropertiesUtil;

/**
 * 自动分配订单
 * 
 * @author shenxiaozhong
 *
 */
@Component
public class AutoDistributeService implements IAutoDistributeService {

	@Autowired
	private EwashingStoreOrderService orderService;

	@Autowired
	private SendReceivePersonService sendService;

	private static final Logger logger = Logger.getLogger(AutoDistributeService.class);

	private static final PropertiesUtil propUtil = new PropertiesUtil("config.properties");

	
	/*@Scheduled(cron = "0/5 * *  * * ? ")*/
	@Override
	public void distributeOrder() {

		// 待取订单分配
		waitingFetchOrder();

		// 待送订单分配
		waitingSendOrder();
	}

	/**
	 * 待送订单分配
	 */
	private void waitingSendOrder() {

		StoreOrder order = new StoreOrder();
		order.setOrderType("2");
		order.setOrderStatus("6");
		order.setSendDistributeStatus("0");
		List<StoreOrder> list = orderService.findO2oOrderList(order);

		if (!CollectionUtils.isEmpty(list)) {
			for (StoreOrder o : list) {

				// 纬度
				Double latitude = o.getLatitude();
				// 经度
				Double longitude = o.getLongitude();
				// 根据订单经纬度分配订单
				List<SendReceivePerson> sendList = sendService.queryXiaoE(longitude, latitude, Integer.valueOf(propUtil.getProperty("distance")));

				if (!CollectionUtils.isEmpty(sendList)) {

					// 分配订单
					SendReceivePerson persion = sendList.get(0);
					// 2：送
					String type = "1";
					SecurityUser loginUser = new SecurityUser();
					loginUser.setUsername("定时任务自动分配");
					loginUser.setId("-1");
					boolean flag = orderService.distribute(o.getId(), loginUser, persion.getId(), type);

					if (!flag) {
						logger.info("distribute order error,orderCode is " + o.getOrderCode());
					}
				}
			}
		}
	}

	/**
	 * 待取订单分配
	 */
	private void waitingFetchOrder() {

		StoreOrder order = new StoreOrder();
		order.setOrderType("2");
		order.setOrderStatus("20");
		order.setDistributeStatus("0");
		List<StoreOrder> list = orderService.findO2oOrderList(order);

		if (!CollectionUtils.isEmpty(list)) {
			for (StoreOrder o : list) {

				// 纬度
				Double latitude = o.getLatitude();
				// 经度
				Double longitude = o.getLongitude();
				// 根据订单经纬度分配订单
				List<SendReceivePerson> sendList = sendService.queryXiaoE(longitude, latitude, Integer.valueOf(propUtil.getProperty("distance")));

				if (!CollectionUtils.isEmpty(sendList)) {

					// 分配订单
					SendReceivePerson persion = sendList.get(0);
					// 1：取
					String type = "1";
					SecurityUser loginUser = new SecurityUser();
					loginUser.setUsername("定时任务自动分配");
					loginUser.setId("-1");
					boolean flag = orderService.distribute(o.getId(), loginUser, persion.getId(), type);

					if (!flag) {
						logger.info("distribute order error,orderCode is " + o.getOrderCode());
					}
				}
			}
		}
	}

}

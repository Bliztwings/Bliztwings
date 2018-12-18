package com.ehyf.ewashing.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.ehyf.ewashing.dao.EwashingStoreOrderDao;
import com.ehyf.ewashing.dao.SendReceivePersonDao;
import com.ehyf.ewashing.dao.SendReceivePersonOrderRelationDao;
import com.ehyf.ewashing.entity.SecurityUser;
import com.ehyf.ewashing.entity.SendReceivePerson;
import com.ehyf.ewashing.entity.SendReceivePersonOrderRelation;
import com.ehyf.ewashing.entity.StoreOrder;
import com.ehyf.ewashing.exection.AppExection;
import com.ehyf.ewashing.restful.client.ResultCode;
import com.ehyf.ewashing.restful.client.ResultData;
import com.ehyf.ewashing.restful.service.TokenService;
import com.ehyf.ewashing.shiro.credentials.PasswordHelper;
import com.ehyf.ewashing.util.UUID;



@Service
public class SendReceivePersonService extends BaseService<SendReceivePersonDao,SendReceivePerson> {
	@Autowired
	SendReceivePersonDao sendReceivePersonDao;
	
	@Autowired
	SendReceivePersonOrderRelationDao sendReceivePersonOrderRelationDao;
	
	@Autowired
	private PasswordHelper passwordHelper;
	
	@Autowired
	private EwashingStoreOrderDao storeOrderDao;
	
	@Transactional(readOnly = false)
	public ResultData<Object> login(SendReceivePerson e){
		Date now = new Date();
		if(null == e){
			return ResultCode.error("无效的用户请求", null);
		}
		if(StringUtils.isBlank(e.getMobile()) || StringUtils.isBlank(e.getPassword())){
			return ResultCode.error("用户名或密码不能为空！", null);
		}
		
		// 密码加密
		//e.setPassword(MD5Util.encode(e.getPassword()));
		
		// 加密密码
		SecurityUser user =new SecurityUser();
		user.setPassword(e.getPassword());
		user.setUsername(e.getMobile());
		e.setPassword(passwordHelper.getEncryptPassword(user, e.getPassword()));
		SendReceivePerson xiaoe = sendReceivePersonDao.getByMbileAndPwd(e);
		if(null == xiaoe){
			return ResultCode.error("用户名或密码错误!", null);
		}
		
		// 控制老版本不让使用
		if(StringUtils.isEmpty(e.getAppVerson())){
			return ResultCode.error("当前app已经停用,请升级 ！", null);
		}
				
		// 验证平台类型是否正确
		if(!e.getPlatformType().equals(xiaoe.getPlatformType())){
			return ResultCode.error("用户不存在,请先申请！", null);
		}
		
		
		
		xiaoe.setToken(TokenService.XIAOE_TOKEN_FPRFIX.concat(UUID.getUUID32()));
		xiaoe.setLastLoginTime(now);
		
		SendReceivePerson s = new SendReceivePerson();
		s.setId(xiaoe.getId());
		s.setToken(xiaoe.getToken());
		s.setLastLoginTime(now);
		return sendReceivePersonDao.update(s)>0?ResultCode.success("操作成功", xiaoe):ResultCode.error("操作失败", null);
	}
	
	public SendReceivePerson getByToken(String token){
		return sendReceivePersonDao.getByToken(token);
	}
	
	
	public static void main(String args[]){
		SecurityUser user =new SecurityUser();
		user.setPassword("111111");
		user.setUsername("15926228709");
		PasswordHelper passwordHelper = new PasswordHelper();
		System.out.println(passwordHelper.getEncryptPassword(user, "111111"));
	}

	/**
	 * 获取附件的小E
	 * @param longitude
	 * @param latitude
	 * @param i
	 * @return
	 */
	public List<SendReceivePerson> queryXiaoE(Double longitude, Double latitude, int distance) {
		
		SendReceivePerson p =new SendReceivePerson();
		p.setLongitude(longitude);
		p.setLatitude(latitude);
		p.setDistance(distance);
		return sendReceivePersonDao.queryXiaoE(p);
	}

	/**
	 * 撤回订单
	 * @param orderId 订单ID
	 * @param type 1：取  2：送
	 * @return
	 */
	@Transactional(readOnly=false)
	public boolean revoke(String orderId,String type) {
		
		// 判断该订单已经接受
		SendReceivePersonOrderRelation r =new SendReceivePersonOrderRelation();
		r.setOrderId(orderId);
		r.setAcceptStatus("1"); // 已分配，可以撤回
		r.setDistributeType(type); // 1：取  2：送
		List<SendReceivePersonOrderRelation> list =sendReceivePersonOrderRelationDao.findRevokeList(r);
		//没有已分配的订单
		if(CollectionUtils.isEmpty(list)){
			return false;
		}
		
		// 更新订单分配关系表状态为拒绝
		r =new SendReceivePersonOrderRelation();
		r.setOrderId(orderId);
		r.setDistributeType(type);
		r.setAcceptStatus("2");
		int result =sendReceivePersonOrderRelationDao.updatSendReceivePersonOrderRelation(r);
		if(result<=0){
			return false;
		}
		
		// 更新订单信息
		StoreOrder order =new StoreOrder();
		order.setId(orderId);
		// 取
		if("1".equals(type)){
			order.setDistributeStatus("0");// 更新为未分配
		}
		//送
		if("2".equals(type)){
			order.setSendDistributeStatus("0");
		}
		int result1 =storeOrderDao.update(order);
		if(result1<=0){
			throw new AppExection("撤销分配订单失败");
		}
		return true;
	}
}

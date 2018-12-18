package com.ehyf.ewashing.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ehyf.ewashing.dao.EwashingStoreDao;
import com.ehyf.ewashing.entity.EwashingStore;
import com.ehyf.ewashing.entity.SecurityUser;

@Service
public class EwashingStoreService extends BaseService<EwashingStoreDao,EwashingStore> {

    @Autowired
    private EwashingStoreDao storeDao;
    
	/**
	 * 根据门店编码获取门店账号
	 * @param storeCode
	 * @return
	 */
	public List<SecurityUser> findSecurityUserList(String storeCode) {
		return null;
	}
	
	/**
     * 根据登录用户id获取门店信息. <br>
     * @param securityUserId 登录用户id
     * @return <br>
     */
	public EwashingStore getEwashingStoreByStoreCode(String storeCode) {
        return storeDao.getEwashingStoreByStoreCode(storeCode);
    }

}

package com.ehyf.ewashing.dao;

import org.springframework.stereotype.Repository;

import com.ehyf.ewashing.entity.EwashingStore;

@Repository
public interface EwashingStoreDao extends BaseDao<EwashingStore> {

    /**
     * 根据登录用户id获取门店信息. <br>
     * 
     * @param securityUserId 登录用户id
     * @return <br>
     */
    EwashingStore getEwashingStoreByStoreCode(String storeCode);

}

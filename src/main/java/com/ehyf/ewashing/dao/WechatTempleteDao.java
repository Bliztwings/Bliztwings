package com.ehyf.ewashing.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.ehyf.ewashing.entity.WechatConfig;

@Repository
public interface WechatTempleteDao extends BaseDao<WechatConfig> {

	WechatConfig getWechatConfigByAppId(@Param("appId") String appId);

}

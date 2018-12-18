package com.ehyf.ewashing.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ehyf.ewashing.common.Constants;
import com.ehyf.ewashing.dao.CheckCodeDao;
import com.ehyf.ewashing.entity.CheckCode;
import com.ehyf.ewashing.restful.client.CheckCodeControllerRestful;
import com.ehyf.ewashing.util.DateUtil;
import com.ehyf.ewashing.util.LocalThreadUtils;

/**
 * 
 * CheckCodeService  Service服务接口类
 * 
 **/
@Service
public class CheckCodeService extends BaseService<CheckCodeDao,CheckCode> {
	Logger logger =Logger.getLogger(CheckCodeService.class);
	@Autowired
	CheckCodeDao checkCodeDao;
	
	/**
	 * 验证码验证
	 * @param checkCode
	 * @return
	 */
	public boolean ckeck(CheckCode checkCode){
		logger.info("验证码验证服务，移动电话："+checkCode.getTelephone()+"，验证码："+checkCode.getCode()+"，appId："+checkCode.getAppId()+"，类型："+checkCode.getType());
		if(null==checkCode || StringUtils.isBlank(checkCode.getTelephone()) || StringUtils.isBlank(checkCode.getCode()) || StringUtils.isBlank(checkCode.getType())) return false;
		if(StringUtils.isBlank(checkCode.getAppId())){
			String appid = (String)LocalThreadUtils.get(LocalThreadUtils.CONSTRANT_APP_ID);
			if(StringUtils.isBlank(appid)) return false;
			checkCode.setAppId(appid);
		}
		Date now = new Date();
		boolean r = false;
		List<CheckCode> c = findList(checkCode);
		if(null!=c && c.size()>0){
			for(CheckCode co:c){
				if(isTimeValid(co.getCreateTime(), now)){
					r= true;break;
				}
			}
			checkCodeDao.invalidCode(checkCode);
		}
		return  r ;
	}
	
	
	private boolean isTimeValid(Date d1,Date d2){
		if(null==d1 || null==d2)return false;
		Long diff = DateUtil.getDiffSecond(d1, d2);
		logger.info("diff minis is "+diff);
		return diff>0?(Constants.CHECKCODE_INVALID_SECOND-diff)>0:true;
	}
}
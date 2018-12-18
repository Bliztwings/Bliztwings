package com.ehyf.ewashing.restful.service;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ehyf.ewashing.entity.Member;
import com.ehyf.ewashing.entity.SendReceivePerson;
import com.ehyf.ewashing.service.MemberService;
import com.ehyf.ewashing.service.SendReceivePersonService;
import com.ehyf.ewashing.util.DateUtil;

/**
 * 获取会员TOKEN
 * @author fqdeng
 *
 */
@Service
public class TokenService {
	
	private static final Logger logger =Logger.getLogger(TokenService.class);
	@Autowired
	MemberService memberService;
	@Autowired
	SendReceivePersonService sendReceivePersonService;
	
	public static final long TOKEN_INVALID_SECOND=60*60*24;//TOKEN失效时长为60*24分钟
	public static final String O2O_TOKEN_FPRFIX = "O2O_";//o2o会员token前缀
	public static final String XIAOE_TOKEN_FPRFIX = "XE_";//小Etoken前缀
	public static final Date VALID_DATE=DateUtil.parseData("1900-01-01 00:00:00");
	
	
	/**
	 * 验证token是否有效
	 * @param token
	 * @return
	 */
	public boolean isTokenValid(String token){
		
		logger.info("currentToken:"+token);
		
		if(!isLegalToken(token))return false;
		Date now = new Date();
		if(token.startsWith(TokenService.O2O_TOKEN_FPRFIX)){
			//O2O用户验证
			Member m = memberService.getByToken(token);
			return null!=m && isTimeValid(m.getLastLoginTime(), now);
		}else if(token.startsWith(TokenService.XIAOE_TOKEN_FPRFIX)){
			//小E用户验证
			SendReceivePerson p = sendReceivePersonService.getByToken(token);
			return null!=p && isTimeValid(p.getLastLoginTime(), now);
		}
		return false;
	}
	
	
	//验证d2-d1是否有效，true:有效，false:无效
	private boolean isTimeValid(Date d1,Date d2){
		if(null==d1 || null==d2)return false;
		Long diff = DateUtil.getDiffSecond(d1, d2);
		return diff>0?(TOKEN_INVALID_SECOND-diff)>0:true;
	}
	
	//验证token合法性，true:合法，false:非法
	private boolean isLegalToken(String token){
		if(StringUtils.isBlank(token))return false;
		return token.startsWith(O2O_TOKEN_FPRFIX) || token.startsWith(XIAOE_TOKEN_FPRFIX);
	}
}

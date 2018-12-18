package com.ehyf.ewashing.restful.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ehyf.ewashing.restful.client.ResultCode;
import com.ehyf.ewashing.restful.client.ResultData;
import com.ehyf.ewashing.util.GsonUtil;
import com.ehyf.ewashing.util.LocalThreadUtils;

/**
 * O2O用户验证filter
 * @author fqdeng
 *
 */
@Component("o2oLoginFilter")
public class O2oLoginFilter implements Filter {
	@Autowired
	TokenService tokenService;
	private static final ResultData<Object> ACCESS_DENIED= ResultCode.accessDenied("拒绝访问", null);
	private List<String> filterUrls = new ArrayList<String>();
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		//简单实现，后续可以配置到字典池中
		filterUrls.add("/registerAddress");
		filterUrls.add("/modifyAddress");
		filterUrls.add("/delAddressById");
		filterUrls.add("/getAddressByMemberId");
		filterUrls.add("/changePassword");
		filterUrls.add("/onlineRecharge");
		filterUrls.add("/queryOrderLogisticsInfo");
		filterUrls.add("/order");
		filterUrls.add("/prepayOrder");
		filterUrls.add("/memberOrder/");
		filterUrls.add("/sendReceiveOrder/");
		filterUrls.add("/member");
		filterUrls.add("/feedBack");
		filterUrls.add("/sealNumber/");
		filterUrls.add("/pay");
		filterUrls.add("/valuation");
		filterUrls.add("/answer");
		filterUrls.add("/orderDetail");
		filterUrls.add("/getMemberCardInfo");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try {
			HttpServletRequest httpServletRequest = (HttpServletRequest)request;
			String token = httpServletRequest.getHeader("token");
			String url = httpServletRequest.getRequestURI();
			String appId = httpServletRequest.getHeader("appid");
			if(StringUtils.isNotBlank(appId)) LocalThreadUtils.set(LocalThreadUtils.CONSTRANT_APP_ID, appId);
			if(StringUtils.isNotBlank(url)) LocalThreadUtils.set(LocalThreadUtils.CONSTRANT_HTTP_REQUEST_URL, url);
			if(isFilterUrl(url)){
				//验证token是否有效
				if(StringUtils.isBlank(token) || !tokenService.isTokenValid(token)){
					//去登录页面
					response.getWriter().write(GsonUtil.toJson(ACCESS_DENIED));
				}else{
					chain.doFilter(request, response);
				}
			}else {
				//无需token验证
				chain.doFilter(request, response);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void destroy() {

	}
	
	private boolean isFilterUrl(String url){
		for(String s:filterUrls){
			if(url.indexOf(s)>0)return true;
		}
		return false;
	}
	

}

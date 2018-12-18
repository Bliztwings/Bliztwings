/*
 * 文 件 名:  CustomFormAuthenticationFilter.java
 * 版    权:  gaoyang. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  gaoyang
 * 修改时间:  2016年12月9日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.ehyf.ewashing.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

/**
 * <继承FormAuthenticationFilter的权限验证自定义类>
 * 
 * @author gaoyang
 * @version [版本号, 2016年12月9日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class CustomFormAuthenticationFilter extends FormAuthenticationFilter {

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		try {
		if (isLoginRequest(request, response)) {
			if (isLoginSubmission(request, response)) {
				String account = this.getUsername(request);

				Subject subject = this.getSubject(request, response);
				String username = (String)SecurityUtils.getSubject().getPrincipal();

				if (account != null && username != null) {
					if(!account.equals(username)){
						subject.logout();
					}else{
						return false;
					}
				}
			}
		}
		}catch(Throwable e) {
		}

		return super.isAccessAllowed(request, response, mappedValue);
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response, Object mappedValue)
			throws Exception {
		HttpSession session = ((HttpServletRequest) request).getSession();
		String captcha = request.getParameter("captcha");
		String verCode = (String) session.getAttribute("verCode");
		if (verCode != null && captcha != null && !verCode.equals(captcha.trim().toLowerCase())) {
			request.setAttribute("shiroLoginFailure", "randomCodeError");
			return true;
		}
		return super.onAccessDenied(request, response, mappedValue);
	}
}
package com.ehyf.ewashing.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {

	@RequestMapping(value = "/login", method = { RequestMethod.POST, RequestMethod.GET })
	public String showLoginForm(HttpServletRequest req, Model model) {
		try {
			String exceptionClassName = (String) req.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
			String resultMsg = "";
			if (UnknownAccountException.class.getName().equals(
					exceptionClassName)) {
				resultMsg = "不存在该用户名";
			} else if (IncorrectCredentialsException.class.getName().equals(
					exceptionClassName)) {
				resultMsg = "用户名/密码错误";
			} else if (LockedAccountException.class.getName().equals(
					exceptionClassName)) {
				resultMsg = "用户账号被锁定";
			} else if (exceptionClassName == null) {
				resultMsg = "";
			}else if("randomCodeError".equals(exceptionClassName)){  
				resultMsg = "验证码错误";
            } else if (exceptionClassName != null) {
				resultMsg = "其他错误：" + exceptionClassName;
			}

			if (req.getParameter("forceLogout") != null) {
				model.addAttribute("error", "您已经被管理员强制退出，请重新登录");
				resultMsg = "您已经被管理员强制退出，请重新登录" ;
			}
			model.addAttribute("resultMsg", resultMsg);
			return "login";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "login";
	}

	/**
	 * 跳转到登录页面
	 * @return
	 */
	@RequestMapping(value = "/toLogin", method = { RequestMethod.POST,RequestMethod.GET })
	public String toLogin() {
		return "login";
	}
	
	/**
	 * 获取验证码
	 * @return
	 */
	@RequestMapping(value = "/getCode", method = { RequestMethod.POST,RequestMethod.GET })
	@ResponseBody
	public String getCode(){
		Subject currentUser = SecurityUtils.getSubject();  
		Session session = currentUser.getSession();
		String sessionCode = (String)session.getAttribute("verCode");		//获取session中的验证码
		return sessionCode;
	}

}

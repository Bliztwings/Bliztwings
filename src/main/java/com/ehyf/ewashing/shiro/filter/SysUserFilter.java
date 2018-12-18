package com.ehyf.ewashing.shiro.filter;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.springframework.beans.factory.annotation.Autowired;

import com.ehyf.ewashing.common.Constants;
import com.ehyf.ewashing.entity.EwashingStore;
import com.ehyf.ewashing.entity.SecurityUser;
import com.ehyf.ewashing.service.EwashingStoreService;
import com.ehyf.ewashing.service.SecurityUserService;
import com.ehyf.ewashing.util.IpUtil;

/**
 * <访问拦截器>
 * 
 * @author gaoyang
 * @version [版本号, 2015-3-17]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class SysUserFilter extends PathMatchingFilter {

    Log log = LogFactory.getLog(SysUserFilter.class);

    @Autowired
    private SecurityUserService userService;

    @Autowired
    private EwashingStoreService storeService;

    @SuppressWarnings("unchecked")
    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        HttpServletRequest hsr = (HttpServletRequest) request;
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        SecurityUser user = userService.findUserByLoginName(username);
        if (null != user) {
            EwashingStore store = storeService.getById(user.getStoreId());
            if (null != store)
                user.setEwashingStore(store);
        }
        request.setAttribute(Constants.CURRENT_USER, user);

        Map<String, HttpSession> sessionMap = (Map<String, HttpSession>) hsr.getServletContext().getAttribute("sessionMap");
        if (sessionMap == null) {
            sessionMap = new HashMap<String, HttpSession>();
        }
        HttpSession session = hsr.getSession();
        if (session != null) {
            session.setAttribute(Constants.CURRENT_USER, user);
            session.setAttribute("user_ip", IpUtil.getIpAddress(hsr));
        }
        sessionMap.put(username, session);
        hsr.getServletContext().setAttribute("sessionMap", sessionMap);
        return true;
    }
}

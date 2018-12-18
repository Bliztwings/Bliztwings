package com.ehyf.ewashing.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ehyf.ewashing.common.Constants;
import com.ehyf.ewashing.entity.SecurityUser;

/**
 * 回话管理
 * @author 
 *
 */
@Controller
@RequestMapping("/session")
public class SessionController {
	private static Log log = LogFactory.getLog(SessionController.class);
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/list",method = {RequestMethod.POST, RequestMethod.GET})
	public String list(HttpServletRequest req, Model model){
		log.info("进入会话管理");
		String username = req.getParameter("username");
		Map<String,HttpSession> sessionMap = (Map<String,HttpSession>)req.getServletContext().getAttribute("sessionMap");
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		
		HttpSession currSession = req.getSession();
		SecurityUser user = (SecurityUser)currSession.getAttribute(Constants.CURRENT_USER);
		
		if(sessionMap!=null && sessionMap.size()>0){
			Iterator<Entry<String,HttpSession>> itor = sessionMap.entrySet().iterator();
			Entry<String,HttpSession> entry = null;
			Map<String,Object> map = null;
			while(itor.hasNext()){   
				entry = itor.next();    
				if(sessionIsValid(entry.getValue())){
					map = new HashMap<String,Object>();
					map.put("loginTime", new Date(entry.getValue().getCreationTime()));
					map.put("username", entry.getKey());
					map.put("isCurr", entry.getKey().equals(user.getUsername()));
					map.put("ip", entry.getValue().getAttribute("user_ip"));
					list.add(map);
				}
			} 
		}
		
		if(StringUtils.isNotBlank(username)){
			list = selectiveListByKeyword(username,list);
		}
		
		Collections.sort(list,new Comparator<Map<String,Object>>(){
			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				Date date1 = (Date)o1.get("loginTime");
				Date date2 = (Date)o2.get("loginTime");
				if(date1.getTime() > date2.getTime()){
					return -1;
				}
				else if(date1.getTime() < date2.getTime()){
					return 1;
				}
				else{
					return 0;
				}
			}
		});  
		
		model.addAttribute("userList", list);
		model.addAttribute("username", username);
		return "security/session/list";	
	}
	
	private List<Map<String,Object>> selectiveListByKeyword(String username,List<Map<String,Object>> list){
		List<Map<String,Object>> newList = new ArrayList<Map<String,Object>>();
		if(list!=null && list.size()>0){
			for(Map<String,Object> map : list){
				if(map.get("username").toString().toLowerCase().contains(username.toLowerCase())){
					newList.add(map);
				}
			}
		}
		return newList;
	}
	
	/**
	 * 检测session是否过期
	 * @param session
	 * @return
	 */
	@SuppressWarnings("unused")
	private boolean sessionIsValid(HttpSession session){
		boolean valid = true;
		try{
			if(session!=null){
				SecurityUser user = (SecurityUser)session.getAttribute(Constants.CURRENT_USER);
			}
		}catch(Exception e){
			valid = false;
			return valid;
		}
		return valid;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/kickOff",method = {RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public String kickOff(HttpServletRequest req,Model model){
		String username = req.getParameter("username");
		Map<String,HttpSession> sessionMap = (Map<String,HttpSession>)req.getServletContext().getAttribute("sessionMap");
		if(sessionMap!=null && sessionMap.size()>0){
			Iterator<Entry<String,HttpSession>> itor = sessionMap.entrySet().iterator();
			Entry<String,HttpSession> entry = null;
			while(itor.hasNext()){   
				entry = itor.next();    
				if(sessionIsValid(entry.getValue()) && entry.getKey().equals(username)){
					entry.getValue().invalidate();
				}
			} 
		}
		
		model.asMap().clear();
		ObjectMapper mapper = new ObjectMapper(); //转换器  
		model.addAttribute("resultMsg", "踢出用户成功！");
		model.addAttribute("resultCode", 1);
		try {
			return mapper.writeValueAsString(model);
		} catch (Exception e) {
			return null;
		}
	}
}

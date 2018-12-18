package com.ehyf.ewashing.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ehyf.ewashing.entity.SecurityOperLog;
import com.ehyf.ewashing.service.SecurityOperLogService;
import com.ehyf.ewashing.util.GsonUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 操作日志
 * @author Dell
 *
 */
@Controller()
@RequestMapping("/operLog")
public class OperLogController {
	
	Log log = LogFactory.getLog(OperLogController.class);
	
	@Autowired
	private SecurityOperLogService securityOperLogService;

	@RequestMapping(value = "/list",method = {RequestMethod.POST, RequestMethod.GET})
	public String list(HttpServletRequest req, SecurityOperLog operLog, Model model) {
		
		String pageNum = req.getParameter("pageNum");
		String pageSize = req.getParameter("pageSize");
		
		String userName = req.getParameter("userName");
		String operObject = req.getParameter("operObject");
		String operStartTime = req.getParameter("operStartTime");
		String operEndTime = req.getParameter("operEndTime");
	
		try {
			if (StringUtils.isBlank(pageNum)) {
				pageNum = "1";
			}
			if (StringUtils.isBlank(pageSize)) {
				pageSize = "10";
			}

			PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize));
			SecurityOperLog entity = new SecurityOperLog();
			List<SecurityOperLog> list = securityOperLogService.findList(entity);
			PageInfo<SecurityOperLog> page = new PageInfo<SecurityOperLog>(list);
			
			model.addAttribute("page", page);
			model.addAttribute("userName", userName);
			model.addAttribute("operStartTime", operStartTime);
			model.addAttribute("operEndTime", operEndTime);
			model.addAttribute("operObject", operObject);
			
			return "security/operLog/list";
			
		} catch (Exception e) {
			log.error("查询操作日志列表出现异常。", e);
			return null;
		}
		
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public String delete(HttpServletRequest req, SecurityOperLog operLog,
			Model model, RedirectAttributes redirectAttributes) {
		String ids = req.getParameter("ids").trim();
		model.asMap().clear();
		String [] arr = ids.split(",");
		if(StringUtils.isBlank(ids)){
			model.addAttribute("resultMsg", "删除操作日志记录时，参数无效！");
			model.addAttribute("resultCode", 0);
			return  GsonUtil.toJson(model);
		}
		
		try {
			ArrayList<String> idList = new ArrayList<String>();
			for (int i = 0; i < arr.length; i++) {
				idList.add(arr[i]);			
			}
			
			securityOperLogService.batchDeleteByIds(idList);
			model.addAttribute("resultMsg", "删除操作日志记录成功！");
			model.addAttribute("resultCode", 1);
			return  GsonUtil.toJson(model);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			model.addAttribute("resultMsg", "删除操作日志记录失败！");
			model.addAttribute("resultCode", 0);
			return  model.toString();
		}
	}
	
	
	
}

package com.ehyf.ewashing.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ehyf.ewashing.emun.FeedBackStatus;
import com.ehyf.ewashing.entity.FeedBack;
import com.ehyf.ewashing.service.FeedBackService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("/feedBack")
public class FeedBackController {

	@Autowired
	private FeedBackService feedBackService;
	
	private static Logger logger =Logger.getLogger(FeedBackController.class);

	
	@RequestMapping(value = "/list", method = { RequestMethod.POST, RequestMethod.GET })
	public String list(HttpServletRequest req, FeedBack feedBack, HttpSession session, Model model) {

		String pageNum = req.getParameter("pageNum");
		String pageSize = req.getParameter("pageSize");

		try {
			if (StringUtils.isBlank(pageNum)) {
				pageNum = "1";
			}
			if (StringUtils.isBlank(pageSize)) {
				pageSize = "10";
			}
			PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize));
			List<FeedBack> list = feedBackService.findList(feedBack);
			
			if(!CollectionUtils.isEmpty(list)){
				for(FeedBack fb :list){
					fb.setFeedBackType(FeedBackStatus.getName(fb.getFeedBackType()));
				}
			}
			
			PageInfo<FeedBack> page = new PageInfo<FeedBack>(list);

			model.addAttribute("page", page);
			return "ewashing/o2o/feedBack";
		} catch (Exception e) {
			logger.error("查询反馈信息失败。", e);
			return null;
		}
	}

	
}

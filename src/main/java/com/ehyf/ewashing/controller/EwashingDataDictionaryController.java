package com.ehyf.ewashing.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.ehyf.ewashing.common.Constants;
import com.ehyf.ewashing.entity.EwashingDataDictionary;
import com.ehyf.ewashing.entity.EwashingStore;
import com.ehyf.ewashing.entity.ProduceCategory;
import com.ehyf.ewashing.entity.SecurityUser;
import com.ehyf.ewashing.service.EwashingDataDictionaryService;
import com.ehyf.ewashing.service.FileUploadService;
import com.ehyf.ewashing.service.ProduceCategoryService;
import com.ehyf.ewashing.util.StringUtils;
import com.ehyf.ewashing.util.UUID;

@Controller
@RequestMapping("/data")
public class EwashingDataDictionaryController {

	@Autowired
	private EwashingDataDictionaryService ewashingDataService;
	@Autowired
	private ProduceCategoryService  produceCategory;
	
	@Autowired
	private FileUploadService fileService;
	private static Logger logger =Logger.getLogger(EwashingDataDictionaryController.class);
	
	
	
	@RequestMapping(value = "/initAdd",method = {RequestMethod.POST, RequestMethod.GET})
	public String initAdd(HttpServletRequest req, EwashingDataDictionary ewashingDataDictionary,HttpSession session,
			Model model) {
		String dataType =req.getParameter("dataType");
		model.addAttribute("dataType", dataType);
		
		List<ProduceCategory>  list =produceCategory.getRoot();
		model.addAttribute("productTypeList", list);
		
		return "ewashing/data/addData";
	}
	
	
	@RequestMapping(value = "/saveData",method = {RequestMethod.POST})
	public String saveData(HttpServletRequest req, EwashingDataDictionary ewashingDataDictionary,HttpSession session,
			Model model) {
		
		SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
		ewashingDataDictionary.setId(UUID.getUUID32());
		ewashingDataDictionary.setCreateDate(new Date());
		ewashingDataDictionary.setCreateUserId(loginUser.getId());
		ewashingDataDictionary.setCreateUserName(loginUser.getUsername());
		
		try {
			

			if (req instanceof MultipartHttpServletRequest) {
	            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) req;
	            // 获取上传的文件
	            Map<String, MultipartFile> fileMap = multipartHttpServletRequest.getFileMap();
	            if(!fileMap.isEmpty()){
	            	for(Map.Entry<String, MultipartFile> entry : fileMap.entrySet()){
	            		if(!StringUtils.isEmptyString(entry.getValue().getOriginalFilename())){
	            			String filePath =fileService.uploadFile(entry.getValue().getInputStream(), entry.getValue().getOriginalFilename());
			                ewashingDataDictionary.setImagePath(filePath);
	            		}
		            }
	            }
	        }
			
			
			int count =ewashingDataService.insertDicData(ewashingDataDictionary);
			if(count<=0){
				model.addAttribute("resultMsg", "添加失败！");
				model.addAttribute("resultCode", 0);
			}
			else{
				model.addAttribute("resultMsg", "添加成功！");
				model.addAttribute("resultCode", 1);
			}
			model.addAttribute("dataType", ewashingDataDictionary.getDataType());
		}
		catch(Exception e){
			logger.error(e.getMessage());
			model.addAttribute("resultMsg", "添加失败！");
			model.addAttribute("resultCode", 0);
			return JSONObject.toJSONString(model);
		}
		return "ewashing/data/addData";
	}
	
	@RequestMapping(value = "/saveDataAjax",method = {RequestMethod.POST})
	@ResponseBody
	public String saveDataAjax(HttpServletRequest req, EwashingDataDictionary ewashingDataDictionary,HttpSession session,
			Model model) {
		
		ObjectMapper objectMapper = new ObjectMapper();
		SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
		ewashingDataDictionary.setId(UUID.getUUID32());
		ewashingDataDictionary.setCreateDate(new Date());
		ewashingDataDictionary.setCreateUserId(loginUser.getId());
		ewashingDataDictionary.setCreateUserName(loginUser.getUsername());
		try {
			model.asMap().clear();
			int count =ewashingDataService.insertDicData(ewashingDataDictionary);
			if(count<=0){
				model.addAttribute("resultMsg", "添加失败！");
				model.addAttribute("resultCode", 0);
			}
			else{
				model.addAttribute("resultMsg", "添加成功！");
				model.addAttribute("resultCode", 1);
			}
			model.addAttribute("dataType", ewashingDataDictionary.getDataType());
			return objectMapper.writeValueAsString(model);
		}
		catch(Exception e){
			logger.error(e.getMessage());
			model.addAttribute("resultMsg", "添加失败！");
			model.addAttribute("resultCode", 0);
			return JSONObject.toJSONString(model);
		}
		
	}
	
	
	@RequestMapping(value = "/getChildren",method = {RequestMethod.POST})
	@ResponseBody
	public String getChildren(HttpServletRequest req, HttpSession session,
			Model model) {
		
		ObjectMapper objectMapper = new ObjectMapper();
		String produceParentCategoryId =req.getParameter("produceParentCategoryId");
		try {
			
			List<ProduceCategory> list =produceCategory.getChildren(produceParentCategoryId, null);
			model.addAttribute("list", list);
			return objectMapper.writeValueAsString(model);
		}
		catch(Exception e){
			logger.error(e.getMessage());
			model.addAttribute("resultMsg", "获取失败！");
			model.addAttribute("resultCode", 0);
			return JSONObject.toJSONString(model);
		}
		
	}
	
	@RequestMapping(value = "/updateData",method = {RequestMethod.POST})
	public String updateData(HttpServletRequest req, EwashingDataDictionary ewashingDataDictionary,HttpSession session,
			Model model) {
		
		SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
		ewashingDataDictionary.setUpdateDate(new Date());
		ewashingDataDictionary.setUpdateUserName(loginUser.getRealname());
		ewashingDataDictionary.setUpdateUserId(loginUser.getId());
		try {
			
			if (req instanceof MultipartHttpServletRequest) {
	            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) req;
	            // 获取上传的文件
	            Map<String, MultipartFile> fileMap = multipartHttpServletRequest.getFileMap();
	            for(Map.Entry<String, MultipartFile> entry : fileMap.entrySet()){
	                System.out.println(entry.getKey() + ":" + entry.getValue().getOriginalFilename());
	                
	                String filePath =fileService.uploadFile(entry.getValue().getInputStream(), entry.getValue().getOriginalFilename());
	                ewashingDataDictionary.setImagePath(filePath);
	            }
	        }
			
			int count =ewashingDataService.updateDicData(ewashingDataDictionary);
			if(count<=0){
				model.addAttribute("resultMsg", "修改失败！");
				model.addAttribute("resultCode", 0);
			}
			else{
				model.addAttribute("resultMsg", "修改成功！");
				model.addAttribute("resultCode", 1);
			}
		}
		catch(Exception e){
			logger.error(e.getMessage());
			model.addAttribute("resultMsg", "修改失败！");
			model.addAttribute("resultCode", 0);
			return JSONObject.toJSONString(model);
		}
		return "ewashing/data/edit";
	}
	
	
	@RequestMapping(value = "/deleteData",method = {RequestMethod.POST})
	@ResponseBody
	public String deleteData(HttpServletRequest req, EwashingStore store,HttpSession session,
			Model model) {
		ObjectMapper objectMapper = new ObjectMapper();
		try{
			model.asMap().clear();
			String id = req.getParameter("id");
			ewashingDataService.deleteById(id);
			model.addAttribute("resultCode", "1");	
			model.addAttribute("resultMsg", "数据删除成功");	
			return objectMapper.writeValueAsString(model);
		}
		catch(Exception e){
			model.addAttribute("resultCode", "0");	
			model.addAttribute("resultMsg", "数据删除失败");
			return JSONObject.toJSONString(model);
		}
		
	}
	
	
	@RequestMapping(value = "/list",method = {RequestMethod.POST, RequestMethod.GET})
	public String list(HttpServletRequest req, EwashingDataDictionary ewashingDataDictionary,HttpSession session,
			Model model) {
		return "ewashing/data/list";
	}
	
	
	
	@RequestMapping(value = "/preUpdate",method = {RequestMethod.POST, RequestMethod.GET})
	public String preUpdate(HttpServletRequest req,HttpSession session,
			Model model) {
		String primaryId =req.getParameter("id");
		EwashingDataDictionary dic = ewashingDataService.getById(primaryId);
		model.addAttribute("dictionary", dic);	
		List<ProduceCategory>  list =produceCategory.getRoot();
		
		List<ProduceCategory>  productChildrenList =produceCategory.getChildren(dic.getProduceParentCategoryId(), null);
		model.addAttribute("productChildrenList", productChildrenList);
		model.addAttribute("productTypeList", list);
		return "ewashing/data/edit";
	}
	
	@RequestMapping(value = "/queryDataByType",method = {RequestMethod.POST})
	@ResponseBody
	public String queryDataByType(HttpServletRequest req, EwashingDataDictionary ewashingDataDictionary,HttpSession session,
			Model model) {
		ObjectMapper objectMapper = new ObjectMapper();
		try{
			model.asMap().clear();
			String dataType = req.getParameter("dataType");
			String serviceType =req.getParameter("serviceType");
			ewashingDataDictionary.setQueryKey(ewashingDataDictionary.getQueryKey());
			ewashingDataDictionary.setServiceType(serviceType);
			ewashingDataDictionary.setDataType(dataType);
			List<EwashingDataDictionary> list = ewashingDataService.findList(ewashingDataDictionary);
			model.addAttribute("dataList", list);	
			model.addAttribute("dataName", ewashingDataDictionary.getDataName());	
			
			model.addAttribute("resultCode", "1");	
			model.addAttribute("resultMsg", "数据删除成功");	
			return objectMapper.writeValueAsString(model);
		}
		catch(Exception e){
			model.addAttribute("resultCode", "0");	
			model.addAttribute("resultMsg", "数据删除失败");
			return JSONObject.toJSONString(model);
		}
	}
	
}

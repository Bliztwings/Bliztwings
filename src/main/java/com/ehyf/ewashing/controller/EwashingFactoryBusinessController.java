package com.ehyf.ewashing.controller;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.ehyf.ewashing.common.Constants;
import com.ehyf.ewashing.emun.ClothesStatus;
import com.ehyf.ewashing.emun.OrderStatus;
import com.ehyf.ewashing.entity.ClothesAttach;
import com.ehyf.ewashing.entity.ClothesFlow;
import com.ehyf.ewashing.entity.ClothesPhoto;
import com.ehyf.ewashing.entity.EwashingStore;
import com.ehyf.ewashing.entity.EwashingStoreUser;
import com.ehyf.ewashing.entity.HandOnArea;
import com.ehyf.ewashing.entity.HandOnNo;
import com.ehyf.ewashing.entity.SecurityUser;
import com.ehyf.ewashing.entity.SendReceivePersonOrderRelation;
import com.ehyf.ewashing.entity.StoreClothes;
import com.ehyf.ewashing.entity.StoreOrder;
import com.ehyf.ewashing.service.ClothesAttachService;
import com.ehyf.ewashing.service.ClothesFlowService;
import com.ehyf.ewashing.service.EwashingFactoryBusinessService;
import com.ehyf.ewashing.service.EwashingHandonAreaService;
import com.ehyf.ewashing.service.EwashingHandonNoService;
import com.ehyf.ewashing.service.EwashingStoreBusinessService;
import com.ehyf.ewashing.service.EwashingStoreClothesService;
import com.ehyf.ewashing.service.EwashingStoreOrderService;
import com.ehyf.ewashing.service.EwashingStoreService;
import com.ehyf.ewashing.service.FileUploadService;
import com.ehyf.ewashing.service.OrderLogisticsInfoService;
import com.ehyf.ewashing.service.SendReceivePersonOrderRelationService;
import com.ehyf.ewashing.service.WechatTempleteService;
import com.ehyf.ewashing.util.DateUtil;
import com.ehyf.ewashing.util.PropertiesUtil;
import com.ehyf.ewashing.util.StringUtils;
import com.ehyf.ewashing.util.UUID;
import com.ehyf.ewashing.wechat.proxy.WeChatProxy;

/**
 * 工厂综合业务
 * 
 * @author zhaodan
 */

@Controller
@RequestMapping("/factory")
public class EwashingFactoryBusinessController {

    @Autowired
    private EwashingStoreService mStoreService;

    @Autowired
    private EwashingStoreClothesService mStoreClothesService;

    @Autowired
    private EwashingStoreBusinessService storeBusiness;

    @Autowired
    private ClothesFlowService clothesFlowService;

    @Autowired
    private EwashingFactoryBusinessService factoryService;
    
    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private EwashingHandonAreaService handonArea;
    
    @Autowired
    private EwashingHandonNoService handonNo;

    @Autowired
    private EwashingStoreOrderService orderQueryService;

    @Autowired
    private ClothesAttachService attachService;
    
    @Autowired
    private SendReceivePersonOrderRelationService sendService;
    
    @Autowired
	private OrderLogisticsInfoService  logisticsInfoService;

    @Autowired
    private EwashingStoreOrderService orderService;
    @Autowired
	private WechatTempleteService templeteService;
    
    private static Logger logger = Logger.getLogger(EwashingFactoryBusinessController.class);
    
    private static PropertiesUtil propertiesUtil= new PropertiesUtil("config.properties");

    /**
     * 签收
     * 
     * @param req
     * @param ewashingStoreUser
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "/main", method = { RequestMethod.POST, RequestMethod.GET })
    public String main(HttpServletRequest req, EwashingStoreUser ewashingStoreUser, HttpSession session, Model model) {
        return "ewashing/factory/main";
    }

    /**
     * 入厂. <br>
     * 
     * @param req
     * @param ewashingStoreUser
     * @param session
     * @param model
     * @return <br>
     */
    @RequestMapping(value = "/inFactory", method = { RequestMethod.POST, RequestMethod.GET })
    public String inFactory(HttpServletRequest req, EwashingStoreUser ewashingStoreUser, HttpSession session, Model model) {
        return "ewashing/factory/inFactory";
    }

    /**
     * 根据门店查询出厂衣服信息
     * 
     * @param req
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "/queryClothes", method = { RequestMethod.POST })
    @ResponseBody
    public String queryClothes(HttpServletRequest req, HttpSession session, Model model) {

        String storeId = req.getParameter("storeId");
        String type = req.getParameter("type");
        String barCode = req.getParameter("barCode");

        List<StoreClothes> list = null;
        EwashingStore store = null;
        List<ClothesAttach> acList = null;
        // 根据门店查询,需要洗涤工序完成，才可以出厂
        if ("1".equals(type)) {
            StoreClothes clothes = new StoreClothes();
            clothes.setStoreId(storeId);
            clothes.setStatus("12");
            list = storeBusiness.findClothes(clothes);

            if (!CollectionUtils.isEmpty(list)) {
                // 获取附件信息
                ClothesAttach cah = new ClothesAttach();
                cah.setClothesId(list.get(0).getId());
                acList = attachService.findList(cah);
                store = mStoreService.getById(storeId);
            }

        }

        // 根据条码查询
        if ("2".equals(type)) {
            if (!StringUtils.isEmptyString(barCode)) {

                // 先查询衣服
                StoreClothes clothes = new StoreClothes();
                //clothes.setStatus("12");
                clothes.setBarCode(barCode);
                // 获取衣服信息
                list = storeBusiness.findClothes(clothes);
                if (!CollectionUtils.isEmpty(list)) {
                    // 获取门店信息
                    store = mStoreService.getById(list.get(0).getStoreId());
                    // 获取附件信息
                    if (CollectionUtils.isEmpty(acList)) {
                        // 获取附件信息
                        ClothesAttach cah = new ClothesAttach();
                        cah.setClothesId(list.get(0).getId());
                        acList = attachService.findList(cah);
                        list.get(0).setAttachList(acList);
                    }
                } else {
                    // 根据条码判断是否为附件
                    ClothesAttach ac = new ClothesAttach();
                    ac.setAttachBarCode(barCode);
                    acList = attachService.findList(ac);
                    // clothes.setStatus("12");
                    // 是附件条码，则获取衣服条码
                    if (!CollectionUtils.isEmpty(acList)) {
                        clothes.setBarCode(acList.get(0).getClothesBarCode());
                    }
                    StoreClothes sc = new StoreClothes();
                    sc.setBarCode(barCode);
                    // 获取衣服信息
                    list = storeBusiness.findClothes(clothes);
                    if (!CollectionUtils.isEmpty(list)) {
                        list.get(0).setAttachList(acList);
                    }
                }
            }
        }
        
        StoreOrder orderInfo =orderQueryService.getStoreOrderByCode(list.get(0).getOrderCode());
        // 获取门店信息,同时生成批次号
        ObjectMapper objectMapper = new ObjectMapper();
        String batchNo = DateUtil.getDefaultStrDate() + new Date().getTime();
        try {
            model.asMap().clear();
            model.addAttribute("store", store);
            model.addAttribute("orderInfo", orderInfo);
            model.addAttribute("batchNo", batchNo);
            model.addAttribute("clothesList", list);
            model.addAttribute("resultCode", "1");
            model.addAttribute("resultMsg", "获取数据成功");
            return objectMapper.writeValueAsString(model);

        } catch (Exception e) {
            model.addAttribute("resultCode", "0");
            model.addAttribute("resultMsg", "获取衣服信息失败");
            return JSONObject.toJSONString(model);
        }
    }

    @RequestMapping(value = "/querywatingHandonClothes", method = { RequestMethod.POST })
    @ResponseBody
    public String querywatingHandonClothes(HttpServletRequest req, HttpSession session, Model model) {

        String storeId = req.getParameter("storeId");
        // 1:门店出厂 2：单件出厂 3：o2o订单
        String type = req.getParameter("type");
        String barCode = req.getParameter("barCode");

        List<StoreClothes> list = null;
        EwashingStore store = null;
        
        // 根据门店查询,需要洗涤工序完成，才可以出厂
        if ("1".equals(type)) {
            StoreClothes clothes = new StoreClothes();
            clothes.setStoreId(storeId);
            clothes.setStatus("12");
            list = storeBusiness.findHandonClothes(clothes);

            if (!CollectionUtils.isEmpty(list)) {
                
            	for(StoreClothes sc :list){
            		// 获取附件信息
                    ClothesAttach cah = new ClothesAttach();
                    cah.setClothesId(list.get(0).getId());
                    sc.setAttachList(attachService.findList(cah));
                    sc.setStatus(ClothesStatus.getName(sc.getStatus()));
                    store = mStoreService.getById(storeId);
            	}
            }

        }

        // 根据条码查询
        if ("2".equals(type)) {
            if (!StringUtils.isEmptyString(barCode)) {

                // 先查询衣服
                StoreClothes clothes = new StoreClothes();
                clothes.setStatus("12");
                clothes.setBarCode(barCode);
                // 获取衣服信息
                list = storeBusiness.findHandonClothes(clothes);
                if (!CollectionUtils.isEmpty(list)) {
                    // 获取门店信息
                    store = mStoreService.getById(list.get(0).getStoreId());
                    // 获取附件信息
                    ClothesAttach cah = new ClothesAttach();
                    cah.setClothesId(list.get(0).getId());
                    list.get(0).setAttachList(attachService.findList(cah));

                    for(StoreClothes c:list){
                    	c.setStatus(ClothesStatus.getName(c.getStatus()));
                    }
                    
                }
            }
        }

        // 获取O2O 订单
        if ("3".equals(type)) {

        	list =storeBusiness.queryO2oOutFactoryOrder("2","12");
            if (!CollectionUtils.isEmpty(list)) {
                // 获取附件信息
                ClothesAttach cah = new ClothesAttach();
                cah.setClothesId(list.get(0).getId());
                list.get(0).setAttachList(attachService.findList(cah));
                
                for(StoreClothes c:list){
                	c.setStatus(ClothesStatus.getName(c.getStatus()));
                }
            }
        }

        
        //生成批次号
        ObjectMapper objectMapper = new ObjectMapper();
        String batchNo = DateUtil.getDefaultStrDate() + new Date().getTime();
        try {
            model.asMap().clear();
            model.addAttribute("store", store);
            model.addAttribute("batchNo", batchNo);
            model.addAttribute("clothesList", list);
            model.addAttribute("resultCode", "1");
            model.addAttribute("resultMsg", "获取数据成功");
            return objectMapper.writeValueAsString(model);

        } catch (Exception e) {
            model.addAttribute("resultCode", "0");
            model.addAttribute("resultMsg", "获取衣服信息失败");
            return JSONObject.toJSONString(model);
        }
    }
    
    
    /**
     * 出厂
     * 
     * @param req
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "/doOutFactory", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public String doOutFactory(HttpServletRequest req, HttpSession session, Model model) {

        SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            model.asMap().clear();
            String ids = req.getParameter("ids");
            String batchNo = req.getParameter("batchNo");
            String storeId = req.getParameter("storeId");
            if (ids == null || "".equals(ids)) {
                model.addAttribute("resultCode", "0");
                model.addAttribute("resultMsg", "出厂衣服失败");
                return JSONObject.toJSONString(model);
            }

            if (ids.endsWith(",")) {
                ids = ids.substring(0, ids.length() - 1);
            }

            boolean flag =factoryService.doOutFactory(loginUser, ids, batchNo, storeId);
            if(!flag){
            	 model.addAttribute("resultCode", "0");
                 model.addAttribute("resultMsg", "出厂衣服失败");
                 return JSONObject.toJSONString(model);
            }
            model.addAttribute("resultCode", "1");
            model.addAttribute("resultMsg", "出厂衣服成功");
            return objectMapper.writeValueAsString(model);
        } catch (Exception e) {
            model.addAttribute("resultCode", "0");
            model.addAttribute("resultMsg", "出厂衣服失败");
            return JSONObject.toJSONString(model);
        }
    }

    /**
     * 入厂，修改衣服当前状态. <br>
     * 
     * @param req
     * @param ewashingStoreUser
     * @param session
     * @param model
     * @return <br>
     */
    @RequestMapping(value = "/queryClothesUpdateType", method = { RequestMethod.POST })
    @ResponseBody
    public String queryClothesUpdateType(HttpServletRequest req, EwashingStoreUser ewashingStoreUser, HttpSession session, Model model) {
        SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
        String barCode = req.getParameter("barCode");
        StoreClothes clothes = new StoreClothes();
        clothes.setBarCode(barCode);
        List<StoreClothes> list = storeBusiness.findClothes(clothes);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            model.asMap().clear();
            if (!CollectionUtils.isEmpty(list)) {
                StoreClothes resultClothes = list.get(0);
                StoreOrder orderInfo =orderQueryService.getStoreOrderByCode(resultClothes.getOrderCode());
                if ("2".equals(ClothesStatus.getType(resultClothes.getStatus())) || "2".equals(orderInfo.getOrderType())) {
                    // 当前类型是待入厂,修改类型为3,表示入厂
                    StoreClothes updateClothes = new StoreClothes();
                    updateClothes.setId(resultClothes.getId());
                    updateClothes.setStatus("3");
                    int updateCount =mStoreClothesService.update(updateClothes);
                    if(updateCount<=0){
                    	model.addAttribute("resultCode", "0");
                        model.addAttribute("resultMsg", "操作失败，更新衣服信息失败");
                        return JSONObject.toJSONString(model);
                    }
                    // 记录衣服流水日志
                    int count =clothesFlowService.insertClothesFlow(resultClothes.getOrderId(),resultClothes.getId(), loginUser, "3");
                    if(count<=0){
                    	model.addAttribute("resultCode", "0");
                        model.addAttribute("resultMsg", "操作失败，记录日志信息失败");
                        return JSONObject.toJSONString(model);
                    }
                    
                    // O2O 订单，需要更新小E 分配订单状态
                    if("2".equals(orderInfo.getOrderType())){
                    	updateSendOrder(orderInfo);
                    }
                    
                    resultClothes.setStatus("3");
                    model.addAttribute("storeclothes", resultClothes);
                    model.addAttribute("orderInfo", orderInfo);
                    model.addAttribute("resultCode", "1");
                    model.addAttribute("resultMsg", "获取数据成功");
                    
                    // 获取已入厂件数
                    ClothesFlow f =new ClothesFlow();
                    f.setOrderId(orderInfo.getId());
                    f.setClothesStatus("3");
                    List<ClothesFlow>  li =clothesFlowService.findClothesFlowList(f);
                    if(!CollectionUtils.isEmpty(li)){
                    	model.addAttribute("handon_count", li.size());
                    }
                    // 设置门店信息
                    if(!StringUtils.isEmptyString(orderInfo.getStoreId())){
                    	model.addAttribute("storeInfo", mStoreService.getById(orderInfo.getStoreId()));
                    }
                    return objectMapper.writeValueAsString(model);
                } else {
                    model.addAttribute("resultCode", "0");
                    model.addAttribute("resultMsg", "该衣服当前状态为:"+resultClothes.getStatus()+",不能入厂!");
                }
            } else {
                model.addAttribute("resultCode", "0");
                model.addAttribute("resultMsg", "没有该衣服条码");
            }
        } catch (Exception e) {
            model.addAttribute("resultCode", "0");
            model.addAttribute("resultMsg", "获取衣服信息失败");
            return JSONObject.toJSONString(model);
        }
        return JSONObject.toJSONString(model);
    }

    private void updateSendOrder(StoreOrder orderInfo) {
    	// 是否更新小E表标识
    	boolean flag =false;
    	// 查询是否有未入厂的订单
    	StoreClothes c =new StoreClothes();
    	c.setOrderCode(orderInfo.getOrderCode());
    	List<StoreClothes> list =mStoreClothesService.findList(c);
    	if(!CollectionUtils.isEmpty(list)){
    		// 判断是否有入厂记录
    		for(StoreClothes sc :list){
    			
    			ClothesFlow f =new ClothesFlow();
    			f.setClothesId(sc.getId());
    			f.setClothesStatus("3");
    			if(!CollectionUtils.isEmpty(clothesFlowService.findClothesFlowList(f))){
    				flag =true;
    			}else{
    				flag =false;
    			}
    		}
    	}
    	
    	if(flag){
    		SendReceivePersonOrderRelation r =new SendReceivePersonOrderRelation();
    		r.setOrderId(orderInfo.getId());
    		r.setTaskStatus("3");
    		r.setDistributeType("1"); //取衣状态
    		// 更新小E分配订单状态
    		sendService.updatSendReceivePersonOrder(r);
    		// 更新订单状态为清洗中
    		StoreOrder o =new StoreOrder();
    		o.setId(orderInfo.getId());
    		o.setOrderStatus("22");// 清洗中
    		orderService.update(o);
    		// 记录物流信息,发送模板消息
    		logisticsInfoService.insertLogisticsInfo(orderInfo.getId(), "您的订单已经进入工厂,准备开始清洗！");
    		// 发送模板消息
    		StoreOrder os=orderService.getById(orderInfo.getId());
    		os.setOrderStatus(OrderStatus.getName("22"));
    		//templeteService.sendOrderStatusNoticeForLocal(os, "您的订单已经进入工厂,准备开始清洗！");
    	}
	}

	/**
     * 质检、洗衣服页面跳转. <br>
     * 
     * @param req
     * @param ewashingStoreUser
     * @param session
     * @param model
     * @return <br>
     */
    @RequestMapping(value = "/doWashing", method = { RequestMethod.POST, RequestMethod.GET })
    public String doWashing(String washingType, HttpServletRequest req, EwashingStoreUser ewashingStoreUser, HttpSession session, Model model) {
        model.addAttribute("washType", washingType);
        model.addAttribute("washTypeStr", ClothesStatus.getName(washingType));
        return "ewashing/factory/doWashing";
    }

    /**
     * 质检、洗衣服工序修改状态. <br>
     * 
     * @param req
     * @param ewashingStoreUser
     * @param session
     * @param model
     * @return <br>
     */
    @RequestMapping(value = "/doWashingUpdateType", method = { RequestMethod.POST })
    @ResponseBody
    public String doWashingUpdateType(HttpServletRequest req, EwashingStoreUser ewashingStoreUser, HttpSession session, Model model) {
        SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
        String barCode = req.getParameter("barCode");
        String washType = req.getParameter("washType");
        StoreClothes clothes = new StoreClothes();
        clothes.setBarCode(barCode);
        List<StoreClothes> list = storeBusiness.findClothes(clothes);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            model.asMap().clear();
            if (!CollectionUtils.isEmpty(list)) {
                StoreClothes resultClothes = list.get(0);
                ClothesFlow clothesFlow = new ClothesFlow();
                clothesFlow.setClothesId(resultClothes.getId());
                clothesFlow.setClothesStatus("3");
                List<ClothesFlow> clothesFlowList = clothesFlowService.findClothesFlowList(clothesFlow);
                if (!CollectionUtils.isEmpty(clothesFlowList)) {
                    // 可以查到已经入厂的衣服，代表可以进行洗衣服工序
                    clothesFlow.setClothesId(resultClothes.getId());
                    clothesFlow.setClothesStatus(washType);
                    List<ClothesFlow> clothesFlowCurrentList = clothesFlowService.findClothesFlowList(clothesFlow);
                    if (CollectionUtils.isEmpty(clothesFlowCurrentList)) {
                        StoreClothes updateClothes = new StoreClothes();
                        updateClothes.setId(resultClothes.getId());
                        updateClothes.setStatus(washType);
                        int updateCount =mStoreClothesService.update(updateClothes);
                        if(updateCount<=0){
                        	model.addAttribute("resultCode", "0");
                            model.addAttribute("resultMsg", "操作失败，更新衣服信息失败!");
                            return JSONObject.toJSONString(model);
                        }
                        // 记录衣服流水日志
                        int count =clothesFlowService.insertClothesFlow(resultClothes.getOrderId(),resultClothes.getId(), loginUser, washType);
                        if(count<=0){
                        	model.addAttribute("resultCode", "0");
                            model.addAttribute("resultMsg", "操作失败，记录日志失败!");
                            return JSONObject.toJSONString(model);
                        }
                        resultClothes.setStatus(washType);
                        model.addAttribute("storeclothes", resultClothes);
                        model.addAttribute("resultCode", "1");
                        model.addAttribute("resultMsg", "获取数据成功");
                        return objectMapper.writeValueAsString(model);
                    } else {
                        model.addAttribute("resultCode", "0");
                        model.addAttribute("resultMsg", "该衣服当前工序已经做过!");
                    }
                } else {
                    model.addAttribute("resultCode", "0");
                    model.addAttribute("resultMsg", "该衣服当前状态不对");
                }
            } else {
                model.addAttribute("resultCode", "0");
                model.addAttribute("resultMsg", "没有该衣服条码");
            }
        } catch (Exception e) {
            model.addAttribute("resultCode", "0");
            model.addAttribute("resultMsg", "获取衣服信息失败");
            return JSONObject.toJSONString(model);
        }
        return JSONObject.toJSONString(model);
    }

    /**
     * 出厂. <br>
     * 
     * @param req
     * @param ewashingStoreUser
     * @param session
     * @param model
     * @return <br>
     */
    @RequestMapping(value = "/outFactory", method = { RequestMethod.POST, RequestMethod.GET })
    public String outFactory(HttpServletRequest req, EwashingStoreUser ewashingStoreUser, HttpSession session, Model model) {
        List<EwashingStore> list = mStoreService.findList(new EwashingStore());
        SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
        model.addAttribute("currentUser", loginUser.getRealname());
        model.addAttribute("storeList", list);
        return "ewashing/factory/outFactory";
    }

    /**
     * 拍照. <br>
     * 
     * @param req
     * @param ewashingStoreUser
     * @param session
     * @param model
     * @return <br>
     */
    @RequestMapping(value = "/takePhoto", method = { RequestMethod.POST, RequestMethod.GET })
    public String takePhoto(HttpServletRequest req, EwashingStoreUser ewashingStoreUser, HttpSession session, Model model) {
//    	String tempUploadPath =propertiesUtil.getProperty("tempUploadPath");
//		model.addAttribute("tempUploadPath", tempUploadPath);
//		model.addAttribute("photoFromType", "1");//1：工厂   2：门店
//		return "ewashing/storeBusiness/receiveClothesPhoto";
    	return "ewashing/factory/takePhoto";
    }
    
    /**
     * 去拍照页面. <br>
     * 
     * @param req
     * @param ewashingStoreUser
     * @param session
     * @param model
     * @return <br>
     */
    @RequestMapping(value = "/goTakePhoto", method = { RequestMethod.POST, RequestMethod.GET })
    public String goTakePhoto(HttpServletRequest req, EwashingStoreUser ewashingStoreUser, HttpSession session, Model model) {
    	String tempUploadPath =propertiesUtil.getProperty("tempUploadPath");
    	String uploadPath =propertiesUtil.getProperty("uploadPath");
		model.addAttribute("tempUploadPath", tempUploadPath);
		model.addAttribute("uploadPath", uploadPath);
		model.addAttribute("photoFromType", "1");//1：工厂   2：门店
		model.addAttribute("barCode", req.getParameter("barCode"));//衣服条码
		return "ewashing/storeBusiness/receiveClothesPhoto";
    }

    /**
     * 保存高拍仪拍照图片信息
     * 
     * @param req
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "/saveClothesPhoto", method = { RequestMethod.POST })
    @ResponseBody
    public String saveClothesPhoto(HttpServletRequest req, HttpSession session, Model model) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            model.asMap().clear();
            String clothesId = req.getParameter("clothesId");
            if (StringUtils.isEmptyString(clothesId)) {
                model.addAttribute("resultMsg", "衣服条码不能为空！");
                model.addAttribute("resultCode", 0);
                return JSONObject.toJSONString(model);
            }
            StoreClothes clothes = new StoreClothes();
            clothes.setBarCode(clothesId);
            List<StoreClothes> list = storeBusiness.findClothes(clothes);
            if (CollectionUtils.isEmpty(list)) {
                model.addAttribute("resultCode", "0");
                model.addAttribute("resultMsg", "没有该衣服条码");
                return JSONObject.toJSONString(model);
            }
            
            String fileName = req.getParameter("photoName");
            String filePath = req.getParameter("filePath");
            if (StringUtils.isEmptyString(filePath)) {
                model.addAttribute("resultMsg", "找不到拍照图片！");
                model.addAttribute("resultCode", 0);
                return JSONObject.toJSONString(model);
            }
            String fileUrl = fileUploadService.uploadFile(filePath, fileName);
            StoreClothes resultClothes = list.get(0);
            SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
            Date currentDate = new Date();
            ClothesPhoto clothesPhoto = new ClothesPhoto();
            clothesPhoto.setId(UUID.getUUID32());
            clothesPhoto.setCreateDate(currentDate);
            clothesPhoto.setCreateUserId(loginUser.getId());
            clothesPhoto.setCreateUserName(loginUser.getRealname());
            clothesPhoto.setClothesId(clothesId);
            clothesPhoto.setPhotoPath(fileUrl);
            clothesPhoto.setPhotoName(fileName);
            clothesPhoto.setPhotoType(req.getParameter("photoType"));
            factoryService.insertClothesPhoto(clothesPhoto);
            model.addAttribute("storeClothes", resultClothes);
            model.addAttribute("resultMsg", "保存图片成功！");
            model.addAttribute("resultCode", 1);
            return objectMapper.writeValueAsString(model);
        } catch (Exception e) {
            logger.error(e.getMessage());
            model.addAttribute("resultMsg", "保存图片失败！");
            model.addAttribute("resultCode", 0);
            return JSONObject.toJSONString(model);
        }
    }

    /**
     * 查询衣服结果页面. <br>
     * 
     * @param req
     * @param ewashingStoreUser
     * @param session
     * @param model
     * @return <br>
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "/queryColthesResult", method = { RequestMethod.POST, RequestMethod.GET })
    public String queryColthesResult(HttpServletRequest req, EwashingStoreUser ewashingStoreUser, HttpSession session, Model model)
            throws UnsupportedEncodingException {
        String clothesListStr = req.getParameter("result");
        String orderInfo =req.getParameter("orderInfo");
        if (com.ehyf.ewashing.util.StringUtils.isNotEmptyString(clothesListStr)) {
            // memberName = new String(memberName.getBytes("ISO-8859-1"),"UTF-8");
            clothesListStr = URLDecoder.decode(clothesListStr, "UTF-8");
            orderInfo =URLDecoder.decode(orderInfo, "UTF-8");
        }
        // StoreClothes clothes = JSONObject.parseObject(clothesListStr,StoreClothes.class);
        model.addAttribute("clothesresult", clothesListStr);
        model.addAttribute("orderInfo", orderInfo);
        
        return "ewashing/factory/queryClothes";
    }

    //查询条码对应的订单，该订单中包含的所有衣服的信息返回
    @RequestMapping(value="/queryOrderInfo")
    @ResponseBody
    public String queryOrderInfo(HttpServletRequest req,Model model)
    {
        int i = 0;
        int value = 0;
        BigDecimal price;
        BigDecimal priceTotal = new BigDecimal("0.00");
        String strPrice;
        String status = "";
        String OrderInfo = "";
        // 条码
        String queryKey = req.getParameter("queryKey");
        //queryKey = "2803080";
        //queryKey = "2801176";

        value = queryKey.length();
        if(value==0) return OrderInfo;

        try
        {
            model.asMap().clear();

            //查询本条码的订单号
            StoreClothes clothes = new StoreClothes();
            clothes.setBarCode(queryKey);
            List<StoreClothes> list2 = storeBusiness.findOrderCode(clothes);
            if (CollectionUtils.isEmpty(list2))
            {
                return OrderInfo;
            }
            queryKey = list2.get(0).getOrderCode();

            //查询订单下所有衣服的信息
            clothes.setOrderCode(queryKey);
            List<StoreClothes> list = storeBusiness.findOrderInfo(clothes);
            if (CollectionUtils.isEmpty(list)) {
                return OrderInfo;
            }


            for (i = 0; i < list.size(); i++)
            {
                //衣服状态从数字转成汉字
                status = list.get(i).getStatus();
                value = Integer.valueOf(status);
                status = GetClothesStatus(value);

                list.get(i).setStatus(status);

                //把价格转成String
                price = list.get(i).getPrice();
                //设置小数位数，第一个变量是小数位数，第二个变量是取舍方法(四舍五入)
                price = price.setScale(2, BigDecimal.ROUND_HALF_UP);
                priceTotal = priceTotal.add(price); //总价
                //转化为字符串
                strPrice = price.toString();

                list.get(i).setHandOnArea(strPrice);
            }

            //总价
            priceTotal = priceTotal.setScale(2, BigDecimal.ROUND_HALF_UP);
            strPrice = priceTotal.toString();
            list.get(0).setHandOnNo(strPrice);

            //把数组序列化成字符串
            JSONArray json = JSONArray.fromObject(list);
            OrderInfo = json.toString();
            OrderInfo = JSONArray.fromObject(list).toString();
        }
        catch (Exception e)
        {
            //log.error(e.toString(), e);
        }
        return OrderInfo;
    }

    //得到洗衣的状态，代码转汉字
    private String GetClothesStatus(int value)
    {
        String status = "";

        switch (value)
        {
            case 0:
                status = "已收衣";
                break;
            case 1:
                status = "待送洗";
                break;
            case 2:
                status = "待入厂";
                break;
            case 3:
                status = "已入厂";
                break;
            case 4:
                status = "水洗";
                break;
            case 5:
                status = "干洗";
                break;
            case 6:
                status = "烘干";
                break;
            case 7:
                status = "熨烫";
                break;
            case 8:
                status = "精洗";
                break;
            case 9:
                status = "皮衣";
                break;
            case 10:
                status = "质检";
                break;
            case 11:
                status = "分拣";
                break;
            case 12:
                status = "工厂已上挂";
                break;
            case 13:
                status = "已出厂";
                break;
            case 14:
                status = "已签收 (门店)";
                break;
            case 15:
                status = "门店已上挂";
                break;
            case 16:
                status = "已取衣";
                break;
            default:
                status = "未知状态";
        }

        return status;

    }
    /**
     * 工厂上挂
     * 
     * @param req
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "/factoryHandOn", method = { RequestMethod.POST, RequestMethod.GET })
    public String factoryHandOn(HttpServletRequest req, HttpSession session, Model model) {

        // 获取门店的挂衣区
        String queryKey  =req.getParameter("queryKey");
        // 获取待上挂订单
        StoreOrder order = new StoreOrder();
        order.setOrderCode(queryKey);
        //order.setOrderStatus("4");
        List<StoreOrder> list = orderQueryService.findFactoryHandOnList(order);
        
        model.addAttribute("queryKey", queryKey);
        model.addAttribute("list", list);
        return "ewashing/factory/waitingHandOn";
    }

    /**
     * 工厂上挂
     * @param req
     * @param session
     * @param model
     * @return
     */
	@RequestMapping(value = "/factoryHandOnNew", method = { RequestMethod.POST, RequestMethod.GET })
	public String factoryHandOnNew(HttpServletRequest req, HttpSession session, Model model) {

		SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
		// 条码
		String queryKey = req.getParameter("queryKey");
		if (!StringUtils.isEmptyString(queryKey))
		{
			// 查询是否为衣服条码
			StoreClothes clothes = new StoreClothes();
			clothes.setBarCode(queryKey);
			List<StoreClothes> list = storeBusiness.findClothesCommon(clothes);
			if (CollectionUtils.isEmpty(list))
			{
				ClothesAttach attach = new ClothesAttach();
				attach.setAttachBarCode(queryKey);
				// 查询是否为附件条码
				List<ClothesAttach> atatchList = attachService.findList(attach);
				if (CollectionUtils.isEmpty(atatchList)) {
					model.addAttribute("resultMsg", "上挂失败，找不对应条码！");
					model.addAttribute("resultCode", 0);
				}
				else
				{
					attachBarcode(atatchList.get(0).getClothesBarCode(), model);
				}
			}
			else
			{
				// 判断衣服是否已经入厂，没有入厂，不能上挂
				ClothesFlow flowH =new ClothesFlow();
				flowH.setClothesId(list.get(0).getId());
				flowH.setClothesStatus("3");
				List<ClothesFlow> flowListH =clothesFlowService.findList(flowH);
				if(CollectionUtils.isEmpty(flowListH)){
					model.addAttribute("resultMsg", "该衣服没有入厂，不能上挂");
					model.addAttribute("resultCode", 0);
				}
				else
				{
					clothesBarCode(list.get(0), model, loginUser);
				}
			}
		}
        model.addAttribute("queryKey", queryKey);

		return "ewashing/factory/factoryHandOnNew";
	}

	private void clothesBarCode(StoreClothes storeClothes, Model model, SecurityUser loginUser) {

		// 1：获取订单信息:
		StoreOrder order = orderQueryService.getStoreOrderByCode(storeClothes.getOrderCode());
		//判断是否已经上挂
		ClothesFlow flow =new ClothesFlow();
		flow.setClothesId(storeClothes.getId());
		flow.setClothesStatus("12");
		List<ClothesFlow> flowList =clothesFlowService.findList(flow);
		String status;
        Boolean empty;

        status = storeClothes.getStatus();
        empty = CollectionUtils.isEmpty(flowList);

		if ("12".equals(storeClothes.getStatus()) || !CollectionUtils.isEmpty(flowList)) {
			model.addAttribute("resultMsg", "该衣服已经上挂");
			model.addAttribute("resultCode", 0);
		} else {
			// 获取隔架信息
			String handOnArea = "";
			String handOnNo = "";
			String handType ="";
			// 3：获取已上挂衣服信息
			StoreClothes handOnQuery = new StoreClothes();
			handOnQuery.setStatus("12");
			handOnQuery.setOrderCode(order.getOrderCode());
			List<StoreClothes> handOnList = storeBusiness.findClothesCommon(handOnQuery);

			int updateFlag =0;
			// 已经有上挂的,不需要更新隔架号
			if (!CollectionUtils.isEmpty(handOnList)) {
				// 获取隔架信息
				handOnArea = handOnList.get(0).getFactoryHandOnArea();
				handOnNo = handOnList.get(0).getFactoryHandOnNo();
			} else {
				// 随机选择隔架号
				java.util.Map<String,String> map =romdonChooseHandOn(handOnArea, handOnNo,order);
				handOnArea = map.get("handOnArea");
				handOnNo = map.get("handOnNo");
				handType =map.get("handType");
				updateFlag=1;
			}

			if (StringUtils.isEmptyString(handOnArea) || StringUtils.isEmptyString(handOnNo)) {
				model.addAttribute("resultMsg", "上挂失败，隔架区或者隔架号不存在，请先设置");
				model.addAttribute("resultCode", 0);
			} else {
				// 上挂
				boolean flag = factoryService.doHandOn(handOnArea, handOnNo, storeClothes.getId(), loginUser,updateFlag,order,handType);
				if (!flag) {
					model.addAttribute("resultMsg", "上挂失败");
					model.addAttribute("resultCode", 0);
				}
				order.setHandOnArea(handOnArea);
				order.setHandOnNo(handOnNo);
			}
			
			// 3：获取已上挂衣服信息
			StoreClothes handOnQueryFinal = new StoreClothes();
			handOnQueryFinal.setStatus("12");
			handOnQueryFinal.setOrderCode(order.getOrderCode());
			
			if("2".equals(order.getOrderType())){
				if(WeChatProxy.judgeAppIdType(order.getAppId())==1){
					handOnQueryFinal.setHandType("3");
				}
				
				if(WeChatProxy.judgeAppIdType(order.getAppId())==2){
					handOnQueryFinal.setHandType("4");
				}
			}else{
				handOnQueryFinal.setHandType("2");
			}
			List<StoreClothes> handOnListFinal = storeBusiness.findClothesO2o(handOnQueryFinal);

			if(!CollectionUtils.isEmpty(handOnListFinal)){
				order.setHandOnArea(handOnListFinal.get(0).getFactoryHandOnArea());
				order.setHandOnNo(handOnListFinal.get(0).getFactoryHandOnNo());
			}
			// 设置衣服、订单信息
			model.addAttribute("handOnList", handOnListFinal);
			model.addAttribute("order", order);
			model.addAttribute("handonCount", handOnListFinal.size());
		}
	}

	private java.util.Map<String,String> romdonChooseHandOn(String handOnArea, String handOnNo, StoreOrder order) {
		
		java.util.Map<String,String> map = new HashMap<String,String>(); 
		// 随机获取隔架信息
		HandOnArea area = new HandOnArea();
		// 判断订单类型
		if("2".equals(order.getOrderType())){
			
			// 判断是萨维亚还是浣衣坊
			if(WeChatProxy.judgeAppIdType(order.getAppId())==1){
				area.setHandType("3"); // 萨维亚
			}
			if(WeChatProxy.judgeAppIdType(order.getAppId())==2){
				area.setHandType("4"); // 浣衣坊
			}
		}
		else{
			area.setHandType("2");
		}
		
		List<HandOnArea> areaList = handonArea.findList(area);
		for (HandOnArea ha : areaList) {
			// 隔架区获取可用隔架号
			HandOnNo no = new HandOnNo();
			no.setHandType(area.getHandType());
			no.setHandonArea(ha.getId());
			no.setStatus("0");
			List<HandOnNo> noList = handonNo.findList(no);
			if (CollectionUtils.isEmpty(noList)) {
				continue;
			} else {
				map.put("handType",area.getHandType());
				map.put("handOnArea",ha.getId());
				map.put("handOnNo", noList.get(0).getId());
				break;
			}
		}
		return map;
	}

	private void attachBarcode(String barCode, Model model) {
		// 获取附件对应衣服信息
		StoreClothes sc = new StoreClothes();
		sc.setBarCode(barCode);
		List<StoreClothes> scList = storeBusiness.findClothes(sc);
		if (CollectionUtils.isEmpty(scList)) {
			model.addAttribute("resultMsg", "上挂失败,根据附件条码,找不到衣服信息");
			model.addAttribute("resultCode", 0);
		} else {
			// 订单信息
			StoreOrder order = orderQueryService.getStoreOrderByCode(scList.get(0).getOrderCode());
			order.setOrderStatus(OrderStatus.getName(order.getOrderStatus()));
			// 判断衣服状态，是否工厂已经上挂
			if ("12".equals(scList.get(0).getStatus())) {
				// 设置工厂隔架区，隔架号
				order.setHandOnArea(sc.getFactoryHandOnArea());
				order.setHandOnNo(sc.getFactoryHandOnNo());

				// 获取该订单，所有已上挂衣服信息
				StoreClothes handOnQuery = new StoreClothes();
				handOnQuery.setStatus("12");
				handOnQuery.setOrderCode(order.getOrderCode());
				List<StoreClothes> handOnList = storeBusiness.findClothes(sc);
				model.addAttribute("handOnList", handOnList);
			}
			// 设置订单信息
			model.addAttribute("order", order);
			model.addAttribute("resultMsg", "此条码为附件条码，请先上挂衣服");
			model.addAttribute("resultCode", 1);
		}
	}

	/**
     * 工厂上挂
     * 
     * @param req
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "/confirmHandOn", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public String confirmHandOn(HttpServletRequest req, HttpSession session, Model model) {
        SecurityUser loginUser = (SecurityUser) req.getAttribute(Constants.CURRENT_USER);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            model.asMap().clear();
            String orders = req.getParameter("orders");
            if (orders == null || "".equals(orders)) {
                model.addAttribute("resultCode", "0");
                model.addAttribute("resultMsg", "上挂失败");
                return JSONObject.toJSONString(model);
            }
            if (orders.endsWith(",")) {
                orders = orders.substring(0, orders.length() - 1);
            }
            boolean flag = storeBusiness.confirmFactoryHandOn(orders, loginUser);
            if (!flag) {
                model.addAttribute("resultCode", "0");
                model.addAttribute("resultMsg", "上挂衣服失败");
                return JSONObject.toJSONString(model);
            }
            model.addAttribute("resultCode", "1");
            model.addAttribute("resultMsg", "上挂衣服成功");
            return objectMapper.writeValueAsString(model);

        } catch (Exception e) {
            model.addAttribute("resultCode", "0");
            model.addAttribute("resultMsg", "上挂衣服失败");
            return JSONObject.toJSONString(model);
        }
    }
    
//    /**
//     * 拍照. <br>
//     * 
//     * @param req
//     * @param ewashingStoreUser
//     * @param session
//     * @param model
//     * @return <br>
//     */
//    @RequestMapping(value = "/photoHistory", method = { RequestMethod.POST, RequestMethod.GET })
//    public String photoHistory(HttpServletRequest req, EwashingStoreUser ewashingStoreUser, HttpSession session, Model model) {
//		return "ewashing/factory/photoHistory";
//    }

}

package com.ehyf.ewashing.restful.client;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ehyf.ewashing.emun.OrderStatus;
import com.ehyf.ewashing.emun.PayStatus;
import com.ehyf.ewashing.emun.ServiceItemStatus;
import com.ehyf.ewashing.entity.EwashingDataDictionary;
import com.ehyf.ewashing.entity.FeedBack;
import com.ehyf.ewashing.entity.Member;
import com.ehyf.ewashing.entity.MemberCard;
import com.ehyf.ewashing.entity.OrderLogisticsInfo;
import com.ehyf.ewashing.entity.PrepayOrder;
import com.ehyf.ewashing.entity.ProduceCategory;
import com.ehyf.ewashing.entity.SecurityUser;
import com.ehyf.ewashing.entity.SendReceivePerson;
import com.ehyf.ewashing.entity.StoreClothes;
import com.ehyf.ewashing.entity.StoreOrder;
import com.ehyf.ewashing.service.EwashingDataDictionaryService;
import com.ehyf.ewashing.service.EwashingStoreBusinessService;
import com.ehyf.ewashing.service.EwashingStoreOrderService;
import com.ehyf.ewashing.service.FeedBackService;
import com.ehyf.ewashing.service.MemberCardService;
import com.ehyf.ewashing.service.OrderLogisticsInfoService;
import com.ehyf.ewashing.service.ProduceCategoryService;
import com.ehyf.ewashing.service.SendReceivePersonOrderRelationService;
import com.ehyf.ewashing.service.SendReceivePersonService;
import com.ehyf.ewashing.service.WechatTempleteService;
import com.ehyf.ewashing.util.DateUtil;
import com.ehyf.ewashing.util.LocalThreadUtils;
import com.ehyf.ewashing.util.RandomUtil;
import com.ehyf.ewashing.util.StringUtils;
import com.ehyf.ewashing.util.UUID;
import com.ehyf.ewashing.vo.OrderPayVo;
import com.ehyf.ewashing.vo.SendUserIncomeVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

import net.sf.json.JSONObject;


/**
 * O2O 订单服务
 * 请求URL http://localhost:8080/ewashing/o2o/feedBack
 * 
 * @author shenxiaozhong
 *
 */
@RestController
@RequestMapping("/o2o")
public class OrderRestfulController {

	@Autowired
	private FeedBackService feedBackService;
	
	@Autowired
	private EwashingStoreOrderService  orderService;
	
	@Autowired
	private ProduceCategoryService categoryService;
	
	@Autowired
	private EwashingDataDictionaryService ewashingDataService;
	
	@Autowired
	private SendReceivePersonOrderRelationService sendRelationService;
	
	@Autowired
	private EwashingStoreBusinessService storeBusiness;
	
	@Autowired
	private OrderLogisticsInfoService orderLogService;
	
	@Autowired
	private WechatTempleteService templeteService;
	
	@Autowired
	private MemberCardService cardService;
	
	@Autowired
	SendReceivePersonService sendReceivePersonService;
	
    private static final Logger logger = Logger.getLogger(OrderRestfulController.class);

	/**
	 * O2O用户下单
	 * @param order
	 * @return 0: 失败 1：成功
	 * @throws IOException
	 */
	@RequestMapping(method = RequestMethod.POST,consumes = "application/json", value = "/order")
	public ResultData<Object> placeOrder(@RequestBody StoreOrder  order) throws IOException {

		Date currentDate =new Date();
		ResultData<Object> resultData=new ResultData<Object>();
		if (StringUtils.isEmptyString(order.getMemberId())) {
			resultData.setMsg("会员ID不能为空");
			resultData.setResCode(ResultCode.FAILTURE);
			return resultData;
		}
		if (StringUtils.isEmptyString(order.getOpenId())) {
			resultData.setMsg("openid不能为空");
			resultData.setResCode(ResultCode.FAILTURE);
			return resultData;
		}

		MemberCard memberCard = cardService.queryCardByMemberId(order.getMemberId());
		if (memberCard == null) {
			resultData.setMsg("会员不存在");
			resultData.setResCode(ResultCode.FAILTURE);
			return resultData;
		}
		
		order.setMemberName(memberCard.getMemberName());
		order.setMobilePhone(memberCard.getMobilePhone());
		order.setCardNumber(memberCard.getCardNumber());
		String randNum = String.valueOf(RandomUtil.getRandNum(1,999999));
		order.setOrderCode(DateUtil.formatDate(new Date(), "yyyyMMddHHmmss")+randNum);
		order.setOrderType("2");
		order.setId(UUID.getUUID32());
		order.setOrderStatus("20");
		order.setDistributeStatus("0");// 待分配
		order.setSendDistributeStatus("0");
		order.setPayStatus("0");// 未付款
		order.setValuation("1");// 待计价
		order.setPrintStatus("1");
		order.setCreateDate(currentDate);
		order.setOrderDate(currentDate);
		logger.info("预约下单："+String.valueOf(LocalThreadUtils.get(LocalThreadUtils.CONSTRANT_APP_ID)));
		order.setAppId(String.valueOf(LocalThreadUtils.get(LocalThreadUtils.CONSTRANT_APP_ID)));
		order.setPaidAmount(BigDecimal.ZERO);
		double longitude =order.getLongitude()==null ? 1 : order.getLongitude();
		double latitude =order.getLatitude()==null ? 2 : order.getLatitude();
		
		order.setLongitude(longitude);
		order.setLatitude(latitude);
		GeometryFactory geometryFactory = new GeometryFactory();
	    com.vividsolutions.jts.geom.Point point = geometryFactory.createPoint(new Coordinate(longitude, latitude));
	    order.setLocationString(point.toText());
		int result= orderService.placeOrder(order);
		if (result == 1) {
			resultData.setMsg("请求成功");
			resultData.setResCode(ResultCode.SUCCESS);
			templeteService.sendOrderMesasage(order);
		} else {
			resultData.setMsg("请求失败");
			resultData.setResCode(ResultCode.FAILTURE);
		}
		return resultData;
		
	}
	
	/**
	 * 修改预约时间
	 * @param order
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(method = RequestMethod.POST,consumes = "application/json", value = "/updateApointmentTime")
	public ResultData<Object> updateApointmentTime(@RequestBody StoreOrder  order) throws IOException {

		ResultData<Object> resultData=new ResultData<Object>();
		if (StringUtils.isEmptyString(order.getId())) {
			resultData.setMsg("订单ID不能为空");
			resultData.setResCode(ResultCode.FAILTURE);
			return resultData;
		}

		int result = orderService.update(order);
		if (result == 1) {
			resultData.setMsg("修改预约时间成功");
			resultData.setResCode(ResultCode.SUCCESS);
		} else {
			resultData.setMsg("修改预约时间失败");
			resultData.setResCode(ResultCode.FAILTURE);
		}
		return resultData;
		
	}
	
	/**
	 * 会员查询我的订单
	 * @param mobile 手机号
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/memberOrder/{mobile}/{pageNo}/{pageSize}")
	public ResultData<Object> queryMemberOrders(@PathVariable(value = "mobile") String mobile,
			@PathVariable(value = "pageNo") String pageNo,
			@PathVariable(value = "pageSize") String pageSize) throws IOException {
		
		ResultData<Object> resultData = new ResultData<Object>();
		if (StringUtils.isEmptyString(mobile)) {
			resultData.setMsg("手机号不能为空");
			resultData.setResCode(ResultCode.FAILTURE);
			return resultData;
		}

		if (StringUtils.isEmptyString(pageNo)) {
			pageNo = "1";
		}
		if (StringUtils.isEmptyString(pageSize)) {
			pageSize = "10";
		}

		PageHelper.startPage(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
		StoreOrder orderQuery = new StoreOrder();
		orderQuery.setOrderType("2");
		orderQuery.setMobilePhone(mobile);
		
		String currentAppId =String.valueOf(LocalThreadUtils.get(LocalThreadUtils.CONSTRANT_APP_ID));
		orderQuery.setAppId(currentAppId);
		
		List<StoreOrder> list = orderService.findO2oOrderList(orderQuery);
		PageInfo<StoreOrder> page = new PageInfo<StoreOrder>(list);

		resultData.setMsg("请求成功");
		resultData.setResCode(ResultCode.SUCCESS);
		resultData.setObj(page.getList());
		return resultData;
    }
	
	@RequestMapping(method = RequestMethod.GET, value = "/queryFeedBacks/{type}/{pageNo}/{pageSize}")
	public ResultData<Object> queryFeedBacks(@PathVariable(value = "type") String type,@PathVariable(value = "pageNo") String pageNo,
			@PathVariable(value = "pageSize") String pageSize) throws IOException {
		
		ResultData<Object> resultData=new ResultData<Object>();
		
		if (StringUtils.isEmptyString(pageNo)) {
			pageNo = "1";
		}
		if (StringUtils.isEmptyString(pageSize)) {
			pageSize = "10";
		}

		PageHelper.startPage(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
		FeedBack feed = new FeedBack();
		feed.setType(type);
		List<FeedBack> list = feedBackService.findList(feed);
		
		PageInfo<FeedBack> page = new PageInfo<FeedBack>(list);

		resultData.setMsg("请求成功");
		resultData.setResCode(ResultCode.SUCCESS);
		resultData.setObj(page.getList());
		return resultData;
    }
	@RequestMapping(method = RequestMethod.GET, value = "/sendReceiveOrder/{sendReceiveUserId}/{distributeType}/{taskStatus}/{pageNo}/{pageSize}/{appId}")
	public ResultData<Object> sendReceiveOrder(@PathVariable(value = "sendReceiveUserId") String sendReceiveUserId,
			@PathVariable(value = "distributeType") String distributeType,
			@PathVariable(value = "taskStatus") String taskStatus,
			@PathVariable(value = "pageNo") String pageNo, 
			@PathVariable(value = "pageSize") String pageSize,
			@PathVariable(value = "appId") String appId)
			throws IOException {
		
		ResultData<Object> resultData=new ResultData<Object>();
		if(StringUtils.isEmptyString(sendReceiveUserId)){
			resultData.setMsg("小E不能为空");
			resultData.setResCode(ResultCode.FAILTURE);
			return resultData;
		}
		
		if (StringUtils.isEmptyString(pageNo)) {
			pageNo = "1";
		}
		if (StringUtils.isEmptyString(pageSize)) {
			pageSize = "10";
		}
		String orderType ="2";
		List<StoreOrder> list;
		int start =Integer.valueOf(pageSize)*(Integer.valueOf(pageNo)-1);
		int limit=10;
		try {
			list = orderService.sendReceiveOrder(orderType,distributeType,sendReceiveUserId,taskStatus,appId,start,limit);
			if(!CollectionUtils.isEmpty(list)){
				for(StoreOrder o :list){
					o.setServiceItem(ServiceItemStatus.getName(o.getServiceItem()));
				}
			}

			resultData.setMsg("请求成功");
			resultData.setResCode(ResultCode.SUCCESS);

			resultData.setObj(list);
			return resultData;
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		return resultData;
		
    }
	@RequestMapping(method = RequestMethod.POST, value = "/member")
    public Member queryMemberByMobile(@RequestParam(value="mobile",required=false) String mobile) throws IOException {
		
		
		return null;
    }
	@RequestMapping(method = RequestMethod.POST,consumes = "application/json", value = "/feedBack")
	public ResultData<Object>  doFeedBack(@RequestBody FeedBack  feedBack) throws IOException {
		
		ResultData<Object> resultData=new ResultData<Object>();
		feedBack.setId(UUID.getUUID32());
		feedBack.setFeedBackDate(new Date());
		int result= feedBackService.insert(feedBack);
		if(result==1){
			resultData.setMsg("意见反馈成功");
			resultData.setResCode(ResultCode.SUCCESS);
		}
		else {
			resultData.setMsg("意见反馈失败");
			resultData.setResCode(ResultCode.FAILTURE);
		}
		return resultData;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/sealNumber/{orderId}/{sealNumber}")
	public ResultData<Object> sealNumber(@PathVariable(value = "orderId") String orderId,
			@PathVariable(value = "sealNumber") String sealNumber) throws IOException {
		
		ResultData<Object> resultData=new ResultData<Object>();
		
		int result=  orderService.updateOrderSealNumberById(orderId,sealNumber);
		if(result==1){
			resultData.setMsg("回写封签号成功");
			resultData.setResCode(ResultCode.SUCCESS);
		}
		else {
			resultData.setMsg("封签号已存在");
			resultData.setResCode(ResultCode.FAILTURE);
		}
		return resultData;
	}
	@RequestMapping(method = RequestMethod.POST, consumes = "application/json",value = "/pay")
	public ResultData<Object> doPay(@RequestBody OrderPayVo  pay){
		
		logger.info("会员支付--会员:"+pay.getMemberId()+",手机号："+pay.getMobilePhone()+",支付类型："+pay.getPayType());
		ResultData<Object> resultData=new ResultData<Object>();
		BigDecimal orderAmount =pay.getOrderAmount();
		BigDecimal freight =pay.getFreight();
		String orderId =pay.getOrderId();
		if(orderAmount==null || BigDecimal.ZERO==orderAmount || orderAmount.compareTo(BigDecimal.ZERO)==-1){
			resultData.setMsg("订单金额不能为空或者为 负数");
			resultData.setResCode(ResultCode.FAILTURE);
			return resultData;
		}
		
		if(freight==null || freight.compareTo(BigDecimal.ZERO)==-1){
			resultData.setMsg("运费不能为负数");
			resultData.setResCode(ResultCode.FAILTURE);
			return resultData;
		}
		
		if(StringUtils.isEmptyString(pay.getMemberId())){
			resultData.setMsg("会员信息不能为空");
			resultData.setResCode(ResultCode.FAILTURE);
			return resultData;
		}
		
		if(StringUtils.isEmptyString(orderId)){
			resultData.setMsg("订单ID不能为空");
			resultData.setResCode(ResultCode.FAILTURE);
			return resultData;
		}
		
		MemberCard memberCard =cardService.queryCardByMemberId(pay.getMemberId());
		if(memberCard==null){
			resultData.setMsg("会员不存在");
			resultData.setResCode(ResultCode.FAILTURE);
			return resultData;
		}
		
		
		StoreOrder order =orderService.getById(orderId);
		if(order==null){
			resultData.setMsg("订单不存在");
			resultData.setResCode(ResultCode.FAILTURE);
			return resultData;
		}
		
		if(!"2".equals(order.getValuation())){
			resultData.setMsg("订单未计价，不能支付");
			resultData.setResCode(ResultCode.FAILTURE);
			return resultData;
		}
		
		BigDecimal receivableAmount =order.getReceivableAmount();
		BigDecimal paidAmount =pay.getOrderAmount().add(pay.getFreight());
		if(paidAmount.compareTo(receivableAmount)!=0){
			resultData.setMsg("支付失败,应付金额与实付金额不");
			resultData.setResCode(ResultCode.FAILTURE);
			return resultData;
		}
		
		SecurityUser loginUser =new SecurityUser();
		loginUser.setId(memberCard.getMemberId());
		loginUser.setUsername(memberCard.getMemberName());
		boolean result;
		try {
			result = storeBusiness.o2oPayment(orderId,orderAmount,freight,pay.getMemberId(),pay.getPayType(),pay.getMobilePhone(),loginUser);
			if(result){
				resultData.setMsg("支付成功");
				resultData.setResCode(ResultCode.SUCCESS);
				logger.info("会员支付成功:"+pay.getMemberId()+",手机号："+pay.getMobilePhone()+",支付类型："+pay.getPayType());
			}
			else {
				resultData.setMsg("支付失败");
				resultData.setResCode(ResultCode.FAILTURE);
			}
		} catch (Exception e) {
			resultData.setMsg("支付失败");
			resultData.setResCode(ResultCode.FAILTURE);
			logger.info("会员支付失败:"+pay.getMemberId()+",手机号："+pay.getMobilePhone()+",支付类型："+pay.getPayType()+","+e.getMessage());
		}
		return resultData;
	}
	@RequestMapping(method = RequestMethod.POST, value = "/valuation")
	public ResultData<Object> valuation(@RequestParam(value = "orderId",required=false) String orderId,
			@RequestParam(value = "orderAmount",required=false) BigDecimal orderAmount,
			@RequestParam(value = "freight",required=false) BigDecimal freight) throws IOException {

		ResultData<Object> resultData=new ResultData<Object>();
		
		if(orderAmount==null || BigDecimal.ZERO==orderAmount || orderAmount.compareTo(BigDecimal.ZERO)==-1){
			resultData.setMsg("订单金额不能为空或者为 负数");
			resultData.setResCode(ResultCode.FAILTURE);
			return resultData;
		}
		
		if(freight!=null && freight.compareTo(BigDecimal.ZERO)==-1){
			resultData.setMsg("运费不能小于0");
			resultData.setResCode(ResultCode.FAILTURE);
			return resultData;
		}
		
		int result =orderService.updateStoreOrderAmount(orderId,orderAmount,freight);
		if(result <=0){
			resultData.setMsg("请求服务失败 ");
			resultData.setResCode(ResultCode.FAILTURE);
		}
		else {
			resultData.setMsg("请求服务成功 ");
			resultData.setResCode(ResultCode.SUCCESS);
			StoreOrder orderInfo =orderService.getById(orderId);
			orderInfo.setOrderStatus(OrderStatus.getName(orderInfo.getOrderStatus()));
			templeteService.sendOrderStatusNoticeForLocal(orderInfo,"订单已经计价，请去支付！");
		}
		return resultData;
	}
	@RequestMapping(method = RequestMethod.GET, value = "/income/{sendUserId}/{userType}")
	public ResultData<Object> income(@PathVariable(value = "sendUserId") String sendUserId,@PathVariable(value = "userType") String userType
			) throws IOException {
		
		ResultData<Object> resultData=new ResultData<Object>();
		
		if(!StringUtils.isNotEmptyString(sendUserId)){
			resultData.setMsg("小EID不能为空");
			resultData.setResCode(ResultCode.FAILTURE);
			return resultData;
		}
		SendUserIncomeVo vo =sendRelationService.queryIncome(sendUserId,userType);
		resultData.setMsg("请求服务成功 ");
		resultData.setResCode(ResultCode.SUCCESS);
		resultData.setObj(vo);
		return resultData;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/answer")
	public ResultData<Object> answer(@RequestParam(value = "orderId",required=false) String orderId,
			@RequestParam(value = "answerType",required=false) String answerType,
			@RequestParam(value = "distributeType",required=false) String distributeType) throws IOException {
		
		ResultData<Object> resultData=new ResultData<Object>();
		boolean result =sendRelationService.updatSendReceivePersonOrderRelation(orderId,answerType,distributeType);
		if(!result){
			resultData.setMsg("请求服务失败 ");
			resultData.setResCode(ResultCode.FAILTURE);
		}
		else {
			resultData.setMsg("请求服务成功 ");
			resultData.setResCode(ResultCode.SUCCESS);
		}
		return resultData;
	}
	@RequestMapping(method = RequestMethod.POST, value = "/sendSinNotice/{orderId}")
	public ResultData<Object> sendSinNotice(@PathVariable(value = "orderId") String orderId) throws IOException {
		
		ResultData<Object> resultData=new ResultData<Object>();
		
		boolean flag =templeteService.sendOrderStatusNotice(orderService.getById(orderId), "您的订单已经送达，请您签收！");
		if(!flag){
			resultData.setMsg("请求服务失败 ");
			resultData.setResCode(ResultCode.FAILTURE);
		}
		else {
			resultData.setMsg("请求服务成功 ");
			resultData.setResCode(ResultCode.SUCCESS);
		}
		return resultData;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/signOrder/{orderId}")
	public ResultData<Object> signOrder(@PathVariable(value = "orderId") String orderId) throws IOException {
		

		ResultData<Object> resultData=new ResultData<Object>();
		int result =orderService.updateOrderAndSendUserStatus(orderId);
		if(result<=0){
			resultData.setMsg("请求服务失败 ");
			resultData.setResCode(ResultCode.FAILTURE);
		}
		else {
			if (result == 2) {
				resultData.setMsg("该订单已经签收 ");
				resultData.setResCode(ResultCode.FAILTURE);
			} else {
				resultData.setMsg("请求服务成功 ");
				resultData.setResCode(ResultCode.SUCCESS);
			}
		}
		return resultData;
	}
	@RequestMapping(method = RequestMethod.GET,value = "/produceCategory")
	public ResultData<Object> produceCategory(@RequestParam(value = "level",required=false) String level,
			@RequestParam(value = "categoryId",required=false) String categoryId,@RequestParam(value = "queryKey",required=false) String queryKey) throws IOException {
		
		ResultData<Object> resultData=new ResultData<Object>();
		
		if(StringUtils.isEmptyString(level)){
			resultData.setMsg("level 不能为空 ");
			resultData.setResCode(ResultCode.FAILTURE);
			return resultData;
		}
		if("1".equals(level)){
			List<ProduceCategory>  list =categoryService.getRoot();
			resultData.setMsg("查询成功 ");
			resultData.setResCode(ResultCode.SUCCESS);
			resultData.setObj(list);
			return resultData;
		}
		if("2".equals(level)){
			List<ProduceCategory>  list =categoryService.getChildren(categoryId, null);
			resultData.setMsg("查询成功 ");
			resultData.setResCode(ResultCode.SUCCESS);
			resultData.setObj(list);
			return resultData;
		}
		if("3".equals(level)){
			List<EwashingDataDictionary>  list =ewashingDataService.getDataDictionaryByCategoryId(categoryId,queryKey);
			resultData.setMsg("查询成功 ");
			resultData.setResCode(ResultCode.SUCCESS);
			resultData.setObj(list);
			return resultData;
		}
		return resultData;
	}
	
	
	@RequestMapping(method = RequestMethod.GET, value = "/orderDetail/{orderId}")
	public ResultData<Object> orderDetail(@PathVariable(value = "orderId") String orderId) throws IOException {
		
		ResultData<Object> resultData=new ResultData<Object>();
		StoreOrder order =orderService.getById(orderId);
		order.setOrderStatus(OrderStatus.getName(order.getOrderStatus()));
		order.setPayStatus(PayStatus.getName(order.getPayStatus()));
		List<OrderLogisticsInfo> logInoList =orderLogService.getLogisticsInfoByOrderId(orderId);
		
		StoreClothes clothes =new StoreClothes();
		clothes.setOrderCode(order.getOrderCode());
		order.setClothesList(storeBusiness.findClothes(clothes));
		order.setLogistics(logInoList);
		resultData.setMsg("查询成功 ");
		resultData.setResCode(ResultCode.SUCCESS);
		resultData.setObj(order);
		return resultData;
	}
	@RequestMapping(method = RequestMethod.GET, value = "/showDetail/{orderId}")
	public ResultData<Object> showDetail(@PathVariable(value = "orderId") String orderId) throws IOException {
		
		ResultData<Object> resultData=new ResultData<Object>();
		StoreOrder order =orderService.getById(orderId);
		order.setOrderStatus(OrderStatus.getName(order.getOrderStatus()));
		order.setPayStatus(PayStatus.getName(order.getPayStatus()));
		List<OrderLogisticsInfo> logInoList =orderLogService.getLogisticsInfoByOrderId(orderId);
		
		StoreClothes clothes =new StoreClothes();
		clothes.setOrderCode(order.getOrderCode());
		order.setClothesList(storeBusiness.findClothes(clothes));
		order.setLogistics(logInoList);
		resultData.setMsg("查询成功 ");
		resultData.setResCode(ResultCode.SUCCESS);
		resultData.setObj(order);
		return resultData;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/cancalOrder/{orderId}")
	public ResultData<Object> cancalOrder(@PathVariable(value = "orderId") String orderId) throws IOException {
		
		ResultData<Object> resultData=new ResultData<Object>();
		StoreOrder order =new StoreOrder();
		order.setId(orderId);
		order.setOrderStatus("30");
		
		StoreOrder orderInfo =orderService.getById(orderId);
		if("1".equals(orderInfo.getPayStatus())){
			resultData.setMsg("已经支付订单，不能取消");
			resultData.setResCode(ResultCode.FAILTURE);
			return resultData;
		}
		
		int result=  orderService.update(order);
		if(result==1){
			resultData.setMsg("取消订单成功");
			resultData.setResCode(ResultCode.SUCCESS);
		}
		else {
			resultData.setMsg("去掉订单失败");
			resultData.setResCode(ResultCode.FAILTURE);
		}
		return resultData;
	}
	
	
	@RequestMapping(method = RequestMethod.POST, consumes = "application/json", value = "/reportGps")
	public ResultData<Object> reportGps(@RequestBody SendReceivePerson person) throws IOException {
		
		ResultData<Object> resultData=new ResultData<Object>();
		
		int result =sendReceivePersonService.update(person);
		
		if(result<1){
			resultData.setMsg("上报GPS坐标成功");
			resultData.setResCode(ResultCode.SUCCESS);
		}
		else {
			resultData.setMsg("上报GPS坐标失败");
			resultData.setResCode(ResultCode.FAILTURE);
		}
		return resultData;
	}
	@RequestMapping(method = RequestMethod.POST, consumes = "application/json", value = "/prepayOrder")
	public ResultData<Object> prepayOrder(@RequestBody PrepayOrder order) {

		ResultData<Object> resultData = new ResultData<Object>();
		StoreOrder orderInfo =null;
		try {
			
			if(order.getOrderAmount()==null || order.getOrderAmount().compareTo(BigDecimal.ZERO)==-1 || order.getOrderAmount().compareTo(BigDecimal.ZERO)==0){
				resultData.setMsg("支付金额不能小于等于0");
				resultData.setResCode(ResultCode.FAILTURE);
				return resultData;
			}
			
			if("1".equals(order.getType())){
				orderInfo =orderService.getById(order.getOrderId());
				if(orderInfo==null){
					resultData.setMsg("订单不存在");
					resultData.setResCode(ResultCode.FAILTURE);
					return resultData;
				}
				
				order.setOrderCode(orderInfo.getOrderCode());
				order.setMemberId(orderInfo.getMemberId());
			}
			
			
			if(StringUtils.isEmptyString(order.getIpAddress())){
				resultData.setMsg("客户端IP不能为空");
				resultData.setResCode(ResultCode.FAILTURE);
				return resultData;
			}
			
			if(StringUtils.isEmptyString(order.getOpenId())){
				resultData.setMsg("openId 不能为空");
				resultData.setResCode(ResultCode.FAILTURE);
				return resultData;
			}
			order.setStatus("1");
			JSONObject result = orderService.prepayOrder(order);
			if(result ==null){
				resultData.setMsg("请求服务失败，appid 为空");
				resultData.setResCode(ResultCode.FAILTURE);
			}
			else if ("SUCCESS".equals(result.getString("return_code"))) {
				resultData.setMsg("调用服务成功，生成预付订单");
				resultData.setResCode(ResultCode.SUCCESS);
				JSONObject obj =new JSONObject();
				generateObj(obj,result);
				resultData.setObj(obj);
				logger.info("返回给O2O 结果:"+obj.toString());
				
			} else {
				resultData.setMsg(result.getString("return_msg"));
				resultData.setResCode(ResultCode.FAILTURE);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultData;

	}

	private void generateObj(JSONObject obj, JSONObject result) {
		obj.put("appId", result.getString("appid"));
		obj.put("timeStamp", result.getString("timestamp"));
		obj.put("nonceStr", result.getString("noncestr"));
		obj.put("package", ""+"prepay_id="+result.getString("prepay_id")+"");
		obj.put("signType", "MD5");
		obj.put("paySign", result.getString("sign"));
	}
}

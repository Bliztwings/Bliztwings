package com.ehyf.ewashing.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 门店订单
 * 
 * @author chenxiaozhong
 * 
 */
public class StoreOrder extends BaseEntity<StoreOrder> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4844072031971871847L;

	/**
	 * 订单ID
	 */
	private String id;

	/**
	 * 门店id
	 */
	private String storeId;

	/**
	 * 门店名称（冗余）
	 */
	private String storeName;

	/**
	 * 订单号
	 */
	private String orderCode;

	/**
	 * 会员编号
	 */
	private String cardNumber;

	/**
	 * 会员名称
	 */
	private String memberName;

	/**
	 * 会员ID
	 */
	private String memberId;

	/**
	 * 移动电话
	 */
	private String mobilePhone;

	/**
	 * 座机
	 */
	private String telephone;

	/**
	 * 订单状态 0：已收衣待送洗 1：已送洗待洗护 2:洗护中 3:洗护完成  4:工厂待上挂  5:工厂已上挂(待出厂) 6:已出厂  7：门店待上挂 8：已上挂待取衣 9：完成
	 * 20：取件中 21：送往工厂 22：清洗中 23：送回中 24：已签收     30 ：已取消
	 */
	private String orderStatus;

	/**
	 * 0:门店订单 2:o2o 订单
	 */
	private String orderType;

	/**
	 * 订单日期
	 */
	private Date orderDate;

	/**
	 * 付款状态 0:未付款 1：已付款 2:洗后付款
	 */
	private String payStatus;

	/**
	 * 1：现金支付 2：会员卡支付 3:微信支付
	 */
	private String payGateWay;

	/**
	 * 付款日期
	 */
	private Date payDate;

	/**
	 * 衣服数量
	 */
	private int clothesCount;

	/**
	 *  已上挂数量
	 */
	private int handOnCount;
	
	/**
	 * 挂衣区
	 */
	private String handOnArea;
	
	/**
	 * 挂衣号
	 */
	private String handOnNo;
	
	/**
	 * 订单总金额
	 */
	private BigDecimal receivableAmount;
	

	/**
	 * 已付款金额
	 */
	private BigDecimal paidAmount;

	/**
	 * 创建时间
	 */
	private Date createDate;

	/**
	 * 创建人
	 */
	private String createUserName;

	/**
	 * 更新时间
	 */
	private Date updateDate;

	/**
	 * 更新人
	 */
	private String updateUserName;

	/**
	 * 创建人ID
	 */
	private String createUserId;
	/**
	 * 修改人ID
	 */
	private String updateUserId;
	
	
	/************************************* o2o 信息 ***************************************************/
	
	/**
	 * 订单地址
	 */
	private String orderAddress;
	
	/**
	 * 取件日期
	 */
	private Date placeOrderDate;
	
	/**
	 * 取件时间段
	 */
	private String placeOrderDateArea;
	
	/**
	 * 用户留言内容
	 */
	private String leaveMessage;
	
	/**
	 *  o2o 订单服务项目1：洗衣 2：洗鞋 3：家居家纺 4：皮具洗护
	 */
	private String serviceItem;
	
	/**
	 * 送衣-分配状态 默认  0：未分配 2：已分配
	 * 
	 */
	private String sendDistributeStatus;
	
	
	/**
	 * 取衣-分配状态 默认 0：未分配 2：已分配
	 * 
	 */
	private String distributeStatus;
	
	/**
	 * 经度
	 */
	private Double longitude;
	/**
	 * 纬度
	 */
	private Double latitude;

	/**
	 * 封签号
	 */
	private String sealNumber;
	
	/**
	 * 地理坐标字符串
	 */
	private String locationString;
	
	/**
	 * 洗衣费用
	 */
	private BigDecimal clothesAmount;
	
	/**
	 * 运费
	 */
	private BigDecimal freight;
	
	/**
	 * 物流信息列表
	 */
	private List<OrderLogisticsInfo> logistics;
	
	/**
	 * 订单衣服信息
	 */
	private List<StoreClothes> clothesList;
	
	/**
	 * 小E 分配的任务状态图
	 */
	private String taskStatus ;
	
	/**
	 * 微信openid
	 */
	private String openId;
	
	/**
	 * 是否计价 1：未计价 2：已计价
	 */
	private String valuation;
	
	/**
	 * 1:已分配  2：已接受 3：已拒绝
	 */
	private String acceptStatus;
	
	/**
	 * 微信公众号ID
	 */
	private String appId;
	
	/**
	 * 1:未打印  2：已打印
	 */
	private String printStatus;
	
	
	
	public String getPrintStatus() {
		return printStatus;
	}

	public void setPrintStatus(String printStatus) {
		this.printStatus = printStatus;
	}

	public String getSendDistributeStatus() {
		return sendDistributeStatus;
	}

	public void setSendDistributeStatus(String sendDistributeStatus) {
		this.sendDistributeStatus = sendDistributeStatus;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAcceptStatus() {
		return acceptStatus;
	}

	public void setAcceptStatus(String acceptStatus) {
		this.acceptStatus = acceptStatus;
	}

	public String getValuation() {
		return valuation;
	}

	public void setValuation(String valuation) {
		this.valuation = valuation;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

	public List<OrderLogisticsInfo> getLogistics() {
		return logistics;
	}

	public void setLogistics(List<OrderLogisticsInfo> logistics) {
		this.logistics = logistics;
	}

	public String getLocationString() {
		return locationString;
	}

	public void setLocationString(String locationString) {
		this.locationString = locationString;
	}

	public String getDistributeStatus() {
		return distributeStatus;
	}

	public void setDistributeStatus(String distributeStatus) {
		this.distributeStatus = distributeStatus;
	}

	public String getLeaveMessage() {
		return leaveMessage;
	}

	public void setLeaveMessage(String leaveMessage) {
		this.leaveMessage = leaveMessage;
	}

	

	public String getPlaceOrderDateArea() {
		return placeOrderDateArea;
	}

	public void setPlaceOrderDateArea(String placeOrderDateArea) {
		this.placeOrderDateArea = placeOrderDateArea;
	}

	public String getOrderAddress() {
		return orderAddress;
	}

	public void setOrderAddress(String orderAddress) {
		this.orderAddress = orderAddress;
	}

	public Date getPlaceOrderDate() {
		return placeOrderDate;
	}

	public void setPlaceOrderDate(Date placeOrderDate) {
		this.placeOrderDate = placeOrderDate;
	}

	

	public String getServiceItem() {
		return serviceItem;
	}

	public void setServiceItem(String serviceItem) {
		this.serviceItem = serviceItem;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	public int getClothesCount() {
		return clothesCount;
	}

	public void setClothesCount(int clothesCount) {
		this.clothesCount = clothesCount;
	}

	public BigDecimal getReceivableAmount() {
		return receivableAmount;
	}

	public void setReceivableAmount(BigDecimal receivableAmount) {
		this.receivableAmount = receivableAmount;
	}

	public BigDecimal getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getUpdateUserName() {
		return updateUserName;
	}

	public void setUpdateUserName(String updateUserName) {
		this.updateUserName = updateUserName;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public Date getPayDate() {
		return payDate;
	}

	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getPayGateWay() {
		return payGateWay;
	}

	public void setPayGateWay(String payGateWay) {
		this.payGateWay = payGateWay;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public String getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}

	public int getHandOnCount() {
		return handOnCount;
	}

	public void setHandOnCount(int handOnCount) {
		this.handOnCount = handOnCount;
	}

	public String getHandOnArea() {
		return handOnArea;
	}

	public void setHandOnArea(String handOnArea) {
		this.handOnArea = handOnArea;
	}

	public String getHandOnNo() {
		return handOnNo;
	}

	public void setHandOnNo(String handOnNo) {
		this.handOnNo = handOnNo;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public String getSealNumber() {
		return sealNumber;
	}

	public void setSealNumber(String sealNumber) {
		this.sealNumber = sealNumber;
	}

	public BigDecimal getFreight() {
		return freight;
	}

	public void setFreight(BigDecimal freight) {
		this.freight = freight;
	}

	public BigDecimal getClothesAmount() {
		return clothesAmount;
	}

	public void setClothesAmount(BigDecimal clothesAmount) {
		this.clothesAmount = clothesAmount;
	}

	public List<StoreClothes> getClothesList() {
		return clothesList;
	}

	public void setClothesList(List<StoreClothes> clothesList) {
		this.clothesList = clothesList;
	}
	
}

package com.ehyf.ewashing.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 门店衣服
 * 
 * @author shenxiaozhong
 *
 */
public class StoreClothes extends BaseEntity<StoreClothes> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2080408987087374500L;

	/**
	 * 主键ID
	 */
	private String id;

	/**
	 * 用户姓名
	 */
	private String userName;

	/**
	 * 门店编码
	 */
	private String storeId;

	/**
	 * 门店名称(冗余字段)
	 */
	private String storeName;

	/**
	 * 订单ID
	 */
	private String orderId;
	
	/**
	 * 订单编号
	 */
	private String orderCode;
	
	/**
	 * 付款状态
	 */
	private String payStatus;
	
	/**
	 * 衣服数量
	 */
	private int clothesCount;

	/**
	 * 衣服条码
	 */
	private String barCode;

	
	/**
	 * 紧急程度
	 */
	private String urgency;

	/**
	 * 会员ID
	 */
	private String memberId;
	
	/**
	 * 会员名称
	 */
	private String memberName;


	/**
	 * 会员卡号
	 */
	private String cardNumber;

	/**
	 * 衣服名称
	 */
	private String clothesName;

	/**
	 * 服务类型  1:单烫 2：普洗 3：尚品洗涤
	 */
	private String serviceType;

	/**
	 * 衣服颜色
	 */
	private String color;

	/**
	 * 品牌
	 */
	private String brand;

	/**
	 * 附件（废弃）
	 */
	private String attachment;

	/**
	 * 移动电话
	 */
	private String mobilePhone;

	/**
	 * 座机
	 */
	private String telephone;

	/**
	 * 瑕疵
	 */
	private String flaw;

	/**
	 * 单价
	 */
	private BigDecimal price;

	/**
	 * 服务费
	 */
	private BigDecimal serviceFee;

	/**
	 * 衣物状态 0:已收衣 1:待送洗 2：待入厂 3:已入厂  4：水洗 5：干洗 6：烘干7：熨烫 8：精洗 9：皮衣 10：质检 11：分拣 12:工厂已上挂
	 * 13：已出厂 14：已签收 (门店) 15：门店已上挂 16已取衣
	 * 洗衣工序:30:质检，31:水洗，32:干洗，33:皮衣，34:精洗，35:熨烫，36:去渍，37:烘干，38:去毛，39:织补，40:鞋类，41:工服洗护
	 */
	private String status;

	/**
	 * 取衣时间
	 */
	private Date takingDate;

	/**
	 * 创建时间\送洗时间
	 */
	private Date createDate;
	/**
	 * 创建人
	 */
	private String createUserName;
	
	/**
	 * 创建人ID
	 */
	private String createUserId;
	/**
	 * 修改人ID
	 */
	private String updateUserId;
	

	/**
	 * 更新时间
	 */
	private Date updateDate;

	/**
	 * 更新人
	 */
	private String updateUserName;
	
	/**
	 * 查询条件 手机号或者会员卡号，需要 一起查询
	 */
	private String queryKey;
	
	/**
	 * 送洗单号
	 */
	private String sendNumber;
	
	
	/**
	 * 送洗时间字符串
	 */
	private String sendDateStr;
	
	
	/**
	 * 隔架区
	 */
	private String factoryHandOnArea;
	
	/**
	 * 隔架号
	 */
	private String factoryHandOnNo;
	
	/**
	 * 门店挂衣区
	 */
	private String handOnArea;
	
	/**
	 * 门店挂衣号
	 */
	private String handOnNo;
	
	/**
	 * 上挂时间
	 */
	private String handOnDateStr;
	
	/**
	 *  已上挂数量
	 */
	private int handOnCount;
	
	/**
	 * 1：门店 2：工厂 3：萨维亚 4：浣衣坊
	 */
	private String handType;
	
	/**
	 * 出厂单号
	 */
	private String leaveFactoryNumber;
	

	/**
	 * 出厂日期
	 */
	private Date leaveDate;
	
	/**
	 * 出厂日期
	 */
	private String leaveDateStr;
	
	/**
	 * 附件列表
	 */
	private List<ClothesAttach> attachList;
	
	/**
	 * 是否有照片 0:没有 1：有
	 */
	private String hasPhoto;
	
	/**
	 * 照片列表
	 */
	private List<ClothesPhoto> photoList;
	
	/**
	 * 微信公众号ID
	 */
	private String appId;

	/**
	 * 地址
	 */
	private String orderAddress;

	public String getName() {
		return userName;
	}

	public void setName(String Name) {
		this.userName = Name;
	}

	public String getHandType() {
		return handType;
	}

	public void setHandType(String handType) {
		this.handType = handType;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getLeaveFactoryNumber() {
		return leaveFactoryNumber;
	}

	public void setLeaveFactoryNumber(String leaveFactoryNumber) {
		this.leaveFactoryNumber = leaveFactoryNumber;
	}

	public Date getLeaveDate() {
		return leaveDate;
	}

	public void setLeaveDate(Date leaveDate) {
		this.leaveDate = leaveDate;
	}

	public String getLeaveDateStr() {
		return leaveDateStr;
	}

	public void setLeaveDateStr(String leaveDateStr) {
		this.leaveDateStr = leaveDateStr;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	
	public String getClothesName() {
		return clothesName;
	}

	public void setClothesName(String clothesName) {
		this.clothesName = clothesName;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
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

	public String getFlaw() {
		return flaw;
	}

	public void setFlaw(String flaw) {
		this.flaw = flaw;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getServiceFee() {
		return serviceFee;
	}

	public void setServiceFee(BigDecimal serviceFee) {
		this.serviceFee = serviceFee;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void so(String status) {
		this.status = status;
	}

	public Date getTakingDate() {
		return takingDate;
	}

	public void setTakingDate(Date takingDate) {
		this.takingDate = takingDate;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
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

	public String getUrgency() {
		return urgency;
	}

	public void setUrgency(String urgency) {
		this.urgency = urgency;
	}

	public String getQueryKey() {
		return queryKey;
	}

	public void setQueryKey(String queryKey) {
		this.queryKey = queryKey;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getSendNumber() {
		return sendNumber;
	}

	public void setSendNumber(String sendNumber) {
		this.sendNumber = sendNumber;
	}
	
	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
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

	public int getClothesCount() {
		return clothesCount;
	}

	public void setClothesCount(int clothesCount) {
		this.clothesCount = clothesCount;
	}

	public int getHandOnCount() {
		return handOnCount;
	}

	public void setHandOnCount(int handOnCount) {
		this.handOnCount = handOnCount;
	}

	public String getSendDateStr() {
		return sendDateStr;
	}

	public void setSendDateStr(String sendDateStr) {
		this.sendDateStr = sendDateStr;
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

	public String getFactoryHandOnArea() {
		return factoryHandOnArea;
	}

	public void setFactoryHandOnArea(String factoryHandOnArea) {
		this.factoryHandOnArea = factoryHandOnArea;
	}

	public String getFactoryHandOnNo() {
		return factoryHandOnNo;
	}

	public void setFactoryHandOnNo(String factoryHandOnNo) {
		this.factoryHandOnNo = factoryHandOnNo;
	}

	public List<ClothesAttach> getAttachList() {
		return attachList;
	}

	public void setAttachList(List<ClothesAttach> attachList) {
		this.attachList = attachList;
	}

	public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	public String getHandOnDateStr() {
		return handOnDateStr;
	}

	public void setHandOnDateStr(String handOnDateStr) {
		this.handOnDateStr = handOnDateStr;
	}

	public String getHasPhoto() {
		return hasPhoto;
	}

	public void setHasPhoto(String hasPhoto) {
		this.hasPhoto = hasPhoto;
	}

	public List<ClothesPhoto> getPhotoList() {
		return photoList;
	}

	public void setPhotoList(List<ClothesPhoto> photoList) {
		this.photoList = photoList;
	}

	public String getOrderAddress() {
		return orderAddress;
	}

	public void setOrderAddress(String orderAddress) {
		this.orderAddress = orderAddress;
	}

}

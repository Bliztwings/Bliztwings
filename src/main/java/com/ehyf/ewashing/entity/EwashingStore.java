package com.ehyf.ewashing.entity;

import java.util.Date;

/**
 * 门店
 * 
 * @author shenxiaozhong
 * 
 */
public class EwashingStore extends BaseEntity<EwashingStore> {

	/**
	 * 主键ID
	 */
	private String id;

	private static final long serialVersionUID = 2635571073259320256L;

	/**
	 * 门店名称
	 */
	private String storeName;

	/**
	 * 门店详细地址
	 */
	private String storeAddress;

	/**
	 * 门店编码
	 */
	private String storeCode;

	/**
	 * 创建时间
	 */
	private Date createDate;

	/**
	 * 创建人ID
	 */
	private String createUserId;
	
	
	/**
	 * 更新人ID
	 */
	private String updateUserId;
	
	
	
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
	 * 门店状态 1:正常 2：关闭
	 */
	private String storeStatus;

	/**
	 * 门店类型1：直营 2：加盟
	 */
	private String storeType;

	/**
	 * 电话
	 */
	private String telephone;

	/**
	 * 省
	 */
	private String province;

	/**
	 * 市
	 */
	private String city;

	/**
	 * 区
	 */
	private String area;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
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

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getStoreAddress() {
		return storeAddress;
	}

	public void setStoreAddress(String storeAddress) {
		this.storeAddress = storeAddress;
	}

	public String getStoreCode() {
		return storeCode;
	}

	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
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

	public String getStoreStatus() {
		return storeStatus;
	}

	public void setStoreStatus(String storeStatus) {
		this.storeStatus = storeStatus;
	}

	public String getStoreType() {
		return storeType;
	}

	public void setStoreType(String storeType) {
		this.storeType = storeType;
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
	
	
}

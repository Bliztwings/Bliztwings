package com.ehyf.ewashing.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 门店字典表
 * 
 * @author Administrator
 * 
 */
public class EwashingDataDictionary extends BaseEntity<EwashingDataDictionary> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5768773282125669334L;

	/**
	 * 主键ID
	 */
	private String id;

	/** 1:衣服名称 2:颜色 3：品牌  4：瑕疵  5：附件**/
	private String dataType;
	
	// 单烫价格
	private BigDecimal price;
	
	// 普洗价格
	private BigDecimal commonPrice;
	
	// 尚洗价格
	private BigDecimal luxuriesPrice;
	
	// O2O价格-浣衣坊价格
	private BigDecimal o2oPrice;
	
	// o2o 萨维亚价格
	private BigDecimal swy_price;
	
	/** 名称 **/
	private String dataName;

	private String createUserId;
	
	private String updateUserId;
	
	private String createUserName;

	private Date createDate;
	
	private String updateUserName;

	private Date updateDate;
	
	/**
	 * 模糊查询条件
	 */
	private String queryKey;
	
	/**
	 * 服务类型 1：单烫 2：普洗 3：精洗
	 */
	private String serviceType;
	
	/**
	 * 子类目ID
	 */
	private String produceCategoryId;
	
	/**
	 * 父类目ID
	 */
	private String produceParentCategoryId;
	
	/**
	 * 图片路径
	 */
	private String imagePath;
	

	
	public String getProduceParentCategoryId() {
		return produceParentCategoryId;
	}

	public void setProduceParentCategoryId(String produceParentCategoryId) {
		this.produceParentCategoryId = produceParentCategoryId;
	}

	public BigDecimal getO2oPrice() {
		return o2oPrice;
	}

	public void setO2oPrice(BigDecimal o2oPrice) {
		this.o2oPrice = o2oPrice;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getProduceCategoryId() {
		return produceCategoryId;
	}

	public void setProduceCategoryId(String produceCategoryId) {
		this.produceCategoryId = produceCategoryId;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getUpdateUserName() {
		return updateUserName;
	}

	public void setUpdateUserName(String updateUserName) {
		this.updateUserName = updateUserName;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getDataName() {
		return dataName;
	}

	public void setDataName(String dataName) {
		this.dataName = dataName;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public BigDecimal getCommonPrice() {
		return commonPrice;
	}

	public void setCommonPrice(BigDecimal commonPrice) {
		this.commonPrice = commonPrice;
	}

	public BigDecimal getLuxuriesPrice() {
		return luxuriesPrice;
	}

	public void setLuxuriesPrice(BigDecimal luxuriesPrice) {
		this.luxuriesPrice = luxuriesPrice;
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

	public String getQueryKey() {
		return queryKey;
	}

	public void setQueryKey(String queryKey) {
		this.queryKey = queryKey;
	}

	public BigDecimal getSwy_price() {
		return swy_price;
	}

	public void setSwy_price(BigDecimal swy_price) {
		this.swy_price = swy_price;
	}
	
}

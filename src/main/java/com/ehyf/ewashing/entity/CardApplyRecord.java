package com.ehyf.ewashing.entity;


public class CardApplyRecord extends BaseEntity<CardApplyRecord> {

	private static final long serialVersionUID = 1L;

	private String id;
	
	private java.util.Date createTimeBegin;
	
	private java.util.Date createTimeEnd;
	
	private String storeName;

	/**所属供应商**/
	private String cardSupplier;

	/**出库单号**/
	private String werehoseNumber;

	/**出库日期**/
	private java.util.Date werehoseTime;

	/**申请数量**/
	private Integer applyNumber;

	/**审批数量**/
	private Integer approvedNumber;

	/**开始卡号**/
	private String numberBegin;

	/**结束卡号**/
	private String numberEnd;

	/**所属门店**/
	private String storeId;

	/**申请人**/
	private String applyer;

	private String remark;

	/**申请状态:待审批：un_approved; 已审批：approved;已出库：warehoused;已收卡：received**/
	private String status;

	/**审批人**/
	private String approver;

	/**审批时间**/
	private java.util.Date approvedTime;

	private java.util.Date createTime;
	
	private String approvedRemark;

	private String createUser;

	private java.util.Date updateTime;

	private String updateUser;

	/**0：否；1：是**/
	private String isDeleted;

	
	
	public String getApprovedRemark() {
		return approvedRemark;
	}

	public void setApprovedRemark(String approvedRemark) {
		this.approvedRemark = approvedRemark;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return this.id;
	}
	public void setCardSupplier(String cardSupplier){
		this.cardSupplier = cardSupplier;
	}

	public String getCardSupplier(){
		return this.cardSupplier;
	}
	public void setWerehoseNumber(String werehoseNumber){
		this.werehoseNumber = werehoseNumber;
	}

	public String getWerehoseNumber(){
		return this.werehoseNumber;
	}
	public void setWerehoseTime(java.util.Date werehoseTime){
		this.werehoseTime = werehoseTime;
	}

	public java.util.Date getWerehoseTime(){
		return this.werehoseTime;
	}
	public void setApplyNumber(Integer applyNumber){
		this.applyNumber = applyNumber;
	}

	public Integer getApplyNumber(){
		return this.applyNumber;
	}
	public void setApprovedNumber(Integer approvedNumber){
		this.approvedNumber = approvedNumber;
	}

	public Integer getApprovedNumber(){
		return this.approvedNumber;
	}
	public void setNumberBegin(String numberBegin){
		this.numberBegin = numberBegin;
	}

	public String getNumberBegin(){
		return this.numberBegin;
	}
	public void setNumberEnd(String numberEnd){
		this.numberEnd = numberEnd;
	}

	public String getNumberEnd(){
		return this.numberEnd;
	}
	public void setStoreId(String storeId){
		this.storeId = storeId;
	}

	public String getStoreId(){
		return this.storeId;
	}
	public void setApplyer(String applyer){
		this.applyer = applyer;
	}

	public String getApplyer(){
		return this.applyer;
	}
	public void setRemark(String remark){
		this.remark = remark;
	}

	public String getRemark(){
		return this.remark;
	}
	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return this.status;
	}
	public void setApprover(String approver){
		this.approver = approver;
	}

	public String getApprover(){
		return this.approver;
	}
	public void setApprovedTime(java.util.Date approvedTime){
		this.approvedTime = approvedTime;
	}

	public java.util.Date getApprovedTime(){
		return this.approvedTime;
	}
	public void setCreateTime(java.util.Date createTime){
		this.createTime = createTime;
	}

	public java.util.Date getCreateTime(){
		return this.createTime;
	}
	public void setCreateUser(String createUser){
		this.createUser = createUser;
	}

	public String getCreateUser(){
		return this.createUser;
	}
	public void setUpdateTime(java.util.Date updateTime){
		this.updateTime = updateTime;
	}

	public java.util.Date getUpdateTime(){
		return this.updateTime;
	}
	public void setUpdateUser(String updateUser){
		this.updateUser = updateUser;
	}

	public String getUpdateUser(){
		return this.updateUser;
	}
	public void setIsDeleted(String isDeleted){
		this.isDeleted = isDeleted;
	}

	public String getIsDeleted(){
		return this.isDeleted;
	}

	public java.util.Date getCreateTimeBegin() {
		return createTimeBegin;
	}

	public void setCreateTimeBegin(java.util.Date createTimeBegin) {
		this.createTimeBegin = createTimeBegin;
	}

	public java.util.Date getCreateTimeEnd() {
		return createTimeEnd;
	}

	public void setCreateTimeEnd(java.util.Date createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}

}

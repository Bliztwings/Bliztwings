package com.ehyf.ewashing.entity;


public class CardOperatorRecord extends BaseEntity<CardOperatorRecord> {

	private static final long serialVersionUID = 1L;

	private String id;

	/**会员卡ID**/
	private String cardId;

	private String storeId;

	/**卡号**/
	private String cardNum;

	/**操作类型:发卡：grant;退卡：reback;补卡：fill;挂失：report_loss**/
	private String operatorType;

	/**申请状态:待审批：un_approved; 已审批：approved;**/
	private String status;

	/**审批人**/
	private String approver;

	/**审批时间**/
	private java.util.Date approvedTime;

	private java.util.Date createTime;

	private String createUser;

	private java.util.Date updateTime;

	private String updateUser;

	/**0：否；1：是**/
	private String isDeleted;

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return this.id;
	}
	public void setCardId(String cardId){
		this.cardId = cardId;
	}

	public String getCardId(){
		return this.cardId;
	}
	public void setStoreId(String storeId){
		this.storeId = storeId;
	}

	public String getStoreId(){
		return this.storeId;
	}
	public void setCardNum(String cardNum){
		this.cardNum = cardNum;
	}

	public String getCardNum(){
		return this.cardNum;
	}
	public void setOperatorType(String operatorType){
		this.operatorType = operatorType;
	}

	public String getOperatorType(){
		return this.operatorType;
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

}

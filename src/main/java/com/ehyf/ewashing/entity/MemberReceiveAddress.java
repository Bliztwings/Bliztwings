package com.ehyf.ewashing.entity;


public class MemberReceiveAddress extends BaseEntity<MemberReceiveAddress> {

	private static final long serialVersionUID = 1L;

	private String id;

	/**会员ID**/
	private String memberId;

	/**区域编号**/
	private String areaCode;

	/**区域名称**/
	private String areaName;

	/**家庭地址**/
	private String homeAddress;

	/**门牌编号**/
	private String doorNumber;

	/**顾客名称**/
	private String customerName;

	/**性别：0:男；1：女**/
	private String sex;

	/**联系电话**/
	private String telephone;

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
	public void setMemberId(String memberId){
		this.memberId = memberId;
	}

	public String getMemberId(){
		return this.memberId;
	}
	public void setAreaCode(String areaCode){
		this.areaCode = areaCode;
	}

	public String getAreaCode(){
		return this.areaCode;
	}
	public void setAreaName(String areaName){
		this.areaName = areaName;
	}

	public String getAreaName(){
		return this.areaName;
	}
	public void setHomeAddress(String homeAddress){
		this.homeAddress = homeAddress;
	}

	public String getHomeAddress(){
		return this.homeAddress;
	}
	public void setDoorNumber(String doorNumber){
		this.doorNumber = doorNumber;
	}

	public String getDoorNumber(){
		return this.doorNumber;
	}
	public void setCustomerName(String customerName){
		this.customerName = customerName;
	}

	public String getCustomerName(){
		return this.customerName;
	}
	public void setSex(String sex){
		this.sex = sex;
	}

	public String getSex(){
		return this.sex;
	}
	public void setTelephone(String telephone){
		this.telephone = telephone;
	}

	public String getTelephone(){
		return this.telephone;
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

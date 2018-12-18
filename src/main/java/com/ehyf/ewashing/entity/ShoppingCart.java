package com.ehyf.ewashing.entity;


public class ShoppingCart extends BaseEntity<ShoppingCart> {

	private static final long serialVersionUID = 1L;

	private String id;

	/**产品ID**/
	private String produceId;

	/**会员ID**/
	private String memberId;

	/**数量**/
	private java.math.BigDecimal count;

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
	public void setProduceId(String produceId){
		this.produceId = produceId;
	}

	public String getProduceId(){
		return this.produceId;
	}
	public void setMemberId(String memberId){
		this.memberId = memberId;
	}

	public String getMemberId(){
		return this.memberId;
	}
	public void setCount(java.math.BigDecimal count){
		this.count = count;
	}

	public java.math.BigDecimal getCount(){
		return this.count;
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

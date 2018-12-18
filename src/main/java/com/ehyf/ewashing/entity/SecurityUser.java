package com.ehyf.ewashing.entity;


public class SecurityUser extends BaseEntity<SecurityUser> {

	private static final long serialVersionUID = 1L;

	private String id;

	private String username;

	private String password;

	private String salt;

	private String realname;

	private String email;

	private String phone;

	private String status;

	/**
	 * 用户类型。空：超级管理员,1:门店用户,2:工厂用户,3:普通管理员 4:o2o管理员
	 */
	private String userType;
	
	private String storeId;
	

	private String createUser;

	private java.util.Date createTime;

	private String updateUser;

	private java.util.Date updateTime;
	
	private String storeName;
	
	/**
	 * 门店对象
	 */
	private EwashingStore ewashingStore;

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return this.id;
	}
	public void setUsername(String username){
		this.username = username;
	}

	public String getUsername(){
		return this.username;
	}
	public void setPassword(String password){
		this.password = password;
	}

	public String getPassword(){
		return this.password;
	}
	public void setSalt(String salt){
		this.salt = salt;
	}

	public String getSalt(){
		return this.salt;
	}
	public void setRealname(String realname){
		this.realname = realname;
	}

	public String getRealname(){
		return this.realname;
	}
	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return this.email;
	}
	public void setPhone(String phone){
		this.phone = phone;
	}

	public String getPhone(){
		return this.phone;
	}
	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return this.status;
	}
	public void setUserType(String userType){
		this.userType = userType;
	}

	public String getUserType(){
		return this.userType;
	}
	public void setCreateUser(String createUser){
		this.createUser = createUser;
	}

	public String getCreateUser(){
		return this.createUser;
	}
	public void setCreateTime(java.util.Date createTime){
		this.createTime = createTime;
	}

	public java.util.Date getCreateTime(){
		return this.createTime;
	}
	public void setUpdateUser(String updateUser){
		this.updateUser = updateUser;
	}

	public String getUpdateUser(){
		return this.updateUser;
	}
	public void setUpdateTime(java.util.Date updateTime){
		this.updateTime = updateTime;
	}

	public java.util.Date getUpdateTime(){
		return this.updateTime;
	}
	
	public String getCredentialsSalt() {
		return username + salt;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

    public EwashingStore getEwashingStore() {
        return ewashingStore;
    }

    public void setEwashingStore(EwashingStore ewashingStore) {
        this.ewashingStore = ewashingStore;
    }

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

}

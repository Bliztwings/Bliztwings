package com.ehyf.ewashing.entity;


public class Member extends BaseEntity<Member> {

	private static final long serialVersionUID = 1L;

	private String id;

	private String name;

	/**0:男；1：女**/
	private String sex;

	private String mobilePhone;

	private String telephone;

	private java.util.Date birthday;

	private String address;

	/**会员类型：持卡会员：card，普通会员:normal**/
	private String type;

	private String remark;

	private String storeId;

	private String province;

	private String city;

	private String area;

	

	private java.util.Date createTime;

	private String createUser;

	private java.util.Date updateTime;

	private String updateUser;

	/**0：否；1：是**/
	private String isDeleted;
	
	/** 辅助字段 **/
	private String storeName;
	
	private String password;
	private String openId;
	private String deviceId;
	private String clientType;//用户类型：pc:pc会员;mobile:手机会员;swy:萨维亚会员;other:其它会员
	private java.util.Date lastLoginTime;
	private String oldPassword;
	private String token;
	private String appId;//微信公众号appId
	private String checkCode;//验证码
	
	
	
	
	public String getCheckCode() {
		return checkCode;
	}

	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getClientType() {
		return clientType;
	}

	public void setClientType(String clientType) {
		this.clientType = clientType;
	}

	public java.util.Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(java.util.Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
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
	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return this.name;
	}
	public void setSex(String sex){
		this.sex = sex;
	}

	public String getSex(){
		return this.sex;
	}
	public void setMobilePhone(String mobilePhone){
		this.mobilePhone = mobilePhone;
	}

	public String getMobilePhone(){
		return this.mobilePhone;
	}
	public void setTelephone(String telephone){
		this.telephone = telephone;
	}

	public String getTelephone(){
		return this.telephone;
	}
	public void setBirthday(java.util.Date birthday){
		this.birthday = birthday;
	}

	public java.util.Date getBirthday(){
		return this.birthday;
	}
	public void setAddress(String address){
		this.address = address;
	}

	public String getAddress(){
		return this.address;
	}
	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return this.type;
	}
	public void setRemark(String remark){
		this.remark = remark;
	}

	public String getRemark(){
		return this.remark;
	}
	public void setStoreId(String storeId){
		this.storeId = storeId;
	}

	public String getStoreId(){
		return this.storeId;
	}
	public void setProvince(String province){
		this.province = province;
	}

	public String getProvince(){
		return this.province;
	}
	public void setCity(String city){
		this.city = city;
	}

	public String getCity(){
		return this.city;
	}
	public void setArea(String area){
		this.area = area;
	}

	public String getArea(){
		return this.area;
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

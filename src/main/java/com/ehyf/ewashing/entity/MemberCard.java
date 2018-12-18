package com.ehyf.ewashing.entity;


public class MemberCard extends BaseEntity<MemberCard> {

	private static final long serialVersionUID = 1L;

	private String id;
	
	private String srcCardNumber;
	
	private String memberId;
	
	private String mobilePhone;
	
	private String memberName;
	
	private String storeName;
	
	private String storeId;

	/**会员卡号**/
	private String cardNumber;

	/**卡现金金额**/
	private java.math.BigDecimal cashAmount;

	/**卡赠送金额**/
	private java.math.BigDecimal givedAmount;

	/**卡总金额**/
	private java.math.BigDecimal totalAmount;

	/**卡折扣**/
	private java.math.BigDecimal discount;


	/**付款方式：现金：cash，支付宝:alipay、微信：wechat、银行卡：blank**/
	private String payedMethod;

	/**是否启用密码:0:是；1：否**/
	private String isUsePassword;

	/**消费密码**/
	private String password;

	/**是否开票:0:是；1：否**/
	private String isInvoiced;

	/**发票备注**/
	private String invoiceRemark;

	private java.util.Date createTime;

	private String createUser;

	private java.util.Date updateTime;

	private String updateUser;

	/**0：否；1：是**/
	private String isDeleted;

	/**卡状态：新卡：new;正常：normal;退卡：back_card；挂失：report_loss;补卡：fill_card**/
	private String cardStatus;
	
	private String queryKey;//临时变量
	private String appId;//临时变量
	
	

	
	public String getQueryKey() {
		return queryKey;
	}

	public void setQueryKey(String queryKey) {
		this.queryKey = queryKey;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getSrcCardNumber() {
		return srcCardNumber;
	}

	public void setSrcCardNumber(String srcCardNumber) {
		this.srcCardNumber = srcCardNumber;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return this.id;
	}
	public void setCardNumber(String cardNumber){
		this.cardNumber = cardNumber;
	}

	public String getCardNumber(){
		return this.cardNumber;
	}
	public void setCashAmount(java.math.BigDecimal cashAmount){
		this.cashAmount = cashAmount;
	}

	public java.math.BigDecimal getCashAmount(){
		return this.cashAmount;
	}
	public void setGivedAmount(java.math.BigDecimal givedAmount){
		this.givedAmount = givedAmount;
	}

	public java.math.BigDecimal getGivedAmount(){
		return this.givedAmount;
	}
	public void setTotalAmount(java.math.BigDecimal totalAmount){
		this.totalAmount = totalAmount;
	}

	public java.math.BigDecimal getTotalAmount(){
		return this.totalAmount;
	}
	public void setDiscount(java.math.BigDecimal discount){
		this.discount = discount;
	}

	public java.math.BigDecimal getDiscount(){
		return this.discount;
	}
	public void setPayedMethod(String payedMethod){
		this.payedMethod = payedMethod;
	}

	public String getPayedMethod(){
		return this.payedMethod;
	}
	public void setIsUsePassword(String isUsePassword){
		this.isUsePassword = isUsePassword;
	}

	public String getIsUsePassword(){
		return this.isUsePassword;
	}
	public void setPassword(String password){
		this.password = password;
	}

	public String getPassword(){
		return this.password;
	}
	public void setIsInvoiced(String isInvoiced){
		this.isInvoiced = isInvoiced;
	}

	public String getIsInvoiced(){
		return this.isInvoiced;
	}
	public void setInvoiceRemark(String invoiceRemark){
		this.invoiceRemark = invoiceRemark;
	}

	public String getInvoiceRemark(){
		return this.invoiceRemark;
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
	public void setCardStatus(String cardStatus){
		this.cardStatus = cardStatus;
	}

	public String getCardStatus(){
		return this.cardStatus;
	}

}

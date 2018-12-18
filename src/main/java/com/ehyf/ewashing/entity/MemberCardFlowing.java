package com.ehyf.ewashing.entity;


public class MemberCardFlowing extends BaseEntity<MemberCardFlowing> {

	private static final long serialVersionUID = 1L;

	private String id;

	private String storeId;

	/**会员卡ID**/
	private String cardId;

	private String cardNum;

	/**操作前现金帐户金额**/
	private java.math.BigDecimal cashAmountBefore;

	/**操作现金帐户金额**/
	private java.math.BigDecimal cashAmountOper;

	/**操作后现金账户金额**/
	private java.math.BigDecimal cashAmountAfter;

	/**操作前赠送帐户金额**/
	private java.math.BigDecimal givedAmountBefore;

	/**操作赠送帐户金额**/
	private java.math.BigDecimal givedAmountOper;

	/**操作后赠送账户金额**/
	private java.math.BigDecimal givedAmountAfter;

	/**操作类型:消费：consumption;充值：recharge;退款：refund**/
	private String operType;

	private java.util.Date createTime;

	private String createUser;

	private java.util.Date updateTime;

	private String updateUser;

	/**0：否；1：是**/
	private String isDeleted;
	
	private String payedMethod;//付款方式：现金：cash，支付宝:alipay、微信：wechat、银行卡：blank
	
	private String sourceId;
	private String remark;
	
	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPayedMethod() {
		return payedMethod;
	}

	public void setPayedMethod(String payedMethod) {
		this.payedMethod = payedMethod;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return this.id;
	}
	public void setStoreId(String storeId){
		this.storeId = storeId;
	}

	public String getStoreId(){
		return this.storeId;
	}
	public void setCardId(String cardId){
		this.cardId = cardId;
	}

	public String getCardId(){
		return this.cardId;
	}
	public void setCardNum(String cardNum){
		this.cardNum = cardNum;
	}

	public String getCardNum(){
		return this.cardNum;
	}
	public void setCashAmountBefore(java.math.BigDecimal cashAmountBefore){
		this.cashAmountBefore = cashAmountBefore;
	}

	public java.math.BigDecimal getCashAmountBefore(){
		return this.cashAmountBefore;
	}
	public void setCashAmountOper(java.math.BigDecimal cashAmountOper){
		this.cashAmountOper = cashAmountOper;
	}

	public java.math.BigDecimal getCashAmountOper(){
		return this.cashAmountOper;
	}
	public void setCashAmountAfter(java.math.BigDecimal cashAmountAfter){
		this.cashAmountAfter = cashAmountAfter;
	}

	public java.math.BigDecimal getCashAmountAfter(){
		return this.cashAmountAfter;
	}
	public void setGivedAmountBefore(java.math.BigDecimal givedAmountBefore){
		this.givedAmountBefore = givedAmountBefore;
	}

	public java.math.BigDecimal getGivedAmountBefore(){
		return this.givedAmountBefore;
	}
	public void setGivedAmountOper(java.math.BigDecimal givedAmountOper){
		this.givedAmountOper = givedAmountOper;
	}

	public java.math.BigDecimal getGivedAmountOper(){
		return this.givedAmountOper;
	}
	public void setGivedAmountAfter(java.math.BigDecimal givedAmountAfter){
		this.givedAmountAfter = givedAmountAfter;
	}

	public java.math.BigDecimal getGivedAmountAfter(){
		return this.givedAmountAfter;
	}
	public void setOperType(String operType){
		this.operType = operType;
	}

	public String getOperType(){
		return this.operType;
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

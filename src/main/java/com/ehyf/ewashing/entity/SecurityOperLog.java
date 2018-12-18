package com.ehyf.ewashing.entity;


public class SecurityOperLog extends BaseEntity<SecurityOperLog> {

	private static final long serialVersionUID = 1L;
	
	private String id;

	/**登录用户名**/
	private String userName;

	/**登录用户实名**/
	private String userRealName;

	/**操作时间**/
	private java.util.Date operTime;

	/**IP地址**/
	private String ip;

	/**操作对象**/
	private String operObject;

	/**操作活动**/
	private String operAction;

	/**操作描述**/
	private String operDesc;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setUserName(String userName){
		this.userName = userName;
	}

	public String getUserName(){
		return this.userName;
	}
	public void setUserRealName(String userRealName){
		this.userRealName = userRealName;
	}

	public String getUserRealName(){
		return this.userRealName;
	}
	public void setOperTime(java.util.Date operTime){
		this.operTime = operTime;
	}

	public java.util.Date getOperTime(){
		return this.operTime;
	}
	public void setIp(String ip){
		this.ip = ip;
	}

	public String getIp(){
		return this.ip;
	}
	public void setOperObject(String operObject){
		this.operObject = operObject;
	}

	public String getOperObject(){
		return this.operObject;
	}
	public void setOperAction(String operAction){
		this.operAction = operAction;
	}

	public String getOperAction(){
		return this.operAction;
	}
	public void setOperDesc(String operDesc){
		this.operDesc = operDesc;
	}

	public String getOperDesc(){
		return this.operDesc;
	}

}

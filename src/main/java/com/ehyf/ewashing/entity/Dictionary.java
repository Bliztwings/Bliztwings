package com.ehyf.ewashing.entity;


public class Dictionary extends BaseEntity<Dictionary> {

	private static final long serialVersionUID = 1L;

	private String id;
	
	private String code;

	/**名称域**/
	private String name;

	/**值域**/
	private String value;

	/**扩展域1**/
	private String extend1;

	/**扩展域2**/
	private String extend2;

	/**扩展域3**/
	private String extend3;

	/**扩展域4**/
	private String extend4;

	/**扩展域5**/
	private String extend5;

	/**排序编号**/
	private Integer sort;

	private java.util.Date createTime;

	private String createUser;

	private java.util.Date updateTime;

	private String updateUser;

	/**0：否；1：是**/
	private String isDeleted;

	
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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
	public void setValue(String value){
		this.value = value;
	}

	public String getValue(){
		return this.value;
	}
	public void setExtend1(String extend1){
		this.extend1 = extend1;
	}

	public String getExtend1(){
		return this.extend1;
	}
	public void setExtend2(String extend2){
		this.extend2 = extend2;
	}

	public String getExtend2(){
		return this.extend2;
	}
	public void setExtend3(String extend3){
		this.extend3 = extend3;
	}

	public String getExtend3(){
		return this.extend3;
	}
	public void setExtend4(String extend4){
		this.extend4 = extend4;
	}

	public String getExtend4(){
		return this.extend4;
	}
	public void setExtend5(String extend5){
		this.extend5 = extend5;
	}

	public String getExtend5(){
		return this.extend5;
	}
	public void setSort(Integer sort){
		this.sort = sort;
	}

	public Integer getSort(){
		return this.sort;
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

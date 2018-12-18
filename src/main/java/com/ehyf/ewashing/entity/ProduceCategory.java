package com.ehyf.ewashing.entity;


public class ProduceCategory extends BaseEntity<ProduceCategory> {

	private static final long serialVersionUID = 1L;

	private String id;

	/**父节点ID**/
	private String parentId;

	/**类目编号**/
	private String code;

	/**类目名称**/
	private String name;

	/**排序编号**/
	private Integer sort;

	private java.util.Date createTime;

	private String createUser;

	private java.util.Date updateTime;

	private String updateUser;

	/**0：否；1：是**/
	private String isDeleted;
	
	private String imgUrl;

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return this.id;
	}
	public void setParentId(String parentId){
		this.parentId = parentId;
	}

	public String getParentId(){
		return this.parentId;
	}
	public void setCode(String code){
		this.code = code;
	}

	public String getCode(){
		return this.code;
	}
	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return this.name;
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

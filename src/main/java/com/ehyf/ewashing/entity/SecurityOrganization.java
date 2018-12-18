package com.ehyf.ewashing.entity;

public class SecurityOrganization extends BaseEntity<SecurityOrganization> {

	private static final long serialVersionUID = 1L;
	
	private String id;

	private String name;

	private String shortName;

	private String parentId;

	private String orgCode;

	private String orgCategory;

	private String description;

	private String createUser;

	private java.util.Date createDate;

	private String updateUser;

	private java.util.Date updateDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return this.name;
	}
	public void setShortName(String shortName){
		this.shortName = shortName;
	}

	public String getShortName(){
		return this.shortName;
	}
	public void setParentId(String parentId){
		this.parentId = parentId;
	}

	public String getParentId(){
		return this.parentId;
	}
	public void setOrgCode(String orgCode){
		this.orgCode = orgCode;
	}

	public String getOrgCode(){
		return this.orgCode;
	}
	public void setOrgCategory(String orgCategory){
		this.orgCategory = orgCategory;
	}

	public String getOrgCategory(){
		return this.orgCategory;
	}
	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return this.description;
	}
	public void setCreateUser(String createUser){
		this.createUser = createUser;
	}

	public String getCreateUser(){
		return this.createUser;
	}
	public void setCreateDate(java.util.Date createDate){
		this.createDate = createDate;
	}

	public java.util.Date getCreateDate(){
		return this.createDate;
	}
	public void setUpdateUser(String updateUser){
		this.updateUser = updateUser;
	}

	public String getUpdateUser(){
		return this.updateUser;
	}
	public void setUpdateDate(java.util.Date updateDate){
		this.updateDate = updateDate;
	}

	public java.util.Date getUpdateDate(){
		return this.updateDate;
	}

}

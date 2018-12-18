package com.ehyf.ewashing.entity;


public class SecurityUserRole extends BaseEntity<SecurityUserRole> {

	private static final long serialVersionUID = 1L;
	
	private String id;

	private String userId;

	private String roleId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return this.userId;
	}
	public void setRoleId(String roleId){
		this.roleId = roleId;
	}

	public String getRoleId(){
		return this.roleId;
	}

}

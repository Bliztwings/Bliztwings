package com.ehyf.ewashing.entity;

public class SecurityOrganizationRole extends BaseEntity<SecurityOrganizationRole> {

	private static final long serialVersionUID = 1L;
	
	private String id;

	private String orgId;

	private String roleId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setOrgId(String orgId){
		this.orgId = orgId;
	}

	public String getOrgId(){
		return this.orgId;
	}
	public void setRoleId(String roleId){
		this.roleId = roleId;
	}

	public String getRoleId(){
		return this.roleId;
	}

}

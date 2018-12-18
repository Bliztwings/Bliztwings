package com.ehyf.ewashing.entity;


public class SecurityRolePermission extends BaseEntity<SecurityRolePermission> {

	private static final long serialVersionUID = 1L;
	
	private String id;

	private String roleId;

	private String permissionId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setRoleId(String roleId){
		this.roleId = roleId;
	}

	public String getRoleId(){
		return this.roleId;
	}
	public void setPermissionId(String permissionId){
		this.permissionId = permissionId;
	}

	public String getPermissionId(){
		return this.permissionId;
	}

}

package com.ehyf.ewashing.vo;

import java.io.Serializable;

public class SecurityUserRoleVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6461437561319564481L;

	private String id;

	private String userId;

	private String roleId;
	
	private String roleName;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
}

package com.ehyf.ewashing.entity;


public class SecurityRole extends BaseEntity<SecurityRole> {

	private static final long serialVersionUID = 1L;
	
	private String id;

	private String name;

	private String code;

	private String description;

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
	public void setCode(String code){
		this.code = code;
	}

	public String getCode(){
		return this.code;
	}
	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return this.description;
	}

}

package com.ehyf.ewashing.entity;



/**
 * 
 * 全国省市区街道
 * 
 **/
public class SysArea extends BaseEntity<SysArea> {

	private static final long serialVersionUID = 1L;

	private Integer id;

	private String code;

	private String parentCode;

	private String name;

	private Integer level;

	public void setId(Integer id){
		this.id = id;
	}

	public Integer getId(){
		return this.id;
	}
	public void setCode(String code){
		this.code = code;
	}

	public String getCode(){
		return this.code;
	}
	public void setParentCode(String parentCode){
		this.parentCode = parentCode;
	}

	public String getParentCode(){
		return this.parentCode;
	}
	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return this.name;
	}
	public void setLevel(Integer level){
		this.level = level;
	}

	public Integer getLevel(){
		return this.level;
	}

}

package com.ehyf.ewashing.entity;

import java.util.List;

public class SecurityResource extends BaseEntity<SecurityResource> {

	private static final long serialVersionUID = 1L;
	
	private String id;

	private String name;

	private String category;

	private String url;

	private String icon;

	private String parentId;

	private String permission;

	private Integer available;

	private List<SecurityResource> children;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<SecurityResource> getChildren() {
		return children;
	}

	public void setChildren(List<SecurityResource> children) {
		this.children = children;
	}
	
	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return this.name;
	}
	public void setCategory(String category){
		this.category = category;
	}

	public String getCategory(){
		return this.category;
	}
	public void setUrl(String url){
		this.url = url;
	}

	public String getUrl(){
		return this.url;
	}
	public void setIcon(String icon){
		this.icon = icon;
	}

	public String getIcon(){
		return this.icon;
	}
	public void setParentId(String parentId){
		this.parentId = parentId;
	}

	public String getParentId(){
		return this.parentId;
	}
	public void setPermission(String permission){
		this.permission = permission;
	}

	public String getPermission(){
		return this.permission;
	}
	public void setAvailable(Integer available){
		this.available = available;
	}

	public Integer getAvailable(){
		return this.available;
	}

}

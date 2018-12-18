package com.ehyf.ewashing.wechat.model;

public class TemplateMsgData {

	private String value;
	
	private String color;
	
	public TemplateMsgData(){
		
	}
	
	public TemplateMsgData(String value,String color){
		this.color = color;
		this.value = value;
	}



	public String getColor() {
	
		return color;
	}

	public void setColor(String color) {
	
		this.color = color;
	}

	public String getValue() {
	
		return value;
	}

	public void setValue(String value) {
	
		this.value = value;
	}

}

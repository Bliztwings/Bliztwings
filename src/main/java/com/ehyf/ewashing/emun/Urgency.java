package com.ehyf.ewashing.emun;

public enum Urgency {

//  0：未付款 1：已支付
	Urgency0("0", "一般 "),
	Urgency1("1", "紧急");
	
		

	private Urgency(String type, String text) {
		this.text = text;
		this.type = type;
	}

	
	public static String getName(String type){
		for(Urgency s :Urgency.values()){
			if(s.getType().equals(type)){
				return s.text;
			}
		}
		return null;
	}
	private String type;

	private String text;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}

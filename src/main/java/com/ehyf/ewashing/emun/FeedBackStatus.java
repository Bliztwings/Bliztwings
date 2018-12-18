package com.ehyf.ewashing.emun;

public enum FeedBackStatus {

//  0：未付款 1：已支付
	FeedBackStatus1("1", "洗护质量"),
	FeedBackStatus2("2", "服务态度"),
	FeedBackStatus3("3", "物流速度"),
	FeedBackStatus4("4", "产品易用性"),
	FeedBackStatus5("5", "付款流程"),
	FeedBackStatus6("6", "其他");
	
	
		

	private FeedBackStatus(String type, String text) {
		this.text = text;
		this.type = type;
	}

	
	public static String getName(String type){
		for(FeedBackStatus s :FeedBackStatus.values()){
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

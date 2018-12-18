package com.ehyf.ewashing.emun;

public enum PayStatus {

	//  0：未付款 1：已支付
	PayStatus0("0", "未付款 "),
	PayStatus1("1", "已支付"),
	PayStatus2("2", "洗后付款");
	
		

	private PayStatus(String type, String text) {
		this.text = text;
		this.type = type;
	}

	
	public static String getName(String type){
		for(PayStatus s :PayStatus.values()){
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

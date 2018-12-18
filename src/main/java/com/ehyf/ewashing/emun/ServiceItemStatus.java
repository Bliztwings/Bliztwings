package com.ehyf.ewashing.emun;

public enum ServiceItemStatus {

	// 0：未付款 1：已支付
	ServiceItemStatus1("1", "洗衣 "), ServiceItemStatus2("2", "洗鞋"), ServiceItemStatus3("3", "家居家纺"), ServiceItemStatus4("4", "皮具洗护");

	private ServiceItemStatus(String type, String text) {
		this.text = text;
		this.type = type;
	}

	public static String getName(String type) {
		for (ServiceItemStatus s : ServiceItemStatus.values()) {
			if (s.getType().equals(type)) {
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

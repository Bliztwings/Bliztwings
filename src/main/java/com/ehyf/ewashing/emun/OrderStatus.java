package com.ehyf.ewashing.emun;

public enum OrderStatus {

	//   0：已收衣待送洗 1：已送洗待洗护 2:洗护中 3:洗护完成  4:工厂待上挂  5:工厂已上挂(待出厂) 6：已出厂 7：已上挂待取衣 8：完成
	OrderStatus0("0", "已收衣待送洗 "),
	OrderStatus1("1", "已送洗待洗护"),
	OrderStatus2("2", "洗护中"),
	OrderStatus3("3", "洗护完成"),
	OrderStatus4("4", "工厂待上挂"),
	OrderStatus5("5", "工厂已上挂(待出厂)"),
	OrderStatus6("6", "已出厂"),
	OrderStatus7("7", "已上挂待取衣"),
	OrderStatus8("8", "完成"),
	
	OrderStatus20("20", "取件中"),
	OrderStatus21("21", "送往工厂"),
	OrderStatus22("22", "清洗中"),
	OrderStatus23("23", "送回中"),
	OrderStatus24("24", "已签收");

	private OrderStatus(String type, String text) {
		this.text = text;
		this.type = type;
	}

	
	public static String getName(String type){
		for(OrderStatus s :OrderStatus.values()){
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

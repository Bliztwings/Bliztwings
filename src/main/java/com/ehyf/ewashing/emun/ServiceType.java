package com.ehyf.ewashing.emun;

public enum ServiceType {

	//1:单烫 2：普洗 3：尚品洗涤
	ServiceType1("1", "单烫"),
	ServiceType2("2", "普洗"),
	ServiceType3("3", "尚品洗涤");
	

	private ServiceType(String type, String text) {
		this.text = text;
		this.type = type;
	}

	
	public static String getName(String type){
		for(ServiceType s :ServiceType.values()){
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

package com.ehyf.ewashing.emun;

public enum ClothesStatus {

	// 衣物状态 0:已收衣 1:待送洗 2：待入厂 3:已入厂  4：水洗 5：干洗 6：烘干7：熨烫 8：精洗 9：皮衣 10：质检 11：分拣 12:工厂上挂 13：出厂 14：已签收 (门店) 15：门店已上挂 16 已取衣
    // 洗衣工序:30:质检，31:水洗，32:干洗，33:皮衣，34:精洗，35:熨烫，36:去渍，37:烘干，38:去毛，39:织补，40:鞋类，41:工服洗护
	ClothesStatus0("0", "已收件"), 
	ClothesStatus1("1", "待送洗"), 
	ClothesStatus2("2", "待入厂 "), 
	ClothesStatus3("3", "已入厂"), 
	ClothesStatus4("4","水洗 "), 
	ClothesStatus5("5", "干洗 "), 
	ClothesStatus6("6", "烘干"), 
	ClothesStatus7("7","熨烫"), 
	ClothesStatus8("8", "精洗"), 
	ClothesStatus9("9", "皮衣 "), 
	ClothesStatus10("10","质检"), 
	ClothesStatus11("11", "分拣"), 
	ClothesStatus12("12", "工厂已上挂"),
	ClothesStatus13("13", "已出厂"),
	ClothesStatus14("14", "门店已签收"),
	ClothesStatus15("15", "门店已上挂 "),
	ClothesStatus16("16", "已取衣 "),
    
    ClothesStatus30("30", "质检"),
    ClothesStatus31("31", "水洗"),
    ClothesStatus32("32", "干洗"),
    ClothesStatus33("33", "皮衣"),
    ClothesStatus34("34", "精洗"),
    ClothesStatus35("35", "熨烫"),
    ClothesStatus36("36", "去渍"),
    ClothesStatus37("37", "烘干"),
    ClothesStatus38("38", "去毛"),
    ClothesStatus39("39", "织补"),
    ClothesStatus40("40", "鞋类"),
    ClothesStatus41("41", "工服洗护");
    
	private ClothesStatus(String type, String text) {
			this.text = text;
			this.type = type;
		}

	public static String getName(String type) {
		for (ClothesStatus s : ClothesStatus.values()) {
			if (s.getType().equals(type)) {
				return s.text;
			}
		}
		return null;
	}
	
	public static String getType(String name) {
		for (ClothesStatus s : ClothesStatus.values()) {
			if (s.getText().equals(name)) {
				return s.type;
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

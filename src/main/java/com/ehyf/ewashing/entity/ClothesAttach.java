package com.ehyf.ewashing.entity;

public class ClothesAttach extends BaseEntity<ClothesAttach> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6774412906386569392L;

	private String id ;
	
	private String clothesId;
	
	private String clothesBarCode;
	
	private String attachBarCode;
	
	private String attachId;
	
	private String attachName;
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getClothesId() {
		return clothesId;
	}

	public void setClothesId(String clothesId) {
		this.clothesId = clothesId;
	}

	public String getClothesBarCode() {
		return clothesBarCode;
	}

	public void setClothesBarCode(String clothesBarCode) {
		this.clothesBarCode = clothesBarCode;
	}

	public String getAttachBarCode() {
		return attachBarCode;
	}

	public void setAttachBarCode(String attachBarCode) {
		this.attachBarCode = attachBarCode;
	}

	public String getAttachId() {
		return attachId;
	}

	public void setAttachId(String attachId) {
		this.attachId = attachId;
	}

	public String getAttachName() {
		return attachName;
	}

	public void setAttachName(String attachName) {
		this.attachName = attachName;
	}
	
}

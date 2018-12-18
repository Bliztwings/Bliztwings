package com.ehyf.ewashing.entity;

import com.ehyf.ewashing.entity.BaseEntity;

/**
 *  送洗明细表
 * @author shenxiaozhong
 *
 */
public class SendWashingDetail extends BaseEntity<SendWashingDetail> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1760663177374325524L;
	
	
	private String id;
	
	/**
	 * 送洗单号
	 */
	private String sendNumber;
	
	/**
	 * 送洗衣服ID
	 */
	private String clothesId;
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSendNumber() {
		return sendNumber;
	}

	public void setSendNumber(String sendNumber) {
		this.sendNumber = sendNumber;
	}

	public String getClothesId() {
		return clothesId;
	}

	public void setClothesId(String clothesId) {
		this.clothesId = clothesId;
	}

}

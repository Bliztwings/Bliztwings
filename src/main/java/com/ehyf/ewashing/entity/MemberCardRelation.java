package com.ehyf.ewashing.entity;


public class MemberCardRelation extends BaseEntity<MemberCardRelation> {

	private static final long serialVersionUID = 1L;

	private String memberId;

	private String cardId;

	public void setMemberId(String memberId){
		this.memberId = memberId;
	}

	public String getMemberId(){
		return this.memberId;
	}
	public void setCardId(String cardId){
		this.cardId = cardId;
	}

	public String getCardId(){
		return this.cardId;
	}

}

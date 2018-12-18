package com.ehyf.ewashing.wechat.model;

public class Result {

	/**
	 * 返回编号
	 */
	private int errcode;

	/**
	 * 返回信息
	 */
	private String errmsg;

	public int getErrcode() {

		return errcode;
	}

	public void setErrcode(int errcode) {

		this.errcode = errcode;
	}

	public String getErrmsg() {

		return errmsg;
	}

	public void setErrmsg(String errmsg) {

		this.errmsg = errmsg;
	}

}

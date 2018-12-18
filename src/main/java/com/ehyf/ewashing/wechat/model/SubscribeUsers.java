package com.ehyf.ewashing.wechat.model;

/**
 * 获取关注列表bean
 * 
 * @author shenxiaozhong Date：2014年8月21日下午3:28:08
 *
 */
public class SubscribeUsers {

	/**
	 * 关注该公众账号的总用户数
	 */
	private Integer total;

	/**
	 * 拉取的OPENID个数，最大值为10000
	 */
	private Integer count;

	/**
	 * 列表数据，OPENID的列表
	 */
	private OpenId data;

	/**
	 * 拉取列表的后一个用户的OPENID
	 */
	private String next_openid;

	public int getTotal() {

		return total;
	}

	public void setTotal(int total) {

		this.total = total;
	}

	public int getCount() {

		return count;
	}

	public void setCount(int count) {

		this.count = count;
	}

	public OpenId getData() {

		return data;
	}

	public void setData(OpenId data) {

		this.data = data;
	}

	public String getNext_openid() {

		return next_openid;
	}

	public void setNext_openid(String next_openid) {

		this.next_openid = next_openid;
	}

}

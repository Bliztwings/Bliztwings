package com.ehyf.ewashing.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ehyf.ewashing.entity.MemberCard;

/**
 * 
 * MemberCardDao  数据库操作接口类
 * 
 **/
@Repository
public interface MemberCardDao extends BaseDao<MemberCard> {
	//获取所有未绑定的会员卡号
	public List<String> getNewCardNos(String storeId);
	
	//根据卡号获取卡信息
	public MemberCard getCardByNo(String cardNumber);

	/**
	 * 根据卡号、手机号、会员名称获取会员信息
	 * @param queryKey
	 * @return
	 */
	public MemberCard queryMemberByCardOrMobile(MemberCard c);
	
	//会员卡挂失
	public int reportLoss(String id);
	//会员卡取消挂失
	public int unReportLoss(String id);
	//会员卡退卡
	public int reback(String id);
	//会员卡作废
	public int cancel(String id);
	
	//根据会员ID查询会员卡
	public MemberCard queryCardByMemberId(String memberId);
}
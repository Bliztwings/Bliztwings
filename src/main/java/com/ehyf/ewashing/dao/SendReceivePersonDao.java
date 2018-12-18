package com.ehyf.ewashing.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ehyf.ewashing.entity.SendReceivePerson;

@Repository
public interface SendReceivePersonDao extends BaseDao<SendReceivePerson> {
	
	public SendReceivePerson getByMbileAndPwd(SendReceivePerson e);
	
	public SendReceivePerson getByToken(String token);

	public List<SendReceivePerson> queryXiaoE(SendReceivePerson p);
}

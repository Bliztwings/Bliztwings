package com.ehyf.ewashing.dao;


import org.springframework.stereotype.Repository;

import com.ehyf.ewashing.entity.HandOnNo;

@Repository
public interface EwashingHandonNoDao extends BaseDao<HandOnNo>{

	int updateHandNo(HandOnNo no);


}

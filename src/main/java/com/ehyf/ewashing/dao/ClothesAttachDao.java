package com.ehyf.ewashing.dao;

import org.springframework.stereotype.Repository;

import com.ehyf.ewashing.entity.ClothesAttach;

@Repository
public interface ClothesAttachDao extends BaseDao<ClothesAttach> {

	int deleteAttachByClothesId(String id);

	int updateAttachByAttachId(ClothesAttach attach);

}

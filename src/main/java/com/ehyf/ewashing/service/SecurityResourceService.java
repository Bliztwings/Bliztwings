package com.ehyf.ewashing.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ehyf.ewashing.dao.SecurityResourceDao;
import com.ehyf.ewashing.entity.SecurityResource;

/**
 * 
 * SecurityResourceService  Service服务接口类
 * 
 **/
@Service
public class SecurityResourceService extends BaseService<SecurityResourceDao,SecurityResource> {
	@Autowired
	private SecurityResourceDao securityResourceDao;
	
	public List<SecurityResource> findList(SecurityResource entity) {
		List<SecurityResource> list = securityResourceDao.findList(entity);
		List<SecurityResource> children = null;
		for (SecurityResource securityResource : list) {
			children = getChildren(securityResource.getId());
			if(children!=null && children.size()>0){
				securityResource.setChildren(children);
			}
		}
		return list;
	}
	
	private List<SecurityResource> getChildren(String parentId) {
		SecurityResource entity = new SecurityResource();
		entity.setCategory("menu");
		entity.setParentId(parentId);
		entity.setAvailable(1);
		return securityResourceDao.findList(entity);
	}
	
	 public static void main(String[] args) {
	        UUID uuid = UUID.randomUUID();
	        System.out.println(uuid);
	    }
}
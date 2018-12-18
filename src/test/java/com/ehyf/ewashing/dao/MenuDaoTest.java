package com.ehyf.ewashing.dao;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ehyf.ewashing.dao.SecurityResourceDao;
import com.ehyf.ewashing.entity.SecurityResource;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ehyf.ewashing.BaseTest;

public class MenuDaoTest extends BaseTest {
	@Autowired
	private SecurityResourceDao securityResourceDao;
	
	@Test
	public void findList(){
		PageHelper.startPage(1,5);
		SecurityResource entity = new SecurityResource();
		List<SecurityResource> list = securityResourceDao.findList(entity);
		PageInfo<SecurityResource> page = new PageInfo<SecurityResource>(list);
		
		System.out.println("总记录数："+page.getTotal());
		System.out.println("总页数："+page.getPages());
	}
	
}

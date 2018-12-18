package com.ehyf.ewashing.service;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ehyf.ewashing.entity.SecurityResource;
import com.ehyf.ewashing.service.SecurityResourceService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ehyf.ewashing.BaseTest;

public class MenuServiceTest extends BaseTest{
	@Autowired
	private SecurityResourceService securityResourceService;
	
	@Test
	public void findPage(){
		PageHelper.startPage(1,5);
		SecurityResource entity = new SecurityResource();
		List<SecurityResource> list = securityResourceService.findList(entity);
		PageInfo<SecurityResource> page = new PageInfo<SecurityResource>(list);
		
		System.out.println("总记录数："+page.getTotal());
		System.out.println("总页数："+page.getPages());
	}
}

package com.ehyf.ewashing.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.ehyf.ewashing.dao.BaseDao;
import com.ehyf.ewashing.entity.BaseEntity;

/**
 * service层基类
 * @author jelly
 *
 * @param <D>
 * @param <T>
 */
@Transactional(readOnly = true)
public abstract class BaseService<D extends BaseDao<T>, T extends BaseEntity<T>> {
	/**
	 * 持久层对象
	 */
	@Autowired
	protected D dao;
	
	/**
	 * 获取单条数据
	 * @param id
	 * @return
	 */
	public T getById(String id) {
		return dao.getById(id);
	}
	
	/**
	 * 查询列表数据
	 * @param entity
	 * @return
	 */
	public List<T> findList(T entity) {
		return dao.findList(entity);
	}
	
	/**
	 * 插入数据
	 * @param entity
	 */
	@Transactional(readOnly = false)
	public int insert(T entity) {
		return dao.insert(entity);
	}
	
	/**
	 * 更新数据
	 * @param entity
	 */
	@Transactional(readOnly = false)
	public int update(T entity) {
		return dao.update(entity);
	}
	
	/**
	 * 删除数据
	 * @param entity
	 */
	@Transactional(readOnly = false)
	public int deleteById(String id) {
		return dao.deleteById(id);
	}
	
	/**
	 * 根据条件进行删除操作
	 * @param entity
	 * @return
	 */
	@Transactional(readOnly = false)
	public int delete(T entity){
		return dao.delete(entity);
	}
	
}

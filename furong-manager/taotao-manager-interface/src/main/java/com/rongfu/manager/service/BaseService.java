package com.rongfu.manager.service;

import java.util.List;

/**
 * 基础Service
 * @author Steven
 *
 */
public interface BaseService<T> {
	// 1. queryById
	/**
	 * 跟据id查询信息
	 * @param id
	 * @return
	 */
	public T queryById(Long id);
	// 2. queryAll
	/**
	 * 查询所有数据
	 * @return
	 */
	public List<T> queryAll();
	// 3. queryCountByWhere
	/**
	 * 跟据查询条件查询总记录数
	 * @param t
	 * @return
	 */
	public Integer queryCountByWhere(T t);
	// 4. queryListByWhere
	/**
	 * 跟据查询条件查询数据列表
	 * @param t
	 * @return
	 */
	public List<T> queryListByWhere(T t);
	// 5. queryByPage
	/**
	 * 分页查询数据
	 * @param page
	 * @param rows
	 * @return
	 */
	public List<T> queryByPage(Integer page,Integer rows,T t);
	// 6. queryOne
	/**
	 * 跟据查询条件，查询一条记录
	 * @param t
	 * @return
	 */
	public T queryOne(T t);
	// 7. save
	/**
	 * 保存数据
	 * @param t
	 */
	public void save(T t);
	
	/**
	 * 保存数据-忽略空字段
	 * @param t
	 */
	public void saveSelective(T t);
	// 8. updateById
	/**
	 * 更新数据
	 * @param t
	 */
	public void updateById(T t);
	
	/**
	 * 更新数据
	 * @param t
	 */
	public void updateByIdSelective(T t);
	// 9. deleteById
	/**
	 * 跟据id删除数据
	 * @param id
	 */
	public void deleteById(Long id);
	// 10. deleteByIds
	/**
	 * 跟据id列表删除数据
	 * @param ids
	 */
	public void deleteByIds(List<Object> ids);

}

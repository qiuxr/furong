package com.rongfu.manager.service;

import java.util.List;

import com.rongfu.manager.pojo.ContentCategory;

public interface ContentCategoryService extends BaseService<ContentCategory> {

	/**
	 * 跟据父id查询内容分类列表
	 * @param parentId
	 * @return
	 */
	List<ContentCategory> queryContentCategoryByParentId(Long parentId);

	/**
	 * 保存分类列表
	 * @param category
	 * @return
	 */
	ContentCategory saveContentCategory(ContentCategory category);

	/**
	 * 删除分类
	 * @param category
	 */
	void deleteContentCategory(ContentCategory category);

}

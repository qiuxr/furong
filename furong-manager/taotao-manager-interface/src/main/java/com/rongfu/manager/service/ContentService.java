package com.rongfu.manager.service;

import java.util.List;
import java.util.Map;

import com.rongfu.common.TaoResult;
import com.rongfu.manager.pojo.Content;

public interface ContentService extends BaseService<Content> {

	/**
	 * 分页查询内容信息
	 * @param page
	 * @param pageSize
	 * @return
	 */
	TaoResult<Content> queryContentByPage(Integer page, Integer pageSize,Content content);

	/**
	 * 跟据类型id查询内容列表
	 * @param tAOTAO_PORTAL_AD
	 * @return
	 */
	String queryContentByCategoryId(Long categoryId);

}

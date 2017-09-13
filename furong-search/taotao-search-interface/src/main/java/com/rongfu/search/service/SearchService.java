package com.rongfu.search.service;

import com.rongfu.common.TaoResult;
import com.rongfu.manager.pojo.Item;

public interface SearchService {

	/**
	 * 分页查询商品数据
	 * @param q 查询条件
	 * @param page 当前页
	 * @param rows 每页查询的记录数
	 * @return
	 */
	TaoResult<Item> search(String q, Integer page, Integer rows);

}

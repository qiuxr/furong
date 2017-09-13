package com.rongfu.manager.service;

import com.rongfu.common.TaoResult;
import com.rongfu.manager.pojo.Item;

/**
 * 商品信息业务逻辑接口
 * @author Steven
 *
 */
public interface ItemService extends BaseService<Item> {

	/**
	 * 保存商品与描述
	 * @param item
	 * @param desc
	 */
	void saveItemAndDesc(Item item, String desc);

	/**
	 * 分页查询商品数据
	 * @param page
	 * @param pageSize
	 * @return
	 */
	TaoResult<Item> queryItemByPage(Integer page, Integer pageSize);

}

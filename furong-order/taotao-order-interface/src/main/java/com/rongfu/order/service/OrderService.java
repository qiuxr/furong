package com.rongfu.order.service;

import com.rongfu.manager.pojo.Order;

/**
 * 订单业务接口
 * @author Steven
 *
 */
public interface OrderService {

	/**
	 * 保存订单
	 * @param order
	 * @return
	 */
	String saveOrder(Order order);

	/**
	 * 跟据id查询订单数据
	 * @param id
	 * @return
	 */
	Order queryOrderById(String id);

}

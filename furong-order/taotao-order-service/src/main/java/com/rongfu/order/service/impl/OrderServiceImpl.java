package com.rongfu.order.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.rongfu.order.jedis.JedisUtils;
import com.rongfu.order.service.OrderService;
import com.rongfu.manager.mapper.OrderItemMapper;
import com.rongfu.manager.mapper.OrderMapper;
import com.rongfu.manager.mapper.OrderShippingMapper;
import com.rongfu.manager.pojo.Order;
import com.rongfu.manager.pojo.OrderItem;
import com.rongfu.manager.pojo.OrderShipping;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private OrderItemMapper orderItemMapper;
	@Autowired
	private OrderShippingMapper shippingMapper;
	
	@Autowired
	private JedisUtils jedisUtils;
	
	@Value("${RONGFU_ORDER_INCR}")
	private String RONGFU_ORDER_INCR;
	
	@Override
	public String saveOrder(Order order) {
		
		Long incr = jedisUtils.incr(RONGFU_ORDER_INCR);
		
		//设置订单号
		order.setOrderId(order.getUserId() + "" + incr);
		order.setStatus(1);
		order.setCreateTime(new Date());
		order.setUpdateTime(order.getCreateTime());
		//保存订单数据
		this.orderMapper.insertSelective(order);
		
		//保存订单商品列表
		for (OrderItem item : order.getOrderItems()) {
			item.setOrderId(order.getOrderId());
			this.orderItemMapper.insertSelective(item);
		}
		
		//保存订单物流信息
		OrderShipping shipping = order.getOrderShipping();
		shipping.setOrderId(order.getOrderId());
		shipping.setCreated(order.getCreateTime());
		shipping.setUpdated(order.getCreateTime());
		this.shippingMapper.insertSelective(shipping);
		
		return order.getOrderId();
	}

	@Override
	public Order queryOrderById(String id) {
		return this.orderMapper.selectByPrimaryKey(id);
	}
	
	
}

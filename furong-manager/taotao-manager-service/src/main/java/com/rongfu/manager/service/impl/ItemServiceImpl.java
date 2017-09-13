package com.rongfu.manager.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rongfu.manager.mapper.ItemDescMapper;
import com.rongfu.manager.mapper.ItemMapper;
import com.rongfu.manager.pojo.Item;
import com.rongfu.manager.pojo.ItemDesc;
import com.rongfu.manager.service.ItemDescService;
import com.rongfu.manager.service.ItemService;
import com.rongfu.common.TaoResult;

@Service
public class ItemServiceImpl extends BaseServiceImpl<Item> implements ItemService {
	
	@Autowired
	private ItemDescService descService;
	
	@Autowired
	private JmsTemplate jmsTemplate;
	@Autowired
	private Destination destination;
	
	private final ObjectMapper mapper = new ObjectMapper();
	
	@Override
	public void saveItemAndDesc(final Item item, String desc) {
		//保存商品信息
		item.setStatus(1);
		this.saveSelective(item);
		
		//保存商品描述
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(item.getId());
		itemDesc.setItemDesc(desc);
		descService.saveSelective(itemDesc);
		
		//发送消息到mq
		jmsTemplate.send(destination,new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage message = new ActiveMQTextMessage();
				Map<String, Object> map = new HashMap<>();
				//消息的类型
				map.put("type", "save");
				//商品的id
				map.put("itemId", item.getId());
				
				try {
					String json = mapper.writeValueAsString(map);
					message.setText(json);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return message;
			}
		});
		
	}
	
	@Override
	public TaoResult<Item> queryItemByPage(Integer page, Integer pageSize) {
		TaoResult<Item> result = new TaoResult<>();
		PageHelper.startPage(page, pageSize);
		//查询商品数据
		List<Item> list = super.queryListByWhere(null);
		//设置查询结果
		result.setRows(list);
		PageInfo<Item> info = new PageInfo<>(list);
		//设置总记录数
		result.setTotal(info.getTotal());
		return result;
	}

}

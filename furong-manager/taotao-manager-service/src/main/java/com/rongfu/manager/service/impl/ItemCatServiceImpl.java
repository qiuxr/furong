package com.rongfu.manager.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rongfu.manager.mapper.ItemCatMapper;
import com.rongfu.manager.pojo.ItemCat;
import com.rongfu.manager.service.ItemCatService;

@Service
public class ItemCatServiceImpl extends BaseServiceImpl<ItemCat> implements ItemCatService{
	
	@Autowired
	private ItemCatMapper itemCatMapper;

	/*@Override
	public List<ItemCat> queryItemCatByPage(Integer page, Integer rows) {
		//设置分页数据
		PageHelper.startPage(page, rows);
		
		List<ItemCat> list = itemCatMapper.select(null);
		return list;
	}*/

}

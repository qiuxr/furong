package com.rongfu.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rongfu.manager.pojo.Item;
import com.rongfu.manager.pojo.ItemDesc;
import com.rongfu.manager.service.ItemDescService;
import com.rongfu.manager.service.ItemService;

@Controller
@RequestMapping("item")
public class ItemController {
	
	@Autowired
	private ItemService itemService;
	
	@Autowired
	private ItemDescService descService;

	//http://item.rongfu.com/item/1474391930.html
	@RequestMapping("{itemId}")
	public String itemDesc(@PathVariable("itemId")Long itemId,Model model){
		//查询商品基本信息
		Item item = this.itemService.queryById(itemId);
		//查询商品详情信息
		ItemDesc itemDesc = this.descService.queryById(itemId);
		
		//返回数据模型
		model.addAttribute("item", item);
		model.addAttribute("itemDesc", itemDesc);
		
		return "item";
		
	}
}

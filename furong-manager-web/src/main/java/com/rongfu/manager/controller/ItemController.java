package com.rongfu.manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rongfu.manager.service.ItemService;
import com.rongfu.common.TaoResult;
import com.rongfu.manager.pojo.Item;

@Controller
@RequestMapping("item")
public class ItemController {
	@Autowired
	private ItemService itemService;
	
	// type: "POST",
	// url: "/rest/item",
	// data: $("#itemAddForm").serialize(),
	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	public String saveItem(Item item,String desc){
		String msg = "0";
		try {
			itemService.saveItemAndDesc(item,desc);
		} catch (Exception e) {
			msg = "1";
			e.printStackTrace();
		}
		return msg;
	}
	
	@RequestMapping(method=RequestMethod.GET)
	@ResponseBody
	public TaoResult<Item> queryItemByPage(@RequestParam(value="page",defaultValue="1")Integer page,@RequestParam("rows")Integer pageSize){
		TaoResult<Item> result = itemService.queryItemByPage(page,pageSize);
		return result;
	}
}

package com.rongfu.manager.controller;

import java.text.DecimalFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rongfu.manager.service.ItemCatService;
import com.rongfu.manager.pojo.ItemCat;

@Controller
@RequestMapping("item/cat")
public class ItemCatController {
	@Autowired
	private ItemCatService itemCatService;

	@RequestMapping("queryItemCatByPage/{page}")
	@ResponseBody
	public List<ItemCat> queryItemCatByPage(@PathVariable("page") Integer page,
			@RequestParam(value = "rows", defaultValue = "2") Integer rows) {
		//return itemCatService.queryItemCatByPage(page, rows);
		return itemCatService.queryByPage(page, rows, null);
	}
	
	//异步树，发起请求，创建树
	// url:'/rest/item/cat',
	// method:'GET',
	// animate:true,
	@RequestMapping(method=RequestMethod.GET)
	@ResponseBody
	public List<ItemCat> queryItemCatByParentId(@RequestParam(value="id",defaultValue="0")Long parentId){
		ItemCat t = new ItemCat();
		t.setParentId(parentId);
		
		List<ItemCat> list = this.itemCatService.queryListByWhere(t);
		
		return list;
	}
	
	public static void main(String[] args) {
		double a = 2.3;
		System.out.println(a * 3);
		
		DecimalFormat format = new DecimalFormat("#.00");
		
		System.out.println(format.format(a * 3));
	}
}

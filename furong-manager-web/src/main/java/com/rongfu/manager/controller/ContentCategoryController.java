package com.rongfu.manager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rongfu.manager.service.ContentCategoryService;
import com.rongfu.manager.pojo.ContentCategory;

@Controller
@RequestMapping("content/category")
public class ContentCategoryController {
	@Autowired
	private ContentCategoryService categoryService;

	// url : '/rest/content/category',
	// animate: true,
	// method : "GET",
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public List<ContentCategory> queryContentCategoryByParentId(
			@RequestParam(value = "id", defaultValue = "0") Long parentId) {
		List<ContentCategory> list = categoryService.queryContentCategoryByParentId(parentId);
		return list;
	}

	/// rest/content/category/add"
	@RequestMapping("add")
	@ResponseBody
	public ContentCategory add(ContentCategory category) {
		category = categoryService.saveContentCategory(category);
		return category;
	}

	// url: "/rest/content/category/update",
	@RequestMapping("update")
	@ResponseBody
	public String update(ContentCategory category) {
		String msg = "0";
		try {
			this.categoryService.updateByIdSelective(category);
		} catch (Exception e) {
			msg = "1";
			e.printStackTrace();
		}
		return msg;
	}
	
	//url: "/rest/content/category/delete"
	@RequestMapping("delete")
	@ResponseBody
	public String delete(ContentCategory category){
		String msg = "0";
		try {
			this.categoryService.deleteContentCategory(category);
		} catch (Exception e) {
			msg = "1";
			e.printStackTrace();
		}
		return msg;
	}
}

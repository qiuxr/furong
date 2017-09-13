package com.rongfu.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.rongfu.common.TaoResult;
import com.rongfu.manager.pojo.Item;
import com.rongfu.search.service.SearchService;

@Controller
@RequestMapping("search")
public class SearchController {

	@Autowired
	private SearchService searchService;

	@Value("${RONGFU_ITEM_LIST_SIZE}")
	private Integer rows;

	@RequestMapping(method = RequestMethod.GET)
	public String search(String q, @RequestParam(value = "page", defaultValue = "1") Integer page, Model model) {

		// 分页搜索商品数据
		TaoResult<Item> result = searchService.search(q, page, rows);

		// 返回商品列表
		model.addAttribute("itemList", result.getRows());
		//计算总页数
		Integer totalPages = result.getTotal() % rows == 0 ? result.getTotal().intValue() / rows
				: (result.getTotal().intValue() / rows) + 1;
		// 返回查询条件
		model.addAttribute("query", q);
		//返回分页数据
		model.addAttribute("page", page);
		model.addAttribute("totalPages", totalPages);
		return "search";
	}
}

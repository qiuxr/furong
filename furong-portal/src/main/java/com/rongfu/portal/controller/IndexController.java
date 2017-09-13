package com.rongfu.portal.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rongfu.manager.service.ContentService;

@Controller
@RequestMapping("index")
public class IndexController {
	
	@Autowired
	private ContentService contentService;
	
	@Value("${TAOTAO_PORTAL_AD}")
	private Long TAOTAO_PORTAL_AD;

	@RequestMapping
	public String toIndex(Model model) {
		//查询大广告
		String ad = contentService.queryContentByCategoryId(TAOTAO_PORTAL_AD);
		//返回大广告数据
		model.addAttribute("AD", ad);
		return "index";
	}
}

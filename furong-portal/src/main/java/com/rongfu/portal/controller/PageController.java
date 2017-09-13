package com.rongfu.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("page")
public class PageController {

	@RequestMapping("{pageName}")
	public String toPage(@PathVariable("pageName")String pageName,String breakUrl,Model model){
		model.addAttribute("breakUrl", breakUrl);
		return pageName;
	}
}

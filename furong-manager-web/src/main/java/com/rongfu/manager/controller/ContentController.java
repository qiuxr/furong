package com.rongfu.manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rongfu.manager.service.ContentService;
import com.rongfu.common.TaoResult;
import com.rongfu.manager.pojo.Content;

@Controller
@RequestMapping("content")
public class ContentController {
	@Autowired
	private ContentService contentService;
	// url:'/rest/content
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public TaoResult<Content> queryContentByPage(@RequestParam(value = "page", defaultValue = "1") Integer page,
			@RequestParam("rows") Integer pageSize,Content content) {
		TaoResult<Content> result = contentService.queryContentByPage(page, pageSize,content);
		return result;
	}
	
	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	public String saveContent(Content content){
		String msg = "0";
		try {
			this.contentService.saveSelective(content);
		} catch (Exception e) {
			msg = "1";
			e.printStackTrace();
		}
		return msg;
	}
}

package com.rongfu.manager.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rongfu.manager.jedis.JedisUtils;
import com.rongfu.manager.mapper.ContentMapper;
import com.rongfu.manager.pojo.Content;
import com.rongfu.manager.service.ContentService;
import com.rongfu.common.TaoResult;

@Service
public class ContentServiceImpl extends BaseServiceImpl<Content> implements ContentService {
	@Autowired
	private ContentMapper contentMapper;
	
	private final ObjectMapper mapper = new ObjectMapper();

	@Override
	public TaoResult<Content> queryContentByPage(Integer page, Integer pageSize,Content content) {
		TaoResult<Content> result = new TaoResult<>();
		PageHelper.startPage(page, pageSize);
		
		//分页查询
		List<Content> list = this.contentMapper.select(content);
		result.setRows(list);
		//获取总记录数
		PageInfo<Content> info = new PageInfo<>(list);
		result.setTotal(info.getTotal());
		
		return result;
	}
	
	@Autowired
	private JedisUtils jedisUtils;
	@Value("${RONGFU_PORTAL_AD}")
	private String RONGFU_PORTAL_AD;

	@Override
	public String queryContentByCategoryId(Long categoryId) {
		
		String json = jedisUtils.get(RONGFU_PORTAL_AD);
		if(StringUtils.isNotBlank(json)){
			return json;
		}
		
		Content t = new Content();
		t.setCategoryId(categoryId);
		//查询数据
		List<Content> list = super.queryListByWhere(t);
		//组装结果集
		List<Map<String, Object>> result = new ArrayList<>();
		
		Map<String, Object> map = null;
		//组装数据
		for (Content content : list) {
			map = new HashMap<>();
			map.put("srcB", content.getPic());
			map.put("height", 240);
			map.put("alt", "");
			map.put("width", 670);
			map.put("src", content.getPic());
			map.put("widthB", 550);
			map.put("href", content.getUrl());
			map.put("heightB", 240);
			result.add(map);
		}
		String string= null;
		try {
			string = mapper.writeValueAsString(result);
			//设置有效时间为1天
			jedisUtils.setAndExpire(RONGFU_PORTAL_AD, string, 60 * 60 * 24);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return string;
	}

}

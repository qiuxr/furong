package com.rongfu.manager.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rongfu.manager.mapper.TestMapper;
import com.rongfu.manager.service.TestService;
@Service
public class TestServiceImpl implements TestService {
	@Autowired
	private TestMapper testMapper;

	@Override
	public String queryDate() {
		return testMapper.queryDate();
	}

}

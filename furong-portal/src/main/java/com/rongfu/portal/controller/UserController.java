package com.rongfu.portal.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rongfu.common.CookieUtils;
import com.rongfu.manager.pojo.User;
import com.rongfu.sso.service.UserService;

@Controller
@RequestMapping("user")
public class UserController {
	@Autowired
	private UserService userService;

	// url : "/user/doRegister.html",
	@RequestMapping("doRegister")
	@ResponseBody
	public Map<String, Object> doRegister(User user) {
		Map<String, Object> map = new HashMap<>();
		try {
			userService.saveUser(user);
			map.put("status", "200");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;

	}

	@Value("${TAOTAO_COOKIES_TICKET}")
	private String TAOTAO_COOKIES_TICKET;

	@RequestMapping("doLogin")
	@ResponseBody
	public Map<String, Object> doLogin(User user, HttpServletRequest request, HttpServletResponse response) {
		 Map<String, Object> map = new HashMap<>();
		// 1. 调用服务进行登录，返回个ticket
		String ticket = this.userService.login(user);
		// 2. ticket不为空说明用户名密码正确，保存cookie并设置有效时间，返回登录成功操作
		if (StringUtils.isNoneBlank(ticket)) {
			//把cookie保存
			CookieUtils.setCookie(request, response, TAOTAO_COOKIES_TICKET, ticket, 60 * 60 * 24, true);
			
			map.put("status", 200);
		}
		// 3. ticket为空说明登录信息不正确返回空
		return map;
	}
}

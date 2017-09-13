package com.rongfu.sso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.rongfu.manager.pojo.User;
import com.rongfu.sso.service.UserService;

@Controller
@RequestMapping("user")
public class UserController {
	@Autowired
	private UserService userService;

	// check/{param}/{type}
	@RequestMapping("check/{param}/{type}")
	public ResponseEntity<String> check(@PathVariable("param") String param, @PathVariable("type") Integer type,String callback) {
		boolean flag = false;
		// 1. 添加方法，接收参数
		// 2. 如果type类型不在1,2,3范围内，侧返回参数无效代码(设置400代码返回)
		if (type == null || type < 1 || type > 3) {
			String back = callback + "(" + flag + ")";
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(back);
		}
		// 3. 查询验证结果并返回，没发生异常设置成功返回
		try {
			flag = this.userService.check(param, type);
			String back = callback + "(" + flag + ")";
			return ResponseEntity.ok(back);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 4. 服务发生异常返回500代码
		String back = callback + "(" + flag + ")";
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(back);
	}

	// http://sso.rongfu.com/user/fe5cb546aeb3ce1bf37abcb08a40493e
	@RequestMapping(value = "{ticket}", method = RequestMethod.GET)
	public ResponseEntity<User> queryUserByTicket(@PathVariable("ticket") String ticket) {
		// 1. 添加方法，接收参数
		try {
			// 2. 调用服务查询数据
			User user = this.userService.queryUserByTicket(ticket);
			if (user != null) {
				// 3. 找到user，没发生异常直接返回ok
				return ResponseEntity.ok(user);
			} else {
				// 4. 如果找不到数据，说明没有登录，返回404错误
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 5. 如果发生异常返回500代码
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
}

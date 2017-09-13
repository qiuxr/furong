package com.rongfu.sso.service;

import com.rongfu.manager.pojo.User;

public interface UserService {

	/**
	 * 检测数据是否可用接口
	 * @param param 要验证的数据
	 * @param type 数据的类型:1、2、3分别代表username、phone、email
	 * @return
	 */
	boolean check(String param, Integer type);

	/**
	 * 跟据ticket查询用户信息
	 * @param ticket
	 * @return
	 */
	User queryUserByTicket(String ticket);

	/**
	 * 注册用户
	 * @param user
	 */
	void saveUser(User user);

	/**
	 * 用户登录
	 * @param user
	 * @return
	 */
	String login(User user);

}

package com.taotao.sso.service.impl;

import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rongfu.manager.mapper.UserMapper;
import com.rongfu.manager.pojo.User;
import com.rongfu.sso.service.UserService;
import com.taotao.sso.jedis.JedisUtils;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserMapper userMapper;

	private final ObjectMapper mapper = new ObjectMapper();

	@Override
	public boolean check(String param, Integer type) {
		User user = new User();
		switch (type) {
		case 1:
			user.setUsername(param);
			break;
		case 2:
			user.setPhone(param);
			break;
		case 3:
			user.setEmail(param);
			break;
		default:
			break;
		}
		boolean flag = false;
		// 检验数据
		int count = this.userMapper.selectCount(user);
		// 如果查询结果为0，说明数据可用
		if (count == 0) {
			flag = true;
		}
		return flag;
	}

	@Autowired
	private JedisUtils jedisUtils;

	@Value("${TAOTAO_SSO_USER_KEY}")
	private String TAOTAO_SSO_USER_KEY;

	@Override
	public User queryUserByTicket(String ticket) {
		User user = null;
		// 1. 从Redis获取用户登录的信息
		String json = jedisUtils.get(TAOTAO_SSO_USER_KEY + ticket);
		// 2. 如果不空说明，已登录
		if (StringUtils.isNotBlank(json)) {
			try {
				// 3. 把json串转换成用户对象
				user = mapper.readValue(json, User.class);
				// 4. 如果用户要检查登录，说说用户是活动状态，这时要重置用户登录有效时间
				this.jedisUtils.expire(TAOTAO_SSO_USER_KEY + ticket, 60 * 30);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 5. 把用户返回
		return user;
	}

	@Override
	public void saveUser(User user) {

		// 把密码md5加密
		user.setPassword(DigestUtils.md5Hex(stringToHexString(user.getPassword())));
		// 初始化时间
		user.setCreated(new Date());
		user.setUpdated(user.getCreated());

		this.userMapper.insertSelective(user);
	}

	/**
	 * 字符串转换为16进制字符串
	 * 
	 * @param s
	 * @return
	 */
	public static String stringToHexString(String s) {
		String str = "";
		for (int i = 0; i < s.length(); i++) {
			int ch = (int) s.charAt(i);
			String s4 = Integer.toHexString(ch);
			str = str + s4;
		}
		return str;
	}

	/**
	 * 16进制字符串转换为字符串
	 * 
	 * @param s
	 * @return
	 */
	public static String hexStringToString(String s) {
		if (s == null || s.equals("")) {
			return null;
		}
		s = s.replace(" ", "");
		byte[] baKeyword = new byte[s.length() / 2];
		for (int i = 0; i < baKeyword.length; i++) {
			try {
				baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			s = new String(baKeyword, "gbk");
			new String();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return s;
	}
	
	@Value("${TAOTAO_SSO_LONGIN_INCR}")
	private String TAOTAO_SSO_LONGIN_INCR;

	@Override
	public String login(User user) {
		// 1. 设置密码加密，因为数据是用md5加密的,根据用户名和密码查询用户
		user.setPassword(DigestUtils.md5Hex(stringToHexString(user.getPassword())));
		//查询用户
		User login = this.userMapper.selectOne(user);
		// 2. 判断用户是否为空，如果不为空表示登录成功
		if(login != null){
			try {
				// 3. 生成Redis中的ticket的key可是使用redis的唯一数(incr)+用户id，注意需要前缀
				String ticket = this.jedisUtils.incr(TAOTAO_SSO_LONGIN_INCR) + ""  + login.getId();
				// 4.
				// 把ticket和用户数据放到redis中,模拟session，原来的session有效时间是半小时(参考单点登录接口中的-通过ticket查询用户信息)
				String json = this.mapper.writeValueAsString(login);
				this.jedisUtils.setAndExpire(TAOTAO_SSO_USER_KEY + ticket, json, 60 * 30);
				// 5. 返回ticket
				return ticket;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 6. 如果用户为空，返回null
		return null;
	}
}

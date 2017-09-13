package com.rongfu.order.jedis.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.rongfu.order.jedis.JedisUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 单机版Jedis实现
 * @author Steven
 *
 */
public class MyPool implements JedisUtils {
	
	@Autowired
	private JedisPool jedisPool;

	@Override
	public String set(String key, String value) {
		Jedis jedis = jedisPool.getResource();
		String set = jedis.set(key, value);
		jedis.close();
		return set;
	}

	@Override
	public String get(String key) {
		Jedis jedis = jedisPool.getResource();
		String get = jedis.get(key);
		jedis.close();
		return get;
	}

	@Override
	public Long del(String key) {
		Jedis jedis = jedisPool.getResource();
		Long del = jedis.del(key);
		jedis.close();
		return del;
	}

	@Override
	public Long expire(String key, int seconds) {
		Jedis jedis = jedisPool.getResource();
		Long exp = jedis.expire(key, seconds);
		jedis.close();
		return exp;
	}

	@Override
	public void setAndExpire(String key, String value, int seconds) {
		Jedis jedis = jedisPool.getResource();
		//保存数据
		jedis.set(key, value);
		//设置有效时间
		jedis.expire(key, seconds);
		jedis.close();

	}

	@Override
	public Long incr(String key) {
		Jedis jedis = jedisPool.getResource();
		Long incr = jedis.incr(key);
		jedis.close();
		return incr;
	}

}

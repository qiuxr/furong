package com.taotao.sso.jedis.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.taotao.sso.jedis.JedisUtils;

import redis.clients.jedis.JedisCluster;

/**
 * 集群版Jedis实现
 * @author Steven
 *
 */
public class MyJedisCluster implements JedisUtils {
	@Autowired
	private JedisCluster cluster;

	@Override
	public String set(String key, String value) {
		String set = cluster.set(key, value);
		return set;
	}

	@Override
	public String get(String key) {
		String get = cluster.get(key);
		return get;
	}

	@Override
	public Long del(String key) {
		Long del = cluster.del(key);
		return del;
	}

	@Override
	public Long expire(String key, int seconds) {
		Long exp = cluster.expire(key, seconds);
		return exp;
	}

	@Override
	public void setAndExpire(String key, String value, int seconds) {
		//保存数据
		cluster.set(key, value);
		//设置有效时间
		cluster.expire(key, seconds);
	}

	@Override
	public Long incr(String key) {
		Long incr = cluster.incr(key);
		return incr;
	}

}

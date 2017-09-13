package com.rongfu.manager.jedis;

public interface JedisUtils {
	/**
	 * 保存数据
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	String set(String key, String value);

	/**
	 * 根据key查询数据
	 * 
	 * @param key
	 * @return
	 */
	String get(String key);

	/**
	 * 删除数据
	 * 
	 * @param key
	 * @return
	 */
	Long del(String key);

	/**
	 * 根据key与秒数设置生存时间，expire指令
	 * 
	 * @param key
	 * @param seconds
	 * @return
	 */
	Long expire(String key, int seconds);

	/**
	 * 保存并设置生存时间
	 * @param key
	 * @param value
	 * @param seconds
	 */
	void setAndExpire(String key, String value, int seconds);
	/**
	 * 跟据key自动统计数据(incr指令vaule加1)
	 * 
	 * @param key
	 * @return
	 */
	Long incr(String key);

}

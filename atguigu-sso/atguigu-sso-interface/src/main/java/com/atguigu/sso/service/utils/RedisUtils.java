package com.atguigu.sso.service.utils;

public interface RedisUtils {

	//保存
	public String set(String key,String value);
	
	//根据key查询
	public String get(String key);
	
	
	//删除del
	public Long del(String key);
	
	
	//设置key生存时间
	public Long expire(String key, Integer time);
	
	//设置一个值并设置生存时间
	public Long set(String key,String value,Integer time);
	
	//值加1
	public Long incr(String key);
	
}

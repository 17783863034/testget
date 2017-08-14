package com.atguigu.sso.service.impl.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import com.atguigu.sso.service.utils.RedisUtils;

@Service
public class RedisUtilsImpl implements RedisUtils {

	//用到的时候再注入required=false
	@Autowired (required=false)
	private ShardedJedisPool shardedJedisPool;
	
	public <T> T excute(Function<ShardedJedis, T> fun){
		ShardedJedis shardedJedis = null;
		try {
			// 从连接池中获取到jedis分片对象
			shardedJedis = shardedJedisPool.getResource();

			return fun.callback(shardedJedis);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != shardedJedis) {
				// 关闭，检测连接是否有效，有效则放回到连接池中，无效则重置状态
				shardedJedis.close();
			}
		}
		return null;
	}

	@Override
	public String set(final String key, final String value) {
		return excute(new Function<ShardedJedis, String>() {
			@Override
			public String callback(ShardedJedis e) {
				return e.set(key, value);
			}
		});
	}

	@Override
	public String get(final String key) {
		return excute(new Function<ShardedJedis, String>() {
			@Override
			public String callback(ShardedJedis e) {
				return e.get(key);
			}
		});
	}

	@Override
	public Long del(final String key) {
		return excute(new Function<ShardedJedis, Long>() {
			@Override
			public Long callback(ShardedJedis e) {
				return e.del(key);
			}
		});
	}

	@Override
	public Long expire(final String key,final Integer seconds) {
		return excute(new Function<ShardedJedis, Long>() {
			@Override
			public Long callback(ShardedJedis e) {
				return e.expire(key, seconds);
			}
		});
	}

	@Override
	public Long set(final String key,final String value,final Integer seconds) {
		return  excute(new Function<ShardedJedis, Long>() {
			@Override
			public Long callback(ShardedJedis e) {
				e.set(key, value);
				return e.expire(key, seconds);
			}
		});
	}

	@Override
	public Long incr(final String key) {
		return excute(new Function<ShardedJedis, Long>() {
			@Override
			public Long callback(ShardedJedis e) {
				return e.incr(key);
			}
		});
	}
	

	

}

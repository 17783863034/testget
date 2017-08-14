package com.atguigu.sso.service.impl;

import java.io.IOException;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atguigu.mapper.UserMapper;
import com.atguigu.pojo.User;
import com.atguigu.sso.service.UserService;
import com.atguigu.sso.service.utils.RedisUtils;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private RedisUtils redisUtils;
	
	private static final ObjectMapper MAPPER = new ObjectMapper();

	/**
	 * 检查用户是否合法
	 */
	@Override
	public Boolean check(String param, Integer type){
		
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
		int count = this.userMapper.selectCount(user);
		
		if (count>0) {
			//用户否在
			return false;
		}else{
			//可以注册
			return true;
		}
	}

	/**
	 * 用户登录
	 * @throws Exception 
	 */
	@Override
	public String login(String username, String password) throws Exception {
		User user = new User();
		user.setUsername(username);
		User user1 = this.userMapper.selectOne(user);
		if (!StringUtils.equals(user1.getPassword(), DigestUtils.md5Hex(password))) {
			//登录失败
			return null;
		}
		
		String ticket = DigestUtils.md5Hex(System.currentTimeMillis()+username);
		
		this.redisUtils.set(ticket, MAPPER.writeValueAsString(user1), 1800);
		
		return ticket;
	}

	/**
	 * 通过ticket查询用户信息
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	@Override
	public User queryUserByTicket(String ticket) throws Exception {
		String string = this.redisUtils.get(ticket);
		
		if (StringUtils.isNoneBlank(string)) {
			//用户已经登录
			User user =MAPPER.readValue(string, User.class);
			this.redisUtils.expire(ticket, 1800);
			return user;
		}
		return null;
	}

	@Override
	public void register(User user) {
		//数据校验...
		
		this.userMapper.insertSelective(user);
	}

	
	
	
}

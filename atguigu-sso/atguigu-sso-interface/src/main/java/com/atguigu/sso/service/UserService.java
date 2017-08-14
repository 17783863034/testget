package com.atguigu.sso.service;

import com.atguigu.pojo.User;

public interface UserService {

	public Boolean check(String param, Integer type);

	public String login(String username, String password)  throws Exception;

	public User queryUserByTicket(String ticket)  throws Exception;

	public void register(User user);

}

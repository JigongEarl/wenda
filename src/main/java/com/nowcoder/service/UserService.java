package com.nowcoder.service;

import java.util.Map;

import com.nowcoder.model.User;

public interface UserService {
	
	public User getUser(int id);
	
	public Map<String, Object> login(User user);

	public Map<String, Object> register(User user);

	public void logout(String ticket);

	public User getUserByName(String name);
}

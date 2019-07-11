package com.nowcoder.model;

import org.springframework.stereotype.Component;

/**
 * 线程独占的用户信息封装类
 * 用于在属于同一线程的访问连链中共享用户信息
 * @author 86156
 *
 */
@Component  //交由spring容器管理
public class HostHolder {
	//线程独占的ThreadLocal变量，每个变量可以存放一个线程独占的User对象
	private static ThreadLocal<User> users = new ThreadLocal<>();

	public User getUser() {
		return users.get();
	}

	public void setUser(User user) {
		users.set(user);
	}

	public String toString() {
		return "HostHolder [users = " + getUser() + "]";
	}

	public void clear() {
		users.remove();
	}
}

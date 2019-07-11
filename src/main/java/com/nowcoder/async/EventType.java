package com.nowcoder.async;
/**
 * 异步队列-->事件类型
 * @author 86156
 *
 */
public enum EventType {
	LIKE(0), COMMENT(1), LOGIN(2), MAIL(3), FOLLOW(4), UNFOLLOW(5);
	
	private int value;
	
	private EventType(int value) {  //枚举类的构造函数需要私有化，每一个枚举值可以看作是枚举类基于该构造函数的实例对象
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
}

package com.nowcoder.async;

import java.util.List;

/**
 * 异步队列-->事件处理器模板
 * @author 86156
 *
 */
public interface EventHandler {
	
	//处理事件
	void doHandle(EventModel model);
	
	//获取该处理器支持处理的事件类型
	List<EventType> getSupportEventTypes();
}

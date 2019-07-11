package com.nowcoder.async;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nowcoder.dao.JedisDAO;
import com.nowcoder.util.JsonUtils;
import com.nowcoder.util.RedisKeyUtils;
/**
 * 异步队列-->事件生产者
 * 
 * 
 * 将待处理事件放入到redis的队列数据结构中
 * @author 86156
 *
 */
@Service
public class EventProducer {
	
	@Autowired
	private JedisDAO jedisDAO;
	
	public boolean fireEvent(EventModel model) {
		try {
			String json = JsonUtils.objectToJson(model);
			String key = RedisKeyUtils.getEventQueueKey();
			jedisDAO.lpush(key, json);  //事件入队
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}

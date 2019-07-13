package com.nowcoder.async;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.nowcoder.dao.JedisDAO;
import com.nowcoder.util.JsonUtils;
import com.nowcoder.util.RedisKeyUtils;
/**
 * 异步队列-->事件处理类
 * 
 * 实现了ApplicationContextAware接口：会在spring启动并加载配置文件时，会自动调用setApplicationContext方法
 * 实现了InitializingBean接口，Spring在初始化bean时，会自动调用afterPropertiesSet方法
 * @author 86156
 *
 */
@Service
public class EventConsumer implements  ApplicationContextAware, InitializingBean{
	
	private static final Logger logger = Logger.getLogger(EventConsumer.class);
	
	//应用上下文对象
	private ApplicationContext applicationContext;
	
	//按时间类型分类存放所有事件处理器的容器   key:事件类型  value：支持该事件类型的所有事件处理器
	private Map<EventType, List<EventHandler>> config = new HashMap<>();

	@Autowired
	private JedisDAO jedisDAO;
	
	
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	
	public void afterPropertiesSet() throws Exception {
		//获取所有事件处理器(其实就是EventHandler的所有实现类)
		Map<String, EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
		
		if(null != beans) {
			//遍历所有Handler，将Handler按照支持的事件类型分类，存入config对象
			for(Map.Entry<String, EventHandler> entry : beans.entrySet()) {
				List<EventType> eventTypes = entry.getValue().getSupportEventTypes();
				
				for(EventType type : eventTypes) {
					if(!config.containsKey(type)) {
						config.put(type, new ArrayList<EventHandler>());
					}
					config.get(type).add(entry.getValue());
				}
			}
		}
		
		//新线程处理异步队列（redis实现）中的事件
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					String key = RedisKeyUtils.getEventQueueKey();
					List<String> events = jedisDAO.brpop(0, key); //获取队列最后一个元素，0 阻塞时间
				
					for(String message : events) {
						if(message.equals(key)) {  //跳过表示key的元素
							continue;
						}
						
						EventModel model = JsonUtils.jsonToObject(message, EventModel.class);
						//System.out.println(model);
						if(!config.containsKey(model.getType())) {
							logger.error("不能识别的事件");
							continue;
						}
						
						for(EventHandler handler : config.get(model.getType())) {
							handler.doHandle(model);
						}
					}
				}
			}
			
		});
		thread.start();
	}
}

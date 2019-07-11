package com.nowcoder.async.handler;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nowcoder.async.EventHandler;
import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventType;
import com.nowcoder.model.Message;
import com.nowcoder.model.User;
import com.nowcoder.service.MessageService;
import com.nowcoder.service.UserService;
/**
 * 点赞通知 异步处理器
 * @author 86156
 *
 */
@Component
public class LikeEventHandler implements EventHandler {
	
	@Autowired
	private MessageService messageServiceImpl;
	@Autowired
	private UserService userServiceImpl;
	
	public void doHandle(EventModel model) {
		Message msg = new Message();
		msg.setFromId(model.getActorId());
		msg.setToId(model.getEntityOwnerId());
		msg.setCreatedDate(new Date());
		User user = userServiceImpl.getUser(model.getActorId());
		msg.setContent("用户" + user.getName() + 
				"点赞了你的评论，http://localhost:8080/question/" + model.getExt("questionId"));
		
		messageServiceImpl.addMessage(msg);
	}

	public List<EventType> getSupportEventTypes() {
		return Arrays.asList(EventType.LIKE);  //数组(此处为一个元素)转为list
	}

}

package com.nowcoder.async.handler;

import com.nowcoder.async.EventHandler;
import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventType;
import com.nowcoder.dao.JedisDAO;
import com.nowcoder.model.*;
import com.nowcoder.service.*;
import com.nowcoder.util.JsonUtils;
import com.nowcoder.util.RedisKeyUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 新鲜事推模式处理器
 * @author 86156
 *
 */
@Component
public class FeedHandler implements EventHandler {
	@Autowired
	FollowService followServiceImpl;

	@Autowired
	UserService userServiceImpl;

	@Autowired
	FeedService feedServiceImpl;

	@Autowired
	QuestionService questionServiceImpl;

	@Autowired
	JedisDAO jedisDAO;

	private String buildFeedData(EventModel model) {
		Map<String, String> map = new HashMap<String, String>();
		// 触发用户是通用的
		User actor = userServiceImpl.getUser(model.getActorId());
		if (actor == null) {
			return null;
		}
		map.put("userId", String.valueOf(actor.getId()));
		map.put("userHead", actor.getHeadUrl());
		map.put("userName", actor.getName());
		//map.put("createdDate", model.ge)
		if (model.getType() == EventType.COMMENT
				|| (model.getType() == EventType.FOLLOW && model.getEntityType() == EntityType.ENTITY_QUESTION)) {
			Question question = questionServiceImpl.selQuesById(model.getEntityId());
			if (question == null) {
				return null;
			}
			map.put("questionId", String.valueOf(question.getId()));
			map.put("questionTitle", question.getTitle());
			return JsonUtils.objectToJson(map);
		}
		return null;
	}

	@Override
	public void doHandle(EventModel model) {
		// 为了测试，把model的userId随机一下
		//model.setActorId(1 + new Random().nextInt(10));

		// 构造一个新鲜事
		Feed feed = new Feed();
		feed.setCreatedDate(new Date());
		feed.setType(model.getType().getValue());
		feed.setUserId(model.getActorId());
		feed.setData(buildFeedData(model));
		if (feed.getData() == null) {
			// 不支持的feed
			return;
		}
		System.out.println("555"+feed);
		feedServiceImpl.addFeed(feed);

		// 获得所有粉丝
		List<Integer> followers = followServiceImpl.getFollowers(EntityType.ENTITY_USER, model.getActorId(),
				Integer.MAX_VALUE);
		// 系统队列
		//followers.add(0);
		// 给所有粉丝推事件
		for (int follower : followers) {
			String timelineKey = RedisKeyUtils.getTimelineKey(follower);
			jedisDAO.lpush(timelineKey, String.valueOf(feed.getId()));
			// 限制最长长度，如果timelineKey的长度过大，就删除后面的新鲜事
		}
	}

	@Override
	public List<EventType> getSupportEventTypes() {
		return Arrays.asList(new EventType[] { EventType.COMMENT, EventType.FOLLOW });
	}
}

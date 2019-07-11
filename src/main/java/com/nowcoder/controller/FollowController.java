package com.nowcoder.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventProducer;
import com.nowcoder.async.EventType;
import com.nowcoder.model.EntityType;
import com.nowcoder.model.HostHolder;
import com.nowcoder.model.Question;
import com.nowcoder.model.User;
import com.nowcoder.model.ViewObject;
import com.nowcoder.model.WendaResult;
import com.nowcoder.service.CommentService;
import com.nowcoder.service.FollowService;
import com.nowcoder.service.QuestionService;
import com.nowcoder.service.UserService;
import com.nowcoder.util.JsonUtils;

/**
 * 关注/取消关注 控制层
 * @author 86156
 *
 */
@Controller
public class FollowController {
	@Autowired
	FollowService followServiceImpl;

	@Autowired
	CommentService commentServiceImpl;

	@Autowired
	QuestionService questionServiceImpl;

	@Autowired
	UserService userServiceImpl;

	@Autowired
	HostHolder hostHolder;

	@Autowired
	EventProducer eventProducer;

	/**
	 * 关注用户
	 * 
	 * @param userId
	 * @return
	 */
	@RequestMapping(path = { "/followUser" }, method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public String followUser(@RequestParam("userId") int userId) {
		WendaResult result = new WendaResult();
		// 先登录
		if (hostHolder.getUser() == null) {
			result.setCode(999);
			return JsonUtils.objectToJson(result);
		}

		boolean ret = followServiceImpl.follow(hostHolder.getUser().getId(), EntityType.ENTITY_USER, userId);
		
		//加入异步处理队列
		eventProducer.fireEvent(new EventModel(EventType.FOLLOW).setActorId(hostHolder.getUser().getId())
				.setEntityId(userId).setEntityType(EntityType.ENTITY_USER).setEntityOwnerId(userId));

		// 返回关注的人数
		result.setCode(ret ? 0 : 1);
		return JsonUtils.objectToJson(result);
	}
	
	
	/**
	 * 取消关注用户
	 * @param userId
	 * @return
	 */
	@RequestMapping(path = { "/unfollowUser" }, method = { RequestMethod.POST })
	@ResponseBody
	public String unfollowUser(@RequestParam("userId") int userId) {
		WendaResult result = new WendaResult();
		// 先登录
		if (hostHolder.getUser() == null) {
			result.setCode(999);
			return JsonUtils.objectToJson(result);
		}

		boolean ret = followServiceImpl.unfollow(hostHolder.getUser().getId(), EntityType.ENTITY_USER, userId);
		
		//加入异步处理队列
		eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW).setActorId(hostHolder.getUser().getId())
				.setEntityId(userId).setEntityType(EntityType.ENTITY_USER).setEntityOwnerId(userId));

		// 返回关注的人数
		result.setCode(ret ? 0 : 1);
		return JsonUtils.objectToJson(result);
	}
	

	/**
	 * 关注问题
	 * @param questionId
	 * @return
	 */
	@RequestMapping(path = { "/followQuestion" }, method = { RequestMethod.POST })
	@ResponseBody
	public String followQuestion(@RequestParam("questionId") int questionId) {
		WendaResult result = new WendaResult();
		// 先登录
		if (hostHolder.getUser() == null) {
			result.setCode(999);
			return JsonUtils.objectToJson(result);
		}
		//判断问题是否存在
		Question ques = questionServiceImpl.selQuesById(questionId);
		if (ques == null) {
			result.setCode(1);
			result.setMsg("问题不存在");
			return JsonUtils.objectToJson(result);
		}
		
		boolean ret = followServiceImpl.follow(hostHolder.getUser().getId(),
												EntityType.ENTITY_QUESTION, questionId);
		//发送站内信通知
		eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
				   .setActorId(hostHolder.getUser().getId()).setEntityId(questionId)
				   .setEntityType(EntityType.ENTITY_QUESTION).setEntityOwnerId(ques.getUserId()));
		//封装结果
		Map<String, Object> info = new HashMap<>();
		info.put("headUrl", hostHolder.getUser().getHeadUrl());
		info.put("name", hostHolder.getUser().getName());
		info.put("id", hostHolder.getUser().getId());
		info.put("count", followServiceImpl.getFollowerCount(EntityType.ENTITY_QUESTION, questionId));
		result.setCode(ret ? 0 : 1);
		result.setMsg(ret ? "关注问题成功" : "关注问题失败");
		result.setData(info);
		
		return JsonUtils.objectToJson(result);
	}
	
	
	/**
	 * 取消关注问题
	 * @param questionId
	 * @return
	 */
	@RequestMapping(path = { "/unfollowQuestion" }, method = { RequestMethod.POST })
	@ResponseBody
	public String unfollowQuestion(@RequestParam("questionId") int questionId) {
		WendaResult result = new WendaResult();
		// 先登录
		if (hostHolder.getUser() == null) {
			result.setCode(999);
			return JsonUtils.objectToJson(result);
		}
		//判断问题是否存在
		Question ques = questionServiceImpl.selQuesById(questionId);
		if (ques == null) {
			result.setCode(1);
			result.setMsg("问题不存在");
			return JsonUtils.objectToJson(result);
		}

		boolean ret = followServiceImpl.unfollow(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION, questionId);
		//发送站内信通知
		eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW).setActorId(hostHolder.getUser().getId())
				.setEntityId(questionId).setEntityType(EntityType.ENTITY_QUESTION).setEntityOwnerId(ques.getUserId()));
		//封装结果
		Map<String, Object> info = new HashMap<>();
		info.put("id", hostHolder.getUser().getId());
		info.put("count", followServiceImpl.getFollowerCount(EntityType.ENTITY_QUESTION, questionId));
		result.setCode(ret ? 0 : 1);
		result.setMsg(ret ? "关注问题成功" : "关注问题失败");
		result.setData(info);
		return JsonUtils.objectToJson(result);
	}
	
	
	/**
	 * 显示某用户的所有粉丝
	 * @param model
	 * @param userId
	 * @return
	 */
	@RequestMapping(path = { "/user/{uid}/followers" }, method = { RequestMethod.GET })
	public String followers(Model model, @PathVariable("uid") int userId) {
		List<Integer> followerIds = followServiceImpl.getFollowers(EntityType.ENTITY_USER, userId, 0, 10);
		if (hostHolder.getUser() != null) {
			model.addAttribute("followers", getUsersInfo(hostHolder.getUser().getId(), followerIds));
		} else {
			model.addAttribute("followers", getUsersInfo(0, followerIds));
		}
		model.addAttribute("followerCount", followServiceImpl.getFollowerCount(EntityType.ENTITY_USER, userId));
		model.addAttribute("curUser", userServiceImpl.getUser(userId));
		return "followers";
	}
	
	
	/**
	 * 显示某用户关注的所有用户
	 * @param model
	 * @param userId
	 * @return
	 */
	@RequestMapping(path = { "/user/{uid}/followees" }, method = { RequestMethod.GET })
	public String followees(Model model, @PathVariable("uid") int userId) {
		List<Integer> followeeIds = followServiceImpl.getFollowees(userId, EntityType.ENTITY_USER, 0, 10);

		if (hostHolder.getUser() != null) {
			model.addAttribute("followees", getUsersInfo(hostHolder.getUser().getId(), followeeIds));
		} else {
			model.addAttribute("followees", getUsersInfo(0, followeeIds));
		}
		model.addAttribute("followeeCount", followServiceImpl.getFolloweeCount(userId, EntityType.ENTITY_USER));
		model.addAttribute("curUser", userServiceImpl.getUser(userId));
		return "followees";
	}
	
	
	private List<ViewObject> getUsersInfo(int localUserId, List<Integer> userIds) {
		List<ViewObject> userInfos = new ArrayList<ViewObject>();
		for (Integer uid : userIds) {
			User user = userServiceImpl.getUser(uid);
			if (user == null) {
				continue;
			}
			ViewObject vo = new ViewObject();
			vo.set("user", user);
			vo.set("commentCount", commentServiceImpl.getUserCommentCount(uid));
			vo.set("followerCount", followServiceImpl.getFollowerCount(EntityType.ENTITY_USER, uid));
			vo.set("followeeCount", followServiceImpl.getFolloweeCount(uid, EntityType.ENTITY_USER));
			if (localUserId != 0) {
				vo.set("followed", followServiceImpl.isFollower(localUserId, EntityType.ENTITY_USER, uid));
			} else {
				vo.set("followed", false);
			}
			userInfos.add(vo);
		}
		return userInfos;
	}
}

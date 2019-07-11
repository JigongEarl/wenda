package com.nowcoder.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventProducer;
import com.nowcoder.async.EventType;
import com.nowcoder.model.Comment;
import com.nowcoder.model.EntityType;
import com.nowcoder.model.HostHolder;
import com.nowcoder.model.WendaResult;
import com.nowcoder.service.CommentService;
import com.nowcoder.service.LikeService;
import com.nowcoder.util.JsonUtils;

@Controller
public class LikeController {
	private static final Logger logger = Logger.getLogger(LikeController.class);
	
	@Autowired
	private LikeService likeServiceImpl;
	@Autowired
	private CommentService commentServiceImpl;
	@Autowired
	private HostHolder hostHolder;
	@Autowired
	private EventProducer eventProducer;
	
	/**
	 * 点赞
	 * @param id
	 * @return
	 */
	@RequestMapping(path = {"/like"}, method = {RequestMethod.POST})
	@ResponseBody
	public String like(@RequestParam("commentId") int commentId) {
		
		WendaResult result = new WendaResult();
		Comment comment = commentServiceImpl.selCommentById(commentId);
		
		//先登录
		if (hostHolder.getUser() == null) {
			result.setCode(999);
            return JsonUtils.objectToJson(result);
        }
		
		//点赞的对象是评论，所以注意实体类型为ENTITY_COMMENT
		long like = likeServiceImpl.like(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, commentId);
		result.setMsg(String.valueOf(like));
		
		//发送站内信通知有人点赞(异步队列实现)
		EventModel model = new EventModel(EventType.LIKE);
		model.setActorId(hostHolder.getUser().getId())
   			 .setEntityId(commentId)
		     .setEntityType(EntityType.ENTITY_COMMENT)
			 .setEntityOwnerId(comment.getUserId())
			 .setExt("questionId", String.valueOf(comment.getEntityId()));
		eventProducer.fireEvent(model);
		
		return JsonUtils.objectToJson(result);
	}
	
	/**
	 * 踩赞
	 * @param id
	 * @return
	 */
	@RequestMapping(path = {"/dislike"}, method = {RequestMethod.POST})
	@ResponseBody
	public String disLike(@RequestParam("commentId") int commentId) {
	
		WendaResult result = new WendaResult();
		
		//先登录
		if (hostHolder.getUser() == null) {
			result.setCode(999);
            return JsonUtils.objectToJson(result);
        }
		
		//处理踩赞信息
		long disLike = likeServiceImpl.disLike(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, commentId);
		result.setLikeCount(disLike);
		result.setMsg(String.valueOf(disLike));
		
		return JsonUtils.objectToJson(result);
	}
	
	
}

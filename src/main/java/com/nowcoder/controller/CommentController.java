package com.nowcoder.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.HtmlUtils;

import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventProducer;
import com.nowcoder.async.EventType;
import com.nowcoder.model.Comment;
import com.nowcoder.model.EntityType;
import com.nowcoder.model.HostHolder;
import com.nowcoder.service.CommentService;
import com.nowcoder.service.QuestionService;
import com.nowcoder.service.impl.SensitiveServiceImpl;

@Controller
public class CommentController {
	private static final Logger logger = Logger.getLogger(CommentController.class);
	@Autowired
	private CommentService commentServiceImpl;
	@Autowired
	private HostHolder hostHolder;
	@Autowired
	private SensitiveServiceImpl sensitiveServiceImpl;
	@Autowired
	private QuestionService questionServiceImpl;
	@Autowired
	private EventProducer eventProducer;
	
	/**
	 * 对某一问题添加评论
	 * @param content
	 * @param questionId
	 * @param request
	 * @return
	 */
	@RequestMapping(path= {"/addComment"}, method = {RequestMethod.POST})
	public String addComment(@RequestParam("content")String content,
						     @RequestParam("questionId") int questionId,
						     HttpServletRequest request) {
		try {
			//先登录
			if(hostHolder.getUser() == null) {
				return "redirect:/reglogin?next=" + request.getRequestURI();
			}
			
			//防止脚本注入和过滤敏感词
			content = HtmlUtils.htmlEscape(content);
			content = sensitiveServiceImpl.filter(content);
			
			//封装Content对象
			Comment comment = new Comment();
			comment.setUserId(hostHolder.getUser().getId());
			comment.setContent(content);
			comment.setEntityId(questionId);
			comment.setEntityType(EntityType.ENTITY_QUESTION);  //评论的对象类型，此处表示是对问题的评论
			comment.setCreatedDate(new Date());
			comment.setStatus(0);
			commentServiceImpl.addComment(comment);
			
			//更新Message对象中的评论数量
			int count = commentServiceImpl.getCommentCount(comment.getEntityId(), comment.getEntityType());
			questionServiceImpl.updCommentCount(comment.getEntityId(), count);
		
			//添加新鲜事通知
			eventProducer.fireEvent(new EventModel(EventType.COMMENT)
							.setActorId(hostHolder.getUser().getId()).setEntityId(questionId));
			
		} catch (Exception e) {
			logger.error("增加评论失败：" + e.getMessage());
			e.printStackTrace();
		}
		return "redirect:/question/" + String.valueOf(questionId);
	}
	
	
}

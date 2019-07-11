package com.nowcoder.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nowcoder.model.Comment;
import com.nowcoder.model.EntityType;
import com.nowcoder.model.HostHolder;
import com.nowcoder.model.Question;
import com.nowcoder.model.User;
import com.nowcoder.model.ViewObject;
import com.nowcoder.model.WendaResult;
import com.nowcoder.service.CommentService;
import com.nowcoder.service.FollowService;
import com.nowcoder.service.LikeService;
import com.nowcoder.service.QuestionService;
import com.nowcoder.service.SearchService;
import com.nowcoder.service.UserService;
import com.nowcoder.service.impl.FollowServiceImpl;
import com.nowcoder.service.impl.SearchServiceImpl;
import com.nowcoder.util.JsonUtils;

@Controller
public class QuestionController {
	private static Logger logger = Logger.getLogger(QuestionController.class);

	@Autowired
	private QuestionService questionServiceImpl;
	@Autowired
	private UserService userServiceImpl;
	@Autowired
	private CommentService commentServiceImpl;
	@Autowired
	private LikeService likeServiceImpl;
	@Autowired
	private FollowService followServiceImpl;
	@Autowired
	private SearchServiceImpl SearchServiceImpl;
	@Autowired
	HostHolder hostHolder;

	/**
	 * 提问
	 * 
	 * @param title
	 * @param content
	 * @param model
	 * @return
	 */
	@RequestMapping("/question/add")
	@ResponseBody
	public String addQuestion(@RequestParam("title") String title, @RequestParam("content") String content,
			Model model) {
		// 结果对象
		WendaResult result = new WendaResult();
		
		//封装数据
		Question ques = new Question();
		ques.setTitle(title);
		ques.setContent(content);
		ques.setCreatedDate(new Date());
		ques.setCommentCount(0);
		
		// 判断是已登录客户还是匿名客户
		if (hostHolder.getUser() != null) {
			ques.setUserId(hostHolder.getUser().getId());
			try {
				int row = questionServiceImpl.addQuestion(ques);
				SearchServiceImpl.add(ques);
				if (row > 0) {
					result.setCode(0);
					result.setMsg("提问成功！");
				}
			} catch (Exception e) {
				result.setCode(1);
				result.setMsg("系统错误，请重试");
				logger.error("数据库新增失败" + e.getMessage());
			}
		} else {
			result.setCode(999);
			result.setMsg("用户未登录");
		}

		return JsonUtils.objectToJson(result);
	}

	/**
	 * 显示具体某一问题(包括评论和关注人)
	 * 
	 * @param model
	 * @param pid
	 * @return
	 */
	@RequestMapping("/question/{qid}")
	public String questionDetail(Model model, @PathVariable("qid") int qid) {

		// 问题显示
		Question question = questionServiceImpl.selQuesById(qid);
		model.addAttribute("question", question);

		// “关注问题”显示
		if (hostHolder.getUser() != null) {
			model.addAttribute("followed",
					followServiceImpl.isFollower(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION, qid));
		} else {
			model.addAttribute("followed", false);
		}

		// 关注人显示
		List<User> followUsers = new ArrayList<>();
		List<Integer> followers = followServiceImpl.getFollowers(EntityType.ENTITY_QUESTION, qid, -1);
		for (int follower : followers) {
			followUsers.add(userServiceImpl.getUser(follower));
		}
		model.addAttribute("followUsers", followUsers);

		// 评论显示
		List<Comment> commentList = commentServiceImpl.selByEntity(qid, EntityType.ENTITY_QUESTION);
		List<ViewObject> vos = new ArrayList<ViewObject>();
		for (Comment comment : commentList) {
			ViewObject vo = new ViewObject();
			vo.set("user", userServiceImpl.getUser(comment.getUserId()));
			vo.set("comment", comment);
			// vo.set("data", );

			// 赞踩的显示
			if (hostHolder.getUser() == null) {
				vo.set("liked", 0);
			} else {
				vo.set("liked", likeServiceImpl.getLikeStatus(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT,
						comment.getId()));
			}
			vo.set("likeCount", likeServiceImpl.getLikeCount(EntityType.ENTITY_COMMENT, comment.getId()));

			vos.add(vo);
		}

		model.addAttribute("comments", vos);

		return "detail";
	}
}

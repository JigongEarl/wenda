package com.nowcoder.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.nowcoder.model.EntityType;
import com.nowcoder.model.HostHolder;
import com.nowcoder.model.Question;
import com.nowcoder.model.User;
import com.nowcoder.model.ViewObject;
import com.nowcoder.service.CommentService;
import com.nowcoder.service.FollowService;
import com.nowcoder.service.QuestionService;
import com.nowcoder.service.UserService;

/**
 * 主页
 * @author 86156
 *
 */
@Controller
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    QuestionService questionServiceImpl;

    @Autowired
    UserService userServiceImpl;
    
    @Autowired
    FollowService followServiceImpl;
    
    @Autowired
    CommentService commentServiceImpl;
    
    @Autowired
    HostHolder hostHolder;
    
    /**
     * 加载问题
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    private List<ViewObject> getQuestions(int userId, int offset, int limit) {
        List<Question> questionList = questionServiceImpl.getLatestQuestions(userId, offset, limit);
        List<ViewObject> vos = new ArrayList<>();
        for (Question question : questionList) {
            ViewObject vo = new ViewObject();
            vo.set("question", question);
            vo.set("followCount", followServiceImpl.getFollowerCount(EntityType.ENTITY_QUESTION, question.getId()));
            vo.set("user", userServiceImpl.getUser(question.getUserId()));
            vos.add(vo);
        }
        return vos;
    }
    
    /**
     * 访问主页
     * @param model
     * @param pop
     * @return
     */
    @RequestMapping(path = {"/", "/index"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String index(Model model,
                        @RequestParam(value = "pop", defaultValue = "0") int pop) {
    	
        model.addAttribute("vos", getQuestions(0, 0, 20));
        return "index";
    }
    
    /**
     * 个人主页
     * @param model
     * @param userId
     * @return
     */
    @RequestMapping(path = {"/user/{userId}"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String userIndex(Model model, @PathVariable("userId") int userId) {
        model.addAttribute("vos", getQuestions(userId, 0, 10));
        
        User user = userServiceImpl.getUser(userId);
        ViewObject vo = new ViewObject();
        vo.set("user", user);
        vo.set("commentCount", commentServiceImpl.getUserCommentCount(userId));
        vo.set("followerCount", followServiceImpl.getFollowerCount(EntityType.ENTITY_USER, userId));
        vo.set("followeeCount", followServiceImpl.getFolloweeCount(userId, EntityType.ENTITY_USER));
        if (hostHolder.getUser() != null) {
            vo.set("followed", followServiceImpl.isFollower(hostHolder.getUser().getId(), EntityType.ENTITY_USER, userId));
        } else {
            vo.set("followed", false);
        }
        model.addAttribute("profileUser", vo);
        return "profile";
    }
}

package com.nowcoder.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nowcoder.model.EntityType;
import com.nowcoder.model.Question;
import com.nowcoder.model.ViewObject;
import com.nowcoder.service.FollowService;
import com.nowcoder.service.UserService;
import com.nowcoder.service.impl.SearchServiceImpl;

@Controller
public class SearchController {
	private static final Logger logger = Logger.getLogger(SearchController.class);

    @Autowired
    private FollowService followServiceImpl;

    @Autowired
    private UserService userServiceImpl;
	
	@Autowired
	private SearchServiceImpl searchServiceImpl;

	@RequestMapping(path = {"/solr/init"}, produces = {"text/html;charset=utf-8"})
	@ResponseBody
	public String init(Model model) {
		try { // 统计初始化数据耗时
			long start = System.currentTimeMillis();
			searchServiceImpl.init();
			long end = System.currentTimeMillis();
			String time = "初始化总时长" + (end - start) / 1000 + "秒";
			System.out.println(time);
			return time;
		} catch (Exception e) {
			e.printStackTrace();
			return "初始化失败";
		}
	}
	
	
	 @RequestMapping(path = {"/search"}, method = {RequestMethod.GET})
	    public String search(Model model, @RequestParam("q") String keyword,
	                         @RequestParam(value = "offset", defaultValue = "0") int offset,
	                         @RequestParam(value = "count", defaultValue = "10") int count) {
	        try {
	            List<Question> questionList = searchServiceImpl.selByQuery(keyword, offset, count,
	                    "<em>", "</em>");
	            List<ViewObject> vos = new ArrayList<>();
	            for (Question question : questionList) {
	                ViewObject vo = new ViewObject();
	                vo.set("question", question);
	                vo.set("followCount", followServiceImpl.getFollowerCount(EntityType.ENTITY_QUESTION, question.getId()));
	                vo.set("user", userServiceImpl.getUser(question.getUserId()));
	                vos.add(vo);
	            }
	            model.addAttribute("vos", vos);
	            model.addAttribute("keyword", keyword);
	        } catch (Exception e) {
	            logger.error("搜索评论失败" + e.getMessage());
	        }
	        return "result";
	    }
}

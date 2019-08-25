package com.nowcoder.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import com.nowcoder.dao.QuestionDAO;
import com.nowcoder.model.Question;
import com.nowcoder.service.QuestionService;
@Service
public class QuestionServiceImpl implements QuestionService{
    @Autowired
    QuestionDAO questionDAO;
    @Autowired
    SensitiveServiceImpl sensitiveServiceImpl;

    public List<Question> getLatestQuestions(int userId, int offset, int limit) {
        return questionDAO.selectLatestQuestions(userId, offset, limit);
    }

	public int addQuestion(Question ques) {
		/**
		 * HTML标签转义，防止脚本注入（XSS注入）
		 */
		ques.setTitle(HtmlUtils.htmlEscape(ques.getTitle()));
		ques.setContent(HtmlUtils.htmlEscape(ques.getContent()));
		/**
		 * 敏感词过滤
		 */
		ques.setTitle(sensitiveServiceImpl.filter(ques.getTitle()));
		ques.setContent(sensitiveServiceImpl.filter(ques.getContent()));

		return questionDAO.addQuestion(ques) > 0 ? ques.getId() : 0;
	}

	public Question selQuesById(int id) {
		return questionDAO.selQuseById(id);
	}

	public int updCommentCount(int id, int count) {
		return questionDAO.updCommentCount(id, count);
	}

	public List<Question> selQuesAll() {
		return questionDAO.selQuesAll();
	}
}

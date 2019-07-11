package com.nowcoder.service;

import java.util.List;

import com.nowcoder.model.Question;

/**
 * 问题 业务层接口
 * @author 86156
 *
 */
public interface QuestionService {

    public List<Question> getLatestQuestions(int userId, int offset, int limit);

	public int addQuestion(Question ques);
	
	public Question selQuesById(int id);

	public int updCommentCount(int entityId, int count);

	public List<Question> selQuesAll();
}

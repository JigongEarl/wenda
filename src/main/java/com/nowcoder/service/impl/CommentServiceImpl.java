package com.nowcoder.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nowcoder.dao.CommentDAO;
import com.nowcoder.model.Comment;
import com.nowcoder.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService{
	@Autowired
	private CommentDAO commentDAO;

	@Override
	public int getCommentCount(int entityId, int entityType) {
		return commentDAO.getCommentCount(entityId, entityType);
	}

	@Override
	public int addComment(Comment comment) {
		return commentDAO.addComment(comment);
	}

	@Override
	public int updateStatus(int entityId, int entityType, int status) {
		return commentDAO.updateStatus(entityId, entityType, status);
	}

	@Override
	public List<Comment> selByEntity(int entityId, int entityType) {
		return commentDAO.selByEntity(entityId, entityType);
	}
	
	public int addLike(int entityId, int entityType, int like){
		return commentDAO.updateLikenum(entityId, entityType, like);
	}

	@Override
	public Comment selCommentById(int id) {
		return commentDAO.selCommentById(id);
	}

	@Override
	public int getUserCommentCount(int userId) {
        return commentDAO.getUserCommentCount(userId);
    }

}

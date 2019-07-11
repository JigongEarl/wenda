package com.nowcoder.service;

import java.util.List;

import com.nowcoder.model.Comment;

public interface CommentService {
	
	public int getCommentCount(int entityId, int entityType);

	public int addComment(Comment comment);
	
	public int updateStatus(int entityId, int entityType, int status);
	
	public List<Comment> selByEntity(int entityId, int entityType);
	
	public int addLike(int entityId, int entityType, int like);

	public Comment selCommentById(int id);
	
	public int getUserCommentCount(int userId);
 }

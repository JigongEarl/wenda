package com.nowcoder.service;

public interface LikeService {
	/**
	 * 点赞
	 * @param userId
	 * @param entityType
	 * @param entityId
	 * @return
	 */
	public long like(int userId, int entityType, int entityId);
	
	/**
	 * 踩赞
	 * @param userId
	 * @param entityType
	 * @param entityId
	 * @return
	 */
	public long disLike(int userId, int entityType, int entityId);
	
	/**
	 * 该评论的点赞数量
	 * @param entityType
	 * @param entityId
	 * @return
	 */
	public long getLikeCount(int entityType, int entityId);
	
	/**
	 * 该评论的踩赞数量
	 * @param entityType
	 * @param entityId
	 * @return
	 */
	public long getDisLikeCount(int entityType, int entityId);
	
	/**
	 * 获取某一用户对某一评论是赞还是踩
	 * @param userId
	 * @param entityType
	 * @param entityId
	 * @return
	 */
	public int getLikeStatus(int userId, int entityType, int entityId);
}

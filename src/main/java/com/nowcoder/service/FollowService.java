package com.nowcoder.service;

import java.util.List;
import java.util.Set;

/**
 * 关注/取消关注 人或实体
 * @author 86156
 *
 */
public interface FollowService {
	
	/**
	 * 关注某一实体
	 * @param userId
	 * @param entityType
	 * @param entityId
	 * @return
	 */
	public boolean follow(int userId, int entityType, int entityId);

	/**
	 * 取消关注
	 * 
	 * @param userId
	 * @param entityType
	 * @param entityId
	 * @return
	 */
	public boolean unfollow(int userId, int entityType, int entityId);
	
	/**
	 * 获取关注某一实体的所有关注人
	 * @param entityType
	 * @param entityId
	 * @param count
	 * @return
	 */
	public List<Integer> getFollowers(int entityType, int entityId, int count);
	
	/**
	 * 获取关注某一实体的所有关注人,分页显示
	 * @param entityType
	 * @param entityId
	 * @param offset
	 * @param count
	 * @return
	 */
	public List<Integer> getFollowers(int entityType, int entityId, int offset, int count);
	
	
	/**
	 * 获取某一用户对某一类实体的所有关注对象
	 * @param userId
	 * @param entityType
	 * @param count
	 * @return
	 */
	public List<Integer> getFollowees(int userId, int entityType, int count);
	
	/**
	 * 获取某一用户对某一类实体的所有关注对象，分页显示
	 * @param userId
	 * @param entityType
	 * @param offset
	 * @param count
	 * @return
	 */
	public List<Integer> getFollowees(int userId, int entityType, int offset, int count);
	
	
	/**
	 * 获取某一实体的关注数量
	 * @param entityType
	 * @param entityId
	 * @return
	 */
	public long getFollowerCount(int entityType, int entityId);
	
	/**
	 * 获取某用户对某一类实体的关注数量
	 * @param userId
	 * @param entityType
	 * @return
	 */
	public long getFolloweeCount(int userId, int entityType);
	
	
	/**
	 * 获取某一集合的所有元素的id值
	 * @param idset
	 * @return
	 */
	public List<Integer> getIdsFromSet(Set<String> idset);

	/**
	 * 判断用户是否关注了某个实体
	 * @param userId
	 * @param entityType
	 * @param entityId
	 * @return
	 */
	public boolean isFollower(int userId, int entityType, int entityId);
}

package com.nowcoder.util;

/**
 * 基于Redis的点踩赞信息集合名
 * @author 86156
 *
 */
public class RedisKeyUtils {
	
	//分隔符
	private static String SPLIT = ":";
	
	//点赞
	private static String LIKE = "LIKE";
	
	//踩赞
	private static String DISLIKE = "DISLIKE";
	
	//异步处理队列
	private static String EVENTQUEUE = "EVENT_QUEUE";  
	
	//粉丝
	private static String FOLLOWER = "FOLLOWER"; 
	
	//关注对象
	private static String FOLLOWEE = "FOLLOWEE"; 
	
	private static String TIMELINE = "TIMELINE"; 
	
	
	/**
	 * redis中，点赞用户信息的集合名       格式：LIKE : 被点赞对象类型(如评论) ：被点赞对象ID(如评论ID)
	 * @param entityType
	 * @param entityId
	 * @return
	 */
	public static String getLikeKey(int entityType, int entityId) {
        return LIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }
	
	/**
	 * redis中，踩赞用户信息的集合名       格式：DISLIKE : 被点赞对象类型(如评论) ：被点赞对象ID(如评论ID)
	 * @param entityType
	 * @param entityId
	 * @return
	 */
    public static String getDisLikeKey(int entityType, int entityId) {
        return DISLIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    public static String getEventQueueKey() {
        return EVENTQUEUE;
    }
    
    /**
     * 某个实体的粉丝key  Set集合
     * @param entityType
     * @param entityId
     * @return
     */
    public static String getFollowerKey(int entityType, int entityId) {
        return FOLLOWER + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }
    
    /**
     * 某个用户对某类实体的关注key   Set集合
     * @param userId
     * @param entityType
     * @return
     */
    public static String getFolloweeKey(int userId, int entityType) {
        return FOLLOWEE + SPLIT + String.valueOf(userId) + SPLIT + String.valueOf(entityType);
    }
    
    
    public static String getTimelineKey(int userId) {
        return TIMELINE + SPLIT + String.valueOf(userId);
    }
    
    
}

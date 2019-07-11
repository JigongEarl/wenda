package com.nowcoder.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nowcoder.dao.JedisDAO;
import com.nowcoder.service.FollowService;
import com.nowcoder.util.RedisKeyUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by rainday on 16/8/11.
 */
@Service
public class FollowServiceImpl implements FollowService{
	
	private static final Logger logger = Logger.getLogger(FollowServiceImpl.class);
    
	@Autowired
    JedisDAO jedisDAO;

    /**
     * 用户关注了某个实体,可以关注问题,关注用户,关注评论等任何实体
     * 
     * 分两步：1、将用户加到实体的粉丝群中；2、将实体加到用户的关注群中
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    public boolean follow(int userId, int entityType, int entityId) {
        String followerKey = RedisKeyUtils.getFollowerKey(entityType, entityId);
        String followeeKey = RedisKeyUtils.getFolloweeKey(userId, entityType);
        Date date = new Date();
        try {
			// 实体的粉丝增加当前用户
			jedisDAO.zadd(followerKey, date.getTime(), String.valueOf(userId));
			// 当前用户对这类实体关注+1
			jedisDAO.zadd(followeeKey, date.getTime(), String.valueOf(entityId));
			return true;
		} catch (Exception e) {
			logger.error("操作出现错误，请稍后再试");
			e.printStackTrace();
		}
        return false;
    }

    /**
     * 取消关注
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    public boolean unfollow(int userId, int entityType, int entityId) {
        String followerKey = RedisKeyUtils.getFollowerKey(entityType, entityId);
        String followeeKey = RedisKeyUtils.getFolloweeKey(userId, entityType);
        try {
			// 实体的粉丝增加当前用户
			jedisDAO.zrem(followerKey, String.valueOf(userId));
			// 当前用户对这类实体关注-1
			jedisDAO.zrem(followeeKey, String.valueOf(entityId));
			return true;
		} catch (Exception e) {
			logger.error("操作出现错误，请稍后再试");
			e.printStackTrace();
		}
        return false;
    }

    public List<Integer> getFollowers(int entityType, int entityId, int count) {
        String followerKey = RedisKeyUtils.getFollowerKey(entityType, entityId);
        return getIdsFromSet(jedisDAO.zrevrange(followerKey, 0, count));
    }

    public List<Integer> getFollowers(int entityType, int entityId, int offset, int count) {
        String followerKey = RedisKeyUtils.getFollowerKey(entityType, entityId);
        return getIdsFromSet(jedisDAO.zrevrange(followerKey, offset, offset+count));
    }

    public List<Integer> getFollowees(int userId, int entityType, int count) {
        String followeeKey = RedisKeyUtils.getFolloweeKey(userId, entityType);
        return getIdsFromSet(jedisDAO.zrevrange(followeeKey, 0, count));
    }

    public List<Integer> getFollowees(int userId, int entityType, int offset, int count) {
        String followeeKey = RedisKeyUtils.getFolloweeKey(userId, entityType);
        return getIdsFromSet(jedisDAO.zrevrange(followeeKey, offset, offset+count));
    }

    public long getFollowerCount(int entityType, int entityId) {
        String followerKey = RedisKeyUtils.getFollowerKey(entityType, entityId);
        return jedisDAO.zcard(followerKey);
    }

    public long getFolloweeCount(int userId, int entityType) {
        String followeeKey = RedisKeyUtils.getFolloweeKey(userId, entityType);
        return jedisDAO.zcard(followeeKey);
    }
    
    /**
     * 将Set集合元素封装到List中
     */
    public List<Integer> getIdsFromSet(Set<String> idset) {
        List<Integer> ids = new ArrayList<>();
        for (String str : idset) {
            ids.add(Integer.parseInt(str));
        }
        return ids;
    }

    /**
     *  判断用户是否关注了某个实体
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    public boolean isFollower(int userId, int entityType, int entityId) {
        String followerKey = RedisKeyUtils.getFollowerKey(entityType, entityId);
        return jedisDAO.zscore(followerKey, String.valueOf(userId)) != null;
    }
}

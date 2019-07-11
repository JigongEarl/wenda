package com.nowcoder.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nowcoder.dao.JedisDAO;
import com.nowcoder.service.LikeService;
import com.nowcoder.util.RedisKeyUtils;

@Service
public class LikeServiceImpl implements LikeService {
	
	@Autowired
	private JedisDAO jedisDAO;
	
	/**
	 * 点赞
	 */
	public long like(int userId, int entityType, int entityId) {
		//添加到redis的like集合中
		String likeKey = RedisKeyUtils.getLikeKey(entityType, entityId);
		jedisDAO.sadd(likeKey, String.valueOf(userId));
		
		//从redis的dislike集合中移除(如果没有将不做操作)
		String disLikeKey = RedisKeyUtils.getDisLikeKey(entityType, entityId);
		jedisDAO.srem(disLikeKey, String.valueOf(userId));
		
		return jedisDAO.scard(likeKey);  //返回点赞总数
	}
	
	/**
	 * 踩赞
	 */
	public long disLike(int userId, int entityType, int entityId) {
		//用户id添加到redis的disLike集合中
		String disLikeKey = RedisKeyUtils.getDisLikeKey(entityType, entityId);
		jedisDAO.sadd(disLikeKey, String.valueOf(userId));
		
		//从redis的like集合中移除(如果没有将不做操作)
		String likeKey = RedisKeyUtils.getLikeKey(entityType, entityId);
		jedisDAO.srem(likeKey, String.valueOf(userId));
		
		return jedisDAO.scard(disLikeKey);
	}
	
	/**
	 * 点赞数量
	 */
	@Override
	public long getLikeCount(int entityType, int entityId) {
		String likeKey = RedisKeyUtils.getLikeKey(entityType, entityId);
		return jedisDAO.scard(likeKey);
	}

	/**
	 * 踩赞数量
	 */
	@Override
	public long getDisLikeCount(int entityType, int entityId) {
		String disLikeKey = RedisKeyUtils.getDisLikeKey(entityType, entityId);
		return jedisDAO.scard(disLikeKey);
	}
	
	/**
	 * 指定用户是赞还是踩
	 */
	@Override
	public int getLikeStatus(int userId, int entityType, int entityId) {
		String likeKey = RedisKeyUtils.getLikeKey(entityType, entityId);
		if(jedisDAO.sismember(likeKey, String.valueOf(userId))) {
			return 1;  //赞
		}
		String disLikeKey = RedisKeyUtils.getDisLikeKey(entityType, entityId);
		return jedisDAO.sismember(disLikeKey, String.valueOf(userId)) ? -1 : 0;  //-1踩， 0没有踩赞
	}

}

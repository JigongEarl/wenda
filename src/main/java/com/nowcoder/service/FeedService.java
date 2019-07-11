package com.nowcoder.service;

import java.util.List;

import com.nowcoder.model.Feed;

public interface FeedService {
	
	/**
	 * 获取已关注的用户的所有新鲜事
	 * @param maxId
	 * @param userIds
	 * @param count
	 * @return
	 */
    public List<Feed> getUserFeeds(int maxId, List<Integer> userIds, int count);
    
    /**
     * 增加先显示
     * @param feed
     * @return
     */
    public boolean addFeed(Feed feed);
    
    /**
     * 获取新鲜事 By Id
     * @param id
     * @return
     */
    public Feed getById(int id);

}

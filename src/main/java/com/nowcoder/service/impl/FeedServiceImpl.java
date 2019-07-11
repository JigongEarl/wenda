package com.nowcoder.service.impl;

import com.nowcoder.dao.FeedDAO;
import com.nowcoder.model.Feed;
import com.nowcoder.service.FeedService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 新鲜事 业务层
 * @author 86156
 *
 */
@Service
public class FeedServiceImpl implements FeedService{
	
    @Autowired
    FeedDAO feedDAO;

    public List<Feed> getUserFeeds(int maxId, List<Integer> userIds, int count) {
        return feedDAO.selectUserFeeds(maxId, userIds, count);
    }

    public boolean addFeed(Feed feed) {
        feedDAO.addFeed(feed);
        return feed.getId() > 0;
    }

    public Feed getById(int id) {
        return feedDAO.getFeedById(id);
    }
}

package com.nowcoder.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.nowcoder.dao.JedisDAO;
import com.nowcoder.model.EntityType;
import com.nowcoder.model.Feed;
import com.nowcoder.model.HostHolder;
import com.nowcoder.service.FeedService;
import com.nowcoder.service.FollowService;
import com.nowcoder.util.RedisKeyUtils;

/**
 * 新鲜事 控制器
 * @author 86156
 *
 */
@Controller
public class FeedController {
    private static final Logger logger = LoggerFactory.getLogger(FeedController.class);

    @Autowired
    FeedService feedServiceImpl;

    @Autowired
    FollowService followServiceImpl;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    JedisDAO jedisDAO;
    
    /**
     * 获取新鲜事  推模式
     * @param model
     * @return
     */
    @RequestMapping(path = {"/pushfeeds"}, method = {RequestMethod.GET, RequestMethod.POST})
    private String getPushFeeds(Model model) {
        int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
        List<String> feedIds = jedisDAO.lrange(RedisKeyUtils.getTimelineKey(localUserId), 0, 10);
        List<Feed> feeds = new ArrayList<Feed>();
        for (String feedId : feedIds) {
            Feed feed = feedServiceImpl.getById(Integer.parseInt(feedId));
            if (feed != null) {
                feeds.add(feed);
            }
        }
        model.addAttribute("feeds", feeds);
        return "feeds";
    }
    
    
    /**
     * 获取新鲜事  拉模式
     * @param model
     * @return
     */
    @RequestMapping(path = {"/pullfeeds"}, method = {RequestMethod.GET, RequestMethod.POST})
    private String getPullFeeds(Model model) {
        int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
        List<Integer> followees = new ArrayList<>();
        if (localUserId != 0) {
            // 关注的人
            followees = followServiceImpl.getFollowees(localUserId, EntityType.ENTITY_USER, Integer.MAX_VALUE);
        }
        List<Feed> feeds = feedServiceImpl.getUserFeeds(Integer.MAX_VALUE, followees, 10);
        System.out.println("feeds长度"+feeds.size());
        model.addAttribute("feeds", feeds);
        
        return "feeds";
    }
}

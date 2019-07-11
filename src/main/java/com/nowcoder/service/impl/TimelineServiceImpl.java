package com.nowcoder.service.impl;

import com.nowcoder.model.Feed;
import com.nowcoder.service.TimeLineService;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimelineServiceImpl implements TimeLineService {

    public List<Feed> pull(int userId, int maxId, int count) {
        return null;
    }
}

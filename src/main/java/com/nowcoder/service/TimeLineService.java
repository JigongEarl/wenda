package com.nowcoder.service;

import java.util.List;

import com.nowcoder.model.Feed;

public interface TimeLineService {
	
	public List<Feed> pull(int userId, int maxId, int count);

}

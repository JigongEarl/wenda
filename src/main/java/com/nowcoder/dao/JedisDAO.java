package com.nowcoder.dao;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Mapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.JedisCluster;

/**
 * redis操作封装类
 * 
 * @author 86156
 *
 */
@Component
public class JedisDAO {

	private static final Logger logger = Logger.getLogger(JedisDAO.class);
	@Resource
	private JedisCluster jedisCluster;
	

	////// redis之Set无序集合(集合不可重复)，用于点赞、踩赞、抽奖、已读、共同好友等功能
	/**
	 * 向集合中添加成员（s开头表示set集合，底层是hash表，无序）
	 * 
	 * @param key   集合名
	 * @param value 集合元素
	 * @return
	 */
	public long sadd(String key, String value) {
		return jedisCluster.sadd(key, value);
	}

	/**
	 * 获取集合成员数
	 * 
	 * @param key
	 * @return
	 */
	public Long scard(String key) {
		return jedisCluster.scard(key);
	}

	/**
	 * 判断指定元素是否在集合中
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean sismember(String key, String value) {
		return jedisCluster.sismember(key, value);
	}

	/**
	 * 移除指定元素
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Long srem(String key, String value) {
		return jedisCluster.srem(key, value);
	}

	
	////// redis之List双向列表，适用于最新列表、关注列表等功能
	/**
	 * 入队，插入到列表头部，如果key不存在将创建新列表
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public long lpush(String key, String value) {
		return jedisCluster.lpush(key, value);
	}
	
	/**
	 * 获取列表中指定区间的元素
	 * @param timelineKey
	 * @param start
	 * @param end
	 * @return
	 */
	public List<String> lrange(String timelineKey, int start, int end) {
		return jedisCluster.lrange(timelineKey, start, end);
	}

	/**
	 * 移除并获取列表key中的最后一个元素，如果没有元素将阻塞队列直到超时或者有新元素入列弹出
	 * 
	 * @param key
	 * @param tiomeout
	 * @return 如果指定时间内没有任何元素被弹出，则返回一个nil和等待时长；反之，返回一个含有
	 *         两个元素的列表，第一个元素是被弹出元素所属的key，第二个元素是被弹出元素的值
	 */
	public List<String> brpop(int timeout, String key) { // timeout 阻塞等待时间 秒
		return jedisCluster.brpop(timeout, key);
	}

	/**
	 * 移除并获取列表key中的第一个元素，如果没有元素将阻塞队列直到超时或者有新元素入列弹出
	 * 
	 * @param key
	 * @param tiomeout
	 * @return
	 */
	public List<String> blpop(int timeout, String key) {
		return jedisCluster.blpop(timeout, key);
	}


	////// redis之SortedSet,适用于排行榜，优先队列
	/**
	 * 向有序集合添加元素
	 * 
	 * @param key
	 * @param score
	 * @param value
	 * @return
	 */
	public long zadd(String key, double score, String value) {
		return jedisCluster.zadd(key, score, value);
	}

	/**
	 * 移除元素
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public long zrem(String key, String value) {
		return jedisCluster.zrem(key, value);
	}
	
	/**
     * 通过索引区间返回有序集合成指定区间内的元素
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<String> zrange(String key, int start, int end) {
            return jedisCluster.zrange(key, start, end);
    }
	    
	/**
     * 返回有序集中指定区间内的元素，通过索引，分数从高到底  若end=-1表示最后一个元素，即返回所有
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<String> zrevrange(String key, int start, int end) {
        return jedisCluster.zrevrange(key, start, end);
    }

	/**
     * 获取有序集合的元素数
     * @param key
     * @return
     */
    public long zcard(String key) {
        return jedisCluster.zcard(key);
    }
	    
	/**
     * 返回指定元素的分数值
     * @param key
     * @param member
     * @return
     */
    public Double zscore(String key, String member) {
		return jedisCluster.zscore(key, member);
    }
}

package com.nowcoder.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import com.nowcoder.dao.LoginTokenDAO;
import com.nowcoder.dao.UserDAO;
import com.nowcoder.model.LoginToken;
import com.nowcoder.model.User;
import com.nowcoder.service.UserService;
import com.nowcoder.util.WendaUtil;

@Service
public class UserServiceImpl implements UserService{
	
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private LoginTokenDAO loginTokenDAO;
    
    /**
     * 登录功能
     */
	public Map<String, Object> login(User user) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		//非空验证
		if(StringUtils.isBlank(user.getName())) {
			map.put("msg", "用户名不能为空");
			return map;
		}
	    if(StringUtils.isBlank(user.getPassword())) {
	        map.put("msg", "密码不能为空");
	        return map;
	    }
	    
	    //用户名/密码验证
	    User _user = userDAO.selectByName(user.getName());
	    if(_user == null) {
	    	map.put("msg", "用户名不存在");
	    	return map;
	    }
	    System.out.println(WendaUtil.MD5(user.getPassword() + _user.getSalt()));
	    if(!_user.getPassword().equals(WendaUtil.MD5(user.getPassword() + _user.getSalt()))) {
	    	map.put("msg", "密码错误！");
	    	return map;
	    }
	    
	    //存储token信息到数据库，共享用户登陆状态
	    String token = addLoginToken(_user.getId());
	    map.put("token", token);
		return map;
	}
	
	
	/**
	 * 新增token信息（封装了token的LoginToken对象）到数据库
	 * @param userId
	 * @return
	 */
	private String addLoginToken(int userId) {
		LoginToken lToken  = new LoginToken();
		lToken.setUserId(userId);
		lToken.setToken(UUID.randomUUID().toString().replace("-", ""));
		Date date = new Date();
		date.setTime(date.getTime() + 1000 * 3600 *24); //token有效期为24小时
		lToken.setExpired(date);
		
		loginTokenDAO.addToken(lToken);
		
		return lToken.getToken();
	}
	

	/**
	 * 注册功能
	 */
	public Map<String, Object> register(User user) {
		
		Map<String, Object> map = new HashMap<>();
		if (StringUtils.isBlank(user.getName())) {
            map.put("msg", "用户名不能为空");
            return map;
        }
        if (StringUtils.isBlank(user.getPassword())) {
            map.put("msg", "密码不能为空");
            return map;
        }
        User _user = userDAO.selectByName(user.getName());
        if (_user != null) {
            map.put("msg", "用户名已经被注册");
            return map;
        }
        
        //封装User对象，加salt和头像
  		user.setSalt(UUID.randomUUID().toString().substring(0,5));
  		String head = String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000));
  		user.setHeadUrl(head);
  		user.setPassword(WendaUtil.MD5(user.getPassword() + user.getSalt()));
       
  		//保存用户信息到数据库
		userDAO.addUser(user);
		map.put("msg", "恭喜您注册成功，请登录！");
		
		//存储token信息到数据库，共享用户登陆状态
		String token = addLoginToken(user.getId());
		//将token信息传递给浏览器
		map.put("token", token);
		
		return map;
	}
	
	
	/**
	 * 退出登录
	 */
	public void logout(String token) {
		loginTokenDAO.updStatus(token, 1);
	}
    
	
    /**
     * 获取指定id的用户信息
     */
    public User getUser(int id) {
        return userDAO.selectById(id);
    }
    
    
    /**
     * 获取指定name的用户信息
     */
	@Override
	public User getUserByName(String name) {
		return userDAO.selectByName(name);
	}
    
}

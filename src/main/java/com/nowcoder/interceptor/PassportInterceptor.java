package com.nowcoder.interceptor;

import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.nowcoder.dao.LoginTokenDAO;
import com.nowcoder.dao.UserDAO;
import com.nowcoder.model.HostHolder;
import com.nowcoder.model.LoginToken;
import com.nowcoder.model.User;
/**
 * 登录访问拦截器
 * @author 86156
 *
 */
@Component  //将该类交给spring容器管理
public class PassportInterceptor implements HandlerInterceptor {
	
	@Autowired
	private LoginTokenDAO loginTokenDAO;
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private HostHolder hostHolder;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		/*
		 * 已经登录的用户在登陆时会在数据库中保存登录信息(LoginToken信息)，并且cookie中key=“token”的cookie保存
		 * 着信息中token关键字的值，尚未登录时没有该数据
		 */
		String token = null;
		Cookie[] cookies = request.getCookies();
		if(cookies != null) {
			for(Cookie cookie : cookies) {
				if(cookie.getName().equals("token")) {
					token = cookie.getValue();
					break;
				}
			}
		}
		if(token != null) {
			LoginToken lToken = loginTokenDAO.selByToken(token);
			
			//未登录，放行，下一步直接访问控制器(后续的代码不再执行)
			if(lToken == null || lToken.getExpired().before(new Date()) || lToken.getStatus() != 0) {
				return true;  
			}
			
			//已登录，获取用户信息并保存到HostHolder域(已经被Spring容器管理，可以实现同一个线程中的数据共享)
			User user = userDAO.selectById(lToken.getUserId());
			hostHolder.setUser(user);
		}
		return true; //放行
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
		//在模板渲染前将已登录的用户信息保存到模型/视图中，供显示在模板上
		if(modelAndView != null && hostHolder.getUser() != null) {
			modelAndView.addObject("user", hostHolder.getUser());
		}
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
		hostHolder.clear();
	}

}

package com.nowcoder.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.nowcoder.model.HostHolder;

/**
 * 登录验证拦截器，只拦截对/user/目录的访问
 * @author 86156
 *
 */
@Component
public class LoginRequiredInterceptor implements HandlerInterceptor{
	
	@Autowired
    HostHolder hostHolder;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		 //客户未登录，重定向到登陆页面，并记录当前页面地址供登陆后返回该页面
		 if (hostHolder.getUser() == null) {
	            response.sendRedirect("/reglogin?next=" + request.getRequestURI());
	        }
	        return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
	}

}

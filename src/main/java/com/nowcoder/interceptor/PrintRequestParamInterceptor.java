package com.nowcoder.interceptor;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
/**
 * 拦截器测试
 * --打印请求参数和响应参数
 * @author 86156
 *
 */
//@Component
public class PrintRequestParamInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//打印请求头
		Enumeration<String> headerNames = request.getHeaderNames();
		while(headerNames.hasMoreElements()) {
			String name = headerNames.nextElement();
			System.out.println(name + " : " + request.getHeader(name));
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
		//打印响应头
		Collection<String> headerNames = response.getHeaderNames();
		Iterator<String> it = headerNames.iterator();
		while(it.hasNext()) {
			String next = it.next();
			System.out.println(next + " : " + response.getHeader(next));
		}
	}

}

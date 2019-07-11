package com.nowcoder.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.nowcoder.model.User;
import com.nowcoder.service.UserService;

@Controller
public class LoginController {
	private static final Logger logger = Logger.getLogger(LoginController.class);
	@Resource
	private UserService userServiceImpl;
	
	/**
	 * 显示登陆注册页面
	 * @param url
	 * @param model
	 * @return
	 */
	@RequestMapping("/reglogin")
	public String showLogin(@RequestHeader(value = "referer", defaultValue = "") String url, Model model) {
		if (url != null && !url.equals("")) {
			model.addAttribute("redirect", url);
		}
		return "login";
	}

	/**
	 * 登录功能
	 * 
	 * @param user
	 * @param model
	 * @return
	 */
	@RequestMapping(path = {"/login"}, method = {RequestMethod.POST})
	public String login(Model model, 
						User user, 
						@RequestParam(value = "next", required = false) String next, // 登录后的跳转地址
						@RequestParam(value = "rememberme", defaultValue = "false") boolean rememberme,
						HttpServletResponse response) {
		
		try {
			Map<String, Object> map = userServiceImpl.login(user);
			if (map.containsKey("token")) { // 登陆信息验证正确，会生成token
				Cookie cookie = new Cookie("token", map.get("token").toString());
				cookie.setPath("/");
				if (rememberme) {
					cookie.setMaxAge(3600 * 24 * 7);
				}
				response.addCookie(cookie);
				if (StringUtils.isNotBlank(next)) {
					return "redirect:" + next;
				}
				return "redirect:/";
			} else { // 登录验证错误,传递登录信息并跳转到登陆页面
				model.addAttribute("msg", map.get("msg"));
				return "login";
			}
		} catch (Exception e) {
			logger.error("登陆异常" + e.getMessage());
			model.addAttribute("msg", "服务器错误");
			e.printStackTrace();
			return "login";
		}
	}

	/**
	 * 注册功能
	 * 
	 * @param user
	 * @param model
	 * @return
	 */
	@RequestMapping(path = {"/reg"}, method = {RequestMethod.POST})
	public String register(Model model, 
						   User user,
						   @RequestParam(value = "next", required = false) String next, // 登录后的跳转地址
						   @RequestParam(value = "rememberme", defaultValue = "false") boolean rememberme,
						   HttpServletResponse response) {
		
		try {
			Map<String, Object> map = userServiceImpl.register(user);
			if (map.containsKey("token")) { // 登陆信息验证正确，会生成token
				Cookie cookie = new Cookie("token", map.get("token").toString());
				cookie.setPath("/");
				if (rememberme) {
					cookie.setMaxAge(3600 * 24 * 7);
				}
				response.addCookie(cookie);
				if (StringUtils.isNotBlank(next)) {
					return "redirect:" + next;
				}
				return "redirect:/";
			} else { // 登录验证错误,传递登录信息并跳转到登陆页面
				model.addAttribute("msg", map.get("msg"));
				return "login";
			}
		} catch (Exception e) {
			logger.error("登陆异常" + e.getMessage());
			model.addAttribute("msg", "服务器错误");
			return "login";
		}
	}
	
	
	/**
	 * 退出登录
	 * @param token
	 * @return
	 */
	@RequestMapping(path = {"/logout"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String logout(@CookieValue("token") String token) {
		userServiceImpl.logout(token);
        return "redirect:/";
	}
}

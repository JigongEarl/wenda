package com.nowcoder.async.handler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.nowcoder.async.EventHandler;
import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventType;
import com.nowcoder.util.MailSender;
/**
 * 登录异常通知 异步处理器
 * @author 86156
 *
 */
public class LoginExceptionHandler implements EventHandler {
	
	@Autowired
	private MailSender mailSender;
	
	public void doHandle(EventModel model) {
		Map<String, Object> map = new HashMap<>();
		map.put("username", model.getExt("username"));
		mailSender.sendWithHTMLTemplate(String.valueOf(model.getExt("email"))
								, "登陆IP异常", "mails/login_exception.html", map);
	}

	public List<EventType> getSupportEventTypes() {
		return Arrays.asList(EventType.LOGIN);
	}

}

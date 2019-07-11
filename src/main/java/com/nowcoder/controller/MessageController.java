package com.nowcoder.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import com.nowcoder.model.HostHolder;
import com.nowcoder.model.Message;
import com.nowcoder.model.User;
import com.nowcoder.model.ViewObject;
import com.nowcoder.model.WendaResult;
import com.nowcoder.service.MessageService;
import com.nowcoder.service.UserService;
import com.nowcoder.service.impl.SensitiveServiceImpl;
import com.nowcoder.util.JsonUtils;

@Controller
public class MessageController {

	private static final Logger logger = Logger.getLogger(MessageController.class);
	@Autowired
	private HostHolder hostHolder;
	@Autowired
	private MessageService messageServiceImpl;
	@Autowired
	private UserService userServiceImpl;
	@Autowired
	private SensitiveServiceImpl sensitiveServiceImpl;
	
	
	/**
	 * 发送私信
	 * @param toName
	 * @param content
	 * @param request
	 * @return
	 */
	@RequestMapping(value = { "/msg/addMessage" }, method = { RequestMethod.POST }) // 注：value=path
	@ResponseBody
	public String addMessage(@RequestParam("toName") String toName, @RequestParam("content") String content,
			HttpServletRequest request) {
		WendaResult result = new WendaResult();
		// 是否登录
		try {
			if (hostHolder.getUser() == null) {
				result.setCode(999);
				return JsonUtils.objectToJson(result);
				// return "redirect:/reglogin?next=" + request.getRequestURI();
			}

			// 防脚本注入、敏感词过滤
			toName = HtmlUtils.htmlEscape(toName);
			toName = sensitiveServiceImpl.filter(toName);
			content = HtmlUtils.htmlEscape(content);
			content = sensitiveServiceImpl.filter(content);

			Message msg = new Message();
			msg.setContent(content);
			msg.setCreatedDate(new Date());
			msg.setFromId(hostHolder.getUser().getId());
			msg.setHasRead(0);
			User to_user = userServiceImpl.getUserByName(toName); // 接收者
			msg.setToId(to_user.getId());
			// fromid_toid的格式
//			String setConversationId = String.format("%d_%d", hostHolder.getUser().getId(), to_user.getId());
//			msg.setConversationId(setConversationId);
			// 0 操作失败 1 成功
			result.setCode(messageServiceImpl.addMessage(msg) == 1 ? 0 : 1);
		} catch (Exception e) {
			result.setCode(1);
			logger.error("增加站内信失败" + e.getMessage());
			e.printStackTrace();
		}
		return JsonUtils.objectToJson(result);
	}
	
	/**
	 * 显示所有私信
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/msg/list" }, method = { RequestMethod.GET })
	public String conversationDetail(Model model) {
		try {
			int localUserId = hostHolder.getUser().getId(); // 当前登录的用户
			List<ViewObject> conversations = new ArrayList<>();
			List<Message> msgs = messageServiceImpl.selMsgsByUserId(localUserId, 0, 10);
			for (Message msg : msgs) {
				ViewObject conversation = new ViewObject();
				conversation.set("message", msg);
				int targetId = msg.getFromId() == localUserId ? msg.getToId() : msg.getFromId();
				User user = userServiceImpl.getUser(targetId); // msg为发出的消息，所以fromid为自己
				conversation.set("user", user);
				// 本人未读
				conversation.set("unread", messageServiceImpl.getConvesationUnreadCount(localUserId, msg.getConversationId()));
				conversations.add(conversation);
			}
			model.addAttribute("conversations", conversations);
		} catch (Exception e) {
			logger.error("获取站内信列表失败" + e.getMessage());
		}

		return "letter";
	}
	
	/**
	 * 显示指定双方的所有私信
	 * @param model
	 * @param conversationId
	 * @return
	 */
	@RequestMapping(path = { "/msg/detail" }, method = { RequestMethod.GET })
	public String conversationDetail(Model model, @RequestParam("conversationId") String conversationId) {
		try {
			List<Message> conversationList = messageServiceImpl.getConversationDetail(conversationId, 0, 10);
			List<ViewObject> messages = new ArrayList<>();
			for (Message msg : conversationList) {
				ViewObject message = new ViewObject();
				message.set("message", msg);
				User user = userServiceImpl.getUser(msg.getFromId());
				if (user == null) {
					continue;
				}
				message.set("user", user);
				
				messages.add(message);
			}
			model.addAttribute("messages", messages);
		} catch (Exception e) {
			logger.error("获取详情消息失败" + e.getMessage());
		}
		return "letterDetail";
	}
}

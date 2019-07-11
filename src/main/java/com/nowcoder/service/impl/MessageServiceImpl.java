package com.nowcoder.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nowcoder.dao.MessageDAO;
import com.nowcoder.model.Message;
import com.nowcoder.service.MessageService;
@Service
public class MessageServiceImpl implements MessageService {
	
	@Autowired
	private MessageDAO messageDAO;

	public int addMessage(Message message) {
		return messageDAO.addMessage(message);
	}

	public List<Message> selMsgsByConversationId(String conversationId, int offset, int limit) {
		return messageDAO.selMsgsByConversationId(conversationId, offset, limit);
	}

	public List<Message> selMsgsByUserId(int userId, int offset, int limit) {
		return messageDAO.selMsgsByUserId(userId, offset, limit);
	}

	public int getConvesationUnreadCount(int userId, String conversationId) {
		return messageDAO.selConvesationUnreadCount(userId, conversationId);
	}

	public List<Message> getConversationDetail(String conversationId, int offset, int limit) {
		return messageDAO.selMsgsByConversationId(conversationId, offset, limit);
	}
}

package com.nowcoder.service;

import java.util.List;
import com.nowcoder.model.Message;

public interface MessageService {

	int addMessage(Message msg);
	
	public List<Message> selMsgsByConversationId(String conversationId, int offset, int limit);
	
	public List<Message> selMsgsByUserId(int userId, int offset, int limit);

	public int getConvesationUnreadCount(int userId, String conversationId);

	public List<Message> getConversationDetail(String conversationId, int i, int j);
}

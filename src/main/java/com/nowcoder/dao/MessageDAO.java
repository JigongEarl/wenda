package com.nowcoder.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.nowcoder.model.Message;

@Mapper
public interface MessageDAO {
	String TABLE_NAME = "message";
	String INSERT_FIELDS = " fromid, toid, content, conversation_id, created_date, has_read ";
	String SELECT_FIELDS = "id, " + INSERT_FIELDS;
	
	@Select({"select", SELECT_FIELDS, "from", TABLE_NAME, "where toid=#{toId}"})
	public List<Message> selByToId(@Param("toId") int toId);
	
	@Select({"select", SELECT_FIELDS, "from", TABLE_NAME, "where conversation_id=#{conversationId}"
			+ "order by id desc limit #{offset}, #{limit}"})
	public List<Message> selMsgsByConversationId(@Param("conversationId") String conversationId, 
												 @Param("offset") int offset, @Param("limit") int limit);
	
	@Insert({"insert into", TABLE_NAME, "values(#{id}, #{fromId}, #{toId}, #{content}, #{conversationId},"
			+ "#{createdDate}, #{hasRead})" })
	public int addMessage(Message message);

	@Select({"select", SELECT_FIELDS, "from", TABLE_NAME, "where fromid=#{userId} or toid=#{userId} "
			+ "order by id desc limit #{offset}, #{limit}"})
	public List<Message> selMsgsByUserId(@Param("userId") int userId, 
			  							 @Param("offset") int offset, @Param("limit") int limit);
	
	@Select({"select ", INSERT_FIELDS, " ,count(id) as id from ( select * from ", TABLE_NAME, 
		" where fromid=#{userId} or toid=#{userId} order by id desc) tt group by conversation_id  "
		+ "order by created_date desc limit #{offset}, #{limit}"})
    List<Message> selConversationList(@Param("userId") int userId,
                                      @Param("offset") int offset, @Param("limit") int limit);
	
	 @Select({"select count(id) from ", TABLE_NAME, " where has_read=0 and toid=#{userId} and "
	 		+ "conversation_id=#{conversationId}"})
	 int selConvesationUnreadCount(@Param("userId") int userId, 
	    						   @Param("conversationId") String conversationId);
	
}

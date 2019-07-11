package com.nowcoder.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.nowcoder.model.Comment;
@Mapper
public interface CommentDAO {
	String TABLE_NAME = "comment";
	String INSERT_FIELD = "content, user_id, entity_id, entity_type, created_date, status, likenum";
	String SELECTED_FIELD = "id, " + INSERT_FIELD;
	
	@Select({"select", SELECTED_FIELD, "from", TABLE_NAME, "where entity_id=#{entityId} and entity_type=#{entityType} order by id desc"})
	public List<Comment> selByEntity(@Param("entityId")int entityId, @Param("entityType") int entityType);
	
	@Insert({"insert into ", TABLE_NAME, "values("
			+ "#{id}, #{content}, #{userId}, #{entityId}, #{entityType}, #{createdDate}, #{status}, #{likenum})"})
	public int addComment(Comment comment);
	
	@Update({"update", TABLE_NAME, "set status=#{status} where entity_id=#{entityId}, entity_type=#{entityType}"})
	public int updateStatus(@Param("entityId")int entityId, @Param("entityType") int entityType, @Param("status")int status);

	@Select({"select count(*) from", TABLE_NAME, "where entity_id=#{entityId} and entity_type=#{entityType}"})
	public int getCommentCount(@Param("entityId")int entityId, @Param("entityType") int entityType);
	
	@Update({"update", TABLE_NAME, "set likenum=#{likenum} where entity_id=#{entityId}, entity_type=#{entityType}"})
	public int updateLikenum(@Param("entityId")int entityId, @Param("entityType") int entityType, @Param("likenum")int likenum);
	
	@Select({"select", SELECTED_FIELD, "from", TABLE_NAME, "where id=#{id} order by id desc"})
	public Comment selCommentById(@Param("id") int id);
	
	@Select({"select", SELECTED_FIELD, "from", TABLE_NAME, "where user_id=#{userId} order by id desc"})
	public Comment selCommentByUserId(@Param("userId") int userId);
	
	@Select({"select count(*) from", TABLE_NAME, "where user_id=#{userId}"})
	public int getUserCommentCount(int userId);
	
	
}

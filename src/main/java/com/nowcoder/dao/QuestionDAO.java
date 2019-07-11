package com.nowcoder.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.nowcoder.model.Question;

/**
 * Created by nowcoder on 2016/7/2.
 */
@Mapper
public interface QuestionDAO {
    String TABLE_NAME = " question ";
    String INSERT_FIELDS = " title, content, created_date, user_id, comment_count ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{title},#{content},#{createdDate},#{userId},#{commentCount})"})
    int addQuestion(Question question);

    List<Question> selectLatestQuestions(@Param("userId") int userId, @Param("offset") int offset,
                                         @Param("limit") int limit);
    
    @Select({"select", SELECT_FIELDS, "from", TABLE_NAME, "where id=#{id}"})
    Question selQuseById(@Param("id") int id);
    
    @Update({"update ", TABLE_NAME, " set comment_count = #{count} where id=#{id}"})
	int updCommentCount(@Param("id")int id, @Param("count")int count);
    
    @Select({"select * from", TABLE_NAME})
	List<Question> selQuesAll();

}

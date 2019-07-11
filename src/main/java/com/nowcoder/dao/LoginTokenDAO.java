package com.nowcoder.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.nowcoder.model.LoginToken;
@Mapper
public interface LoginTokenDAO {
	String TABLE_NAME = "login_token";
	String INSERT_FIELDS = " userId, token, expired, status ";
	String SELECT_FIELDS = "id, " + INSERT_FIELDS;
	
	@Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, "where token = #{token}"})
	LoginToken selByToken(String token);
	
	@Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
    ") values (#{userId},#{token},#{expired},#{status})"})
	int addToken(LoginToken loginToken);
	
	@Update({"update ", TABLE_NAME, " set status = #{status} where token = #{token}"})
	void updStatus(@Param("token")String token, @Param("status")int status);
}

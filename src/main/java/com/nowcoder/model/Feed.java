package com.nowcoder.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Date;

/**
 * feed实体类
 * @author 86156
 *
 */
public class Feed {
    private int id;
    private int type;
    private int userId;
    private Date createdDate;
    private String data;
    
    //json字符串对象
    private JsonNode dataJSON;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
        try {
			dataJSON =new ObjectMapper().readTree(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }


	/*
	 * velocity中的$obj.xxx，会自动匹配如下bean中的方法：
	 * obj.getxxx()
	 * obj.get("xxx")
	 * obj.isxxx()等格式
	 */
    
    //dataJSON:表示整个json串(JsonNode对象)；findValue(key):获取串中指定key的value(依旧是一个JsonNode对象)，
    //asText():将jsonNode对象转为字符串
    public String get(String key) {
        return dataJSON == null ? null : dataJSON.findValue(key).asText();
    }
}

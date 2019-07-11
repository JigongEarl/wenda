package com.nowcoder.util;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {
	//定义json对象
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	
	/**
	 * 把对象转化为json字符串
	 * @param obj
	 * @return
	 */
	public static String objectToJson(Object obj) {
		try {
			String string = MAPPER.writeValueAsString(obj);
			return string;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	} 
	
	
	/**
	 * 把json字符串转化为对象
	 * @param <T>
	 * @param string
	 * @param beanType
	 * @return
	 */
	public static <T> T jsonToObject(String string, Class<T> beanType) {
		try {
			T t = MAPPER.readValue(string, beanType);
			return t;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 将json转化为List集合
	 * @param <T>
	 * @param jsonData
	 * @param beanType
	 * @return
	 */
	 public static <T>List<T> jsonToList(String jsonData, Class<T> beanType) {
	    	JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, beanType);
	    	try {
	    		List<T> list = MAPPER.readValue(jsonData, javaType);
	    		return list;
			} catch (Exception e) {
				e.printStackTrace();
			}
	    	
	    	return null;
	    }


}	

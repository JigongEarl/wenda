package com.nowcoder;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nowcoder.model.User;
import com.nowcoder.util.JsonUtils;
/**
 * Jackson包方法测试
 * @author 86156
 *
 */
public class JsonTest {
//	public static void main(String[] args) {
//		User user = new User();
//		user.setId(0);
//		user.setName("zhangsan");
//		user.setPassword("000");
//		String userJson = JsonUtils.objectToJson(user);
//		
//		try {
//			JsonNode nodes = new ObjectMapper().readTree(userJson);
//			//获取所有的节点
//			//Iterator<Entry<String, JsonNode>> node = nodes.fields();
//			//获取key为name的节点
//			JsonNode node = nodes.findValue("name");
//			System.out.println(node);
//			System.out.println(node.getClass().getName());
//			
//			String s = node.toString();
//			System.out.println(s);
//			System.out.println(s.getClass().getName());
//			
//			String s1 = node.asText();
//			System.out.println(s1);
//			System.out.println(s1.getClass().getName());
//			
//			
//			//获取key为id的节点
//			System.out.println(nodes.findValue("id"));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
}

package com.nowcoder.model;

import java.util.Date;

public class LoginToken {
	private int id; 
	private int userId;
	private String token; 
	private Date expired; //token有效期
	private int status;  //1失效，0有效
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Date getExpired() {
		return expired;
	}
	public void setExpired(Date expired) {
		this.expired = expired;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "LoginToken [id=" + id + ", userId=" + userId + ", token=" + token + ", expired=" + expired + ", status="
				+ status + "]";
	}
	
}

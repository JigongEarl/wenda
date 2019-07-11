package com.nowcoder.model;
/**
 * 异步请求中的返回结果封装类
 * @author 86156
 *
 */
public class WendaResult {
	private int code;  //0成功 1失败
	private Object data;
	private String msg;
	private long likeCount;
	
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public long getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(long likeCount) {
		this.likeCount = likeCount;
	}

	@Override
	public String toString() {
		return "WendaResult [code=" + code + ", data=" + data + ", msg=" + msg + ", likeCount=" + likeCount + "]";
	}
	
}

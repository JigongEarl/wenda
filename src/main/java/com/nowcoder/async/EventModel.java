package com.nowcoder.async;

import java.util.HashMap;
import java.util.Map;

/**
 * 异步队列-->事件模板
 * 
 * 
  * 该类设计中有两个点需要注意
 * 1、扩展字段的设置(Map类型)，用于保存可能产生的处理信息。提供set处理方法；
 * 2、每一个set方法中添加返回，返回对象本身(this)，实现类似于XXX.append(XX).append(YY)这样的连续操作；
 * @author 86156
 *
 */
public class EventModel {
	/**
	 * 如A给B的某一“问题回答”点赞，时间类型：点赞；时间发起者：A; 实体的id/类型：“问题回答的id/类型”；实体拥有者：B
	 */
	private EventType type;  //事件类型
	private int actorId;   //事件发起者
	private int entityId;  //事件作用于的实体的id
	private int entityType; //事件作用于的实体的类型
	private int entityOwnerId;  //实体的拥有者
	//事件处理时可能需要有各种信息需要保留,预留一个扩展字段
	private Map<String, String> exts = new HashMap<String, String>();
	
	public EventModel() {}
	
	public EventModel(EventType type) {
		this.type = type;
	}
	
	public EventModel setExt(String key, String value) {
		exts.put(key, value);
		return this;
	}
	
	public String getExt(String key) {
		return exts.get(key);
	}
	
	public EventType getType() {
		return type;
	}
	
	public EventModel setType(EventType type) {
		this.type = type;
		return this;
	}
	
	public int getActorId() {
		return actorId;
	}
	
	public EventModel setActorId(int actorId) {
		this.actorId = actorId;
		return this;
	}
	public int getEntityId() {
		return entityId;
	}
	
	public EventModel setEntityId(int entityId) {
		this.entityId = entityId;
		return this;
	}
	
	public int getEntityType() {
		return entityType;
	}
	
	public EventModel setEntityType(int entityType) {
		this.entityType = entityType;
		return this;
	}
	
	public int getEntityOwnerId() {
		return entityOwnerId;
	}
	
	public EventModel setEntityOwnerId(int entityOwnerId) {
		this.entityOwnerId = entityOwnerId;
		return this;
	}
	
	public Map<String, String> getExts() {
		return exts;
	}

	public EventModel setExts(Map<String, String> exts) {
		this.exts = exts;
		return this;
	}

	@Override
	public String toString() {
		return "EventModel [type=" + type + ", actorId=" + actorId + ", entityId=" + entityId + ", entityType="
				+ entityType + ", entityOwnerId=" + entityOwnerId + ", exts=" + exts + "]";
	}
	
	
}

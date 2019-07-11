package com.nowcoder.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 前缀树
 * @author 86156
 *
 */
public class TriedNode {
	
	/**
	 * 是否为敏感词结尾
	 */
	private boolean end = false;
	
	/**
	 * 当前节点的子节点集，key表示对应的字符
	 */
	private Map<Character, TriedNode> subNodes = new HashMap<>();
	
	/**
	 * 向指定位置添加节点树
	 * @param c
	 * @param node
	 */
	public void addSubNode(Character key, TriedNode node) {
		subNodes.put(key, node);
	}
	
	/**
	 * 获取下一个指定子节点树
	 * @param key
	 * @return
	 */
	public TriedNode getSubNode(Character key) {
		return subNodes.get(key);
	}
	
	public boolean isKeywordEnd() {
         return end;
     }

     public void setKeywordEnd(boolean end) {
         this.end = end;
     }

     public int getSubNodeCount() {
         return subNodes.size();
     }
}

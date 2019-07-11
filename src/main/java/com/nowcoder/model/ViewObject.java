package com.nowcoder.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 向velocity传值的对象
 * @author 86156
 *
 */
public class ViewObject {
    
	private Map<String, Object> objs = new HashMap<String, Object>();
    
    public void set(String key, Object value) {
        objs.put(key, value);
    }

    public Object get(String key) {
        return objs.get(key);
    }
}

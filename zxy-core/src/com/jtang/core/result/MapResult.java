package com.jtang.core.result;

import java.util.HashMap;
import java.util.Map;

import com.jtang.core.protocol.StatusCode;

/**
 * map类型的结果集封装
 * @author vinceruan
 *
 * @param <T>
 * @param <S>
 */
public class MapResult<T, S> {
	public short statusCode = 0;
	
	public Map<T, S> item = new HashMap<T, S>();
	
	public void put(T t, S s) {
		item.put(t, s);
	}
	
	public static <T, S> MapResult<T, S> StatusCode(short statusCode) {
		MapResult<T, S> result = new MapResult<T, S>();
		result.statusCode = statusCode;
		return result;
	}
	
	public  boolean isFail() {
		return statusCode != StatusCode.SUCCESS;
	}
	
	public boolean isOk() {
		return statusCode == StatusCode.SUCCESS;
	}
}

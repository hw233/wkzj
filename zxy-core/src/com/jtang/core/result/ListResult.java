package com.jtang.core.result;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.protocol.StatusCode;

/**
 * 列表类型的结果集
 * @author 0x737263
 *
 * @param <T>
 */
public class ListResult<T extends Object> {
	
	public short statusCode;
	
	public List<T> item = new ArrayList<T>();
	
	public ListResult() {
		
	}
	
	public void add(T t) {
		item.add(t);
	}
	
	/**
	 * 错误返回
	 * @param statusCode
	 * @return
	 */
	public static <T extends Object> ListResult<T> statusCode(short statusCode) {
		ListResult<T> result = new ListResult<T>();
		result.statusCode = statusCode;
		return result;
	}
	
	/**
	 * 返回成功
	 * @param t
	 * @return
	 */
	public static <T extends Object> ListResult<T> list(T t) {
		ListResult<T> result = new ListResult<T>();
		result.item.add(t);
		return result;
	}
	
	/**
	 * 返回成功
	 * @param item
	 * @return
	 */
	public static <T extends Object> ListResult<T> list(List<T> item) {
		ListResult<T> result = new ListResult<T>();
		result.item = item;
		return result;
	}
	
	public  boolean isFail() {
		return statusCode != StatusCode.SUCCESS;
	}
	
	public boolean isOk() {
		return statusCode == StatusCode.SUCCESS;
	}
}

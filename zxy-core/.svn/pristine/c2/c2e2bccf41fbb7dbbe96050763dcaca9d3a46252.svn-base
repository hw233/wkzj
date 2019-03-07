package com.jtang.core.result;

import com.jtang.core.protocol.StatusCode;

/**
 * 对象类型的结果集
 * @author 0x737263
 *
 * @param <T>
 */
public class TResult<T extends Object> {
	
	public short statusCode;
	
	public T item;
		
	public static <T> TResult<T> sucess(T result) {
		TResult<T> res = new TResult<T>();
		res.item = result;
		res.statusCode = StatusCode.SUCCESS;
		return res;
	}
	
	public static <T> TResult<T> valueOf(short statusCode) {
		TResult<T> result = new TResult<T>();
		result.statusCode = statusCode;
		return result;
	}
	
	public boolean isFail() {
		return statusCode != StatusCode.SUCCESS;
	}
	
	public boolean isOk() {
		return statusCode == StatusCode.SUCCESS;
	}
}

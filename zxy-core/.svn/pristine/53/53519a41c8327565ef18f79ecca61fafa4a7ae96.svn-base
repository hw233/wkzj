package com.jtang.core.result;

import com.jtang.core.protocol.StatusCode;

/**
 * 最简单的结果类,返回一个状态啥的用这个
 * @author 0x737263
 *
 */
public class Result {

	public short statusCode = 0;

	public Result() {
	}
	
	public Result(short statusCode) {
		this.statusCode = statusCode;
	}
	
	public static Result valueOf() {
		return new Result();
	}

	public static Result valueOf(short statusCode) {
		return new Result(statusCode);
	}
	
	public  boolean isFail() {
		return statusCode != StatusCode.SUCCESS;
	}
	
	public boolean isOk() {
		return statusCode == StatusCode.SUCCESS;
	}
}

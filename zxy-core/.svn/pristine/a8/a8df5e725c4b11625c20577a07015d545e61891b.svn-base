package com.jtang.core.mina;

import java.nio.charset.Charset;

/**
 * socket相关约束常量
 * @author 0x737263
 *
 */
public class SocketConstant {
	
	// ----------------------------------------------------------------------------------
	// 客户端与服务端通讯包结构解析
	// headerFlag	int		包头标识 HEADER_FLAG
	// packageLen	int	 	整个包的长度
	// hashCode   	int		效验值. hashcode = hash(value)
	// module     	byte	模块id.用于派 发数据到具体的模块handler
	// cmd        	byte	命令id.用于区分具体的接收方法
	// statuscode 	short	响应的状态码{@see ResponseCode}.有错误状态时才会带该状态码
	// value      	byte[]	具体的消息内容.有可能是单个值，有可能是个vo
	// 
	// 服务端[接收]完整的包顺序:
	// packageLen(4) + module(1) + cmd(1) + hashCode(4) + value(n)
	// 服务端[响应]完整的包顺序：
	// packageLen(4) + module(1) + cmd(1) + statuscode(2) + value(n) 
	// ----------------------------------------------------------------------------------

	
	// ----------------------------------------------------------------------------------
	// GAP与服务端通讯包结构解析
	// headerFlag	int		包头标识 HEADER_FLAG
	// packageLen	int	 	整个包的长度
	// module     	byte	模块id.用于派 发数据到具体的模块handler
	// cmd        	byte	命令id.用于区分具体的接收方法
	// statuscode 	short	响应的状态码{@see ResponseCode}.有错误状态时才会带该状态码
	// value      	byte[]	具体的消息内容.有可能是单个值，有可能是个vo
	// 
	// 服务端[接收]完整的包顺序:
	// packageLen(4) + module(1) + cmd(1) + value(n)
	// 服务端[响应]完整的包顺序：
	// packageLen(4) + module(1) + cmd(1) + statuscode(2) + value(n) 
	// ----------------------------------------------------------------------------------
	
	
	/**
	 * 编码utf-8
	 */
	public static final Charset CHARSET = Charset.forName("UTF-8");
	
	/**
	 * 包头标识值.(数值待改)
	 */
	public static final int HEADER_FLAG = -1860108940;
	
	/**
	 * 包头标识长度,代表HEADER_FLAG的字节数
	 */
	public static final int HEADER_FLAG_LENGTH = 4;
		
}

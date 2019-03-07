package com.jtang.gameserver.module.ally.handler.request;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * GPS坐标更新的请求，客户端将自己的坐标定时发送到服务端缓存的请求
 * @author pengzy
 *
 */
public class CoordinateUpdateRequest extends IoBufferSerializer {

	/**
	 * 经度
	 */
	public double longitude;
	/**
	 * 纬度
	 */
	public double latitude;
	/**
	 * 水平误差
	 */
	public double levelError;
	
	public CoordinateUpdateRequest(byte[] bytes){
		super(bytes);
	}
	
	@Override
	public void read() {
		longitude = readDouble();
		latitude = readDouble();
		levelError = readDouble();
	}

}

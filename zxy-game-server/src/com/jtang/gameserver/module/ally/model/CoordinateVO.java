package com.jtang.gameserver.module.ally.model;

public class CoordinateVO {

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
	
	
	public static CoordinateVO valueOf(double longitude, double latitude, double levelError) {
		CoordinateVO coordinate = new CoordinateVO();
		coordinate.longitude = longitude;
		coordinate.latitude = latitude;
		coordinate.levelError = levelError;
		return coordinate;
	}
	
}

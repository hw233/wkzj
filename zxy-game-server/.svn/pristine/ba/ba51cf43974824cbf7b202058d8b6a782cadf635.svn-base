package com.jtang.gameserver.module.ally.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.ally.model.CoordinateVO;
/**
 * 请求被添加盟友坐标的回复
 * @author pengzy
 *
 */
public class CoordinateResponse extends IoBufferSerializer{

	private CoordinateVO coordinate;
	
	/**
	 * 盟友数量
	 */
	private int allyNum;
	
	/**
	 * 等级
	 */
	private int actorLevel;
	
	public CoordinateResponse(CoordinateVO coordinate, int allyNum, int actorLevel) {
		this.coordinate = coordinate;
		this.allyNum = allyNum;
		this.actorLevel = actorLevel;
	}
	
	@Override
	public void write() {
		writeDouble(coordinate.longitude);
		writeDouble(coordinate.latitude);
		writeDouble(coordinate.levelError);
		this.writeInt(allyNum);
		this.writeInt(actorLevel);
	}

}

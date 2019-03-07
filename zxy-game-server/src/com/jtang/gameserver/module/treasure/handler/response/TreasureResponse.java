package com.jtang.gameserver.module.treasure.handler.response;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.demon.model.OpenTime;
import com.jtang.gameserver.module.treasure.model.TreasureVO;

public class TreasureResponse extends IoBufferSerializer {
	
	/**
	 * 迷宫是否开启
	 * 0.关闭 1.开启
	 */
	private int isOpen;
	
	/**
	 * 整个地图信息
	 */
	private TreasureVO treasureVO;
	
	/**
	 * 迷宫寻宝开启结束时间
	 */
	List<OpenTime> openTimes = new ArrayList<>();

	public TreasureResponse(TreasureVO treasureVO,List<OpenTime> openTimes){
		this.treasureVO = treasureVO;
		this.openTimes = openTimes;
		this.isOpen = 1;
	}
	
	public TreasureResponse(boolean isOpen){
		this.isOpen = isOpen?1:0;
	}
	
	@Override
	public void write() {
		if(isOpen == 1){
			writeInt(isOpen);
			writeBytes(treasureVO.getBytes());
			this.writeShort((short) this.openTimes.size());
			for (OpenTime openTime : openTimes) {
				this.writeBytes(openTime.getBytes());
			}
		}else{
			writeInt(isOpen);
		}
	}
}

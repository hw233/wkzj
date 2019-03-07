package com.jtang.gameserver.module.extapp.welkin.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.dataconfig.model.WelkinGlobalConfig;
import com.jtang.gameserver.dataconfig.service.WelkinService;
import com.jtang.gameserver.module.extapp.welkin.module.WelkinVO;

public class WelkinResponse extends IoBufferSerializer {

	/**
	 * 开始时间
	 */
	public int startTime;
	/**
	 * 结束时间
	 */
	public int endTime;
	
	/**
	 * 天宫信息
	 */
	public WelkinVO welkinVO;
	
	/**
	 * 每次消耗多少金币
	 */
	public int gold;
	
	/**
	 * 金币使用次数
	 */
	public int goldNum;
	
	/**
	 * 每个区域到达的区间
	 * 30_100_300
	 */
	public String place;
	
	
	public WelkinResponse(WelkinVO welkinVO,int gold,int goldNum,String place) {
		this.welkinVO = welkinVO;
		WelkinGlobalConfig globalConfig = WelkinService.getWelKinGlobalConfig();
		this.startTime = globalConfig.start;
		this.endTime = globalConfig.end;
		this.gold = gold;
		this.goldNum = goldNum;
		this.place = place;
	}
	
	@Override
	public void write() {
		writeInt(startTime);
		writeInt(endTime);
		writeBytes(welkinVO.getBytes());
		writeInt(gold);
		writeInt(goldNum);
		writeString(place);
	}

}

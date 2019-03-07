package com.jtang.gameserver.module.demon.handler.response;

import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.demon.model.OpenTime;
/**
 * 集众降魔时间回复
 * @author ludd
 *
 */
public class DemonTimeResponse extends IoBufferSerializer {

	/**
	 * 开放日期
	 */
	private int openDate;
	/**
	 * 开放时间
	 */
	private List<OpenTime> openTimes;
	/**
	 * 兑换星期
	 */
	private List<Integer> exchangeWeek;
	/**
	 * 兑换时间
	 */
	private List<OpenTime> exchangeTime;
	
	
	
	public DemonTimeResponse(int openDate, List<OpenTime> openTimes, List<Integer> exchangeWeek, List<OpenTime> exchangeTime) {
		super();
		this.openDate = openDate;
		this.openTimes = openTimes;
		this.exchangeWeek = exchangeWeek;
		this.exchangeTime = exchangeTime;
	}



	@Override
	public void write() {
		this.writeInt(this.openDate);
		this.writeShort((short) this.openTimes.size());
		for (OpenTime openTime : openTimes) {
			this.writeBytes(openTime.getBytes());
		}
		
		this.writeIntList(this.exchangeWeek);
		
		this.writeShort((short) this.exchangeTime.size());
		for (OpenTime openTime : exchangeTime) {
			this.writeBytes(openTime.getBytes());
		}
	}

}

package com.jtang.gameserver.module.ally.handler.response;

import java.util.Collection;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.ally.model.AllyVO;

/**
 * 请求好友列表的回复
 * @author pengzy
 *
 */
public class AllyListResponse extends IoBufferSerializer{

	/**
	 * 盟友列表
	 */
	private Collection<AllyVO> allyList;
	/**
	 * 重置倒计时
	 */
	private int countDownSeconds;
	
	/**
	 * 今日已切磋次数
	 */
	private int dayFightCount;
	
	public AllyListResponse(Collection<AllyVO> allyList, int countDownSeconds, int dayFightCount){
		this.allyList = allyList;
		this.countDownSeconds = countDownSeconds;
		this.dayFightCount = dayFightCount;
	}
	@Override
	public void write() {
		writeAllyVOList();
		writeFightCountVO();
	}
	
	private void writeAllyVOList() {
		writeShort((short)allyList.size());
		for(AllyVO allyVO : allyList){
			allyVO.writePacket(this);
		}
	}
	private void writeFightCountVO() {
		writeInt(countDownSeconds);
		writeInt(dayFightCount);
	}
}

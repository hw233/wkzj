package com.jtang.gameserver.module.chat.handler.response;

import java.util.List;

import com.jtang.core.model.RewardObject;
import com.jtang.gameserver.module.icon.model.IconVO;

public class WelkinChatResponse extends ChatResponse {

	/**
	 * 类型
	 * 0.配置奖励 
	 * 其他位置奖励
	 */
	public int type;
	
	/**
	 * 奖励列表
	 */
	public List<RewardObject> list;
	
	public WelkinChatResponse(int msgType, String sendName, long actorId,
			int level, int vipLevel,int type,List<RewardObject> list,IconVO iconVO) {
		super(msgType, sendName, actorId, level, vipLevel,iconVO);
		this.type = (byte) type;
		this.list = list;
	}
	
	@Override
	public void write() {
		super.write();
		writeByte((byte)type);
		writeShort((short) list.size());
		for(RewardObject rewardObject:list){
			writeBytes(rewardObject.getBytes());
		}
	}

}

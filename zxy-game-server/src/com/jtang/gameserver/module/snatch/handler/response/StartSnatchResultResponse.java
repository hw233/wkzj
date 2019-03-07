package com.jtang.gameserver.module.snatch.handler.response;

import java.util.List;

import com.jtang.core.model.RewardObject;
import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.battle.model.FightData;
import com.jtang.gameserver.module.snatch.model.SnatchVO;

/**
 * 抢夺魂魄，碎片，金币战斗结果
 * @author liujian
 *
 */
public class StartSnatchResultResponse extends IoBufferSerializer {

 
	/**
	 * 抢夺战斗结果
	 */
	public FightData fightData;
	
	/**
	 * 抢夺结果
	 */
	public SnatchVO snatchVO;
	
	/**
	 * 抢夺奖励
	 */
	private List<RewardObject> rewardList;
	
	/**
	 * 剩余抢夺次数
	 */
	public int snatchNum;

	public StartSnatchResultResponse(FightData fightData, SnatchVO snatchVO, List<RewardObject> rewardList,int snatchNum) {
		this.fightData = fightData;
		this.snatchVO = snatchVO;
		this.rewardList = rewardList;
		this.snatchNum = snatchNum;
	}

	@Override
	public void write() {
		writeFightResult();
		writeSnatchResult();
		writeShort((short) rewardList.size());
		for(RewardObject rewardObject:rewardList){
			writeBytes(rewardObject.getBytes());
		}
		writeInt(snatchNum);
	}
	
	private void writeFightResult(){
//		fightData.setIoBuffer(buffer);
//		fightData.write();
		writeBytes(fightData.getBytes());
	}
	
	private void writeSnatchResult(){
		writeInt(snatchVO.result);
		writeInt(snatchVO.score);
		writeInt(snatchVO.goodsId);
		writeInt(snatchVO.goodsNum);
		writeLong(snatchVO.golds);
	}

	public String format() {
		StringBuilder sb = new StringBuilder();
		sb.append(fightData.format(""));
		sb.append("snatch result:" + snatchVO.result + "\t\n");
		sb.append("snatch score:" + snatchVO.score + "\t\n");
		sb.append("snatch id:" + snatchVO.goodsId + "\t\n");
		sb.append("snatch number:" + snatchVO.goodsNum + "\t\n");
		return sb.toString();
	}

}

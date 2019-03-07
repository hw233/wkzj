package com.jtang.gameserver.module.treasure.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.dataconfig.service.TreasureService;
import com.jtang.gameserver.module.treasure.type.MoveType;

public class TreasureVO extends IoBufferSerializer {

	/**
	 * 角色id
	 */
	public long actorId;

	/**
	 * 格子列表
	 */
	public Map<String, GridVO> map;

	/**
	 * 玩家所在格子横坐标
	 */
	public int gridX; 

	/**
	 * 玩家所在格子纵坐标
	 */
	public int gridY;

	/**
	 * 消耗点券次数
	 */
	public int ticketCount;
	
	/**
	 * 玩家消耗免费次数
	 * @return
	 */
	public int useNum;
	
	/**
	 * 获取兑换物品的数量
	 */
	public int getExchangeNum;
	
	/**
	 * 每走一步消耗的点券数量
	 */
	public int costTicket;
	
	/**
	 * 免费次数上限
	 */
	public int maxCount;
	
	/**
	 * 保底步数
	 */
	public int leastStep;
	
	/**
	 * 保底需要走的格子
	 * 方向(MoveType)_走几步|...
	 */
	public Map<MoveType,Integer> step = new HashMap<>();

	public static TreasureVO valueOf(long actorId,int level) {
		TreasureVO treasureVO = new TreasureVO();
		treasureVO.actorId = actorId;
		treasureVO.map = TreasureService.getGridMap(level);
		treasureVO.gridX = 0;
		treasureVO.gridY = 0;
		treasureVO.ticketCount = 0;
		treasureVO.useNum = 0;
		treasureVO.getExchangeNum = TreasureService.getExchangeNum(level);
		treasureVO.costTicket = TreasureService.getCostTicket(level);
		treasureVO.maxCount = TreasureService.getmaxCount();
		treasureVO.leastStep = TreasureService.getleastStep();
		return treasureVO;
	}

	/**
	 * 格子是否走完了
	 * 
	 * @return
	 */
	public boolean isMoveOver() {
		boolean isEnd = true;
		for (String key : map.keySet()) {
			GridVO vo = map.get(key);
			if (vo.isGet() == false) {
				isEnd = false;
			}
		}
		return isEnd;
	}

	@Override
	public void write() {
		writeLong(actorId);
		writeShort((short) map.size());
		for (Entry<String, GridVO> entry : map.entrySet()) {
			writeString(entry.getKey());
			writeBytes(entry.getValue().getBytes());
		}
		writeInt(gridX);
		writeInt(gridY);
		writeInt(ticketCount);
		writeInt(useNum);
		writeInt(getExchangeNum);
		writeInt(costTicket);
		writeInt(maxCount);
	}

	public void reset(int level) {
		map = TreasureService.getGridMap(level);
		gridX = 0;
		gridY = 0;
		ticketCount = 0;
		useNum = 0;
	}

}

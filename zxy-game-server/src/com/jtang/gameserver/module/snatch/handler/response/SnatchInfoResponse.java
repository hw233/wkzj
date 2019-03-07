package com.jtang.gameserver.module.snatch.handler.response;

import java.util.List;
import java.util.Map;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.core.rhino.FormulaHelper;
import com.jtang.gameserver.dataconfig.model.SnatchConfig;
import com.jtang.gameserver.dataconfig.service.SnatchService;
import com.jtang.gameserver.dbproxy.entity.Snatch;
import com.jtang.gameserver.module.snatch.model.SnatchEnemyVO;

/**
 * 抢夺基本信息
 * @author liujian
 *
 */
public class SnatchInfoResponse extends IoBufferSerializer {

	/**
	 * 积分
	 */
	public int score;
	
	/**
	 * 成就进度列表
	 */
	public Map<Integer, Integer> achiments;
	
	/**
	 * 抢夺敌人列表
	 */
	public List<SnatchEnemyVO> enemyList;
	
	/**
	 * 抢夺次数
	 */
	public int snatchNum;
	
	/**
	 * 抢夺购买次数
	 */
	public int buySnatchNum;
	
	/**
	 * 下次购买消耗的点券
	 */
	public int costTicket;
	
	/**
	 * 抢夺次数上限
	 */
	public int snatchMaxNum;
	
	/**
	 * 下次刷新战斗次数需要的时间
	 */
	public int flushFightTime;
	
	
	public SnatchInfoResponse(Snatch snatch,List<SnatchEnemyVO> enemyList) {
		this.score = snatch.score;
		this.achiments = snatch.getAchimentMap();
		this.enemyList = enemyList;
		this.snatchNum = snatch.snatchNum;
		this.buySnatchNum = snatch.ticketNum;
		SnatchConfig config = SnatchService.get();
		this.costTicket = FormulaHelper.executeCeilInt(config.costTicket, snatch.ticketNum);
		this.snatchMaxNum = config.snatchMaxNum;
		int nextFlushTime = snatch.flushFightTime + SnatchService.get().flushTime;
		this.flushFightTime = nextFlushTime;
	}

	@Override
	public void write() {
		writeInt(score);
		writeIntMap(achiments);
		writeShort((short) enemyList.size());
		for(SnatchEnemyVO snatchVO:enemyList){
			snatchVO.writePacket(this);
		}
		writeInt(snatchNum);
		writeInt(buySnatchNum);
		writeInt(costTicket);
		writeInt(snatchMaxNum);
		writeInt(flushFightTime);
	}

}

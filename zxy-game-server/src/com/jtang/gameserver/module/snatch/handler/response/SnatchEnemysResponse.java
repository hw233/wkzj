package com.jtang.gameserver.module.snatch.handler.response;

import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.snatch.model.SnatchEnemyVO;

/**
 * 抢夺获取敌人回复
 * @author liujian
 *
 */
public class SnatchEnemysResponse extends IoBufferSerializer {

	/**
	 * 抢夺敌人列表
	 */
	public List<SnatchEnemyVO> enemyList;
	
	
	public SnatchEnemysResponse(List<SnatchEnemyVO> list) {
		this.enemyList = list;
	}

	@Override
	public void write() {
		writeShort((short) enemyList.size());
		for (SnatchEnemyVO enemy : enemyList) {
			enemy.writePacket(this);
		}
	}


}

package com.jtang.gameserver.module.adventures.bable.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.adventures.bable.model.BableBattleResult;

/**
 * 登天塔战斗结果
 * @author ludd
 *
 */
public class BableBattleResultResponse extends IoBufferSerializer {

	/**
	 * 战斗结果数据 {@code BableBattleResult}
	 */
	private BableBattleResult bableBattleResult;
	
	

	public BableBattleResultResponse(BableBattleResult bableBattleResult) {
		super();
		this.bableBattleResult = bableBattleResult;
	}



	@Override
	public void write() {
		this.writeBytes(bableBattleResult.getBytes());
	}
	
}

package com.jtang.gameserver.module.treasure.dao;

import com.jtang.gameserver.dbproxy.entity.Treasure;
import com.jtang.gameserver.module.treasure.model.TreasureVO;

public interface TreasureDao {

	/**
	 * 获取角色的寻宝信息
	 * 
	 * @param actorId
	 * @return
	 */
	public TreasureVO getTreasureVO(long actorId, int level);

	/**
	 * 获取角色使用次数信息
	 */
	public Treasure getTreasure(long actorId);

	/**
	 * 更新玩家使用次数
	 */
	public void update(Treasure treasure);

	/**
	 * 清理迷宫数据
	 */
	public void clean();

}

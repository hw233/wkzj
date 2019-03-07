package com.jtang.gameserver.module.app.dao;


import java.util.List;

import com.jtang.gameserver.dbproxy.entity.AppGlobal;
import com.jtang.gameserver.module.app.model.extension.BaseGlobalInfoVO;

public interface AppGlobalDao {
	/**
	 * 获取
	 * @param appId
	 * @return
	 */
	AppGlobal get(long appId);
	/**
	 * 获取扩展字段vo
	 * @param appId
	 * @return
	 */
	<T extends BaseGlobalInfoVO> T getGloabalInfoVO(long appId);
	
	/**
	 * 更新
	 * @param appGlobal
	 */
	void update(AppGlobal appGlobal);
	
	/**
	 * 获取最高等级的角色Id
	 * @return
	 */
	long getMaxLevelOfAcotr();
	
	/**
	 * 获取最高积分的角色id
	 * @return
	 */
	long getMaxScoreOfActor();
	
	/**
	 * 获取最强势力第一的角色id
	 * @return
	 */
	long getMaxPowerOfActor();
	
	/**
	 * 获取充值前三名玩家
	 * @return
	 */
	public List<Long> getMaxPayMoney(int startTime,int endTime);
}

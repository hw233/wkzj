package com.jtang.gameserver.module.app.facade;

import java.util.Map;

import com.jtang.core.model.RewardObject;
import com.jtang.core.result.ListResult;
import com.jtang.core.result.Result;
import com.jtang.gameserver.module.app.model.AppGlobalVO;
import com.jtang.gameserver.module.app.model.AppRecordVO;

/**
 * 应用接口
 * @author 0x737263
 *
 */
public interface AppFacade {

	/**
	 * 获取活动配置
	 * @param appId 
	 * @return
	 */
	AppGlobalVO getAppGlobalVO(long actorId,long appId);
	
	/**
	 * 领取奖励
	 * @param actorId
	 * @param appId 
	 * @return
	 */
	ListResult<RewardObject> getReward(long actorId, long appId, Map<String, String> paramsMaps);
	
	/**
	 * 获取活动记录
	 * @param actorId 
	 * @param appId
	 * @return
	 */
	AppRecordVO getAppRecord(long actorId, long appId);
	
	/**
	 * 当前活动是否开启
	 * @param actorId 
	 * @param id 
	 * @return
	 */
	Result appEnable(long actorId, long id);
	
}

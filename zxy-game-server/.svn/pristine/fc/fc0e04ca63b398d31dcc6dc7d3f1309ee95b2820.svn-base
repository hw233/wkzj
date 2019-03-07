package com.jtang.gameserver.module.app.dao;

import java.util.Map;

import com.jtang.gameserver.dbproxy.entity.AppRecord;
import com.jtang.gameserver.module.app.model.extension.BaseRecordInfoVO;

public interface AppRecordDao {
	/**
	 * 获取所有活动
	 * @param actorId
	 * @return
	 */
	Map<Long, AppRecord> getAll(long actorId);
	
	/**
	 * 获取一个活动
	 * @param actorId
	 * @param appId
	 * @return
	 */
	AppRecord get(long actorId, long appId);
	
	/**
	 * 获取扩展字段vo
	 * @param actorId
	 * @param appId
	 * @return
	 */
	<T extends BaseRecordInfoVO> T getRecordInfoVO(long actorId, long appId);
	
	/**
	 * 更新
	 * @param appRecord
	 */
	void update(AppRecord appRecord);
	
	/**
	 * 清理一条活动记录
	 * @param actorId
	 * @param appId
	 * @return
	 */
	boolean resetRecord(long actorId,long appId);
	
	/**
	 * 删除角色活动记录
	 * @param actorId
	 * @return
	 */
	boolean resetRecord(long actorId);
	
}

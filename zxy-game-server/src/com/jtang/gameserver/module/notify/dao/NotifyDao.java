package com.jtang.gameserver.module.notify.dao;

import java.util.List;

import com.jtang.gameserver.dbproxy.entity.Notify;
import com.jtang.gameserver.module.notify.model.AbstractNotifyVO;

public interface NotifyDao {
	
	/**
	 * 获取Notify
	 * @param actorId
	 * @return
	 */
	Notify get(long actorId);
	
	/**
	 * 获取某条信息通知
	 * @param actorId
	 * @param nId
	 * @return
	 */
	AbstractNotifyVO getNotifyVO(long actorId, long nId);
	
	/**
	 * 获取角色的已发箱列表
	 * @param actorId
	 * @return
	 */
	List<AbstractNotifyVO> getSendList(long actorId);
	
	/**
	 * 获取角色已收箱列表
	 * @param actorId
	 * @return
	 */
	List<AbstractNotifyVO> getReceiveList(long actorId);
	
	/**
	 * 更新某条信息通知
	 * @param actorId
	 * @return
	 */
	boolean update(Notify notify);
	
}

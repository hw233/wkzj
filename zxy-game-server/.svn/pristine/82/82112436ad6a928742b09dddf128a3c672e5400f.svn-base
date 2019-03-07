package com.jtang.gameserver.module.icon.facade;

import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.module.icon.hander.response.IconResponse;
import com.jtang.gameserver.module.icon.model.IconVO;

public interface IconFacade {

	/**
	 * 获取头像信息
	 * @param actorId 
	 * @return
	 */
	public TResult<IconResponse> getIconInfo(long actorId);

	/**
	 * 设置头像
	 * @param iocn
	 * @return
	 */
	public Result setIcon(long actorId,int iocn);

	/**
	 * 设置边框
	 * @param iocn
	 * @return
	 */
	public Result setFram(long actorId,int iocn);
	
	/**
	 * 获取现在使用的头像和边框
	 */
	public IconVO getIconVO(long actorId);
	
	/**
	 * 随机获取抢夺机器人的头像和边框
	 */
	public IconVO getRobotIconVO();

	/**
	 * 解锁一个头像
	 * @param actorId
	 * @param unLockIcon
	 */
	public void unLock(long actorId, int unLockIcon);
	
	/**
	 * 设置性别
	 * @param actorId
	 * @param sex
	 * @return
	 */
	public Result setSex(long actorId, byte sex);
	
	/**
	 * 结婚解锁头像
	 * @param actorId
	 */
	public boolean marryUnLock(long actorId);
	
	/**
	 * 离婚锁定头像
	 * @param actorId
	 */
	public boolean marryLock(long actorId);

}

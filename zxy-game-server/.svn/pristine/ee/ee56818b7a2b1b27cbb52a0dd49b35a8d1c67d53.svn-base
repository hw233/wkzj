package com.jtang.gameserver.module.sysmail.facade;

import java.util.List;

import com.jtang.core.model.RewardObject;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.dbproxy.entity.Sysmail;
import com.jtang.gameserver.module.sysmail.type.SysmailType;

public interface SysmailFacade {
	
	/**
	 * 获取系统邮件列表
	 * @param actorId
	 * @return
	 */
	List<Sysmail> getList(long actorId);
	
	/**
	 * 获取某封邮件的附件物品
	 * @param actorId
	 * @param sysMailId
	 * @return
	 */
	TResult<Sysmail> getAttach(long actorId,long sysMailId);
	
	/**
	 * 删除某封邮件
	 * @param actorId
	 * @param sysMailId
	 * @return
	 */
	Result remove(long actorId,long sysMailId);
	
	/**
	 * 发送邮件
	 * @param actorId
	 * @param mailType 邮件type
	 * @param list 奖励列表
	 * @param args 参数列表
	 * @return
	 */
	void sendSysmail(long actorId, SysmailType mailType, List<RewardObject> list, Number... args);
	/**
	 * 发送邮件
	 * @param actorId
	 * @param mailType 邮件type
	 * @param list 奖励列表
	 * @param args 参数列表
	 * @return
	 */
	void sendSysmail(long actorId, SysmailType mailType, List<RewardObject> list, String... args);

	/**
	 * 平台发送邮件
	 * @param actorId
	 * @param list
	 * @param text
	 * @return
	 */
	Result channelSendMail(long actorId, List<RewardObject> list, String text);

	/**
	 * 一键收取邮件
	 * @param actorId
	 * @return
	 */
	Result oneKeyGetAttach(long actorId);
}

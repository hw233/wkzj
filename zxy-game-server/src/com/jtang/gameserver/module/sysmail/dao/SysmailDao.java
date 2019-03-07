package com.jtang.gameserver.module.sysmail.dao;

import java.util.List;

import com.jtang.gameserver.dbproxy.entity.Sysmail;
//import com.jtang.sm2.component.model.RewardObject;

public interface SysmailDao {
	
	/**
	 * 获取某一封邮件
	 * @param actorId
	 * @param sysMailId
	 * @return
	 */
	Sysmail get(long actorId, long sysMailId);
	
	/**
	 * 获取邮件列表
	 * @param actorId
	 * @param flushCache		是否刷新dao的缓存
	 * @return
	 */
	List<Sysmail> getList(long actorId, boolean flushCache);

	/**
	 * 删除某一封邮件
	 * @param actorId
	 * @param sysMailId
	 */
	void remove(long actorId, long sysMailId);

	/**
	 * 新建系统邮件
	 * @return
	 */
	Sysmail save(Sysmail sysmail);
	
	/**
	 * 更新系统邮件
	 */
	boolean update(Sysmail sysmail);

	/**
	 * 从数据库直接读取邮件
	 * @param actorId
	 * @return
	 */
	List<Sysmail> loadFromDB(Long actorId);

	/**
	 * 用数据库数据刷新缓存数据
	 * @param actorId
	 * @param dbMailList
	 */
	void flushCache(Long actorId, Sysmail sysmail);

	/**
	 * 读取缓存邮件
	 * @param actorId
	 * @return
	 */
	List<Sysmail> loadFromCache(Long actorId);
}

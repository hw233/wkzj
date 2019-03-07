package com.jtang.worldserver.module.crossbattle.dao;

import java.util.List;

import com.jtang.worldserver.dbproxy.entity.CrossBattle;

public interface CrossBattleDao {
	
	/**
	 * 获取某个服务器
	 * @param serverId
	 * @return
	 */
	CrossBattle getCrossBattle(int serverId);
	
	/**
	 * 更新某个服务器信息实体
	 * @param crossBattle
	 */
	void update(CrossBattle crossBattle);
	/**
	 * 更新某个服务器信息实体
	 * @param crossBattle
	 */
	void update(List<CrossBattle> crossBattle);
	
	/**
	 * 获取所有服务器信息实体
	 * @return
	 */
	List<CrossBattle> getList();
	
	/**
	 * 清理上赛季数据
	 */
	void deleteLast();

}

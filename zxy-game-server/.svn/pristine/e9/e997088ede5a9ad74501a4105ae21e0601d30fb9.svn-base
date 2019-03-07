package com.jtang.gameserver.module.extapp.welkin.dao;

import java.util.List;

import com.jtang.gameserver.dbproxy.entity.Welkin;

public interface WelkinDao {

	/**
	 * 获取天宫寻宝信息
	 * @param actorId
	 * @return
	 */
	public Welkin getWelkin(long actorId);

	/**
	 * 更新天宫寻宝
	 * @param welkin
	 */
	public void update(Welkin welkin);

	/**
	 * 获取排行榜
	 * @param count 上榜最低使用次数
	 * @param rank 排行只取前多少名
	 * @return
	 */
	public List<Welkin> getRank(int count, int rank); 
	
	/**
	 * 获取所有满足条件上榜的玩家
	 * @param count
	 * @return
	 */
	public List<Welkin> getRuank(int count);
	

}

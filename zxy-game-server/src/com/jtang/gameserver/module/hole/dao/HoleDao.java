package com.jtang.gameserver.module.hole.dao;

import java.util.List;

import com.jtang.gameserver.dbproxy.entity.Hole;

public interface HoleDao {

	/**
	 * 取得玩家的洞府列表
	 * @param acorId
	 * @return
	 */
	public List<Hole> getHoles(long actorId);

	/**
	 * 获得玩家的指定洞府
	 * @param id
	 * @param actorId
	 * @return
	 */
	public Hole getHole(long id, long actorId);

	/**
	 * 更新洞府
	 * @param hole
	 */
	public void updateHole(Hole hole);

	/**
	 * 开启洞府
	 * @param holeVO
	 */
	public long addHole(Hole hole);
//
//	/**
//	 * 清除过期洞府
//	 */
//	public void cleanHole(Hole hole);

	/**
	 * 获取玩家自身开启的洞府
	 * @param actorId
	 * @return
	 */
	public Hole getHole(long actorId);
	
	/**
	 * 获取玩家被邀请开启的指定洞府
	 * @param 邀请人id
	 * @param 被邀请人id
	 * @param 洞府id
	 */
	public Hole getHole(long acceptActor,long actor,int holeId);

}

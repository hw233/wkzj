package com.jtang.gameserver.module.hole.model;

import java.util.Collections;
import java.util.Map;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.dataconfig.model.HoleConfig;
import com.jtang.gameserver.dataconfig.service.HoleService;
import com.jtang.gameserver.dbproxy.entity.Hole;

/**
 * 洞府的信息
 * 
 * @author jianglf
 * 
 */
public class HoleVO extends IoBufferSerializer {

	/**
	 * 邀请类型 0.自己 1.盟友邀请
	 */
	public int type;

	/**
	 * 自增id
	 */
	public long id;

	/**
	 * 洞府id
	 */
	public int holeId;

	/**
	 * 角色id
	 */
	public long actorId;

	/**
	 * 洞府关闭剩余时间
	 */
	public int remainingTime;

	/**
	 * 战斗信息 key 洞府关卡id value 通关星数
	 */
	public Map<Integer, Integer> fightMap;

	/**
	 * 奖励信息 key 奖励星数 value 是否领取
	 */
	public Map<Integer, Integer> rewardMap;

	/**
	 * type为1则为空 我邀请的盟友 key 盟友id value 是否通关(0未通关1已通关)
	 */
	public Map<Long, Integer> acceptAllys;

	/**
	 * type为0则为空 邀请我的盟友
	 */
	public long ally;

	/**
	 * 盟友通关大礼包 0未领取 1已领取
	 */
	public int packageGift;

	/**
	 * 洞府总星级
	 */
	public byte holeStar;

	/**
	 * 邀请盟友的名字
	 */
	public String targetName;

	public static HoleVO valueOf(Hole hole, String targetName) {
		HoleVO holeVO = new HoleVO();
		holeVO.id = hole.getPkId();
		holeVO.actorId = hole.actorId;
		holeVO.holeId = hole.holeId;
		holeVO.type = hole.type;
		holeVO.remainingTime = hole.flushTime - TimeUtils.getNow();
		holeVO.fightMap = hole.getFightsMap();
		holeVO.rewardMap = hole.getRewardMap();
		holeVO.acceptAllys = hole.getAllyMap();
		holeVO.packageGift = hole.packageGift;
		holeVO.ally = hole.ally;
		HoleConfig config = HoleService.get(hole.holeId);
		if (hole.getFightsMap().size() == config.getHoleBattleNum()) {
			holeVO.holeStar = Collections.min(hole.getFightsMap().values()).byteValue();
		} else {
			holeVO.holeStar = 0;
		}
		holeVO.targetName = targetName;
		return holeVO;
	}

	@Override
	public void write() {
		writeInt(type);
		writeLong(id);
		writeInt(holeId);
		writeLong(actorId);
		writeInt(remainingTime);
		writeShort((short) fightMap.size());
		for (Integer key : fightMap.keySet()) {
			writeInt(key);
			writeInt(fightMap.get(key));
		}
		writeShort((short) rewardMap.size());
		for (Integer key : rewardMap.keySet()) {
			writeInt(key);
			writeInt(rewardMap.get(key));
		}
		writeShort((short) acceptAllys.size());
		for (Long key : acceptAllys.keySet()) {
			writeLong(key);
			writeInt(acceptAllys.get(key));
		}
		writeLong(ally);
		writeInt(packageGift);
		writeByte(holeStar);
		writeString(targetName);
	}
}

package com.jtang.gameserver.module.hole.handler.response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.battle.model.FightData;
import com.jtang.gameserver.module.hole.model.HoleBattleResult;

public class HoleFightResponse extends IoBufferSerializer {

	/**
	 * 战斗数据
	 */
	public FightData fightData;
	/**
	 * 战斗获得星级
	 */
	public byte battleStar;

	/**
	 * 洞府总星级
	 */
	public byte holeStar;

	/**
	 * 装备掉落
	 */
	public List<Long> equipList = new ArrayList<>();

	/**
	 * 仙人魂魄掉落
	 */
	public Map<Integer, Integer> herosoulMap = new HashMap<>();

	/**
	 * 物品掉落
	 */
	public Map<Long, Integer> awardGoods = new HashMap<>();
	
	/**
	 * 金币掉落
	 */
	public Map<Byte, Long> awardAttribute = new HashMap<>();
	
	/**
	 * 仙人获得的经验
	 */
	public Map<Integer, Integer> awardHeroExp;
	
	/**
	 * 洞府自增id
	 */
	public long id;
	
	/**
	 * 洞府关卡id
	 */
	public int holeBattleId;

	public HoleFightResponse(HoleBattleResult holeBattleResult) {
		this.fightData = holeBattleResult.fightData;
		this.battleStar = holeBattleResult.battleStar;
		this.holeStar = holeBattleResult.holeStar;
		this.equipList = holeBattleResult.equipList;
		this.herosoulMap = holeBattleResult.herosoulMap;
		this.awardGoods = holeBattleResult.awardGoods;
		this.awardAttribute = holeBattleResult.awardAttribute;
		this.awardHeroExp = holeBattleResult.awardHeroExp;
		this.id = holeBattleResult.id;
		this.holeBattleId = holeBattleResult.holeBattleId;
	}

	@Override
	public void write() {
		this.writeBytes(fightData.getBytes());
		this.writeByte(battleStar);
		this.writeByte(holeStar);
		this.writeShort((short) equipList.size());
		for (long equip : equipList) {
			this.writeLong(equip);
		}
		this.writeShort((short) herosoulMap.size());
		for (Integer key : herosoulMap.keySet()) {
			this.writeInt(key);
			this.writeInt(herosoulMap.get(key));
		}
		this.writeShort((short) awardGoods.size());
		for (long key : awardGoods.keySet()) {
			this.writeLong(key);
			this.writeInt(awardGoods.get(key));
		}
		this.writeShort((short) awardAttribute.size());
		for(Byte key:awardAttribute.keySet()){
			this.writeByte(key);
			this.writeLong(awardAttribute.get(key));
		}
		this.writeIntMap(this.awardHeroExp);
		this.writeLong(id);
		this.writeInt(holeBattleId);
	}

	public String format(String indentStr) {
		String lv1Indent = indentStr;
		StringBuilder sb = new StringBuilder();
		sb.append(fightData.format(lv1Indent));
		return sb.toString();
	}

}

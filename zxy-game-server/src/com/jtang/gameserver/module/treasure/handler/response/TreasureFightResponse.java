package com.jtang.gameserver.module.treasure.handler.response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.battle.model.FightData;
import com.jtang.gameserver.module.treasure.model.GridVO;
import com.jtang.gameserver.module.treasure.model.TreasureBattleResult;
import com.jtang.gameserver.module.treasure.type.MoveType;

public class TreasureFightResponse extends IoBufferSerializer {

	/**
	 * 是否战斗
	 * 0.未战斗 1.有战斗
	 */
	private int isFight;
	
	/**
	 * 玩家所在格子信息
	 */
	private GridVO gridVO;

	/**
	 * 玩家走了哪一步
	 * 
	 * @param gridVO
	 */
	private Entry<MoveType, Integer> entry;

	/**
	 * 玩家已经使用的次数
	 */
	private int useNum;

	/**
	 * 战斗数据
	 */
	public FightData fightData;
	/**
	 * 战斗获得星级
	 */
	public byte battleStar;

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
	public Map<Byte, Integer> awardAttribute = new HashMap<>();

	/**
	 * 仙人获得经验
	 */
	public int addHeroExp;
	
	/**
	 * 兑换物品的数量
	 */
	public int num;

	public TreasureFightResponse(int isFight, GridVO gridVO, Entry<MoveType, Integer> entry, int useNum,TreasureBattleResult treasureBattleResult,int num) {
		this.isFight = isFight;
		this.gridVO = gridVO;
		this.entry = entry;
		this.useNum = useNum;
		if(isFight == 1){
			this.fightData = treasureBattleResult.fightData;
			this.battleStar = treasureBattleResult.battleStar;
			this.equipList = treasureBattleResult.equipList;
			this.herosoulMap = treasureBattleResult.herosoulMap;
			this.awardGoods = treasureBattleResult.awardGoods;
			this.awardAttribute = treasureBattleResult.awardAttribute;
			this.addHeroExp = treasureBattleResult.addHeroExp;
		}
		this.num = num;
	}

	@Override
	public void write() {
		this.writeInt(isFight);
		this.writeInt(entry.getKey().getCode());
		this.writeInt(entry.getValue());
		this.writeBytes(gridVO.getBytes());
		this.writeInt(useNum);
		if(isFight == 1){
			this.writeBytes(fightData.getBytes());
			this.writeByte(battleStar);
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
			for (Byte key : awardAttribute.keySet()) {
				this.writeByte(key);
				this.writeInt(awardAttribute.get(key));
			}
			this.writeInt(addHeroExp);
		}
		this.writeInt(num);
	}

	public String format(String indentStr) {
		String lv1Indent = indentStr;
		StringBuilder sb = new StringBuilder();
		sb.append(fightData.format(lv1Indent));
		return sb.toString();
	}
}

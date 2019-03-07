package com.jtang.gameserver.module.story.handler.response;

import java.util.List;
import java.util.Map;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.module.battle.model.FightData;
import com.jtang.gameserver.module.story.helper.StoryHelper;
import com.jtang.gameserver.module.story.model.StoryBattleResult;

/**
 * 战斗数据推送
 * @author vinceruan
 *
 */
public class BattleDataResponse extends IoBufferSerializer {
	/**
	 * 战斗数据 
	 */
	FightData fightData;
	
	/**
	 * 奖励物品 key:唯一id, value:数量
	 */
	public Map<Long, Integer> awardGoods;
	
	/**
	 * 奖励仙人经验
	 * <pre>
	 * 格式是:
	 * Map<heroId,ExpCount>
	 * </pre>
	 */
	public Map<Integer, Integer> awardHeroExp;
	
	/**
	 * 关卡星级数
	 */
	public byte battleStar;
	
	/**
	 * 奖励的装备
	 * <pre>
	 * List<装备全局ID>
	 * </pre>
	 */
	public List<Long> awardEquips;
	
	/**
	 * 奖励的魂魄数,格式是Map<HeroSoulId, Count>
	 */
	public Map<Integer, Integer> awardHeroSouls;
	
	/**
	 * 奖励角色属性列表,格式是Map<Code,Value>, Code参考{@code ActorAttributeKey}
	 */
	public Map<Byte, Number> awardAttribute;
	
	
	public BattleDataResponse(StoryBattleResult data) {
		this.fightData = data.fightData;
		this.awardGoods = data.awardGoods;		
		this.awardEquips = data.equips;
		this.awardHeroExp = data.awardHeroExp;
		this.awardAttribute = data.awardAttribute;
		this.awardHeroSouls = data.awardHeroSouls;
		this.battleStar = StoryHelper.computeBattleStar(data.fightData.result);
	}
	
	@Override
	public void write() {
		writeFightData(fightData);
		writeLongIntMap(awardGoods); //writeStringList(awardGoods);
		writeIntMap(awardHeroExp);		
		writeByte(battleStar);
		writeLongList(awardEquips);
		writeIntMap(awardHeroSouls);
		writeShort((short)awardAttribute.size());
//		writeByteIntMap(awardAttribute);
		for (Map.Entry<Byte, Number> entry : awardAttribute.entrySet()) {
			this.writeByte(entry.getKey());
			this.writeObject(entry.getValue());
		}
	}
	
	private void writeFightData(FightData fightData) {
//		fightData.setIoBuffer(buffer);
//		fightData.write();
		this.writeBytes(fightData.getBytes());
	}
	
	public String format(String indentStr) {
		String lv1Indent = indentStr;
		
		StringBuilder sb = new StringBuilder();
		sb.append(fightData.format(lv1Indent));
		sb.append(String.format("awardGoods:%s\r\n", StringUtils.collection2SplitString(awardGoods.keySet(), ",")));
		sb.append(String.format("awardHeroExp:%s\r\n", awardHeroExp));
		sb.append(String.format("battleStar:%s\r\n", battleStar));
		sb.append(String.format("awardEquips:%s\r\n", StringUtils.collection2SplitString(awardEquips, ",")));
		sb.append(String.format("awardHeroSouls:%s\r\n", StringUtils.map2DelimiterString(awardHeroSouls,Splitable.ATTRIBUTE_SPLIT,Splitable.ELEMENT_DELIMITER)));
		sb.append(String.format("awardAttribute:%s\r\n", StringUtils.numberMap2String(awardAttribute)));		
		return sb.toString();
	}
}

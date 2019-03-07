package com.jtang.gameserver.module.lineup.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.model.AttackerAttributeKey;
import com.jiatang.common.model.BufferVO;
import com.jiatang.common.model.EquipVO;
import com.jiatang.common.model.HeroVO;
import com.jiatang.common.model.HeroVOAttributeKey;
import com.jiatang.common.model.LineupFightModel;
import com.jtang.core.result.ObjectReference;
import com.jtang.core.utility.CollectionUtils;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.dataconfig.model.HeroConfig;
import com.jtang.gameserver.dataconfig.model.PassiveSkillConfig;
import com.jtang.gameserver.dataconfig.service.HeroService;
import com.jtang.gameserver.dataconfig.service.SkillService;
import com.jtang.gameserver.dbproxy.entity.Lineup;
import com.jtang.gameserver.module.buffer.facade.BufferFacade;
import com.jtang.gameserver.module.buffer.model.HeroBuffer;
import com.jtang.gameserver.module.buffer.type.BufferSourceType;
import com.jtang.gameserver.module.equip.facade.EquipFacade;
import com.jtang.gameserver.module.hero.facade.HeroFacade;
import com.jtang.gameserver.module.hero.helper.HeroHelper;
import com.jtang.gameserver.module.lineup.constant.LineupRule;
import com.jtang.gameserver.module.lineup.facade.LineupFacade;
import com.jtang.gameserver.module.lineup.model.LineupHeadItem;
import com.jtang.gameserver.module.skill.effect.outbattle.OutBattleEffectContext;
import com.jtang.gameserver.module.skill.helper.SkillHeper;
import com.jtang.gameserver.module.skill.trigger.PassiveSkillTriggerParser;
import com.jtang.gameserver.module.skill.trigger.PassiveSkillTriggerParserContext;
import com.jtang.gameserver.module.skill.type.SkillTriggerItem;

@Component
public class LineupHelper {
	protected final Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	@Autowired
	HeroFacade heroFacade;
	
	@Autowired
	BufferFacade bufferFacade;
	
	@Autowired
	EquipFacade equipFacade;
	
	@Autowired
	PassiveSkillTriggerParserContext context;
	
	@Autowired
	private OutBattleEffectContext effectContext;
	
	@Autowired
	LineupFacade lineupFacade;
	
	private static final ObjectReference<LineupHelper> ref = new ObjectReference<LineupHelper>();
	
	@PostConstruct
	protected void init() {
		ref.set(this);
	}
	
	public static LineupHelper getInstance() {
		return ref.get();
	}
	

	/**
	 * 获取仙人在阵型中的位置(阵型中的位置索引从1开始)
	 * 
	 * @param hero
	 * @param lineup
	 */
	public static int findGridIndexInLineup(HeroVO hero, Lineup lineup) {
		for (LineupHeadItem item : lineup.getHeadItemList()) {
			if (item.heroId == hero.getHeroId()) {
				return item.gridIndex;
			}
		}
		return -1;
	}
	
	/**
	 * 获取参战人员对象
	 * @param actorId	角色id
	 * @return
	 */
	public static LineupFightModel getLineupFight(long actorId) {
		return getLineupFight(actorId, 9);
	}
	
	/**
	 * 获取参战人员对象
	 * @param actorId		角色id
	 * @param maxHeroNum	上限最大仙人数(不足该数则返回整个阵型人员)
	 * @return
	 */
	public static LineupFightModel getLineupFight(long actorId, int maxHeroNum) {
		LineupFightModel model = new LineupFightModel();

		Lineup lineup = getInstance().lineupFacade.getLineup(actorId);

		int addedCount = 0;
		for (LineupHeadItem item : lineup.getHeadItemList()) {
			if (item.heroId == 0) {
				continue;
			}
			if (addedCount >= maxHeroNum) {
				break;
			}
			HeroVO hero = getInstance().heroFacade.getHero(actorId, item.heroId);
			model.getHeros().put(item.gridIndex, hero);
			addedCount++;

			// 处理各种buffer加成
			getInstance().processAttributeChange(actorId, hero, item, model);
		}

		return model;
	}
	
	/**
	 * 获取仙人属性值（包括装备加成）
	 * @param actorId
	 * @param heroId
	 * @return
	 */
	public Map<AttackerAttributeKey, Integer> getEquipAndBaseAtt(long actorId, int heroId){
		boolean b = getInstance().lineupFacade.isHeroInLineup(actorId, heroId);
		Map<AttackerAttributeKey, Integer> map = new HashMap<>();
		HeroVO heroVO = heroFacade.getHero(actorId, heroId);
		if (b) {
			Lineup lineup = getInstance().lineupFacade.getLineup(actorId);
			LineupHeadItem[] list = lineup.getHeadItemList();
			for (LineupHeadItem lineupHeadItem : list) {
				if (lineupHeadItem.heroId == heroId){
					if (lineupHeadItem.atkEquipUuid != 0) {
						EquipVO equip = equipFacade.get(actorId, lineupHeadItem.atkEquipUuid);
						if (equip != null) {
							map.put(AttackerAttributeKey.ATK, heroVO.atk + equip.atk);
						} else {
							map.put(AttackerAttributeKey.ATK, heroVO.atk);
						}
					} else {
						map.put(AttackerAttributeKey.ATK, heroVO.atk);
					}
					if (lineupHeadItem.defEquipUuid != 0) {
						EquipVO equip = equipFacade.get(actorId, lineupHeadItem.defEquipUuid);
						if (equip != null) {
							map.put(AttackerAttributeKey.DEFENSE, heroVO.defense + equip.defense);
						} else {
							map.put(AttackerAttributeKey.DEFENSE, heroVO.defense);
						}
					} else {
						map.put(AttackerAttributeKey.DEFENSE, heroVO.defense);
					}
					if (lineupHeadItem.decorationUuid != 0) {
						EquipVO equip = equipFacade.get(actorId, lineupHeadItem.decorationUuid);
						if (equip != null) {
							map.put(AttackerAttributeKey.HP, heroVO.hp + equip.hp);
						} else {
							map.put(AttackerAttributeKey.HP, heroVO.hp);
						}
					} else {
						map.put(AttackerAttributeKey.HP, heroVO.hp);
					}
				}
			}
		} else {
			map.put(AttackerAttributeKey.HP, heroVO.hp);
			map.put(AttackerAttributeKey.DEFENSE, heroVO.defense);
			map.put(AttackerAttributeKey.ATK, heroVO.atk);
		}
		return map;
	}
	
	
	/**
	 * 获取自己与盟友的 参战人员对象
	 * @param actorId		自己的角色id
	 * @param allyActorId	盟友的角色id
	 * @return
	 */
	public static LineupFightModel getAllyLineupFight(long actorId, long allyActorId) {
		LineupFightModel model = getLineupFight(actorId);
		if(allyActorId > 0){
			LineupFightModel allyModel = getLineupFight(allyActorId);
			Map<Integer, HeroVO> allyHeros = allyModel.getHeros();

			for (Map.Entry<Integer, HeroVO> entry : allyHeros.entrySet()) {
				model.getHeros().put(entry.getKey() + LineupRule.MAX_GRID_COUNT, entry.getValue());
			}
			model.getAttributeChanges().putAll(allyModel.getAttributeChanges());
		}
		return model;
	}
	
	
	private void processAttributeChange(long actorId, HeroVO hero, LineupHeadItem item, LineupFightModel model) {
		Map<AttackerAttributeKey, Integer> attMap = new HashMap<>();
		model.getAttributeChanges().put(hero.getSpriteId(), attMap);
		
		//读取各种buffer加成
		HeroBuffer buffers = this.bufferFacade.getHeroBuffer(actorId, item.heroId);
		if (buffers != null && buffers.bufferTypeMap != null) {
			for (List<BufferVO> buffList : buffers.bufferTypeMap.values()) {
				for (BufferVO buf : buffList) {
					AttackerAttributeKey key = null;
					switch (buf.key) {
					case HP:
						key = AttackerAttributeKey.HP;
						break;
					case ATTACK_SCOPE:
						key = AttackerAttributeKey.ATTACK_SCOPE;
						break;
					case ATK:
						key = AttackerAttributeKey.ATK;
						break;
					case DEFENSE:
						key = AttackerAttributeKey.DEFENSE;
						break;							
					default:
						break;
					}
					Integer oldVal = attMap.get(key);
					if (oldVal == null) {
						attMap.put(key, buf.addVal);
					} else {
						attMap.put(key, buf.addVal+oldVal);
					}
				}
			}
		}
		
		//记录装备信息		
		if (item.atkEquipUuid != 0) {
			EquipVO equip = equipFacade.get(actorId, item.atkEquipUuid);
			attMap.put(AttackerAttributeKey.WEAPON_ID, equip.equipId);
			attMap.put(AttackerAttributeKey.WEAPON_LEVEL, equip.level);
		}
		if (item.decorationUuid != 0) {
			EquipVO equip = equipFacade.get(actorId, item.decorationUuid);
			attMap.put(AttackerAttributeKey.ORNAMENTS_ID, equip.equipId);
			attMap.put(AttackerAttributeKey.ORNAMENTS_LEVEL, equip.level);
		}
		if (item.defEquipUuid != 0) {
			EquipVO equip = equipFacade.get(actorId, item.defEquipUuid);
			attMap.put(AttackerAttributeKey.ARMOR_ID, equip.equipId);
			attMap.put(AttackerAttributeKey.ARMOR_LEVEL, equip.level);
		}
	}
	
	
	
	
	

	/**
	 * @param heroId
	 * @param equip
	 * @param buffers
	 */
	protected void updateEquipAffect(long actorId, int heroId, EquipVO equip) {
		if (equip.atk > 0) {
			BufferVO buff = new BufferVO(HeroVOAttributeKey.ATK, equip.atk, BufferSourceType.EQUIP_ATTR_BUFFER);
			bufferFacade.addBuff(actorId, heroId, buff);
		}
		if (equip.defense > 0) {
			BufferVO buff = new BufferVO(HeroVOAttributeKey.DEFENSE, equip.defense, BufferSourceType.EQUIP_ATTR_BUFFER);
			bufferFacade.addBuff(actorId,heroId, buff);
		}
		if (equip.hp > 0) {
			BufferVO buff = new BufferVO(HeroVOAttributeKey.HP, equip.hp, BufferSourceType.EQUIP_ATTR_BUFFER);
			bufferFacade.addBuff(actorId, heroId, buff);
		}
		if (equip.attackScope > 0) {
			BufferVO buff = new BufferVO(HeroVOAttributeKey.ATTACK_SCOPE, equip.attackScope, BufferSourceType.EQUIP_ATTR_BUFFER);
			bufferFacade.addBuff(actorId, heroId, buff);
		}
	}
	
	/**
	 * 移除阵型和装备激活的技能、buffer.
	 * @param actorId
	 * @param heroId
	 */
	public void removeLineupBuffer(long actorId, int heroId) {
		/** step1 将由阵型和装备激活的被动技能去掉 */
		HeroVO hero = heroFacade.getHero(actorId, heroId);
		Iterator<Integer> iter = hero.getPassiveSkillList().iterator();
		while (iter.hasNext()) {
			int skillId = iter.next();
			PassiveSkillConfig skill = SkillService.getPassiveSkill(skillId);
			if (skill == null) {
				continue;
			}
			if (skill.getTriggerItem() == SkillTriggerItem.TriggerEquip 
					|| skill.getTriggerItem() == SkillTriggerItem.TriggerLineup) {
				iter.remove();				
			}
		}
		
		heroFacade.updateHero(actorId, hero);
		
		
		bufferFacade.removeBufferBySourceType(actorId, heroId, BufferSourceType.EQUIP_BUFFER, BufferSourceType.LINEUP_BUFFER, BufferSourceType.EQUIP_ATTR_BUFFER);
	}
	
	/**
	 * 更新阵型激活的buffer和技能
	 * @param lineup
	 * @param containsEquip 是否包含装备buff
	 * @return
	 */
	public Set<Integer> updateLineupBuffer(Lineup lineup) {
		Set<Integer> affectHeros = new HashSet<>();
		for (LineupHeadItem item : lineup.getHeadItemList()) {			
			if (item.heroId > 0) {
				updateLineupBuffer4SingleHero(lineup, item);
				affectHeros.add(item.heroId);
			}
		}
		return affectHeros;		
	}

	/**
	 * 更新阵型激活的buffer
	 * @param lineup
	 * @return
	 */
	public void updateLineupBuffer4SingleHero(Lineup lineup, LineupHeadItem grid) {
		int heroId = grid.heroId;
		if (heroId <= 0){
			return;
		}
		long actorId = lineup.getPkId();
		HeroVO hero = HeroHelper.getInstance().getHero(actorId, heroId);
		
		bufferFacade.removeBufferBySourceType(actorId, hero.getHeroId(),BufferSourceType.EQUIP_ATTR_BUFFER);
		addEquipsAffect(lineup, grid, actorId, heroId);
		
		/** step1 返回以前由装备和阵型激活的技能列表 */
		Set<Integer> skillSet = new HashSet<>();
		skillSet.addAll(getActivedSkillByType(hero.getPassiveSkillList(), SkillTriggerItem.TriggerEquip));
		skillSet.addAll(getActivedSkillByType(hero.getPassiveSkillList(), SkillTriggerItem.TriggerLineup));
		
		/** step2 返回当前由装备和阵型激活的被动技能 */
		Set<Integer> currentSkillSet = new HashSet<>();
		currentSkillSet.addAll(getActiveSkill(hero, lineup, SkillTriggerItem.TriggerEquip));
		currentSkillSet.addAll(getActiveSkill(hero, lineup, SkillTriggerItem.TriggerLineup));
				
		/** step3 更新技能 */
		//旧的移除
		hero.getPassiveSkillList().removeAll(skillSet);
		
		//增加新的技能
		hero.getPassiveSkillList().addAll(currentSkillSet);
		heroFacade.updateHero(actorId, hero);
		
		/** step4 更新buffer */		
		//重新生成
		//先移除
		bufferFacade.removeBufferBySourceType(actorId, heroId, BufferSourceType.ATTACK_BUFFER, BufferSourceType.DEFENSE_BUFFER,BufferSourceType.EQUIP_BUFFER,
				BufferSourceType.HP_BUFFER, BufferSourceType.LEVEL_BUFFER, BufferSourceType.LINEUP_BUFFER);
		//计算获取buf
		List<BufferVO> addBuffs = SkillHeper.getOutBattleBufferRaisedBySkill(actorId, hero, hero.getPassiveSkillList(), effectContext);
		BufferVO[] buffArr = new BufferVO[addBuffs.size()];
		//再添加
		bufferFacade.addBuff(actorId, hero.getHeroId(), addBuffs.toArray(buffArr));
	}

	/**
	 * @param lineup
	 * @param item
	 * @param buffers
	 * @param actorId
	 * @param heroId
	 */
	protected void addEquipsAffect(Lineup lineup, LineupHeadItem grid, long actorId, int heroId) {
		if (grid != null) {			
			if (grid.atkEquipUuid != 0) {
				EquipVO equip = equipFacade.get(actorId, grid.atkEquipUuid);
				if (equip != null) {
					updateEquipAffect(actorId, heroId, equip);
				}
			}
			if (grid.defEquipUuid != 0) {
				EquipVO equip = equipFacade.get(actorId, grid.defEquipUuid);
				if (equip != null) {
					updateEquipAffect(actorId, heroId, equip);
				}
			}
			if (grid.decorationUuid != 0) {
				EquipVO equip = equipFacade.get(actorId, grid.decorationUuid);
				if (equip != null) {
					updateEquipAffect( actorId, heroId, equip);
				}
			}
		}
	}
	
	/**
	 * 从技能id集合中找出某种激活类型的技能
	 * @param skillList
	 * @param trigerItem
	 * @return
	 */
	public Set<Integer> getActivedSkillByType(List<Integer> skillList, int trigerItem) {
		Set<Integer> activeSkillSet = new HashSet<>();
		for (int skid : skillList) {
			PassiveSkillConfig skill = SkillService.getPassiveSkill(skid);
			if (skill == null) {
				continue;
			}
			if (skill.getTriggerItem() == trigerItem) {
				activeSkillSet.add(skid);
			}			
		}
		return activeSkillSet;
	}
	

	/**
	 * 返回当前激活的某类型的技能列表
	 * @param heroId
	 * @param lineup
	 * @return
	 */
	public Set<Integer> getActiveSkill(HeroVO hero, Lineup lineup, int trigerItem) {
		HeroConfig heroConf = HeroService.get(hero.getHeroId());
		Set<Integer> activeSkillSet = new HashSet<>();
		
		//如果该仙人没有被动技能,直接跳过
		if (CollectionUtils.isEmpty(heroConf.getPassiveSkills())) {
			return activeSkillSet;
		}

		for (int skillId : heroConf.getPassiveSkills()) {
			PassiveSkillConfig skill = SkillService.getPassiveSkill(skillId);
			if (skill == null) {
				continue;
			}
			//如果不是指定类型触发的技能，直接跳过
			if (skill.getTriggerItem() != trigerItem) {
				continue;
			}
			
			PassiveSkillTriggerParser parser = context.getParser(trigerItem);
			
			//检查技能是否被激活
			boolean isActive = false;
			
			try {
				isActive = parser.isTrigger(hero, skill, lineup);	
			} catch(Exception ex) {
				LOGGER.error("",ex.getMessage());
			}
			
			if (isActive) {
				activeSkillSet.add(skillId);
			}
		}
		return activeSkillSet;
	}	
	
	/**
	 * 获取阵形英雄信息：位置_仙人id_等级|位置_仙人id_等级
	 * @param actorId
	 * @return
	 */
	public static String getOssLineupHeroString(long actorId){
		Lineup lineup = getInstance().lineupFacade.getLineup(actorId);
		List<String> itemList = new ArrayList<>();
		for (LineupHeadItem item : lineup.getHeadItemList()) {
			if (item.heroId > 0){
				List<Object> itemAttList = new ArrayList<>();
				HeroVO heroVO = getInstance().heroFacade.getHero(actorId, item.heroId);
				itemAttList.add(item.gridIndex);
				itemAttList.add(item.heroId);
				itemAttList.add(heroVO.level);
				String str = StringUtils.collection2SplitString(itemAttList, Splitable.ATTRIBUTE_SPLIT);
				itemList.add(str);
			}
		}
		
		return StringUtils.collection2SplitString(itemList, Splitable.ELEMENT_DELIMITER);
		
	}
	/**
	 * 获取阵形装备信息 位置_装备id_等级|位置_装备id_等级
	 * @param actorId
	 * @return
	 */
	public static String getOssLineupEquipString(long actorId){
		Lineup lineup = getInstance().lineupFacade.getLineup(actorId);
		List<String> itemList = new ArrayList<>();
		for (LineupHeadItem item : lineup.getHeadItemList()) {
			if (item.atkEquipUuid != 0){
				List<Object> itemAttList = new ArrayList<>();
				EquipVO equipVO = getInstance().equipFacade.get(actorId, item.atkEquipUuid);
				itemAttList.add(item.gridIndex);
				itemAttList.add(equipVO.equipId);
				itemAttList.add(equipVO.level);
				String str = StringUtils.collection2SplitString(itemAttList, Splitable.ATTRIBUTE_SPLIT);
				itemList.add(str);
			}
			if (item.defEquipUuid != 0){
				List<Object> itemAttList = new ArrayList<>();
				EquipVO equipVO = getInstance().equipFacade.get(actorId, item.defEquipUuid);
				itemAttList.add(item.gridIndex);
				itemAttList.add(equipVO.equipId);
				itemAttList.add(equipVO.level);
				String str = StringUtils.collection2SplitString(itemAttList, Splitable.ATTRIBUTE_SPLIT);
				itemList.add(str);
			}
			if (item.decorationUuid != 0){
				List<Object> itemAttList = new ArrayList<>();
				EquipVO equipVO = getInstance().equipFacade.get(actorId, item.decorationUuid);
				itemAttList.add(item.gridIndex);
				itemAttList.add(equipVO.equipId);
				itemAttList.add(equipVO.level);
				String str = StringUtils.collection2SplitString(itemAttList, Splitable.ATTRIBUTE_SPLIT);
				itemList.add(str);
			}
		}
		
		return StringUtils.collection2SplitString(itemList, Splitable.ELEMENT_DELIMITER);
		
	}
}

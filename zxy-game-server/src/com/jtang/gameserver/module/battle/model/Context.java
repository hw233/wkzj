package com.jtang.gameserver.module.battle.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import com.jtang.core.utility.CollectionUtils;
import com.jtang.gameserver.dataconfig.model.InbattleEffectConfig;
import com.jtang.gameserver.dataconfig.model.MapConfig;
import com.jtang.gameserver.module.battle.helper.FightResultRecorder;
import com.jtang.gameserver.module.battle.type.Camp;
import com.jtang.gameserver.module.skill.model.TargetReport;
import com.jtang.gameserver.module.skill.type.ProcessType;


/**
 * 战斗上下文信息, 保存各个玩家的战斗情况
 * 
 * @author vinceruan
 */
public class Context {
	/** 所有参战者的候选技能效果 */
	public Map<Integer, Map<Fighter, Set<InbattleEffectConfig>>>  fighterAvailableSkillsEffects = new HashMap<Integer, Map<Fighter,Set<InbattleEffectConfig>>>();
		
	/** 每个阵型的选手列表 */
	private Map<Camp, List<Fighter>> camps = new HashMap<Camp, List<Fighter>>();
	
	/** 当前受到攻击的仙人, 用于死亡处理和技能反攻 */
	private Set<Fighter> fightersBeAtked = new HashSet<Fighter>();
	/** 当前收到治疗的仙人，用于处理收到治疗时触犯技能 */
	private Set<Fighter> fightersHpAdded = new HashSet<Fighter>();
		
	/** 当前战斗阶段, 用于跟踪调试 */
	private ProcessType processType = ProcessType.BEGIN_FIGHT;
	
	/** 当前普通攻击的目标,有些技能的释放目标就是根据普通攻击者定位的，所以需要缓存起来,并且在每个玩家出手前都要清空*/
	public Fighter targetEnermy = null;
	
	/** 普通攻击造成的伤害, 有些技能的伤害值是根据普攻造成的伤害来计算的,所以需要缓存起来. */
	private int commonAtkHurt = 0;
	
	/** 非普攻造成的伤害, 有些技能需要这个值 */
	private int skillAtkHurt = 0;
	
	/** 保存技能成功释放的次数,有些技能在每次战斗中有释放次数的限制 */
	private Map<Fighter, Map<InbattleEffectConfig,Integer>> skillReleaseCount = new HashMap<Fighter, Map<InbattleEffectConfig,Integer>>();
	
	/** buffer id 生成器 */
	public AtomicInteger bufferIdGenerator = new AtomicInteger(1);
	
	/** 记录战斗过程 */
	public FightResultRecorder fightRecorder = new FightResultRecorder();
	
	/** 当前是第几轮 */
	public int attackRound = 0; 
	
	/** 地图配置 */
	public MapConfig mapConfig;
	
	/** 地图管理 */
	public BattleMap battleMap = null;
	
	/** 记录两个阵型累计的攻击伤害值,用于计算胜负类型 */
	public Map<Camp, Integer> atkPoints = new HashMap<>();
	
	/**
	 * 是否超过最大回合
	 */
	public boolean overflowRound;
	
	/**
	 * 临时战报
	 */
	public List<TargetReport> tempTargetReports = new ArrayList<>();
	
	/**
	 *  是否跳过首回合战斗
	 */
	private boolean skipFirstRound = false;
			
	public Context(MapConfig mapConfig) {
		this.mapConfig = mapConfig;
		
		int rowGridCount = mapConfig.getGridCol();
		int colGridCount = mapConfig.getGridRow();
		this.battleMap = new BattleMap(rowGridCount, colGridCount);
	}
	
	/**
	 * 记录技能的释放次数
	 * @param fighter
	 * @param effect
	 */
	public void addSkillEffectReaseCount(Fighter fighter, InbattleEffectConfig effect) {
		Map<InbattleEffectConfig, Integer> map = skillReleaseCount.get(fighter);
		if (map == null) {
			map = new HashMap<InbattleEffectConfig, Integer>();
			skillReleaseCount.put(fighter, map);
		}
		
		Integer count = map.get(effect);
		if (count == null) {
			count = 0;
		}
		map.put(effect, ++count);		
	}
	
	/**
	 * 获取技能的释放次数
	 * @param fighter
	 * @param effect
	 * @return
	 */
	public int getSkillEeffectReleaseCount(Fighter fighter, InbattleEffectConfig effect) {
		Map<InbattleEffectConfig, Integer> map = skillReleaseCount.get(fighter);
		if (map == null) {
			return 0;
		}
		
		Integer count = map.get(effect);
		return count == null ? 0 : count;
	}
	
	public void addSpriteAvailableSkillEffects(Fighter fighter, List<InbattleEffectConfig> skillEffects) {
		if (skillEffects != null) {
			for (InbattleEffectConfig effect : skillEffects) {
				addSpriteAvailableSkill(fighter, effect);
			}
		}
	}
	
	/**
	 * 根据触发时机、玩家 分类缓存各种技能效果配置
	 * @param fighter
	 * @param skillEffect
	 */
	public void addSpriteAvailableSkill(Fighter fighter, InbattleEffectConfig skillEffect) {
		Map<Fighter, Set<InbattleEffectConfig>> spriteSkillMap = fighterAvailableSkillsEffects.get(skillEffect.getProcessType());
		if (spriteSkillMap == null) {
			spriteSkillMap = new HashMap<Fighter, Set<InbattleEffectConfig>>();
			fighterAvailableSkillsEffects.put(skillEffect.getProcessType(), spriteSkillMap);
		}
		
		Set<InbattleEffectConfig> list = spriteSkillMap.get(fighter);
		if (list == null) {
			list = new HashSet<InbattleEffectConfig>();
			spriteSkillMap.put(fighter, list);
		}		
		list.add(skillEffect);
	}
	
	public Map<Fighter, Set<InbattleEffectConfig>> getSpriteSkillMapByProcessType(int processType) {
		return this.fighterAvailableSkillsEffects.get(processType);
	}
	
	public Set<InbattleEffectConfig> getAllSkill(Fighter fighter) {
		Set<InbattleEffectConfig> skill = new HashSet<>();
		for (Map<Fighter, Set<InbattleEffectConfig>> entry: this.fighterAvailableSkillsEffects.values()) {
			Set<InbattleEffectConfig> set = entry.get(fighter);
			if (CollectionUtils.isEmpty(set) == false) {
				skill.addAll(set);
			}
		}
		return skill;
	}
	
	public InbattleEffectConfig getEffectConfig(Fighter fighter, int effectId) {
		Set<InbattleEffectConfig> skillSet = getSpriteSkillMapByProcessType(ProcessType.EVENT.getCode()).get(fighter);
		for (InbattleEffectConfig conf : skillSet) {
			if (conf.getEffectId() == effectId) {
				return conf;
			}
		}
		return null;
	}
	
	public void addFighter2Camp(Fighter figther) {
		Camp camp = figther.getCamp();
		if (camps.get(camp) == null) {
			camps.put(camp, new ArrayList<Fighter>());
		}
		camps.get(camp).add(figther);
	}
	
	public List<Fighter> getTeamListByCamp(Camp camp) {
		return this.camps.get(camp);
	}

	public ProcessType getProcessType() {
		return processType;
	}

	public void setProcessType(ProcessType processType) {
		this.processType = processType;
	}

	public int getCommonAtkHurt() {
		return commonAtkHurt;
	}

	public void setCommonAtkHurt(int commonAtkHurt) {
		this.commonAtkHurt = commonAtkHurt;
	}

	public Set<Fighter> getFightersBeAtked() {
		return fightersBeAtked;
	}
	
	public void addFighterBeAtcked(Fighter fighter) {
		this.fightersBeAtked.add(fighter);
	}

	public Set<Fighter> getFightersHpAdded() {
		return fightersHpAdded;
	}
	
	public void addFighterHpAdded(Fighter fighter) {
		this.fightersHpAdded.add(fighter);
	}
	
	public int generateBufferId() {
		return this.bufferIdGenerator.getAndIncrement();
	}

	public BattleMap getBattleMap() {
		return battleMap;
	}

	public void setBattleMap(BattleMap battleMap) {
		this.battleMap = battleMap;
	}
	
	/**
	 * 统计每个阵营造成的攻击伤害
	 * @param cmap
	 * @param hurt
	 */
	public void addAtkHur(Camp cmap, int hurt) {
		Integer h = this.atkPoints.get(cmap);
		if (h == null) {
			h = 0;			
		}
		this.atkPoints.put(cmap, hurt+h);
	}
	
	public void fighterRevive(Fighter fighter) {
		battleMap.addBarrier(fighter.getTile());
	}

	public Fighter getTargetEnermy() {
		return targetEnermy;
	}

	public void setTargetEnermy(Fighter targetEnermy) {
		this.targetEnermy = targetEnermy;
	}

	public int getSkillAtkHurt() {
		return skillAtkHurt;
	}

	public void setSkillAtkHurt(int skillAtkHurt) {
		this.skillAtkHurt = skillAtkHurt;
	}
	
	public List<Fighter> getAllFighter(){
		List<Fighter> result = new ArrayList<>();
		for (List<Fighter> fighters : this.camps.values()) {
			result.addAll(fighters);
		}
		return result;
	}
	
	public boolean isSkipFirstRound() {
		return skipFirstRound;
	}
	
	public void setSkipFirstRound(boolean skipFirstRound) {
		this.skipFirstRound = skipFirstRound;
	}
}

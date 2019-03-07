package com.jtang.gameserver.module.skill.trigger.parser;

import static com.jtang.gameserver.module.lineup.constant.LineupRule.LINEUP_GRID_COUNT_EACH_ROW;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.jiatang.common.model.HeroVO;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.dataconfig.model.PassiveSkillConfig;
import com.jtang.gameserver.dbproxy.entity.Lineup;
import com.jtang.gameserver.module.lineup.helper.LineupHelper;
import com.jtang.gameserver.module.lineup.model.LineupHeadItem;
import com.jtang.gameserver.module.skill.trigger.AbstractPassiveSkillTriggerParser;
import com.jtang.gameserver.module.skill.type.FormationCondition;
import com.jtang.gameserver.module.skill.type.SkillTriggerItem;

/**
 * 阵型调整激活技能
 * @author vinceruan
 *
 */
@Component
public class TriggerByLineup extends AbstractPassiveSkillTriggerParser {
	@Override
	protected int getType() {
		return SkillTriggerItem.TriggerLineup;
	}

	@Override
	public boolean isTrigger(HeroVO hero, PassiveSkillConfig skillConfig, Lineup lineup) {
		String triggerVal[] = skillConfig.getTriggerValue().split(Splitable.ELEMENT_SPLIT);
		int subCondition = Integer.valueOf(triggerVal[0]);
		boolean result = false;
		switch (subCondition) {
		case FormationCondition.BATTLE_WITH:
			//一起上阵
			result = processBattleWithHeros(hero, skillConfig, lineup, triggerVal[1]);
			break;
		case FormationCondition.BEHIND_SOMEONE:
			//在某位仙人的后面
			result = processBehindSomeone(hero, skillConfig, lineup, triggerVal[1]);
			break;
		case FormationCondition.SAME_COL:
			//同列
			result =  processSameCol(hero, skillConfig, lineup, triggerVal[1]);
			break;
		case FormationCondition.SAME_ROW:
			//同排
			result = processSameRow(hero, skillConfig, lineup, triggerVal[1]);
			break;
		default:
			LOGGER.error("配置文件有误,否则不应该运行到这里");
			return false;
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("角色:[{}] 的被动技能[{}]是否被激活,结果:[{}] ", hero.getHeroId(), skillConfig.getName(), result);
		}
		return result;
	}
	
	/**
	 * 判断hero是否在指定的仙人的后面
	 * @param hero
	 * @param skillConfig
	 * @param companions
	 * @param triggerVal
	 * @return
	 */
	private boolean processBehindSomeone(HeroVO hero, PassiveSkillConfig skillConfig, Lineup lineup, String triggerVal) {
		int targetHeroId = Integer.valueOf(triggerVal);
				
		//找出hero在阵型列表中的位置索引
		int posInd = LineupHelper.findGridIndexInLineup(hero, lineup);
		if (posInd <= 0) {
			LOGGER.error("仙人{{}}没有上阵", hero.getHeroId());
			return false;
		}
		
		//计算hero前面的位置索引
		int targetGridIndex = posInd - LINEUP_GRID_COUNT_EACH_ROW;
		if (targetGridIndex < 1) {
			return false;
		}
		
		for (LineupHeadItem item : lineup.getHeadItemList()) {
			if (item.heroId == targetHeroId && item.gridIndex == targetGridIndex) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 判断hero是否和指定的仙人在同一列
	 * @param hero
	 * @param skillConfig
	 * @param companions
	 * @param triggerVal
	 * @return
	 */
	private boolean processSameCol(HeroVO hero, PassiveSkillConfig skillConfig, Lineup lineup, String triggerVal) {
		String splitVals[] = triggerVal.split(Splitable.ATTRIBUTE_SPLIT);		
		if (splitVals.length >0 ) {
			Set<Integer> heroIdList = new HashSet<Integer>();
			for (String heroId : splitVals) {
				heroIdList.add(Integer.parseInt(heroId));
			}
			
//			List<LineupGridItem> lineupGrids = lineup.getLineupGridList();
			//查找hero在阵型列表中的位置索引
			int heroPos = LineupHelper.findGridIndexInLineup(hero, lineup);
			if (heroPos <= 0) {
				LOGGER.error("仙人{{}}没有上阵", hero.getHeroId());
				return false;
			}
			
			//验证同列的各个位置的仙人是否符合条件
			int i = heroPos - LINEUP_GRID_COUNT_EACH_ROW;
			Set<Integer> indexSet = new HashSet<>();
			while(i > 0) {
				indexSet.add(i);				
				i -= LINEUP_GRID_COUNT_EACH_ROW;	
			}
			
			i = heroPos + LINEUP_GRID_COUNT_EACH_ROW;
			while(i <= 9) {
				indexSet.add(i);
				i += LINEUP_GRID_COUNT_EACH_ROW;
			}
			
			for (LineupHeadItem item : lineup.getHeadItemList()) {
				if (indexSet.contains(item.gridIndex)) {
					heroIdList.remove(item.heroId);
				}
			}
			
			return heroIdList.isEmpty();
		}
		LOGGER.error("配置文件有误,否则不应该运行到这里");
		return false;
		
	}

	/**
	 * 判断hero是否和指定的仙人在同一排
	 * @param hero
	 * @param skillConfig
	 * @param companions
	 * @param triggerVal
	 * @return
	 */
	private boolean processSameRow(HeroVO hero, PassiveSkillConfig skillConfig, Lineup lineup, String triggerVal) {
		String splitVals[] = triggerVal.split(Splitable.ATTRIBUTE_SPLIT);
		if (splitVals.length >0 ) {
			Set<Integer> heroIdList = new HashSet<Integer>();
			for (String heroId : splitVals) {
				heroIdList.add(Integer.parseInt(heroId));
			}
			
			//查找hero在阵型列表中的位置
			int heroPos = LineupHelper.findGridIndexInLineup(hero, lineup);
			if (heroPos <= 0) {
				LOGGER.error("仙人[{}]没有上阵", hero.getHeroId());
				return false;
			}
			
			//查找同行的仙人是否符合条件
			int rowsBefore = (heroPos-1)/LINEUP_GRID_COUNT_EACH_ROW;//找出仙人前面有多少排
			int startInd = rowsBefore*LINEUP_GRID_COUNT_EACH_ROW+1;//仙人所在排的第一个位置
			int endInd = startInd + LINEUP_GRID_COUNT_EACH_ROW - 1;//仙人所在排的最后一个位置			
			
			for (LineupHeadItem item : lineup.getHeadItemList()) {
				if (item.heroId > 0 && item.gridIndex >= startInd && item.gridIndex <= endInd) {
					heroIdList.remove(item.heroId);
				}
			}
			
			return heroIdList.isEmpty();
		}
		LOGGER.error("配置文件有误,否则不应该运行到这里");
		return false;
	}

	/**
	 * 判断hero是否和指定的仙人一起上阵
	 * @param hero
	 * @param skillConfig
	 * @param companions
	 * @param triggerVal
	 * @return
	 */
	private boolean processBattleWithHeros(HeroVO hero, PassiveSkillConfig skillConfig, Lineup lineup, String triggerVal) {
		if (StringUtils.isNotBlank(triggerVal)) {
			String splitVals[] = triggerVal.split(Splitable.ATTRIBUTE_SPLIT);
			if (splitVals.length > 0) {				
				Set<Integer> heroIdList = new HashSet<Integer>();
				for (String heroId : splitVals) {
					heroIdList.add(Integer.parseInt(heroId));
				}
				for (LineupHeadItem item : lineup.getHeadItemList()) {
					if (item.heroId > 0) {
						heroIdList.remove(item.heroId);
					}
				}
				return heroIdList.isEmpty();
			}			
		}
		LOGGER.error("配置文件有误,否则不应该运行到这里");
		return false;
	}

}

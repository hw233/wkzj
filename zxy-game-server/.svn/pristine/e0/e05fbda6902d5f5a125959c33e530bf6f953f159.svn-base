package com.jtang.gameserver.module.adventures.achievement.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jtang.core.event.Event;
import com.jtang.core.event.Receiver;
import com.jtang.gameserver.component.event.AddHeroEvent;
import com.jtang.gameserver.component.event.EventKey;
import com.jtang.gameserver.component.event.HeroDelveEvent;
import com.jtang.gameserver.component.event.HeroLevelUpEvent;
import com.jtang.gameserver.dataconfig.model.AchievementConfig;
import com.jtang.gameserver.dataconfig.model.HeroConfig;
import com.jtang.gameserver.dataconfig.service.AchievementService;
import com.jtang.gameserver.dataconfig.service.HeroService;
import com.jtang.gameserver.module.adventures.achievement.helper.AchievePushHelper;
import com.jtang.gameserver.module.adventures.achievement.model.AchieveVO;
import com.jtang.gameserver.module.adventures.achievement.type.AchieveAttributeKey;
import com.jtang.gameserver.module.adventures.achievement.type.AchieveType;

/**
 * 仙人相关的成就达成
 * @author 0x737263
 *
 */
@Component
public class HeroAchieve extends AbstractAchieve implements Receiver {

	@Override
	public void register() {
		eventBus.register(EventKey.ADD_HERO, this);
		eventBus.register(EventKey.HERO_ATTRIBUTE_CHANGE, this);
		eventBus.register(EventKey.HERO_LEVEL_UP, this);
		eventBus.register(EventKey.HERO_DELVE, this);
	}
	
	@Override
	public void onEvent(Event paramEvent) {

		//添加新仙人
		if (paramEvent.getName() == EventKey.ADD_HERO) {
			AddHeroEvent addHeroEvent = (AddHeroEvent) paramEvent;
			HeroConfig heroConfig = HeroService.get(addHeroEvent.heroId);
			finishHeroSumAchieve(addHeroEvent.actorId, AchieveType.HERO_SUM.getId(), heroConfig.getStar());
		}

		//属性变更达成
		if (paramEvent.getName() == EventKey.HERO_DELVE) {
			HeroDelveEvent changeEvent = (HeroDelveEvent) paramEvent;
			// 累计潜修次数达成
			for (int i = 0; i < changeEvent.delveNum; i++) {
				finishNumAchievement(changeEvent.actorId, AchieveType.DELVE_NUM);
			}
		}
		
		// 仙人升级达成
		if (paramEvent.getName() == EventKey.HERO_LEVEL_UP) {
			HeroLevelUpEvent levelUpEvent = (HeroLevelUpEvent) paramEvent;
			finishNumAchievement(levelUpEvent.actorId, AchieveType.HERO_LEVEL, levelUpEvent.getHeroLevel());
		}		
	}
	
	private void finishHeroSumAchieve(long actorId, int achieveType, int star) {
		List<AchievementConfig> configList = AchievementService.getByType(achieveType);
		if(configList == null){
			return;
		}
		List<AchieveVO> list = new ArrayList<>();
		for(AchievementConfig config : configList){
			if(config.getCondition() > star){
				continue;
			}
			
			AchieveVO achieveVO = achieveDao.getAchieveVO(actorId, config.getAchieveId(), config.getAchieveType());
			if(achieveVO == null){
				if(config.getNeedFinishNum() == 1){
					achieveVO = AchieveVO.valueOf(true, config.getAchieveId(), config.getConditionList());
					list.add(achieveVO);
				}
				else if(config.getNeedFinishNum() > 1){
					achieveVO = AchieveVO.valueOf(false, config.getAchieveId(), config.getConditionList());
				}
				else{
//					LOGGER.error("achievement config story pass condition error, finish num is 0 or less than 0");
					continue;
				}
				
				achieveDao.addAchieveVO(actorId, achieveVO);
//				AchievePushHelper.pushAchievement(actorId, achieveVO);
			} else if(achieveVO.getConditionList() == null){
				continue;
			} else{
				if(achieveVO.isAchieved()){
					continue;
				}
				Map<AchieveAttributeKey, Object> changedValues = new HashMap<>();
				int num = achieveVO.increaseFinishNum();
				if(num >= config.getNeedFinishNum()){
					achieveVO.setFinished();
					changedValues.put(AchieveAttributeKey.ACHIEVE_STATE, achieveVO.getState());
					list.add(achieveVO);
				}
				
				achieveDao.update(actorId);
				changedValues.put(AchieveAttributeKey.ACHIEVE_CONDITIONS, achieveVO.getConditionList());
//				AchievePushHelper.pushAttribute(actorId, achieveVO.getAchieveId(), changedValues);
			}
		}
		AchievePushHelper.pushAchievement(actorId, list);
	}

}

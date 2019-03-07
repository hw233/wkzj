package com.jtang.gameserver.module.adventures.achievement.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jtang.core.event.Event;
import com.jtang.core.event.Receiver;
import com.jtang.gameserver.component.event.ContinueLoginEvent;
import com.jtang.gameserver.component.event.EventKey;
import com.jtang.gameserver.dataconfig.model.AchievementConfig;
import com.jtang.gameserver.dataconfig.service.AchievementService;
import com.jtang.gameserver.module.adventures.achievement.helper.AchievePushHelper;
import com.jtang.gameserver.module.adventures.achievement.model.AchieveVO;
import com.jtang.gameserver.module.adventures.achievement.type.AchieveAttributeKey;
import com.jtang.gameserver.module.adventures.achievement.type.AchieveType;

/**
 * 连续登陆达成条件
 * @author 0x737263
 *
 */
@Component
public class ContinueLoginAchieve extends AbstractAchieve implements Receiver {
	
	@Override
	public void register() {
		eventBus.register(EventKey.CONTINUE_LOGIN, this);
	}

	@Override
	public void onEvent(Event paramEvent) {
		ContinueLoginEvent event = (ContinueLoginEvent) paramEvent;
		continueNumAchievement(event.actorId, AchieveType.CONTINUE_LOGIN, event.day);
	}
	
	private void continueNumAchievement(long actorId, AchieveType achieveType, int condition) {
		List<AchievementConfig> configList = AchievementService.getByType(achieveType.getId());
		if(configList == null){
			return;
		}
		List<AchieveVO> list = new ArrayList<>();
		for(AchievementConfig config : configList){
			AchieveVO achieveVO = achieveDao.getAchieveVO(actorId, config.getAchieveId(), config.getAchieveType());
			if(achieveVO == null){
				if(condition >= config.getCondition()){
					achieveVO = AchieveVO.valueOf(config.getAchieveId(), config.getCondition());
					achieveVO.setFinished();
					list.add(achieveVO);
				}
				else{
					achieveVO = AchieveVO.valueOf(config.getAchieveId(), condition);
				}
				achieveDao.addAchieveVO(actorId, achieveVO);
//				AchievePushHelper.pushAchievement(actorId, achieveVO);
			}else if(achieveVO.getConditionList() == null){
				continue;
			}else{
				if(achieveVO.isAchieved()){
					continue;
				}
				else{
					Map<AchieveAttributeKey, Object> changedValues = new HashMap<>();
					if (condition < config.getCondition() && condition != achieveVO.getFinishedCondition()) {
						
						achieveVO.setCondition1(condition);
						changedValues.put(AchieveAttributeKey.ACHIEVE_CONDITIONS, achieveVO.getConditionList());
						
					} else if (condition >= config.getCondition()) {// 完成
						achieveVO.setFinished();
						changedValues.put(AchieveAttributeKey.ACHIEVE_STATE, achieveVO.getState());
						achieveVO.setCondition1(config.getCondition());
						changedValues.put(AchieveAttributeKey.ACHIEVE_CONDITIONS, achieveVO.getConditionList());
						list.add(achieveVO);
					}
					
					if(changedValues.size() > 0) {
						achieveDao.update(actorId);
//						AchievePushHelper.pushAttribute(actorId, achieveVO.getAchieveId(), changedValues);						
					}
				}
			}
		}
		AchievePushHelper.pushAchievement(actorId, list);
	}
}

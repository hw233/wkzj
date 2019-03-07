package com.jtang.gameserver.module.adventures.achievement.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jtang.core.event.Event;
import com.jtang.core.event.Receiver;
import com.jtang.gameserver.component.event.EventKey;
import com.jtang.gameserver.component.event.ShopBuyEvent;
import com.jtang.gameserver.dataconfig.model.AchievementConfig;
import com.jtang.gameserver.dataconfig.service.AchievementService;
import com.jtang.gameserver.module.adventures.achievement.helper.AchievePushHelper;
import com.jtang.gameserver.module.adventures.achievement.model.AchieveVO;
import com.jtang.gameserver.module.adventures.achievement.type.AchieveAttributeKey;
import com.jtang.gameserver.module.adventures.achievement.type.AchieveType;

/**
 * 云游仙商购买某物品,x次
 * @author 0x737263
 *
 */
@Component
public class ShopBuyAchieve extends AbstractAchieve implements Receiver {

	@Override
	public void register() {
		eventBus.register(EventKey.SHOP_BUY, this);
	}
	
	@Override
	public void onEvent(Event paramEvent) {
		ShopBuyEvent event = (ShopBuyEvent) paramEvent;
		for (int i = 0; i < event.todayBuyCount; i++) {
			finishShopbuy(event);
		}
	}
	
	private void finishShopbuy(ShopBuyEvent event) {
		List<AchievementConfig> configList = AchievementService.getByType(AchieveType.SHOP_BUY.getId());
		if (configList == null) {
			return;
		}
		List<AchieveVO> list = new ArrayList<>();
		for (AchievementConfig config : configList) {
			List<Integer> conditionList = config.getConditionList();
			if (event.shopId != conditionList.get(0)) {
				continue;
			}

			AchieveVO achieveVO = achieveDao.getAchieveVO(event.actorId, config.getAchieveId(), config.getAchieveType());
			if (achieveVO == null) {
				achieveVO = AchieveVO.valueOf(false, config.getAchieveId(), config.getConditionList());
				
				//判断是否达成完成次数
				if (achieveVO.getConditionList().get(1) >= config.getNeedFinishNum()) {
					achieveVO.setFinished();
					list.add(achieveVO);
				}
				achieveDao.addAchieveVO(event.actorId, achieveVO);
//				AchievePushHelper.pushAchievement(event.actorId, achieveVO);
				
			} else if(achieveVO.getConditionList() == null){
				continue;
			} else {
				
				if (achieveVO.isAchieved()) {
					continue;
				}
				
				Map<AchieveAttributeKey, Object> changedValues = new HashMap<>();
				int num = achieveVO.increaseFinishNum();
				if (num >= conditionList.get(1)) {
					achieveVO.setFinished();
					changedValues.put(AchieveAttributeKey.ACHIEVE_STATE, achieveVO.getState());
				}
				achieveDao.update(event.actorId);
				changedValues.put(AchieveAttributeKey.ACHIEVE_CONDITIONS, achieveVO.getConditionList());
//				AchievePushHelper.pushAttribute(event.actorId, achieveVO.getAchieveId(), changedValues);
			}
		}
		AchievePushHelper.pushAchievement(event.actorId, list);
	}
	
}

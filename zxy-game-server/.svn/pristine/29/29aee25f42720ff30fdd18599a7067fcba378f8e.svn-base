package com.jtang.gameserver.module.adventures.achievement.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.jtang.core.event.EventBus;
import com.jtang.gameserver.dataconfig.model.AchievementConfig;
import com.jtang.gameserver.dataconfig.service.AchievementService;
import com.jtang.gameserver.module.adventures.achievement.dao.AchieveDao;
import com.jtang.gameserver.module.adventures.achievement.helper.AchievePushHelper;
import com.jtang.gameserver.module.adventures.achievement.model.AchieveVO;
import com.jtang.gameserver.module.adventures.achievement.type.AchieveAttributeKey;
import com.jtang.gameserver.module.adventures.achievement.type.AchieveType;

/**
 * 成就处理抽象类
 * @author 0x737263
 *
 */
public abstract class AbstractAchieve {
	
	@Autowired
	protected AchieveDao achieveDao;
	
	@Autowired
	protected EventBus eventBus;
	
	/**
	 * 注册事件抽象类
	 */
	public abstract void register();
	
	@PostConstruct
	private void init() {
		register();
	}
	
	/**
	 * a大于等于条件 并且b小于等于条件的达成
	 * @param actorId			角色id
	 * @param achieveType		达成类型
	 * @param a					a需要是大于等于配置条件
	 * @param b					b需要是小于等于配置条件
	 */
	public void finishAGreaterAndBLessNum(long actorId, AchieveType achieveType, int aValue, int bValue) {
		List<AchievementConfig> configList = AchievementService.getByType(achieveType.getId());
		if (configList == null) {
			return;
		}
		List<AchieveVO> list = new ArrayList<>();
		for (AchievementConfig config : configList) {
			List<Integer> conditionList = config.getConditionList();
			if (aValue < conditionList.get(0)) {
				continue;
			}
			
			AchieveVO achieveVO = achieveDao.getAchieveVO(actorId, config.getAchieveId(), config.getAchieveType());
			if (achieveVO == null) {
				achieveVO = AchieveVO.valueOf(config.getAchieveId(), aValue, bValue);
				if (bValue <= conditionList.get(1)) {
					achieveVO.setFinished();
					list.add(achieveVO);
				}
				achieveDao.addAchieveVO(actorId, achieveVO);
				//AchievePushHelper.pushAchievement(actorId, achieveVO);
			}else if(achieveVO.getConditionList() == null){
				continue;
			}else {
				if (achieveVO.isAchieved()) {
					continue;
				}

				Map<AchieveAttributeKey, Object> changedValues = new HashMap<>();
				if (bValue <= conditionList.get(1)) {// 完成
					achieveVO.setCondition2(bValue);
					achieveVO.setFinished();
					changedValues.put(AchieveAttributeKey.ACHIEVE_STATE, achieveVO.getState());
					changedValues.put(AchieveAttributeKey.ACHIEVE_CONDITIONS, achieveVO.getConditionList());
					list.add(achieveVO);
				} else {
					achieveVO.setCondition2(bValue);
					changedValues.put(AchieveAttributeKey.ACHIEVE_CONDITIONS, achieveVO.getConditionList());
				}
				achieveDao.update(actorId);
				//AchievePushHelper.pushAttribute(actorId, achieveVO.getAchieveId(), changedValues);
			}
		}
		AchievePushHelper.pushAchievement(actorId, list);
	}
	
	/**
	 * XX达到X数值的类型
	 * @param actorId
	 * @param achieveType 成就类型
	 * @param condition 当前达到的最大值，该值的变化可能是跳跃性的，比如从1-4
	 */
	public void finishNumAchievement(long actorId, AchieveType achieveType, int condition) {
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
				}else{
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
					if(condition <= achieveVO.getFinishedCondition()){
						continue;
					}
					Map<AchieveAttributeKey, Object> changedValues = new HashMap<>();
					if(condition >= config.getCondition()){//完成
						achieveVO.setFinished();
						changedValues.put(AchieveAttributeKey.ACHIEVE_STATE, achieveVO.getState());
						achieveVO.setCondition1(config.getCondition());
						changedValues.put(AchieveAttributeKey.ACHIEVE_CONDITIONS, achieveVO.getConditionList());
						list.add(achieveVO);
					}
					else{
						achieveVO.setCondition1(condition);
						changedValues.put(AchieveAttributeKey.ACHIEVE_CONDITIONS, achieveVO.getConditionList());
					}
					
					achieveDao.update(actorId);
//					AchievePushHelper.pushAttribute(actorId, achieveVO.getAchieveId(), changedValues);
				}
			}
		}
		AchievePushHelper.pushAchievement(actorId, list);
	}
	
	/**
	 * 一次次递增的成就类型
	 * @param actorId
	 * @param achieveType
	 */
	public void finishNumAchievement(long actorId, AchieveType achieveType) {
		List<AchievementConfig> configList = AchievementService.getByType(achieveType.getId());
		if(configList == null){
			return;
		}
		List<AchieveVO> list = new ArrayList<>();
		for(AchievementConfig config : configList){
			AchieveVO achievementVO = achieveDao.getAchieveVO(actorId, config.getAchieveId(), config.getAchieveType());
			if(achievementVO == null){
				if(1 >= config.getCondition()){
					achievementVO = AchieveVO.valueOf(config.getAchieveId(), config.getCondition());
					achievementVO.setFinished();
					list.add(achievementVO);
				}
				else{
					achievementVO = AchieveVO.valueOf(config.getAchieveId(), 1);
				}
				achieveDao.addAchieveVO(actorId, achievementVO);
//				AchievePushHelper.pushAchievement(actorId, achievementVO);
			}else if(achievementVO.getConditionList() == null){
				continue;
			}else{
				if(achievementVO.isAchieved()){
					continue;
				}
				else{
					Map<AchieveAttributeKey, Object> changedValues = new HashMap<>();
					achievementVO.increaseFirstCondition();//递增一次
					if(achievementVO.getFinishedCondition() >= config.getCondition()){
						achievementVO.setFinished();
						changedValues.put(AchieveAttributeKey.ACHIEVE_STATE, achievementVO.getState());
						list.add(achievementVO);
					}
					
					achieveDao.update(actorId);
					changedValues.put(AchieveAttributeKey.ACHIEVE_CONDITIONS, achievementVO.getConditionList());
//					AchievePushHelper.pushAttribute(actorId, achievementVO.getAchieveId(), changedValues);
				}
			}
		}
		AchievePushHelper.pushAchievement(actorId, list);
	}
	
	/**
	 * 数值累加类型
	 * @param actorId
	 * @param achieveType
	 * @param sum 
	 */
	public void finishSumAccumulateAchieve(long actorId, AchieveType achieveType, int sum) {
		List<AchievementConfig> configList = AchievementService.getByType(achieveType.getId());
		if (configList == null) {
			return;
		}
		List<AchieveVO> list = new ArrayList<>();
		for (AchievementConfig config : configList) {
			AchieveVO achieveVO = achieveDao.getAchieveVO(actorId, config.getAchieveId(), config.getAchieveType());
			if (achieveVO == null) {
				if (sum >= config.getCondition()) {
					achieveVO = AchieveVO.valueOf(config.getAchieveId(), config.getCondition());
					achieveVO.setFinished();
					list.add(achieveVO);
				} else {
					achieveVO = AchieveVO.valueOf(config.getAchieveId(), sum);
				}
				achieveDao.addAchieveVO(actorId, achieveVO);
//				AchievePushHelper.pushAchievement(actorId, achieveVO);
			} else if(achieveVO.getConditionList() == null){
				continue;
			} else {
				if (achieveVO.isAchieved()) {
					continue;
				} else {
					Map<AchieveAttributeKey, Object> changedValues = new HashMap<>();
					int finishedCondition = achieveVO.getFinishedCondition() + sum;
					int needCondition = config.getCondition();
					if (finishedCondition >= needCondition) {
						achieveVO.setFinished();
						achieveVO.setCondition1(needCondition);
						changedValues.put(AchieveAttributeKey.ACHIEVE_STATE, achieveVO.getState());
						list.add(achieveVO);
					} else {
						achieveVO.setCondition1(finishedCondition);
					}
					changedValues.put(AchieveAttributeKey.ACHIEVE_CONDITIONS, achieveVO.getConditionList());

					achieveDao.update(actorId);
//					AchievePushHelper.pushAttribute(actorId, achieveVO.getAchieveId(), changedValues);
				}
			}
		}
		AchievePushHelper.pushAchievement(actorId, list);
	}

}

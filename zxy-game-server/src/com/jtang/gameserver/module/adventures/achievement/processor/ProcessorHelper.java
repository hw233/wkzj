package com.jtang.gameserver.module.adventures.achievement.processor;
//package com.jtang.sm2.module.adventures.achievement.processor;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.jtang.sm2.dataconfig.model.AchievementConfig;
//import com.jtang.sm2.dataconfig.service.AchievementService;
//import com.jtang.sm2.module.adventures.achievement.dao.AchievementDao;
//import com.jtang.sm2.module.adventures.achievement.helper.AchievementPushHelper;
//import com.jtang.sm2.module.adventures.achievement.model.AchievementVO;
//import com.jtang.sm2.module.adventures.achievement.type.AchieveAttributeKey;
//import com.jtang.sm2.module.adventures.achievement.type.AchievementType;
//
//@Component
//public class ProcessorHelper {
//
//	@Autowired
//	private AchievementDao achieveDao;
//	
//	/**
//	 * XX达到X数值的类型
//	 * @param actorId
//	 * @param achieveType 成就类型
//	 * @param condition 当前达到的最大值，该值的变化可能是跳跃性的，比如从1-4
//	 */
//	public void finishNumAchievement(long actorId, AchievementType achieveType, int condition) {
//		List<AchievementConfig> configList = AchievementService.getByType(achieveType.getType());
//		if(configList == null){
//			return;
//		}
//		
//		for(AchievementConfig config : configList){
//			AchievementVO achieveVO = achieveDao.getAchieveVO(actorId, config.getAchieveId(), config.getAchieveType());
//			if(achieveVO == null){
//				if(condition >= config.getCondition()){
//					achieveVO = AchievementVO.valueOf(config.getAchieveId(), config.getCondition());
//					achieveVO.setFinished();
//				}
//				else{
//					achieveVO = AchievementVO.valueOf(config.getAchieveId(), condition);
//				}
//				achieveDao.addAchieveVO(actorId, achieveVO);
//				AchievementPushHelper.pushAchievement(actorId, achieveVO);
//			}
//			else{
//				if(achieveVO.isAchieved()){
//					continue;
//				}
//				else{
//					if(condition <= achieveVO.getFinishedCondition()){
//						continue;
//					}
//					Map<AchieveAttributeKey, Object> changedValues = new HashMap<>();
//					if(condition >= config.getCondition()){//完成
//						achieveVO.setFinished();
//						changedValues.put(AchieveAttributeKey.ACHIEVE_STATE, achieveVO.getState());
//						achieveVO.setFinishedCondition(config.getCondition());
//						changedValues.put(AchieveAttributeKey.ACHIEVE_CONDITIONS, achieveVO.getConditionList());
//					}
//					else{
//						achieveVO.setFinishedCondition(condition);
//						changedValues.put(AchieveAttributeKey.ACHIEVE_CONDITIONS, achieveVO.getConditionList());
//					}
//					
//					achieveDao.updateAchieveVO(actorId, achieveVO);
//					AchievementPushHelper.pushAttribute(actorId, achieveVO.getAchieveId(), changedValues);
//				}
//			}
//		}
//	}
//	
//	/**
//	 * 一次次递增的成就类型
//	 * @param actorId
//	 * @param achieveType
//	 */
//	public void finishNumAchievement(long actorId, AchievementType achieveType) {
//		List<AchievementConfig> configList = AchievementService.getByType(achieveType.getType());
//		if(configList == null){
//			return;
//		}
//
//		for(AchievementConfig config : configList){
//			AchievementVO achievementVO = achieveDao.getAchieveVO(actorId, config.getAchieveId(), config.getAchieveType());
//			if(achievementVO == null){
//				if(1 >= config.getCondition()){
//					achievementVO = AchievementVO.valueOf(config.getAchieveId(), config.getCondition());
//					achievementVO.setFinished();
//				}
//				else{
//					achievementVO = AchievementVO.valueOf(config.getAchieveId(), 1);
//				}
//				achieveDao.addAchieveVO(actorId, achievementVO);
//				AchievementPushHelper.pushAchievement(actorId, achievementVO);
//			}
//			else{
//				if(achievementVO.isAchieved()){
//					continue;
//				}
//				else{
//					Map<AchieveAttributeKey, Object> changedValues = new HashMap<>();
//					achievementVO.increaseFirstCondition();//递增一次
//					if(achievementVO.getFinishedCondition() >= config.getCondition()){
//						achievementVO.setFinished();
//						changedValues.put(AchieveAttributeKey.ACHIEVE_STATE, achievementVO.getState());
//					}
//					
//					achieveDao.updateAchieveVO(actorId, achievementVO);
//					changedValues.put(AchieveAttributeKey.ACHIEVE_CONDITIONS, achievementVO.getConditionList());
//					AchievementPushHelper.pushAttribute(actorId, achievementVO.getAchieveId(), changedValues);
//				}
//			}
//		}
//	}
//	
//	/**
//	 * 数值累加类型
//	 * @param actorId
//	 * @param achieveType
//	 * @param sum 
//	 */
//	public void finishSumAccumulateAchieve(long actorId, AchievementType achieveType, int sum) {
//		List<AchievementConfig> configList = AchievementService.getByType(achieveType.getType());
//		if (configList == null) {
//			return;
//		}
//
//		for (AchievementConfig config : configList) {
//			AchievementVO achieveVO = achieveDao.getAchieveVO(actorId, config.getAchieveId(), config.getAchieveType());
//			if (achieveVO == null) {
//				if (sum >= config.getCondition()) {
//					achieveVO = AchievementVO.valueOf(config.getAchieveId(), config.getCondition());
//					achieveVO.setFinished();
//				} else {
//					achieveVO = AchievementVO.valueOf(config.getAchieveId(), sum);
//				}
//				achieveDao.addAchieveVO(actorId, achieveVO);
//				AchievementPushHelper.pushAchievement(actorId, achieveVO);
//			} else {
//				if (achieveVO.isAchieved()) {
//					continue;
//				} else {
//					Map<AchieveAttributeKey, Object> changedValues = new HashMap<>();
//					int finishedCondition = achieveVO.getFinishedCondition() + sum;
//					int needCondition = config.getCondition();
//					if (finishedCondition >= needCondition) {
//						achieveVO.setFinished();
//						achieveVO.setFinishedCondition(needCondition);
//						changedValues.put(AchieveAttributeKey.ACHIEVE_STATE, achieveVO.getState());
//					} else {
//						achieveVO.setFinishedCondition(finishedCondition);
//					}
//					changedValues.put(AchieveAttributeKey.ACHIEVE_CONDITIONS, achieveVO.getConditionList());
//
//					achieveDao.updateAchieveVO(actorId, achieveVO);
//					AchievementPushHelper.pushAttribute(actorId, achieveVO.getAchieveId(), changedValues);
//				}
//			}
//		}
//	}
//	
//	public void finishEquipAchievementIfAble(long actorId, AchievementType achieveType, int star) {
//		List<AchievementConfig> configList = AchievementService.getByType(achieveType.getType());
//		if (configList == null) {
//			return;
//		}
//
//		for (AchievementConfig config : configList) {
//			List<Integer> conditionList = config.getConditionList();
//			if (config.getCondition() == star) {
//				continue;
//			}
//			AchievementVO achieveVO = achieveDao.getAchieveVO(actorId, config.getAchieveId(), config.getAchieveType());
//			if (achieveVO == null) {
//				if (conditionList.get(conditionList.size() - 1) == 1) {
//					achieveVO = AchievementVO.valueOf(true, config.getAchieveId(), config.getConditionList());
//				} else if (conditionList.get(conditionList.size() - 1) > 1) {
//					achieveVO = AchievementVO.valueOf(false, config.getAchieveId(), config.getConditionList());
//				} else {
//					// LOGGER.error("achievement config story pass condition error, finish num is 0 or less than 0");
//					return;
//				}
//				achieveDao.addAchieveVO(actorId, achieveVO);
//				AchievementPushHelper.pushAchievement(actorId, achieveVO);
//			} else {
//				if (achieveVO.isAchieved()) {
//					return;
//				}
//				Map<AchieveAttributeKey, Object> changedValues = new HashMap<>();
//				int num = achieveVO.increaseFinishNum();
//				if (num >= conditionList.get(conditionList.size() - 1)) {
//					achieveVO.setFinished();
//					changedValues.put(AchieveAttributeKey.ACHIEVE_STATE, achieveVO.getState());
//				}
//
//				achieveDao.updateAchieveVO(actorId, achieveVO);
//
//				changedValues.put(AchieveAttributeKey.ACHIEVE_CONDITIONS, achieveVO.getConditionList());
//				AchievementPushHelper.pushAttribute(actorId, achieveVO.getAchieveId(), changedValues);
//			}
//		}
//	}
//	
//}

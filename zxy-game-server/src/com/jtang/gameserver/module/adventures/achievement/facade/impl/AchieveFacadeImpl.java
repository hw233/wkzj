package com.jtang.gameserver.module.adventures.achievement.facade.impl;

import static com.jiatang.common.GameStatusCodeConstant.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.result.Result;
import com.jtang.gameserver.dataconfig.model.AchievementConfig;
import com.jtang.gameserver.dataconfig.service.AchievementService;
import com.jtang.gameserver.module.adventures.achievement.dao.AchieveDao;
import com.jtang.gameserver.module.adventures.achievement.facade.AchieveFacade;
import com.jtang.gameserver.module.adventures.achievement.helper.AchievePushHelper;
import com.jtang.gameserver.module.adventures.achievement.model.AchieveVO;
import com.jtang.gameserver.module.adventures.achievement.type.AchieveAttributeKey;
import com.jtang.gameserver.module.adventures.achievement.type.AchieveState;
import com.jtang.gameserver.module.equip.facade.EquipFacade;
import com.jtang.gameserver.module.equip.type.EquipAddType;
import com.jtang.gameserver.module.goods.facade.GoodsFacade;
import com.jtang.gameserver.module.goods.type.GoodsAddType;
import com.jtang.gameserver.module.hero.facade.HeroFacade;
import com.jtang.gameserver.module.hero.facade.HeroSoulFacade;
import com.jtang.gameserver.module.hero.type.HeroAddType;
import com.jtang.gameserver.module.hero.type.HeroSoulAddType;
import com.jtang.gameserver.module.recruit.facade.RecruitFacade;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.facade.VipFacade;
import com.jtang.gameserver.module.user.type.EnergyAddType;
import com.jtang.gameserver.module.user.type.GoldAddType;
import com.jtang.gameserver.module.user.type.ReputationAddType;
import com.jtang.gameserver.module.user.type.TicketAddType;
import com.jtang.gameserver.module.user.type.VITAddType;

@Component
public class AchieveFacadeImpl implements AchieveFacade {	
	@Autowired
	private AchieveDao achieveDao;
	@Autowired
	private ActorFacade actorFacade;
	@Autowired
	private GoodsFacade goodsFacade;
	@Autowired
	private EquipFacade equipFacade;
	@Autowired
	private HeroSoulFacade heroSoulFacade;
	@Autowired
	private HeroFacade heroFacade;
	@Autowired
	private VipFacade vipFacade;
	@Autowired
	private RecruitFacade recruitFacade;
	
	@Override
	public List<AchieveVO> getAchieve(long actorId) {
		List<AchieveVO> achievementList = achieveDao.getAchieveVOList(actorId);
		return achievementList;
	}

	@Override
	public Result getReward(long actorId, int achieveId) {
		AchievementConfig achieveConfig = AchievementService.get(achieveId);
		if(achieveConfig == null){
			return Result.valueOf(ACHIEVEMENT_REWARD_CONFIG_UN_EXISTS);
		}

		AchieveVO achieveVO = achieveDao.getAchieveVO(actorId, achieveId, achieveConfig.getAchieveType());
		if(achieveVO == null){
			return Result.valueOf(ACHIEVEMENT_UN_EXISTS);
		}
		if(achieveVO.getConditionList() == null){
			return Result.valueOf(ACHIEVEMENT_REWARD_ALREADY_GETED);
		}
		if(achieveVO.getState() == AchieveState.UN_ACHIEVED.getState()){
			return Result.valueOf(ACHIEVEMENT_UN_ACHIEVED);
		}
		if(achieveVO.getState() == AchieveState.GETED.getState()){
			return Result.valueOf(ACHIEVEMENT_REWARD_ALREADY_GETED);
		}
		
		giveAchieveReward(actorId, achieveConfig);
		
		achieveVO.setGeted();
		achieveDao.update(actorId);
		
		Map<AchieveAttributeKey, Object> changedValues = new HashMap<>();
		changedValues.put(AchieveAttributeKey.ACHIEVE_STATE, achieveVO.getState());
		AchievePushHelper.pushAttribute(actorId, achieveVO.getAchieveId(), changedValues);
		
		return Result.valueOf();
	}
	
	/**
	 * 添加达成的奖励
	 * @param actorId
	 * @param achieveConfig
	 */
	private void giveAchieveReward(long actorId, AchievementConfig achieveConfig) {
		if (achieveConfig.getGolds() > 0) {
			actorFacade.addGold(actorId, GoldAddType.ACHIEVEMENT, achieveConfig.getGolds());
		}
		
		if (achieveConfig.getTickets() > 0) {
			vipFacade.addTicket(actorId, TicketAddType.ACHIEVEMENT, achieveConfig.getTickets());
		}
		
		if (achieveConfig.getEnergy() > 0) {
			actorFacade.addEnergy(actorId, EnergyAddType.ACHIEVE, achieveConfig.getEnergy());
		}
		
		if (achieveConfig.getVit() > 0) {
			actorFacade.addVIT(actorId, VITAddType.ACHIEVE, achieveConfig.getVit());
		}
		
		if(achieveConfig.isEnergyFull()){
			actorFacade.fullEnergy(actorId, EnergyAddType.ACHIEVE);
		}
		
		if(achieveConfig.isVitFull()){
			actorFacade.fullVIT(actorId, VITAddType.ACHIEVE);
		}
		
		if(achieveConfig.getReputation() > 0){
			actorFacade.addReputation(actorId, ReputationAddType.ACHIEVE, achieveConfig.getReputation());
		}
		
		Map<Integer, Integer> goods = achieveConfig.getGoods();
		for(Entry<Integer, Integer> good : goods.entrySet()){
			goodsFacade.addGoodsVO(actorId, GoodsAddType.ACHIEVE_AWARD, good.getKey(), good.getValue());
		}
		
		Map<Integer, Integer> heroSouls = achieveConfig.getHeroSouls();
		for(Entry<Integer, Integer> heroSoul : heroSouls.entrySet()){
			heroSoulFacade.addSoul(actorId, HeroSoulAddType.ACHIEVEMENT, heroSoul.getKey(), heroSoul.getValue());
		}
		
		Map<Integer, Integer> equips = achieveConfig.getEquips();
		for(Entry<Integer, Integer> equip : equips.entrySet()){
			for (int i = 0; i < equip.getValue(); i++) {
				equipFacade.addEquip(actorId, EquipAddType.ACHIEVE_AWARD, equip.getKey());
			}
		}
		
		Map<Integer, Integer> heros = achieveConfig.getHeros();
		for(Entry<Integer, Integer> hero : heros.entrySet()){
			for (int i = 0; i < hero.getValue(); i++) {
				heroFacade.addHero(actorId, HeroAddType.ACHIEVEMENT, hero.getKey());
			}
		}
	}
	
}

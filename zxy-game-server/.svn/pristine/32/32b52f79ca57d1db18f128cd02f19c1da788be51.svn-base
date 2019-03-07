package com.jtang.gameserver.module.adventures.vipactivity.facade.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.GameStatusCodeConstant;
import com.jiatang.common.model.EquipVO;
import com.jiatang.common.model.HeroVO;
import com.jtang.core.protocol.StatusCode;
import com.jtang.core.result.Result;
import com.jtang.core.schedule.Schedule;
import com.jtang.core.schedule.ZeroListener;
import com.jtang.gameserver.component.listener.ActorLoginListener;
import com.jtang.gameserver.dataconfig.model.HeroConfig;
import com.jtang.gameserver.dataconfig.model.ResetHeroEquipConfig;
import com.jtang.gameserver.dataconfig.model.SmeltConfig;
import com.jtang.gameserver.dataconfig.service.BreakThroughService;
import com.jtang.gameserver.dataconfig.service.EquipService;
import com.jtang.gameserver.dataconfig.service.HeroService;
import com.jtang.gameserver.dataconfig.service.ResetHeroEquipService;
import com.jtang.gameserver.dataconfig.service.SmeltService;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.module.adventures.vipactivity.constant.HeroEquipResetRule;
import com.jtang.gameserver.module.adventures.vipactivity.facade.ResetHeroEquipFacade;
import com.jtang.gameserver.module.adventures.vipactivity.helper.VipActivityPushHelper;
import com.jtang.gameserver.module.adventures.vipactivity.type.VipActivityKey;
import com.jtang.gameserver.module.equip.facade.EquipFacade;
import com.jtang.gameserver.module.equip.type.EquipAddType;
import com.jtang.gameserver.module.equip.type.EquipDecreaseType;
import com.jtang.gameserver.module.goods.facade.GoodsFacade;
import com.jtang.gameserver.module.goods.type.GoodsAddType;
import com.jtang.gameserver.module.hero.facade.HeroFacade;
import com.jtang.gameserver.module.hero.type.HeroAddType;
import com.jtang.gameserver.module.hero.type.HeroDecreaseType;
import com.jtang.gameserver.module.smelt.constant.SmeltRule;
import com.jtang.gameserver.module.smelt.facade.SmeltFacade;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.facade.VipFacade;
import com.jtang.gameserver.module.user.model.VipPrivilege;
import com.jtang.gameserver.module.user.type.GoldAddType;
import com.jtang.gameserver.module.user.type.TicketDecreaseType;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class ResetHeroEquipFacadeImpl implements ResetHeroEquipFacade,ZeroListener,ActorLoginListener {

	@Autowired
	private ActorFacade actorFacade;

	@Autowired
	private EquipFacade equipFacade;

	@Autowired
	private HeroFacade heroFacade;

	@Autowired
	private VipFacade vipFacade;

	@Autowired
	private GoodsFacade goodsFacade;
	
	@Autowired
	private Schedule schedule;
	
	@Autowired
	private PlayerSession playerSession;
	
	@Autowired
	private SmeltFacade smeltFacade;

	@Override
	public Result resetHero(long actorId,int heroId) {
		int vipLevel = vipFacade.getVipLevel(actorId);
		VipPrivilege vipPrivilege = vipFacade.getVipPrivilege(vipLevel);
		if (vipPrivilege.addHeroEquipReset == 0) {
			return Result.valueOf(GameStatusCodeConstant.VIP_LEVEL_NO_ENOUGH);
		}
		if (getRestNum(actorId) >= vipPrivilege.addHeroEquipReset){
			return Result.valueOf(GameStatusCodeConstant.RESET_NUM_NOT_ENOUGH);
		}
		Actor actor = actorFacade.getActor(actorId);
		if (actor == null) {
			return Result.valueOf(GameStatusCodeConstant.ACTOR_ID_ERROR);
		}

		HeroVO heroVO = heroFacade.getHero(actorId, heroId);
		if (heroVO == null) {
			return Result.valueOf(GameStatusCodeConstant.HERO_NOT_EXITS);
		}
		if(heroVO.delveCostGold == 0 && heroVO.delveStoneNum == 0 && heroVO.breakThroughCount == 0){
			return Result.valueOf(GameStatusCodeConstant.HERO_NOT_DELVE);
		}

		List<Integer> heroIds = new ArrayList<Integer>();
		heroIds.add(heroId);
		short canDelete = heroFacade.canDelete(actorId, heroIds);
		if (canDelete != 0) {
			return Result.valueOf(canDelete);
		}

		ResetHeroEquipConfig config = ResetHeroEquipService.getConfig(1);
		int heroStar = HeroService.get(heroVO.heroId).getStar();
		if (!vipFacade.decreaseTicket(actorId, TicketDecreaseType.HERO_RESET, config.getCostTicket(heroStar), 0, 0)) {// 扣除点券
			return Result.valueOf(StatusCode.TICKET_NOT_ENOUGH);
		}
		
		heroFacade.removeHero(actorId, HeroDecreaseType.HERO_RESET, heroIds);// 删除仙人
		long giveGold = 0;
		if(actor.level <= HeroEquipResetRule.RESET_MAX_REWARD_LEVEL){
			giveGold = heroVO.delveCostGold;
		}else{
			giveGold = vipPrivilege.getGold(heroVO.delveCostGold);
		}
		actorFacade.addGold(actorId, GoldAddType.HERO_RESET, giveGold);// 返还金币
		
		heroFacade.addResetNum(actorId);//增加仙人重置次数
		
		int goodsId = config.returnGoodsId;
		int goodsNum = 0;
		if(actor.level <= HeroEquipResetRule.RESET_MAX_REWARD_LEVEL){
			goodsNum = heroVO.delveStoneNum;
		}else{
			goodsNum = vipPrivilege.getGoods(heroVO.delveStoneNum);
		}
		goodsFacade.addGoodsVO(actorId, GoodsAddType.HERO_RESET, goodsId, goodsNum);// 返还潜修石

		//如果仙人突破过,根据vip等级、突破消耗过的魂魄换算一定比例的精魄数返还给玩家
		int heroSoulNum = 0;
		HeroConfig heroConfig = HeroService.get(heroVO.heroId);
		for (int i = 1; i <= heroVO.breakThroughCount; i++) {
			heroSoulNum += BreakThroughService.get(heroConfig.getStar(), i).getSoulCount();
		}
		if(heroSoulNum != 0 && vipPrivilege.breakThrough > 0){
			SmeltConfig smeltConfig = SmeltService.getSmeltConfig(heroConfig.getStar(), false);
			int soulNum = 0;
			if(actor.level <= HeroEquipResetRule.RESET_MAX_REWARD_LEVEL){
				soulNum = smeltConfig.soul * heroSoulNum;
			}else{
				soulNum = vipPrivilege.getBreakThrough(smeltConfig.soul * heroSoulNum);
			}
			if(soulNum > 0){
				goodsFacade.addGoodsVO(actorId, GoodsAddType.HERO_RESET, SmeltRule.SOUL_GOODS_ID,soulNum);
			}
		}
		heroFacade.addHero(actorId, HeroAddType.HERO_RESET, heroId);// 添加仙人
		
		return Result.valueOf();
	}

	@Override
	public Result resetEquip(long actorId, long uuid) {
		int vipLevel = vipFacade.getVipLevel(actorId);
		VipPrivilege vipPrivilege = vipFacade.getVipPrivilege(vipLevel);
		if (vipPrivilege.addHeroEquipReset == 0) {
			return Result.valueOf(GameStatusCodeConstant.VIP_LEVEL_NO_ENOUGH);
		}
		if (getRestNum(actorId) >= vipPrivilege.addHeroEquipReset){
			return Result.valueOf(GameStatusCodeConstant.RESET_NUM_NOT_ENOUGH);
		}
		Actor actor = actorFacade.getActor(actorId);
		if (actor == null) {
			return Result.valueOf(GameStatusCodeConstant.ACTOR_ID_ERROR);
		}

		EquipVO equipVO = equipFacade.get(actorId, uuid);
		if (equipVO == null) {
			return Result.valueOf(GameStatusCodeConstant.EQUIP_NOT_EXISTS);
		}
		if(equipVO.costGold == 0 && equipVO.costStoneNum == 0){
			return Result.valueOf(GameStatusCodeConstant.EQUIP_NOT_UP);
		}

		List<Long> uuidList = new ArrayList<>();
		uuidList.add(uuid);
		short canDelete = equipFacade.canDelete(actorId, uuidList);
		if (canDelete != 0) {
			return Result.valueOf(canDelete);
		}

		ResetHeroEquipConfig config = ResetHeroEquipService.getConfig(2);
		int equipStar = EquipService.get(equipVO.equipId).getStar();
		if (!vipFacade.decreaseTicket(actorId, TicketDecreaseType.EQUIP_RESET, config.getCostTicket(equipStar), 0, 0)) {// 扣除点券
			return Result.valueOf(StatusCode.TICKET_NOT_ENOUGH);
		}
		equipFacade.delEquip(actorId, EquipDecreaseType.EQUIP_RESET, uuidList);// 删除装备
		long golds = 0;
		if(actor.level <= HeroEquipResetRule.RESET_MAX_REWARD_LEVEL){
			golds = equipVO.costGold;
		}else{
			golds = vipPrivilege.getGold(equipVO.costGold);
		}
		actorFacade.addGold(actorId, GoldAddType.EQUIP_RESET, golds);// 返还金币

		equipFacade.addResetNum(actorId);
		
		int goodsId = config.returnGoodsId;
		int goodsNum = 0;
		if(actor.level <= HeroEquipResetRule.RESET_MAX_REWARD_LEVEL){
			goodsNum = equipVO.costStoneNum;
		}else{
			goodsNum = vipPrivilege.getGoods(equipVO.costStoneNum);
		}
		goodsFacade.addGoodsVO(actorId, GoodsAddType.EQUIP_RESET, goodsId, goodsNum);// 返还精炼石

		equipFacade.addEquip(actorId, EquipAddType.EQUIP_RESET, equipVO.equipId);// 添加装备

		return Result.valueOf();
	}
	
	private int getRestNum(long actorId){
		int heroResetNum = heroFacade.getResetNum(actorId);
		int equipResetNum = equipFacade.getResetNum(actorId);
		return heroResetNum + equipResetNum;
	}

	@Override
	public void onZero() {
		for(Long actorId:playerSession.onlineActorList()){
			equipFacade.flushResetNum(actorId);
			heroFacade.flushResetNum(actorId);
			VipActivityPushHelper.pushVipActivity(actorId, VipActivityKey.HERO_EQUIP_RESET_NUM, 0);
		}
	}

	@Override
	public void onLogin(long actorId) {
		equipFacade.flushResetNum(actorId);
		heroFacade.flushResetNum(actorId);
	}
}

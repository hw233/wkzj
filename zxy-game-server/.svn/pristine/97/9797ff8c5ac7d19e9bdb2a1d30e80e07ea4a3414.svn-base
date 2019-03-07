package com.jtang.gameserver.module.adventures.vipactivity.helper;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.result.ObjectReference;
import com.jtang.core.utility.CollectionUtils;
import com.jtang.gameserver.module.adventures.vipactivity.model.FailReward;
import com.jtang.gameserver.module.equip.facade.EquipFacade;
import com.jtang.gameserver.module.equip.type.EquipAddType;
import com.jtang.gameserver.module.goods.facade.GoodsFacade;
import com.jtang.gameserver.module.goods.type.GoodsAddType;
import com.jtang.gameserver.module.hero.facade.HeroSoulFacade;
import com.jtang.gameserver.module.hero.type.HeroSoulAddType;
@Component
public class ComposeHelper {
	private static ObjectReference<ComposeHelper> ref = new ObjectReference<ComposeHelper>();
	
	@Autowired
	private EquipFacade equipFacade;
	@Autowired
	private GoodsFacade goodsFacade;
	@Autowired
	private HeroSoulFacade heroSoulFacade;
	
	@PostConstruct
	protected void init() {
		ref.set(this);
	}
	
	/**
	 * 装备合成失败处理
	 * @param actorId
	 * @param list
	 */
	public static void equipComposeFail(long actorId, List<FailReward> list) {
		ref.get().equipComposeFailReward(actorId, list);
	}
	/**
	 * 英雄合成失败处理
	 * @param actorId
	 * @param list
	 */
	public static void heroComposeFail(long actorId, List<FailReward> list) {
		ref.get().heroComposeFailReward(actorId, list);
	}
	
	private void equipComposeFailReward(long actorId, List<FailReward> list) {
		if (CollectionUtils.isEmpty(list)) {
			return;
		}
		
		for (FailReward failReward : list) {
			switch (failReward.type) {
			case EQUIP:
				equipFacade.addEquip(actorId, EquipAddType.COMPOSE_EQUIP_FAIL, failReward.id);
				break;
			case GOODS:
				goodsFacade.addGoodsVO(actorId, GoodsAddType.COMPOSE_EQUIP_FAIL, failReward.id, failReward.num);
				break;
			case HEROSOUL:
				heroSoulFacade.addSoul(actorId, HeroSoulAddType.COMPOSE_EQUIP_FAIL, failReward.id, failReward.num);
				break;

			default:
				break;
			}
		}
	}
	private void heroComposeFailReward(long actorId, List<FailReward> list) {
		if (CollectionUtils.isEmpty(list)) {
			return;
		}
		
		for (FailReward failReward : list) {
			switch (failReward.type) {
			case EQUIP:
				equipFacade.addEquip(actorId, EquipAddType.COMPOSE_HERO_FAIL, failReward.id);
				break;
			case GOODS:
				goodsFacade.addGoodsVO(actorId, GoodsAddType.COMPOSE_HERO_FAIL, failReward.id, failReward.num);
				break;
			case HEROSOUL:
				heroSoulFacade.addSoul(actorId, HeroSoulAddType.COMPOSE_HERO_FAIL, failReward.id, failReward.num);
				break;
				
			default:
				break;
			}
		}
	}
}

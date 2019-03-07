package com.jtang.gameserver.module.user.helper;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.result.ObjectReference;
import com.jtang.gameserver.module.battle.facade.BattleFacade;
import com.jtang.gameserver.module.goods.facade.GoodsFacade;
import com.jtang.gameserver.module.hero.facade.HeroSoulFacade;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.facade.VipFacade;

@Component
public class VipHelper {
	private static ObjectReference<VipHelper> ref = new ObjectReference<VipHelper>();
	
	@Autowired
	private VipFacade vipFacade;
	
	@Autowired
	private BattleFacade battleFacade;
	
	@Autowired
	private HeroSoulFacade heroSoulFacade;
	
	@Autowired
	private ActorFacade actorFacade;
	
	@Autowired
	private GoodsFacade goodsFacade;
	@PostConstruct
	protected void init() {
		ref.set(this);
	}
	
	public static VipHelper getInstance() {
		return ref.get();
	}
	
//	/**
//	 * 处理12vip等级特权
//	 * @param attackId
//	 * @param heros
//	 */
//	public Map<Integer, Integer> handlerVip12(long attackId, Collection<HeroVO> heros){
//		Map<Integer, Integer> herosoul = new HashMap<>();
//		//11级vip特权
//		int vipLevel =  vipFacade.getVipLevel(attackId);
//		if (vipLevel >= Vip12Privilege.vipLevel){
//			int winNum = battleFacade.getBatteWinNum(attackId, BattleType.POWER);
//			winNum += battleFacade.getBatteWinNum(attackId, BattleType.SNATCH);
//			winNum += battleFacade.getBatteWinNum(attackId, BattleType.ALLY);
//			Vip12Privilege vip11Privilege = (Vip12Privilege) vipFacade.getVipPrivilege(Vip12Privilege.vipLevel);
//			if (winNum <= vip11Privilege.battleTimes){
//				List<HeroVO> herovos = new ArrayList<>();
//				for (HeroVO heroVO : heros) {
//					herovos.add(heroVO);
//				}
//				int index = RandomUtils.nextIntIndex(herovos.size());
//				
//				HeroVO resultHero = herovos.get(index);
//				heroSoulFacade.addSoul(attackId, HeroSoulAddType.VIP_GIVE, resultHero.getHeroId(), 1);
//				herosoul.put(resultHero.getHeroId(), 1);
//			}
//		}
//		return herosoul;
//	}
	
//	/**
//	 * vip11最强势力获得物品翻倍
//	 * @param actorId
//	 * @param goodsId 物品id
//	 * @param goodsNum 物品数量
//	 * @return 返回翻倍结果
//	 */
//	public int vip12PowerRewardGoodsHandler(long actorId, int goodsId, int goodsNum) {
//		int vipLevel =  vipFacade.getVipLevel(actorId);
//		Vip12Privilege vip11Privilege = (Vip12Privilege) vipFacade.getVipPrivilege(Vip12Privilege.vipLevel);
//		if (vipLevel >= vip11Privilege.getVipLevel()) {
//			if (goodsId == vip11Privilege.goodsId) {
//				return goodsNum * vip11Privilege.rewardNum;
//			}
//		}
//		return goodsNum;
//		
//	}
	
	
//	/**
//	 * vip3每天前3次抢夺等级不低于自己的对手大胜，必得聚仙符
//	 * @param actorId
//	 * @param winlevel
//	 * @param targetLevel
//	 * @return 物品集合
//	 */
//	public Map<Integer, Integer> handlerVip3(long actorId, WinLevel winlevel, int targetLevel) {
//		Map<Integer, Integer> goods = new HashMap<Integer, Integer>();
//		if (winlevel == WinLevel.BIG_WIN){
//			int battleTimes = battleFacade.getBatteWinNum(actorId, BattleType.SNATCH);
//			int vipLevel = vipFacade.getVipLevel(actorId);
//			Vip3Privilege vip3Privilege = (Vip3Privilege) vipFacade.getVipPrivilege(Vip3Privilege.vipLevel);
//			int selfActorLevel = actorFacade.getActor(actorId).level;
//			if (vipLevel >= vip3Privilege.getVipLevel() && battleTimes <= vip3Privilege.snakeTimes && targetLevel >= selfActorLevel){
//				goodsFacade.addGoodsVO(actorId, GoodsAddType.SNATCH_VIP_ADD, vip3Privilege.earnGoodsId, 1);
//				goods.put(vip3Privilege.earnGoodsId, 1);
//			}
//		}
//		
//		return goods;
//	}
	
//	public void addRefineEquipNum(EquipVO equipVO, int vipLevel) {
//		VipPrivilege vipPrivilege = vipFacade.getVipPrivilege(vipLevel);
//		if (vipPrivilege != null) {
//			equipVO.maxRefineNum += vipPrivilege.addRefineEquipNum;
//		}
//	}
	
}

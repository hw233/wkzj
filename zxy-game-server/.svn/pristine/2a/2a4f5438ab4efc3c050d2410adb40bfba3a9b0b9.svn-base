//package com.jtang.gameserver.module.adventures.vipactivity.facade.impl;
//
//import static com.jiatang.common.GameStatusCodeConstant.ACTOR_HAD_RESET;
//import static com.jiatang.common.GameStatusCodeConstant.ACTOR_NOT_FOUND;
//import static com.jiatang.common.GameStatusCodeConstant.VIP_LEVEL_NO_ENOUGH;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.jtang.core.db.DBQueue;
//import com.jtang.core.db.Entity;
//import com.jtang.core.lock.ChainLock;
//import com.jtang.core.lock.LockUtils;
//import com.jtang.core.protocol.StatusCode;
//import com.jtang.core.result.Result;
//import com.jtang.gameserver.dataconfig.model.HeroConfig;
//import com.jtang.gameserver.dataconfig.service.HeroService;
//import com.jtang.gameserver.dataconfig.service.VipService;
//import com.jtang.gameserver.dbproxy.entity.Achievement;
//import com.jtang.gameserver.dbproxy.entity.Actor;
//import com.jtang.gameserver.dbproxy.entity.Ally;
//import com.jtang.gameserver.dbproxy.entity.Bable;
//import com.jtang.gameserver.dbproxy.entity.Battle;
//import com.jtang.gameserver.dbproxy.entity.Buffers;
//import com.jtang.gameserver.dbproxy.entity.Delve;
//import com.jtang.gameserver.dbproxy.entity.Equips;
//import com.jtang.gameserver.dbproxy.entity.Favor;
//import com.jtang.gameserver.dbproxy.entity.Gift;
//import com.jtang.gameserver.dbproxy.entity.Goods;
//import com.jtang.gameserver.dbproxy.entity.HeroSoul;
//import com.jtang.gameserver.dbproxy.entity.Heros;
//import com.jtang.gameserver.dbproxy.entity.Lineup;
//import com.jtang.gameserver.dbproxy.entity.Recruit;
//import com.jtang.gameserver.dbproxy.entity.Shop;
//import com.jtang.gameserver.dbproxy.entity.Sign;
//import com.jtang.gameserver.dbproxy.entity.Snatch;
//import com.jtang.gameserver.dbproxy.entity.Stories;
//import com.jtang.gameserver.dbproxy.entity.TrialCave;
//import com.jtang.gameserver.dbproxy.entity.VipPrivilege;
//import com.jtang.gameserver.module.adventures.achievement.dao.AchieveDao;
//import com.jtang.gameserver.module.adventures.bable.dao.BableDao;
//import com.jtang.gameserver.module.adventures.favor.dao.FavorDao;
//import com.jtang.gameserver.module.adventures.shop.dao.ShopDao;
//import com.jtang.gameserver.module.adventures.vipactivity.constant.ActorResetRule;
//import com.jtang.gameserver.module.adventures.vipactivity.dao.VipPrivilegeDao;
//import com.jtang.gameserver.module.adventures.vipactivity.facade.ActorResetFacade;
//import com.jtang.gameserver.module.ally.dao.AllyDao;
//import com.jtang.gameserver.module.app.dao.AppRecordDao;
//import com.jtang.gameserver.module.battle.dao.BattleDao;
//import com.jtang.gameserver.module.buffer.dao.BufferDao;
//import com.jtang.gameserver.module.delve.dao.DelveDao;
//import com.jtang.gameserver.module.equip.dao.EquipDao;
//import com.jtang.gameserver.module.equip.facade.EquipFacade;
//import com.jtang.gameserver.module.gift.dao.GiftDao;
//import com.jtang.gameserver.module.goods.dao.GoodsDao;
//import com.jtang.gameserver.module.goods.facade.GoodsFacade;
//import com.jtang.gameserver.module.hero.dao.HeroDao;
//import com.jtang.gameserver.module.hero.dao.HeroSoulDao;
//import com.jtang.gameserver.module.hero.facade.HeroFacade;
//import com.jtang.gameserver.module.lineup.dao.LineupDao;
//import com.jtang.gameserver.module.recruit.dao.RecruitDao;
//import com.jtang.gameserver.module.sign.dao.SignDao;
//import com.jtang.gameserver.module.snatch.dao.SnatchDao;
//import com.jtang.gameserver.module.story.dao.StoryDao;
//import com.jtang.gameserver.module.user.dao.ActorDao;
//import com.jtang.gameserver.module.user.facade.UserDisableFacade;
//import com.jtang.gameserver.module.user.facade.VipFacade;
//import com.jtang.gameserver.module.user.helper.UserPushHelper;
//import com.jtang.gameserver.module.user.type.KickOffType;
//import com.jtang.gameserver.module.user.type.TicketAddType;
//import com.jtang.gameserver.module.user.type.TicketDecreaseType;
//@Component
//public class ActorResetFacadeImpl implements ActorResetFacade {
//	
//	private static final Logger LOGGER = LoggerFactory.getLogger(ActorResetFacadeImpl.class);
//
//	@Autowired
//	private DBQueue dbQueue;
//	@Autowired
//	private ActorDao actorDao;
//	@Autowired
//	private AppRecordDao appRecordDao;
//	@Autowired
//	private AchieveDao achieveDao;
//	@Autowired
//	private AllyDao allyDao;
//	@Autowired
//	private BableDao bableDao;
//	@Autowired
//	private BattleDao battleDao;
//	@Autowired
//	private BufferDao bufferDao;
//	@Autowired
//	private DelveDao delveDao;
//	@Autowired
//	private EquipDao equipDao;
//	@Autowired
//	private FavorDao favorDao;
//	@Autowired
//	private GiftDao giftDao;
//	@Autowired
//	private GoodsDao goodsDao;
//	@Autowired
//	private HeroDao heroDao;
//	@Autowired
//	private HeroSoulDao heroSoulDao;
//	@Autowired
//	private LineupDao lineupDao;
//	@Autowired
//	private RecruitDao recruitDao;
////	@Autowired
////	private RefineDao refineDao;
//	@Autowired
//	private ShopDao shopDao;
//	@Autowired
//	private SignDao signDao;
//	@Autowired
//	private SnatchDao snatchDao;
//	@Autowired
//	private StoryDao storyDao;
//	@Autowired
//	private VipPrivilegeDao vipPrivilegeDao;
//	
//	@Autowired
//	private UserDisableFacade userDisableFacade;
//	
//	@Autowired
//	private VipFacade vipFacade;
//	
//	@Autowired
//	private GoodsFacade goodsFacade;
//	
//	@Autowired
//	private HeroFacade heroFacade;
//	
//	@Autowired
//	private EquipFacade equipFacade;
//	
//	@Override
//	public Result resetActor(long actorId) {
//		int vipLevel = vipFacade.getVipLevel(actorId);
//		if (vipLevel < ActorResetRule.RESET_USE_VIP_LEVEL) { 
//			return Result.valueOf(VIP_LEVEL_NO_ENOUGH);
//		}
//		Actor actor = actorDao.getActor(actorId);
//		if (actor == null) {
//			return Result.valueOf(ACTOR_NOT_FOUND);
//		}
//		
//		if (actor.getGuideMap().containsKey(ActorResetRule.RESET_ACOTR_KEY)) {
//			return Result.valueOf(ACTOR_HAD_RESET);
//		}
//		//TODO
//		//封住账号
//		//踢人下线
//		//备份数据
//		//清理数据
//		//解封账号
//		userDisableFacade.disable(actorId, ActorResetRule.RESET_USE_TIME);
//		UserPushHelper.kickOff(actorId, KickOffType.RESET_ACTOR);
//		
//		List<Entity<?>> entities = new ArrayList<>();
//		entities.add(actor);
//		Achievement achievement = achieveDao.get(actorId);
//		entities.add(achievement);
//		Ally ally = allyDao.get(actorId);
//		entities.add(ally);
//		Bable bable = bableDao.get(actorId);
//		entities.add(bable);
//		Battle battle = battleDao.get(actorId);
//		entities.add(battle);
//		Buffers buffers = bufferDao.get(actorId);
//		entities.add(buffers);
//		Delve delve = delveDao.get(actorId);
//		entities.add(delve);
////		Enhanced enhanced = enhancedDao.get(actorId);
////		entities.add(enhanced);
//		Equips equips = equipDao.get(actorId);
//		entities.add(equips);
//		Favor favor = favorDao.get(actorId);
//		entities.add(favor);
//		Gift gift = giftDao.get(actorId);
//		entities.add(gift);
//		Goods goods = goodsDao.get(actorId);
//		entities.add(goods);
//		Heros heros = heroDao.get(actorId);
//		entities.add(heros);
//		HeroSoul heroSoul = heroSoulDao.get(actorId);
//		entities.add(heroSoul);
//		Lineup lineup = lineupDao.getLineup(actorId);
//		entities.add(lineup);
//		Recruit recruit = recruitDao.get(actorId);
//		entities.add(recruit);
////		Refine refine = refineDao.get(actorId);
////		entities.add(refine);
//		Shop shop = shopDao.get(actorId);
//		entities.add(shop);
//		Sign sign = signDao.get(actorId);
//		entities.add(sign);
//		Snatch snatch = snatchDao.get(actorId);
//		entities.add(snatch);
//		Stories stories = storyDao.get(actorId);
//		entities.add(stories);
//		VipPrivilege vipPrivilege = vipPrivilegeDao.get(actorId);
//		entities.add(vipPrivilege);
//		
//		ChainLock lock = LockUtils.getLock(entities);
//		try {
//			lock.lock();
//			achievement.reset();
//			ally.reset();
//			actor.reset();
//			battle.reset();
//			buffers.reset();
//			delve.reset();
////			enhanced.reset();
//			equips.reset();
//			favor.reset();
//			gift.reset();
//		    goods.reset();
//		    HeroConfig heroConfig = HeroService.get(actor.heroId);
//			heros.reset(heroConfig);
//			heroSoul.reset();
//			lineup.reset(actor.heroId);
//			recruit.reset();
////			refine.reset();
//			shop.reset();
////			sign.reset();
//			snatch.reset();
//			stories.reset();
//			vipPrivilege.reset();
//			appRecordDao.resetRecord(actorId);//清理活动记录
//			
//			actor.addGuide(ActorResetRule.RESET_ACOTR_KEY, 1);
//			dbQueue.updateQueue(entities);
//			
//			//扣除所有点券
//			int totalTicket = vipFacade.getTicket(actorId);
//			vipFacade.decreaseTicket(actorId, TicketDecreaseType.ACTOR_RESET, totalTicket, 0, 0);
//			//返还部分点券
//			int giveNum = VipService.getByLevel(vipLevel).rechargeTicket * 2;//双倍
//			vipFacade.addTicket(actorId, TicketAddType.ACTOR_RESET, giveNum);
//			
//			userDisableFacade.enable(actorId);
//			
//			LOGGER.warn(String.format("use actor reset, actorId:[%s]", actorId));
//			return Result.valueOf();
//		} catch (Exception e) {
//			LOGGER.error("{}", e);
//			return Result.valueOf(StatusCode.OPERATION_ERROR);
//		} finally {
//			lock.unlock();
//		}
//		
//	}
//
//}

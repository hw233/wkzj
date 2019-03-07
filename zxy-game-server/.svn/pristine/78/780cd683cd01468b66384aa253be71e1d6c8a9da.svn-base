package com.jtang.gameserver.module.herobook.facade.impl;

import static com.jiatang.common.GameStatusCodeConstant.*;

import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.model.HeroVO;
import com.jtang.core.event.Event;
import com.jtang.core.event.EventBus;
import com.jtang.core.event.GameEvent;
import com.jtang.core.event.Receiver;
import com.jtang.core.lock.ChainLock;
import com.jtang.core.lock.LockUtils;
import com.jtang.core.model.RewardObject;
import com.jtang.core.model.RewardType;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.component.event.EventKey;
import com.jtang.gameserver.component.event.HeroAttributeChangeEvent;
import com.jtang.gameserver.component.event.HeroLevelUpEvent;
import com.jtang.gameserver.component.listener.ActorLoginListener;
import com.jtang.gameserver.dataconfig.model.HeroBookConfig;
import com.jtang.gameserver.dataconfig.model.HeroBookRewardConfig;
import com.jtang.gameserver.dataconfig.model.HeroConfig;
import com.jtang.gameserver.dataconfig.service.HeroBookRewardService;
import com.jtang.gameserver.dataconfig.service.HeroBookService;
import com.jtang.gameserver.dataconfig.service.HeroService;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.dbproxy.entity.HeroBook;
import com.jtang.gameserver.module.chat.facade.ChatFacade;
import com.jtang.gameserver.module.equip.facade.EquipFacade;
import com.jtang.gameserver.module.equip.type.EquipAddType;
import com.jtang.gameserver.module.goods.facade.GoodsFacade;
import com.jtang.gameserver.module.goods.type.GoodsAddType;
import com.jtang.gameserver.module.hero.facade.HeroFacade;
import com.jtang.gameserver.module.hero.facade.HeroSoulFacade;
import com.jtang.gameserver.module.hero.type.HeroSoulAddType;
import com.jtang.gameserver.module.herobook.constant.HeroBookRule;
import com.jtang.gameserver.module.herobook.dao.HeroBookDao;
import com.jtang.gameserver.module.herobook.facade.HeroBookFacade;
import com.jtang.gameserver.module.herobook.handler.response.HeroBookResponse;
import com.jtang.gameserver.module.herobook.handler.response.HeroBookRewardResponse;
import com.jtang.gameserver.module.user.facade.ActorFacade;
@Component
public class HeroBookFacadeImpl implements HeroBookFacade, Receiver, ActorLoginListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(HeroBookFacadeImpl.class);
	
	@Autowired
	private HeroBookDao heroBookDao;
	
	@Autowired
	private EquipFacade equipFacade;
	
	@Autowired
	private GoodsFacade goodsFacade;
	
	@Autowired
	private HeroSoulFacade heroSoulFacade;
	
	@Autowired
	private EventBus eventBus;
	
	@Autowired
	private HeroFacade heroFacade;
	
	@Autowired
	private ActorFacade actorFacade;
	
	@Autowired
	private ChatFacade chatFacade;
	
	@PostConstruct
	private void init() {
		eventBus.register(EventKey.HERO_LEVEL_UP, this);
		eventBus.register(EventKey.HERO_ATTRIBUTE_CHANGE, this);
	}
	
	@Override
	public HeroBookResponse getHeroBookData(long actorId) {
		HeroBook heroBook = heroBookDao.get(actorId);
		HeroBookResponse heroBookResponse = new HeroBookResponse(heroBook);
		return heroBookResponse;
	}

	@Override
	public TResult<HeroBookRewardResponse> getReward(long actorId, int orderId) {
		Actor actor = actorFacade.getActor(actorId);
		if (actor.level < HeroBookRule.OPEN_ACTOR_LEVEL) {
			return TResult.valueOf(HERO_BOOK_ACTOR_LEVEL_NOT_ENOUGH);
		}
		HeroBook heroBook = heroBookDao.get(actorId);
		if (heroBook.containsOrderId(orderId) == false) {
			return TResult.valueOf(HERO_BOOK_REWARD_ERROR);
		}
		HeroBookRewardConfig cfg = HeroBookRewardService.get(orderId);
		if (cfg == null) {
			return TResult.valueOf(HERO_BOOK_REWARD_ERROR);
		}
		int completeNum = 0;
		for (Integer heroId : heroBook.historyHeroIds) {
			HeroConfig heroCfg = HeroService.get(heroId);
			if (heroCfg.getStar() == cfg.getHeroStar()) {
				completeNum += 1;
			}
		}
		
		if (cfg.getHeroNum() > completeNum) {
			return TResult.valueOf(HERO_BOOK_REWARD_NOT_FINISH);
		}
		
		heroBook.updateOrderId(orderId, cfg.getNextOrderId());
		heroBookDao.update(heroBook);
		List<RewardObject> list = cfg.getRewardObjects();
		sendReward(actorId, list);
		HeroBookRewardResponse response = new HeroBookRewardResponse(cfg.getNextOrderId(), list);
		chatFacade.sendHeroBookChat(actorId, cfg.getHeroNum(), cfg.getHeroStar(), list);
		return TResult.sucess(response);
	}
	
	
	/**
	 * 发放奖励
	 * @param actorId
	 * @param id
	 * @param num
	 * @param rewardType
	 */
	private void sendReward(long actorId, int id, int num, RewardType rewardType) {
		switch (rewardType) {
		case EQUIP: {
			for (int i = 0; i < num; i++) {
				equipFacade.addEquip(actorId, EquipAddType.DEMON_EXCHANGE, id);
			}
			break;
		}
		case HEROSOUL: {
			heroSoulFacade.addSoul(actorId, HeroSoulAddType.DEMON_EXCHANGE, id, num);
			break;
		}
		case GOODS: {
			goodsFacade.addGoodsVO(actorId, GoodsAddType.DEMON_EXCHANGE, id, num);
			break;
		}

		default:
			LOGGER.error(String.format("类型错误，type:[%s]", rewardType.getCode()));
			break;
		}
	}
	
	/**
	 * 发放奖励
	 * @param actorId
	 * @param list
	 */
	private void sendReward(long actorId, List<RewardObject> list) {
		for (RewardObject rewardObject : list) {
			sendReward(actorId, rewardObject.id, rewardObject.num, rewardObject.rewardType);
		}
	}
	
	@Override
	public void onEvent(Event paramEvent) {
		GameEvent gameEvent = paramEvent.convert();
		long actorId = gameEvent.actorId;
		int heroId = 0;
		if (gameEvent.name.equals(EventKey.HERO_LEVEL_UP)) {
			HeroLevelUpEvent heroLevelUpEvent = gameEvent.convert();
			heroId = heroLevelUpEvent.getHeroId();
		} else if (gameEvent.name.equals(EventKey.HERO_ATTRIBUTE_CHANGE)) {
			HeroAttributeChangeEvent heroAttributeChangeEvent = gameEvent.convert();
			heroId = heroAttributeChangeEvent.getHeroId();
		} else {
			return;
		}
		
		updateBook(actorId, heroId);
	}

	@Override
	public void onLogin(long actorId) {
		HeroBook heroBook = heroBookDao.get(actorId);
		if (heroBook.historyHeroIds.isEmpty() == false) { //用于已初始化过的角色过滤，减轻计算量
			return;
		}
		Collection<HeroVO> list = heroFacade.getList(actorId);
		for (HeroVO heroVO : list) {
			updateBook(actorId, heroVO.heroId);
		}
	}
	
	/**
	 * 更新角色英雄达成条件
	 * @param actorId
	 * @param heroId
	 */
	private void updateBook(long actorId, int heroId) {
		HeroVO hero = heroFacade.getHero(actorId, heroId);
		HeroBookConfig cfg = HeroBookService.get(heroId);
		if (cfg == null) {
			return;
		}
		if (cfg.getDelveNum() > hero.usedDelveCount || cfg.getHeroLevel() > hero.level) {
			return;
		}
		
		HeroBook heroBook = heroBookDao.get(actorId);
		ChainLock lock = LockUtils.getLock(heroBook);
		try {
			lock.lock();
			if (heroBook.historyHeroIds.contains(heroId)) {
				return;
			}
			heroBook.historyHeroIds.add(heroId);
		} catch (Exception e) {
			LOGGER.error("{}", e);
		} finally {
			lock.unlock();
		}
		heroBookDao.update(heroBook);
	}
	

}

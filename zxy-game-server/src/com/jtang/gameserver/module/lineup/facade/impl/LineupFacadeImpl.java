package com.jtang.gameserver.module.lineup.facade.impl;

import static com.jiatang.common.GameStatusCodeConstant.ACTIVE_GRID_RAN_OUT;
import static com.jiatang.common.GameStatusCodeConstant.EQUIP_LEVEL_NOT_ENOUGH;
import static com.jiatang.common.GameStatusCodeConstant.EQUIP_NOT_EXITS_IN_GRID;
import static com.jiatang.common.GameStatusCodeConstant.EQUIP_NOT_FOUND;
import static com.jiatang.common.GameStatusCodeConstant.EQUIP_TYPE_INVALID;
import static com.jiatang.common.GameStatusCodeConstant.GRID_NOT_EXISTS;
import static com.jiatang.common.GameStatusCodeConstant.HERO_ALREADY_IN_LINEUP;
import static com.jiatang.common.GameStatusCodeConstant.HERO_NOT_EXITS;
import static com.jiatang.common.GameStatusCodeConstant.HERO_NOT_IN_LINEUP;
import static com.jiatang.common.GameStatusCodeConstant.LEVEL_NOT_REACH;
import static com.jiatang.common.GameStatusCodeConstant.POWER_NO_HERO_IN_LINEUP;
import static com.jtang.core.protocol.StatusCode.OPERATION_ERROR;
import static com.jtang.core.protocol.StatusCode.TICKET_NOT_ENOUGH;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.jiatang.common.GameStatusCodeConstant;
import com.jiatang.common.model.BufferVO;
import com.jiatang.common.model.EquipVO;
import com.jiatang.common.model.HeroVO;
import com.jiatang.common.model.HeroVOAttributeKey;
import com.jtang.core.event.AbstractReceiver;
import com.jtang.core.event.Event;
import com.jtang.core.event.EventBus;
import com.jtang.core.lock.ChainLock;
import com.jtang.core.lock.LockUtils;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.component.event.ActorLevelUpEvent;
import com.jtang.gameserver.component.event.EventKey;
import com.jtang.gameserver.component.event.LineupUnlockEvent;
import com.jtang.gameserver.component.listener.ActorLoginListener;
import com.jtang.gameserver.component.oss.GameOssLogger;
import com.jtang.gameserver.dataconfig.model.EquipConfig;
import com.jtang.gameserver.dataconfig.model.LineupUnlockConfig;
import com.jtang.gameserver.dataconfig.service.EquipService;
import com.jtang.gameserver.dataconfig.service.LineupService;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.dbproxy.entity.Lineup;
import com.jtang.gameserver.module.buffer.facade.BufferFacade;
import com.jtang.gameserver.module.buffer.model.HeroBuffer;
import com.jtang.gameserver.module.equip.facade.EquipFacade;
import com.jtang.gameserver.module.hero.facade.HeroFacade;
import com.jtang.gameserver.module.hero.handler.response.HeroResponse;
import com.jtang.gameserver.module.hero.helper.HeroPushHelper;
import com.jtang.gameserver.module.lineup.constant.LineupRule;
import com.jtang.gameserver.module.lineup.dao.LineupDao;
import com.jtang.gameserver.module.lineup.facade.LineupFacade;
import com.jtang.gameserver.module.lineup.handler.response.ViewLineupResponse;
import com.jtang.gameserver.module.lineup.helper.LineupHelper;
import com.jtang.gameserver.module.lineup.helper.LineupPushHelper;
import com.jtang.gameserver.module.lineup.model.LineupHeadItem;
import com.jtang.gameserver.module.lineup.model.LineupHero;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.facade.VipFacade;
import com.jtang.gameserver.module.user.type.TicketDecreaseType;

/**
 * 阵型模块对外接口
 * @author vinceruan
 *
 */
@Component
public class LineupFacadeImpl extends AbstractReceiver implements LineupFacade, ActorLoginListener {
	@Autowired
	LineupDao lineupDao;
	
	@Autowired
	HeroFacade heroFacade;
	
	@Autowired
	EquipFacade equipFacade;
	
	@Autowired
	LineupHelper lineupHelper;
	
	@Autowired
	ActorFacade actorFacade;
	
	@Autowired
	EventBus eventBus;
	
	@Autowired
	BufferFacade bufferFacade;
	
	@Autowired
	private VipFacade vipFacade;

	@Override
	public Lineup getLineup(long actorId) {
		Lineup lineup = this.lineupDao.getLineup(actorId);
		return lineup;
	}

	@Override
	public Result assignHero(long actorId, int heroId, int headIndex, int gridIndex) {
		HeroVO hero = heroFacade.getHero(actorId, heroId);
		//没有该仙人
		if (hero == null) {
			return Result.valueOf(HERO_NOT_EXITS);
		}
		Lineup lineup = getLineup(actorId);
		ChainLock lock = LockUtils.getLock(lineup);
		try {
			lock.lock();
			//重复上阵
			for (LineupHeadItem item : lineup.getHeadItemList()) {
				if (item.heroId == heroId) {
					return Result.valueOf(HERO_ALREADY_IN_LINEUP);
				}
			}

			//格子超出范围
			if (headIndex > LineupRule.MAX_GRID_COUNT || gridIndex > LineupRule.MAX_GRID_COUNT) {
				return Result.valueOf(GRID_NOT_EXISTS);
			}
			
			//取出顶部格子LineupHeadItem和底部格子LineupGrid
			LineupHeadItem headItem = lineup.getHeadItemList()[headIndex - 1];
			Assert.isTrue(headItem.headIndex == headIndex);
			
			//已激活格子已经用完
			if (headItem.heroId == 0 && lineup.useGridNum() >= lineup.activedGridCount) {
				return Result.valueOf(ACTIVE_GRID_RAN_OUT);
			}
			
			//如果格子上面已有仙人,则将其替换
			int replaceHeroId = headItem.heroId;
			
			//校验完毕,上阵, 更新阵型信息
			lineup.assignHero(heroId, headIndex, gridIndex);
			this.lineupDao.updateLineup(lineup);
//			updateLineupFirstHeroId(lineup);
			
			//更新仙人的激活技能和buffer
			Set<Integer> affectHeros = LineupHelper.getInstance().updateLineupBuffer(lineup);
			
			//如果有仙人下阵,则去除其身上的buffer和技能
			if (replaceHeroId > 0) {
				LineupHelper.getInstance().removeLineupBuffer(actorId, replaceHeroId);
				affectHeros.add(replaceHeroId);
			}
			
			//推送仙人技能、buffer更新
			pushHeroSkillAndBuffers(actorId, affectHeros);
			
			//OSS日志
			List<Integer> heroIdList = new ArrayList<> ();
			for (LineupHeadItem item : lineup.getHeadItemList()) {
				heroIdList.add(item.heroId);
			}
					
			return Result.valueOf();
		} catch (Exception e) {
			LOGGER.error("{}", e);
			return Result.valueOf(OPERATION_ERROR);
		} finally {
			lock.unlock();
		}
		
		
	}

	/**
	 * 处理技能和buffer的推送
	 * @param actorId
	 * @param result
	 */
	private void pushHeroSkillAndBuffers(long actorId, Set<Integer> affectHeros) {
		Map<Integer, List<BufferVO>> buffers = new HashMap<>();
		for (int hid : affectHeros) {
			HeroPushHelper.pushUpdateHero(actorId, hid, HeroVOAttributeKey.PASSIVE_SKILL);
			List<BufferVO> list = bufferFacade.getBufferList(actorId, hid);
			buffers.put(hid, list);
		}
		HeroPushHelper.pushHeroBuffers(actorId, buffers);
	}
	
	/**
	 * 处理技能和buffer的推送
	 * @param actorId
	 * @param result
	 */
	private void pushHeroSkillAndBuffers(long actorId, Integer heroId) {
		HeroPushHelper.pushUpdateHero(actorId, heroId, HeroVOAttributeKey.PASSIVE_SKILL);
		List<BufferVO> list = bufferFacade.getBufferList(actorId, heroId);
		HeroPushHelper.pushHeroBuffers(actorId, heroId, list);
	}

	@Override
	public Result assignEquip(long actorId, long equipUuid, int headIndex) {
		EquipVO equip = equipFacade.get(actorId,equipUuid);		
		if (equip == null) {
			return Result.valueOf(EQUIP_NOT_FOUND);
		}
		EquipConfig config = EquipService.get(equip.equipId);
		Actor actor  = actorFacade.getActor(actorId);
		if(actor.level < config.needLevel){
			return Result.valueOf(EQUIP_LEVEL_NOT_ENOUGH);
		}
		
		int index = isEquipInLineup(actorId, equipUuid);
		if (index != 0 ) {
			if (index != headIndex) {
				return Result.valueOf(GameStatusCodeConstant.EQUIP_EXITS_IN_GRID);
			} else {
				return Result.valueOf();
			}
		}
		
		Lineup lineup = getLineup(actorId);
		ChainLock lock = LockUtils.getLock(lineup);
		try {
			lock.lock();
			LineupHeadItem headItem = lineup.getHeadItemList()[headIndex - 1];
			
			switch (config.getType()) {
			case 1: //武器			 
				headItem.atkEquipUuid = equipUuid;
				break;
			case 2: //防具			
				headItem.defEquipUuid = equipUuid;
				break;
			case 3: //饰品			
				headItem.decorationUuid = equipUuid;
				break;
			default:
				return Result.valueOf(EQUIP_TYPE_INVALID);
			}
			
			this.lineupDao.updateLineup(lineup);
			//更新装备引起的buffer变动
			if (headItem.heroId != 0) {
				LineupHelper.getInstance().updateLineupBuffer4SingleHero(lineup, headItem);
				pushHeroSkillAndBuffers(actorId, headItem.heroId);
			}
			
			return Result.valueOf();
		} catch (Exception e) {
			LOGGER.error("{}", e);
			return Result.valueOf(OPERATION_ERROR);
		} finally {
			lock.unlock();
		}
				
	}

	@Override
	public Result unassignHero(long actorId, int heroId) {
		HeroVO hero = heroFacade.getHero(actorId, heroId);
		//没有该仙人
		if (hero == null) {
			return Result.valueOf(HERO_NOT_EXITS);
		}
		Lineup lineup = getLineup(actorId);
		ChainLock lock = LockUtils.getLock(lineup);
		try {
			lock.lock();
			//最后一个仙人不能下阵
			if(lineup.useGridNum() == 1) {
				return Result.valueOf(GameStatusCodeConstant.LINEUP_SHOULD_NOT_EMPTY);
			}
			
			//查找仙人在3*3格子中的位置
			LineupHeadItem item = null;
			for (LineupHeadItem l : lineup.getHeadItemList()) {
				if (l.heroId == heroId) {
					item = l;
					break;
				}
			}
			
			if (item == null) {
				return Result.valueOf(HERO_NOT_IN_LINEUP);
			}
			
			//将仙人从阵型中脱离
			lineup.unAssignHero(heroId, item.headIndex);
			
			//更新阵型信息
			this.lineupDao.updateLineup(lineup);
//			updateLineupFirstHeroId(lineup);
			
			//处理下阵仙人身上的buffer和技能
			LineupHelper.getInstance().removeLineupBuffer(actorId, heroId);
			pushHeroSkillAndBuffers(actorId, heroId);
			
			//处理上阵所有仙人的buffer和技能
			Set<Integer> affectHeros = LineupHelper.getInstance().updateLineupBuffer(lineup);
			pushHeroSkillAndBuffers(actorId, affectHeros);
			
			return Result.valueOf();
		} catch (Exception e) {
			LOGGER.error("{}", e);
			return Result.valueOf(OPERATION_ERROR);
		} finally {
			lock.unlock();
		}
		
	}

	@Override
	public Result unassignEquip(long actorId, long equivId, boolean isPush2Client) {
		EquipVO equip = equipFacade.get(actorId,equivId);		
		if (equip == null) {
			return Result.valueOf(EQUIP_NOT_FOUND);
		}
		
		Lineup lineup = getLineup(actorId);
		ChainLock lock = LockUtils.getLock(lineup);
		try {
			lock.lock();
			LineupHeadItem headItem = null;
			for (LineupHeadItem item : lineup.getHeadItemList()) {
				if (equivId == item.atkEquipUuid) {
					headItem = item;
					item.atkEquipUuid = 0;
				} else if (equivId == item.defEquipUuid) {
					headItem = item;
					item.defEquipUuid = 0;
				} else if (equivId == item.decorationUuid) {
					headItem = item;
					item.decorationUuid = 0;
				}
			}
			
			//装备没有找到
			if (headItem == null) {
				return Result.valueOf(EQUIP_NOT_EXITS_IN_GRID);
			}
			
			//更新阵型信息
//		lineup.updateLineupStr();
			lineupDao.updateLineup(lineup);
			
			//如果装备对应的格子上面有仙人,则更新仙人的buffer信息
			int heroId = -1;
			if (headItem.gridIndex > 0){
				heroId = headItem.heroId;
				LineupHelper.getInstance().updateLineupBuffer4SingleHero(lineup, headItem);
			}
			
			if (isPush2Client && heroId > 0) {
				pushHeroSkillAndBuffers(actorId, heroId);
			}
			return Result.valueOf();
		} catch (Exception e) {
			LOGGER.error("{}", e);
			return Result.valueOf(OPERATION_ERROR);
		} finally {
			lock.unlock();
		}
		
	}

	@Override
	public Result changeHeroGrid(long actorId, int heroId, int gridIndex) {
		Lineup lineup = this.getLineup(actorId);
		ChainLock lock = LockUtils.getLock(lineup);
		try {
			lock.lock();
			//查找仙人的位置
			LineupHeadItem item = null;
			for (LineupHeadItem l : lineup.getHeadItemList()) {
				if (l.heroId == heroId) {
					item = l;
					break;
				}
			}
			
			//仙人没有上阵
			if (item == null) {
				return Result.valueOf(HERO_NOT_IN_LINEUP);
			}
			
			//格子超出范围
			if (gridIndex > LineupRule.MAX_GRID_COUNT) {
				return Result.valueOf(GRID_NOT_EXISTS);
			}
			
			LineupHeadItem item2 = lineup.getHeadItemByGridIndex(gridIndex);
			if (item2 == null) {
				return Result.valueOf(GRID_NOT_EXISTS);
			}
			item2.gridIndex = item.gridIndex;
			item.gridIndex = gridIndex;
			
			this.lineupDao.updateLineup(lineup);
//			updateLineupFirstHeroId(lineup);
			
			//更新buffer信息
			Set<Integer> result = LineupHelper.getInstance().updateLineupBuffer(lineup);
			pushHeroSkillAndBuffers(actorId, result);
			
			return Result.valueOf();
		} catch (Exception e) {
			LOGGER.error("{}", e);
			return Result.valueOf(OPERATION_ERROR);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public Result exChangeHeroGrid(long actorId, int heroId1, int heroId2) {
		Lineup lineup = this.getLineup(actorId);
		ChainLock lock = LockUtils.getLock(lineup);
		try {
			lock.lock();
			//查找仙人在阵型中的位置
			LineupHeadItem item1 = null;
			LineupHeadItem item2 = null;
			for (LineupHeadItem l : lineup.getHeadItemList()) {
				if (l.heroId == heroId1) {
					item1 = l;
				} else if (l.heroId == heroId2) {
					item2 = l;
				}
			}
			
			//仙人没有上阵
			if (item1 == null || item2 == null) {
				return Result.valueOf(HERO_NOT_IN_LINEUP);
			}
			
			//交换两个人的位置
			int temp = item1.gridIndex;
			item1.gridIndex = item2.gridIndex;
			item2.gridIndex = temp;
			
			//更新阵型信息
//		lineup.updateLineupStr();
			this.lineupDao.updateLineup(lineup);
//			updateLineupFirstHeroId(lineup);
			
			//更新buffer信息,推送消息告诉客户端
			Set<Integer> result = LineupHelper.getInstance().updateLineupBuffer(lineup);
			pushHeroSkillAndBuffers(actorId, result);
			
			return Result.valueOf();
		} catch (Exception e) {
			LOGGER.error("{}", e);
			return Result.valueOf(OPERATION_ERROR);
		} finally {
			lock.unlock();
		}
	}
	
//	public void updateLineupFirstHeroId(Lineup lineup) {
//		List<Integer> list = new ArrayList<>();
//		for (LineupHeadItem item : lineup.getHeadItemList()) {
//			if(item.heroId > 0){
//				list.add(item.gridIndex);
//			}
//		}
//		Collections.sort(list);
//		for(LineupHeadItem item : lineup.getHeadItemList()){
//			if(item.gridIndex == list.get(0)){
//				this.actorFacade.updateLineupFirstHeroId(lineup.getPkId(), item.heroId);
//				return;
//			}
//		}
//	}

	/**
	 * 解锁格子
	 * @param actorId
	 * @param auto      是否自动		
	 * @return
	 */
	public Result unlockLineup(long actorId, boolean auto) {
		Lineup lineup = this.getLineup(actorId);
		int count = lineup.activedGridCount;
		int toUnlock = count + 1;
		LineupUnlockConfig config = LineupService.get(toUnlock);
		if (config == null) {
			return Result.valueOf(GameStatusCodeConstant.LINEUP_UNLOCK_CONFIG_MISS);
		}
		
		//如果是需要手工解锁的,则不允许自动解锁,所以直接返回成功但是不解锁格子
		if (config.isAutoUnlock() == false && auto) {
			return Result.valueOf();
		}
		
		int level = config.getNeedActorLevel();
		int tick = config.getNeedTick();
		Actor actor = this.actorFacade.getActor(actorId);
		
		if (level > 0) {
			if (actor.level < level) {
				return Result.valueOf(LEVEL_NOT_REACH);
			}
		}
		
		if (tick > 0) {	
			boolean decreaseResult = this.vipFacade.decreaseTicket(actorId, TicketDecreaseType.LINEUP_UNLOCK, tick, 0, 0);
			if (decreaseResult == false){
				return Result.valueOf(TICKET_NOT_ENOUGH);
			}
		}
		
		lineup.activedGridCount = toUnlock;
		
		LineupPushHelper.pushLineupUnlock(actorId, toUnlock);
		
		this.lineupDao.updateLineup(lineup);
		
		eventBus.post(new LineupUnlockEvent(actorId, toUnlock));

		GameOssLogger.lineupGridUnlock(actor.uid, actor.platformType, actor.channelId, actor.serverId, actorId, toUnlock, auto);
		
		return Result.valueOf();
	}

	@Override
	public Result manualUnlockLineup(long actorId) {
		return this.unlockLineup(actorId, false);
	}

	public void autoUnlockLineup(long actorId) {
		this.unlockLineup(actorId, true);
	}

	@Override
	public String[] getEventNames() {
		return new String[]{EventKey.ACTOR_LEVEL_UP};
	}

	@Override
	public void onEvent(Event paramT) {
		if (paramT.name.equalsIgnoreCase(EventKey.ACTOR_LEVEL_UP)) {
			ActorLevelUpEvent event = paramT.convert();
			for (int i = event.oldLevel; i < event.actor.level; i++) {
				this.autoUnlockLineup(event.actor.getPkId());
			}
		}
	}

	@Override
	public List<LineupHero> getLineupHeros(long actorId) {
		Lineup lineup = this.getLineup(actorId);
		
		Map<Integer, Integer> indexMap = new HashMap<Integer, Integer>();			
		for (LineupHeadItem index : lineup.getHeadItemList()) {
			if (index.heroId > 0) {
				indexMap.put(index.gridIndex, index.heroId);
			}
		}
		
		List<LineupHero> list = new ArrayList<>();	
		for (LineupHeadItem item : lineup.getHeadItemList()) {
			if (item.gridIndex > 0) {				
				LineupHero lineupHero = new LineupHero(item);
				list.add(lineupHero);
			}
		}
		
		return list;
	}

	@Override
	public boolean isHeroInLineup(long actorId, int heroId) {
		Lineup lineup = this.getLineup(actorId);
		for (LineupHeadItem item : lineup.getHeadItemList()) {
			if (item.heroId == heroId) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int isEquipInLineup(long actorId, long equipUuid) {
		Lineup lineup = getLineup(actorId);
		for (LineupHeadItem item : lineup.getHeadItemList()) {
			if (equipUuid == item.atkEquipUuid) {
				return item.headIndex;
			}
			if (equipUuid == item.defEquipUuid) {
				return item.headIndex;
			}
			if (equipUuid == item.decorationUuid) {
				return item.headIndex;
			}
		}
		return 0;
	}

	@Override
	public void onLogin(long actorId) {
		Actor actor = actorFacade.getActor(actorId);
		Lineup lineup = this.lineupDao.getLineup(actorId);
		int num = LineupService.gridNumByLevel(actor.level);
		if (lineup.activedGridCount < num) {
			lineup.activedGridCount = num;
			this.lineupDao.updateLineup(lineup);
		}
	}
	
	@Override
	public TResult<ViewLineupResponse> getLineupInfo(long actorId) {
		List<LineupHero> lineupHeros = getLineupHeros(actorId);
		if (lineupHeros == null || lineupHeros.size() == 0) {
			return TResult.valueOf(POWER_NO_HERO_IN_LINEUP);
		}
		Map<Integer, List<EquipVO>> gridEquipMap = new HashMap<>();// 阵型中的装备
		Map<Integer, HeroResponse> lineupHerosResp = new HashMap<>();
		for (LineupHero lineupHero : lineupHeros) {
			HeroVO hero = heroFacade.getHero(actorId, lineupHero.heroId);
			if (hero == null) {
				continue;
			}
			// 获取英雄buffer
			List<BufferVO> buffers = new ArrayList<BufferVO>();
			HeroBuffer heroBuffer = bufferFacade.getHeroBuffer(actorId, lineupHero.heroId);
			if (heroBuffer != null) {
				for (List<BufferVO> bl : heroBuffer.bufferTypeMap.values()) {
					buffers.addAll(bl);
				}
			}
			lineupHerosResp.put(lineupHero.index, HeroResponse.valueOf(hero, buffers));
			// 获取该阵型位置装备
			List<EquipVO> equips = new ArrayList<EquipVO>();// 被查看装备
			for (long equipUuid : lineupHero.equipList) {
				if (equipUuid == 0) {
					continue;
				}
				EquipVO equip = equipFacade.get(actorId, equipUuid);
				if (equip == null) {
					continue;
				}
				equips.add(equip);
			}
			gridEquipMap.put(lineupHero.index, equips);
		}
		return TResult.sucess(new ViewLineupResponse(lineupHerosResp, gridEquipMap));
	}

	@Override
	public TResult<ViewLineupResponse> changeLineup(long actorId,
			int lineupIndex) {
		Lineup lineup = getLineup(actorId);
		lineup.setCurrentIndex(lineupIndex);
		lineupDao.updateLineup(lineup);
		return getLineupInfo(actorId);
	}

	@Override
	public int getFirstHero(long actorId) {
		Lineup lineup = getLineup(actorId);
		return lineup.getFirstHeroId();
	}
}

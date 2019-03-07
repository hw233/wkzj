package com.jtang.gameserver.module.icon.facade.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.GameStatusCodeConstant;
import com.jiatang.common.model.HeroVO;
import com.jtang.core.event.Event;
import com.jtang.core.event.EventBus;
import com.jtang.core.event.Receiver;
import com.jtang.core.lock.ChainLock;
import com.jtang.core.lock.LockUtils;
import com.jtang.core.protocol.StatusCode;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.core.rhino.FormulaHelper;
import com.jtang.core.utility.DateUtils;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.component.event.AddHeroEvent;
import com.jtang.gameserver.component.event.ContinueLoginEvent;
import com.jtang.gameserver.component.event.EventKey;
import com.jtang.gameserver.component.event.HeroDelveEvent;
import com.jtang.gameserver.component.event.PowerRankChangeEvent;
import com.jtang.gameserver.component.event.VipLevelChangeEvent;
import com.jtang.gameserver.dataconfig.model.HeroConfig;
import com.jtang.gameserver.dataconfig.model.IconConfig;
import com.jtang.gameserver.dataconfig.model.IconFramConfig;
import com.jtang.gameserver.dataconfig.service.HeroService;
import com.jtang.gameserver.dataconfig.service.IconService;
import com.jtang.gameserver.dbproxy.entity.Icon;
import com.jtang.gameserver.dbproxy.entity.Love;
import com.jtang.gameserver.module.hero.facade.HeroFacade;
import com.jtang.gameserver.module.icon.dao.IconDao;
import com.jtang.gameserver.module.icon.facade.IconFacade;
import com.jtang.gameserver.module.icon.hander.response.IconResponse;
import com.jtang.gameserver.module.icon.helper.IconPushHelper;
import com.jtang.gameserver.module.icon.model.IconVO;
import com.jtang.gameserver.module.icon.type.IconFramType;
import com.jtang.gameserver.module.icon.type.IconType;
import com.jtang.gameserver.module.ladder.facade.impl.LadderFacadeImpl;
import com.jtang.gameserver.module.love.dao.LoveDao;
import com.jtang.gameserver.module.user.constant.ActorRule;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.facade.VipFacade;
import com.jtang.gameserver.module.user.helper.ActorPushHelper;
import com.jtang.gameserver.module.user.type.TicketDecreaseType;

@Component
public class IconFacadeImpl implements IconFacade,Receiver {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LadderFacadeImpl.class);
	
	@Autowired
	IconDao iconDao;
	@Autowired
	HeroFacade heroFacade;
	@Autowired
	EventBus eventBus;
	@Autowired
	private VipFacade vipFacade;
	@Autowired
	private ActorFacade actorFacade;
	@Autowired
	private LoveDao loveDao;
	@PostConstruct
	public void init() {
		eventBus.register(EventKey.POWER_RANK_CHANGE, this);
		eventBus.register(EventKey.HERO_DELVE, this);
		eventBus.register(EventKey.VIP_LEVEL_CHANGE, this);
		eventBus.register(EventKey.ADD_HERO, this);
		eventBus.register(EventKey.CONTINUE_LOGIN, this);
	}

	@Override
	public TResult<IconResponse> getIconInfo(long actorId) {
		Icon icon = iconDao.get(actorId);
		List<Integer> heroIds = icon.getHeroId();
		List<Integer> framIds = icon.getFram();
		IconResponse response = new IconResponse(heroIds,framIds,icon.icon,icon.fram, icon.sex);
		return TResult.sucess(response);
	}

	@Override
	public void onEvent(Event paramEvent) {
		if(paramEvent.name == EventKey.POWER_RANK_CHANGE){
			PowerRankChangeEvent event = paramEvent.convert();
			unLockFram(event.actorId, IconFramType.POWER, event.newRank);
		}else if(paramEvent.name == EventKey.HERO_DELVE){
			HeroDelveEvent event = paramEvent.convert();
			unLockIcon(event.actorId, IconType.DELVE, event.getHeroId());
		}else if(paramEvent.name == EventKey.VIP_LEVEL_CHANGE){
			VipLevelChangeEvent event = paramEvent.convert();
			unLockFram(event.actorId, IconFramType.VIP, event.vipLevel);
		}else if(paramEvent.name == EventKey.CONTINUE_LOGIN){
			ContinueLoginEvent event = paramEvent.convert();
			unLockFram(event.actorId, IconFramType.LOGIN,0);
		}else if(paramEvent.name == EventKey.ADD_HERO){
			AddHeroEvent event = paramEvent.convert();
			unLockIcon(event.actorId, IconType.ADD, event.heroId);
		}
	}

	private void unLockFram(long actorId,IconFramType type,int value){
		Icon icon = iconDao.get(actorId);
		List<IconFramConfig> framConfig = new ArrayList<>();
		for(IconFramConfig config : IconService.getAllIconFram()){//取出还未解锁的边框 
			if(icon.getFram().contains(config.framId) == false){
				framConfig.add(config);
			}
		}
		if(framConfig.size() == 0){//全部已经解锁
			return;
		}
		List<Integer> list = new ArrayList<>();
		for(IconFramConfig config : framConfig){
			if(config.framType == type.getCode() && type == IconFramType.POWER){//排行榜边框解锁处理
				List<Integer> ranks = StringUtils.delimiterString2IntList(config.unLock, Splitable.ATTRIBUTE_SPLIT);
				if(value <= ranks.get(1)){
					icon.getFram().add(config.framId);
					iconDao.update(icon);
					list.add(config.framId);
				}
			}
			
			if(config.framType == type.getCode() && type == IconFramType.VIP){//VIP边框解锁处理
				List<Integer> vips = StringUtils.delimiterString2IntList(config.unLock, Splitable.ATTRIBUTE_SPLIT);
				if(vips.get(0) <= value && value <= vips.get(1)){
					icon.getFram().add(config.framId);
					iconDao.update(icon);
					list.add(config.framId);
				}
			}
			
			if(config.framType == type.getCode() && type == IconFramType.LOGIN){//登陆即解锁
				int now = TimeUtils.getNow();
				List<String> times = StringUtils.delimiterString2List(config.unLock, Splitable.ATTRIBUTE_SPLIT);
				Date dateStart = DateUtils.string2Date(times.get(0), "yyyy-MM-dd HH:mm:ss");
				Long start = dateStart.getTime() / 1000;
				int startTime = start.intValue();
				Date dateEnd = DateUtils.string2Date(times.get(1), "yyyy-MM-dd HH:mm:ss");
				Long end = dateEnd.getTime() / 1000;
				int endTime = end.intValue();
				if(startTime <= now && now <= endTime){
					icon.getFram().add(config.framId);
					iconDao.update(icon);
					list.add(config.framId);
				}
			}
			IconPushHelper.pushUnLockFram(actorId,list);
		}
	}
	
	@Override
	public void unLock(long actorId,int heroId){
		Icon icon = iconDao.get(actorId);
		ChainLock lock = LockUtils.getLock(icon);
		try{
			lock.lock();
			HeroConfig heroConfig = HeroService.get(heroId);
			int star = 0;
			if(heroConfig != null){
				star = heroConfig.getStar();
			}
			List<Integer> heroIds = icon.getHeroId(star);
			if(heroIds.contains(heroId)){//已经解锁了这个仙人
				return;
			}
			icon.unLockHero(star,heroId);
			iconDao.update(icon);
			IconPushHelper.pushUnLockIcon(actorId, heroId);
		}catch(Exception e){
			LOGGER.error("{}",e);
		}finally{
			lock.unlock();
		}
	}
	
	private void unLockIcon(long actorId, IconType iconType, int heroId){
		int star = HeroService.get(heroId).getStar();
		Icon icon = iconDao.get(actorId);
		List<Integer> heroIds = icon.getHeroId(star);
		if(heroIds.contains(heroId)){//已经解锁了这个仙人
			return;
		}
		HeroVO heroVO = heroFacade.getHero(actorId, heroId);
		List<IconConfig> iconConfigs = IconService.getIcon((byte) star);
		for(IconConfig config : iconConfigs){
			if(iconType.getCode() == config.unLockType && iconType == IconType.DELVE){//潜修解锁
				if(heroVO.usedDelveCount >= Integer.valueOf(config.unLockNum) && config.heroId == heroId){//符合条件
					icon.unLockHero(star,heroId);
					iconDao.update(icon);
					IconPushHelper.pushUnLockIcon(actorId, heroId);
				}
			}
			
			if(iconType.getCode() == config.unLockType && iconType == IconType.ADD){//获取即解锁
				if(heroId == config.heroId){
					icon.unLockHero(star,heroId);
					iconDao.update(icon);
					IconPushHelper.pushUnLockIcon(actorId, heroId);
				}
			}
		}
		
	}

	@Override
	public Result setIcon(long actorId,int icon) {
		Icon actorIcon = iconDao.get(actorId);
		HeroConfig heroConfig = HeroService.get(icon);
		int star = 0;
		if(heroConfig != null){
			star = heroConfig.getStar();
		}
		if(actorIcon.getHeroId(star).contains(icon) == false){
			return Result.valueOf(GameStatusCodeConstant.ICON_UNLOCK);
		}
		actorIcon.icon = icon;
		iconDao.update(actorIcon);
		return Result.valueOf();
	}

	@Override
	public Result setFram(long actorId,int fram) {
		Icon actorIcon = iconDao.get(actorId);
		if(actorIcon.getFram().contains(fram) == false){
			return Result.valueOf(GameStatusCodeConstant.FRAM_UNLOCK);
		}
		actorIcon.fram = fram;
		iconDao.update(actorIcon);
		return Result.valueOf();
	}

	@Override
	public IconVO getIconVO(long actorId) {
		Icon icon = iconDao.get(actorId);
		IconVO iconVO = new IconVO(icon.icon,icon.fram, icon.sex);
		return iconVO;
	}

	@Override
	public IconVO getRobotIconVO() {
		IconVO iconVO = IconService.randomIconVO();
		return iconVO;
	}
	
	@Override
	public Result setSex(long actorId, byte sex) {
		Love love = loveDao.get(actorId);
		if(love.married()){
			return Result.valueOf(GameStatusCodeConstant.MARRIED_NOT_SET_SEX);
		}
		if (sex > 1 || sex < 0) {
			return Result.valueOf(StatusCode.DATA_VALUE_ERROR);
		}
		Icon actorIcon = iconDao.get(actorId);
		if (sex == actorIcon.sex) {
			return Result.valueOf(StatusCode.DATA_VALUE_ERROR);
		}
		
		if(actorIcon.modifySexNum > 0){//首次修改名称免费
			int costTicket = FormulaHelper.executeCeilInt(ActorRule.RESEX_COST_TICKET, actorIcon.modifySexNum);
			boolean isOk = vipFacade.decreaseTicket(actorId, TicketDecreaseType.ACTOR_RENAME, costTicket, 0, 0);
			if(isOk == false){
				return Result.valueOf(StatusCode.TICKET_NOT_ENOUGH);
			}
		}
		
		actorIcon.sex = sex;
		actorIcon.modifySexNum += 1;
		iconDao.update(actorIcon);
		ActorPushHelper.pushActorBuyInfo(actorId,actorFacade.getActorBuy(actorId).item);
		return Result.valueOf();
	}

	@Override
	public boolean marryUnLock(long actorId) {
		Love love = loveDao.get(actorId);
		Icon icon = iconDao.get(actorId);
		ChainLock lock = LockUtils.getLock(icon,love);
		try{
			lock.lock();
			if(love.married() == false){
				return false;
			}
			int heroId = 0;
			if(icon.sex == 0){//性别不同,解锁头像不同
				heroId = 50002;
			}else{
				heroId = 50001;
			}
			HeroConfig heroConfig = HeroService.get(heroId);
			int star = 0;
			if(heroConfig != null){
				star = heroConfig.getStar();
			}
			List<Integer> heroIds = icon.getHeroId(star);
			if(heroIds.contains(heroId)){//已经解锁了这个仙人
				return false;
			}
			icon.unLockHero(star,heroId);
			iconDao.update(icon);
			IconPushHelper.pushUnLockIcon(actorId, heroId);
		}catch(Exception e){
			LOGGER.error("{}",e);
		}finally{
			lock.unlock();
		}
		return true;
	}

	@Override
	public boolean marryLock(long actorId) {
		Love love = loveDao.get(actorId);
		Icon icon = iconDao.get(actorId);
		ChainLock lock = LockUtils.getLock(icon,love);
		try{
			lock.lock();
			if(love.married()){
				return false;
			}
			Integer heroId = 0;
			if(icon.sex == 0){//性别不同,解锁头像不同
				heroId = 50002;
			}else{
				heroId = 50001;
			}
			HeroConfig heroConfig = HeroService.get(heroId);
			int star = 0;
			if(heroConfig != null){
				star = heroConfig.getStar();
			}
			List<Integer> heroIds = icon.getHeroId(star);
			if(heroIds.contains(heroId) == false){//还没解锁这个仙人
				return false;
			}
			heroIds.remove(heroId);
			icon.icon = icon.getHeroId().get(0);//锁定某些头像,防止他还在使用,还原现在头像为默认头像
			iconDao.update(icon);
			IconPushHelper.pushLockIcon(actorId, heroId);
		}catch(Exception e){
			LOGGER.error("{}",e);
		}finally{
			lock.unlock();
		}
		return true;
	}
}

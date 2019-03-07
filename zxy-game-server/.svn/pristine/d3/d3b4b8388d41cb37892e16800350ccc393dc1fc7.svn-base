package com.jtang.gameserver.module.app.effect;

import static com.jiatang.common.GameStatusCodeConstant.ACTOR_ID_ERROR;
import static com.jiatang.common.GameStatusCodeConstant.APP_CLOSED;
import static com.jiatang.common.GameStatusCodeConstant.APP_NOT_EXSIT;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.event.EventBus;
import com.jtang.core.event.GameEvent;
import com.jtang.core.model.RewardObject;
import com.jtang.core.result.ListResult;
import com.jtang.core.result.Result;
import com.jtang.core.schedule.Schedule;
import com.jtang.core.utility.DateUtils;
import com.jtang.gameserver.dataconfig.model.AppRuleConfig;
import com.jtang.gameserver.dataconfig.service.AppRuleService;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.dbproxy.entity.AppGlobal;
import com.jtang.gameserver.dbproxy.entity.AppRecord;
import com.jtang.gameserver.module.app.dao.AppGlobalDao;
import com.jtang.gameserver.module.app.dao.AppRecordDao;
import com.jtang.gameserver.module.app.model.AppGlobalVO;
import com.jtang.gameserver.module.app.model.AppRecordVO;
import com.jtang.gameserver.module.app.type.EffectId;
import com.jtang.gameserver.module.equip.facade.EquipFacade;
import com.jtang.gameserver.module.equip.type.EquipAddType;
import com.jtang.gameserver.module.goods.facade.GoodsFacade;
import com.jtang.gameserver.module.goods.type.GoodsAddType;
import com.jtang.gameserver.module.hero.facade.HeroSoulFacade;
import com.jtang.gameserver.module.hero.type.HeroSoulAddType;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.facade.VipFacade;
import com.jtang.gameserver.module.user.type.GoldAddType;
import com.jtang.gameserver.server.session.PlayerSession;

/**
 * 此类处理全局的事情，以及通用的方法
 * @author 0x737263
 *
 */
@Component
public abstract class AppParser{
	protected final Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	@Autowired
	protected EquipFacade equipFacade;
	@Autowired
	protected GoodsFacade goodsFacade;
	@Autowired
	protected ActorFacade actorFacade;
	@Autowired
	protected HeroSoulFacade heroSoulFacade;
	@Autowired
	protected AppGlobalDao appGlobalDao;
	@Autowired
	protected AppRecordDao appRecordDao;
	@Autowired
	protected EventBus eventBus;
	@Autowired
	protected PlayerSession playerSession;
	@Autowired
	protected VipFacade vipFacade;
	@Autowired
	protected Schedule schedule;
	
	private static Map<EffectId, AppParser> facades = new HashMap<EffectId, AppParser>();
	
	@PostConstruct
	private void init() {
		facades.put(getEffect(), this);
	}
	
	/**
	 * 获取活动解析器id
	 * @return
	 */
	public abstract EffectId getEffect();
	
	/**
	 * 获取活动业务处理器
	 * @param effectId
	 * @return
	 */
	public static AppParser getAppParser(long appId) {
		AppRuleConfig appRuleConfig = AppRuleService.get(appId);
		EffectId effectId = EffectId.getById(appRuleConfig.getEffect());
		return facades.get(effectId);
	}
	
	/**
	 * 获取活动业务处理器
	 * @param effectId
	 * @return
	 */
	public static AppParser getAppParser(EffectId effectId) {
		return facades.get(effectId);
	}
	
	/**
	 * 活动活动配置规则(appRuleConfig.xml)
	 * @return
	 */
	public AppRuleConfig getAppRuleConfig(long appId) {
		return  AppRuleService.get(appId);
	}
	
	/**
	 * 发放活动奖励
	 * @param actorId
	 * @param rewardGoods
	 */
	protected boolean sendReward(long actorId, List<RewardObject> rewardGoods) {
		if (rewardGoods.isEmpty()) {
			return false;
		}
		boolean flag = true;
		for (RewardObject rewardObject: rewardGoods) {
			boolean result = sendReward(actorId, rewardObject);
			flag = flag && result;
		}
		return flag;
	}
	
	/**
	 * 发放活动奖励
	 * @param actorId
	 * @param rewardGoods
	 * @return
	 */
	protected boolean sendReward(long actorId, RewardObject rewardObject) {
		switch (rewardObject.rewardType) {
		case EQUIP: {
			equipFacade.addEquip(actorId, EquipAddType.APP_REWARD, rewardObject.id);
			break;
		}
		case GOLD: {
			actorFacade.addGold(actorId, GoldAddType.APP_REWARD, rewardObject.num);
			break;
		}
		case HEROSOUL: {
			heroSoulFacade.addSoul(actorId, HeroSoulAddType.APP_REWARD, rewardObject.id, rewardObject.num);
			break;
		}
		case GOODS: {
			goodsFacade.addGoodsVO(actorId, GoodsAddType.APP_REWARD, rewardObject.id, rewardObject.num);
			break;
		}
		default:
			return false;
		}
		return true;
	}

	/**
	 * 判断某个角色是否参加该活动
	 */
	public Result appEnable(long actorId,long appId) {
		AppRuleConfig appRuleConfig = getAppRuleConfig(appId);
		if (appRuleConfig == null) {
			return Result.valueOf(APP_NOT_EXSIT);
		}

		if (DateUtils.isActiveTime(appRuleConfig.getStartTime(), appRuleConfig.getEndTime()) == false) {
			return Result.valueOf(APP_CLOSED);
		}
		
		//清理过期记录
		AppRecord record = appRecordDao.get(actorId, appId);
		if (record.operationTime != 0 && (record.operationTime < appRuleConfig.getStartTime() || record.operationTime > appRuleConfig.getEndTime())) {
			record.reset();
			appRecordDao.update(record);
		}
				
		List<Integer> chanel = appRuleConfig.getChanelIds();
		if (chanel.isEmpty()) {
			return Result.valueOf();
		}
		
		Actor actor = actorFacade.getActor(actorId);
		if (actor == null) {
			return Result.valueOf(ACTOR_ID_ERROR);
		}

		if (chanel.contains(actor.channelId) == false) {
			return Result.valueOf(APP_NOT_EXSIT);
		}
		
		return Result.valueOf();
	}
	
	/**
	 * 全局判断活动是否开启
	 * @return
	 */
	protected boolean appEnable(long appId) {
		AppRuleConfig appRuleConfig = AppRuleService.get(appId);
		if (appRuleConfig == null) {
			return false;
		}

		if (DateUtils.isActiveTime(appRuleConfig.getStartTime(), appRuleConfig.getEndTime()) == false) {
			return false;
		}
		
		//清理过期记录
		AppGlobal gloabalRecord = appGlobalDao.get(appId);
		if (gloabalRecord.operationTime != 0 && (gloabalRecord.operationTime < appRuleConfig.getStartTime() || gloabalRecord.operationTime > appRuleConfig.getEndTime())) {
			gloabalRecord.reset();
			appGlobalDao.update(gloabalRecord);
		}

		
		return true;
	}
	
	/**
	 * 时间调度
	 * @param actorId
	 * @param appId
	 */
	public abstract void onApplicationEvent();
	
	/**
	 * 游戏事件处理
	 * @param paramEvent
	 * @param appId
	 */
	public abstract void onGameEvent(GameEvent paramEvent,long appId);
	
	/**
	 * 获取配置
	 */
	public abstract AppGlobalVO getAppGlobalVO(long actorId,long appId);
	
	/**
	 * 获取活动记录
	 * @param actorId
	 */
	public abstract AppRecordVO getAppRecord(long actorId,long appId);

	/**
	 * 获取活动奖励
	 * @param actorId
	 * @param paramsMaps
	 * @return
	 */
	public abstract ListResult<RewardObject> getReward(long actorId,Map<String, String> paramsMaps,long appId);

	/**
	 * 角色登陆
	 * @param actorId
	 * @param appId
	 */
	public abstract void onActorLogin(long actorId, long appId);
	
}

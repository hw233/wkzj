package com.jtang.gameserver.module.app.effect;

import static com.jiatang.common.GameStatusCodeConstant.APP_LEVEL_NOT_REACH;

import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.GameStatusCodeConstant;
import com.jiatang.common.model.HeroVO;
import com.jtang.core.event.GameEvent;
import com.jtang.core.lock.ChainLock;
import com.jtang.core.lock.LockUtils;
import com.jtang.core.model.RewardObject;
import com.jtang.core.model.RewardType;
import com.jtang.core.protocol.StatusCode;
import com.jtang.core.result.ListResult;
import com.jtang.core.rhino.FormulaHelper;
import com.jtang.core.utility.DateUtils;
import com.jtang.gameserver.dataconfig.model.AppRuleConfig;
import com.jtang.gameserver.dataconfig.service.AppRuleService;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.dbproxy.entity.AppRecord;
import com.jtang.gameserver.module.app.constant.AppRule;
import com.jtang.gameserver.module.app.helper.AppPushHelper;
import com.jtang.gameserver.module.app.model.AppGlobalVO;
import com.jtang.gameserver.module.app.model.AppRecordVO;
import com.jtang.gameserver.module.app.model.extension.record.RecordInfoVO15;
import com.jtang.gameserver.module.app.model.extension.rule.RuleConfigVO15;
import com.jtang.gameserver.module.app.model.extension.rulevo.ExtBuyHeroVO;
import com.jtang.gameserver.module.app.type.AppKey;
import com.jtang.gameserver.module.app.type.EffectId;
import com.jtang.gameserver.module.hero.facade.HeroFacade;
import com.jtang.gameserver.module.hero.type.HeroSoulAddType;
import com.jtang.gameserver.module.user.helper.ActorHelper;
import com.jtang.gameserver.module.user.type.TicketDecreaseType;

@Component
public class AppParser15 extends AppParser { 

	@Autowired
	HeroFacade heroFacade;
	
	@Override
	public EffectId getEffect() {
		return EffectId.EFFECT_ID_15;
	}

	@Override
	public void onApplicationEvent() {
		for (Long actorId : playerSession.onlineActorList()) {
			for(long appId : AppRuleService.getAppId(getEffect().getId())){
				resetValue(actorId,appId);
			}
		}
	}
	
	@Override
	public void onGameEvent(GameEvent paramEvent, long appId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public AppGlobalVO getAppGlobalVO(long actorId, long appId) {
		int level = ActorHelper.getActorLevel(actorId);
		AppRuleConfig appConfig = getAppRuleConfig(appId);
		AppGlobalVO appConfigVO = new AppGlobalVO(appConfig,level);
		return appConfigVO;
	}

	@Override
	public AppRecordVO getAppRecord(long actorId, long appId) {
		RecordInfoVO15 recordInfoVO15 = appRecordDao.getRecordInfoVO(actorId, appId);
		if (recordInfoVO15 == null) {
			recordInfoVO15 = new RecordInfoVO15();
		}
		AppRecordVO appRecordVO = new AppRecordVO(appId, recordInfoVO15.getRecordInfoMaps());
		return appRecordVO;
	}

	@Override
	public ListResult<RewardObject> getReward(long actorId,
			Map<String, String> paramsMaps, long appId) {
		
		Actor actor = actorFacade.getActor(actorId);
		if(actor.level < AppRule.APP_LEVEL_LIMIT){
			return ListResult.statusCode(APP_LEVEL_NOT_REACH);
		}
		
		if(paramsMaps.isEmpty()){
			return ListResult.statusCode(GameStatusCodeConstant.APP_ARGS_ERROR);
		}
		int heroId = Integer.valueOf(paramsMaps.get(AppKey.GOODS_ID.getKey()));
		AppRuleConfig config = getAppRuleConfig(appId);
		RuleConfigVO15 ruleConfigVO15 = config.getConfigRuleVO();
		ExtBuyHeroVO buyHeroVO = null;
		for(Integer key : ruleConfigVO15.map.keySet()){//配置里面是否有选中的仙人
			if(key == heroId){
				buyHeroVO = ruleConfigVO15.map.get(key);
			}
		}
		if(buyHeroVO == null){
			return ListResult.statusCode(GameStatusCodeConstant.APP_ARGS_ERROR);
		}
		
		HeroVO heroVO = null;
		for(HeroVO vo:heroFacade.getList(actorId)){//是否有选中的仙人
			if(vo.heroId == heroId){
				heroVO = vo;
			}
		}
		if(heroVO == null){
			return ListResult.statusCode(GameStatusCodeConstant.APP_NOT_HAVE_HERO);
		}
		
		AppRecord record = appRecordDao.get(actorId, appId);
		RecordInfoVO15 appRecordExtVO15 = record.getRecordInfoVO();
		Map<Integer,Integer> buyMap = appRecordExtVO15.map;
		int costTicket = 0;
		int useNum = 0;
		if(buyMap.containsKey(heroId)){//已经有购买记录
			for(Entry<Integer,Integer> entry : buyMap.entrySet()){
				if(entry.getKey() == heroId){
					useNum = entry.getValue();
				}
			}
		}
		if(useNum >= buyHeroVO.buyNum){//次数已经用完
			return ListResult.statusCode(GameStatusCodeConstant.HERO_BUY_MAX);
		}else{
			useNum += 1;
		}
		costTicket = FormulaHelper.executeCeilInt(buyHeroVO.costTicket, useNum);
		boolean isOk = vipFacade.decreaseTicket(actorId, TicketDecreaseType.ACTIVE_BUY, costTicket, 0, 0);//扣除点券
		if(isOk == false){
			return ListResult.statusCode(StatusCode.TICKET_NOT_ENOUGH);
		}
		heroSoulFacade.addSoul(actorId, HeroSoulAddType.APP_REWARD, buyHeroVO.heroId, buyHeroVO.num);//添加仙人魂魄
		appRecordExtVO15.map.put(heroId, useNum);
		appRecordDao.update(record);
		RewardObject rewardGoods = new RewardObject(RewardType.HEROSOUL,buyHeroVO.heroId,buyHeroVO.num);
		
		AppRecordVO appRecordVO = new AppRecordVO(record.appId, appRecordExtVO15.getRecordInfoMaps());
		AppPushHelper.pushAppRecord(actorId, appRecordVO);
		return ListResult.list(rewardGoods);
	}

	@Override
	public void onActorLogin(long actorId, long appId) {
		resetValue(actorId,appId);
	}
	
	private void resetValue(long actorId, long appId) {
		if (appEnable(actorId, appId).isFail()) {
			return;
		}
		AppRecord record = appRecordDao.get(actorId, appId);
		ChainLock lock = LockUtils.getLock(record);
		try{
			lock.lock();
			RecordInfoVO15 recordInfoVO15 = record.getRecordInfoVO();
			if (DateUtils.isToday(record.operationTime) == false) {
				recordInfoVO15.map.clear();
				appRecordDao.update(record);
			}
		}catch(Exception e){
			LOGGER.error("{}",e);
		}finally{
			lock.unlock();
		}
	}

}

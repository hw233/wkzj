package com.jtang.gameserver.module.sign.facade.impl;

import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.GameStatusCodeConstant;
import com.jtang.core.event.Event;
import com.jtang.core.event.EventBus;
import com.jtang.core.event.Receiver;
import com.jtang.core.lock.ChainLock;
import com.jtang.core.lock.LockUtils;
import com.jtang.core.model.RewardType;
import com.jtang.core.result.TResult;
import com.jtang.core.schedule.Schedule;
import com.jtang.core.schedule.ZeroListener;
import com.jtang.core.utility.DateUtils;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.component.event.EventKey;
import com.jtang.gameserver.component.event.RechargeTicketsEvent;
import com.jtang.gameserver.component.listener.ActorLoginListener;
import com.jtang.gameserver.dataconfig.model.SignRewardConfig;
import com.jtang.gameserver.dataconfig.service.SignService;
import com.jtang.gameserver.dbproxy.entity.Sign;
import com.jtang.gameserver.module.equip.facade.EquipFacade;
import com.jtang.gameserver.module.equip.type.EquipAddType;
import com.jtang.gameserver.module.goods.facade.GoodsFacade;
import com.jtang.gameserver.module.goods.type.GoodsAddType;
import com.jtang.gameserver.module.hero.facade.HeroFacade;
import com.jtang.gameserver.module.hero.facade.HeroSoulFacade;
import com.jtang.gameserver.module.hero.type.HeroSoulAddType;
import com.jtang.gameserver.module.sign.constant.SignRule;
import com.jtang.gameserver.module.sign.dao.SignDao;
import com.jtang.gameserver.module.sign.facade.SignFacade;
import com.jtang.gameserver.module.sign.handler.response.SignInfoResponse;
import com.jtang.gameserver.module.sign.helper.SignPushHelper;
import com.jtang.gameserver.module.sign.type.SignType;
import com.jtang.gameserver.module.sign.type.VipSignType;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.facade.VipFacade;
import com.jtang.gameserver.module.user.type.TicketAddType;
import com.jtang.gameserver.server.session.PlayerSession;
@Component
public class SignFacadeImpl implements SignFacade, ActorLoginListener, ZeroListener,Receiver {

	private static final Logger LOGGER = LoggerFactory.getLogger(SignFacadeImpl.class);
	
	@Autowired
	private GoodsFacade goodsFacade;
	@Autowired
	private HeroSoulFacade heroSoulFacade;
	@Autowired
	private HeroFacade heroFacade;
	@Autowired
	private EquipFacade equipFacade;
	@Autowired
	private ActorFacade actorFacade;
	@Autowired
	private SignDao signDao;
	@Autowired
	private VipFacade vipFacade;
	@Autowired
	Schedule schedule;
	@Autowired
	PlayerSession playerSession;
	@Autowired
	private EventBus eventBus;
	
	@PostConstruct
	public void init() {
		eventBus.register(EventKey.TICKETS_RECHARGE, this);
	}
	

	@Override
	public void onZero() {
		Set<Long> actorIds = playerSession.onlineActorList();
		for(long actorId:actorIds){
			Sign sign = signDao.get(actorId);
			ChainLock lock = LockUtils.getLock(sign);
			try{
				lock.lock();
				sign.dayClean();
				sign.monthClean();
				signDao.update(sign);
				SignPushHelper.pushSignInfo(actorId,getInfo(actorId));
			}catch(Exception e){
				LOGGER.error("{}",e);
			}finally{
				lock.unlock();
			}
		}
	}
	
	@Override
	public void onLogin(long actorId) {
		Sign sign = signDao.get(actorId);
		if(DateUtils.isToday(sign.operationTime) == false){
			ChainLock lock = LockUtils.getLock(sign);
			try{
				lock.lock();
				sign.dayClean();
				sign.monthClean();
				signDao.update(sign);
			}catch(Exception e){
				LOGGER.error("{}",e);
			}finally{
				lock.unlock();
			}
		}
	}
	
	@Override
	public TResult<SignInfoResponse> getInfo(long actorId) {
		Sign sign = signDao.get(actorId);
		SignInfoResponse response = new SignInfoResponse(sign);
		return TResult.sucess(response);
	}
	
	@Override
	public TResult<SignInfoResponse> sign(long actorId) {
		Sign sign = signDao.get(actorId);
		ChainLock lock = LockUtils.getLock(sign);
		try{
			lock.lock();
			if(sign.isSign == SignType.IN_SIGN.getCode()){
				return TResult.valueOf(GameStatusCodeConstant.SIGN_REWARD_ALREADY_GET);
			}
			sign.day += 1;
			sign.isSign = (byte)SignType.IN_SIGN.getCode();
			SignRewardConfig signReward = SignService.getSignReward(sign.day,TimeUtils.getMonth());
			sendReward(actorId,signReward);
			signDao.update(sign);
		}catch(Exception e){
			LOGGER.error("{}",e);
		}finally{
			lock.unlock();
		}
		return getInfo(actorId);
	}
	
	@Override
	public TResult<SignInfoResponse> vipSign(long actorId) {
		Sign sign = signDao.get(actorId);
		ChainLock lock = LockUtils.getLock(sign);
		try{
			lock.lock();
			if(sign.vipSignState == VipSignType.IN_SIGN.getCode()){
				return TResult.valueOf(GameStatusCodeConstant.SIGN_REWARD_ALREADY_GET);
			}
			if(sign.vipSignState == VipSignType.NOT_SIGN.getCode()){
				return TResult.valueOf(GameStatusCodeConstant.SIGN_NOT_REWARD);
			}
			sign.vipDay += 1;
			sign.vipSignState = (byte)VipSignType.IN_SIGN.getCode();
			SignRewardConfig signReward = SignService.getLuxuryReward(sign.vipDay,TimeUtils.getMonth());
			sendReward(actorId,signReward);
			signDao.update(sign);
		}catch(Exception e){
			LOGGER.error("{}",e);
		}finally{
			lock.unlock();
		}
		return getInfo(actorId);
	}
	
	private void sendReward(long actorId ,SignRewardConfig signReward) {
		int num = signReward.rewardNum;
		int vipLevel = vipFacade.getVipLevel(actorId);
		if(vipLevel>= signReward.vipLevel && signReward.vipLevel > 0){
			num = signReward.vipNum;
		}
		switch (RewardType.getType(signReward.rewardType)) {
		case GOODS:
			goodsFacade.addGoodsVO(actorId, GoodsAddType.SIGN_AWARD, signReward.rewardId,num);
			break;
		case EQUIP:
			equipFacade.addEquip(actorId, EquipAddType.SIGN_AWARD, signReward.rewardId);
			break;
		case HEROSOUL:
			heroSoulFacade.addSoul(actorId, HeroSoulAddType.SIGN, signReward.rewardId,num);
			break;
		case TICKET:
			vipFacade.addTicket(actorId, TicketAddType.SIGN, num);
		default:
			break;
		}
	}

	@Override
	public void onEvent(Event paramEvent) {
		if(paramEvent.name == EventKey.TICKETS_RECHARGE){
			RechargeTicketsEvent event = paramEvent.convert();
			Sign sign = signDao.get(event.actorId);
			ChainLock lock = LockUtils.getLock(sign);
			try{
				lock.lock();
				if(sign.vipSignState == VipSignType.NOT_SIGN.getCode()){
					sign.rechageNum += event.money;
					if(sign.rechageNum >= SignRule.SIGN_RECHARGE_NUM){
						sign.vipSignState = (byte)VipSignType.SIGN.getCode();
						signDao.update(sign);
						SignPushHelper.pushSignInfo(event.actorId,getInfo(event.actorId));
					}
				}
			}catch(Exception e){
				LOGGER.error("{}",e);
			}finally{
				lock.unlock();
			}
		}
	}

}

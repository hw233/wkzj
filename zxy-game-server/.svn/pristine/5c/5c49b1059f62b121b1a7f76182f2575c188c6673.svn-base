package com.jtang.gameserver.module.extapp.vipbox.facade.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.GameStatusCodeConstant;
import com.jtang.core.lock.ChainLock;
import com.jtang.core.lock.LockUtils;
import com.jtang.core.model.RewardObject;
import com.jtang.core.model.RewardType;
import com.jtang.core.protocol.StatusCode;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.core.schedule.Schedule;
import com.jtang.core.schedule.ZeroListener;
import com.jtang.core.utility.DateUtils;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.component.listener.ActorLoginListener;
import com.jtang.gameserver.dataconfig.model.EquipConfig;
import com.jtang.gameserver.dataconfig.model.VipBoxControlConfig;
import com.jtang.gameserver.dataconfig.model.VipBoxRewardConfig;
import com.jtang.gameserver.dataconfig.model.VipBoxTimeConfig;
import com.jtang.gameserver.dataconfig.service.EquipService;
import com.jtang.gameserver.dataconfig.service.VipBoxService;
import com.jtang.gameserver.dbproxy.entity.VipBox;
import com.jtang.gameserver.module.equip.facade.EquipFacade;
import com.jtang.gameserver.module.equip.type.EquipAddType;
import com.jtang.gameserver.module.extapp.vipbox.dao.VipBoxDao;
import com.jtang.gameserver.module.extapp.vipbox.facade.VipBoxFacade;
import com.jtang.gameserver.module.extapp.vipbox.handler.response.VipBoxConfigResponse;
import com.jtang.gameserver.module.extapp.vipbox.handler.response.VipBoxResponse;
import com.jtang.gameserver.module.extapp.vipbox.helper.VipBoxPushHelper;
import com.jtang.gameserver.module.extapp.vipbox.type.VipBoxType;
import com.jtang.gameserver.module.goods.facade.GoodsFacade;
import com.jtang.gameserver.module.goods.type.GoodsAddType;
import com.jtang.gameserver.module.goods.type.GoodsDecreaseType;
import com.jtang.gameserver.module.hero.facade.HeroSoulFacade;
import com.jtang.gameserver.module.hero.type.HeroSoulAddType;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.facade.VipFacade;
import com.jtang.gameserver.module.user.type.GoldAddType;
import com.jtang.gameserver.module.user.type.TicketAddType;
import com.jtang.gameserver.module.user.type.TicketDecreaseType;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class VipBoxFacadeImpl implements VipBoxFacade,ZeroListener,ActorLoginListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(VipBoxFacadeImpl.class);
	
	@Autowired
	public VipBoxDao vipBoxDao;
	@Autowired
	public VipFacade vipFacade;
	@Autowired
	public GoodsFacade goodsFacade;
	@Autowired
	public EquipFacade equipFacade;
	@Autowired
	public ActorFacade actorFacade;
	@Autowired
	public HeroSoulFacade heroSoulFacade;
	@Autowired
	public PlayerSession playerSession;
	@Autowired
	public Schedule schedule;
	
	/** 单服产出控制 */
	private static Map<Integer,Integer> OPEN_MAP = new ConcurrentHashMap<>();
	
	@PostConstruct
	public void init() {
		resetOpenMap();
	}

	
	@Override
	public TResult<VipBoxResponse> getInfo(long actorId) {
		VipBox vipBox = vipBoxDao.get(actorId);
		int vipLevel = vipFacade.getVipLevel(actorId);
		int boxNum = VipBoxService.getBoxNum(vipLevel);
		int goodsId = VipBoxService.getBoxId(vipLevel);
		int goodsNum = goodsFacade.getCount(actorId, goodsId);
		int getBoxNum = boxNum - (vipBox.openNum + goodsNum);
		VipBoxResponse response = new VipBoxResponse(vipBox.isReward,getBoxNum,vipBox.openNum);
		return TResult.sucess(response);
	}

	@Override
	public Result getBox(long actorId) {
		if(isOpen() == false){
			return Result.valueOf(GameStatusCodeConstant.VIP_BOX_CLOSED);
		}
		VipBox vipBox = vipBoxDao.get(actorId);
		ChainLock lock = LockUtils.getLock(vipBox);
		try{
			lock.lock();
			int vipLevel = vipFacade.getVipLevel(actorId);
			int boxNum = VipBoxService.getBoxNum(vipLevel);
			int goodsId = VipBoxService.getBoxId(vipLevel);
			int goodsNum = goodsFacade.getCount(actorId, goodsId);
			if(vipBox.isReward == 1){
				return Result.valueOf(GameStatusCodeConstant.VIP_BOX_IS_GET);
			}
			if(boxNum == 0){
				return Result.valueOf(GameStatusCodeConstant.VIP_BOX_VIP_LEVEL_NOT_ENOUGH);
			}
			if(goodsNum + vipBox.openNum >= boxNum && boxNum != 0){
				return Result.valueOf(GameStatusCodeConstant.VIP_BOX_MAX);
			}
			goodsFacade.addGoodsVO(actorId, GoodsAddType.VIP_BOX, goodsId,boxNum - (vipBox.openNum + goodsNum));
			vipBox.isReward = 1;
			vipBox.getTime = TimeUtils.getNow();
			vipBoxDao.update(vipBox);
		}catch(Exception e){
			LOGGER.error("{}",e);
		}finally{
			lock.unlock();
		}
		return Result.valueOf();
	}

	@Override
	public TResult<List<RewardObject>> openBox(long actorId,int useNum) {
		VipBox vipBox = vipBoxDao.get(actorId);
		int vipLevel = vipFacade.getVipLevel(actorId);
		int goodsId = VipBoxService.getBoxId(vipLevel);
		int goodsNum = goodsFacade.getCount(actorId, goodsId);
		if(goodsNum <= 0){
			return TResult.valueOf(GameStatusCodeConstant.VIP_BOX_NOT_ENOUGH);
		}
		if(goodsNum < useNum){
			useNum = goodsNum;
		}
		if(VipBoxService.getOpenMax() < vipBox.openNum + useNum || useNum == 0){ 
			return TResult.valueOf(GameStatusCodeConstant.VIP_BOX_OPEN_MAX);
		}
		int ticketNum = vipFacade.getTicket(actorId);
		if(ticketNum <= 0){
			return TResult.valueOf(StatusCode.TICKET_NOT_ENOUGH);
		}
		int costTicket = 0;
		for (int i = 1; i <= useNum; i++) {
			costTicket += VipBoxService.getCostTicket(vipBox.openNum + i);
			if(costTicket > ticketNum){
				useNum = vipBox.openNum + i;
				costTicket -= VipBoxService.getCostTicket(vipBox.openNum + i);
			}
		}
		if(costTicket <= 0){
			return TResult.valueOf(StatusCode.TICKET_NOT_ENOUGH);
		}
		boolean isOk = vipFacade.decreaseTicket(actorId, TicketDecreaseType.VIP_BOX, costTicket, 0, 0);
		if(isOk == false){
			return TResult.valueOf(StatusCode.TICKET_NOT_ENOUGH);
		}
		goodsFacade.decreaseGoods(actorId, GoodsDecreaseType.VIP_BOX, goodsId, useNum);
		ChainLock lock = LockUtils.getLock(vipBox,OPEN_MAP);
		List<RewardObject> list = new ArrayList<>();
		for (int i = 0; i < useNum; i++) {
			List<VipBoxRewardConfig> rewardList = VipBoxService.getReward(vipBox.openNum + 1, OPEN_MAP);
			putOpen(rewardList);
			list.addAll(sendReward(actorId, rewardList));
		}
		try{
			lock.lock();
			vipBox.openNum += useNum;
			vipBox.getTime = TimeUtils.getNow();
			vipBoxDao.update(vipBox);
		}catch(Exception e){
			LOGGER.error("{}",e);
		}finally{
			lock.unlock();
		}
		VipBoxPushHelper.pushBoxResponse(actorId, getInfo(actorId).item);
		return TResult.sucess(list);
	}

	private List<RewardObject> sendReward(long actorId,List<VipBoxRewardConfig> rewardList) {
		List<RewardObject> list = new ArrayList<>();
		for(VipBoxRewardConfig config : rewardList){
			VipBoxType type = VipBoxType.getType(config.rewardType);
			RewardObject rewardObject = null;
			switch(type){
			case EQUIP:
				equipFacade.addEquip(actorId, EquipAddType.VIP_BOX, config.rewardId);
				rewardObject = new RewardObject(RewardType.EQUIP,config.rewardId,1);
				break;
			case FRAGMENT:
				goodsFacade.addGoodsVO(actorId, GoodsAddType.VIP_BOX, config.rewardId,config.rewardNum);
				rewardObject = new RewardObject(RewardType.GOODS,config.rewardId,config.rewardNum);
				break;
			case GOLD:
				actorFacade.addGold(actorId, GoldAddType.VIP_BOX, config.rewardNum);
				rewardObject = new RewardObject(RewardType.GOLD,config.rewardId,config.rewardNum);
				break;
			case GOODS:
				goodsFacade.addGoodsVO(actorId, GoodsAddType.VIP_BOX, config.rewardId,config.rewardNum);
				rewardObject = new RewardObject(RewardType.GOODS,config.rewardId,config.rewardNum);
				break;
			case HEROSOUL:
				heroSoulFacade.addSoul(actorId, HeroSoulAddType.VIP_BOX, config.rewardId, config.rewardNum);
				rewardObject = new RewardObject(RewardType.HEROSOUL,config.rewardId,config.rewardNum);
				break;
			case TICKET:
				vipFacade.addTicket(actorId, TicketAddType.VIP_BOX, config.rewardNum);
				rewardObject = new RewardObject(RewardType.TICKET,config.rewardId,config.rewardNum);
				break;
			case NONE:
				break;
			default:
				break;
			}
			list.add(rewardObject);
		}
		return list;
	}

	@Override
	public void onZero() {
		ChainLock openMapLock = LockUtils.getLock(OPEN_MAP);
		try{
			openMapLock.lock();
			resetOpenMap();
		}catch(Exception e){
			LOGGER.error("{}",e);
		}finally{
			openMapLock.unlock();
		}
		
		
		for (long actorId : playerSession.onlineActorList()) {
			VipBox vipBox = vipBoxDao.get(actorId);
			ChainLock lock = LockUtils.getLock(vipBox);
			try{
				lock.lock();
				vipBox.isReward = 0;
				vipBox.openNum = 0;
				vipBox.getTime = TimeUtils.getNow();
				vipBoxDao.update(vipBox);
			}catch(Exception e){
				LOGGER.error("{}",e);
			}finally{
				lock.unlock();
			}
			VipBoxPushHelper.pushBoxResponse(actorId, getInfo(actorId).item);
		}
	}

	@Override
	public void onLogin(long actorId) {
		VipBox vipBox = vipBoxDao.get(actorId);
		ChainLock lock = LockUtils.getLock(vipBox);
		try{
			lock.lock();
			if(DateUtils.isToday(vipBox.getTime) == false){
				vipBox.isReward = 0;
				vipBox.openNum = 0;
				vipBox.getTime = TimeUtils.getNow();
				vipBoxDao.update(vipBox);
			}
		}catch(Exception e){
			LOGGER.error("{}",e);
		}finally{
			lock.unlock();
		}
	}

	@Override
	public TResult<VipBoxConfigResponse> getConfig() {
		VipBoxTimeConfig config = VipBoxService.getOpenTime();
		VipBoxConfigResponse response = new VipBoxConfigResponse(config.start,config.end);
		return TResult.sucess(response);
	}
	
	private boolean isOpen(){
		int now = TimeUtils.getNow();
		VipBoxTimeConfig config = VipBoxService.getOpenTime();
		return config.start <= now && now <= config.end;
	}
	
	private void putOpen(List<VipBoxRewardConfig> rewardList) {
		Map<Integer,Integer> map = new ConcurrentHashMap<>();
		for(Entry<Integer,Integer> entry : OPEN_MAP.entrySet()){
			VipBoxControlConfig controlConfig = VipBoxService.getControl(entry.getKey());
			for(VipBoxRewardConfig rewardConfig : rewardList){
				if(rewardConfig.rewardType == controlConfig.type){
					if(controlConfig.type == 3){//装备
						EquipConfig config = EquipService.get(rewardConfig.rewardId);
						if(config != null && config.getStar() >= controlConfig.context){
							map.put(entry.getKey(), entry.getValue() + 1);
						}
					}else if(controlConfig.type == 2 || controlConfig.type == 4 || controlConfig.type == 5){//物品、魂魄、碎片
						if(controlConfig.context == rewardConfig.rewardId){
							map.put(entry.getKey(), entry.getValue() + 1);
						}
					}
				}
			}
		}
		if(map.isEmpty() == false){
			OPEN_MAP.putAll(map);
		}
	}
	
	private void resetOpenMap() {
		Collection<VipBoxControlConfig> list = VipBoxService.getOpenControl();
		for(VipBoxControlConfig config : list){
			OPEN_MAP.put(config.index, 0);
		}
	}
}

package com.jtang.gameserver.module.goods.facade.impl;

import static com.jiatang.common.GameStatusCodeConstant.GOODS_COMPOSE_NOT_USE;
import static com.jiatang.common.GameStatusCodeConstant.GOODS_CONFIG_NOT_EXISTS;
import static com.jiatang.common.GameStatusCodeConstant.GOODS_DENY_ADD_GOLD;
import static com.jiatang.common.GameStatusCodeConstant.GOODS_DENY_ADD_TICKET;
import static com.jiatang.common.GameStatusCodeConstant.GOODS_NOT_COMPOSE;
import static com.jiatang.common.GameStatusCodeConstant.GOODS_NOT_ENOUGH;
import static com.jiatang.common.GameStatusCodeConstant.GOODS_NOT_EXISTS;
import static com.jiatang.common.GameStatusCodeConstant.GOODS_NUM_ADD_ERROR;
import static com.jiatang.common.GameStatusCodeConstant.GOODS_NUM_NOT_ENOUGH;
import static com.jtang.core.protocol.StatusCode.DATA_VALUE_ERROR;
import static com.jtang.core.protocol.StatusCode.OPERATION_ERROR;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.model.EquipType;
import com.jtang.core.lock.ChainLock;
import com.jtang.core.lock.LockUtils;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.core.utility.RandomUtils;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.component.oss.GameOssLogger;
import com.jtang.gameserver.dataconfig.model.EquipConfig;
import com.jtang.gameserver.dataconfig.model.GoodsComposeConfig;
import com.jtang.gameserver.dataconfig.model.GoodsConfig;
import com.jtang.gameserver.dataconfig.model.GoodsUseConfig;
import com.jtang.gameserver.dataconfig.service.EquipService;
import com.jtang.gameserver.dataconfig.service.GoodsService;
import com.jtang.gameserver.dataconfig.service.UseGoodsService;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.dbproxy.entity.Goods;
import com.jtang.gameserver.module.goods.constant.GoodsRule;
import com.jtang.gameserver.module.goods.dao.GoodsDao;
import com.jtang.gameserver.module.goods.effect.UseParser;
import com.jtang.gameserver.module.goods.effect.UseParser16;
import com.jtang.gameserver.module.goods.facade.GoodsFacade;
import com.jtang.gameserver.module.goods.handler.response.StartComposeResponse;
import com.jtang.gameserver.module.goods.helper.GoodsPushHelper;
import com.jtang.gameserver.module.goods.helper.UseGoodsHelper;
import com.jtang.gameserver.module.goods.model.GoodsVO;
import com.jtang.gameserver.module.goods.type.GoodsAddType;
import com.jtang.gameserver.module.goods.type.GoodsDecreaseType;
import com.jtang.gameserver.module.goods.type.UseGoodsParserType;
import com.jtang.gameserver.module.goods.type.UseGoodsResult;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.facade.VipFacade;
import com.jtang.gameserver.module.user.model.VipPrivilege;
import com.jtang.gameserver.module.user.type.GoldAddType;

@Component
public class GoodsFacadeImpl implements GoodsFacade {
	private final Logger LOGGER = LoggerFactory.getLogger(GoodsFacadeImpl.class);

	@Autowired
	GoodsDao goodsDao;
	@Autowired
	ActorFacade actorFacade;
	@Autowired
	VipFacade vipFacade;
	
	@Override
	public Collection<GoodsVO> getList(long actorId) {
		Goods entity = goodsDao.get(actorId);
		return entity.getGoodsMap().values();
	}

	@Override
	public GoodsVO getGoodsVO(long actorId, int goodsId) {
		Goods entity = goodsDao.get(actorId);
		return entity.getGoodsVO(goodsId);
	}

	@Override
	public TResult<Long> addGoodsVO(long actorId, GoodsAddType type, int goodsId, int num) {
		if (goodsId == GoodsRule.GOODS_ID_GOLD) {
			return TResult.valueOf(GOODS_DENY_ADD_GOLD);
		}
		if (goodsId == GoodsRule.GOODS_ID_TICKET) {
			return TResult.valueOf(GOODS_DENY_ADD_TICKET);
		}
		
		if (goodsId < 1 || GoodsService.get(goodsId) == null) {
			LOGGER.warn(String.format("add goods fail actorId:[%s] addType:[%s] goodsId:[%s] num:[%s]", actorId, type.getId(), goodsId, num));
			return TResult.valueOf(GOODS_CONFIG_NOT_EXISTS);
		}
		
		if (num < 1) {
			return TResult.valueOf(GOODS_NUM_ADD_ERROR);
		}

		Goods entity = goodsDao.get(actorId);
		ChainLock lock = LockUtils.getLock(entity);
		try {
			lock.lock();
			GoodsVO vo = entity.addGoodsVO(goodsId, num);
			goodsDao.update(entity);
			GoodsPushHelper.pushGoodsAttribute(actorId, vo);
			
			//oss
			Actor actor = actorFacade.getActor(actorId);
			GameOssLogger.goodsAdd(actor.uid, actor.platformType, actor.channelId, actor.serverId, actorId, type, goodsId, num);
			
			return TResult.sucess(Long.valueOf(vo.goodsId));
		} catch (Exception e) {
			LOGGER.error("{}", e);
			return TResult.valueOf(OPERATION_ERROR);
		} finally {
			lock.unlock();
		}
	}
	
	@Override
	public TResult<List<Long>> addGoodsVO(long actorId, GoodsAddType type, Map<Integer, Integer> goods) {
		List<Long> uuids = new ArrayList<>();

		for (Integer goodsId : goods.keySet()) {
			TResult<Long> goodsResult = addGoodsVO(actorId, type, goodsId, goods.get(goodsId));
			if (goodsResult.isFail()) {
				continue;
			}
			uuids.add(goodsResult.item);
		}
		return TResult.sucess(uuids);
	}

	@Override
	public int getCount(long actorId, int goodsId) {
		Goods entity = goodsDao.get(actorId);
		GoodsVO vo = entity.getGoodsVO(goodsId);
		if (vo == null) {
			return 0;
		}
		return vo.num;
	}
	
	@Override
	public TResult<List<UseGoodsResult>> useGoods(long actorId, int goodsId, int useNum, int useFlag, String phoneNum) {
		GoodsConfig goodsConfig = GoodsService.get(goodsId);
		if(goodsConfig == null){
			return TResult.valueOf(DATA_VALUE_ERROR);
		}
		if(goodsConfig.parserId == UseGoodsParserType.TYPE12.getType()){//不是装备碎片类型
			Goods goods = goodsDao.get(actorId);
			GoodsVO goodsVO = goods.getGoodsVO(goodsId);
			if(goodsVO == null){
				return TResult.valueOf(GOODS_NOT_EXISTS);
			}
			if(goodsVO.composeTime - TimeUtils.getNow() > 0){//合成时间还没过
				return TResult.valueOf(GOODS_COMPOSE_NOT_USE);
			}else{
				goodsVO.composeTime = -1;
				goodsDao.update(goods);
			}
		}
		
		if (goodsConfig.parserId == UseGoodsParserType.TYPE16.getType()) {
			UseGoodsParserType type = UseGoodsParserType.getUseGoodsParserType(goodsConfig.parserId);
			UseParser16 uGoodsEffectParser = (UseParser16)UseParser.getParser(type);
			return uGoodsEffectParser.extHandler(actorId, goodsId, useNum, useFlag, phoneNum);
		}
			
		return UseGoodsHelper.processUseGoods(actorId, goodsId, useNum, useFlag);
	}

	@Override
	public Result decreaseGoods(long actorId,GoodsDecreaseType type, int goodsId, int useNum) {
		GoodsConfig goodsConfig = GoodsService.get(goodsId);
		if (goodsConfig == null) {
			return Result.valueOf(GOODS_CONFIG_NOT_EXISTS);
		}
		if (useNum <= 0) {
			return Result.valueOf(DATA_VALUE_ERROR);
		}

		Goods goods = goodsDao.get(actorId);
		ChainLock lock = LockUtils.getLock(goods);
		try {
			lock.lock();

			GoodsVO goodsVo = goods.getGoodsVO(goodsId);
			if (goodsVo == null) {
				return Result.valueOf(GOODS_NOT_EXISTS);
			}

			if (goodsVo.num < useNum) {
				return Result.valueOf(GOODS_NOT_ENOUGH);
			}

			boolean result = goods.decreaseGoods(goodsId, useNum);
			if (result) {
				GoodsPushHelper.pushGoodsAttribute(actorId, goodsVo);// 推送消息
				goodsDao.update(goods);
				Actor actor = actorFacade.getActor(actorId);
				GameOssLogger.goodsDecrease(actor.uid, actor.platformType, actor.channelId, actor.serverId, actorId, type, goodsId, useNum);
			}
		} catch (Exception e) {
			LOGGER.error("{}", e);
		} finally {
			lock.unlock();
		}

		return Result.valueOf();
	}

	@Override
	public int hasGoodsByTypeStar(long actorId, int type, int subType, int star) {
		Collection<GoodsVO> list = getList(actorId);
		for (GoodsVO goodsVO : list) {
			GoodsConfig cfg = GoodsService.get(goodsVO.goodsId);
			if (cfg.getGoodsType() == type && cfg.getGoodsSubType() == subType && cfg.getStar() == star){
				return goodsVO.goodsId;
			}
		}
		return 0;
	}
	
	/**
	 * 物品保底
	 * @param actorId
	 * @param goodsId
	 * @return
	 */
	public boolean leastGoods(long actorId, int goodsId) {
		if (UseGoodsService.hasGoods(goodsId) == false) {
			return false;
		}
		GoodsUseConfig cfg = UseGoodsService.get(goodsId);
		if (cfg == null) {
			return false;
		}
		if (cfg.numberOfUseMax == cfg.numberOfUseMin && cfg.numberOfUseMax == 0 && cfg.numberOfUseMin == 0) {
			return false;
		}
		if (cfg.numberOfUseMax < cfg.numberOfUseMin) {
			return false;
		}
		Goods goods = goodsDao.get(actorId);
		int useNum = 0;
		if (goods.useRecordMap.containsKey(goodsId)) {
			useNum = goods.useRecordMap.get(goodsId);
		} 
		useNum += 1;
		
		int leastNum = goods.getLeastNum(goodsId);
		if (leastNum == 0) { //计算保底次数
			int maxValue = cfg.numberOfUseMax;
			int minValue = cfg.numberOfUseMin;
			minValue = Math.max(minValue, useNum);
			leastNum = RandomUtils.nextInt(minValue, maxValue);
			if (leastNum == 0) {
				return false;
			}
			goods.setLeastNum(goodsId, leastNum);
		}
		
		
		if (useNum % leastNum == 0) {
			useNum = 0;
			goods.useRecordMap.put(goodsId, useNum);
			goodsDao.update(goods);
			goods.setLeastNum(goodsId, 0);
			return true;
		} else {
			goods.useRecordMap.put(goodsId, useNum);
			goodsDao.update(goods);
			return false;
		}
		
	}

	@Override
	public TResult<Integer> sellGoods(long actorId, int goodsId, int goodsNum) {
		GoodsConfig config = GoodsService.get(goodsId);
		if(config == null || goodsNum <= 0){
			return TResult.valueOf(DATA_VALUE_ERROR);
		}
		int hasNum = getCount(actorId, goodsId);
		if(hasNum < goodsNum){
			return TResult.valueOf(GOODS_NOT_ENOUGH);
		}
		decreaseGoods(actorId, GoodsDecreaseType.GOODS_SELL, goodsId, goodsNum);
		int goldNum = goodsNum * config.getSellPrice();
		actorFacade.addGold(actorId, GoldAddType.GOODS_SELL, goldNum);
		return TResult.sucess(goldNum);
	}

	@Override
	public TResult<StartComposeResponse> composeGoods(long actorId, int goodsId) {
		GoodsConfig goodsConfig = GoodsService.get(goodsId);
		if(goodsConfig == null){
			return TResult.valueOf(DATA_VALUE_ERROR);
		}
		if(goodsConfig.parserId != UseGoodsParserType.TYPE12.getType()){//不是装备碎片类型
			return TResult.valueOf(GOODS_NOT_COMPOSE);
		}
		Goods goods = goodsDao.get(actorId);
		GoodsVO goodsVO = goods.getGoodsVO(goodsId);
		if(goodsVO == null){
			return TResult.valueOf(GOODS_NOT_EXISTS);
		}
		for(GoodsVO vo:goods.getGoodsMap().values()){
			if(vo.composeTime > 0){
				return TResult.valueOf(GOODS_COMPOSE_NOT_USE);
			}
		}
		String[] depends = goodsConfig.depends.split(Splitable.ATTRIBUTE_SPLIT);
		int dependsId = Integer.valueOf(depends[0]);
		int dependsNum = Integer.valueOf(depends[1]);
		int hasNum = getCount(actorId,dependsId);
		if(dependsNum > hasNum){//碎片数量不足
			return TResult.valueOf(GOODS_NUM_NOT_ENOUGH);
		}
		List<String[]> list = StringUtils.delimiterString2Array(goodsConfig.effectValue);
		String str[] = list.get(0);
		String equip[] = str[2].split(Splitable.BETWEEN_ITEMS);
		int equipId = Integer.valueOf(equip[0]);
		EquipConfig equipConfig = EquipService.get(equipId);
		GoodsComposeConfig config = GoodsService.getComposeConfig(equipConfig.getStar());
		EquipType equipType = EquipType.getType(equipConfig.getType());
		switch (equipType) {
		case ARMOR:
			goodsVO.composeTime = TimeUtils.getNow() + config.armor;
			break;
		case ORNAMENTS:
			goodsVO.composeTime = TimeUtils.getNow() + config.ornaments;
			break;
		case WEAPON:
			goodsVO.composeTime = TimeUtils.getNow() + config.weapon;
			break;
		default:
			break;
		}
		//vip特权缩短合成时间
		int vipLevel = vipFacade.getVipLevel(actorId);
		VipPrivilege vipPrivilege = vipFacade.getVipPrivilege(vipLevel);
		if(vipPrivilege != null){
			int useTime = goodsVO.composeTime - TimeUtils.getNow();
			if(vipPrivilege.getComposeGoods(useTime) > 0){
				goodsVO.composeTime = TimeUtils.getNow() + vipPrivilege.getComposeGoods(useTime);
			}
		}
		goodsDao.update(goods);
		StartComposeResponse response = new StartComposeResponse(goodsVO.composeTime - TimeUtils.getNow());
		return TResult.sucess(response);
	}

}

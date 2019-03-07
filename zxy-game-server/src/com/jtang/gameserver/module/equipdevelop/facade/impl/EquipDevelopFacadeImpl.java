package com.jtang.gameserver.module.equipdevelop.facade.impl;

import static com.jiatang.common.GameStatusCodeConstant.EQUIP_CAN_NOT_CONVERT;
import static com.jiatang.common.GameStatusCodeConstant.EQUIP_CAN_NOT_DEVELOP;
import static com.jiatang.common.GameStatusCodeConstant.EQUIP_DEVELOP_FAIL;
import static com.jiatang.common.GameStatusCodeConstant.EQUIP_EXITS_IN_GRID;
import static com.jiatang.common.GameStatusCodeConstant.EQUIP_NOT_EXIST;
import static com.jiatang.common.GameStatusCodeConstant.GOODS_COMPOSE_NOT_USE;
import static com.jiatang.common.GameStatusCodeConstant.GOODS_NOT_ENOUGH;
import static com.jiatang.common.GameStatusCodeConstant.GOODS_NOT_EXISTS;
import static com.jiatang.common.GameStatusCodeConstant.GOODS_NUM_ADD_ERROR;
import static com.jiatang.common.GameStatusCodeConstant.PIECE_CAN_NOT_CONVERT;
import static com.jtang.gameserver.admin.GameAdminStatusCodeConstant.ACTOR_NOT_EXIST;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.model.EquipType;
import com.jiatang.common.model.EquipVO;
import com.jtang.core.protocol.StatusCode;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.admin.facade.MaintainFacade;
import com.jtang.gameserver.component.oss.GameOssLogger;
import com.jtang.gameserver.dataconfig.model.DevelopConfig;
import com.jtang.gameserver.dataconfig.model.EquipConfig;
import com.jtang.gameserver.dataconfig.model.GoodsConfig;
import com.jtang.gameserver.dataconfig.service.EquipDevelopService;
import com.jtang.gameserver.dataconfig.service.EquipService;
import com.jtang.gameserver.dataconfig.service.GoodsService;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.dbproxy.entity.Goods;
import com.jtang.gameserver.module.equip.facade.EquipFacade;
import com.jtang.gameserver.module.equip.type.EquipAttributeKey;
import com.jtang.gameserver.module.equip.type.EquipDecreaseType;
import com.jtang.gameserver.module.equipdevelop.facade.EquipDevelopFacade;
import com.jtang.gameserver.module.equipdevelop.handler.request.EquipConvertRequest;
import com.jtang.gameserver.module.equipdevelop.handler.response.EquipDevelopResponse;
import com.jtang.gameserver.module.equipdevelop.model.ItemDTO;
import com.jtang.gameserver.module.equipdevelop.type.ConvertTypeEnum;
import com.jtang.gameserver.module.equipdevelop.util.Util;
import com.jtang.gameserver.module.goods.dao.GoodsDao;
import com.jtang.gameserver.module.goods.facade.GoodsFacade;
import com.jtang.gameserver.module.goods.model.GoodsVO;
import com.jtang.gameserver.module.goods.type.GoodsAddType;
import com.jtang.gameserver.module.goods.type.GoodsDecreaseType;
import com.jtang.gameserver.module.lineup.facade.LineupFacade;
import com.jtang.gameserver.module.user.facade.ActorFacade;

@Component
public class EquipDevelopFacadeImpl implements EquipDevelopFacade {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EquipDevelopFacadeImpl.class);
	
	@Autowired
	GoodsDao goodsDao;
	
	@Autowired
	EquipFacade equipFacade;
	
	@Autowired
	LineupFacade lineupFacade;
	
	@Autowired
	GoodsFacade goodsFacade;
	
	@Autowired
	EquipFacade EquipFacade;            
	
	@Autowired
	ActorFacade actorFacade;
	
	@Autowired
	MaintainFacade maintainFacade;
	
	@Override
	public short equipConvert(long actorId, EquipConvertRequest request) {
		ConvertTypeEnum type = ConvertTypeEnum.get(request.getType());
		if(type == null){
			return EQUIP_CAN_NOT_CONVERT;
		}
		long uuid = request.getUuid();
		int id = request.getConfigId();
		int num = request.getNum();
		int star = 0;
		if(num < 1){
			return GOODS_NUM_ADD_ERROR;
		}
		//判断装备、碎片类型是否可提炼
		if(!EquipDevelopService.isCanConvert(type, id)){
			if(type == ConvertTypeEnum.EQUIP){
				return EQUIP_CAN_NOT_CONVERT;
			}
			return PIECE_CAN_NOT_CONVERT;
		}
		switch (type) {
		case PIECE://碎片
			Goods goods = goodsDao.get(actorId);
			GoodsVO goodsVO = goods.getGoodsVO(id);
			if(goodsVO == null){
				return GOODS_NOT_EXISTS;
			}
			GoodsConfig goodsConfig = GoodsService.get(id);
			if(goodsConfig == null){
				return GOODS_NOT_EXISTS;
			}
			if(goodsVO.composeTime - TimeUtils.getNow() > 0){//合成时间还没过
				return GOODS_COMPOSE_NOT_USE;
			}
			if(goodsVO.num < num){
				return GOODS_NUM_ADD_ERROR;
			}
			//扣除物品
			Result result = goodsFacade.decreaseGoods(actorId, GoodsDecreaseType.PIECE,id, num);
			if(result.isFail()){
				return GOODS_NOT_ENOUGH;
			}
			star = goodsConfig.getStar();
			break;
		case EQUIP://装备
			EquipVO equip = equipFacade.get(actorId, uuid);
			if(equip == null){
				return EQUIP_NOT_EXIST;
			}
			EquipConfig equipConfig = EquipService.get(equip.equipId);
			if(equipConfig == null){
				return EQUIP_NOT_EXIST;
			}
			int index = lineupFacade.isEquipInLineup(actorId, uuid);
			if (index != 0 ) {
				return EQUIP_EXITS_IN_GRID;
			}
			//扣除装备
			short success = equipFacade.delEquip(actorId, EquipDecreaseType.CONVERT,Arrays.asList(uuid));
			if(success != StatusCode.SUCCESS){
				return EQUIP_NOT_EXIST;
			}
			star = equipConfig.getStar();
			num = 1;
			break;
		}
		//扣除消耗
		List<ItemDTO> consumes = EquipDevelopService.getConsumes(type, star, num);
		if(consumes != null && consumes.size() > 0){
			if(!consumesGoods(actorId, consumes)){
				return GOODS_NOT_ENOUGH;
			}
		}
		//提炼获得装备突破所必需物品
		Map<Integer,Integer> goods = EquipDevelopService.getRewardGoodsMap(type,star, num);
		goodsFacade.addGoodsVO(actorId, GoodsAddType.EQUIP_CONVERT, goods);
		
		return StatusCode.SUCCESS;
	}


	@Override
	public TResult<EquipDevelopResponse> equipDevelop(long actorId, long uuid) {
		Actor actor = actorFacade.getActor(actorId);
		if(actor == null){
			return TResult.valueOf(ACTOR_NOT_EXIST);
		}
		EquipVO equip = equipFacade.get(actorId, uuid);
		if(equip == null){
			return TResult.valueOf(EQUIP_NOT_EXIST);
		}
		EquipConfig equipConfig = EquipService.get(equip.equipId);
		if(equipConfig == null){
			return TResult.valueOf(EQUIP_NOT_EXIST);
		}
		DevelopConfig developConfig = EquipDevelopService.getDevelopConfig(EquipType.getType(equipConfig.getType()), equipConfig.getStar(), equip.developNum + 1);
		if(developConfig == null){
			return TResult.valueOf(EQUIP_CAN_NOT_DEVELOP);
		}
		//扣除所需消耗
		List<ItemDTO> consumes = Util.parserConsumes(developConfig.getConsume());
		if(consumes != null && consumes.size() > 0){
			if(!consumesGoods(actorId, consumes)){
				return TResult.valueOf(GOODS_NOT_ENOUGH);
			}
		}
		Map<EquipAttributeKey, Number> attributeMaps = new HashMap<EquipAttributeKey, Number>();
		attributeMaps.put(EquipAttributeKey.DEVELOP_NUM, developConfig.getNum());
		switch (equip.equipType) {
		case WEAPON://武器 
			attributeMaps.put(EquipAttributeKey.ATK, equip.atk + developConfig.getAddValue());
			break;
		case ARMOR://防具
			attributeMaps.put(EquipAttributeKey.DEFENSE, equip.defense + developConfig.getAddValue());
			break;
		case ORNAMENTS://饰品
			attributeMaps.put(EquipAttributeKey.HP, equip.hp + developConfig.getAddValue());
			break;
		}
		attributeMaps.put(EquipAttributeKey.MAX_REFINE_NUM, developConfig.getAddRefineNum());
		short code = equipFacade.updateAttribute(actorId,uuid, attributeMaps);
		if(code != StatusCode.SUCCESS) {
			LOGGER.warn(String.format("update attribute error actorId:[%s] uuid:[%s] statuscode:[%s]", actorId, uuid, code));
			return TResult.valueOf(EQUIP_DEVELOP_FAIL);
		}
		//公告
		String noticeMsg = EquipDevelopService.getNoticeConfig(actor.actorName, equipConfig.getName(), equip.developNum);
		if(noticeMsg != null){
			maintainFacade.sendNotice(noticeMsg, 1, 0, new ArrayList<Integer>());
		}
		
		//OSS
		GameOssLogger.equipDevelop(actor.uid, actor.platformType, actor.channelId, actor.serverId, actorId, uuid, equip.equipId,
				equip.equipType, 1, equip.developNum - 1, equip.developNum);
		
		EquipDevelopResponse result = new EquipDevelopResponse(equip);
		return TResult.sucess(result);
	}
	
	
	/**
	 * 检查消耗物品是否足够
	 * @param actorId
	 * @param consumes
	 * @return
	 */
	public boolean hasEnoughGoods(long actorId,List<ItemDTO> consumes){
		for(ItemDTO dto : consumes){
			int goodsNum = goodsFacade.getCount(actorId, dto.getId());
			if(goodsNum < dto.getNum()){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 消耗物品
	 * @param actorId
	 * @param consumes
	 * @return
	 */
	public boolean consumesGoods(long actorId,List<ItemDTO> consumes){
		//检查物品是否足够
		if(!hasEnoughGoods(actorId, consumes)){
			return false;
		}
		//足够则可以消耗
		for(ItemDTO dto : consumes){
			goodsFacade.decreaseGoods(actorId, GoodsDecreaseType.EQUIP_CONVERT, dto.getId(), dto.getNum());
		}
		return true;
	}
}

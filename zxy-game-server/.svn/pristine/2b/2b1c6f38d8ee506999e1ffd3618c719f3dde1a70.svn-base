package com.jtang.gameserver.admin.facade.impl;

import static com.jtang.gameserver.admin.GameAdminStatusCodeConstant.ACTOR_NOT_EXIST;
import static com.jtang.gameserver.admin.GameAdminStatusCodeConstant.ADD_EQUIP_ERROR;
import static com.jtang.gameserver.admin.GameAdminStatusCodeConstant.EQUIP_NOT_EXIST;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.model.EquipVO;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.admin.facade.EquipMaintianFacade;
import com.jtang.gameserver.dataconfig.model.EquipConfig;
import com.jtang.gameserver.dataconfig.service.EquipService;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.module.enhanced.facade.EnhancedFacade;
import com.jtang.gameserver.module.equip.facade.EquipFacade;
import com.jtang.gameserver.module.equip.type.EquipAddType;
import com.jtang.gameserver.module.equip.type.EquipDecreaseType;
import com.jtang.gameserver.module.lineup.facade.LineupFacade;
import com.jtang.gameserver.module.user.facade.ActorFacade;

@Component
public class EquipMaintianFacadeImpl implements EquipMaintianFacade {

	@Autowired
	private EquipFacade equipFacade;

	@Autowired
	private ActorFacade actorFacade;
	
	@Autowired
	private LineupFacade lineupFacade;
	
	@Autowired
	private EnhancedFacade enhancedFacade;
	
	@Override
	public Result addEquip(long actorId, int equipId) {
		Actor actor = actorFacade.getActor(actorId);
		if (actor == null) {
			return Result.valueOf(ACTOR_NOT_EXIST);
		}
		TResult<Long> result = equipFacade.addEquip(actorId, EquipAddType.ADMIN_ADD, equipId);
		if (result.isOk()) {
			return Result.valueOf();
		}
		return Result.valueOf(ADD_EQUIP_ERROR);
	}

	@Override
	public Result deleteEquip(long actorId, long uuid) {
		Actor actor = actorFacade.getActor(actorId);
		if (actor == null) {
			return Result.valueOf(ACTOR_NOT_EXIST);
		}
		List<Long> uuidList = new ArrayList<>();
		uuidList.add(uuid);
		int index = lineupFacade.isEquipInLineup(actorId, uuid);
		if(index == 0){
			return Result.valueOf(equipFacade.delEquip(actorId,EquipDecreaseType.ADMIN_DELETE, uuidList));
		}else{
			Result result = lineupFacade.unassignEquip(actorId, uuid, true);
			result.statusCode = equipFacade.delEquip(actorId,EquipDecreaseType.ADMIN_DELETE, uuidList);
			return result;
		}
	}

	@Override
	public Result modifyEquipLevel(long actorId, long uuid, int level) {
		Actor actor = actorFacade.getActor(actorId);
		if(actor == null){
			return Result.valueOf(ACTOR_NOT_EXIST);
		}
		EquipVO equipVO=equipFacade.get(actorId, uuid);
		if(equipVO == null){
			return Result.valueOf(EQUIP_NOT_EXIST);
		}
		Result result=enhancedFacade.enhanceEquip(actorId, uuid, level);
		return result;
	}
	
	@Override
	public Result addAllEquip(long actorId) {
		Actor actor = actorFacade.getActor(actorId);
		if (actor == null) {
			return Result.valueOf(ACTOR_NOT_EXIST);
		}
		Collection<EquipConfig> collection = EquipService.getAllEquipConfigs();
		for (EquipConfig equipConfig : collection) {
			TResult<Long> result = equipFacade.addEquip(actorId, EquipAddType.ADMIN_ADD, equipConfig.getEquipId());
			if (result.isFail()) {
				return Result.valueOf(ADD_EQUIP_ERROR);
			}
		}
		return Result.valueOf();
	}

}

package com.jtang.gameserver.module.adventures.favor.effect;

import static com.jiatang.common.GameStatusCodeConstant.FAVOR_PRIVILEGE_NUM_ALREADY_EXHAUST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.result.TResult;
import com.jtang.gameserver.dataconfig.service.FavorRightService;
import com.jtang.gameserver.dbproxy.entity.Favor;
import com.jtang.gameserver.module.adventures.favor.model.PrivilegeVO;
import com.jtang.gameserver.module.adventures.favor.type.FavorId;
import com.jtang.gameserver.module.adventures.favor.type.FavorParserType;
import com.jtang.gameserver.module.battle.facade.BattleFacade;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.type.EnergyAddType;
import com.jtang.gameserver.module.user.type.VITAddType;
@Component
public class FavorParser1 extends FavorParser {

	@Autowired
	private BattleFacade battleFacade;
	@Autowired
	private ActorFacade actorFacade;

	@Override
	public TResult<PrivilegeVO>  execute(long actorId, Favor favor) {
		PrivilegeVO privilegeVO = favor.getPrivilegeVO(FavorId.ID1.getId());
		if (privilegeVO == null || privilegeVO.getUsedNum() >= FavorRightService.RIGHT_1_USE_NUM){
			return TResult.valueOf(FAVOR_PRIVILEGE_NUM_ALREADY_EXHAUST);
		}
		privilegeVO.setUsedNum(privilegeVO.getUsedNum() + 1);
		actorFacade.fullVIT(actorId, VITAddType.FAVOR);
		actorFacade.fullEnergy(actorId, EnergyAddType.FAVOR);
		
		if (privilegeVO.getUsedNum() == FavorRightService.RIGHT_1_USE_NUM){
			favor.removePrivilegeVO(privilegeVO.getPrivilegeId());
		}
		return TResult.sucess(privilegeVO);
	}

	@Override
	public int getParserId() {
		return FavorParserType.TYPE1.getType();
	}

}

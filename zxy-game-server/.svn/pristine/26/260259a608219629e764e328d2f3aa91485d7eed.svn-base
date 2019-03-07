package com.jtang.gameserver.module.adventures.favor.effect;

import static com.jiatang.common.GameStatusCodeConstant.FAVOR_NOT_ACIVE;
import static com.jiatang.common.GameStatusCodeConstant.FAVOR_PRIVILEGE_NUM_ALREADY_EXHAUST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.result.TResult;
import com.jtang.gameserver.dataconfig.model.FavorRightConfig;
import com.jtang.gameserver.dataconfig.service.FavorRightService;
import com.jtang.gameserver.dbproxy.entity.Favor;
import com.jtang.gameserver.module.adventures.favor.model.PrivilegeVO;
import com.jtang.gameserver.module.adventures.favor.type.FavorId;
import com.jtang.gameserver.module.adventures.favor.type.FavorParserType;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.facade.VipFacade;
import com.jtang.gameserver.module.user.type.EnergyAddType;
import com.jtang.gameserver.module.user.type.VITAddType;
@Component
public class FavorParser2 extends FavorParser {

	@Autowired
	private ActorFacade actorFacade;
	@Autowired
	private VipFacade vipFacade;
	
	@Override
	public TResult<PrivilegeVO> execute(long actorId, Favor favor) {
		FavorRightConfig favorConfig = FavorRightService.getById(FavorId.ID2.getId());
		int tickNum = vipFacade.getTotalRechargeTicket(actorId);
		int needTicket = favorConfig.useCondition;
		if (tickNum < needTicket){
			return TResult.valueOf(FAVOR_NOT_ACIVE);
		}
		PrivilegeVO privilegeVO = favor.getPrivilegeVO(favorConfig.id);
		if (privilegeVO.getUsedNum() >= FavorRightService.RIGHT_2_USE_NUM){
			return TResult.valueOf(FAVOR_PRIVILEGE_NUM_ALREADY_EXHAUST);
		}
		privilegeVO.setUsedNum(privilegeVO.getUsedNum() + 1);
		actorFacade.fullVIT(actorId, VITAddType.FAVOR);
		actorFacade.fullEnergy(actorId, EnergyAddType.FAVOR);
		return TResult.sucess(privilegeVO);
	}

	@Override
	public int getParserId() {
		return FavorParserType.TYPE2.getType();
	}

}

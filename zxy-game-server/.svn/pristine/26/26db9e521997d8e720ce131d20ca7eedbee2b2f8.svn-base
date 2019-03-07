package com.jtang.gameserver.module.adventures.favor.effect;


import static com.jiatang.common.GameStatusCodeConstant.*;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.result.TResult;
import com.jtang.gameserver.dataconfig.model.FavorRightConfig;
import com.jtang.gameserver.dataconfig.service.FavorRightService;
import com.jtang.gameserver.dbproxy.entity.Favor;
import com.jtang.gameserver.module.adventures.favor.model.PrivilegeVO;
import com.jtang.gameserver.module.adventures.favor.type.FavorId;
import com.jtang.gameserver.module.adventures.favor.type.FavorParserType;
import com.jtang.gameserver.module.user.facade.VipFacade;
import com.jtang.gameserver.module.user.type.TicketAddType;
@Component
public class FavorParser3 extends FavorParser {

	@Autowired
	private VipFacade vipFacade;
	

	@Override
	public TResult<PrivilegeVO> execute(long actorId, Favor favor) {
		FavorRightConfig favorConfig = FavorRightService.getById(FavorId.ID3.getId());
		int tickNum = vipFacade.getTotalRechargeTicket(actorId);
		int needTicket = favorConfig.useCondition;
		if (tickNum < needTicket){
			return TResult.valueOf(FAVOR_NOT_ACIVE);
		}
		PrivilegeVO privilegeVO = favor.getPrivilegeVO(favorConfig.id);
		
		if (privilegeVO.getUsedNum() >= FavorRightService.RIGHT_3_USE_NUM){
			return TResult.valueOf(FAVOR_PRIVILEGE_NUM_ALREADY_EXHAUST);
		}
		privilegeVO.setUsedNum(privilegeVO.getUsedNum() + 1);
		vipFacade.addTicket(actorId, TicketAddType.FAVOR, FavorRightService.ADD_TICKETS_NUM);
		return TResult.sucess(privilegeVO);
	}

	@Override
	public int getParserId() {
		return FavorParserType.TYPE3.getType();
	}

}

package com.jtang.gameserver.module.extapp.monthcard.helper;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.ObjectReference;
import com.jtang.gameserver.dbproxy.entity.MonthCard;
import com.jtang.gameserver.module.extapp.monthcard.handler.MonthCardCmd;
import com.jtang.gameserver.module.extapp.monthcard.handler.response.MonthCardResponse;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class MonthCardPushHelper {
	@Autowired
	PlayerSession playerSession;

	private static ObjectReference<MonthCardPushHelper> ref = new ObjectReference<MonthCardPushHelper>();

	@PostConstruct
	protected void init() {
		ref.set(this);
	}

	private static MonthCardPushHelper getInstance() {
		return ref.get();
	}
	
	public static void pushMonthCard(long actorId,MonthCard monthCard,int rechargeNum){
		MonthCardResponse response = new MonthCardResponse(monthCard,rechargeNum);
		Response rsp = Response.valueOf(ModuleName.MONTH_CARD, MonthCardCmd.GET_MONTH_CARD_INFO, response.getBytes());
		getInstance().playerSession.push(actorId, rsp);
	}
}

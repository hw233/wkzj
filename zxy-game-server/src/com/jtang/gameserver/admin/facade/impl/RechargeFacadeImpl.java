package com.jtang.gameserver.admin.facade.impl;

import static com.jtang.core.protocol.StatusCode.TICKET_NOT_ENOUGH;
import static com.jtang.gameserver.admin.GameAdminStatusCodeConstant.ACTOR_NOT_EXIST;
import static com.jtang.gameserver.admin.GameAdminStatusCodeConstant.TICKET_ADD_DB_ERROR;
import static com.jtang.gameserver.admin.GameAdminStatusCodeConstant.TICKET_ADD_ERROR;
import static com.jtang.gameserver.admin.GameAdminStatusCodeConstant.TICKET_ADD_ORDER_DUPLICATE_ERROR;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.result.Result;
import com.jtang.gameserver.admin.facade.RechargeFacade;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.dbproxy.entity.RechargeLog;
import com.jtang.gameserver.module.user.dao.RechargeLogDao;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.facade.VipFacade;
import com.jtang.gameserver.module.user.type.TicketAddType;
import com.jtang.gameserver.module.user.type.TicketDecreaseType;

@Component
public class RechargeFacadeImpl implements RechargeFacade {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	@Autowired
	private ActorFacade actorFacade;

	@Autowired
	private VipFacade vipFacade;

	@Autowired
	private RechargeLogDao rechargeLogDao;

	@Override
	public Result recharge(String orderSnid, int platformId, int serverId, String uid, int payWayId, String tradeSnid, int rechargeId, byte discount,
			BigDecimal payMoney, int rechargeTicketNum, int giveTicketNum, int mediaId, int channelId, int reachargeTime,long actorId) {
		if (rechargeTicketNum < 1) {
			LOGGER.error(String.format("充值数值错误，rechargeNum:[%s], giveNum:[%s]", rechargeTicketNum, giveTicketNum));
			return Result.valueOf(TICKET_ADD_ERROR);
		}
		RechargeLog rechargeLog = new RechargeLog(orderSnid, platformId, serverId, uid,actorId, payWayId, tradeSnid, rechargeId, rechargeTicketNum, discount,
				payMoney, mediaId, channelId, reachargeTime);
		RechargeLog orderId = rechargeLogDao.getByOrderSid(orderSnid);
		if (orderId != null) {
			return Result.valueOf(TICKET_ADD_ORDER_DUPLICATE_ERROR);
		}
		Actor actor = actorFacade.getActorId(platformId, uid, serverId,actorId);
		if (actor == null) {
			return Result.valueOf(ACTOR_NOT_EXIST);
		}

		int saveResult = rechargeLogDao.save(rechargeLog);
		if (saveResult == 1) {
			return Result.valueOf(TICKET_ADD_ORDER_DUPLICATE_ERROR);
		} else if (saveResult == 2) {
			return Result.valueOf(TICKET_ADD_DB_ERROR);
		}
		
		boolean result = vipFacade.rechargeTicket(actorId, rechargeTicketNum, giveTicketNum, payMoney.intValue(),rechargeId);
		if (!result) {
			return Result.valueOf(TICKET_ADD_ERROR);
		}
		return Result.valueOf();
	}

	@Override
	public Result giveTicket(long actorId, int giveNum) {
		Actor actor = actorFacade.getActor(actorId);
		if (actor == null) {
			return Result.valueOf(ACTOR_NOT_EXIST);
		}
		if (giveNum < 1) {
			LOGGER.error(String.format("赠送数值错误，giveNum:[%s]", giveNum));
			return Result.valueOf(TICKET_ADD_ERROR);
		}

		vipFacade.addTicket(actorId, TicketAddType.PLATFORM_GIVE, giveNum);
		return Result.valueOf();
	}

	@Override
	public Result dicreaseTicket(long actorId, int ticket) {
		Actor actor = actorFacade.getActor(actorId);
		if (actor == null) {
			return Result.valueOf(ACTOR_NOT_EXIST);
		}
		if (ticket < 1) {
			LOGGER.error(String.format("扣除数值错误，ticket:[%s]", ticket));
			return Result.valueOf(TICKET_ADD_ERROR);
		}
		boolean isOk = vipFacade.decreaseTicket(actorId, TicketDecreaseType.ADMIN, ticket, 0, 0);
		if (isOk) {
			return Result.valueOf();
		}
		return Result.valueOf(TICKET_NOT_ENOUGH);
	}

}

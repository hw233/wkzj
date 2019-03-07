package com.jtang.gameserver.module.msg.facade.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.GameStatusCodeConstant;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.core.utility.DateUtils;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.dbproxy.entity.Message;
import com.jtang.gameserver.module.chat.helper.MessageHelper;
import com.jtang.gameserver.module.icon.facade.IconFacade;
import com.jtang.gameserver.module.msg.dao.MsgDao;
import com.jtang.gameserver.module.msg.facade.MsgFacade;
import com.jtang.gameserver.module.msg.helper.MsgPushHelper;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.facade.VipFacade;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class MsgFacadeImpl implements MsgFacade {

	@Autowired
	private PlayerSession playerSession;
	@Autowired
	private ActorFacade actorFacade;

	@Autowired
	private MsgDao msgDao;
	@Autowired
	private IconFacade iconFacade;
	@Autowired
	private VipFacade vipFacade;

	@Override
	public List<Message> get(long actorId) {
		List<Message> messageList = msgDao.get(actorId);
		if(messageList == null){
			return new ArrayList<>();
		}
		ArrayList<Message> list = new ArrayList<>();
		list.addAll(messageList);
		list.addAll(msgDao.getSendMsg(actorId));
		for (Message msg : list) {
			setTemporaryInfo(msg,actorId);
		}
		return list;
	}

	private void setTemporaryInfo(Message msg,long actorId) {
		long id = 0;
		if(msg.fromActorId == actorId){//自己发出去的
			id = msg.toActorId;
		}else{//别人发给自己的
			id = msg.fromActorId;
		}
		msg.vipLevel = vipFacade.getVipLevel(id);
		msg.iconVO = iconFacade.getIconVO(id);
		Actor actor = actorFacade.getActor(id);
		if(actor!=null){
			msg.fromActorName = actor.actorName;
			msg.fromActorLevel = actor.level;
		}
	}

	@Override
	public TResult<Message> sendMsg(long fromActorId, long toActorId, String content) {
		if (fromActorId == toActorId) {
			return TResult.valueOf(GameStatusCodeConstant.MSG_SEND_FAIL);
		}
		
		Actor toActor = actorFacade.getActor(toActorId);
		if (toActorId == 0 || toActor == null){
			return TResult.valueOf(GameStatusCodeConstant.MSG_SEND_FAIL);
		}

		boolean isValid = MessageHelper.isValid(content);
		if (!isValid) {
			return TResult.valueOf(GameStatusCodeConstant.MSG_UN_VALID_MSG);
		}
		int time = DateUtils.getNowInSecondes();
		Message msg = msgDao.createMsg(fromActorId, toActorId, content, time);

		List<Long> deletedMsgList = msgDao.removeOldMessage(toActorId, msg.getPkId());

		setTemporaryInfo(msg,fromActorId);
		MsgPushHelper.pushMsg(fromActorId, msg);
		if (playerSession.isOnline(toActorId)) {
			setTemporaryInfo(msg,toActorId);
			MsgPushHelper.pushMsg(toActorId, msg);
			if (deletedMsgList != null && deletedMsgList.size() > 0) {
				MsgPushHelper.pushRemove(toActorId, deletedMsgList);
			}
		}
		return TResult.sucess(msg);
	}
	@Override
	public TResult<Message> sendMsg2one(long fromActorId, long toActorId, String content) {
		if (fromActorId == toActorId) {
			return TResult.valueOf(GameStatusCodeConstant.MSG_SEND_FAIL);
		}
		
		Actor toActor = actorFacade.getActor(toActorId);
		if (toActorId == 0 || toActor == null){
			return TResult.valueOf(GameStatusCodeConstant.MSG_SEND_FAIL);
		}
		
		boolean isValid = MessageHelper.isValid(content);
		if (!isValid) {
			return TResult.valueOf(GameStatusCodeConstant.MSG_UN_VALID_MSG);
		}
		int time = DateUtils.getNowInSecondes();
		Message msg = msgDao.createMsg(fromActorId, toActorId, content, time);
		
		List<Long> deletedMsgList = msgDao.removeOldMessage(toActorId, msg.getPkId());
		
//		setTemporaryInfo(msg,fromActorId);
//		MsgPushHelper.pushMsg(fromActorId, msg);
		if (playerSession.isOnline(toActorId)) {
			setTemporaryInfo(msg,toActorId);
			MsgPushHelper.pushMsg(toActorId, msg);
			if (deletedMsgList != null && deletedMsgList.size() > 0) {
				MsgPushHelper.pushRemove(toActorId, deletedMsgList);
			}
		}
		return TResult.sucess(msg);
	}

	@Override
	public Result removeMsg(long actorId, List<Long> mIdList) {
		msgDao.remove(actorId, mIdList);
		MsgPushHelper.pushRemove(actorId, mIdList);
		return Result.valueOf();
	}

	@Override
	public Result setReaded(long actorId) {
		msgDao.setReaded(actorId);
		return Result.valueOf();
	}
}

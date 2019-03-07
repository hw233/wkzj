package com.jtang.gameserver.module.gift.helper;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.ObjectReference;
import com.jtang.gameserver.dbproxy.entity.Gift;
import com.jtang.gameserver.module.gift.handler.GiftCmd;
import com.jtang.gameserver.module.gift.handler.response.GiftInfoResponse;
import com.jtang.gameserver.module.gift.handler.response.PushGiftStateResponse;
import com.jtang.gameserver.server.broadcast.Broadcast;

/**
 * 礼物信息推送类
 * @author vinceruan
 *
 */
@Component
public class PushGiftHelper {
	@Autowired
	Broadcast broadcast;
	
	private static ObjectReference<PushGiftHelper> REF = new ObjectReference<PushGiftHelper>();

	@PostConstruct
	protected void init(){
		REF.set(this);
	}
	
	private static PushGiftHelper getInstance(){
		return REF.get();
	}
	
	/**
	 * 推送礼物状态变更
	 * @param actorId
	 * @param type
	 * @param allyActorId
	 */
	public static void pushGiftState(long actorId,PushGiftStateResponse response) {
		Response res = Response.valueOf(ModuleName.GIFT, GiftCmd.PUSH_NEW_GIFT, response.getBytes());
		getInstance().broadcast.push(actorId, res);
	}
	
	/**
	 * 推送进度条进度
	 * */
	public static void pushGiftInfo(long actorId,Gift gift){
		GiftInfoResponse response=new GiftInfoResponse(gift);
		Response res = Response.valueOf(ModuleName.GIFT, GiftCmd.GIFT_PACKAGE_INFO,response.getBytes());
		getInstance().broadcast.push(actorId, res);
	}
}

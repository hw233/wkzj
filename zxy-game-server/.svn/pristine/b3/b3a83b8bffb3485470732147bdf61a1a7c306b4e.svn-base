package com.jtang.gameserver.module.adventures.vipactivity.helper;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.ObjectReference;
import com.jtang.gameserver.module.adventures.vipactivity.handler.VipActivityCmd;
import com.jtang.gameserver.module.adventures.vipactivity.handler.response.GiveEquipInfoResponse;
import com.jtang.gameserver.module.adventures.vipactivity.handler.response.MainHeroResponse;
import com.jtang.gameserver.module.adventures.vipactivity.handler.response.VipActivityResponse;
import com.jtang.gameserver.server.broadcast.Broadcast;

@Component
public class VipActivityPushHelper {
	@Autowired
	Broadcast broadcast;

	private static ObjectReference<VipActivityPushHelper> ref = new ObjectReference<VipActivityPushHelper>();

	@PostConstruct
	protected void init() {
		ref.set(this);
	}

	private static VipActivityPushHelper getInstance() {
		return ref.get();
	}
	
	/**
	 * 推送清除主力仙人
	 * @param actorId
	 */
	public static void pushMainHeroReset(long actorId, int percent){
		MainHeroResponse mainHeroResponse = new MainHeroResponse(0, (byte)0, percent);
		Response response = Response.valueOf(ModuleName.VIPACTIVITY, VipActivityCmd.MAIN_HERO_INFO, mainHeroResponse.getBytes());
		getInstance().broadcast.push(actorId, response);
	}
	
	/**
	 * 推送天财地宝信息
	 */
	public static void pushGiveEquipInfo(long actorId,int isUse,String name,int level,int firstHeroId){
		GiveEquipInfoResponse giveEquipResponse = new GiveEquipInfoResponse(isUse,name,level,firstHeroId);
		Response response = Response.valueOf(ModuleName.VIPACTIVITY, VipActivityCmd.PUSH_GIVE_EQUIP,giveEquipResponse.getBytes());
		getInstance().broadcast.push(actorId, response);
	}
	
	/**
	 * 推送奇遇信息变更
	 */
	public static void pushVipActivity(long actorId,int key,int value){
		VipActivityResponse response = new VipActivityResponse(key,value);
		Response rsp = Response.valueOf(ModuleName.VIPACTIVITY, VipActivityCmd.PUSH_INFO_RESET,response.getBytes());
		getInstance().broadcast.push(actorId, rsp);
	}
}

package com.jtang.gameserver.module.equip.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jiatang.common.model.EquipVO;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.ObjectReference;
import com.jtang.gameserver.module.equip.handler.EquipCmd;
import com.jtang.gameserver.module.equip.handler.response.EquipAttributeResponse;
import com.jtang.gameserver.module.equip.handler.response.EquipDelResponse;
import com.jtang.gameserver.module.equip.handler.response.EquipListResponse;
import com.jtang.gameserver.module.equip.type.EquipAttributeKey;
import com.jtang.gameserver.server.broadcast.Broadcast;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class EquipPushHelper {
	@Autowired
	Broadcast broadcast;
	
	@Autowired 
	PlayerSession playerSession;
	
	private static ObjectReference<EquipPushHelper> REF = new ObjectReference<>();
	
	@PostConstruct
	protected void init(){
		REF.set(this);
	}
	
	private static EquipPushHelper getInstance(){
		return REF.get();
	}
	
	/**
	 * 推送添加装备
	 * @param equipVo
	 */
	public static void pushAddEquip(long actorId, EquipVO equipVo, int composeNum){
		List<EquipVO> list = new ArrayList<>();
		list.add(equipVo);
		pushAddEquip(actorId, list, composeNum);
	}
	
	/**
	 * 推送添加装备集合
	 * @param equipVoList
	 */
	public static void pushAddEquip(long actorId, List<EquipVO> equipVoList, int composeNum){
		EquipListResponse result = new EquipListResponse(equipVoList, composeNum);
		Response response = Response.valueOf(ModuleName.EQUIP, EquipCmd.PUSH_ADD_EQUIP, result.getBytes());
		getInstance().playerSession.push(actorId, response);
	}
	
	/**
	 * 推送删除装备
	 * @param equip
	 */
	public static void pushDelEquip(long actorId, long uuid){
		EquipDelResponse result = new EquipDelResponse(uuid);
		Response response = Response.valueOf(ModuleName.EQUIP, EquipCmd.PUSH_DEL_EQUIP, result.getBytes());
		getInstance().broadcast.push(actorId, response);
	}
	
	/**
	 * 更新装备属性
	 * @param actorId
	 * @param key
	 * @param value
	 */
	public static void pushEquipAttribute(long actorId, long uuid, Map<EquipAttributeKey, Number> attributeMaps) {
		EquipAttributeResponse result = new EquipAttributeResponse(uuid, attributeMaps);
		Response response = Response.valueOf(ModuleName.EQUIP, EquipCmd.PUSH_EQUIP_ATTRUBITE, result.getBytes());
		getInstance().broadcast.push(actorId, response);
	}
	
}

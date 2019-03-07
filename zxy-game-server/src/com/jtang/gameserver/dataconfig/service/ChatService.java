package com.jtang.gameserver.dataconfig.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.jiatang.common.model.EquipType;
import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.gameserver.dataconfig.model.ChatConfig;
import com.jtang.gameserver.dataconfig.model.EquipConfig;

@Component
public class ChatService extends ServiceAdapter {

	private static ChatConfig chatConfig = new ChatConfig();
	
	@Override
	public void clear() {
		chatConfig.clear();
	}

	@Override
	public void initialize() {
		List<ChatConfig> list=dataConfig.listAll(this, ChatConfig.class);
		for(ChatConfig config:list){
			chatConfig = config;
		}
	}

	/**
	 * 装备添加类型是否可以发送公告
	 * @param addType
	 * @return
	 */
	public static boolean isAddEquipType(int addType) {
		for(Integer type:chatConfig.addEquipTypeList){
			if(type == addType){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 仙人添加类型是否可以发送公告
	 * @param addType
	 * @return
	 */
	public static boolean isAddHeroType(int addType) {
		for(Integer type:chatConfig.addHeroTypeList){
			if(type == addType){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 系统聊天限制获取仙人星级
	 * @return
	 */
	public static int getHeroStar(){
		return chatConfig.heroStar;
	}
	
	/**
	 * 装备星级是否够发送公告的条件
	 * @param equipConfig
	 * @return
	 */
	public static boolean isSendEquip(EquipConfig equipConfig) {
		boolean isEquip = false;
		EquipType equipType = EquipType.getType(equipConfig.getType());
		switch (equipType) {
		case WEAPON:
			isEquip = equipConfig.getStar() >= chatConfig.weaponStar;
			break;
		case ARMOR:
			isEquip = equipConfig.getStar() >= chatConfig.armorStar;
			break;
		case ORNAMENTS:
			isEquip = equipConfig.getStar() >= chatConfig.ornamentsStar;
			break;
		}
		return isEquip;
	}

}

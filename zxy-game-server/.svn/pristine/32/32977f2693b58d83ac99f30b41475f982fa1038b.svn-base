package com.jtang.gameserver.module.adventures.vipactivity.model;

import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.module.user.model.Vip13Privilege;

public class GiveEquipVO implements VipBaseVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4234209051923572419L;

	/**
	 * 接受天财地宝的actorId
	 */
	public long actorId;

	/**
	 * 发送天财地宝时间
	 */
	public int sendTime;

	public static GiveEquipVO valueOf(String str) {
		GiveEquipVO giveEquipVO = new GiveEquipVO();
		String value[] = StringUtils.split(str, Splitable.ATTRIBUTE_SPLIT);
		giveEquipVO.actorId = Long.valueOf(value[1]);
		giveEquipVO.sendTime = Integer.valueOf(value[2]);
		return giveEquipVO;
	}

	@Override
	public String parseToString() {
		StringBuffer sb = new StringBuffer(String.valueOf(Vip13Privilege.vipLevel));
		sb.append(Splitable.ATTRIBUTE_SPLIT);
		sb.append(actorId);
		sb.append(Splitable.ATTRIBUTE_SPLIT);
		sb.append(sendTime);
		return sb.toString();
	}

	public static GiveEquipVO valueOf() {
		GiveEquipVO giveEquipVO = new GiveEquipVO();
		giveEquipVO.actorId = 0;
		giveEquipVO.sendTime = 0;
		return giveEquipVO;
	}

}

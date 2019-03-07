package com.jtang.gameserver.module.equip.handler.response;

import java.util.Collection;

import com.jiatang.common.model.EquipVO;
import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 服务器回复客户端请求装备列表信息
 * 请求消息为：{@code EquipListRequest}
 * @author liujian
 *
 */
public class EquipListResponse extends IoBufferSerializer {

	/**
	 * 装备列表(装备序顺看writePacket())
	 */
	public Collection<EquipVO> equipList;
	
	private int composeNum;
	
	
	
	public EquipListResponse(Collection<EquipVO> equipList, int composeNum) {
		this.equipList = equipList;
		this.composeNum = composeNum;
	}

	@Override
	public void write() {
		writeShort((short) equipList.size());
		for (EquipVO equip : equipList) {
			writeBytes(equip.getBytes());
		}
		writeInt(this.composeNum);
	}
}

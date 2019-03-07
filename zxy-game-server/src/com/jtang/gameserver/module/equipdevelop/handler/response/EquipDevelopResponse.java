package com.jtang.gameserver.module.equipdevelop.handler.response;

import com.jiatang.common.model.EquipVO;
import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 装备突破返回对象
 * @author hezh
 *
 */
public class EquipDevelopResponse extends IoBufferSerializer {

	/**
	 * 突破后更新的装备对象
	 */
	public EquipVO equipVO;
	
	public EquipDevelopResponse(EquipVO equipVO) {
		super();
		this.equipVO = equipVO;
	}

	@Override
	public void write() {
		writeBytes(equipVO.getBytes());
	}
}

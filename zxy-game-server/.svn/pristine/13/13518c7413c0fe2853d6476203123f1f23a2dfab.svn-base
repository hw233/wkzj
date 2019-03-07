package com.jtang.gameserver.module.hole.handler.response;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.hole.model.HoleNotifyVO;

/**
 * 通知盟友洞府数据
 * @author jerry
 *
 */
public class AllyHoleNotifyResponse extends IoBufferSerializer {

	private HoleNotifyVO holeNotifyVO;
	public AllyHoleNotifyResponse(HoleNotifyVO holeNotifyVO) {
		super();
		this.holeNotifyVO = holeNotifyVO;
	}
	
	@Override
	public void write() {
		this.writeBytes(this.holeNotifyVO.getBytes());
	}

}

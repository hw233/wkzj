package com.jtang.gameserver.module.notify.handler.response;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.notify.model.AbstractNotifyVO;

/**
 * 获取角色所有类型的信息通知
 */
public class NotifyListResponse extends IoBufferSerializer {

	/**
	 * 包含所有类型的信息结构
	 */
	private List<AbstractNotifyVO> allList;

	public NotifyListResponse(List<AbstractNotifyVO> allList) {
		this.allList = new ArrayList<>();
		for (AbstractNotifyVO vo : allList) {
			this.allList.add(vo);
		}
	}

	@Override
	public void write() {
		writeShort((short) allList.size());
		for (AbstractNotifyVO response : allList) {
			writeBytes(response.getBytes());
		}
	}
}

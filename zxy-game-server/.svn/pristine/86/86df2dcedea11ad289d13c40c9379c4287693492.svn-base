package com.jtang.gameserver.module.dailytask.handler.response;

import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.dailytask.model.DailyTaskVO;
/**
 * 每日任务列表回复
 * @author ludd
 *
 */
public class DailyTaskInfoResponse extends IoBufferSerializer {
	/**
	 * 任务列表
	 */
	private List<DailyTaskVO> list;

	public DailyTaskInfoResponse(List<DailyTaskVO> list) {
		this.list = list;
	}
	
	@Override
	public void write() {
		this.writeShort((short) this.list.size());
		for (DailyTaskVO vo : list) {
			this.writeBytes(vo.getBytes());
		}
	}
	
}

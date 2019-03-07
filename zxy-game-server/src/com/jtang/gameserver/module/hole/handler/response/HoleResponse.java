package com.jtang.gameserver.module.hole.handler.response;

import java.util.Collection;
import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.hole.model.HoleNotifyVO;
import com.jtang.gameserver.module.hole.model.HoleVO;

public class HoleResponse extends IoBufferSerializer {

	
	/**
	 * 洞府集合 
	 */
	private List<HoleVO> holes;
	/**
	 * 通知集合
	 */
	private Collection<HoleNotifyVO> holeNotifyVOs;
	
	/**
	 * 自己触发次数
	 */
	private int selfCount;
	
	/**
	 * 盟友触发次数
	 */
	private int allyCount;

	public HoleResponse(List<HoleVO> holes, Collection<HoleNotifyVO> holeNotifyVOs, int selfCount, int allyCount) {
		this.holes = holes;
		this.holeNotifyVOs = holeNotifyVOs;
		this.selfCount = selfCount;
		this.allyCount = allyCount;
	}

	@Override
	public void write() {
		writeShort((short) holes.size());
		for (HoleVO holeVO : holes) {
			writeBytes(holeVO.getBytes());
		}
		writeShort((short) holeNotifyVOs.size());
		for (HoleNotifyVO vo : holeNotifyVOs) {
			writeBytes(vo.getBytes());
		}
		this.writeInt(this.selfCount);
		this.writeInt(this.allyCount);
	}

}

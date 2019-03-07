package com.jtang.gameserver.module.demon.handler.response;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.demon.model.DemonRankVO;

/**
 * 查看上次排名回复
 * @author ludd
 *
 */
public class DemonLastRankResponse extends IoBufferSerializer {

	/**
	 * 自己的功勋排名
	 */
	private DemonRankVO selfDemonRankVO;
	/**
	 * 排名列表
	 * {@code DemonRankVO}
	 */
	private List<DemonRankVO> list = new ArrayList<>();
	
	
	public DemonLastRankResponse(DemonRankVO selfDemonRankVO, List<DemonRankVO> list) {
		super();
		this.list = list;
		this.selfDemonRankVO = selfDemonRankVO;
	}


	@Override
	public void write() {
		this.writeBytes(selfDemonRankVO.getBytes());
		this.writeShort((short)list.size());
		for (DemonRankVO demonRankVO : list) {
			this.writeBytes(demonRankVO.getBytes());
		}
	}

}

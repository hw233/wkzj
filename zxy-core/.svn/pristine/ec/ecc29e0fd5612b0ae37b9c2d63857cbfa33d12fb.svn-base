package com.jiatang.common.crossbattle.response;

import java.util.ArrayList;
import java.util.List;

import com.jiatang.common.crossbattle.model.LastBattleResultVO;
import com.jtang.core.protocol.IoBufferSerializer;

public class LastBattleResultW2G extends IoBufferSerializer {
	
	/**
	 * 上次对阵结果
	 */
	public List<LastBattleResultVO> list;
	
	public LastBattleResultW2G(byte[] bytes){
		setReadBuffer(bytes);
	}
	
	
	public LastBattleResultW2G(List<LastBattleResultVO> list) {
		super();
		this.list = list;
	}


	@Override
	public void write() {
		this.writeShort((short) this.list.size());
		for (LastBattleResultVO lastBattleResultVO : list) {
			this.writeBytes(lastBattleResultVO.getBytes());
		}
	}
	
	@Override
	protected void read() {
		list = new ArrayList<>();
		short len = readShort();
		for (int i = 0; i < len; i++) {
			LastBattleResultVO battleResultVO = new LastBattleResultVO();
			battleResultVO.readBuffer(this);
			list.add(battleResultVO);
		}
	}
}

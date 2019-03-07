package com.jiatang.common.crossbattle.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jtang.core.protocol.IoBufferSerializer;

public class ServerScoreList extends IoBufferSerializer {

	/**
	 * key：组id
	 */
	private Map<Integer, List<ServerScoreVO>> map;

	
	public ServerScoreList(Map<Integer,  List<ServerScoreVO>> list) {
		super();
		this.map = list;
	}

	public ServerScoreList() {
		super();
	}

	@Override
	public void write() {
		this.writeShort((short) map.size());
		for (Map.Entry<Integer,  List<ServerScoreVO>> entry : map.entrySet()) {
			this.writeInt(entry.getKey());
			List<ServerScoreVO> list = entry.getValue();
			this.writeShort((short) list.size());
			for (ServerScoreVO serverRankVO : list) {
				this.writeBytes(serverRankVO.getBytes());
			}
		}
	}
	
	@Override
	public void readBuffer(IoBufferSerializer buffer) {
		short len = buffer.readShort();
		map = new HashMap<Integer, List<ServerScoreVO>>();
		for (int i = 0; i < len; i++) {
			int key = buffer.readInt();
			short listLen = buffer.readShort();
			List<ServerScoreVO> list = new ArrayList<>();
			for (int j = 0; j < listLen; j++) {
				ServerScoreVO serverRankVO = new ServerScoreVO();
				serverRankVO.readBuffer(buffer);
				list.add(serverRankVO);
			}
			map.put(key, list);
		}
	}
}

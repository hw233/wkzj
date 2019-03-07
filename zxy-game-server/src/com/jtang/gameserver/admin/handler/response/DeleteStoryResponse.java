package com.jtang.gameserver.admin.handler.response;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.dbproxy.entity.Stories;
import com.jtang.gameserver.module.story.model.StoryVO;

public class DeleteStoryResponse extends IoBufferSerializer {
	/**
	 * 最新已占领的战场id(0代表未占领任何战场)
	 */
	public int battleId;
	
	/**
	 * 战斗数据
	 */
	List<StoryVO> battleResult;
	
	@Override
	public void write() {
		writeInt(battleId);
		writeShort((short) battleResult.size());
		for (StoryVO st : battleResult) {
			writeInt(st.storyId);
			writeByte(st.storyStar);
			writeByte(st.oneStarAwarded);
			writeByte(st.twoStarAwarded);
			writeByte(st.threeStarAwarded);
			writeIntByteMap(st.getBattleMap());
		}
	}
	
	public DeleteStoryResponse(Stories st) {
		this.battleId = st.getBattleId();
		this.battleResult = new ArrayList<>();
		for (StoryVO sv : st.getStoryMap().values()) {
			this.battleResult.add(sv);
		}
	}
}

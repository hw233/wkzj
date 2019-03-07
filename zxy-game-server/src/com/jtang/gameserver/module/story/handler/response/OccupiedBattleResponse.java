package com.jtang.gameserver.module.story.handler.response;
//package com.jtang.sm2.module.story.handler.response;
//
//import com.jtang.core.protocol.WritablePacket;
//
///**
// * 推送最新已占领的战场(重复占领的战场不会推送)
// * @author vinceruan
// *
// */
//public class OccupiedBattleResponse extends WritablePacket {
//	int battleId;
//	
//	@Override
//	public void write() {
//		this.writeInt(battleId);
//	}
//	
//	public OccupiedBattleResponse(int battleId) {
//		this.battleId = battleId;
//	}
//}

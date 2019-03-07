package com.jtang.gameserver.module.lineup.handler.response;

import java.util.List;
import java.util.Map;

import com.jiatang.common.model.EquipVO;
import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.hero.handler.response.HeroResponse;
/**
 * 查看阵型的回复
 * @author pengzy
 *
 */
public class ViewLineupResponse extends IoBufferSerializer {
	/**
	 * key-阵型中的位置；value-阵型中的英雄
	 */
	public Map<Integer, HeroResponse> lineupHeros;
	
	/**
	 * key-阵型中的位置；value-阵型中的装备列表
	 */
	public Map<Integer, List<EquipVO>> lineupEquips;
	
	public ViewLineupResponse(Map<Integer, HeroResponse> lineupHeros, Map<Integer, List<EquipVO>> lineupEquips) {
		this.lineupHeros = lineupHeros;
		this.lineupEquips = lineupEquips;
	}
	
	@Override
	public void write() {
		writeShort((short) lineupHeros.size());// 阵型中的英雄数量
		for (Integer position : lineupHeros.keySet()) {
			writeByte(position.byteValue());// 阵型位置
			writeBytes(lineupHeros.get(position).getBytes());
		}

		writeShort((short) lineupEquips.size());// 阵型中的英雄数量
		for (Integer position : lineupEquips.keySet()) {
			writeByte(position.byteValue());// 阵型位置
			writeEquips(position);// 发送该位置装备
		}
	}
	
	private void writeEquips(int index) {
		List<EquipVO> equips = lineupEquips.get(index);
		writeShort((short) equips.size());// 装备的数量
		for (EquipVO equip : equips) {
			writeBytes(equip.getBytes());
		}
	}
}

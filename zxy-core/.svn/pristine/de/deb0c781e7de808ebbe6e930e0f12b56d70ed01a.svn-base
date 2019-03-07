package com.jiatang.common.crossbattle.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.jiatang.common.model.EquipVO;
import com.jiatang.common.model.HeroAndBuff;
import com.jtang.core.protocol.IoBufferSerializer;

public class ViewLineupVO extends IoBufferSerializer {
	/**
	 * key-阵型中的位置；value-阵型中的英雄
	 */
	public Map<Integer, HeroAndBuff> lineupHeros;
	
	/**
	 * key-阵型中的位置；value-阵型中的装备列表
	 */
	public Map<Integer, List<EquipVO>> lineupEquips;
	
	public ViewLineupVO(Map<Integer, HeroAndBuff> lineupHeros, Map<Integer, List<EquipVO>> lineupEquips) {
		super();
		this.lineupHeros = lineupHeros;
		this.lineupEquips = lineupEquips;
	}
	
	public ViewLineupVO(byte[] bytes) {
		setReadBuffer(bytes);
	}
	
	public ViewLineupVO() {
	}
	
	@Override
	public void read() {
		this.lineupHeros = new HashMap<Integer, HeroAndBuff>();
		short mapLen = readShort();
		for (int i = 0; i < mapLen; i++) {
			int key = readInt();
			HeroAndBuff value = new HeroAndBuff();
			value.readBuffer(this);
			this.lineupHeros.put(key, value);
		}
		
		this.lineupEquips = new HashMap<>();
		mapLen = readShort();
		for (int i = 0; i < mapLen; i++) {
			int key = readByte();
			List<EquipVO> list = new ArrayList<>();
			short listLen = readShort();
			for (int j = 0; j < listLen; j++) {
				EquipVO equipVO = new EquipVO();
				equipVO.readBuffer(this);
				list.add(equipVO);
			}
			this.lineupEquips.put(key, list);
		}
	}
	
	@Override
	public void readBuffer(IoBufferSerializer buffer) {
		this.lineupHeros = new HashMap<Integer, HeroAndBuff>();
		short mapLen = buffer.readShort();
		for (int i = 0; i < mapLen; i++) {
			int key = buffer.readByte();
			HeroAndBuff value = new HeroAndBuff();
			value.readBuffer(buffer);
			this.lineupHeros.put(key, value);
		}
		
		this.lineupEquips = new HashMap<>();
		mapLen = buffer.readShort();
		for (int i = 0; i < mapLen; i++) {
			int key = buffer.readByte();
			List<EquipVO> list = new ArrayList<>();
			short listLen = buffer.readShort();
			for (int j = 0; j < listLen; j++) {
				EquipVO equipVO = new EquipVO();
				equipVO.readBuffer(buffer);
				list.add(equipVO);
			}
			this.lineupEquips.put(key, list);
		}
	}
	
	@Override
	public void write() {
		this.writeShort((short) this.lineupHeros.size());
		for (Entry<Integer, HeroAndBuff> entry : this.lineupHeros.entrySet()) {
			this.writeByte(entry.getKey().byteValue());
			this.writeBytes(entry.getValue().getBytes());
		}
		this.writeShort((short) this.lineupEquips.size());
		for (Entry<Integer, List<EquipVO>> entry : this.lineupEquips.entrySet()) {
			this.writeByte(entry.getKey().byteValue());
			List<EquipVO> list = entry.getValue();
			this.writeShort((short) list.size());
			for (EquipVO equipVO : list) {
				this.writeBytes(equipVO.getBytes());
			}
		}
	}
}

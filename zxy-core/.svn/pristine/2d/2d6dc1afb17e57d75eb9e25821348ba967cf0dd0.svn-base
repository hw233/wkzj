package com.jiatang.common.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 
 * @author vinceruan
 *
 */
public class LineupFightModel extends IoBufferSerializer{

	/**
	 * 阵型上面的英雄信息
	 */
	private Map<Integer, HeroVO> heros ;
	
	/**
	 * 阵型带来的属性加成(装备、技能、阵法加成)
	 */
	private Map<Long, Map<AttackerAttributeKey, Integer>> attributeChanges ;
	
	
	public LineupFightModel(){
		heros = new TreeMap<>();
		attributeChanges = new HashMap<>();
	}
	
	public LineupFightModel(byte[] bytes) {
		setReadBuffer(bytes);
	}
	
	@Override
	protected void read() {
		short heroLen = readShort();
		heros = new TreeMap<>();
		for (int i = 0; i < heroLen; i++) {
			int key = readInt();
			HeroVO heroVO = new HeroVO();
			heroVO.readBuffer(this);
			heros.put(key, heroVO);
		}
		short attsLen = readShort();
		attributeChanges = new HashMap<>();
		for (int i = 0; i < attsLen; i++) {
			long key = readLong();
			Map<AttackerAttributeKey, Integer> values = null;
			if (attributeChanges.containsKey(key)) {
				values = attributeChanges.get(key);
			} else {
				values = new HashMap<AttackerAttributeKey, Integer>();
				attributeChanges.put(key, values);
			}
			short attLen = readShort();
			for (int j = 0; j < attLen; j++) {
				AttackerAttributeKey attackerAttributeKey = AttackerAttributeKey.getByCode(readByte());
				int value = readInt();
				values.put(attackerAttributeKey, value);
			}
		}
	}

	@Override
	public void write() {
		this.writeShort((short)this.heros.size());
		for (Map.Entry<Integer, HeroVO> entry : heros.entrySet()) {
			this.writeInt(entry.getKey());
			this.writeBytes(entry.getValue().getBytes());
		}
		this.writeShort((short)this.attributeChanges.size());
		for (Map.Entry<Long, Map<AttackerAttributeKey, Integer>> entry : attributeChanges.entrySet()) {
			this.writeLong(entry.getKey());
			Map<AttackerAttributeKey, Integer> atts = entry.getValue();
			this.writeShort((short)atts.size());
			for (Map.Entry<AttackerAttributeKey, Integer> att : atts.entrySet()) {
				this.writeByte(att.getKey().getCode());
				this.writeInt(att.getValue());
			}
		}
	}
	
	public void setSpriteId(List<Long> id) {
		//key:oldKey value:newKey
		Map<Long, Long> change = new HashMap<>();
		int i = 0;
		for (HeroVO heroVO : heros.values()) {
			long newId = id.get(i);
			long oldId = heroVO.getSpriteId();
			heroVO.setSpriteId(newId);
			change.put(oldId, newId);
			i++;
		}
		
		Map<Long, Map<AttackerAttributeKey, Integer>> newAttributeChanges = new HashMap<>();
		for (Entry<Long, Map<AttackerAttributeKey, Integer>> att : attributeChanges.entrySet()) {
			long key = att.getKey();
			Map<AttackerAttributeKey, Integer> map = att.getValue();
			long newKey = change.get(key);
			newAttributeChanges.put(newKey, map);
		}
		this.attributeChanges = newAttributeChanges;
	}

	public Map<Integer, HeroVO> getHeros() {
		return heros;
	}

	public void setHeros(Map<Integer, HeroVO> heros) {
		this.heros = heros;
	}

	public Map<Long, Map<AttackerAttributeKey, Integer>> getAttributeChanges() {
		return attributeChanges;
	}

	public void setAttributeChanges(
			Map<Long, Map<AttackerAttributeKey, Integer>> attributeChanges) {
		this.attributeChanges = attributeChanges;
	}
	
	
	
}

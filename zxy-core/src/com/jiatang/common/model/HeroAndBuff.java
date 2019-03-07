package com.jiatang.common.model;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 仙人和buffer信息
 * @author vinceruan
 *
 */
public class HeroAndBuff extends IoBufferSerializer{
	
	 public HeroVO hero;
	 public List<BufferVO> buffList;
	
	public static HeroAndBuff valueOf(HeroVO hero, List<BufferVO> buffList) {
		HeroAndBuff heroRes = new HeroAndBuff();
		heroRes.hero = hero;
		heroRes.buffList = buffList;
		if (heroRes.buffList == null) {
			heroRes.buffList = new ArrayList<>();
		}
		return heroRes;
	}

	private void writeBufferList() {
		this.writeShort((short) buffList.size());
		for (BufferVO buf : buffList) {
//			this.writeByte(buf.key.getCode());
//			this.writeInt(buf.addVal);
//			this.writeInt(buf.sourceType);
			this.writeBytes(buf.getBytes());
		}
	}
	
	private void writeHero() {
//		this.writeInt(hero.heroId);
//		this.writeInt(hero.level);
//		this.writeInt(hero.atk);
//		this.writeInt(hero.defense);
//		this.writeInt(hero.hp);
//		this.writeInt(hero.exp);
//		this.writeInt(hero.atkScope);
//		this.writeInt(hero.skillId);
//		this.writeIntList(hero.passiveSkillList);
//		this.writeInt(hero.availableDelveCount);
//		this.writeInt(hero.usedDelveCount);
//		this.writeInt(hero.breakThroughCount);
//		this.writeInt(hero.allowReDelve());  //0 是不允许  1是允许
		this.writeBytes(this.hero.getBytes());
	}

	@Override
	public void write() {
		writeHero();
		writeBufferList();
	}
	
	@Override
	public void readBuffer(IoBufferSerializer buffer) {
		this.hero = new HeroVO();
		hero.readBuffer(buffer);
		buffList = new ArrayList<>();
		short len = buffer.readShort();
		for (int i = 0; i < len; i++) {
			BufferVO bufferVO = new BufferVO();
			bufferVO.readBuffer(buffer);;
			buffList.add(bufferVO);
		}
	}
}

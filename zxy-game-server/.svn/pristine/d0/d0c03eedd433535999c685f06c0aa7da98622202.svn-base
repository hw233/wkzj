package com.jtang.gameserver.module.hero.handler.response;


import java.util.ArrayList;
import java.util.List;

import com.jiatang.common.model.BufferVO;
import com.jiatang.common.model.HeroVO;
import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 仙人和buffer信息
 * @author vinceruan
 *
 */
public class HeroResponse extends IoBufferSerializer{
	
	 public HeroVO hero;
	 public List<BufferVO> buffList;
	
	public static HeroResponse valueOf(HeroVO hero, List<BufferVO> buffList) {
		HeroResponse heroRes = new HeroResponse();
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
			this.writeByte(buf.key.getCode());
			this.writeInt(buf.addVal);
		}
	}
	
	private void writeHero() {
		this.writeInt(hero.heroId);
		this.writeInt(hero.level);
		this.writeInt(hero.atk);
		this.writeInt(hero.defense);
		this.writeInt(hero.hp);
		this.writeInt(hero.exp);
		this.writeInt(hero.atkScope);
		this.writeInt(hero.skillId);
		this.writeIntList(hero.passiveSkillList);
		this.writeInt(hero.availableDelveCount);
		this.writeInt(hero.usedDelveCount);
		this.writeInt(hero.breakThroughCount);
		this.writeInt(hero.allowReDelve());  //0 是不允许  1是允许
		this.writeLong(hero.delveCostGold);  //0 是不允许  1是允许
		this.writeInt(hero.delveStoneNum);  //0 是不允许  1是允许
	}

	@Override
	public void write() {
		writeHero();
		writeBufferList();
	}
}

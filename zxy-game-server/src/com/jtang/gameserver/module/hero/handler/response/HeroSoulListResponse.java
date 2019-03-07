package com.jtang.gameserver.module.hero.handler.response;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.hero.model.HeroSoulVO;

/**
 * 返回魂魄列表
 * @author vinceruan
 *
 */
public class HeroSoulListResponse extends IoBufferSerializer {
	/**
	 * 魂魄列表
	 */
	public List<HeroSoulVO> heroSoulList;
	

	@Override
	public void write() {
		writeShort((short) heroSoulList.size());
		for (HeroSoulVO h : heroSoulList) {
			writeInt(h.heroId);
			writeInt(h.count);
		}
	}
	
	public static HeroSoulListResponse valueOf(List<HeroSoulVO> list) {
		HeroSoulListResponse res = new HeroSoulListResponse();
		res.heroSoulList = list;
		return res;
	}
	
	public static HeroSoulListResponse valueOf(HeroSoulVO hs) {
		List<HeroSoulVO> list = new ArrayList<>();
		list.add(hs);
		return valueOf(list);
	}
}

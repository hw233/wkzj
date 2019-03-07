package com.jtang.gameserver.module.hero.handler.response;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 仙人删除列表推送
 * @author 0x737263
 *
 */
public class HeroRemoveListResponse extends IoBufferSerializer {
	
	/**
	 * 删除仙人id列表
	 */
	List<Integer> heroIdList;
		
	@Override
	public void write() {
		writeShort((short) heroIdList.size());
		for (int heroId : heroIdList) {
			writeInt(heroId);
		}
	}
	
	public static HeroRemoveListResponse valueOf(int heroId) {
		HeroRemoveListResponse response = new HeroRemoveListResponse();
		response.heroIdList = new ArrayList<>();
		response.heroIdList.add(heroId);
		return response;
	}
	
	public static HeroRemoveListResponse valueOf(List<Integer> heroIdList) {
		HeroRemoveListResponse response = new HeroRemoveListResponse();
		response.heroIdList = heroIdList;
		return response;
	}
	
}

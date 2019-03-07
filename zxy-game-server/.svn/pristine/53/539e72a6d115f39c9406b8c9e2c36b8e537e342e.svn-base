package com.jtang.gameserver.module.hero.handler.response;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.jiatang.common.model.BufferVO;
import com.jiatang.common.model.HeroVO;
import com.jtang.core.protocol.IoBufferSerializer;

/**
 * 返回仙人列表
 * @author vinceruan
 *
 */
public class HeroListResponse extends IoBufferSerializer {
	/**
	 * 仙人列表
	 */
	List<HeroResponse> heroList;
	
	/**
	 * 仙人合成次数
	 */
	private int composeNum;
		
	@Override
	public void write() {
		writeShort((short) heroList.size());
		for (HeroResponse rsp : heroList) {
			writeBytes(rsp.getBytes());
		}
		writeInt(composeNum);
	}
	
	public static HeroListResponse valueOf(List<HeroResponse> list, int composeNum) {
		HeroListResponse res = new HeroListResponse();
		res.heroList  = list;
		res.composeNum = composeNum;
		return res;
	}
	
	public static HeroListResponse valueOfList(List<HeroVO> heroVOList, int composeNum) {
		HeroListResponse res = new HeroListResponse();
		res.heroList = new ArrayList<>();
		for (HeroVO vo : heroVOList) {
			res.heroList.add(HeroResponse.valueOf(vo, new LinkedList<BufferVO>()));
		}
		res.composeNum = composeNum;
		return res;
	}
	
	public static HeroListResponse valueOf(HeroVO hero, List<BufferVO> bufferList, int composeNum) {
		List<HeroResponse> list = new ArrayList<>();
		HeroResponse res = HeroResponse.valueOf(hero, bufferList);
		list.add(res);
		return valueOf(list, composeNum);
	}
}

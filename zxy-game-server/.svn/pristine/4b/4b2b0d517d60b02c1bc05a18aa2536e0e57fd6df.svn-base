package com.jtang.gameserver.module.extapp.deitydesc.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.dataconfig.service.DeityDescendService;

public class DeityDescendVO extends IoBufferSerializer implements Serializable {

	private static final long serialVersionUID = -8481128897791508083L;

	/**
	 * 当前仙人id
	 */
	public int heroId;
	
	/**
	 * 当前仙人需要点亮的字[1,4] 
	 */
	public byte curIndex;
	
	/**
	 * 当前仙人总共砸了多少次
	 */
	public int totalHit;
	
	/**
	 * 总字段数
	 */
	private static final int COLUMN_NUM = 3;
	
	public static DeityDescendVO valueOf(String[] param) {
		DeityDescendVO vo = new DeityDescendVO();
		param = StringUtils.fillStringArray(param, COLUMN_NUM, "0");
		vo.heroId = Integer.valueOf(param[0]);
		vo.curIndex = Byte.valueOf(param[1]);
		vo.totalHit = Integer.valueOf(param[2]);
		return vo;
	}
	
	public static DeityDescendVO valueOf(int heroId) {
		DeityDescendVO vo = new DeityDescendVO();
		vo.heroId = heroId;
		vo.curIndex = 0;
		vo.totalHit = 0;
		return vo;
	}
	
	@Override
	public void write() {
		writeInt(heroId);
		writeByte(curIndex);
		writeInt(totalHit);
	}

	/**
	 * EquipVO转Blob String
	 * @return
	 */
	public String parse2String() {
		List<Object> list = new ArrayList<Object>();
		list.add(heroId);
		list.add(curIndex);
		list.add(totalHit);
		return StringUtils.collection2SplitString(list, Splitable.ATTRIBUTE_SPLIT);
	}
	
	
	public boolean isAllLighted() {
		if (this.curIndex >= DeityDescendService.MAX_CHAR_NUM) {
			return true;
		}
		return false;
	}
}

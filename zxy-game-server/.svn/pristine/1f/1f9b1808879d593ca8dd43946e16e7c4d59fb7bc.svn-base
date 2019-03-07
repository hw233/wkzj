package com.jtang.gameserver.module.app.model.extension.rulevo;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;

public class ErnieVO {

	/**
	 * 奖励物品id
	 */
	public int goodsId;
	
	/**
	 * 角色id
	 */
	public long actorId;
	
	/**
	 * 联系电话
	 */
	public long phoneNum;
	
	public String parse2String() {
		List<Object> list = new ArrayList<>();
		list.add(goodsId);
		list.add(actorId);
		list.add(phoneNum);
		return StringUtils.collection2SplitString(list, Splitable.ATTRIBUTE_SPLIT);
	}
	
	public ErnieVO() {
		
	}
	
	public ErnieVO(String[] items) {
		items = StringUtils.fillStringArray(items, 3, "0");
		this.goodsId = Integer.valueOf(items[0]);
		this.actorId = Long.valueOf(items[1]);
		this.phoneNum = Long.valueOf(items[2]);
	}
	
	@Override
	public String toString() {
		return this.parse2String();
	}
}

package com.jtang.gameserver.module.notify.model.impl;

import java.util.List;

import com.jtang.gameserver.module.notify.model.AbstractNotifyVO;

/**
 * vip赠送装备(天材地宝)
 * @author 0x737263
 *
 */
public class GiveEquipNotifyVO extends AbstractNotifyVO {
	private static final long serialVersionUID = 9116881336551613759L;

	/**
	 * 装备id
	 */
	public int equipId;
	
	/**
	 * 装备数量
	 */
	public int equipNum;
	
	/**
	 * 是否领取，0未领取，1为已领取
	 */
	public byte isGet;
	
	public GiveEquipNotifyVO() {
		
	}
	
	public GiveEquipNotifyVO(int equipId, int equipNum, byte isGet) {
		this.equipId = equipId;
		this.equipNum = equipNum;
		this.isGet = isGet;
	}

	@Override
	protected void subClazzWrite() {
		writeInt(equipId);
		writeInt(equipNum);
		writeByte(isGet);
	}

	@Override
	protected void subClazzString2VO(String[] items) {
		this.equipId = Integer.valueOf(items[0]);
		this.equipNum = Integer.valueOf(items[1]);
		this.isGet = Byte.valueOf(items[2]);
	}

	@Override
	protected void subClazzParse2String(List<String> attributes) {
		attributes.add(String.valueOf(equipId));
		attributes.add(String.valueOf(equipNum));
		attributes.add(String.valueOf(isGet));
	}
}

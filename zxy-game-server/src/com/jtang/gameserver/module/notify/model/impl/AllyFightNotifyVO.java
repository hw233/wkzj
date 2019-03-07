package com.jtang.gameserver.module.notify.model.impl;

import java.util.List;

import com.jtang.gameserver.module.notify.model.AbstractNotifyVO;

/**
 * 盟友切磋的信息通知，切磋双方将收到此信息
 * @author pengzy
 *
 */
public class AllyFightNotifyVO extends AbstractNotifyVO {
	private static final long serialVersionUID = 5734586419506863559L;

	/**
	 * 切磋类型(1.切磋    2.被切磋)
	 */
	public byte attackType;
	
	/**
	 * 奖励的气势值
	 */
	public int morale;
	
	/**
	 * 胜利数
	 */
	public int successNum;
	
	/**
	 * 失败数
	 */
	public int failNum;
	
	public AllyFightNotifyVO() {
		
	}
	
	public AllyFightNotifyVO(byte attackType, int morale, int successNum, int failNum) {
		this.attackType = attackType;
		this.morale = morale;
		this.successNum = successNum;
		this.failNum = failNum;
	}
	

	@Override
	protected void subClazzWrite() {
		writeByte(attackType);
		writeInt(morale);
		writeInt(successNum);
		writeInt(failNum);
	}

	@Override
	protected void subClazzString2VO(String[] subItems) {
		this.attackType = Byte.valueOf(subItems[0]);
		this.morale = Integer.valueOf(subItems[1]);
		this.successNum = Integer.valueOf(subItems[2]);
		this.failNum = Integer.valueOf(subItems[3]);
	}

	@Override
	protected void subClazzParse2String(List<String> attributes) {
		attributes.add(String.valueOf(attackType));
		attributes.add(String.valueOf(morale));
		attributes.add(String.valueOf(successNum));
		attributes.add(String.valueOf(failNum));
	}
	
}

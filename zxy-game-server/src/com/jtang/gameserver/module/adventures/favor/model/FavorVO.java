package com.jtang.gameserver.module.adventures.favor.model;

import java.util.Collection;

import com.jtang.core.protocol.IoBufferSerializer;

public class FavorVO extends IoBufferSerializer{

	/**
	 * 倒计时(utc秒)
	 */
	private int countDownTime;
	
	/**
	 * 是否是持久（0非持久，1是持久）
	 */
	private int isPermanent;
	
	
	/**
	 * 特权列表
	 */
	private Collection<PrivilegeVO> privilegeList;
	
	public static FavorVO valueOf(int countDownTime, Collection<PrivilegeVO> privilegeList, int isPermanent) {
		FavorVO favorVO = new FavorVO();
		favorVO.countDownTime = countDownTime;
		favorVO.privilegeList = privilegeList;
		favorVO.isPermanent = isPermanent;
		return favorVO;
	}

	@Override
	public void write() {
		this.writeInt(countDownTime);
		this.writeInt(isPermanent);
		this.writeShort((short) (privilegeList.size()));
		for (PrivilegeVO privilegeVO : privilegeList) {
			this.writeBytes(privilegeVO.getBytes());
		}
	}


}

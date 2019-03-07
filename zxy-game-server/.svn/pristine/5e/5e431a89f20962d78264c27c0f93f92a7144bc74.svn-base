package com.jtang.gameserver.module.notify.model.impl;

import java.util.List;

import com.jtang.gameserver.module.notify.model.AbstractNotifyVO;

/**
 * 势力排行挑战的信息结构
 * @author pengzy
 *
 */
public class PowerNotifyVO extends AbstractNotifyVO {
	private static final long serialVersionUID = -5221843319742790021L;

	/**
	 * 1-主攻；2-被攻
	 */
	public byte attackType;
	
	/**
	 * 只有当battleResult为胜利时，此值才有意义
	 * 是否成功占领了挑战的排行，1为true，0为false
	 */
	public byte isCaptureRankSucess;
	
	/**
	 * 挑战者的新排名
	 */
	public int rank;
	
	/**
	 * 被挑战者的新排名
	 */
	public int targetRank;
	
	public PowerNotifyVO() {
		
	}
	
	public PowerNotifyVO(byte attackType, int isCaptureRankSucess, int rank, int targetBank) {
		this.attackType = attackType;
		this.isCaptureRankSucess = (byte)isCaptureRankSucess;
		this.rank = rank;
		this.targetRank = targetBank;
	}

	@Override
	protected void subClazzWrite() {
		writeByte(attackType);
		writeByte(isCaptureRankSucess);
		writeInt(rank);
		writeInt(targetRank);
	}

	@Override
	protected void subClazzString2VO(String[] items) {
		attackType = Byte.valueOf(items[0]);
		isCaptureRankSucess = Byte.valueOf(items[1]);
		rank = Integer.valueOf(items[2]);
		targetRank = Integer.valueOf(items[3]);
	}

	@Override
	protected void subClazzParse2String(List<String> attributes) {
		attributes.add(String.valueOf(attackType));
		attributes.add(String.valueOf(isCaptureRankSucess));
		attributes.add(String.valueOf(rank));
		attributes.add(String.valueOf(targetRank));
		
	}
}

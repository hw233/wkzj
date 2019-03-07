package com.jiatang.common.crossbattle.model;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.model.RewardObject;
import com.jtang.core.protocol.IoBufferSerializer;

public class DayEndRewardVO extends IoBufferSerializer{
	/**
	 * 服务器积分
	 */
	private int score;
	
	/**
	 * 总伤害
	 */
	private long totalHurt;
	
	/**
	 * 本服伤害排名
	 */
	private int rank;
	
	/**
	 * 本服排名贡献点
	 */
	private int rankPoint;
	
	/**
	 * 杀人数
	 */
	private int killNum;
	
	/**
	 * 杀人贡献点
	 */
	private int killPoint;
	
	/**
	 * 服务器获胜贡献点
	 */
	private int winServerExtPoint;
	
	/**
	 * 服务器获胜奖励
	 */
	public List<RewardObject> winServerExtGoods;
	
	/**
	 * 服务器胜负标记
	 * 1：失败
	 * 2：平局
	 * 3：胜利
	 */
	public int serverWinFlag;
	
	public DayEndRewardVO(int score, long totalHurt, int rank,
			int rankPoint, int killNum, int killPoint, int winServerExtPoint,
			List<RewardObject> winServerExtGoods, int serverWinFlag) {
		super();
		this.score = score;
		this.totalHurt = totalHurt;
		this.rank = rank;
		this.rankPoint = rankPoint;
		this.killNum = killNum;
		this.killPoint = killPoint;
		this.winServerExtPoint = winServerExtPoint;
		this.winServerExtGoods = winServerExtGoods;
		this.serverWinFlag = serverWinFlag;
	}


	public DayEndRewardVO() {
	}


	@Override
	public void write() {
		this.writeInt(this.score);
		this.writeLong(this.totalHurt);
		this.writeInt(this.rank);
		this.writeInt(this.rankPoint);
		this.writeInt(this.killNum);
		this.writeInt(this.killPoint);
		this.writeInt(this.winServerExtPoint);
		this.writeShort((short) this.winServerExtGoods.size());
		for (RewardObject rewardObject : this.winServerExtGoods) {
			this.writeBytes(rewardObject.getBytes());
		}
		this.writeInt(this.serverWinFlag);
	}
	
	@Override
	public void readBuffer(IoBufferSerializer buffer) {
		this.score = buffer.readInt();
		this.totalHurt = buffer.readLong();
		this.rank = buffer.readInt();
		this.rankPoint = buffer.readInt();
		this.killNum = buffer.readInt();
		this.killPoint = buffer.readInt();
		this.winServerExtPoint = buffer.readInt();
		short len = buffer.readShort();
		this.winServerExtGoods = new ArrayList<>();
		for (int i = 0; i < len; i++) {
			RewardObject rewardObject = new RewardObject();
			rewardObject.readBuffer(buffer);;
			this.winServerExtGoods.add(rewardObject);
		}
		this.serverWinFlag = buffer.readInt();
	}

	
}

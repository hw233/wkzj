package com.jtang.gameserver.module.demon.handler.response;

import java.util.List;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.demon.model.DemonVO;
/**
 * 阵营数据回复
 * @author ludd
 *
 */
public class DemonCampDataResponse extends IoBufferSerializer {
	/**
	 * 阵营数据
	 */
	private List<DemonVO> demons;
	/**
	 * boss当前血
	 */
	private int bossHp;
	
	/**
	 * boss最大血
	 */
	private int bossHpMax;
	
	/**
	 * 难度
	 */
	private int difficult;
	/**
	 * bossId
	 */
	private int bossId;
	
	/**
	 * 攻击玩家次数
	 */
	private int attackPlayerNum;
	
	/**
	 * 攻击boss倒计时
	 */
	private int time;
	
	
	public DemonCampDataResponse(List<DemonVO> demons, int bossHp, int bossHpMax, int difficult, int bossId, int attackPlayerNum, int time) {
		super();
		this.demons = demons;
		this.bossHp = bossHp;
		this.bossHpMax = bossHpMax;
		this.difficult = difficult;
		this.bossId = bossId;
		this.attackPlayerNum = attackPlayerNum;
		this.time = time;
	}


	@Override
	public void write() {
		this.writeShort((short)demons.size());
		for (DemonVO demonVO : demons) {
			this.writeBytes(demonVO.getBytes());
		}
		
		this.writeInt(this.bossHp);
		this.writeInt(this.bossHpMax);
		this.writeInt(this.difficult);
		this.writeInt(this.bossId);
		this.writeInt(this.attackPlayerNum);
		this.writeInt(this.time);
	}

}

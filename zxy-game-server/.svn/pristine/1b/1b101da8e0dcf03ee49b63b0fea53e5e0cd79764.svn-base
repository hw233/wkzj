package com.jtang.gameserver.module.user.handler.response;

import java.util.Map;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.dbproxy.entity.Actor;

/**
 * 角色登陆信息
 * @author 0x737263
 *
 */
public class ActorLoginResponse extends IoBufferSerializer {
	
	/**
	 * 角色id
	 */
	public long actorId;
	
	/**
	 * 角色名
	 */
	public String actorName;
	
	/**
	 * 角色等级
	 */
	public int level;
	
	/**
	 * 角色声望(经验值)
	 */
	public long reputation;
	
	/**
	 * 金币
	 */
	public long gold;
	
	/**
	 * 点券数
	 */
	public int ticket;
	
	/**
	 * 精力
	 */
	public int energy;
	
	/**
	 * 最大精力值
	 */
	public int maxEnergy;
	
	/**
	 * 精力倒计时(秒)
	 */
	public int energyCountdown;
	
	/**
	 * 活力值 
	 */
	public int vit;
	
	/**
	 * 最大活力值
	 */
	public int maxVit;
	
	/**
	 * 活力倒计时(秒)
	 */
	public int vitCountdown;
	
	/**
	 * 气势值
	 */
	public int morale;
	
	/**
	 * 新手引导存储区
	 */
	public Map<Integer,Integer> guideMap;
	
	/**
	 * vip等级
	 */
	public int vipLevel;
	
	/**
	 * 充值所得点券累计值
	 */
	public int rechargeTicketNum;
	
	/**
	 * 创建角色时的渠道id
	 */
	public int channelId;
	
	/**
	 * 角色修改名称次数
	 */
	public int renameNum;
	
	/**
	 * 角色补充精力次数
	 */
	public int energyNum;
	
	/**
	 * 角色补充活力次数
	 */
	public int vitNum;
	
	public ActorLoginResponse(Actor actor, int ticket, int rechargeTicketNum, int vipLevel) {
		this.actorId = actor.getPkId();
		this.actorName = actor.actorName;
		this.level = actor.level;
		this.reputation = actor.reputation;
		this.gold = actor.gold;
		this.ticket = ticket;
		this.energy = actor.energy;
		this.maxEnergy = actor.maxEnergy;
		this.energyCountdown = actor.getEnergyCountdown();
		this.vit = actor.vit;
		this.maxVit = actor.maxVit;
		this.vitCountdown = actor.getVitCountdown();
		this.morale = actor.morale;
		this.guideMap = actor.getGuideMap();
		this.vipLevel = vipLevel;
		this.rechargeTicketNum = rechargeTicketNum;
		this.channelId = actor.channelId;
		this.renameNum = actor.renameNum;
		this.energyNum = actor.energyNum;
		this.vitNum = actor.vitNum;
	}

	@Override
	public void write() {
		writeLong(actorId);
		writeString(actorName);
		writeInt(level);
		writeLong(reputation);
		writeLong(gold);
		writeInt(ticket);
		writeInt(energy);
		writeInt(maxEnergy);
		writeInt(energyCountdown);
		writeInt(vit);
		writeInt(maxVit);
		writeInt(vitCountdown);
		writeInt(morale);
		writeIntMap(guideMap);
		writeInt(vipLevel);
		writeInt(rechargeTicketNum);
		writeInt(channelId);
		writeInt(renameNum);
		writeInt(energyNum);
		writeInt(vitNum);
	}
	
}

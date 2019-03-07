package com.jtang.gameserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;
import com.jtang.gameserver.module.user.constant.ActorRule;

/**
 * vip充值表
 * <pre>
 * 
 * --以下为db说明---------------------------
 * 主键为actorId,每个角色仅有一行记录
 * </pre>
 * @author 0x737263
 *
 */
@TableName(name="vip", type = DBQueueType.NONE)
public class Vip extends Entity<Long> {
	private static final long serialVersionUID = -1095662411812737867L;

	/**
	 * 角色id
	 */
	@Column(pk = true)
	public long actorId;

	/**
	 * 充值次数(第几次充值)
	 */
	@Column
	public int rechargeNum;
	
	/**
	 * 当前赠送的剩余点券数(有游戏内送的,或充值附赠的)
	 */
	@Column
	public int giveTicket;
	
	/**
	 * 当前充入剩余点券数
	 */
	@Column
	public int ticket;
	
	/**
	 * 累计充入点券数
	 */
	@Column
	public int totalTicket;
	
	/**
	 * vip等级.默认为0
	 */
	@Column
	public int vipLevel;
	
	/**
	 * 首次充值时间
	 */
	@Column
	public int firstRechargeTime;
	
	/**
	 * 最后一次充值时间(每次充值时刷新该时间)
	 */
	@Column
	public int lastRechargeTime;
	
	/**
	 * 累计赠送的
	 */
	@Column
	public int totalGive;

	@Override
	public Long getPkId() {
		return this.actorId;
	}

	@Override
	public void setPkId(Long pk) {
		this.actorId = pk;
	}
	
	public static Vip valueOf(long actorId){
		Vip vip = new Vip();
		vip.actorId = actorId;
		vip.giveTicket =  ActorRule.ACTOR_INIT_TICKET;
		return vip;
	}

	@Override
	protected Entity<Long> readData(ResultSet rs, int rowNum) throws SQLException {
		Vip vip = new Vip();
		vip.actorId = rs.getLong("actorId");
		vip.rechargeNum = rs.getInt("rechargeNum");
		vip.giveTicket = rs.getInt("giveTicket");
		vip.ticket = rs.getInt("ticket");
		vip.totalTicket = rs.getInt("totalTicket");
		vip.vipLevel = rs.getInt("vipLevel");
		vip.firstRechargeTime = rs.getInt("firstRechargeTime");
		vip.lastRechargeTime = rs.getInt("lastRechargeTime");
		vip.totalGive = rs.getInt("totalGive");
		return vip;
	}

	@Override
	protected void hasReadEvent() {
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> value = new ArrayList<>();
		if (containsPK){
		    value.add(this.actorId);
		}
	    value.add(this.rechargeNum);
	    value.add(this.giveTicket);
	    value.add(this.ticket);
	    value.add(this.totalTicket);
	    value.add(this.vipLevel);
	    value.add(this.firstRechargeTime);
	    value.add(this.lastRechargeTime);
	    value.add(this.totalGive);
		return value;
	}

	@Override
	protected void beforeWritingEvent() {
	}

	@Override
	protected void disposeBlob() {
		// TODO Auto-generated method stub
		
	}

}

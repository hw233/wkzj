package com.jtang.gameserver.dbproxy.entity;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;

@TableName(name="rechargeLog", type = DBQueueType.NONE)
public class RechargeLog extends Entity<Long> {
	private static final long serialVersionUID = -3832794256740740449L;

	/**
	 * 自增id
	 */
	@Column(pk = true)
	private long id;
	
	/**
	 * 内部流水号
	 */
	@Column
	public String orderSnid;
	
	/**
	 * 平台id
	 */
	@Column
	public int platformId;
	
	/**
	 * 游戏服id
	 */
	@Column
	public int serverId;
	
	/**
	 * 平台uid
	 */
	@Column
	public  String uid;
	
	/**
	 * 角色id
	 */
	@Column
	public long actorId;
	
	/**
	 * 支付渠道ID
	 */
	@Column
	public int payWayId;
	
	/**
	 * 各合作渠道的交易号
	 */
	@Column
	public String tradeSnid;
	
	/**
	 * 购买物品ID
	 */
	@Column
	public int goodsId;
	
	/**
	 * 购买数量
	 */
	@Column
	public int buyCount;
	
	/**
	 * 折扣
	 */
	@Column
	public int discount;
	
	/**
	 * 支付金额
	 */
	@Column
	public BigDecimal payMoney;
	
	/**
	 * 媒体id
	 */
	@Column
	public int mediaId;
	/**
	 * 渠道ID
	 */
	@Column
	public int channelId;
	
	/**
	 * 充值时间（秒）
	 */
	@Column
	public int rechargeTime;

	@Override
	public Long getPkId() {
		return this.id;
	}

	@Override
	public void setPkId(Long pk) {
		this.id = pk;
	}

	public RechargeLog() {
	}

	public RechargeLog(String orderSnid, int platformId, int serverId, String uid,long actorId, int payWayId, String tradeSnid, int goodsId, int buyCount,
			byte discount, BigDecimal payMoney, int mediaId, int channelId, int rechargeTime) {
		super();
		this.orderSnid = orderSnid;
		this.platformId = platformId;
		this.serverId = serverId;
		this.uid = uid;
		this.actorId = actorId;
		this.payWayId = payWayId;
		this.tradeSnid = tradeSnid;
		this.goodsId = goodsId;
		this.buyCount = buyCount;
		this.discount = discount;
		this.payMoney = payMoney;
		this.mediaId = mediaId;
		this.channelId = channelId;
		this.rechargeTime = rechargeTime;
	}

	@Override
	protected Entity<Long> readData(ResultSet rs, int rowNum) throws SQLException {
		RechargeLog order = new RechargeLog();
		order.id = rs.getLong("id");
		order.orderSnid = rs.getString("orderSnid");
		order.platformId = rs.getInt("platformId");
		order.serverId = rs.getInt("serverId");
		order.uid = rs.getString("uid");
		order.actorId = rs.getLong("actorId");
		order.payWayId = rs.getInt("payWayId");
		order.tradeSnid = rs.getString("tradeSnid");
		order.goodsId = rs.getInt("goodsId");
		order.buyCount = rs.getInt("buyCount");
		order.discount = rs.getByte("discount");
		order.payMoney = rs.getBigDecimal("payMoney");
		order.mediaId = rs.getInt("mediaId");
		order.channelId = rs.getInt("channelId");
		order.rechargeTime = rs.getInt("rechargeTime");
		return order;
	}

	@Override
	protected void hasReadEvent() {
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> value = new ArrayList<>();
		if (containsPK) {
			value.add(this.id);
		}
		value.add(this.orderSnid);
		value.add(this.platformId);
		value.add(this.serverId);
		value.add(this.uid);
		value.add(this.actorId);
		value.add(this.payWayId);
		value.add(this.tradeSnid);
		value.add(this.goodsId);
		value.add(this.buyCount);
		value.add(this.discount);
		value.add(this.payMoney);
		value.add(this.mediaId);
		value.add(this.channelId);
		value.add(this.rechargeTime);
		return value;
	}

	@Override
	protected void beforeWritingEvent() {
	}
	
	/**
	 * platformId-serverId-uid
	 * @return
	 */
	public String getKey() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.platformId);
		sb.append(this.serverId);
		sb.append(this.uid);
		return sb.toString();
	}

	@Override
	protected void disposeBlob() {
		// TODO Auto-generated method stub
		
	}

}

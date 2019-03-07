package com.jtang.gameserver.admin.handler.request;

import java.math.BigDecimal;

import com.jtang.core.protocol.IoBufferSerializer;
/**
 * 充值请求协议
 * @author ludd
 *
 */
public class RechargeRequest extends IoBufferSerializer {

	/**
	 * 内部流水号
	 */
	public String orderSnid;
	/**
	 * 平台id
	 */
	public int platformId;
	/**
	 * 游戏服id
	 */
	public int serverId;
	/**
	 * 平台uid
	 */
	public  String uid;
	/**
	 * 支付渠道ID
	 */
	public int payWayId;
	/**
	 * 各合作渠道的交易号
	 */
	public String tradeSnid;
	/**
	 * 购买物品ID
	 */
	public int rechargeId;
	/**
	 * 购买数量
	 */
	public int buyCount;
	
	/**
	 * 折扣
	 */
	public byte discount;
	
	/**
	 * 支付金额
	 */
	public BigDecimal payMoney;
		
	/**
	 * 赠送点券数
	 */
	public int sendCount;
	
	/**
	 * 媒体id
	 */
	public int mediaId;
	/**
	 * 渠道ID
	 */
	public int channelId;
	/**
	 * 充值时间（自1970到现在的秒数）
	 */
	public int rechargeTime;
	/**
	 * 角色id
	 */
	public long actorId;
	
	public RechargeRequest(byte[] bytes) {
		super(bytes);
	}
	
	@Override
	public void read() {
		this.orderSnid = readString().trim();
		this.platformId = readInt();
		this.serverId = readInt();
		this.uid = readString().trim();
		this.payWayId = readInt();
		this.tradeSnid = readString().trim();
		this.rechargeId = readInt();
		this.buyCount = readInt();
		this.discount = readByte();
		String str = readString().trim();
		this.payMoney = new BigDecimal(str);
		this.sendCount = readInt();
		this.mediaId = readInt();
		this.channelId = readInt();
		this.rechargeTime = readInt();
		this.actorId = readLong();
	}
	
	public static RechargeRequest valueOf(byte[] bytes) {
		return new RechargeRequest(bytes);
	}

}

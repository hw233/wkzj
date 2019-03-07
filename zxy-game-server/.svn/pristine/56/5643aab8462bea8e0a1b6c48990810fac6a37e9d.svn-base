package com.jtang.gameserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;
import com.jtang.core.utility.TimeUtils;

/**
 * 角色在线表
 * <pre>
 * 登陆时写一条数据。退出时删除该条数据
 * --以下为db说明---------------------------
 * 主键为actorId,每个角色仅有一行记录
 * </pre>
 * @author 0x737263
 *
 */
@TableName(name = "online", type = DBQueueType.NONE)
public class Online extends Entity<Long> {
	private static final long serialVersionUID = 8384680935853290004L;

	/**
	 * 角色id
	 */
	@Column(pk = true)
	private long actorId;
	
	/**
	 * 平台类型id
	 */
	@Column
	public int platformType;

	/**
	 * 游戏服id
	 */
	@Column
	public int serverId;
	
	/**
	 * 平台uid
	 */
	@Column
	public String uid;
	
	/**
	 * 创建角色的来源渠道id
	 */
	@Column
	public int channelId;
	
	/**
	 * sim信息
	 */
	@Column
	public String sim;

	/**
	 * mac地址
	 */
	@Column
	public String mac;

	/**
	 * imei信息
	 */
	@Column
	public String imei;
	
	/**
	 * ip地址
	 */
	@Column
	public String ip;

	/**
	 * 登陆的时间
	 */
	@Column
	public int loginTime;

	@Override
	public Long getPkId() {
		return this.actorId;
	}

	@Override
	public void setPkId(Long pk) {
		this.actorId = pk;
	}

	public static Online valueOf(long actorId, int platformType, int serverId, String uid, int channelId, String sim, String mac, String imei,
			String ip) {
		Online online = new Online();
		online.actorId = actorId;
		online.platformType = platformType;
		online.serverId = serverId;
		online.uid = uid;
		online.channelId = channelId;
		online.sim = sim;
		online.mac = mac;
		online.imei = imei;
		online.ip = ip;
		online.loginTime = TimeUtils.getNow();
		return online;
	}

	@Override
	protected Entity<Long> readData(ResultSet rs, int rowNum) throws SQLException {
		Online online = new Online();
		online.actorId = rs.getLong("actorId");
		online.platformType = rs.getInt("platformType");
		online.serverId = rs.getInt("serverId");
		online.uid = rs.getString("uid");
		online.channelId = rs.getInt("channelId");
		online.sim = rs.getString("sim");
		online.mac = rs.getString("mac");
		online.imei = rs.getString("imei");
		online.ip = rs.getString("ip");
		online.loginTime = rs.getInt("loginTime");

		return online;
	}

	@Override
	protected void hasReadEvent() {
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> value = new ArrayList<>();
		if (containsPK) {
			value.add(this.actorId);
		}
		value.add(this.platformType);
		value.add(this.serverId);
		value.add(this.uid);
		value.add(this.channelId);
		value.add(this.sim);
		value.add(this.mac);
		value.add(this.imei);
		value.add(this.ip);
		value.add(this.loginTime);
		return value;
	}

	@Override
	protected void beforeWritingEvent() {
	}

	@Override
	protected void disposeBlob() {
		
	}
}

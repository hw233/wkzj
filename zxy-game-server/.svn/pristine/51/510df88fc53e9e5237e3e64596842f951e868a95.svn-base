package com.jtang.gameserver.dbproxy.entity;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;
import com.jtang.core.utility.SecurityUtils;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.dataconfig.model.InviteRewardConfig;
import com.jtang.gameserver.dataconfig.service.InviteService;
import com.jtang.gameserver.module.extapp.invite.helper.InvitePushHelper;
import com.jtang.gameserver.module.extapp.invite.type.ReceiveStatusType;
import com.jtang.gameserver.module.user.helper.ActorHelper;

/**
 * 邀请好友活动表
 * @author jianglf
 *
 */
@TableName(name="invite", type = DBQueueType.IMPORTANT)
public class Invite extends Entity<Long> {

	private static final long serialVersionUID = -4679466669014593502L;
	
	/**
	 * 玩家id
	 */
	@Column(pk=true)
	public long actorId;
	
	/**
	 * 邀请码
	 */
	@Column
	public String inviteCode;
	
	/**
	 * 已经邀请的人
	 * actorId
	 */
	@Column
	public long inviteActor;
	
	/**
	 * 已经被谁邀请
	 */
	@Column
	public long targetInvite;
	
	/**
	 * 领取奖励记录
	 */
	@Column
	public String receivedRecord;
	
	/**
	 * 奖励领取情况
	 * 格式Map<邀请人数, 礼包状态>
	 * status : 1 未领取、2 可 领取、3已领取
	 */
	public Map<Integer,Integer> rewardMap = new HashMap<Integer,Integer>();

	@Override
	public Long getPkId() {
		return actorId;
	}

	@Override
	public void setPkId(Long pk) {
		this.actorId = pk;
	}

	@Override
	protected Entity<Long> readData(ResultSet rs, int rowNum)
			throws SQLException {
		Invite invite = new Invite();
		invite.actorId = rs.getLong("actorId");
		invite.inviteCode = rs.getString("inviteCode");
		invite.inviteActor = rs.getLong("inviteActor");
		invite.targetInvite = rs.getLong("targetInvite");
		invite.receivedRecord = rs.getString("receivedRecord");
		return invite;
	}

	@Override
	protected void hasReadEvent() {
		rewardMap = StringUtils.delimiterString2IntMap(receivedRecord);
		if (rewardMap.isEmpty()) {
			Collection<InviteRewardConfig> rewardCollection = InviteService.getAllRewardConfigs();
			for (InviteRewardConfig rewardConfig : rewardCollection) {
				rewardMap.put(rewardConfig.inviteLevel, ReceiveStatusType.DID_NOT_RECEIVE.getType());
			}
		}
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> values = new ArrayList<>();
		if(containsPK){
			values.add(actorId);
		}
		values.add(inviteCode);
		values.add(inviteActor);
		values.add(targetInvite);
		values.add(receivedRecord);
		return values;
	}

	@Override
	protected void beforeWritingEvent() {
		receivedRecord = StringUtils.map2DelimiterString(rewardMap, Splitable.ATTRIBUTE_SPLIT, Splitable.ELEMENT_DELIMITER);
	}

	@Override
	protected void disposeBlob() {
		receivedRecord = EMPTY_STRING;
	}

	public static Invite valueOf(long actorId) {
		Invite invite = new Invite();
		invite.actorId = actorId;
		BigInteger big = new BigInteger(String.valueOf(actorId));
		invite.inviteCode = SecurityUtils.hexadecimalConversion(big, 36);
		invite.inviteActor = 0L;
		invite.targetInvite = 0L;
		return invite;
	}
	
	/**
	 * 
	 * @param actorId 被邀请者id
	 * @param actorLevel 被邀请者level
	 */
	public void receivedChange(long beInviteId, long beInviteActorLevel) {
		this.inviteActor = beInviteId;
		this.arrangementRewardMap(beInviteActorLevel);
		InvitePushHelper.pushInviteReward(this, ActorHelper.getActorName(this.inviteActor));
	}
	
	public void arrangementRewardMap(long beInviteActorLevel) {
		for (Map.Entry<Integer, Integer> record : rewardMap.entrySet()) {
			if (record.getKey() == 0) {
				continue;
			}
			int key = record.getKey();
			int value = 0;
			if (beInviteActorLevel >= key) {
				value = Math.max(record.getValue(), ReceiveStatusType.CAN_RECEIVE.getType());
			} else {
				value = Math.max(record.getValue(), ReceiveStatusType.DID_NOT_RECEIVE.getType());
			}
			rewardMap.put(key, value);
		}
	}

}

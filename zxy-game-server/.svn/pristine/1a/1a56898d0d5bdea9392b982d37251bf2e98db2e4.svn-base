package com.jtang.gameserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.dataconfig.service.SprintGiftService;
import com.jtang.gameserver.module.sprintgift.type.SprintGiftStatusType;

/**
 * 等级奖励礼包信息表
 * <pre>
 * 
 * --以下为db说明---------------------------
 * 主键为actorId,每个角色仅有一行记录
 * </pre>
 * @author lig
 */
@TableName(name="sprintGift", type = DBQueueType.IMPORTANT)
public class SprintGift extends Entity<Long> {

	private static final long serialVersionUID = 5228285240499144227L;

	/**
	 * 角色id，主键
	 */
	@Column(pk=true)
	private long actorId;
	
	/**
	 * 是否领取所有等级奖励礼包
	 * 默认值 false
	 */
	@Column
	public boolean acceptAllGift;

	/**
	 * 未领取的等级奖励
	 * 格式:等级_领取状态|等级_领取状态|....|等级_领取状态
	 */
	@Column
	private String unreceivedGifts;

	/**
	 * 记录所有等级礼包的收取情况
	 */
	public Map<Integer, Integer> sprintGiftsMap = new HashMap<Integer, Integer>();


	@Override
	public Long getPkId() {
		return this.actorId;
	}

	@Override
	public void setPkId(Long pk) {
		this.actorId = pk;
	}

	public static SprintGift valueOf(long actorId) {
		SprintGift gift = new SprintGift();
		gift.setPkId(actorId);
		gift.unreceivedGifts = "";
		gift.acceptAllGift = false;
		gift.sprintGiftsMap = SprintGiftService.getDefaultStatusMap();
		return gift;
	}
	
	public void judgeHadAcceptAllGift() {
		if (sprintGiftsMap.isEmpty()) {
			return;
		}
		this.acceptAllGift = true;
		for (Integer status : sprintGiftsMap.values()) {
			if (status != SprintGiftStatusType.HAD_RECEIVED.getType()) {
				this.acceptAllGift = false;
			}
		}
	}
	public Map<Integer, Integer> getUnreceivedMap() {
		Map<Integer, Integer> unreceivedMap = new HashMap<Integer, Integer>();
		for (Map.Entry<Integer, Integer> entry : sprintGiftsMap.entrySet()) {
			if (entry.getValue() != SprintGiftStatusType.HAD_RECEIVED.getType()) {
				unreceivedMap.put(entry.getKey(), entry.getValue());
			}
		}
		return unreceivedMap;
	}
	
	
	@Override
	protected Entity<Long> readData(ResultSet rs, int rowNum) throws SQLException {
		SprintGift gift = new SprintGift();
		gift.actorId = rs.getLong("actorId");
		gift.acceptAllGift = rs.getBoolean("acceptAllGift");
		gift.unreceivedGifts = rs.getString("unreceivedGifts");
		return gift;
	}

	@Override
	protected void hasReadEvent() {
		this.sprintGiftsMap = StringUtils.delimiterString2IntMap(this.unreceivedGifts);
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> value = new ArrayList<>();
		if (containsPK) {
		    value.add(this.actorId);
		}
		value.add(this.acceptAllGift);
		value.add(this.unreceivedGifts);
		return value;
	}

	
	@Override
	protected void beforeWritingEvent() {
		this.unreceivedGifts = StringUtils.map2DelimiterString(this.sprintGiftsMap, Splitable.ATTRIBUTE_SPLIT, Splitable.ELEMENT_DELIMITER);
	}
	
	@Override
	protected void disposeBlob() {
		this.unreceivedGifts = EMPTY_STRING;
	}
}
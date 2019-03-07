package com.jtang.gameserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.util.ConcurrentHashSet;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.core.utility.TimeUtils;

/**
 * 礼物信息表
 * <pre>
 * 
 * --以下为db说明---------------------------
 * 主键为actorId,每个角色仅有一行记录
 * </pre>
 * @author vinceruan
 */
@TableName(name="gift", type = DBQueueType.IMPORTANT)
public class Gift extends Entity<Long> {
	private static final long serialVersionUID = -1598162150958752753L;

	/**
	 * 角色id，主键
	 */
	@Column(pk=true)
	private long actorId;
	
	/**
	 * 最近一次重置时间(单位:秒)
	 */
	@Column
	public int resetTime;
	
	/**
	 * 是否已经领取大礼包
	 */
	@Column
	public boolean acceptGiftPackage;

	/**
	 * 已经收到我礼物的盟友, 格式:盟友id|盟友id|盟友id...
	 */
	@Column
	private String allysReceivedMyGift;
	
	/**
	 * 我所有收到但是未接受(即未打开)的礼物,格式:盟友id_礼物id_礼物数量_礼物id_数量|盟友id_礼物id_礼物数量_礼物id_数量
	 */
	@Column
	private String receivedGifts;
	
	/**
	 * 我已经接受其礼物的盟友,格式盟友id|盟友id|盟友id...
	 */
	@Column
	private String acceptedAllys;
	
	/**
	 * 记录所有的已经收到我礼物的盟友id
	 */
	private Set<Long> allysReceivedMyGiftSet =  new ConcurrentHashSet<>();

	
	/**
	 * 记录所有我已经收取其礼物的盟友id
	 */
	private Set<Long> acceptedAllysSet =  new ConcurrentHashSet<>();

	
	/**
	 * 记录所有盟友送给我的礼物(还没收取的)
	 */
	private Map<Long, Map<Integer, Integer>> receivedGiftsMap = new ConcurrentHashMap<>();

	/**
	 * 记录我已送礼的盟友id
	 * @param allyActorId
	 */
	public void sendGift2Ally(long allyActorId) {
		this.allysReceivedMyGiftSet.add(allyActorId);
	}
	
	/**
	 * 收到好友的礼物
	 * @param allyActorId
	 * @param gifts
	 */
	public void receiveGiftFromAlly(long allyActorId, Map<Integer, Integer> gifts) {
		this.getReceivedGiftsMap().put(allyActorId, gifts);
	}
	
	/**
	 * 接受好友的礼物
	 * @param allyActor
	 */
	public void acceptAllyGift(long allyActor) {
		//从未领取列表中移除
		this.acceptedAllysSet.add(allyActor);
		//添加到已领取列表
		this.receivedGiftsMap.remove(allyActor);
	}

	public Set<Long> getAllysReceivedMyGiftSet() {
		return allysReceivedMyGiftSet;
	}

	public Set<Long> getAcceptedAllysSet() {
		return acceptedAllysSet;
	}

	public Map<Long, Map<Integer, Integer>> getReceivedGiftsMap() {
		return receivedGiftsMap;
	}

	@Override
	public Long getPkId() {
		return this.actorId;
	}

	@Override
	public void setPkId(Long pk) {
		this.actorId = pk;
	}

	public static Gift valueOf(long actorId) {
		Gift gift = new Gift();
		gift.setPkId(actorId);
		gift.resetTime = TimeUtils.getNow();
		gift.acceptGiftPackage = false;
		gift.acceptedAllys = "";
		gift.allysReceivedMyGift = "";
		gift.receivedGifts = "";
		return gift;
	}
	
	public void cleanGiftInfo() {
		resetTime = TimeUtils.getNow();
		acceptGiftPackage = false;
		getAcceptedAllysSet().clear();
		acceptedAllys = "";
		getAcceptedAllysSet().clear();
		allysReceivedMyGift = "";
		getAllysReceivedMyGiftSet().clear();
		receivedGifts = "";
	}

	@Override
	protected Entity<Long> readData(ResultSet rs, int rowNum) throws SQLException {
		Gift gift = new Gift();
		gift.actorId = rs.getLong("actorId");
		gift.resetTime = rs.getInt("resetTime");
		gift.allysReceivedMyGift = rs.getString("allysReceivedMyGift");
		gift.receivedGifts = rs.getString("receivedGifts");
		gift.acceptedAllys = rs.getString("acceptedAllys");
		gift.acceptGiftPackage = rs.getBoolean("acceptGiftPackage");
		return gift;
	}

	@Override
	protected void hasReadEvent() {
		
		this.allysReceivedMyGiftSet = StringUtils.delimiterString2LongSet(this.allysReceivedMyGift, Splitable.ELEMENT_SPLIT);
		this.acceptedAllysSet = StringUtils.delimiterString2LongSet(this.acceptedAllys, Splitable.ELEMENT_SPLIT);
		this.receivedGiftsMap = StringUtils.delimiterString2LongIntMap(this.receivedGifts);
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> value = new ArrayList<>();
		if (containsPK) {
		    value.add(this.actorId);
		}
		value.add(this.resetTime);
	    value.add(this.acceptGiftPackage);
	    value.add(this.allysReceivedMyGift);
	    value.add(this.receivedGifts);
	    value.add(this.acceptedAllys);
		return value;
	}

	@Override
	protected void beforeWritingEvent() {
		
		this.allysReceivedMyGift = StringUtils.Set2SplitString(this.allysReceivedMyGiftSet, Splitable.ELEMENT_DELIMITER);
		this.acceptedAllys = StringUtils.Set2SplitString(this.acceptedAllysSet, Splitable.ELEMENT_DELIMITER);
		this.receivedGifts = StringUtils.longIntMap2delimiterString(this.receivedGiftsMap);
	}
	
	public void reset() {
		this.acceptedAllysSet.clear();
		this.acceptGiftPackage = false;
		this.allysReceivedMyGiftSet.clear();
		this.receivedGiftsMap.clear();
		this.resetTime = 0;
	}
	
	@Override
	protected void disposeBlob() {
		this.allysReceivedMyGift = EMPTY_STRING;
		this.acceptedAllys = EMPTY_STRING;
		this.receivedGifts = EMPTY_STRING;
	}
}
package com.jtang.gameserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import java.util.Set;

import org.apache.mina.util.ConcurrentHashSet;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;

/**
 * 仙人图鉴实体
 * <pre>
 * --以下为db说明---------------------------
 * 说明:
 * >主键为actorId,每个角色有一行记录
 * </pre>
 * @author ludd
 *
 */
@TableName(name="heroBook", type=DBQueueType.IMPORTANT)
public class HeroBook extends Entity<Long> {
	private static final long serialVersionUID = -258634389057936013L;
	
	/**
	 * 角色id
	 */
	@Column(pk = true)
	private long actorId;
	
	/**
	 * 领取的奖励序号
	 */
	@Column
	private String getOrderId;
	
	/**
	 * 点亮的仙人Id
	 */
	@Column
	private String heroId;
	
	/**
	 * 点亮仙人id
	 */
	public Set<Integer> historyHeroIds = new ConcurrentHashSet<>();
	/**
	 * 领取奖励序号
	 */
	private Set<Integer> orderIds = new ConcurrentHashSet<>();
	
	@Override
	public Long getPkId() {
		return actorId;
	}

	@Override
	public void setPkId(Long pk) {
		this.actorId = pk;
	}

	@Override
	protected Entity<Long> readData(ResultSet resultset, int rowNum) throws SQLException {
		HeroBook herobook = new HeroBook();
		herobook.actorId = resultset.getLong("actorId");
		herobook.getOrderId = resultset.getString("getOrderId");
		herobook.heroId = resultset.getString("heroId");
		return herobook;
	}

	@Override
	protected void hasReadEvent() {
		List<Integer> ids = StringUtils.delimiterString2IntList(this.getOrderId, Splitable.ATTRIBUTE_SPLIT);
		for (Integer orderId : ids) {
			this.orderIds.add(orderId);
		}
		List<Integer> list = StringUtils.delimiterString2IntList(this.heroId, Splitable.ATTRIBUTE_SPLIT);
		for (Integer heroId : list) {
			this.historyHeroIds.add(heroId);
		}
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> value = new ArrayList<>();
		if (containsPK) {
			value.add(this.actorId);
		}
		value.add(this.getOrderId);
		value.add(this.heroId);
		return value;
	}

	@Override
	protected void beforeWritingEvent() {
		this.heroId = StringUtils.collection2SplitString(this.historyHeroIds, Splitable.ATTRIBUTE_SPLIT);
		this.getOrderId = StringUtils.collection2SplitString(this.orderIds, Splitable.ATTRIBUTE_SPLIT);
	}

	public void updateOrderId(int oldOrderId, int newOrderId) {
		this.orderIds.remove(oldOrderId);
		this.orderIds.add(newOrderId);
	}
	
	public Set<Integer> getOrderIds() {
		return orderIds;
	}

	public static HeroBook valueOf(long actorId, Set<Integer> startNode) {
		HeroBook heroBook = new HeroBook();
		heroBook.actorId = actorId;
		heroBook.orderIds.addAll(startNode);
		return heroBook;
	}
	
	public boolean containsOrderId(int orderId) {
		return this.getOrderIds().contains(orderId);
	}
	
	public boolean containsHeroId(int heroId) {
		return this.historyHeroIds.contains(heroId);
	}
	
	@Override
	protected void disposeBlob() {
		this.getOrderId = EMPTY_STRING;
		this.heroId = EMPTY_STRING;
	}

}

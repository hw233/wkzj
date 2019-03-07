package com.jtang.gameserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.module.lineup.constant.LineupRule;
import com.jtang.gameserver.module.lineup.model.LineupHeadItem;

/**
 * 阵形表
 * <pre>
 * 存储上阵仙人数据
 * 每个凹可放装备(武器、防具、饰品各一件)
 * 
 * --以下为db说明---------------------------
 * 主键为actorId,每个角色仅有一行记录
 * </pre>
 * @author 0x737263
 *
 */
@TableName(name="lineup", type = DBQueueType.IMPORTANT)
public class Lineup extends Entity<Long> {
	private static final long serialVersionUID = 4116497233926480025L;

	/**
	 * 角色ID
	 */
	@Column(pk=true)
	private long actorId;
	
	/**
	 * 上阵仙人的顺序Blob字段
	 * 格式:格子索引_阵位_heroId_装备uuid_防具uuid_饰品uuid|
	 */
	@Column
	private String heros;
	
//	/**
//	 * 阵型页面顶部的仙人列表
//	 */
//	private LineupHeadItem[] headItemList = new LineupHeadItem[9];
	
	private List<LineupHeadItem[]> headList = new ArrayList<>();
	
	/**
	 * 已经解锁的格子数
	 */
	@Column
	public int activedGridCount;
	
	/**
	 * 当前使用阵型
	 */
	@Column
	private int currentIndex = 0;
	

	@Override
	public Long getPkId() {
		return this.actorId;
	}

	@Override
	public void setPkId(Long id) {
		this.actorId = id;
	}
	
	public boolean isEmpty() {
		return this.useGridNum() == 0;
	}

	public LineupHeadItem[] getHeadItemList() {
		return this.headList.get(this.currentIndex);
	}
	
	/**
	 * 获取阵型中第一个仙人Id
	 * @return
	 */
	public int getFirstHeroId() {
		LineupHeadItem[] list = getHeadItemList();
		int heroId = 0;
		for (LineupHeadItem item : list) {
			if (item.heroId > 0) {
				heroId = item.heroId;
				break;
			}
		}
		return heroId;
	}
	
	/**
	 * 更新仙人到阵型(不做验证)
	 * @param heroId
	 * @param gridIndex
	 */
	public void assignHero(int heroId, int headIndex, int gridIndex) {
		LineupHeadItem[] list = getHeadItemList();
		LineupHeadItem item = list[headIndex - 1];
		item.heroId = heroId;

		if (item.gridIndex != gridIndex) {
			LineupHeadItem item2 = getHeadItemByGridIndex(gridIndex);
			item2.gridIndex = item.gridIndex;
			item.gridIndex = gridIndex;
		}
	}
	
	/**
	 * 仙人下阵(不做验证)
	 * @param heroId
	 * @param gridIndex
	 */
	public void unAssignHero(int heroId, int headIndex) {
		LineupHeadItem[] list = getHeadItemList();
		LineupHeadItem item = list[headIndex - 1];
		Assert.isTrue(heroId == item.heroId, "仙人不在该位置");
		item.heroId = 0;
	}

	public String getHeros() {
		return heros;
	}

	
	public LineupHeadItem getHeadItemByGridIndex(int gridIndex) {
		for (LineupHeadItem item : getHeadItemList()) {
			if (item.gridIndex == gridIndex) {
				return item;
			}
		}
		return null;
	}
	
	/**
	 * 初始化阵型
	 * @param actorId
	 * @param heroId
	 * @return
	 */
	public static Lineup valueOf(long actorId) {
		Lineup lineup = new Lineup();
		LineupHeadItem[] headItemList = new LineupHeadItem[9];
		lineup.headList.add(headItemList);
		lineup.setPkId(actorId);
		lineup.activedGridCount = LineupRule.LINEUP_INIT_ACTIVED_GRID_COUNT;
		for (int i = 1; i <= LineupRule.MAX_GRID_COUNT; i++) {
			LineupHeadItem item = new LineupHeadItem();
			item.headIndex = i;
			item.gridIndex = i;
			lineup.getHeadItemList()[i - 1] = item;
		}
		return lineup;
	}

	@Override
	protected Entity<Long> readData(ResultSet rs, int rowNum) throws SQLException {
		Lineup lineup = new Lineup();
		lineup.actorId = rs.getLong("actorId");
		lineup.heros = rs.getString("heros");
		lineup.activedGridCount = rs.getInt("activedGridCount");
		lineup.currentIndex = rs.getInt("currentIndex");
		return lineup;
	}

	@Override
	protected void hasReadEvent() {
		
		//解析顶部列表
		List<String> headItems = StringUtils.delimiterString2List(this.heros, Splitable.ELEMENT_SPLIT);
		LineupHeadItem[] headItemList = new LineupHeadItem[9];
		for (int i = 0; i < headItemList.length; i++) {
			LineupHeadItem lh = new LineupHeadItem();
			if (i >= headItems.size()) {
				lh.headIndex = i;
				lh.gridIndex = i;
			} else {
				List<String> itemInfo = StringUtils.delimiterString2List(headItems.get(i), Splitable.ATTRIBUTE_SPLIT);
				lh.headIndex = Integer.valueOf(itemInfo.get(0));
				lh.gridIndex = Integer.valueOf(itemInfo.get(1));
				lh.heroId = Integer.valueOf(itemInfo.get(2));
				lh.atkEquipUuid = Long.valueOf(StringUtils.isBlank(itemInfo.get(3)) || "null".equals(itemInfo.get(3)) ? "0" : itemInfo.get(3));
				lh.defEquipUuid = Long.valueOf(StringUtils.isBlank(itemInfo.get(4)) || "null".equals(itemInfo.get(4))? "0" : itemInfo.get(4));
				lh.decorationUuid = Long.valueOf(StringUtils.isBlank(itemInfo.get(5)) || "null".equals(itemInfo.get(4)) ? "0" : itemInfo.get(5));
				headItemList[i] = lh;
			}
		}
		
		this.headList.add(headItemList);
		
		
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> value = new ArrayList<>();
		if (containsPK){
		    value.add(this.actorId);
		}
	    value.add(this.heros);
	    value.add(this.activedGridCount);
	    value.add(this.currentIndex);
		return value;
	}

	@Override
	protected void beforeWritingEvent() {
		
		List<String> blobList = new ArrayList<String>();
		for (LineupHeadItem[] headItemList : this.headList) {
			for (LineupHeadItem item : headItemList) {
				StringBuilder sb = new StringBuilder();
				sb.append(item.headIndex).append(Splitable.ATTRIBUTE_SPLIT);
				sb.append(item.gridIndex).append(Splitable.ATTRIBUTE_SPLIT);
				sb.append(item.heroId).append(Splitable.ATTRIBUTE_SPLIT);
				sb.append(item.atkEquipUuid).append(Splitable.ATTRIBUTE_SPLIT);
				sb.append(item.defEquipUuid).append(Splitable.ATTRIBUTE_SPLIT);
				sb.append(item.decorationUuid);
				blobList.add(sb.toString());
			}
		}
		this.heros = StringUtils.collection2SplitString(blobList, Splitable.ELEMENT_DELIMITER);
		
	}
	
	public void reset(int heroId) {
		this.headList.clear();
		LineupHeadItem[] headItemList = new LineupHeadItem[9];
		headList.add(headItemList);
		this.activedGridCount = LineupRule.LINEUP_INIT_ACTIVED_GRID_COUNT;
		for (int i = 1; i <= LineupRule.MAX_GRID_COUNT; i++) {
			LineupHeadItem item = new LineupHeadItem();
			item.headIndex = i;
			item.gridIndex = i;
			this.getHeadItemList()[i - 1] = item;
		}
		if (heroId > 0) {
			this.assignHero(heroId, 1, 1);
		}
	}
	/**
	 * 计算已使用格子数
	 * @return
	 */
	public int useGridNum() {
		int num = 0;
		LineupHeadItem[] headItemList = getHeadItemList();
		for (int i = 0; i < headItemList .length; i++) {
			LineupHeadItem item = headItemList[i];
			if (item.heroId > 0) {
				num += 1;
			}
		}
		return num;
		
	}

	/**
	 * 更换阵型
	 */
	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}

	@Override
	protected void disposeBlob() {
		heros = EMPTY_STRING;
	}
}

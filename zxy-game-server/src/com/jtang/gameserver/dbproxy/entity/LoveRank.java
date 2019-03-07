package com.jtang.gameserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.module.love.model.FightVO;

/**
 * 结婚系统排行榜
 * @author jianglf
 *
 */
@TableName(name="loveRank", type = DBQueueType.IMPORTANT)
public class LoveRank extends Entity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5924750149081241596L;
	
	/**
	 * 结缘id1
	 */
	@Column(pk=true)
	public long loveId1;
	
	/**
	 * 结缘id2
	 */
	@Column
	public long loveId2;
	
	/**
	 * 排行
	 */
	@Column
	public int rank;
	
	/**
	 * 战斗记录
	 */
	@Column
	public String fightInfo;
	
	public ConcurrentLinkedQueue<FightVO> fightList = new ConcurrentLinkedQueue<>();
	
	@Override
	public Long getPkId() {
		return loveId1;
	}

	@Override
	public void setPkId(Long pk) {
		this.loveId1 = pk;
	}

	@Override
	protected Entity<Long> readData(ResultSet rs, int rowNum)
			throws SQLException {
		LoveRank loveRank = new LoveRank();
		loveRank.loveId1 = rs.getLong("loveId1");
		loveRank.loveId2 = rs.getLong("loveId2");
		loveRank.rank = rs.getInt("rank");
		loveRank.fightInfo = rs.getString("fightInfo");
		return loveRank;
	}

	@Override
	protected void hasReadEvent() {
		List<String[]> list = StringUtils.delimiterString2Array(fightInfo);
		for(String[] str:list){
			FightVO fightVO = FightVO.valueOf(str);
			fightList.add(fightVO);
		}
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> values =  new ArrayList<>();
		if(containsPK){
			values.add(this.loveId1);
		}
		values.add(this.loveId2);
		values.add(this.rank);
		values.add(this.fightInfo);
		return values;
	}

	@Override
	protected void beforeWritingEvent() {
		StringBuffer sb = new StringBuffer();
		for(FightVO fightVO:fightList){
			sb.append(fightVO.parser2String()).append(Splitable.ELEMENT_DELIMITER);
		}
		if(fightList.isEmpty() == false){
			sb.deleteCharAt(sb.length() - 1);
		}
		this.fightInfo = sb.toString();
	}

	@Override
	protected void disposeBlob() {
		fightInfo = EMPTY_STRING;
	}

	public static LoveRank valueOf(long actorId1,long actorId2,int rank) {
		LoveRank loveRank = new LoveRank();
		loveRank.loveId1 = actorId1;
		loveRank.loveId2 = actorId2;
		loveRank.rank = rank;
		return loveRank;
	}

}

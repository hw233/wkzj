package com.jtang.gameserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.module.love.model.BossVO;

/**
 * 仙侣合作表
 * @author jianglf
 *
 */
@TableName(name="loveMonster", type = DBQueueType.IMPORTANT)
public class LoveMonster extends Entity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7911072487138313517L;

	/**
	 * 玩家id1
	 */
	@Column(pk=true)
	public long loveId1;
	
	/**
	 * 玩家id2
	 */
	@Column
	public long loveId2;
	
	/**
	 * boss挑战状态列表
	 */
	@Column
	public String bossState;
	
	/**
	 * 挑战时间
	 */
	@Column
	public int fightTime;
	
	public Map<Integer,BossVO> bossMap = new ConcurrentHashMap<>();
	
	@Override
	public Long getPkId() {
		return loveId1;
	}

	@Override
	public void setPkId(Long pk) {
		this.loveId2 = pk;
	}

	@Override
	protected Entity<Long> readData(ResultSet rs, int rowNum)
			throws SQLException {
		LoveMonster loveMonster = new LoveMonster();
		loveMonster.loveId1 = rs.getLong("loveId1");
		loveMonster.loveId2 = rs.getLong("loveId2");
		loveMonster.bossState = rs.getString("bossState");
		loveMonster.fightTime = rs.getInt("fightTime");
		return loveMonster;
	}

	@Override
	protected void hasReadEvent() {
		List<String[]> list = StringUtils.delimiterString2Array(bossState);
		for(String[] str:list){
			BossVO bossStateVO = BossVO.valueOf(str);
			bossMap.put(bossStateVO.id, bossStateVO);
		}
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> values = new ArrayList<>();
		if(containsPK){
			values.add(this.loveId1);
		}
		values.add(this.loveId2);
		values.add(this.bossState);
		values.add(this.fightTime);
		return values;
	}

	@Override
	protected void beforeWritingEvent() {
		StringBuffer sb = new StringBuffer();
		for(BossVO bossStateVO: bossMap.values()){
			sb.append(bossStateVO.parser2String()).append(Splitable.ELEMENT_DELIMITER);
		}
		if(bossMap.size() > 0){
			sb.deleteCharAt(sb.length() - 1);
		}
		this.bossState = sb.toString();
	}

	@Override
	protected void disposeBlob() {
		bossState = EMPTY_STRING;
	}

	public static LoveMonster valueOf(long actorId1, long actorId2) {
		LoveMonster loveMonster = new LoveMonster();
		loveMonster.loveId1 = actorId1;
		loveMonster.loveId2 = actorId2;
		return loveMonster;
	}

}

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
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.module.battle.constant.WinLevel;
import com.jtang.gameserver.module.battle.type.BattleType;
import com.jtang.gameserver.module.battle.type.BattleVO;

/**
 * 战斗数据表（主要记录战斗时间和战斗类型）
 * <pre>
 * --以下为db说明---------------------------
 * 主键为actorId,每个角色仅有一行记录
 * </pre>
 * @author ludd
 */
@TableName(name = "battle", type = DBQueueType.IMPORTANT)
public class Battle extends Entity<Long> {
	private static final long serialVersionUID = -3722754047298246890L;

	/**
	 * 角色id
	 */
	@Column(pk = true)
	public long actorId;
	
	/**
	 * <pre>
	 * 名称：战斗数据
	 * 解析：战斗类型_胜利次数_失败次数
	 * </pre>
	 */
	@Column
	private String battleData;
	
	/**
	 * 最后战斗时间（用于清除记录）
	 */
	private int lastBatteTime;
	
	/**
	 * battleData转换成的对象
	 */
	private Map<Integer, BattleVO> battleMap = new ConcurrentHashMap<Integer, BattleVO>();

	@Override
	public Long getPkId() {
		return this.actorId;
	}

	@Override
	public void setPkId(Long pk) {
		this.actorId = pk;

	}
	
	public void updateBattleData(BattleType battleType, WinLevel winLevel){
		if (TimeUtils.beforeTodayZero(lastBatteTime)){
			battleMap.clear();
		}
		if (battleMap.containsKey(battleType.getCode())){
			BattleVO battleVO = battleMap.get(battleType.getCode());
			if (winLevel.isWin()){
				battleVO.winNum = battleVO.winNum + 1;
			} else {
				battleVO.failNum = battleVO.failNum + 1;
			}
			battleVO.lastBattleTime = TimeUtils.getNow();
		} else {
			BattleVO battleVO = new BattleVO();
			battleVO.battleType = battleType;
			if (winLevel.isWin()){
				battleVO.winNum = 1;
			} else {
				battleVO.failNum = 0;
			}
			battleVO.lastBattleTime = TimeUtils.getNow();
			battleMap.put(battleType.getCode(), battleVO);
		}
		
		for (BattleVO battleVO : battleMap.values()) {
			if (lastBatteTime < battleVO.lastBattleTime){
				lastBatteTime = battleVO.lastBattleTime;
			}
		}
	}
	
	public static Battle valueOf(long actorId) {
		Battle battle = new Battle();
		battle.setPkId(actorId);
		battle.battleData = "";
		return battle;
	}
	
	public BattleVO getBattleVO(BattleType battleType) {
		if (battleMap.containsKey(battleType.getCode())) {
			BattleVO battleVO = battleMap.get(battleType.getCode());
			return battleVO;
		}
		return null;
	}

	@Override
	protected Entity<Long> readData(ResultSet rs, int rowNum) throws SQLException {
		Battle battle = new Battle();
		battle.actorId = rs.getLong("actorId");
		battle.battleData = rs.getString("battleData");
		return battle;
	}

	@Override
	protected void hasReadEvent() {
		
		if(StringUtils.isNotBlank(this.battleData)) {
			String[] strs = this.battleData.split(Splitable.ELEMENT_SPLIT);
			for (String string : strs) {
				String[] values = string.split(Splitable.ATTRIBUTE_SPLIT);
				BattleVO battleVO = new BattleVO(values);
				this.battleMap.put(battleVO.battleType.getCode(), battleVO);
			}
		}
		for (BattleVO battleVO : this.battleMap.values()) {
			if (lastBatteTime < battleVO.lastBattleTime){
				lastBatteTime = battleVO.lastBattleTime;
			}
		}
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> value = new ArrayList<>();
		if (containsPK){
		    value.add(this.actorId);
		}
	    value.add(this.battleData);
		return value;
	}

	@Override
	protected void beforeWritingEvent() {
		List<String> list = new ArrayList<>();
		for (BattleVO vo : this.battleMap.values()) {
			list.add(vo.parse2String());
		}
		this.battleData = StringUtils.collection2SplitString(list, Splitable.ELEMENT_DELIMITER);
	}
	
	public void reset() {
		this.battleMap.clear();
		this.lastBatteTime = 0;
	}
	
	@Override
	protected void disposeBlob() {
		this.battleData = EMPTY_STRING;
	}

}

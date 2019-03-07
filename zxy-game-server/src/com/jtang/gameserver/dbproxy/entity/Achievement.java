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
import com.jtang.gameserver.dataconfig.model.AchievementConfig;
import com.jtang.gameserver.dataconfig.service.AchievementService;
import com.jtang.gameserver.module.adventures.achievement.model.AchieveVO;

/**
 * 成就表
 * <pre>
 * 
 * --以下为db说明---------------------------
 * 主键为actorId,每个角色仅有一行记录
 * </pre>
 * @author pengzy
 * 
 */
@TableName(name="achievement", type = DBQueueType.IMPORTANT)
public class Achievement extends Entity<Long> {
	private static final long serialVersionUID = 517824666048242908L;

	@Column(pk = true)
	private long actorId;
	
	/**
	 * <pre>
	 * 名称：成就项记录
	 * 解析：成就Id_状态id_达成条件id列表|成就Id_状态id_达成条件id列表
	 * 状态id：0未达成、1达成未领取奖励、2已领取奖励
	 * </pre>
	 */
	@Column
	private String achievement;
	
	/**
	 * achievement转换成的对象
	 */
	private ConcurrentHashMap<Integer, Map<Integer, AchieveVO>> achieveMap = new ConcurrentHashMap<>();
	
	public Map<Integer, AchieveVO> getAchievementList(int achieveId) {
		return getAchieveMap().get(achieveId);
	}

	private ConcurrentHashMap<Integer, Map<Integer, AchieveVO>> getAchieveMap() {
		return this.achieveMap;
	}
	
	public void addAchievement(AchieveVO achieveVO) {
		AchievementConfig config = AchievementService.get(achieveVO.getAchieveId());
		if(config == null) {
			return;
		}
		
		AchieveVO vo = getAchievement(achieveVO.getAchieveId(), config.getAchieveType());
		if (vo != null) {
			return;
		}
		
		Map<Integer, AchieveVO> list = getAchieveMap().get(config.getAchieveType());
		if (list == null) {
			list = new ConcurrentHashMap<>();
		}
		list.put(achieveVO.getAchieveId(), achieveVO);
		getAchieveMap().put(config.getAchieveType(), list);
	}

	@Override
	public Long getPkId() {
		return actorId;
	}

	@Override
	public void setPkId(Long pk) {
		this.actorId = pk;
	}
	
	public static Achievement valueOf(long actorId) {
		Achievement achieve = new Achievement();
		achieve.actorId = actorId;
		achieve.achievement = "";
		return achieve;
	}

	public List<AchieveVO> getAll() {
		List<AchieveVO> achiveVOList = new ArrayList<>();
		for (Map<Integer, AchieveVO> list : getAchieveMap().values()) {
			achiveVOList.addAll(list.values());
		}
		return achiveVOList;
	}

	public AchieveVO getAchievement(int achieveId, int achieveType) {
		Map<Integer, AchieveVO> list = getAchieveMap().get(achieveType);
		if(list == null || list.size() < 1) {
			return null;
		}
		if (list.containsKey(achieveId)) {
			return list.get(achieveId);
		}
		return null;
	}

	@Override
	protected Entity<Long> readData(ResultSet rs, int rowNum) throws SQLException {
		Achievement achievement = new Achievement();
		achievement.actorId = rs.getLong("actorId");
		achievement.achievement = rs.getString("achievement");
		return achievement;
	}

	@Override
	protected void hasReadEvent() {
		if (StringUtils.isNotBlank(this.achievement)) {
			List<String[]> list = StringUtils.delimiterString2Array(achievement);
			for (String[] array : list) {
				AchieveVO achievementVO = AchieveVO.valueOf(array);
				this.addAchievement(achievementVO);
			}
		}
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> value = new ArrayList<>();
		if (containsPK) {
			value.add(this.actorId);
		}
		value.add(this.achievement);
		return value;
	}

	@Override
	protected void beforeWritingEvent() {

		List<String> blobList = new ArrayList<>();
		for (Map<Integer, AchieveVO> achieveList : achieveMap.values()) {
			for (AchieveVO vo : achieveList.values()) {
				String blob = vo.parseToString();
				blobList.add(blob);
			}
		}
		this.achievement = StringUtils.collection2SplitString(blobList, Splitable.ELEMENT_DELIMITER);
	}

	public void removeAchieveId(int achieveId, int achieveType) {
		AchieveVO achieveVO = getAchievement(achieveId, achieveType);
		if (achieveVO == null) {
			return;
		}
		Map<Integer, AchieveVO> list = getAchievementList(achieveType);
		if (list == null) {
			return;
		}
		list.remove(achieveId);
	}
	
	public void reset() {
		this.achieveMap.clear();
	}
	
	@Override
	protected void disposeBlob() {
		this.achievement = EMPTY_STRING;
	}
}

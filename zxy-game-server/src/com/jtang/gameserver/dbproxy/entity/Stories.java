package com.jtang.gameserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.module.story.helper.StoryHelper;
import com.jtang.gameserver.module.story.model.StoryVO;

/**
 * 故事通关记录
 * <pre>
 * 
 * --以下为db说明---------------------------
 * 主键为actorId,每个角色仅有一行记录
 * </pre>
 * @author vinceruan
 */
@TableName(name="stories", type = DBQueueType.IMPORTANT)
public class Stories extends Entity<Long> {
	private static final long serialVersionUID = 4489219156828877307L;

	@Column(pk=true)
	private long actorId;
	
	/**
	 * 通关记录 
	 * <pre>
	 * 格式: 故事1id_通关奖励_2星奖励_3星奖励_故事得星_战斗id:得星,战斗id:得星...|故事1id_通关奖励_2星奖励_3星奖励_故事得星_战斗id:得星,战斗id:得星...
	 * </pre>
	 */
	@Column
	private String stories;
	
	/**
	 * 关卡战斗记录
	 * 关卡id_战斗次数
	 */
	@Column
	private String battleRecord;
	
	/**
	 * 最新已占领的战场id(0代表未占领任何战场)
	 */
	@Column
	private int battleId;
	
	/**
	 * 扫荡符购买次数
	 */
	@Column
	public int num;
	
	/**
	 * 上次购买时间
	 */
	@Column
	public int buyTime;
	
	/**
	 * 邀请盟友合作关卡次数
	 */
	@Column
	private String allyFight;
	
	/**
	 * stories转换成的对象
	 */
	private Map<Integer, StoryVO> storyMap = new ConcurrentHashMap<>();
	
	/**
	 * 闯关次数
	 */
	public Map<Integer,Integer> battleRecordMap = new HashMap<>();
	
	/**
	 * 保底次数
	 * key:物品id
	 * value:保底次数
	 */
	private Map<Integer, Integer> leastNum = new ConcurrentHashMap<>();
	
	/**
	 * 邀请盟友合作关卡列表
	 */
	public Map<Long,Integer> allyFightMap = new ConcurrentHashMap<>();

	@Override
	public Long getPkId() {
		return this.actorId;
	}

	@Override
	public void setPkId(Long pk) {
		this.actorId = pk;
	}

	public Map<Integer, StoryVO> getStoryMap() {
		return this.storyMap;
	}
	
	/**
	 * 添加战斗结果
	 * @param storyId
	 * @param battleId
	 * @param star
	 */
	public void addBattleResult(int storyId, int battleId, byte star) {
		StoryVO st = this.storyMap.get(storyId);
		if (st == null) {
			st = new StoryVO();
			st.storyId = storyId;
			this.storyMap.put(storyId, st);
		}
		
		st.putBattleStar(battleId, star);
		
//		Byte oldStar = st.battleStarMap.get(battleId);
//		if (oldStar != null && oldStar >= star) {
//			return;
//		}
//		st.battleStarMap.put(battleId, star);
		updateStoryStar(st);
//		updateStoryStr();
	}
	
	public void addStoryAwardResult(int storyId, int star) {
		StoryVO stVO = this.getStoryMap().get(storyId);
		switch (star) {
		case 1:
			stVO.oneStarAwarded = 1;
			break;
		case 2:
			stVO.twoStarAwarded = 1;
			break;
		case 3:
			stVO.threeStarAwarded = 1;
			break;
		default:
			return;
		}
		
//		updateStoryStr();
	}
	
//	private void updateStoryStr() {
//
//	}
	
	/**
	 * 更新故事的星级
	 * @param story
	 */
	private void updateStoryStar(StoryVO story) {		
		story.storyStar = StoryHelper.computeStoryStar(story);
	}

	public int getBattleId() {
		return battleId;
	}

	public void setBattleId(int battleId) {
		this.battleId = battleId;
	}

	public void setStoryMap(Map<Integer, StoryVO> storyMap) {
		this.storyMap = storyMap;
	}
	
	public static Stories valueOf(long actorId) {
		Stories s = new Stories();
		s.actorId = actorId;
		s.battleId = 0;
		return s;
	}

	@Override
	protected Entity<Long> readData(ResultSet rs, int rowNum) throws SQLException {
		Stories stories = new Stories();
		stories.actorId = rs.getLong("actorId");
		stories.stories = rs.getString("stories");
		stories.battleRecord = rs.getString("battleRecord");
		stories.battleId = rs.getInt("battleId");
		stories.num = rs.getInt("num");
		stories.buyTime = rs.getInt("buyTime");
		stories.allyFight = rs.getString("allyFight");
		return stories;
	}

	@Override
	protected void hasReadEvent() {
		if (StringUtils.isNotBlank(this.stories)) {
			List<String[]> sts = StringUtils.delimiterString2Array(this.stories);
			for (String[] str : sts) {
				StoryVO storyVO = StoryVO.valueOf(str);
				this.storyMap.put(storyVO.storyId, storyVO);
			}
		}
		
		if(StringUtils.isNotBlank(this.battleRecord)){
			this.battleRecordMap = StringUtils.delimiterString2IntMap(this.battleRecord);
		}
		
		if(StringUtils.isNotBlank(this.allyFight)){
			this.allyFightMap = StringUtils.delimiterString2Long_IntMap(this.allyFight);
		}
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> value = new ArrayList<>();
		if (containsPK) {
			value.add(this.actorId);
		}
		value.add(this.stories);
		value.add(this.battleRecord);
		value.add(this.battleId);
		value.add(this.num);
		value.add(this.buyTime);
		value.add(this.allyFight);
		return value;
	}

	@Override
	protected void beforeWritingEvent() {
		
		List<String> stvoList = new ArrayList<>();
		for (StoryVO vo : this.storyMap.values()) {
			StringBuilder sb = new StringBuilder();
			sb.append(vo.storyId);
			sb.append(Splitable.ATTRIBUTE_SPLIT);
			sb.append(vo.oneStarAwarded);
			sb.append(Splitable.ATTRIBUTE_SPLIT);
			sb.append(vo.twoStarAwarded);
			sb.append(Splitable.ATTRIBUTE_SPLIT);
			sb.append(vo.threeStarAwarded);
			sb.append(Splitable.ATTRIBUTE_SPLIT);
			sb.append(vo.storyStar);
			sb.append(Splitable.ATTRIBUTE_SPLIT);
			String tmp = StringUtils.map2DelimiterString(vo.getBattleMap(), Splitable.DELIMITER_ARGS, Splitable.BETWEEN_ITEMS);
			sb.append(tmp);
			sb.append(Splitable.ATTRIBUTE_SPLIT);
			sb.append(vo.vitRewarded);
			sb.append(Splitable.ATTRIBUTE_SPLIT);
			sb.append(vo.rewardGoods);
			stvoList.add(sb.toString());
		}
		this.stories = StringUtils.collection2SplitString(stvoList, Splitable.ELEMENT_DELIMITER);
		this.battleRecord = StringUtils.numberMap2String(this.battleRecordMap);
		this.allyFight = StringUtils.numberMap2String(this.allyFightMap);
		
	}
	
	public void reset() {
		this.battleId = 0;
		this.storyMap.clear();
	}

	@Override
	protected void disposeBlob() {
		stories = EMPTY_STRING;
		battleRecord = EMPTY_STRING;
	}
	
	/**
	 * 获取保底次数
	 * @param battleId
	 * @return
	 */
	public int getLeastNum(int battleId) {
		if (this.leastNum.containsKey(battleId)) {
			return this.leastNum.get(battleId);
		}
		return 0;
	}
	
	/**
	 * 设置保底次数
	 * @param battleId
	 * @param num
	 */
	public void setLeastNum(int battleId, int num){
		this.leastNum.put(battleId, num);
	}
}

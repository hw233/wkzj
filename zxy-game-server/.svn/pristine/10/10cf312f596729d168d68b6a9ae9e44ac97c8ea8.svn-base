package com.jtang.gameserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;

/**
 * 仙人魂魄表
 * <pre>
 * 
 * --以下为db说明---------------------------
 * 主键为actorId,每个角色仅有一行记录
 * </pre>
 * @author 0x737263
 * 
 */
@TableName(name="herosoul", type = DBQueueType.IMPORTANT)
public class HeroSoul extends Entity<Long> {
	private static final long serialVersionUID = 4175465627517361058L;

	/**
	 * 角色Id
	 */
	@Column(pk=true)
	private long actorId;
	
	/**
	 * 仙人魂魄Blob字段
	 * <pre>
	 * 格式：仙人Id_魂魄数量|仙人Id_魂魄数量|仙人Id_魂魄数量
	 * </pre>
	 */
	@Column
	private String heroSoulIds;
	
	/**
	 * 仙人魂魄列表
	 */
	private Map<Integer, Integer> heroSoulMap = new ConcurrentHashMap<>();
	
	@Override
	public Long getPkId() {
		return this.actorId;
	}
	
	@Override
	public void setPkId(Long id) {
		this.actorId = id;
	}
	
	/**
	 * 仙人魂魄列表
	 * @return
	 */
	public Map<Integer, Integer> getHeroSoulMap() {
		return this.heroSoulMap;
	}
	
	/**
	 * 获取某个仙人的魂魄数量
	 * @param heroId
	 * @return
	 */
	public int getSoulCount(int heroId) {
		Map<Integer, Integer> heroSoulMap = getHeroSoulMap();
		Integer count = heroSoulMap.get(heroId);
		return count == null ? 0 : count;
	}
	
	/**
	 * 获取收集的仙人魂魄种类数目
	 * @return
	 */
	public int getHeroCount() {
		Map<Integer, Integer> heroSoulMap = getHeroSoulMap();
		return heroSoulMap.size();
	}
	
	/**
	 * 查询是否收集某个仙人的魂魄
	 * @param heroId
	 * @return
	 */
	public boolean isHeroSoulExists(int heroId) {
		return getSoulCount(heroId) > 0;
	}
	
	/**
	 * 添加一个仙人魂魄
	 * @param heroId	仙人id
	 * @param soulNum	魂魄数
	 * @return
	 */
	public void addSoul(int heroId, int soulNum) {
		Integer num = getHeroSoulMap().get(heroId);
		if (num == null) {
			num = 0;
		}
		num += soulNum;
		heroSoulMap.put(heroId, num);
	}
	
	/**
	 * 扣除一定数量的魂魄
	 * @param heroId
	 * @param soulNum
	 * @return
	 */
	public boolean reduceSoul(int heroId, int soulNum) {
		Integer num = getHeroSoulMap().get(heroId);
		if (num == null) {
			return false;
		}
		if (num < soulNum) {
			return false;
		}

		num -= soulNum;
		if (num == 0) {
			heroSoulMap.remove(heroId);
		} else {
			heroSoulMap.put(heroId, num);
		}
		return true;
	}
	
	public static HeroSoul valueOf(long actorId) {
		HeroSoul soul = new HeroSoul();
		soul.actorId = actorId;
		soul.heroSoulIds = "";
		return soul;
	}

	@Override
	protected Entity<Long> readData(ResultSet rs, int rowNum) throws SQLException {
		HeroSoul herosoul = new HeroSoul();
		herosoul.actorId = rs.getLong("actorId");
		herosoul.heroSoulIds = rs.getString("heroSoulIds");
		return herosoul;
	}

	@Override
	protected void hasReadEvent() {
		
		if(StringUtils.isNotBlank(this.heroSoulIds)) {
			this.heroSoulMap = StringUtils.delimiterString2IntMap(this.heroSoulIds);	
		}
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> value = new ArrayList<>();
		if (containsPK){
		    value.add(this.actorId);
		}
	    value.add(this.heroSoulIds);
		return value;
	}

	@Override
	protected void beforeWritingEvent() {
		
		this.heroSoulIds = StringUtils.map2DelimiterString(this.heroSoulMap, Splitable.ATTRIBUTE_SPLIT, Splitable.ELEMENT_DELIMITER);
		
	}
	
	public void reset() {
		this.heroSoulMap.clear();
	}

	@Override
	protected void disposeBlob() {
		heroSoulIds = EMPTY_STRING;
	}
}

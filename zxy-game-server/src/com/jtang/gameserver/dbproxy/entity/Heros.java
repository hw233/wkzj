package com.jtang.gameserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.jiatang.common.model.HeroVO;
import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.dataconfig.model.HeroConfig;
import com.jtang.gameserver.dataconfig.model.HeroUpgradeConfig;
import com.jtang.gameserver.dataconfig.service.HeroUpgradeService;

/**
 * 仙人列表
 * <pre>
 * --以下为db说明---------------------------
 * 说明:
 * >主键为actorId,每个角色有一行记录
 * </pre>
 * @author 0x737263
 *
 */
@TableName(name="heros", type = DBQueueType.IMPORTANT)
public class Heros extends Entity<Long> {
	private static final long serialVersionUID = 6966568884161859773L;

	/**
	 * 角色Id
	 */
	@Column(pk = true)
	private long actorId;
	
	/**
	 * 主力仙人Id
	 */
	@Column
	public int mainHeroId;
	
	/**
	 * 设置主力时间
	 */
	@Column
	public int mainHeroTime;
	
	/**
	 * 设置主力仙人的属性id
	 */
	@Column
	public byte attributeId;
	
	/**
	 * 仙人存储Blob字段
	 * <pre>
	 * 最大约300个仙人
	 * 格式：HeroVO对象|HeroVO对象|HeroVO对象
	 * {@code HeroVO}
	 * </pre>
	 */
	@Column
	private String heros;
	
	/**
	 * 已经获得的仙人id
	 * 格式heroId_heroId...
	 */
	@Column
	private String heroIds;
	
	/**
	 * 合成时间
	 */
	@Column
	public int composeTime;
	
	/**
	 * 合成次数
	 */
	@Column
	public int composeNum;
	
	/**
	 * 重置次数
	 */
	@Column
	public int resetNum;
	
	/**
	 * 重置时间
	 */
	@Column
	public int resetTime;
	
	/**
	 * 仙人列表
	 * <pre>
	 * 由heros字符串序列化而成
	 * </pre>
	 */
	private Map<Integer, HeroVO> heroMap = new ConcurrentHashMap<>();
	
	/**
	 * 已经获得的仙人列表
	 */
	public List<Integer> heroIdList = new ArrayList<>();
	
	@Override
	public Long getPkId() {
		return this.actorId;
	}

	@Override
	public void setPkId(Long id) {
		this.actorId = id;
	}
	
	/**
	 * 获取HeroVO map
	 * @return
	 */
	public Map<Integer, HeroVO> getHeroVOMap() {
		return this.heroMap;
	}
	
	/**
	 * 获取仙人VO对象
	 * @param heroId
	 * @return
	 */
	public HeroVO getHeroVO(int heroId) {
		Map<Integer,HeroVO> maps = getHeroVOMap();
		return maps.get(heroId);
	}
	
	/**
	 * 添加仙人
	 * @param hero
	 */
	public boolean addHeroVO(HeroVO hero) {
		if (this.getHeroVOMap().containsKey(hero.getHeroId())) {
			return false;
		}

		this.getHeroVOMap().put(hero.getHeroId(), hero);
		return true;
	}
	
	/**
	 * 移除仙人
	 * @param heroUid
	 * @return
	 */
	public boolean removeHeroVO(int heroId){
		HeroVO old = this.getHeroVOMap().remove(heroId);
		if (old != null) {
			return true;
		}
		return false;
	}
	
	/**
	 * 移除仙人
	 * @param heroIds
	 * @return
	 */
	public boolean removeHeroVO(List<Integer> heroIds) {
		List<HeroVO> removeHeros = new ArrayList<HeroVO>();
		for (Integer heroId : heroIds) {
			HeroVO old = this.getHeroVOMap().remove(heroId);
			if (old != null) {
				removeHeros.add(old);
			}
		}

		// 还原
		if (removeHeros.size() != heroIds.size()) {
			for (HeroVO vo : removeHeros) {
				this.getHeroVOMap().put(vo.heroId, vo);
			}
			return false;
		}
		return true;
	}
	
	/**
	 * 更新仙人
	 * @param hero
	 */
	public boolean updateHeroVO(HeroVO hero) {
		Map<Integer, HeroVO> heroMap = this.getHeroVOMap();
		if (heroMap.containsKey(hero.getHeroId())) {
//			heroMap.put(hero.getHeroId(), hero);
			return true;
		}
		return false;
	}
	
	/**
	 * 初始化仙人列表
	 * @param actorId
	 * @param conf
	 * @return
	 */
	public static Heros valueOf(long actorId, HeroConfig heroConfig) {
		Heros heros = new Heros();
		heros.actorId = actorId;
		heros.mainHeroId = 0;
		heros.mainHeroTime = 0;
		heros.attributeId = 0;
		heros.heros = "";
		heros.heroIds = "";
		heros.composeTime = 0;
		heros.composeNum = 0;
		heros.resetNum = 0;
		heros.resetTime = 0;
		
		if (heroConfig != null) {
			int atk = heroConfig.getAttack();
			byte atkScope = (byte) heroConfig.getAttackScope();
			int defense = heroConfig.getDefense();
			int hp = heroConfig.getHp();
			int skillId = heroConfig.getAttackSkillId();

			HeroUpgradeConfig upgradeConfig = HeroUpgradeService.get(1, heroConfig.getStar());
			int availableDelveCount = upgradeConfig.getUpgradeDelve();
			HeroVO hero = HeroVO.valueOf(atk, atkScope, defense, heroConfig.getHeroId(), hp, skillId, availableDelveCount);
			heros.addHeroVO(hero);
		}
		
		return heros;
	}
	
	public static HeroVO createHeroVO(HeroConfig heroConfig) {
		if (heroConfig != null) {
			int atk = heroConfig.getAttack();
			byte atkScope = (byte) heroConfig.getAttackScope();
			int defense = heroConfig.getDefense();
			int hp = heroConfig.getHp();
			int skillId = heroConfig.getAttackSkillId();

			HeroUpgradeConfig upgradeConfig = HeroUpgradeService.get(1, heroConfig.getStar());
			int availableDelveCount = upgradeConfig.getUpgradeDelve();
			HeroVO hero = HeroVO.valueOf(atk, atkScope, defense, heroConfig.getHeroId(), hp, skillId, availableDelveCount);
			return hero;
		}
		return null;
	}

	@Override
	protected Entity<Long> readData(ResultSet rs, int rowNum) throws SQLException {
		Heros heros = new Heros();
		heros.actorId = rs.getLong("actorId");
		heros.mainHeroId = rs.getInt("mainHeroId");
		heros.mainHeroTime = rs.getInt("mainHeroTime");
		heros.attributeId = rs.getByte("attributeId");
		heros.heros = rs.getString("heros");
		heros.heroIds = rs.getString("heroIds");
		heros.composeTime = rs.getInt("composeTime");
		heros.composeNum = rs.getInt("composeNum");
		heros.resetNum = rs.getInt("resetNum");
		heros.resetTime = rs.getInt("resetTime");
		return heros;
	}

	@Override
	protected void hasReadEvent() {
		
		if (StringUtils.isNotBlank(this.heros)) {
			List<String[]> heroList = StringUtils.delimiterString2Array(this.heros);
			for (String[] heroArray : heroList) {
				HeroVO hero = HeroVO.valueOf(heroArray);
				this.heroMap.put(hero.getHeroId(), hero);
			}
		}
		
		this.heroIdList = StringUtils.delimiterString2IntList(this.heroIds,Splitable.ATTRIBUTE_SPLIT);
		
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> value = new ArrayList<>();
		if (containsPK) {
			value.add(this.actorId);
		}
		value.add(this.mainHeroId);
		value.add(this.mainHeroTime);
		value.add(this.attributeId);
		value.add(this.heros);
		value.add(this.heroIds);
		value.add(this.composeTime);
		value.add(this.composeNum);
		value.add(this.resetNum);
		value.add(this.resetTime);
		return value;
	}

	@Override
	protected void beforeWritingEvent() {
		
		//将仙人列表转化成字符串(持久化)
		Map<Integer, HeroVO> heroMap = this.getHeroVOMap();
		List<String> heroList = new ArrayList<String>();
		for (HeroVO hero : heroMap.values()) {
			heroList.add(hero.parse2String());
		}
		this.heros = StringUtils.collection2SplitString(heroList, Splitable.ELEMENT_DELIMITER);
		
		this.heroIds = StringUtils.intArray2DelimiterString(heroIdList);
	}
	
	public void reset(HeroConfig heroConfig) {
		this.heroMap.clear();
		this.mainHeroId = 0;
		this.mainHeroTime = 0;
		this.attributeId = 0;
		this.composeTime = 0;
		this.composeNum = 0;
		if (heroConfig != null) {

			int atk = heroConfig.getAttack();
			byte atkScope = (byte) heroConfig.getAttackScope();
			int defense = heroConfig.getDefense();
			int hp = heroConfig.getHp();
			int skillId = heroConfig.getAttackSkillId();

			HeroUpgradeConfig upgradeConfig = HeroUpgradeService.get(1, heroConfig.getStar());
			int availableDelveCount = upgradeConfig.getUpgradeDelve();
			HeroVO hero = HeroVO.valueOf(atk, atkScope, defense, heroConfig.getHeroId(), hp, skillId, availableDelveCount);
			this.addHeroVO(hero);
		}
	}
	
	@Override
	protected void disposeBlob() {
		this.heros = EMPTY_STRING;
	}
	
}

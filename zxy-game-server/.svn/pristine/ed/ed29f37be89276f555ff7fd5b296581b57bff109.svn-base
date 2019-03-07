package com.jtang.gameserver.module.battle.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiatang.common.model.AttackerAttributeKey;
import com.jiatang.common.model.HeroVO;
import com.jtang.core.utility.CollectionUtils;
import com.jtang.gameserver.component.model.MonsterVO;
import com.jtang.gameserver.dataconfig.model.AwardGoodsConfig;
import com.jtang.gameserver.dataconfig.model.InbattleEffectConfig;
import com.jtang.gameserver.dataconfig.service.HeroService;
import com.jtang.gameserver.dataconfig.service.MonsterService;
import com.jtang.gameserver.module.battle.type.Camp;
import com.jtang.gameserver.module.battle.type.SpriteType;
import com.jtang.gameserver.module.buffer.helper.BufferHelper;
import com.jtang.gameserver.module.buffer.model.FighterBuffer;
import com.jtang.gameserver.module.buffer.type.CycleType;
import com.jtang.gameserver.module.buffer.type.ImmobilezeState;
/**
 * <pre>
 * 战斗角色的封装类, 该类存在的原因:
 * 1.游戏中无论是Hero还是Monster, 在参与战斗时他们都有类似的行为, 因此需要将他们封装以便对外提供统一的接口.
 * 2.仙人/怪物在参与战斗时可能会多增加一些属性,比如在战场的坐标，所处的阵型等, 这些属性在本类进行封装.
 * 3.仙人/怪物在战斗过程中,可能会被附加一些buffer,而这些buffer是临时性的，战斗结束需要移除,所以将这些buffer放在本类而不是底层的对象更容易处理，因为Fighter是只有在参与战斗才存在的一个实体.
 * @author vinceruan
 * 
 * </pre>
 *
 */
public class Fighter implements Comparable<Fighter> {
	private static final Logger LOGGER = LoggerFactory.getLogger(Fighter.class);

	// 阵营
	private Camp camp;

	// 生命值
	private int hp;

	// 攻击范围
	private int atkScope;

	// 在战场上的坐标
	private Tile tile;

	// 总掉血量
	private int totalHpLost = 0;

	// 名称
	private String name;

	// 战场上的唯一id
	private byte fighterId;

	// 攻击力
	private int atk;

	// 防御力
	private int defense;

	// 生命值上限
	private int hpMax;

	// 配置ID
	private int heroId;

	// 技能id
	private int skillId;

	// 普攻id
	private int commAtkSkillId;

	// 等级
	private int level;

	// 被动技能列表
	private List<Integer> passiveSkillList;

	// 身上的buffer
	private Map<AttackerAttributeKey, List<FighterBuffer>> buffers = new ConcurrentHashMap<AttackerAttributeKey, List<FighterBuffer>>();

	// 仙人技能触发几率的buffer.
	private Map<Integer, Integer> skillRateBuffers = new HashMap<Integer, Integer>();

	// 一些战斗的临时值
	private Map<String, Object> fightSessionAttributes = new HashMap<String, Object>();

	// 随身携带的物品,被击杀时有几率掉落
	public List<AwardGoodsConfig> dropGoodsConfig = new ArrayList<>();

	// 英雄所属的玩家,用于捡到物品时定位到具体的玩家
	public long actorId;

	// 种类(1:英雄 2:怪物)
	public SpriteType type;

	public int weaponId;// 武器ID
	public int weaponLevel;// 武器等级
	public int armorId;// 防具id
	public int armorLevel;// 防具等级
	public int ornamentsId;// 饰品id
	public int ornamentsLevel;// 饰品等级

	/**
	 * 移动次数
	 */
	public int moveNum = 1;
	
	/**
	 * 入场时是否是死的
	 */
	public boolean initIsDead = false;
	
	public byte direction = 1;
	
	/**
	 * 已经突破的次数
	 */
	public int breakThroughCount;
	/**
	 * 技能概率
	 */
	public Map<InbattleEffectConfig, Integer> skillRate = new HashMap<InbattleEffectConfig, Integer>();
	
	/**
	 * 构造函数
	 * @param sprite
	 * @param camp
	 * @param tile
	 */
	public Fighter(MonsterVO sprite, Camp camp, Tile tile, byte dire, byte fighterId) {
		this.camp = camp;
		this.hp = sprite.getHp();
		this.tile = tile;
		this.fighterId = fighterId;
		this.atkScope = sprite.getAtkScope();
		this.atk = sprite.getAtk();
		this.defense = sprite.getDefense();
		this.hpMax = sprite.getMaxHp();
		this.heroId = sprite.getHeroId();
		this.skillId = sprite.getSkillId();
		this.passiveSkillList = sprite.getPassiveSkillList();
		this.level = sprite.getLevel();
		this.name = MonsterService.get(sprite.getHeroId()).getMonsterName();
		this.actorId = -1;//不属于任何玩家
		this.dropGoodsConfig.addAll(sprite.getDropGoodsConfig());
		this.type = SpriteType.MONSTER;
		this.commAtkSkillId = MonsterService.get(heroId).getCommAtkSkillId();
		this.direction = dire;
	}
	
	public Fighter(MonsterVO sprite, Camp camp, Tile tile, Map<AttackerAttributeKey, Integer> attrChange, byte dire, byte fighterId) {
		this(sprite, camp, tile, dire, fighterId);
		processAttrChange(attrChange);
	}

	/**
	 * @param attrChange
	 */
	protected void processAttrChange(Map<AttackerAttributeKey, Integer> attrChange) {
		if (CollectionUtils.isEmpty(attrChange) ) {
			return;
		}
		for (Entry<AttackerAttributeKey, Integer> entry : attrChange.entrySet()) {
			AttackerAttributeKey key = entry.getKey();
			Integer value = entry.getValue();
			addAttrVal(key, value);
		}
		
		this.hp = Math.max(this.hp, this.hpMax);
		this.hpMax = this.hp;
	}

	/**
	 * @param key
	 * @param value
	 */
	protected void addAttrVal(AttackerAttributeKey key, Integer value) {
		switch (key) {
		case HP:
			this.hp += value;
			break;
		case ATTACK_SCOPE:
			this.atkScope += value;
			break;
		case ATK:
			this.atk += value;
			break;
		case DEFENSE:
			this.defense += value;
			break;
		case HP_MAX:
			this.hpMax += value;
			break;
		case WEAPON_ID:
			this.weaponId = value;
			break;
		case WEAPON_LEVEL:
			this.weaponLevel = value;
			break;
		case ARMOR_ID:
			this.armorId = value;
			break;
		case ARMOR_LEVEL:
			this.armorLevel = value;
			break;
		case  ORNAMENTS_ID:
			this.ornamentsId = value;
			break;
		case ORNAMENTS_LEVEL:
			this.ornamentsLevel = value;
			break;
		default:
			throw new IllegalArgumentException("暂时不支持的属性加成:"+key);
		}
	}
	
	/**
	 * 构造函数
	 * @param sprite
	 * @param camp
	 * @param tile
	 */
	private Fighter(HeroVO sprite, Camp camp, Tile tile, long actorId, byte dire, byte fighterId) {
		this.tile = tile;
		this.camp = camp;
		this.name = HeroService.get(sprite.getHeroId()).getHeroName();
		this.fighterId = fighterId;
		this.heroId = sprite.getHeroId();
		this.skillId = sprite.getSkillId();
		this.passiveSkillList = sprite.getPassiveSkillList();
		this.level = sprite.getLevel();
		this.hp = sprite.getHp();
		this.atkScope = sprite.getAtkScope();
		this.atk = sprite.getAtk();
		this.defense = sprite.getDefense();
		
		this.actorId = actorId;
		this.type = SpriteType.HERO;
		this.commAtkSkillId = HeroService.get(heroId).getCommAtkSkillId();
		this.direction = dire;
		this.breakThroughCount = sprite.breakThroughCount;
	}
	
	public Fighter(HeroVO sprite, Camp camp, Tile tile, Map<AttackerAttributeKey, Integer> attrChange, long actorId, boolean continueHP, byte dire, byte fighterId) {
		this(sprite, camp, tile, actorId, dire, fighterId);
		if (continueHP) {
			this.hpMax = sprite.getMaxHp();
			processAttrChangeContinueHP(attrChange);
		} else {
			this.hpMax = sprite.getHp();
			this.processAttrChange(attrChange);		
		}
		
	}
	
	private void processAttrChangeContinueHP(Map<AttackerAttributeKey, Integer> attrChange) {
		if (CollectionUtils.isEmpty(attrChange) == false) {
			for (Entry<AttackerAttributeKey, Integer> entry : attrChange.entrySet()) {
				AttackerAttributeKey key = entry.getKey();
				Integer value = entry.getValue();
				addAttrVal(key, value);
			}
		}
		
		initIsDead = this.hp <= 0 ? true :false;
		if (hp > this.hpMax) {
			this.hpMax = hp;
		}
	}
	
	/**
	 * 添加仙人技能触发几率的buffer
	 * @param skillEffectId
	 * @param rate
	 */
	public void addSkillRateBuffer(int skillEffectId, int rate) {
		this.skillRateBuffers.put(skillEffectId, rate);
	}
	
	/**
	 * 获取技能触发几率的加成值
	 * @param skillEffectId
	 * @return
	 */
	public int getSkillRateBuffer(int skillEffectId) {
		Integer rate = skillRateBuffers.get(skillEffectId);
		if (rate == null) {
			return 0;
		}
		return rate;
	}
	
	/**
	 * 添加buffer
	 * @param buffer
	 * @return 权重，属性buffer返回0
	 */
	public int addBuffer(FighterBuffer buffer) {
		addBuffer2Map(buffer, buffer.getAttr(), buffers);
		int result = 0;
		if (buffer.getAttr() == AttackerAttributeKey.IMMOBILIZE || buffer.getAttr() == AttackerAttributeKey.IN_FIGHTING) {
			List<FighterBuffer> list = this.buffers.get(buffer.getAttr());
			ImmobilezeState state = null;
			for (FighterBuffer fighterBuffer : list) {
				byte value = (byte) fighterBuffer.getAddVal();
				ImmobilezeState immobilezeState = ImmobilezeState.getByState(value);
				if (state == null) {
					state = immobilezeState;
				} else {
					if (state.getWeight() < immobilezeState.getWeight()) {
						state = immobilezeState;
					}
				}
			}
			result = state.getState();
		}
		return result;
	}
	
	
	/**
	 * 添加buffer到map
	 * @param buffer
	 * @param key
	 * @param map
	 */
	public void addBuffer2Map(FighterBuffer buffer, AttackerAttributeKey key, Map<AttackerAttributeKey, List<FighterBuffer>> map) {
		List<FighterBuffer> buffers = map.get(key);
		if (buffers == null) {
			buffers = new ArrayList<FighterBuffer>();
			this.buffers.put(key, buffers);
		}
		buffers.add(buffer);
	}
	
	public boolean isDead() {
		return this.hp <= 0;
	}
	
	/**
	 * 获取攻击范围(包括加成值)
	 * @return
	 */
	public int getAtkScope() {
		int atkScope = this.atkScope;
		atkScope += this.getAttrBuf(AttackerAttributeKey.ATTACK_SCOPE);
		return atkScope;
	}
	
	/**
	 * 获取属性加成值
	 * @param attrKey
	 * @return
	 */
	private int getAttrBuf(AttackerAttributeKey attrKey) {
		int bufferVal = 0;
		if (this.buffers.get(attrKey) != null) {
			for (FighterBuffer buffer : this.buffers.get(attrKey)) {
				if (!buffer.isTimeout()) {
					bufferVal += buffer.getAccumulateBufferVal();
				}
			}
		}
		return bufferVal;
	}
	
	public Camp getCamp() {
		return camp;
	}

	public void setCamp(Camp camp) {
		this.camp = camp;
	}
	
	public int getFightPower() {
		return getAtk() + getDefense() + getHp();
	}
	
	/**
	 * 获取攻击力(基础攻击力+加成)
	 * @return
	 */
	public int getAtk() {
		int value = this.atk+ this.getAttrBuf(AttackerAttributeKey.ATK);
		return value > 0 ? value : 0;
	}
	
	/**
	 * 获取攻击力(基础攻击)
	 * @return
	 */
	public int getBaseAtk() {
		return this.atk;
	}
	
	/**
	 * 获取防御值(基础防御值+加成)
	 * @return
	 */
	public int getDefense() {
		int value = this.defense + this.getAttrBuf(AttackerAttributeKey.DEFENSE);
		return value > 0 ? value : 0;
	}
	
	/**
	 * 获取防御值(基础防御值)
	 * @return
	 */
	public int getBaseDefense() {
		return this.defense;
	}
	
	/** 获取HP值 */
	public int getHp() {
		return this.hp;
	}
	
	public int getHpMax() {
		return this.hpMax + this.getAttrBuf(AttackerAttributeKey.HP_MAX);
	}
	
	public int getBaseHpMax() {
		return this.hpMax;
	}
	
	/** 增加伤害 */
	public void addHurt(int hurt) {
		this.hp -= hurt;
		this.totalHpLost += hurt;
		if (this.hp < 0 ){
			this.hp = 0;
		}
	}
	
	
	/** 回复生命值 */
	public void addHp(int hp) {
		this.hp += hp;
		int hpMax = getHpMax(); 
		if (this.hp > hpMax) {
			this.hp = hpMax;
		}
	}
	
	public int getExtraCommAtkTimes() {
		return this.getAttrBuf(AttackerAttributeKey.COMMON_ATTACK_TIMES);
	}

	public Tile getTile() {
		return tile;
	}

	public void setTile(Tile tile) {
		this.tile = tile;
	}
	
	/** 判断仙人是否已受伤 */
	public boolean isHurt() {
		return this.getHp() < this.getHpMax();
	}

	public Map<AttackerAttributeKey, List<FighterBuffer>> getBuffers() {
		return buffers;
	}
	
	public List<FighterBuffer> removeBufferExceptDieType(){
		List<FighterBuffer> removeBuffer = new ArrayList<>();
		Iterator<List<FighterBuffer>> it = buffers.values().iterator();
		while (it.hasNext()){
			List<FighterBuffer> list = it.next();
			Iterator<FighterBuffer> listIt = list.iterator();
			while(listIt.hasNext()){
				FighterBuffer next = listIt.next();
				if (next.getCycleType() == CycleType.DIE){
					removeBuffer.add(next);
					listIt.remove();
				}
			}
			if (list.size() == 0){
				it.remove();
			}
		}
		return removeBuffer;
	}

	public void setBuffers(Map<AttackerAttributeKey, List<FighterBuffer>> buffers) {
		this.buffers = buffers;
	}
	
	public int getAttrVal(AttackerAttributeKey key) {
		switch (key) {
		case HP:
			return getHp();
		case HP_MAX:
			return getHpMax();
		case ATK:
			return getAtk();
		case ATTACK_SCOPE:
			return getAtkScope();
		case DEFENSE:
			return getDefense();
		case IMMOBILIZE:
			return BufferHelper.isUnitInImmobilize(buffers);
		case COMMON_ATTACK_TIMES:
			return getExtraCommAtkTimes();
		case CAMP:
			return this.camp.getType();
		case HERT_CHANGE:
			return this.getAttrBuf(key);
		default:
			LOGGER.error("未知的类型"+key.getCode());
			return 0;
		}
	}

	@Override
	public int hashCode() {
		return Long.valueOf(fighterId).hashCode();
	}
	
	public byte getFighterId() {
		return this.fighterId;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Fighter)) {
			return false;
		}
		return this.getFighterId() == (((Fighter)obj).getFighterId());
	}

	public void setHp(int hp) {
		this.hp = hp;
	}
	
	public void addDefense(int def) {
		this.defense += def;
	}

	public Map<String, Object> getFightSessionAttributes() {
		return fightSessionAttributes;
	}

	public int getTotalHpLost() {
		return totalHpLost;
	}

	public void setTotalHpLost(int totalHpLost) {
		this.totalHpLost = totalHpLost;
	}

	public String getName() {
		return name+"&"+fighterId;
	}

	public int getHeroId() {
		return heroId;
	}

	public int getSkillId() {
		return skillId;
	}

	public List<Integer> getPassiveSkillList() {
		return passiveSkillList == null ? new ArrayList<Integer>() : passiveSkillList;
	}

	public int getLevel() {
		return level;
	}

	public List<AwardGoodsConfig> getDropGoodsConfig() {
		return dropGoodsConfig;
	}

	public int getCommAtkSkillId() {
		return commAtkSkillId;
	}
	
	public int getSpriteStar() {
		if (this.type == SpriteType.HERO) {
			return HeroService.get(this.heroId).getStar();
		} else {
			return MonsterService.get(heroId).getStar();
		}
	}
	
	public int getSpriteSex() {
		if (this.type == SpriteType.HERO) {
			return HeroService.get(this.heroId).getSex();
		} else {
			return MonsterService.get(heroId).getSex();
		}
	}
	
	/**
	 * 获取已有buffer数量
	 * @param effectId
	 * @param key
	 * @return
	 */
	public int hasBufferNum(int effectId, AttackerAttributeKey key) {
		int num = 0;
		Map<AttackerAttributeKey, List<FighterBuffer>> bufs = this.getBuffers();
		List<FighterBuffer> list = bufs.get(key);
		if (list == null) {
			return 0;
		}
		
		for (FighterBuffer fighterBuffer : list) {
			if (fighterBuffer.getEffectId() == effectId) {
				num += 1;
			}
		}
		return num;
	}

	public boolean isMark() {
		List<FighterBuffer> buf = this.buffers.get(AttackerAttributeKey.MARK);
		return buf != null && buf.size() > 0;
	}
	
	public int getHert() {
		if (initIsDead) {
			return 0;
		}
		return totalHpLost;
	}
	
	@Override
	public int compareTo(Fighter target) {
		if (this.equals(target)) {
			return 0;
		}
		Tile targetTile = target.getTile();
		Tile selfTile = this.getTile();
		int targetValue = targetTile.getX()* targetTile.getX() + targetTile.getY() * targetTile.getY();
		int selfValue = selfTile.getX() * selfTile.getX() + selfTile.getY() * selfTile.getY();
		if (selfValue > targetValue) {
			return 1;
		} else if (selfValue < targetValue) {
			return -1;
		} else {
			if (selfTile.getY()>=targetTile.getY()) {
				return 1;
			} else {
				return -1;
			}
		}
	}
}

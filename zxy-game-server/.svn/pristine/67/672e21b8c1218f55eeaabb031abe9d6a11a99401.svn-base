package com.jtang.gameserver.module.hero.helper;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.model.HeroVO;
import com.jiatang.common.model.HeroVOAttributeKey;
import com.jtang.core.result.ObjectReference;
import com.jtang.gameserver.module.buffer.facade.BufferFacade;
import com.jtang.gameserver.module.buffer.model.HeroBuffer;
import com.jtang.gameserver.module.hero.facade.HeroFacade;

@Component
public class HeroHelper {
	private static final ObjectReference<HeroHelper> ref = new ObjectReference<HeroHelper>();
	
	@Autowired
	HeroFacade heroFacade;
		
	@Autowired
	BufferFacade bufferFacade;
	
	@PostConstruct
	protected void init() {
		ref.set(this);
	}
	
	public static HeroHelper getInstance() {
		return ref.get();
	}
	
	public HeroVO getHero(long actorId, int heroId) {
		return heroFacade.getHero(actorId, heroId);
	}
	
	/**
	 * 获取仙人的属性-值列表
	 * @param actorId
	 * @param heroId
	 * @param attributeSet
	 * @return
	 */
	public Map<HeroVOAttributeKey, Object> getHeroAttributeVals(long actorId, int heroId, Set<HeroVOAttributeKey> attributeSet) {
		HeroBuffer buffer = this.bufferFacade.getHeroBuffer(actorId, heroId);
		HeroVO hero = this.heroFacade.getHero(actorId, heroId);
		Map<HeroVOAttributeKey, Object> resultMap = new HashMap<HeroVOAttributeKey, Object>();
		for (HeroVOAttributeKey key : attributeSet) {
			resultMap.put(key, getHeroAttributeVal(buffer, hero, key));
		}
		return resultMap;
	}

	/**
	 * 获取仙人的属性值
	 * @param buffer
	 * @param hero
	 * @param key
	 * @return
	 */
	public Object getHeroAttributeVal(HeroBuffer buffer, HeroVO hero, HeroVOAttributeKey key) {
		Object val = 0;
		switch (key) {
		case HP:
			val = hero.getHp();
			break;
		case EXP:
			val = hero.exp;
			break;
		case ATTACK_SCOPE:
			
			val = hero.getAtkScope();
			break;
		case ATK:
			val = hero.getAtk();
			break;
		case DEFENSE:
			val = hero.getDefense();
			break;
		case LEVEL:
			val = hero.getLevel();
			break;
		case AVAILABLE_DEVLE_COUNT:
			val = hero.availableDelveCount;
			break;
		case USED_DEVLE_COUNT:
			val = hero.usedDelveCount;
			break;
		case BREAK_THROUGH_COUNT:
			val = hero.breakThroughCount;
			break;
		case PASSIVE_SKILL:
			val = hero.getPassiveSkillList();
			break;
		case ALLOW_REDELVE:
			val = hero.allowReDelve();
			break;
		case SKILL_ID:
			val = hero.getSkillId();
			break;
		case COST_GOLD:
			val = hero.delveCostGold;
			break;
		case COST_STONE:
			val = hero.delveStoneNum;
			break;
		default:
			throw new RuntimeException("无法识别的属性");
		}
		return val;
	}
}

package com.jtang.gameserver.dataconfig.service;

import static com.jiatang.common.GameStatusCodeConstant.HERO_COMPOSE_STAR_DIFFERENT;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.core.result.TResult;
import com.jtang.gameserver.dataconfig.model.HeroConfig;
import com.jtang.gameserver.dataconfig.model.HeroSelectConfig;

/**
 * Hero配置服务读取类
 * @author 0x737263
 *
 */
@Component
public class HeroService extends ServiceAdapter {

	/**
	 * 仙人配置列表
	 */
	private static Map<Integer, HeroConfig> HERO_CONFIG_MAPS = new HashMap<>();
	
	/**
	 * 创建角色时首次选择的仙人列表
	 */
	private static List<Integer> HERO_SELECT_LIST = new ArrayList<>();

	@Override
	public void clear() {
		HERO_CONFIG_MAPS.clear();
		HERO_SELECT_LIST.clear();
	}
	
	@Override
	public void initialize() {
		List<HeroConfig> heroList = dataConfig.listAll(this, HeroConfig.class);
		for (HeroConfig hero : heroList) {
			HERO_CONFIG_MAPS.put(hero.getHeroId(), hero);
		}

		List<HeroSelectConfig> heroSelectList = dataConfig.listAll(this, HeroSelectConfig.class);
		for (HeroSelectConfig cfg : heroSelectList) {
			HERO_SELECT_LIST.add(cfg.getHeroId());
		}
	}

	/**
	 * 获取Hero
	 * @param heroId 仙人Id
	 * @return
	 */
	public static HeroConfig get(int heroId) {
		if (HERO_CONFIG_MAPS.containsKey(heroId)) {
			return HERO_CONFIG_MAPS.get(heroId);
		}
		return null;
	}

	/**
	 * 获取所有仙人对象
	 * 
	 * @return
	 */
	public static Collection<HeroConfig> getAll() {
		return HERO_CONFIG_MAPS.values();
	}
	
	/**
	 * 判断是否为配置中的仙人
	 * @param heroId
	 * @return
	 */
	public static boolean isSelectHeroId(int heroId) {
		return HERO_SELECT_LIST.contains(heroId);
	}
	
	/**
	 * 获取仙人相同的星级
	 * @param heroIdList
	 * @return
	 */
	public static TResult<Integer> getStar(List<Integer> heroIdList) {
		int lastStar = 0;
		for(Integer heroId : heroIdList) {
			HeroConfig config = get(heroId);
			if(config == null) {
				return TResult.valueOf(HERO_COMPOSE_STAR_DIFFERENT);
			}
			if(lastStar == 0) {
				lastStar = config.getStar();
				continue;
			}
			if(lastStar != config.getStar()) {
				return TResult.valueOf(HERO_COMPOSE_STAR_DIFFERENT);
			}
		}
		return TResult.sucess(lastStar);
	}
	
	/**
	 * 获取X星级以上的仙人id
	 */
	public static List<Integer> getAllHeroByStar(int star){
		List<Integer> list = new ArrayList<>();
		for(HeroConfig config : HERO_CONFIG_MAPS.values()){
			if(config.getStar() >= star){
				list.add(config.getHeroId());
			}
		}
		return list;
	}
	
}

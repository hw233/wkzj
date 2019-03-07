package com.jtang.gameserver.dataconfig.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.gameserver.dataconfig.model.GoodsComposeConfig;
import com.jtang.gameserver.dataconfig.model.GoodsConfig;

/**
 * 物品配置读取服务类
 * @author 0x737263
 *
 */
@Component
public class GoodsService extends ServiceAdapter {
	private static final Logger LOGGER = LoggerFactory.getLogger(GoodsService.class);

	/**
	 * 物品id列表
	 */
	private static Map<Integer, GoodsConfig> GOODS_MAPS = new HashMap<>(); 
	
	/**
	 * 物品分类列表
	 */
	private static Map<Integer, Map<Integer, List<GoodsConfig>>> GOODS_TYPE_MAPS = new HashMap<>();
	
	/**
	 * 碎片合成时间配置
	 */
	private static Map<Integer,GoodsComposeConfig> GOODS_COMPOSE_MAP = new HashMap<>();
	
	@Override
	public void clear() {
		GOODS_MAPS.clear();
		GOODS_TYPE_MAPS.clear();
		GOODS_COMPOSE_MAP.clear();
	}
	
	@Override
	public void initialize() {
		List<GoodsConfig> list = dataConfig.listAll(this, GoodsConfig.class);
		for (GoodsConfig cfg : list) {
			GOODS_MAPS.put(cfg.getGoodsId(), cfg);

			Map<Integer, List<GoodsConfig>> typeMap = GOODS_TYPE_MAPS.get(cfg.getGoodsType());
			if (typeMap == null) {
				typeMap = new HashMap<>();
			}
			List<GoodsConfig> subList = typeMap.get(cfg.getGoodsSubType());
			if (subList == null) {
				subList = new ArrayList<>();
			}
			subList.add(cfg);
			typeMap.put(cfg.getGoodsSubType(), subList);
			GOODS_TYPE_MAPS.put(cfg.getGoodsType(), typeMap);
		}
		
		List<GoodsComposeConfig> composeList = dataConfig.listAll(this, GoodsComposeConfig.class);
		for(GoodsComposeConfig config:composeList){
			GOODS_COMPOSE_MAP.put(config.star, config);
		}
	}
	
	/**
	 * 根据物品id获取物品对象
	 * @param goodsId
	 * @return
	 */
	public static GoodsConfig get(int goodsId) {
		GoodsConfig cfg = GOODS_MAPS.get(goodsId);
		if (cfg != null) {
			return cfg;
		}
		
		if (LOGGER.isWarnEnabled()) {
			LOGGER.warn(String.format("GoodsConfig缺少配置，goodsId: [%s]", goodsId));
		}
		return null;
	}
	
	/**
	 * 根据大类型获取物品列表
	 * @param goodsType
	 * @return
	 */
	public static List<GoodsConfig> getTypeList(int goodsType) {
		Map<Integer,List<GoodsConfig>> map = GOODS_TYPE_MAPS.get(goodsType);
		
		List<GoodsConfig> list = new ArrayList<>();
		for (Entry<Integer, List<GoodsConfig>> entry : map.entrySet()) {
			for(GoodsConfig cfg : entry.getValue()) {
				list.add(cfg);
			}
		}

		return list;
	}
	
	/**
	 * 根据物品分类获取列表
	 * @param goodsType		物品大类型
	 * @param goodsSubType	物品子类型
	 * @return
	 */
	public static List<GoodsConfig> getTypeList(int goodsType, int goodsSubType) {
		Map<Integer,List<GoodsConfig>> map = GOODS_TYPE_MAPS.get(goodsType);
		if(map == null) {
			LOGGER.warn(String.format("GoodsConfig找不到goodsType:[%s]", goodsType));
			return null;
		}
		return map.get(goodsSubType);
	}
	
	/**
	 * 获取星级列表
	 * @param goodsType			物品大类型
	 * @param goodsSubType		物品子类型
	 * @param star				星级
	 * @return
	 */
	public static List<GoodsConfig> getTypeList(int goodsType, int goodsSubType, int star) {
		List<GoodsConfig> goodsList = getTypeList(goodsType, goodsSubType);
		if (goodsList == null) {
			LOGGER.warn(String.format("GoodsConfig找不到goodsType:[%s] goodsSubType:[%s]", goodsType, goodsSubType));
			return null;
		}

		List<GoodsConfig> goodsStarList = new ArrayList<>();
		for (GoodsConfig cfg : goodsList) {
			if (cfg.getStar() == star) {
				goodsStarList.add(cfg);
			}
		}

		return goodsStarList;
	}

	/**
	 * 
	 * 获取所有物品配置
	 */
	public static Collection<GoodsConfig> getAllConfigList() {
		return GOODS_MAPS.values();
	}
	
	/**
	 * 获取装备碎片合成时间配置
	 */
	public static GoodsComposeConfig getComposeConfig(int star){
		return GOODS_COMPOSE_MAP.get(star);
	}
}

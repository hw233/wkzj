package com.jtang.gameserver.dataconfig.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.gameserver.dataconfig.model.GoodsUseConfig;

/**
 * 宝箱使用保底配置服务
 * @author ludd
 *
 */
@Component
public class UseGoodsService extends ServiceAdapter {

	private static Map<Integer, GoodsUseConfig> map = new HashMap<Integer, GoodsUseConfig>();
	
	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public void initialize() {
		List<GoodsUseConfig> list = dataConfig.listAll(this, GoodsUseConfig.class);
		for (GoodsUseConfig useGoodsConfig : list) {
			map.put(useGoodsConfig.useGoodsId, useGoodsConfig);
		}
		
	}
	
	/**
	 * 是否包含有使用记录的物品
	 * @param goodsId
	 * @return
	 */
	public static boolean hasGoods(int goodsId) {
		return map.containsKey(goodsId);
	}
	
	/**
	 * 获取保底配置
	 * @param goodsId
	 * @param useNum
	 * @return
	 */
	public static GoodsUseConfig get(int goodsId) {
		return map.get(goodsId);
	}

}

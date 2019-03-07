package com.jtang.gameserver.dataconfig.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 多个物品捆包掉落的配置
 * @author vinceruan
 *
 */
public class AwardGoodsPacketConfig {
	/**
	 * 概率
	 */
	public int rate;
	
	/**
	 * 概率命中后掉落的物品配置,格式:key=物品配置id,value=数量
	 */
	public Map<Integer, Integer> goodsMap = new HashMap<Integer, Integer>();
}

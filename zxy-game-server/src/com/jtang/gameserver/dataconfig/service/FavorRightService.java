package com.jtang.gameserver.dataconfig.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.core.utility.Splitable;
import com.jtang.gameserver.dataconfig.model.FavorRightConfig;
import com.jtang.gameserver.module.adventures.favor.type.FavorId;
@Component
public class FavorRightService  extends ServiceAdapter{
	/**
	 * key-favorRightConfig.id
	 */
	public static Map<Integer, FavorRightConfig> CONFIG_MAP = new HashMap<>();
	/**
	 * 特权1使用次数
	 */
	public static int RIGHT_1_USE_NUM = 0;
	/**
	 *特权2使用次数
	 */
	public static int RIGHT_2_USE_NUM = 0;
	/**
	 * 特权3使用次数
	 */
	public static int RIGHT_3_USE_NUM = 0;
	/**
	 * 特权3点券添加数量
	 */
	public static int ADD_TICKETS_NUM = 0;
	
	@Override
	public void clear() {
		CONFIG_MAP.clear();
	}
	
	@Override
	public void initialize() {
		List<FavorRightConfig> list = dataConfig.listAll(this, FavorRightConfig.class);
		for (FavorRightConfig favorConfig : list) {
			CONFIG_MAP.put(favorConfig.id, favorConfig);
			if (favorConfig.id == FavorId.ID1.getId()){
				RIGHT_1_USE_NUM = Integer.valueOf(favorConfig.parseValue);
			} else if (favorConfig.id == FavorId.ID2.getId()) {
				RIGHT_2_USE_NUM = Integer.valueOf(favorConfig.parseValue);
			} else if (favorConfig.id == FavorId.ID3.getId()) {
				String[] pasrseValues = favorConfig.parseValue.split(Splitable.ATTRIBUTE_SPLIT);
				ADD_TICKETS_NUM = Integer.valueOf(pasrseValues[0]);
				RIGHT_3_USE_NUM = Integer.valueOf(pasrseValues[1]);
			}
			
		}
	}
	
	public static FavorRightConfig getById(int id){
		return CONFIG_MAP.get(id);
	}
	public static FavorRightConfig getByParserId(int id){
		for (FavorRightConfig favorRightConfig : CONFIG_MAP.values()) {
			if (favorRightConfig.parseId == id){
				return favorRightConfig;
			}
		}
		return null;
	}
	
}

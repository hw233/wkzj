package com.jtang.gameserver.dataconfig.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.module.adventures.vipactivity.model.FailReward;

/**
 * 炼器宗师配置
 * @author 0x737263
 *
 */
@DataFile(fileName = "equipComposeConfig")
public class EquipComposeConfig implements ModelAdapter {

	/**
	 * 装备类型
	 */
	private int type;
	
	/**
	 * 合成的星级
	 */
	private int composeStar;
	
	/**
	 * 需要的装备数
	 */
	private int requireEquipNum;
	
	/**
	 * 随机基础百分比(0-100)
	 */
	private int basePercent;
	
	/**
	 * 使用点券的百分比(0-100)
	 */
	private int useTicketPercent;
	
	/**
	 * 消耗点券数
	 */
	private int consumeTicket;
	
	/**
	 * 随机的装备列表 格式:装备id_概率百分比|装备id_概率百分比|装备id_概率百分比
	 */
	private String equipList;
	
	/**
	 * 失败时送的物品
	 */
	private String failReward;
	@FieldIgnore
	public List<FailReward> list = new ArrayList<>();
	@FieldIgnore
	private Map<Integer,Integer> equipMaps = new HashMap<>();
	
	@Override
	public void initialize() {
		equipMaps = StringUtils.delimiterString2IntMap(equipList);
		List<String[]> items = StringUtils.delimiterString2Array(failReward);
		for (String[] item : items) {
			FailReward f = FailReward.valueOf(item);
			list.add(f);
		}
		
		this.equipList = null;
	}

	public int getType() {
		return type;
	}

	public int getComposeStar() {
		return composeStar;
	}

	public int getRequireEquipNum() {
		return requireEquipNum;
	}

	public int getBasePercent() {
		return basePercent;
	}
	public int getUseTicketPercent() {
		return useTicketPercent;
	}

	public int getConsumeTicket() {
		return consumeTicket;
	}
	
	public Map<Integer, Integer> getEquipMaps() {
		return equipMaps;
	}

}

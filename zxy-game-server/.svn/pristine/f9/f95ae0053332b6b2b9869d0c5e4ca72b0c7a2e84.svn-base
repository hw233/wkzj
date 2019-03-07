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
 * 仙人合成配置
 * @author 0x737263
 *
 */
@DataFile(fileName = "heroComposeConfig")
public class HeroComposeConfig implements ModelAdapter {

	/**
	 * 合成的星级
	 */
	private int composeStar;
	
	/**
	 * 需要的仙人数
	 */
	private int requireHeroNum;
	
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
	 * 随机的仙人列表 格式:仙人id_概率百分比|仙人id_概率百分比|仙人id_概率百分比
	 */
	private String heroList;
	
	/**
	 * 获得魂魄数量
	 */
	private int heroSoulNum;
	
	/**
	 * 失败时送的物品
	 */
	private String failReward;
	
	@FieldIgnore
	public List<FailReward> list = new ArrayList<>();
	
	@FieldIgnore
	private Map<Integer,Integer> heroMaps = new HashMap<>();
	
	@Override
	public void initialize() {
		heroMaps = StringUtils.delimiterString2IntMap(heroList);
		List<String[]> items = StringUtils.delimiterString2Array(failReward);
		for (String[] item : items) {
			FailReward f = FailReward.valueOf(item);
			list.add(f);
		}
		
		this.heroList = null;
	}


	public int getComposeStar() {
		return composeStar;
	}

	public int getRequireHeroNum() {
		return requireHeroNum;
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
	
	public Map<Integer, Integer> getHeroMaps() {
		return heroMaps;
	}
	
	public int getHeroSoulNum() {
		return heroSoulNum;
	}

}

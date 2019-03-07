package com.jtang.gameserver.dataconfig.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.utility.RandomUtils;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;

@DataFile(fileName = "treasureConfig")
public class TreasureConfig implements ModelAdapter {

	/**
	 * 格子横坐标
	 */
	public int gridX;

	/**
	 * 格子纵坐标
	 */
	public int gridY;

	/**
	 * 格子内容 type(type:0.物品、1.魂魄)_奖励id_数量_几率|...
	 */
	public String gridContent;

	/**
	 * 是否有怪物
	 * 0.没有 1.有
	 */
	public int monster;

	/**
	 * 大奖 type_id_数量_几率
	 */
	public String bigReward;

	@FieldIgnore
	private List<RewardConfig> gridContentList = new ArrayList<>();

	@FieldIgnore
	private RewardConfig treasureBigReward = null;

	@Override
	public void initialize() {
		parseGridContent();
		parseBigReward();
		
		this.gridContent = null;
		this.bigReward = null;
	}

	private void parseGridContent() {
		List<String[]> list = StringUtils.delimiterString2Array(gridContent);
		for (String[] array : list) {
			RewardConfig reward = new RewardConfig(Arrays.asList(array));
			gridContentList.add(reward);
		}
	}

	private void parseBigReward() {
		List<String> list = StringUtils.delimiterString2List(bigReward, Splitable.ATTRIBUTE_SPLIT);
		treasureBigReward = new RewardConfig(list);
	}

	/**
	 * 随机填充格子内容
	 * @return
	 */
	public RewardConfig getContent() {
		Map<Integer, Integer> map = new HashMap<>();
		for (int i = 0; i < gridContentList.size(); i++) {
			map.put(i, gridContentList.get(i).proportion);
		}
		Integer index = RandomUtils.randomHit(1000, map);
		if(index == null){
			return null;
		}
		return gridContentList.get(index);
	}

	
	public RewardConfig getBigReward() {
		return treasureBigReward;
	}
}

package com.jtang.gameserver.dataconfig.model;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.model.RewardObject;
import com.jtang.core.utility.StringUtils;
/**
 * 集众降魔结束奖励配置
 * @author ludd
 *
 */
@DataFile(fileName = "demonEndRewardConfig")
public class DemonEndRewardConfig implements ModelAdapter {

	
	/**
	 * 难度id
	 */
	private int difficultId;
	/**
	 * 功勋排名
	 */
	private int featsRank;
	/**
	 * 奖励物品
	 */
	private String rewardGoods;
	
	
	
	@FieldIgnore
	private List<RewardObject> rewardObjects = new ArrayList<>();
	
	@Override
	public void initialize() {
		rewardObjects.clear();
		List<String[]> list = StringUtils.delimiterString2Array(rewardGoods);
		for (String[] str : list) {
			RewardObject obj = RewardObject.valueOf(str);
			rewardObjects.add(obj);
		}
		
	}

	public int getDifficultId() {
		return difficultId;
	}

	public int getFeatsRank() {
		return featsRank;
	}


	public String getRewardGoods() {
		return rewardGoods;
	}

	public List<RewardObject> getRewardObjects() {
		List<RewardObject> list = new ArrayList<>();
		list.addAll(rewardObjects);
		return list;
	}
	
	
	
	



	
	

}

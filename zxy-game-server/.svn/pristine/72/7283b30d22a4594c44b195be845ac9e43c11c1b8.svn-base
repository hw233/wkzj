package com.jtang.gameserver.dataconfig.model;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.model.RewardObject;
import com.jtang.core.model.RewardType;
import com.jtang.core.utility.StringUtils;

/**
 * 抢夺奖励配置
 * @author 0x737263
 *
 */
@DataFile(fileName = "snatchRewardConfig")
public class SnatchRewardConfig implements ModelAdapter {
	
	private int snatchLevel;
	
	private int enemyFromLevel;
	
	private int enemyToLevel;
	
	private int rate;
	
	private int snatchTotalNum;
	
	private String rewardList; 
	
	@FieldIgnore
	private List<RewardObject> rewardObjectList = new ArrayList<>();
	
	@Override
	public void initialize() {
		List<String[]> itemList = StringUtils.delimiterString2Array(rewardList);
		for (String[] item : itemList) {
			if (item != null && item.length == 3) {
				RewardType rewardType = RewardType.getType(Integer.valueOf(item[0]));
				int id = Integer.valueOf(item[1]);
				int num = Integer.valueOf(item[2]);
				RewardObject rewardObject = new RewardObject(rewardType, id, num);
				rewardObjectList.add(rewardObject);
			}
		}
		this.rewardList = null;
	}

	/**
	 * 抢夺者的等级
	 * @return
	 */
	public int getSnatchLevel() {
		return snatchLevel;
	}
	
	/**
	 * 对方敌人开始等级
	 * @return
	 */
	public int getEnemyFromLevel() {
		return enemyFromLevel;
	}
	
	/**
	 * 对方敌人结束等级
	 * @return
	 */
	public int getEnemyToLevel() {
		return enemyToLevel;
	}

	/**
	 * 千分比概率1到1000
	 * @return
	 */
	public int getRate() {
		return rate;
	}

	/**
	 * 抢夺总数达到该值则不不予奖励
	 * @return
	 */
	public int getSnatchTotalNum() {
		return snatchTotalNum;
	}

	/**
	 * 奖励类型(0物品,1装备,2魂魄) _id_数量|奖励类型(0物品,1装备,2魂魄) _id_数量 
	 * @return
	 */
	public List<RewardObject> getRewardObjectList() {
		return rewardObjectList;
	}

}

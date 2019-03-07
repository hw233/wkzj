package com.jtang.gameserver.dataconfig.model;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.model.RewardObject;
import com.jtang.core.model.RewardType;

/**
 * 通天塔配置
 * @author 0x737263
 *
 */
@DataFile(fileName = "bableRankRewardConfig")
public class BableRankRewardConfig implements ModelAdapter {
	
	/**
	 * 登天塔类型
	 */
	public int bableType;
	
	/**
	 * 名次
	 */
	public int rank;
	
	/**
	 * 奖励类型
	 */
	public int rewardType;
	
	/**
	 * 奖励id
	 */
	public int rewardId;
	
	/**
	 * 奖励数量
	 */
	public int rewardNum;
	
	@FieldIgnore
	public List<RewardObject> rewardObjects = new ArrayList<>();
	
	@Override
	public void initialize() {
		RewardObject obj = new RewardObject(RewardType.getType(rewardType),rewardId,rewardNum);
		rewardObjects.add(obj);
		
	}
}

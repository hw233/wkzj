package com.jtang.gameserver.dataconfig.model;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.model.RewardObject;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
/**
 * 势力排行奖励配置
 * @author pengzy
 *
 */
@DataFile(fileName = "loveRankRewardConfig")
public class LoveRankRewardConfig  implements ModelAdapter{
	
	/**
	 * 排名
	 */
	public String rank;
	/**
	 * 奖励
	 */
	private String rewards;
	
	@FieldIgnore
	public List<RewardObject> rewardList = new ArrayList<>();
	@FieldIgnore
	public int startRank;
	@FieldIgnore
	public int endRank;
	
	@Override
	public void initialize() {
		List<Integer> rankList = StringUtils.delimiterString2IntList(rank, Splitable.ATTRIBUTE_SPLIT);
		startRank = rankList.get(0);
		endRank = rankList.get(1);
		
		List<String[]> list = StringUtils.delimiterString2Array(rewards);
		for (String[] item : list) {
			RewardObject rewardObject = RewardObject.valueOf(item);
			rewardList.add(rewardObject);
		}
		
	}
	
}

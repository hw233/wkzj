package com.jtang.gameserver.dataconfig.model;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.model.RewardObject;
import com.jtang.core.model.RewardType;
import com.jtang.core.utility.StringUtils;

@DataFile(fileName = "storyBattleRecordConfig")
public class StoryBattleRecordConfig implements ModelAdapter {
	
	/**
	 * 物品id
	 */
	public int battleId;
	/**
	 * 使用次数最小值
	 */
	public int numberOfUseMin;
	/**
	 * 使用次数最大值
	 */
	public int numberOfUseMax;
	/**
	 * 奖励物品
	 */
	private String rewardGoods;
	@FieldIgnore
	public List<RewardObject> rewardList = new ArrayList<>();
	
	@Override
	public void initialize() {
		if (StringUtils.isNotBlank(this.rewardGoods)) {
			List<String[]> list = StringUtils.delimiterString2Array(rewardGoods);
			for (String[] item : list) {
				RewardObject rewardObject = new RewardObject();
				rewardObject.rewardType = RewardType.getType(Integer.valueOf(item[0]));
				rewardObject.id = Integer.valueOf(item[1]);
				rewardObject.num = Integer.valueOf(item[2]);
				rewardList.add(rewardObject);
			}
			this.rewardGoods = null;
		}
	}

}

package com.jtang.gameserver.dataconfig.model;

import java.util.ArrayList;
import java.util.List;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.model.RewardObject;
import com.jtang.core.utility.StringUtils;

/**
 * <pre>
 * 故事配置:
 * 	id:		故事id
 *	order: 	通关顺序
 *	name:	故事名称
 * </pre>
 * @author vinceruan
 *
 */
@DataFile(fileName = "storyConfig")
public class StoryConfig implements ModelAdapter {
	/**
	 * id
	 */
	private int storyId;
	
	/**
	 * 名称
	 */
	private String name;
	
	/**
	 * 顺序
	 */
	private int order;
	
	private String crossReward;
	private String twoStarReward;
	private String threeStarRaward;
	
//	@FieldIgnore
//	private List<BattleConfig> battleList = new ArrayList<>();
	
	@FieldIgnore
	private List<RewardObject> crossRewardList = new ArrayList<>();
	@FieldIgnore
	private List<RewardObject> twoStarRewardList = new ArrayList<>();
	@FieldIgnore
	private List<RewardObject> threeStarRawardList = new ArrayList<>();
	
	@Override
	public void initialize() {
		List<String[]> list = StringUtils.delimiterString2Array(crossReward);
		for(String[] str:list){
			RewardObject rewardObject = RewardObject.valueOf(str);
			crossRewardList.add(rewardObject);
		}
		list = StringUtils.delimiterString2Array(twoStarReward);
		for(String[] str:list){
			RewardObject rewardObject = RewardObject.valueOf(str);
			twoStarRewardList.add(rewardObject);
		}
		list = StringUtils.delimiterString2Array(threeStarRaward);
		for(String[] str:list){
			RewardObject rewardObject = RewardObject.valueOf(str);
			threeStarRawardList.add(rewardObject);
		}
		
	}

	public int getStoryId() {
		return storyId;
	}

	public String getName() {
		return name;
	}

	public int getOrder() {
		return order;
	}

	public List<RewardObject> getCrossRewardList() {
		return crossRewardList;
	}
	
	public List<RewardObject> getTwoStarRewardList() {
		return twoStarRewardList;
	}
	
	public List<RewardObject> getThreeStarRawardList() {
		return threeStarRawardList;
	}
//	
//	public List<BattleConfig> getBattleList() {
//		return battleList;
//	}

}

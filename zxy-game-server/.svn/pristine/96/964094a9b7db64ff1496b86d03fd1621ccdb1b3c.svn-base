package com.jtang.gameserver.dataconfig.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.model.RandomRewardObject;
import com.jtang.core.model.RewardObject;
import com.jtang.core.utility.RandomUtils;
import com.jtang.core.utility.StringUtils;
@DataFile(fileName = "recruitLeastConfig")
public class RecruitLeastConfig implements ModelAdapter {

	/**
	 * 类型
	 */
	public int type;
	
	/**
	 * 保底次数
	 */
	public int num;
	
	/**
	 * 次数类型
	 */
	public int numType;
	
	/**
	 * 保底物品
	 * 类型_id_数量_几率|...
	 */
	private String goods;
	
	
	@FieldIgnore
	public List<RandomRewardObject> rewardList = new ArrayList<>();
	
	@Override
	public void initialize() {
		rewardList.clear();
		List<String[]> list = StringUtils.delimiterString2Array(goods);
		for(String[] str:list){
			RandomRewardObject obj = RandomRewardObject.valueOf(str);
			rewardList.add(obj);
		}
		this.goods = null;
	}
	
	public RewardObject get() {
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		for (int i = 0; i < rewardList.size(); i++) {
			RandomRewardObject obj = rewardList.get(i);
			map.put(i, obj.rate);
		}
		Integer index = RandomUtils.randomHit(1000, map);
		if(index == null){
			return null;
		}
		return rewardList.get(index);
	}
	
	
}

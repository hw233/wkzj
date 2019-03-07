package com.jtang.gameserver.dataconfig.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.utility.RandomUtils;
import com.jtang.core.utility.StringUtils;

@DataFile(fileName="traderLeastConfig")
public class TraderLeastConfig implements ModelAdapter {

	/**
	 * id
	 */
	public int id;
	
	/**
	 * 刷新次数
	 */
	public int flushNum;
	
	/**
	 * 保底物品
	 */
	private String reward;
	
	
	@FieldIgnore
	private List<RandomReward> rewardList = new ArrayList<>();
	
	@Override
	public void initialize() {
		List<String[]> list = StringUtils.delimiterString2Array(reward);
		for(String[] str:list){
			RandomReward randomReward = new RandomReward(str);
			rewardList.add(randomReward);
		}
		reward = null;
	}
	
	public Map<Integer,Integer> getReward(){
		Map<Integer,Integer> rateMap = new HashMap<>();
		for(int i = 0;i<rewardList.size();i++){
			RandomReward randomReward = rewardList.get(i);
			rateMap.put(i, randomReward.rate);
		}
		Integer index = RandomUtils.randomHit(1000, rateMap);
		RandomReward randomReward = rewardList.get(index);
		Map<Integer,Integer> map = new HashMap<>();
		map.put(randomReward.id, randomReward.discountId);
		return map;
	}
	
	
	class RandomReward{
		
		/**
		 * 商品id
		 */
		public int id;
		
		/**
		 * 打折id
		 */
		public int discountId;
		
		/**
		 * 几率
		 */
		public int rate;

		public RandomReward(String[] str) {
			this.id = Integer.valueOf(str[0]);
			this.discountId = Integer.valueOf(str[1]);
			this.rate = Integer.valueOf(str[2]);
		}
		
	}
}
